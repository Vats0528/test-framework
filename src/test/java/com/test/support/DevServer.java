package com.test.support;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Lance l'application dans un Tomcat embarqué, pour l'essayer dans un navigateur
 * sans installer Tomcat. Utilisé par ./run.sh à la racine du projet.
 *
 * Port : 8080 par défaut, sinon -Dport=XXXX
 * Arrêt : Ctrl+C
 */
public final class DevServer {

    private static final String CONTEXT_PATH = "/test-project";

    private DevServer() {
    }

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getProperty("port", "8080"));

        File docBase = new File("src/main/webapp").getAbsoluteFile();
        if (!new File(docBase, "WEB-INF/web.xml").isFile()) {
            System.err.println("webapp introuvable : " + docBase);
            System.err.println("Lancer ./run.sh depuis la racine du projet.");
            System.exit(1);
        }

        Path baseDir = Path.of("target", "dev-tomcat").toAbsolutePath();
        Files.createDirectories(baseDir);

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(baseDir.toString());
        tomcat.setPort(port);
        tomcat.getConnector();

        Context context = tomcat.addWebapp(CONTEXT_PATH, docBase.getAbsolutePath());
        context.setReloadable(false);

        tomcat.start();

        String baseUrl = "http://localhost:" + port + CONTEXT_PATH;
        System.out.println();
        System.out.println("=====================================================");
        System.out.println(" Application demarree : " + baseUrl + "/");
        System.out.println("=====================================================");
        System.out.println(" Pages de test :");
        System.out.println("   " + baseUrl + "/index.html          (accueil)");
        System.out.println("   " + baseUrl + "/sprint8-index.jsp   (binding d'objets)");
        System.out.println("   " + baseUrl + "/sprint11-index.jsp  (sessions et securite)");
        System.out.println("   " + baseUrl + "/upload.html         (upload de fichiers)");
        System.out.println(" Exemples d'endpoints :");
        System.out.println("   " + baseUrl + "/front/page");
        System.out.println("   " + baseUrl + "/api/employees       (JSON)");
        System.out.println("   " + baseUrl + "/user/42");
        System.out.println();
        System.out.println(" Ctrl+C pour arreter.");
        System.out.println("=====================================================");
        System.out.println();

        tomcat.getServer().await();
    }
}
