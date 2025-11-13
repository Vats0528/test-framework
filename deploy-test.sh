#!/bin/bash

# Nom de l'application (WAR)
APP_NAME="test-project"
WAR_NAME="$APP_NAME-1.0-SNAPSHOT.war"

# Répertoires du projet
BUILD_DIR="target"
LIB_DIR="lib"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_BIN="/opt/tomcat/bin"

# Étape 1 : Vérifier que le framework JAR existe
echo "==> Vérification de la présence du framework JAR"
if [ ! -f "$LIB_DIR/framework-1.0-SNAPSHOT.jar" ]; then
    echo "❌ $LIB_DIR/framework-1.0-SNAPSHOT.jar n'existe pas."
    exit 1
fi
echo "✅ Framework JAR trouvé"

# Étape 2 : Vérifier l'accès à Tomcat
echo "==> Vérification de l'accès à Tomcat"
if [ ! -d "$TOMCAT_WEBAPPS" ]; then
    echo "❌ Répertoire Tomcat non trouvé: $TOMCAT_WEBAPPS"
    exit 1
fi
echo "✅ Accès Tomcat OK"

# Étape 3 : Construction du projet avec Maven
echo "==> Construction du projet test"
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la construction Maven"
    exit 1
fi

# Étape 4 : Vérifier que le WAR a été généré
WAR_FILE="$BUILD_DIR/$WAR_NAME"
if [ ! -f "$WAR_FILE" ]; then
    echo "❌ WAR non trouvé: $WAR_FILE"
    echo "📋 Fichiers dans $BUILD_DIR/:"
    ls -la $BUILD_DIR/
    exit 1
fi
echo "✅ WAR généré avec succès"

# Étape 5 : Afficher le contenu du WAR
echo "📊 Contenu du WAR:"
jar -tf "$WAR_FILE" | grep -E "(WEB-INF/lib/|WEB-INF/web.xml)"

# Étape 6 : Arrêt de Tomcat
echo "==> Arrêt de Tomcat..."
if [ -f "$TOMCAT_BIN/shutdown.sh" ]; then
    $TOMCAT_BIN/shutdown.sh
    sleep 5
    echo "✅ Tomcat arrêté"
fi

# Étape 7 : Déployer le WAR
echo "==> Déploiement de l'application sur Tomcat..."
cp "$WAR_FILE" "$TOMCAT_WEBAPPS/$APP_NAME.war"
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du déploiement sur Tomcat."
    exit 1
fi
echo "✅ Application déployée avec succès"

# Étape 8 : Redémarrage de Tomcat
echo "==> Redémarrage de Tomcat..."
if [ -f "$TOMCAT_BIN/startup.sh" ]; then
    $TOMCAT_BIN/startup.sh
    if [ $? -ne 0 ]; then
        echo "❌ Erreur lors du redémarrage de Tomcat."
        exit 1
    fi
    echo "✅ Tomcat redémarré avec succès"
fi

# Étape 9 : Attente du déploiement
sleep 10

# Étape 10 : Message de succès
echo ""
echo "🎉 Déploiement terminé avec succès!"
echo "🌐 Accédez à l'application via:"
echo "    http://localhost:8080/test-project/front

    http://localhost:8080/test-project/front/home

    http://localhost:8080/test-project/front/about

    http://localhost:8080/test-project/front/test"
