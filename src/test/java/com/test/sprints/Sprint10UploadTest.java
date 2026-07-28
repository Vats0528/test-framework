package com.test.sprints;

import com.test.support.HttpTest;
import com.test.support.HttpTest.FilePart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sprint 10 - Upload de fichiers (multipart/form-data)
 *
 * - UploadedFile (fichier unique)
 * - UploadedFile[] (plusieurs fichiers sur le même champ)
 * - Map<String, UploadedFile> (tous les fichiers de la requête)
 * - champs texte et fichiers mélangés dans la même requête
 */
@DisplayName("Sprint 10 - Upload de fichiers")
class Sprint10UploadTest {

    private final HttpTest client = HttpTest.newClient();

    @Test
    @DisplayName("Fichier unique + champ texte")
    void singleFileWithTextField() {
        String content = "Contenu du fichier de test";
        HttpTest.Response response = client.postMultipart("/upload", HttpTest.parts(
                FilePart.file("file", "test.txt", content),
                FilePart.field("description", "mon fichier")));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("filename=test.txt"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("size=" + content.getBytes(java.nio.charset.StandardCharsets.UTF_8).length),
                "taille incorrecte : " + response.body());
        assertTrue(response.bodyContains("desc=mon fichier"),
                "le champ texte doit aussi être lié : " + response.body());
    }

    @Test
    @DisplayName("Aucun fichier envoyé -> paramètre null géré")
    void noFileSent() {
        HttpTest.Response response = client.postMultipart("/upload", HttpTest.parts(
                FilePart.field("description", "sans fichier")));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Aucun fichier reçu"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Plusieurs fichiers sur le même champ -> UploadedFile[]")
    void multipleFilesSameField() {
        HttpTest.Response response = client.postMultipart("/uploadMultiple", HttpTest.parts(
                FilePart.file("photos", "a.txt", "AAA"),
                FilePart.file("photos", "b.txt", "BBBBB"),
                FilePart.file("photos", "c.txt", "CC")));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Reçu 3 fichier(s)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("a.txt (3 bytes)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("b.txt (5 bytes)"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("c.txt (2 bytes)"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Tous les fichiers de la requête -> Map<String, UploadedFile>")
    void allFilesAsMap() {
        HttpTest.Response response = client.postMultipart("/uploadMap", HttpTest.parts(
                FilePart.file("cv", "cv.txt", "mon CV"),
                FilePart.file("photo", "photo.txt", "ma photo"),
                FilePart.field("commentaire", "ignoré car pas un fichier")));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("Reçu 2 fichier(s)"),
                "seuls les fichiers doivent être dans la Map : " + response.body());
        assertTrue(response.bodyContains("cv => cv.txt"), "corps inattendu : " + response.body());
        assertTrue(response.bodyContains("photo => photo.txt"), "corps inattendu : " + response.body());
    }

    @Test
    @DisplayName("Le contenu binaire est transmis intact")
    void binaryContentIsPreserved() {
        byte[] binary = new byte[256];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) i;
        }
        HttpTest.Response response = client.postMultipart("/upload", HttpTest.parts(
                new FilePart("file", "data.bin", "application/octet-stream", binary),
                FilePart.field("description", "binaire")));

        assertEquals(200, response.status());
        assertTrue(response.bodyContains("size=256"), "les 256 octets devraient arriver : " + response.body());
    }
}
