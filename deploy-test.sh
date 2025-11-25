#!/bin/bash

# Nom de l'application (WAR)
APP_NAME="test-project"
WAR_NAME="$APP_NAME-1.0-SNAPSHOT.war"

# Répertoires du projet
BUILD_DIR="target"
LIB_DIR="lib"
TOMCAT_WEBAPPS="/opt/tomcat10/webapps"
TOMCAT_BIN="/opt/tomcat10/bin"

# Vérification de l'environnement
echo "==> Vérification de l'environnement Java"
java -version
echo ""

# Étape 1 : Vérifier que le framework JAR existe
echo "==> Vérification de la présence du framework JAR"
if [ ! -f "$LIB_DIR/framework-1.0-SNAPSHOT.jar" ]; then
    echo "❌ $LIB_DIR/framework-1.0-SNAPSHOT.jar n'existe pas."
    echo "   Veuillez d'abord compiler et déployer le framework avec './deploy.sh'"
    exit 1
fi
echo "✅ Framework JAR trouvé"

# Étape 2 : Vérifier l'accès à Tomcat 10
echo "==> Vérification de l'accès à Tomcat 10"
if [ ! -d "$TOMCAT_WEBAPPS" ]; then
    echo "❌ Répertoire Tomcat 10 non trouvé: $TOMCAT_WEBAPPS"
    echo "   Vérifiez que Tomcat 10 est installé dans /opt/tomcat10"
    exit 1
fi
echo "✅ Accès Tomcat 10 OK"

# Vérifier les permissions
if [ ! -w "$TOMCAT_WEBAPPS" ]; then
    echo "❌ Permissions d'écriture manquantes sur $TOMCAT_WEBAPPS"
    echo "   Essayez avec sudo ou vérifiez les permissions"
    exit 1
fi

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
jar -tf "$WAR_FILE" | grep -E "(WEB-INF/lib/framework|WEB-INF/web.xml|WEB-INF/classes/com/test/controllers)" | head -20

# Étape 6 : Arrêt de Tomcat 10 (si en cours)
echo "==> Arrêt de Tomcat 10..."
if [ -f "$TOMCAT_BIN/shutdown.sh" ]; then
    # Vérifier si Tomcat tourne
    if ps aux | grep -v grep | grep tomcat10 > /dev/null; then
        echo "🛑 Arrêt de Tomcat 10 en cours..."
        $TOMCAT_BIN/shutdown.sh
        # Vérifier que Tomcat est bien arrêté
        if ps aux | grep -v grep | grep tomcat10 > /dev/null; then
            echo "⚠️  Tomcat 10 toujours en cours, arrêt forcé..."
            pkill -f tomcat10
            sleep 1
        fi
        echo "✅ Tomcat 10 arrêté"
    else
        echo "ℹ️  Tomcat 10 n'est pas en cours d'exécution"
    fi
else
    echo "⚠️  Script shutdown.sh non trouvé, arrêt via systemd ou kill"
    sudo systemctl stop tomcat10 2>/dev/null || pkill -f tomcat10
fi

# Étape 7 : Nettoyer l'ancienne déploiement
echo "==> Nettoyage de l'ancienne application..."
sudo rm -rf "$TOMCAT_WEBAPPS/$APP_NAME" "$TOMCAT_WEBAPPS/$APP_NAME.war" 2>/dev/null
rm -rf "$TOMCAT_WEBAPPS/$APP_NAME" "$TOMCAT_WEBAPPS/$APP_NAME.war" 2>/dev/null

# Étape 8 : Déployer le WAR
echo "==> Déploiement de l'application sur Tomcat 10..."
sudo cp "$WAR_FILE" "$TOMCAT_WEBAPPS/$APP_NAME.war" 2>/dev/null || cp "$WAR_FILE" "$TOMCAT_WEBAPPS/$APP_NAME.war"
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du déploiement sur Tomcat 10."
    echo "   Essayez de copier manuellement: sudo cp $WAR_FILE $TOMCAT_WEBAPPS/$APP_NAME.war"
    exit 1
fi
echo "✅ Application déployée avec succès"

# Étape 9 : Redémarrage de Tomcat 10
echo "==> Démarrage de Tomcat 10..."
if [ -f "$TOMCAT_BIN/startup.sh" ]; then
    $TOMCAT_BIN/startup.sh
    if [ $? -ne 0 ]; then
        echo "❌ Erreur lors du démarrage de Tomcat 10 via startup.sh"
        echo "   Tentative via systemd..."
        sudo systemctl start tomcat10 2>/dev/null || echo "⚠️  Démarrage manuel nécessaire"
    else
        echo "✅ Tomcat 10 démarré avec succès"
    fi
else
    echo "⚠️  Script startup.sh non trouvé, démarrage via systemd"
    sudo systemctl start tomcat10
    if [ $? -ne 0 ]; then
        echo "❌ Erreur lors du démarrage via systemd"
        echo "   Démarrez Tomcat 10 manuellement: sudo systemctl start tomcat10"
    else
        echo "✅ Tomcat 10 démarré avec succès via systemd"
    fi
fi

# Étape 10 : Attente du déploiement
echo "==> Attente du déploiement de l'application..."
for i in {1..15}; do
    echo -n "."
    sleep 1
done
echo ""

# Étape 10 : Message de succès
echo ""

# Étape 11 : Vérification des logs
echo "==> Vérification du déploiement..."
LOG_FILE="/opt/tomcat10/logs/catalina.out"
if [ -f "$LOG_FILE" ]; then
    echo "📋 Dernières lignes des logs:"
    tail -20 "$LOG_FILE" | grep -E "(test-project|Framework|Controller|URL|deploy)" || echo "Aucune information de déploiement trouvée dans les logs"
else
    echo "📋 Logs alternatifs:"
    sudo find /opt/tomcat10/logs -name "*.log" -type f -exec tail -5 {} \; 2>/dev/null | grep -E "(test-project|deploy)" | head -10 || echo "Aucun log trouvé"
fi

# Étape 12 : Vérification du déploiement dans le manager
echo ""
echo "🔍 Vérification du statut de déploiement..."
if [ -d "$TOMCAT_WEBAPPS/$APP_NAME" ]; then
    echo "✅ Application déployée: $TOMCAT_WEBAPPS/$APP_NAME"
    echo "📁 Contenu déployé:"
    ls -la "$TOMCAT_WEBAPPS/$APP_NAME/WEB-INF/lib/framework*" 2>/dev/null && echo "✅ Framework présent dans WEB-INF/lib"
else
    echo "⚠️  Le répertoire de déploiement n'existe pas encore, attendez quelques secondes"
fi

# Étape 13 : Message de succès
echo ""
echo "🎉 Déploiement terminé avec succès!"
echo "🌐 Accédez à l'application via:"
echo "   http://localhost:8080/$APP_NAME/front/home"
echo "   http://localhost:8080/$APP_NAME/front/about" 
echo "   http://localhost:8080/$APP_NAME/front/test"
echo ""
echo "📝 Pour surveiller les logs:"
echo "   tail -f /opt/tomcat10/logs/catalina.out"
echo ""
echo "⚙️  Gestion de Tomcat 10:"
echo "   sudo systemctl status tomcat10"
echo "   sudo systemctl restart tomcat10"
echo "   sudo systemctl stop tomcat10"