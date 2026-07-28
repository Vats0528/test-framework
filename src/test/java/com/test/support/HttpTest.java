package com.test.support;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Petit client HTTP pour les tests, avec gestion des cookies (donc de la
 * session HTTP) : chaque instance = un "navigateur" indépendant.
 *
 * Utilisation :
 *   HttpTest client = HttpTest.newClient();
 *   Response r = client.get("/front/page");
 *   assertEquals(200, r.status());
 */
public final class HttpTest {

    /** Réponse HTTP simplifiée */
    public record Response(int status, String body, String contentType) {

        public boolean bodyContains(String needle) {
            return body != null && body.contains(needle);
        }
    }

    private final HttpClient client;
    private final String baseUrl;

    private HttpTest() {
        this.baseUrl = TestServer.baseUrl();
        this.client = HttpClient.newBuilder()
                .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    /** Nouveau client = nouvelle session (aucun cookie partagé) */
    public static HttpTest newClient() {
        return new HttpTest();
    }

    public Response get(String path) {
        return send(HttpRequest.newBuilder(uri(path)).GET());
    }

    public Response postForm(String path, Map<String, String> form) {
        return send(HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(encode(form), StandardCharsets.UTF_8)));
    }

    public Response putForm(String path, Map<String, String> form) {
        return send(HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(encode(form), StandardCharsets.UTF_8)));
    }

    public Response delete(String path) {
        return send(HttpRequest.newBuilder(uri(path)).DELETE());
    }

    /** POST multipart/form-data (Sprint 10) */
    public Response postMultipart(String path, List<FilePart> parts) {
        String boundary = "----FrmTestBoundary" + System.nanoTime();
        byte[] body = buildMultipartBody(boundary, parts);
        return send(HttpRequest.newBuilder(uri(path))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body)));
    }

    /** Un élément de requête multipart : fichier (fileName != null) ou champ texte */
    public record FilePart(String fieldName, String fileName, String contentType, byte[] content) {

        public static FilePart file(String fieldName, String fileName, String text) {
            return new FilePart(fieldName, fileName, "text/plain", text.getBytes(StandardCharsets.UTF_8));
        }

        public static FilePart field(String fieldName, String value) {
            return new FilePart(fieldName, null, null, value.getBytes(StandardCharsets.UTF_8));
        }
    }

    // === interne ===

    private URI uri(String path) {
        return URI.create(baseUrl + path);
    }

    private Response send(HttpRequest.Builder builder) {
        try {
            HttpResponse<String> response = client.send(
                    builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return new Response(
                    response.statusCode(),
                    response.body(),
                    response.headers().firstValue("Content-Type").orElse(""));
        } catch (Exception e) {
            throw new IllegalStateException("Requête HTTP en échec", e);
        }
    }

    private static String encode(Map<String, String> form) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : form.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(java.net.URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
              .append('=')
              .append(java.net.URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static byte[] buildMultipartBody(String boundary, List<FilePart> parts) {
        var out = new java.io.ByteArrayOutputStream();
        try {
            for (FilePart part : parts) {
                StringBuilder header = new StringBuilder();
                header.append("--").append(boundary).append("\r\n");
                header.append("Content-Disposition: form-data; name=\"").append(part.fieldName()).append('"');
                if (part.fileName() != null) {
                    header.append("; filename=\"").append(part.fileName()).append('"');
                }
                header.append("\r\n");
                if (part.contentType() != null) {
                    header.append("Content-Type: ").append(part.contentType()).append("\r\n");
                }
                header.append("\r\n");

                out.write(header.toString().getBytes(StandardCharsets.UTF_8));
                out.write(part.content());
                out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Construction du corps multipart impossible", e);
        }
        return out.toByteArray();
    }

    /** Petit helper pour écrire des formulaires lisibles dans les tests */
    public static Map<String, String> form(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("form() attend des paires clé/valeur");
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put(keyValues[i], keyValues[i + 1]);
        }
        return map;
    }

    /** Liste mutable de parts, pour composer un multipart dans les tests */
    public static List<FilePart> parts(FilePart... items) {
        return new ArrayList<>(List.of(items));
    }
}
