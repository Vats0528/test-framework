package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprints 6, 6 bis & 6 ter - Verbes HTTP et paramètres d'URL dynamiques
 *
 * - @GetMapping / @PostMapping / @PutMapping / @DeleteMapping sur la même URL
 * - paramètres dans le chemin : /user/{id}
 * - une même URL avec un autre verbe HTTP ne matche pas (404)
 */
@DisplayName("Sprint 6 - Verbes HTTP et URLs dynamiques")
class Sprint06HttpMethodsTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("GET /user/{id} : le paramètre d'URL est injecté et converti en int")
    void getWithPathParameter() {
        HttpTest.Response response = client.get("/user/42");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Utilisateur récupéré : ID=42"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("POST /user/{id} : paramètre d'URL + paramètre de formulaire")
    void postWithPathParameterAndFormParameter() {
        HttpTest.Response response = client.postForm("/user/7", HttpTest.form("nom", "Alice"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Utilisateur créé : ID=7, Nom=Alice"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("PUT /user/{id} : les paramètres du corps sont lus")
    void putReadsBodyParameters() {
        HttpTest.Response response = client.putForm("/user/9", HttpTest.form("nom", "Bob"));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Utilisateur modifié : ID=9, Nouveau nom=Bob"),
                "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("DELETE /user/{id}")
    void deleteWithPathParameter() {
        HttpTest.Response response = client.delete("/user/13");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Utilisateur supprimé : ID=13"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Paramètre d'URL de type String : /zavatra/{valeur}")
    void stringPathParameter() {
        HttpTest.Response response = client.get("/zavatra/salama");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Valeur reçue : salama"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Mauvais verbe HTTP sur une URL connue -> 404")
    void wrongHttpMethodDoesNotMatch() {
        // /custom n'est mappée qu'en GET (@RequestMapping method = GET)
        HttpTest.Response response = client.postForm("/custom", HttpTest.form());

        assertEquals(404, response.status());
    }
}
