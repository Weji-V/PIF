package fr.iutfbleau.pif;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * Classe utilitaire chargée de gérer le nom et l’emplacement
 * du fichier de sortie au format PIF.
 *
 * Elle permet de générer automatiquement un chemin de sortie
 * cohérent à partir du fichier d’entrée.
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class GestionSortiePIF {

    /**
     * Vérifie qu’un chemin se termine bien par l’extension ".pif".
     * Si ce n’est pas le cas, l’extension est ajoutée.
     *
     * @param chemin chemin fourni par l’utilisateur
     * @return chemin garanti avec l’extension ".pif"
     */
    public static String forcerExtensionPIF(String chemin) {

        if (chemin == null) {
            return null;
        }
    
        // On nettoie juste le nom final (pas tout le chemin !)
        File f = new File(chemin);
        String parent = f.getParent();
        String nom = f.getName();
    
        nom = nettoyerNomFichier(nom);
    
        String min = nom.toLowerCase();
        if (!min.endsWith(".pif")) {
            nom = nom + ".pif";
        }
    
        if (parent == null) {
            return nom;
        } else {
            return new File(parent, nom).getPath();
        }
    }
    
    /**
     * Ouvre un JFileChooser pour demander à l'utilisateur où enregistrer le fichier PIF.
     * On force ensuite l’extension .pif.
     *
     * @param nomParDefaut nom proposé (ex: image.pif)
     * @return chemin complet choisi, ou null si annulé
     */
    public static String choisirCheminSortieAvecFenetre(String nomParDefaut) {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choisir l'emplacement du fichier PIF");

        if (nomParDefaut != null) {
            chooser.setSelectedFile(new File(nomParDefaut));
        }

        int resultat = chooser.showSaveDialog(null);

        if (resultat != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File fichier = chooser.getSelectedFile();
        String chemin = fichier.getPath();

        return forcerExtensionPIF(chemin);
    }
    
/**
 * Construit automatiquement un chemin de sortie pour un fichier PIF
 * dans le dossier <code>res/sortie</code> du projet.
 *
 * <p>
 * Le fichier de sortie porte le même nom que le fichier image d’entrée,
 * avec l’extension <code>.pif</code>.
 * Les espaces présents dans le nom du fichier sont remplacés
 * par des underscores afin d’éviter tout problème de lecture.
 * </p>
 *
 * <p>
 * Exemple :
 * </p>
 * <ul>
 *   <li>Image d’entrée : <code>mon image test.png</code></li>
 *   <li>Fichier créé  : <code>res/sortie/mon_image_test.pif</code></li>
 * </ul>
 *
 * <p>
 * Si le dossier <code>res</code> ou <code>res/sortie</code> n’existe pas,
 * il est créé automatiquement.
 * </p>
 *
 * @param fichierEntree fichier image sélectionné en entrée
 * @return chemin complet du fichier de sortie au format PIF
 */
    public static String cheminSortieAutoDansResSortie(File fichierEntree) {

        // Dossier res/output
        File dossierRes = new File("res");
        File dossierOutput = new File(dossierRes, "sortie");
    
        // Création si nécessaire
        if (!dossierOutput.exists()) {
            dossierOutput.mkdirs();
        }
    
        // Nom du fichier sans extension
        String nom = fichierEntree.getName();
        int point = nom.lastIndexOf('.');
        if (point != -1) {
        nom = nom.substring(0, point);
        }
    
        // Suppression des espaces
        nom = nettoyerNomFichier(nom);
    
        File sortie = new File(dossierOutput, nom + ".pif");
        return sortie.getPath();
    }

/**
 * Remplace les espaces (et autres caractères gênants) dans un nom de fichier.
 * Ici on remplace les espaces par des '_' pour éviter les soucis en ligne de commande.
 *
 * @param nom nom de fichier (sans dossier)
 * @return nom nettoyé
 */
    public static String nettoyerNomFichier(String nom) {

    if (nom == null) {
        return null;
    }

    nom = nom.trim();

    // Remplace les espaces par underscore
    nom = nom.replace(' ', '_');

    // Optionnel : si tu veux aussi éviter les tabulations
    nom = nom.replace('\t', '_');

    return nom;
}

    
}
