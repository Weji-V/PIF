# PIF — Primitive Image Format 📁


Bienvenue sur notre projet **"PIF"**, ce projet a été réalisé dans le cadre de la [SAÉ 3.2](https://iut-fbleau.fr/sitebp/sae3/32_2025/TC6H66SI9M28YUVH.php) du département Informatique à l’IUT de Fontainebleau et a pour objectif de créer un support logiciel pour un nouveau format d’image, appelé PIF (Primitive Image Format), inspiré du format JFIF (JPEG File Interchange Format)



## Table des matières
- [Présentation](#présentation)
- [Organisation du projet](#organisation-du-projet)
- [Compilation et lancement](#Compilation-et-lancement)
- [Création de la documentation](#création-de-la-documentation)
- [Nettoyage des fichiers temporaires](#Nettoyage-des-fichiers-temporaires)
- [Rapport d'avancement](#Rapport-d-avancement)
- [Crédits](#crédits)

---

## Présentation 🧩

Le format PIF est un format d’image compressé sans perte, basé sur l’algorithme de Huffman. Son objectif est de mettre en œuvre les principes de la compression et du décodage des données, plutôt que de concurrencer les formats d’images existants déjà optimisés (comme PNG ou JPEG).

Ce projet consiste à développer deux programmes en Java :


### Visualisateur PIF

- Ouvre et affiche une image contenue dans un fichier .pif 

- Le chemin du fichier peut être fourni en argument ou choisi via un JFileChooser

- Fenêtre redimensionnable et image navigable à la souris


### Convertisseur vers PIF

- Charge une image dans un format supporté par ImageIO.

- Affiche des informations détaillées (image, tables de fréquences, codes Huffman et codes canoniques)

- Permet d’exporter l’image au format .pif

---

Ce projet a été développé en **Java** en respectant les consignes de l’IUT, avec une organisation claire du code (séparation des rôles) afin de faciliter la compréhension, la maintenance et l’évolution du programme.

---

## Organisation du projet

L’organisation du projet suit une structure claire permettant de séparer le code source, les composants externes et les fichiers de configuration. Voici l'aborescence :

```
SAE32_2025/
├── Documentations/
│   ├── diagramme_uml/
│      ├── diagramme_classes
│      └── diagramme_objets
│   └── Rapports.pdf
├── res/ #Image que l'ont veut convertir en PIF (dans ce cas on met celle du projet)
│       ├── rotsnake.pif
│       └── rotsnake.png
├── src/ # Code source du projet
│ └── fr/iutbleau/pif/ # Package principal contenant toutes les classes Java
│       ├── CodageRGb.java
│       ├── ControleurAfficheurImage.java
│       ├── ControleurVisualisateur.java
│       ├── EcrivainPIF.java
│       ├── EntreeCanonique.java
│       ├── EntreeLongueur.java
│       ├── FenetreConvertisseur.java
│       ├── FenetreVisualisateur.java
│       ├── FormateurTexte.java
│       ├── GestionSortiePIF.java
│       ├── Huffman.java
│       ├── LecteurPIF.java
│       ├── MainConvertisseur.java
│       ├── MainVisualisateur.java
│       ├── Noeud.java
│       ├── NoeudDecodage.java
│       ├── NoeudFeuille.java
│       ├── NoeudInterne.java
│       ├── OutilsImageSwing.java
│       ├── PIFImage.java
│       ├── SelecteurFichier.java
│       └── TableCodesCanoniques.java
├── .gitignore # Permet d’ignorer les fichiers .class générés lors de la compilation
├── Makefile # Automatisation pour la compilation et l’exécution
└── README.md # Fichier actuelle

```
---

## Compilation et lancement



Pour avoir accès au projet faire les commandes suivantes:

```bash
# Cloner le dépôt
git clone https://grond.iut-fbleau.fr/sehl/SAE32_2025/
cd SAE32_2025
```
---

### Compilation et création des JAR :

```bash
make
#  ou
make all
```
  Compile toutes les classes Java
  
  Crée les archives :
   - dist/convertisseur.jar
   - dist/visualisateur.jar

---

### Lancer le convertisseur PIF :
```bash
make run_convertisseur
```

  Ouvre une fenêtre pour choisir l’image à convertir
  -> Le fichier .pif est créé automatiquement

 Avec arguments :
  - make run_convertisseur image.png
  - make run_convertisseur image.png sortie.pif

---

  Si un seul argument est fourni :
   - l’image est utilisée comme entrée
   - la sortie est générée automatiquement dans res/sortie/

---

  Si deux arguments sont fournis :
   - premier : image d’entrée
   - second  : fichier de sortie (.pif forcé automatiquement)

---

### Lancer le visualisateur PIF :

```bash
make run_visualisateur
```
  Ouvre une fenêtre pour choisir un fichier .pif

Avec argument :

```bash
make run_visualisateur image.pif
```
---


### Création de la documentation

La documentation du code source est générée via **Javadoc**. Utilisez :

```bash
make javadoc
```

---

### Nettoyage des fichiers temporaires 

Pour supprimer les fichiers intermédiaires on fait :

```bash
make clean
```

 Supprime les dossiers :
     - build/
     - dist/

  Pour supprimer la documentation JavaDoc (dossier javadoc/) : 

  ```bash 
make cleanjavadoc
   ```


---

## Rapport d'avancement 

Le rapport du projet est disponible dans le dossier `Documentations`. Accédez-y directement ici : [Documentations/Pif-Rapport.pdf](./Documentations/Pif-Rapport.pdf).

---

## Crédits

Ce projet a été réalisé par :
- [Jenson VAL](https://grond.iut-fbleau.fr/val)
- [Aylane SEHL](https://grond.iut-fbleau.fr/sehl)
- [Séri-Khane YOLOU](https://grond.iut-fbleau.fr/yolou)

Professeur : **Luc Hernandez**.
