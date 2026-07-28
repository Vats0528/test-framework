package com.test.support;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * Tomcat embarqué partagé par toute la suite de tests.
 *
 * Déploie la VRAIE webapp (src/main/webapp, donc le vrai WEB-INF/web.xml et les
 * vraies JSP) : les tests valident donc à la fois le framework et sa
 * configuration de déploiement, exactement comme sur un Tomcat installé.
 *
 * Démarré une seule fois (à la première utilisation), arrêté à la fin de la JVM.
 */
public final class TestServer {

    private static final String CONTEXT_PATH = "/test-project";

    private static Tomcat tomcat;
    private static int port;

    private TestServer() {
    }

    /** URL de base de l'application, ex. http://localhost:34567/test-project */
    public static synchronized String baseUrl() {
        start();
        return "http://localhost:" + port + CONTEXT_PATH;
    }

    private static synchronized void start() {
        if (tomcat != null) {
            return;
        }
        try {
            Path baseDir = Path.of("target", "embedded-tomcat").toAbsolutePath();
            deleteRecursively(baseDir);
            Files.createDirectories(baseDir);

            File docBase = new File("src/main/webapp").getAbsoluteFile();
            if (!new File(docBase, "WEB-INF/web.xml").isFile()) {
                throw new IllegalStateException(
                        "webapp introuvable : " + docBase + " (lancer les tests depuis test-framework/)");
            }

            Tomcat server = new Tomcat();
            server.setBaseDir(baseDir.toString());
            server.setPort(0); // port libre choisi par l'OS
            server.getConnector();

            // Les classes (framework + contrôleurs) sont déjà sur le classpath de
            // surefire ; le classloader de la webapp délègue au parent, donc le
            // scan de "com.test.controllers" par la FrontServlet les retrouve.
            Context context = server.addWebapp(CONTEXT_PATH, docBase.getAbsolutePath());
            context.setReloadable(false);

            server.start();
            port = server.getConnector().getLocalPort();
            tomcat = server;

            Runtime.getRuntime().addShutdownHook(new Thread(TestServer::stop));

            System.out.println("[TestServer] webapp déployée sur " + baseUrl());
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de démarrer Tomcat embarqué", e);
        }
    }

    private static void stop() {
        try {
            if (tomcat != null) {
                tomcat.stop();
                tomcat.destroy();
            }
        } catch (Exception ignored) {
            // rien à faire à l'arrêt de la JVM
        }
    }

    private static void deleteRecursively(Path path) throws Exception {
        if (!Files.exists(path)) {
            return;
        }
        try (var stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.delete(p);
                } catch (Exception ignored) {
                    // best effort
                }
            });
        }
    }
}
