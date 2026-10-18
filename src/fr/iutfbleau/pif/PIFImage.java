package fr.iutfbleau.pif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * La classe <code>PIFImage</code> représente une image en mémoire
 * à l'aide de ses dimensions et d'un tableau de pixels.
 *
 * <p>Elle constitue la structure centrale du projet pour manipuler
 * les images, aussi bien lors de la compression que lors de la
 * décompression.</p>
 *
 * <p>Les pixels sont stockés sous forme d'entiers.</p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class PIFImage {

    /**
     * Largeur de l'image (en nombre de pixels).
     */
    private int largeur;

    /**
     * Hauteur de l'image (en nombre de pixels).
     */
    private int hauteur;

    /**
     * Tableau contenant les pixels de l'image.
     * Chaque pixel est codé sous la forme d'un entier.
     */
    private int[] pixels;

    /**
     * Construit une image à partir de ses dimensions et de son tableau de pixels.
     *
     * @param largeur largeur de l'image
     * @param hauteur hauteur de l'image
     * @param pixels tableau de pixels 
     */
    public PIFImage(int largeur, int hauteur, int[] pixels) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pixels = pixels;
    }

    /**
     * Renvoie la largeur de l'image.
     *
     * @return la largeur en pixels
     */
    public int getLargeur() {
        return largeur;
    }

    /**
     * Renvoie la hauteur de l'image.
     *
     * @return la hauteur en pixels
     */
    public int getHauteur() {
        return hauteur;
    }

    /**
     * Renvoie le tableau des pixels de l'image.
     *
     * @return tableau des pixels au format ARGB
     */
    public int[] getPixels() {
        return pixels;
    }

    /**
     * Charge une image classique (PNG, JPG, etc.) depuis le disque
     * et la convertit en un objet <code>PIFImage</code>.
     *
     * <p>Cette méthode utilise la classe <code>ImageIO</code> pour lire
     * l'image, puis extrait tous les pixels dans un tableau.</p>
     *
     * @param cheminImage chemin du fichier image à charger
     * @return une instance de <code>PIFImage</code> correspondant à l'image
     * @throws IOException si le fichier ne peut pas être lu ou si le format
     *         n'est pas supporté
     */
    public static PIFImage chargerDepuisImage(String cheminImage) throws IOException {

        BufferedImage imageChargee = ImageIO.read(new File(cheminImage));

        if (imageChargee == null) {
            throw new IOException("Image illisible ou format non supporté : " + cheminImage);
        }

        int largeurImage = imageChargee.getWidth();
        int hauteurImage = imageChargee.getHeight();

        int[] pixelsImage = new int[largeurImage * hauteurImage];
        imageChargee.getRGB(0, 0, largeurImage, hauteurImage, pixelsImage, 0, largeurImage);

        return new PIFImage(largeurImage, hauteurImage, pixelsImage);
    }
}
