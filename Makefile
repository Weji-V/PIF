# =========================
# VARIABLES
# =========================
JC = javac
JCFLAGS = -encoding UTF-8 -implicit:none -d build -cp "build" -sourcepath "src"

JVM = java
JVMFLAGS = -cp "build"

JAR = jar

PACKAGE_NAME = fr.iutfbleau.pif.
SRC_PATH = src/fr/iutfbleau/pif/
BUILD_PATH = build/fr/iutfbleau/pif/
DIST_PATH = dist/

# =========================
# CIBLE PAR DEFAUT
# =========================
all : jar

# =========================
# UTILITAIRES
# =========================
$(BUILD_PATH)SelecteurFichier.class : $(SRC_PATH)SelecteurFichier.java
	$(JC) $(JCFLAGS) $(SRC_PATH)SelecteurFichier.java

$(BUILD_PATH)PIFImage.class : $(SRC_PATH)PIFImage.java
	$(JC) $(JCFLAGS) $(SRC_PATH)PIFImage.java

$(BUILD_PATH)CodageRGB.class : $(SRC_PATH)CodageRGB.java
	$(JC) $(JCFLAGS) $(SRC_PATH)CodageRGB.java

$(BUILD_PATH)GestionSortiePIF.class : $(SRC_PATH)GestionSortiePIF.java
	$(JC) $(JCFLAGS) $(SRC_PATH)GestionSortiePIF.java

# =========================
# HUFFMAN (ARBRE)
# =========================
$(BUILD_PATH)Noeud.class : $(SRC_PATH)Noeud.java
	$(JC) $(JCFLAGS) $(SRC_PATH)Noeud.java

$(BUILD_PATH)NoeudFeuille.class : $(SRC_PATH)NoeudFeuille.java $(BUILD_PATH)Noeud.class
	$(JC) $(JCFLAGS) $(SRC_PATH)NoeudFeuille.java

$(BUILD_PATH)NoeudInterne.class : $(SRC_PATH)NoeudInterne.java $(BUILD_PATH)Noeud.class
	$(JC) $(JCFLAGS) $(SRC_PATH)NoeudInterne.java

# =========================
# ENTREES CANONIQUES
# =========================
$(BUILD_PATH)EntreeCanonique.class : $(SRC_PATH)EntreeCanonique.java
	$(JC) $(JCFLAGS) $(SRC_PATH)EntreeCanonique.java

$(BUILD_PATH)EntreeLongueur.class : $(SRC_PATH)EntreeLongueur.java
	$(JC) $(JCFLAGS) $(SRC_PATH)EntreeLongueur.java

# =========================
# TABLE CODES CANONIQUES
# =========================
$(BUILD_PATH)TableCodesCanoniques.class : $(SRC_PATH)TableCodesCanoniques.java $(BUILD_PATH)EntreeCanonique.class
	$(JC) $(JCFLAGS) $(SRC_PATH)TableCodesCanoniques.java

# =========================
# HUFFMAN
# =========================
$(BUILD_PATH)Huffman.class : $(SRC_PATH)Huffman.java \
	$(BUILD_PATH)Noeud.class \
	$(BUILD_PATH)NoeudFeuille.class \
	$(BUILD_PATH)NoeudInterne.class \
	$(BUILD_PATH)TableCodesCanoniques.class
	$(JC) $(JCFLAGS) $(SRC_PATH)Huffman.java

# =========================
# ECRITURE / LECTURE PIF
# =========================
$(BUILD_PATH)EcrivainPIF.class : $(SRC_PATH)EcrivainPIF.java $(BUILD_PATH)PIFImage.class
	$(JC) $(JCFLAGS) $(SRC_PATH)EcrivainPIF.java

$(BUILD_PATH)NoeudDecodage.class : $(SRC_PATH)NoeudDecodage.java
	$(JC) $(JCFLAGS) $(SRC_PATH)NoeudDecodage.java

$(BUILD_PATH)LecteurPIF.class : $(SRC_PATH)LecteurPIF.java \
	$(BUILD_PATH)PIFImage.class \
	$(BUILD_PATH)NoeudDecodage.class \
	$(BUILD_PATH)EntreeLongueur.class
	$(JC) $(JCFLAGS) $(SRC_PATH)LecteurPIF.java

# =========================
# AFFICHAGE
# =========================
$(BUILD_PATH)ControleurVisualisateur.class : $(SRC_PATH)ControleurVisualisateur.java
	$(JC) $(JCFLAGS) $(SRC_PATH)ControleurVisualisateur.java

$(BUILD_PATH)FenetreVisualisateur.class : $(SRC_PATH)FenetreVisualisateur.java \
	$(BUILD_PATH)PIFImage.class \
	$(BUILD_PATH)ControleurVisualisateur.class
	$(JC) $(JCFLAGS) $(SRC_PATH)FenetreVisualisateur.java

$(BUILD_PATH)FormateurTexte.class : $(SRC_PATH)FormateurTexte.java
	$(JC) $(JCFLAGS) $(SRC_PATH)FormateurTexte.java

$(BUILD_PATH)OutilsImageSwing.class : $(SRC_PATH)OutilsImageSwing.java $(BUILD_PATH)PIFImage.class
	$(JC) $(JCFLAGS) $(SRC_PATH)OutilsImageSwing.java

# =========================
# FENETRE CONVERTISSEUR
# =========================
$(BUILD_PATH)FenetreConvertisseur.class : $(SRC_PATH)FenetreConvertisseur.java \
	$(BUILD_PATH)PIFImage.class \
	$(BUILD_PATH)Huffman.class \
	$(BUILD_PATH)FormateurTexte.class \
	$(BUILD_PATH)OutilsImageSwing.class
	$(JC) $(JCFLAGS) $(SRC_PATH)FenetreConvertisseur.java

# =========================
# MAINS
# =========================
$(BUILD_PATH)MainConvertisseur.class : $(SRC_PATH)MainConvertisseur.java \
	$(BUILD_PATH)SelecteurFichier.class \
	$(BUILD_PATH)PIFImage.class \
	$(BUILD_PATH)CodageRGB.class \
	$(BUILD_PATH)Huffman.class \
	$(BUILD_PATH)EcrivainPIF.class \
	$(BUILD_PATH)GestionSortiePIF.class \
	$(BUILD_PATH)FenetreConvertisseur.class
	$(JC) $(JCFLAGS) $(SRC_PATH)MainConvertisseur.java

$(BUILD_PATH)MainVisualisateur.class : $(SRC_PATH)MainVisualisateur.java \
	$(BUILD_PATH)SelecteurFichier.class \
	$(BUILD_PATH)LecteurPIF.class \
	$(BUILD_PATH)FenetreVisualisateur.class
	$(JC) $(JCFLAGS) $(SRC_PATH)MainVisualisateur.java

# =========================
# DIST (création dossier)
# =========================
distdir:
ifeq ($(OS),Windows_NT)
	@if not exist dist mkdir dist
else
	mkdir -p $(DIST_PATH)
endif

# =========================
# JAR (avec Main-Class) -> dist/
# =========================
# Les vrais artefacts
JAR_CONV = $(DIST_PATH)convertisseur.jar
JAR_VISU = $(DIST_PATH)visualisateur.jar

all: $(JAR_CONV) $(JAR_VISU)

$(JAR_CONV): distdir $(BUILD_PATH)MainConvertisseur.class
	$(JAR) cfe $(JAR_CONV) $(PACKAGE_NAME)MainConvertisseur -C build fr

$(JAR_VISU): distdir $(BUILD_PATH)MainVisualisateur.class
	$(JAR) cfe $(JAR_VISU) $(PACKAGE_NAME)MainVisualisateur -C build fr

jar: $(JAR_CONV) $(JAR_VISU)

# =========================
# EXECUTION DES JAR
# =========================

# Arguments donnés après la cible dans la ligne de commande
run_convertisseur: $(JAR_CONV)
	$(JVM) -jar $(JAR_CONV) $(filter-out $@,$(MAKECMDGOALS))

run_visualisateur: $(JAR_VISU)
	$(JVM) -jar $(JAR_VISU) $(filter-out $@,$(MAKECMDGOALS))

%:
	@:

# =========================
# JAVADOC
# =========================
javadoc :
ifeq ($(OS),Windows_NT)
	if not exist javadoc mkdir javadoc
else
	mkdir -p javadoc
endif
	javadoc -d javadoc -sourcepath src -subpackages fr.iutfbleau.pif

# =========================
# NETTOYAGE
# =========================
clean:
ifeq ($(OS),Windows_NT)
	-rmdir /S /Q build
	-rmdir /S /Q dist
else
	-rm -rf build
	-rm -rf dist
endif

cleanjavadoc:
ifeq ($(OS),Windows_NT)
	-rmdir /S /Q javadoc
else
	-rm -rf javadoc
endif

# =========================
# BUTS FACTICES
# =========================
.PHONY : all clean javadoc cleanjavadoc run_convertisseur run_visualisateur distdir

