package fr.iutfbleau.pif;

import java.io.File;

/**
 * Le programme <code>MainVisualisateur</code> permet de lire un fichier
 * au format <code>.pif</code>, de le décoder à l’aide du lecteur PIF,
 * puis d’afficher l’image reconstruite dans une fenêtre graphique.
 *
 * Ce programme correspond à la partie "visualisation" du projet,
 * indépendante de la compression.
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class MainVisualisateur {

    /**
     * Point d’entrée du visualisateur PIF.
     * <p>
     * Si un chemin de fichier est fourni en argument, il est utilisé.
     * Sinon, une fenêtre de sélection de fichier est ouverte.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {

        try {
            /* ===============================
             * Sélection du fichier .pif
             * =============================== */
            File fichier = SelecteurFichier.getFichier(args);

            if (fichier == null) {
                System.out.println("Aucun fichier selectionne.");
                return;
            }

            String chemin = fichier.getAbsolutePath();
            System.out.println("Lecture du fichier : " + fichier.getName());

            /* ===============================
             * Lecture et décodage du fichier PIF
             * =============================== */
            PIFImage image = LecteurPIF.lire(chemin);

            System.out.println("Image decodée");
            System.out.println("Dimensions : " + image.getLargeur() + " x " + image.getHauteur());

            /* ===============================
             * Affichage de l'image reconstruite
             * =============================== */
            FenetreVisualisateur.afficher(image,"Visualisateur PIF - " + fichier.getName());

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du fichier PIF :");
            e.printStackTrace();
        }
    }
}
