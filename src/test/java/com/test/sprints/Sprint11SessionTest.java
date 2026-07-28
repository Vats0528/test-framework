package com.test.sprints;

import com.test.support.HttpTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 11 - Gestion des sessions
 *
 * - @Session CustomSession : injection de la session complète
 * - @Session String / @Session("cle") : injection d'une valeur de session
 * - set / get / clear et isolation entre deux clients
 */
@DisplayName("Sprint 11 - Sessions")
class Sprint11SessionTest {

    @Test
    @DisplayName("@Session CustomSession : écriture puis relecture entre deux requêtes")
    void sessionValuesSurviveAcrossRequests() {
        HttpTest client = HttpTest.newClient();

        HttpTest.Response login = client.get("/session/login?username=alice&password=secret");
        assertEquals(200, login.status());
        assertTrue(login.bodyContains("Connexion réussie"), "corps inattendu : " + login.body());

        // requête suivante : la valeur vient de la session, pas de la query string
        HttpTest.Response profile = client.get("/session/profile");
        assertEquals(200, profile.status());
        assertTrue(profile.bodyContains("Profil de alice"), "corps inattendu : " + profile.body());
    }

    @Test
    @DisplayName("@Session(\"cle\") avec nom personnalisé et conversion de type")
    void sessionValueWithCustomKey() {
        HttpTest client = HttpTest.newClient();
        client.get("/session/login?username=bob&password=secret");

        // profile(@Session String username, @Session("loginTime") Long loginTime)
        HttpTest.Response profile = client.get("/session/profile");

        assertEquals(200, profile.status());
        assertTrue(profile.bodyContains("Durée de la session"),
                "loginTime devrait être injecté depuis la session : " + profile.body());
        assertTrue(profile.bodyContains("secondes"), "corps inattendu : " + profile.body());
    }

    @Test
    @DisplayName("Session non initialisée : les valeurs injectées sont nulles")
    void noSessionValuesForNewClient() {
        HttpTest client = HttpTest.newClient();

        HttpTest.Response profile = client.get("/session/profile");

        assertEquals(200, profile.status());
        assertTrue(profile.bodyContains("Vous n'êtes pas connecté"), "corps inattendu : " + profile.body());
    }

    @Test
    @DisplayName("CustomSession expose l'id et l'ensemble des données")
    void customSessionExposesIdAndData() {
        HttpTest client = HttpTest.newClient();
        client.get("/session/login?username=carol&password=secret");

        HttpTest.Response info = client.get("/session/info");

        assertEquals(200, info.status());
        assertTrue(info.bodyContains("Session ID"), "corps inattendu : " + info.body());
        assertTrue(info.bodyContains("Données de session"), "corps inattendu : " + info.body());
        assertTrue(info.bodyContains("carol"), "les données de session devraient contenir l'utilisateur : "
                + info.body());
    }

    @Test
    @DisplayName("Modification d'un objet stocké en session (POST)")
    void updateObjectStoredInSession() {
        HttpTest client = HttpTest.newClient();
        client.get("/session/login?username=dave&password=secret");

        HttpTest.Response update = client.postForm("/session/update",
                HttpTest.form("email", "dave@nouveau.com"));

        assertEquals(200, update.status());
        assertTrue(update.bodyContains("Email mis à jour : dave@nouveau.com"),
                "corps inattendu : " + update.body());
        assertTrue(update.bodyContains("dave@nouveau.com"), "corps inattendu : " + update.body());
    }

    @Test
    @DisplayName("clear() vide la session : l'utilisateur n'est plus connecté")
    void logoutClearsSession() {
        HttpTest client = HttpTest.newClient();
        client.get("/session/login?username=erin&password=secret");

        HttpTest.Response logout = client.get("/session/logout");
        assertEquals(200, logout.status());
        assertTrue(logout.bodyContains("Déconnexion réussie"), "corps inattendu : " + logout.body());

        HttpTest.Response profile = client.get("/session/profile");
        assertTrue(profile.bodyContains("Vous n'êtes pas connecté"),
                "la session devrait être vide après clear() : " + profile.body());
    }

    @Test
    @DisplayName("Deux clients ont des sessions indépendantes")
    void sessionsAreIsolatedBetweenClients() {
        HttpTest alice = HttpTest.newClient();
        HttpTest bob = HttpTest.newClient();

        alice.get("/session/login?username=alice&password=secret");
        bob.get("/session/login?username=bob&password=secret");

        HttpTest.Response aliceProfile = alice.get("/session/profile");
        assertTrue(aliceProfile.bodyContains("Profil de alice"), "corps inattendu : " + aliceProfile.body());
        assertFalse(aliceProfile.bodyContains("Profil de bob"), "fuite de session entre clients");

        HttpTest.Response bobProfile = bob.get("/session/profile");
        assertTrue(bobProfile.bodyContains("Profil de bob"), "corps inattendu : " + bobProfile.body());
    }

    @Test
    @DisplayName("Mise à jour sans session -> message d'erreur, pas de crash")
    void updateWithoutSession() {
        HttpTest client = HttpTest.newClient();

        HttpTest.Response update = client.postForm("/session/update",
                HttpTest.form("email", "x@y.com"));

        assertEquals(200, update.status());
        assertTrue(update.bodyContains("Vous devez être connecté"), "corps inattendu : " + update.body());
    }
}
