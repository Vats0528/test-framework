package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cohérence de la webapp : les pages de test doivent pointer vers des URLs
 * réellement mappées (contrôleur + web.xml).
 *
 * Ce test attrape le cas où un formulaire poste vers une URL qui n'existe pas :
 * les tests par sprint appellent les endpoints directement et ne verraient pas
 * une page HTML/JSP dont l'action est erronée.
 */
@DisplayName("Webapp - cohérence des pages de test et des URLs")
class WebappLinksTest {

    private static final Path WEBAPP = Path.of("src/main/webapp");

    private final HttpTest client = HttpTest.newClient();

    @ParameterizedTest(name = "la page {0} est servie")
    @ValueSource(strings = {
            "/index.html",
            "/index-sprint8.html",
            "/sprint3-calcul.html",
            "/sprint6-methods.html",
            "/sprint8.html",
            "/sprint8-index.jsp",
            "/sprint11-index.jsp",
            "/upload.html",
            "/test.jsp"
    })
    @DisplayName("Les pages de test se chargent sans erreur")
    void testPagesAreServed(String page) {
        HttpTest.Response response = client.get(page);

        assertEquals(200, response.status(), "page en erreur : " + page);
        assertFalse(response.bodyContains("org.apache.jasper"),
                "erreur de compilation JSP dans " + page + " : " + response.body());
    }

    @Test
    @DisplayName("Chaque action de formulaire des pages correspond à une URL mappée")
    void everyFormActionResolvesToAMappedUrl() throws IOException {
        Set<Form> forms = collectForms();
        assertFalse(forms.isEmpty(), "aucun formulaire trouvé dans la webapp");

        for (Form form : forms) {
            // requête vide avec le verbe déclaré par le formulaire :
            // on vérifie seulement que l'URL est routée (pas de 404)
            HttpTest.Response response = "GET".equals(form.method())
                    ? client.get(form.action())
                    : client.postForm(form.action(), HttpTest.form());

            assertTrue(response.status() != 404,
                    "formulaire [" + form.method() + "] pointant vers une URL non mappée : "
                            + form.action());
        }
    }

    @Test
    @DisplayName("Tous les liens de la page d'accueil répondent (aucun lien mort)")
    void everyLinkOnHomePageResolves() throws IOException {
        String home = Files.readString(WEBAPP.resolve("index.html"), StandardCharsets.UTF_8);

        // capture aussi un éventuel data-expects="404" (lien de démonstration du 404)
        Matcher matcher = Pattern.compile("href=\"([^\"]+)\"(\\s+data-expects=\"(\\d+)\")?")
                .matcher(home);
        int checked = 0;

        while (matcher.find()) {
            String href = matcher.group(1);
            if (href.startsWith("#") || href.startsWith("http")) {
                continue;
            }
            // liens relatifs à la racine du contexte : "front/page" -> "/front/page"
            String path = href.startsWith("/") ? href : "/" + href;
            String expected = matcher.group(3);

            HttpTest.Response response = client.get(path);

            if (expected != null) {
                // lien volontairement en erreur (démo de la page 404 du framework)
                assertEquals(Integer.parseInt(expected), response.status(),
                        "le lien " + href + " devrait répondre " + expected);
            } else {
                // 401/403 restent valides : ce sont les liens de sécurité
                assertTrue(response.status() != 404,
                        "lien mort sur la page d'accueil : " + href);
            }
            assertFalse(response.bodyContains("org.apache.jasper"),
                    "erreur JSP derrière le lien " + href + " : " + response.body());
            checked++;
        }

        assertTrue(checked >= 25,
                "la page d'accueil devrait lister les pages de tous les sprints, "
                        + checked + " liens seulement");
    }

    @Test
    @DisplayName("La page d'accueil référence chaque sprint de 1 à 11")
    void homePageListsEverySprint() throws IOException {
        String home = Files.readString(WEBAPP.resolve("index.html"), StandardCharsets.UTF_8);

        for (String sprint : List.of(
                "Sprint 1", "Sprint 3", "Sprint 5", "Sprint 6",
                "Sprint 7", "Sprint 8", "Sprint 9", "Sprint 10", "Sprint 11")) {
            assertTrue(home.contains(sprint), "sprint absent de la page d'accueil : " + sprint);
        }
    }

    @Test
    @DisplayName("Aucune page ne référence l'ancien préfixe /sprint8/")
    void noPageUsesObsoleteSprint8Prefix() throws IOException {
        for (Path page : webappPages()) {
            String content = Files.readString(page, StandardCharsets.UTF_8);
            assertFalse(content.contains("/sprint8/save"),
                    page.getFileName() + " utilise le préfixe obsolète /sprint8/ "
                            + "(les endpoints sont mappés sur /saveXxx)");
        }
    }

    // === helpers ===

    private static List<Path> webappPages() throws IOException {
        try (Stream<Path> files = Files.walk(WEBAPP)) {
            return files.filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        return name.endsWith(".jsp") || name.endsWith(".html");
                    })
                    .toList();
        }
    }

    /** Un formulaire de la webapp : verbe HTTP + URL relative au contexte */
    private record Form(String method, String action) {
    }

    /**
     * Extrait les balises <form> des pages avec leur méthode, et normalise les
     * actions en chemin relatif au contexte (le contexte est ajouté par le
     * client de test).
     */
    private static Set<Form> collectForms() throws IOException {
        Pattern formPattern = Pattern.compile("<form\\b([^>]*)>", Pattern.CASE_INSENSITIVE);
        Pattern actionPattern = Pattern.compile("action=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Pattern methodPattern = Pattern.compile("method=\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Set<Form> forms = new LinkedHashSet<>();

        for (Path page : webappPages()) {
            Matcher formMatcher = formPattern.matcher(Files.readString(page, StandardCharsets.UTF_8));
            while (formMatcher.find()) {
                String attributes = formMatcher.group(1);

                Matcher actionMatcher = actionPattern.matcher(attributes);
                if (!actionMatcher.find()) {
                    continue;
                }
                String action = actionMatcher.group(1)
                        .replace("${pageContext.request.contextPath}", "")
                        .replace("<%= request.getContextPath() %>", "")
                        .replace("/test-project", "");
                if (!action.startsWith("/") || action.startsWith("//")) {
                    continue;
                }

                Matcher methodMatcher = methodPattern.matcher(attributes);
                String method = methodMatcher.find()
                        ? methodMatcher.group(1).toUpperCase()
                        : "GET"; // valeur par défaut HTML

                forms.add(new Form(method, action));
            }
        }
        return forms;
    }
}
