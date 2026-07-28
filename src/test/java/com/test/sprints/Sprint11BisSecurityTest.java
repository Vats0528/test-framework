package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 11 bis - Sécurité et contrôle d'accès
 *
 * - @Authenticated : 401 si aucun utilisateur en session
 * - @RequiresRole("X") : 401 si non connecté, 403 si rôle insuffisant
 * - @AllowAnonymous : toujours accessible
 * - la vérification a lieu AVANT l'invocation de la méthode
 */
@DisplayName("Sprint 11 bis - Sécurité (401 / 403)")
class Sprint11BisSecurityTest {

    private static HttpTest loggedInAs(String username, String password) {
        HttpTest client = HttpTest.newClient();
        HttpTest.Response login = client.get("/session/login?username=" + username + "&password=" + password);
        assertEquals(200, login.status(), "la connexion devrait réussir");
        return client;
    }

    @Test
    @DisplayName("@AllowAnonymous : accessible sans connexion")
    void anonymousCanAccessPublicPage() {
        HttpTest.Response response = HttpTest.newClient().get("/session/public");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Page publique"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@Authenticated sans connexion -> 401")
    void anonymousGets401OnAuthenticatedEndpoint() {
        HttpTest.Response response = HttpTest.newClient().get("/session/dashboard");

        assertEquals(401, response.status());
        assertTrue(response.bodyContains("401"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("@Authenticated connecté -> 200 et objet Auth injecté")
    void authenticatedUserReachesDashboard() {
        HttpTest.Response response = loggedInAs("frank", "secret").get("/session/dashboard");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Bienvenue sur votre tableau de bord, frank"),
                "l'objet Auth devrait être injecté : " + response.body());
    }

    @Test
    @DisplayName("@RequiresRole sans connexion -> 401")
    void anonymousGets401OnRoleProtectedEndpoint() {
        HttpTest.Response response = HttpTest.newClient().get("/session/admin");

        assertEquals(401, response.status());
    }

    @Test
    @DisplayName("@RequiresRole(\"ADMIN\") avec rôle USER -> 403 et méthode non exécutée")
    void userRoleGets403OnAdminEndpoint() {
        HttpTest.Response response = loggedInAs("grace", "secret").get("/session/admin");

        assertEquals(403, response.status());
        assertTrue(response.bodyContains("403"), "corps inattendu : " + response.body());
        assertFalse(response.bodyContains("Panneau d'administration"),
                "la méthode ne doit PAS être exécutée : " + response.body());
    }

    @Test
    @DisplayName("@RequiresRole(\"ADMIN\") avec rôle ADMIN -> 200")
    void adminRoleReachesAdminEndpoint() {
        HttpTest.Response response = loggedInAs("admin", "admin123").get("/session/admin");

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Panneau d'administration"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Un ADMIN n'a pas pour autant le rôle MODERATOR -> 403")
    void adminIsNotModerator() {
        HttpTest.Response response = loggedInAs("admin", "admin123").get("/session/moderate");

        assertEquals(403, response.status());
        assertFalse(response.bodyContains("Panneau de modération"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Après déconnexion, les pages protégées redonnent 401")
    void logoutRevokesAccess() {
        HttpTest client = loggedInAs("admin", "admin123");
        assertEquals(200, client.get("/session/admin").status());

        client.get("/session/logout");

        assertEquals(401, client.get("/session/admin").status());
        assertEquals(401, client.get("/session/dashboard").status());
    }

    @Test
    @DisplayName("Les endpoints sans annotation de sécurité restent publics")
    void endpointsWithoutSecurityAnnotationStayPublic() {
        HttpTest client = HttpTest.newClient();

        assertEquals(200, client.get("/session/info").status());
        assertEquals(200, client.get("/front/custom").status());
    }
}
