package fr.iutfbleau.pif;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Classe utilitaire regroupant des méthodes liées à l'affichage
 * des images dans une interface Swing.
 *
 * <p>
 * Elle permet notamment :
 * </p>
 * <ul>
 *   <li>de convertir un objet {@link PIFImage} en image RGB affichable,</li>
 *   <li>de redimensionner une image si elle est trop grande pour l’écran.</li>
 * </ul>
 *
 * <p>
 * Cette classe ne contient aucune logique de compression ou de lecture de fichier.
 * Elle est dédiée uniquement à l’adaptation des images pour l’affichage graphique.
 * </p>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class OutilsImageSwing {

    /**
     * Construit une image RGB affichable par Swing à partir d’un objet {@link PIFImage}.
     *
     * <p>
     * Les composantes rouge, verte et bleue sont extraites de chaque pixel
     * afin de créer une {@link BufferedImage} compatible avec un {@code ImageIcon}.
     * </p>
     *
     * @param image image en mémoire contenant les pixels ARGB
     * @return une image RGB utilisable pour l’affichage
     */
    public static BufferedImage construireImageRGB(PIFImage image) {

        int largeur = image.getLargeur();
        int hauteur = image.getHauteur();
        int[] pixels = image.getPixels();

        BufferedImage buffer = new BufferedImage(largeur,hauteur,BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < hauteur; y = y + 1) {
            for (int x = 0; x < largeur; x = x + 1) {

                int pixel = pixels[y * largeur + x];

                int rouge = (pixel >> 16) & 0xFF;
                int vert  = (pixel >> 8) & 0xFF;
                int bleu  = pixel & 0xFF;

                int rgb = (rouge << 16) | (vert << 8) | bleu;
                buffer.setRGB(x, y, rgb);
            }
        }

        return buffer;
    }

    /**
     * Réduit une image si ses dimensions dépassent la moitié
     * de la taille de l’écran.
     *
     * <p>
     * Le redimensionnement conserve les proportions de l’image
     * afin d’éviter toute déformation.
     * </p>
     *
     * <p>
     * Si l’image est déjà de taille raisonnable, elle est retournée
     * sans modification.
     * </p>
     *
     * @param img image à vérifier et éventuellement redimensionner
     * @return image affichable, redimensionnée si nécessaire
     */
    public static Image reduireSiNecessaire(BufferedImage img) {

        int largeur = img.getWidth();
        int hauteur = img.getHeight();

        Dimension ecran = Toolkit.getDefaultToolkit().getScreenSize();
        int maxLargeur = ecran.width / 2;
        int maxHauteur = ecran.height / 2;

        // Si l'image tient déjà dans l'écran, on ne la modifie pas
        if (largeur <= maxLargeur && hauteur <= maxHauteur) {
            return img;
        }

        // Calcul du facteur de réduction en conservant les proportions
        double facteurLargeur = (double) maxLargeur / largeur;
        double facteurHauteur = (double) maxHauteur / hauteur;
        double facteur = Math.min(facteurLargeur, facteurHauteur);

        int nouvelleLargeur = (int) (largeur * facteur);
        int nouvelleHauteur = (int) (hauteur * facteur);

        return img.getScaledInstance(nouvelleLargeur, nouvelleHauteur,Image.SCALE_SMOOTH);
    }
}
