package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 9 - API REST et réponses JSON
 *
 * - @Get / @Json / @API déclenchent la sérialisation JSON
 * - le Content-Type devient application/json
 * - une List est enveloppée avec "count", un retour null donne une erreur 404 JSON
 */
@DisplayName("Sprint 9 - API REST (JSON)")
class Sprint09RestApiTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("@Get : liste sérialisée en JSON avec status, code et count")
    void listIsSerializedToJson() {
        HttpTest.Response response = client.get("/api/employees");

        assertEquals(200, response.status());
        assertTrue(response.contentType().contains("application/json"),
                "Content-Type inattendu : " + response.contentType());
        assertTrue(response.bodyContains("\"status\":\"success\""), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("\"code\":200"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("\"count\":3"), "une List doit exposer count : " + response.body());
        assertTrue(response.bodyContains("John Doe"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("Jane Smith"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@Json sur @GetMapping avec paramètre d'URL dynamique")
    void singleObjectAsJson() {
        HttpTest.Response response = client.get("/api/employees/2");

        assertEquals(200, response.status());
        assertTrue(response.contentType().contains("application/json"),
                "Content-Type inattendu : " + response.contentType());
        assertTrue(response.bodyContains("Jane Smith"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("\"salary\":60000.0"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Retour null -> enveloppe d'erreur JSON code 404")
    void nullResultProducesJsonError() {
        HttpTest.Response response = client.get("/api/employees/999");

        assertTrue(response.contentType().contains("application/json"),
                "Content-Type inattendu : " + response.contentType());
        assertTrue(response.bodyContains("\"status\":\"error\""), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("\"code\":404"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@API : filtrage via query string, JSON avec count")
    void filteredListAsJson() {
        HttpTest.Response response = client.get("/api/search?department=IT");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("\"count\":2"), "2 employés IT attendus : " + response.body());
        assertTrue(response.bodyContains("Bob Martin"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Retour String sérialisé en JSON")
    void stringResultAsJson() {
        HttpTest.Response response = client.get("/api/ping");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("\"data\":\"pong\""), "corps inattendu : " + response.body());
    }
}
