package fr.iutfbleau.pif;

/**
 * Classe utilitaire qui sert à extraire les composantes Rouge, Vert et Bleu
 * d'une image stockée sous forme de pixels.
 *
 * Chaque composante peut être traitée séparément (par exemple pour le codage
 * Huffman), puis recombinée pour reconstruire l'image finale.
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class CodageRGB {


    /**
     * Extrait la composante rouge (0..255) de chaque pixel.
     *
     * @param pixels tableau des pixels (format int Java)
     * @return tableau des rouges (0..255)
     */
    
    public static int[] extraireRouge(int[] pixels) {
        int[] rouges = new int[pixels.length];

        for (int i = 0; i < pixels.length; i = i + 1) {
            int pixel = pixels[i];
            rouges[i] = (pixel >> 16) & 0xFF;
        }

        return rouges;
    }

    /**
     * Extrait la composante verte (0..255) de chaque pixel.
     *
     * @param pixels tableau des pixels (format int Java)
     * @return tableau des verts (0..255)
     */

    public static int[] extraireVert(int[] pixels) {
        int[] verts = new int[pixels.length];

        for (int i = 0; i < pixels.length; i = i + 1) {
            int pixel = pixels[i];
            verts[i] = (pixel >> 8) & 0xFF;
        }

        return verts;
    }

    /**
     * Extrait la composante bleue (0..255) de chaque pixel.
     *
     * @param pixels tableau des pixels (format int Java)
     * @return tableau des bleus (0..255)
     */

    public static int[] extraireBleu(int[] pixels) {
        int[] bleus = new int[pixels.length];

        for (int i = 0; i < pixels.length; i = i + 1) {
            int pixel = pixels[i];
            bleus[i] = pixel & 0xFF;
        }

        return bleus;
    }

}
