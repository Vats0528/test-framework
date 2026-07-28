package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 5 - ModelView et rendu des vues JSP
 *
 * - le ModelView retourné est forwardé vers la JSP correspondante
 * - les attributs du ModelView sont exposés à la vue (request attributes)
 * - une méthode retournant String est rendue directement en HTML
 */
@DisplayName("Sprint 5 - ModelView et vues JSP")
class Sprint05ModelViewTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("ModelView -> forward vers la JSP avec ses attributs")
    void modelViewForwardsToJspWithAttributes() {
        HttpTest.Response response = client.get("/front/page");

        assertEquals(200, response.status());
        // test.jsp affiche ${titre} et ${message}
        assertTrue(response.bodyContains("<title>Test JSP</title>"),
                "la JSP devrait être rendue : " + response.body());
        assertTrue(response.bodyContains("Bienvenue"), "attribut 'titre' absent : " + response.body());
        assertTrue(response.bodyContains("Default message"), "attribut 'message' absent : " + response.body());
    }

    @Test
    @DisplayName("Le nom de vue est complété avec .jsp automatiquement")
    void viewNameIsCompletedWithJspExtension() {
        // SessionController retourne new ModelView("sprint11-result") (sans .jsp)
        HttpTest.Response response = client.get("/session/public");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Page publique"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("L'ancienne annotation @Url reste supportée")
    void legacyUrlAnnotationStillWorks() {
        HttpTest.Response response = client.get("/page5");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Sprint 5"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Un retour String est rendu directement en HTML")
    void stringReturnIsRenderedAsHtml() {
        HttpTest.Response response = client.get("/front/custom");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Résultat :"), "corps inattendu : " + response.body());
    }
}
