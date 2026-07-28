package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 7 - @RequestMapping générique
 *
 * @RequestMapping(value = "...", method = HttpMethod.X) doit être équivalent
 * aux annotations spécialisées, et cohabiter avec elles.
 */
@DisplayName("Sprint 7 - @RequestMapping générique")
class Sprint07RequestMappingTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("@RequestMapping(method = GET) répond en GET")
    void requestMappingWithGetMethod() {
        HttpTest.Response response = client.get("/custom");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Endpoint personnalisé avec @RequestMapping"),
                "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@RequestMapping et @GetMapping cohabitent dans la même application")
    void requestMappingCoexistsWithSpecializedAnnotations() {
        assertEquals(200, client.get("/custom").status());          // @RequestMapping
        assertEquals(200, client.get("/front/page").status());      // @GetMapping
        assertEquals(200, client.get("/page5").status());           // @Url
        assertEquals(200, client.get("/front/dashboard").status()); // @Url
    }
}
