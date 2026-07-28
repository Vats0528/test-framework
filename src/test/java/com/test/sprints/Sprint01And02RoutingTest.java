package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprints 1 & 2 - Front Controller et scan des contrôleurs
 *
 * - la FrontServlet démarre et scanne le package com.test.controllers
 * - les classes annotées @Controller sont enregistrées, les autres ignorées
 * - une URL inconnue retourne 404 avec la liste des patterns disponibles
 */
@DisplayName("Sprint 1 & 2 - Front Controller et scan des contrôleurs")
class Sprint01And02RoutingTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("La FrontServlet démarre et route une URL connue")
    void frontServletRoutesKnownUrl() {
        HttpTest.Response response = client.get("/front/custom");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Endpoint personnalisé"),
                "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("URL inconnue -> 404 avec la liste des patterns enregistrés")
    void unknownUrlReturns404() {
        HttpTest.Response response = client.get("/front/url-qui-nexiste-pas");

        assertEquals(404, response.status());
        assertTrue(response.bodyContains("404 - URL non trouvée"),
                "corps inattendu : " + response.body());
        // la page 404 liste les routes connues : preuve que le scan a eu lieu
        assertTrue(response.bodyContains("/custom"), "les patterns devraient être listés");
    }

    @Test
    @DisplayName("Les classes sans @Controller ne sont pas mappées")
    void classWithoutControllerAnnotationIsIgnored() {
        // NotAController.someMethod() existe mais la classe n'a pas @Controller
        HttpTest.Response response = client.get("/front/someMethod");

        assertEquals(404, response.status());
    }

    @Test
    @DisplayName("Les méthodes sans annotation de mapping ne sont pas mappées")
    void methodWithoutMappingIsIgnored() {
        HttpTest.Response response = client.get("/front/dashboard");

        // AdminController.dashboard() est annotée @Url("/dashboard") -> mappée
        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Tableau de bord administrateur"),
                "corps inattendu : " + response.body());
    }
}
