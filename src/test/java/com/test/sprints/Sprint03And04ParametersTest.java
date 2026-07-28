package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprints 3 & 4 - Injection des paramètres de requête
 *
 * - @RequestParam sur la query string (GET)
 * - @RequestParam sur le corps d'un POST (application/x-www-form-urlencoded)
 * - conversion automatique String -> int
 */
@DisplayName("Sprint 3 & 4 - Paramètres de requête et @RequestParam")
class Sprint03And04ParametersTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("@RequestParam lit la query string en GET")
    void requestParamFromQueryString() {
        HttpTest.Response response = client.get("/front/pageWithParams?titre=Titre+de+test&message=Message+de+test");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Titre de test"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Message de test"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Paramètre absent -> valeur nulle gérée par le contrôleur")
    void missingParameterFallsBackToDefault() {
        HttpTest.Response response = client.get("/front/pageWithParams");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Bienvenue"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@RequestParam lit le corps d'un POST et convertit en int")
    void requestParamFromPostBodyWithConversion() {
        HttpTest.Response response = client.postForm("/calcul",
                HttpTest.form("nombre1", "12", "nombre2", "30"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("12 + 30 = 42"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Les paramètres de la query string sont aussi lus en POST")
    void postAlsoReadsQueryString() {
        HttpTest.Response response = client.postForm("/calcul?nombre1=5&nombre2=7",
                HttpTest.form());

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("5 + 7 = 12"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Paramètre primitif absent -> valeur par défaut, pas d'erreur 500")
    void missingPrimitiveParameterUsesDefaultValue() {
        HttpTest.Response response = client.postForm("/calcul", HttpTest.form("nombre1", "5"));

        assertEquals(200, response.status(), "un int absent ne doit pas provoquer une 500");
        assertTrue(response.bodyContains("5 + 0 = 5"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Valeur non numérique pour un int -> valeur par défaut, pas d'erreur 500")
    void invalidNumberUsesDefaultValue() {
        HttpTest.Response response = client.postForm("/calcul",
                HttpTest.form("nombre1", "abc", "nombre2", "3"));

        assertEquals(200, response.status(), "une valeur invalide ne doit pas provoquer une 500");
        assertTrue(response.bodyContains("0 + 3 = 3"), "corps inattendu : " + response.body());
    }
}
