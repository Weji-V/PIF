package fr.iutfbleau.pif;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * La classe <code>EcrivainPIF</code> permet d'écrire une image compressée
 * au format <code>.pif</code> (RGB) à l'aide de la compression de Huffman.
 *
 * <p>Le fichier PIF contient :</p>
 * <ul>
 *   <li>une signature permettant d'identifier le format,</li>
 *   <li>les dimensions de l'image (largeur et hauteur),</li>
 *   <li>trois tables de codes canoniques (Rouge, Vert, Bleu),</li>
 *   <li>trois flux compressés correspondant aux composantes RGB.</li>
 * </ul>
 *
 * <p>Les tables canoniques ne stockent que les longueurs des codes.
 * Les codes binaires complets sont reconstruits lors de la lecture.</p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class EcrivainPIF {

    /** Signature "PIF" en int : 0x50 0x49 0x46 0x32. */
    private static final int SIGNATURE_PIF = 0x50494632;

    /**
    * Écrit un fichier PIF à partir d'une image et de ses données compressées.
    * Les composantes Rouge, Vert et Bleu sont écrites dans cet ordre.
    *
    * @param cheminSortie chemin du fichier de sortie
    * @param image image source (dimensions)
    * @param codesRouge table canonique Rouge (valeur -> code)
    * @param codesVert table canonique Vert (valeur -> code)
    * @param codesBleu table canonique Bleu (valeur -> code)
    * @param nombreBitsRouge nombre de bits utiles du flux Rouge
    * @param nombreBitsVert nombre de bits utiles du flux Vert
    * @param nombreBitsBleu nombre de bits utiles du flux Bleu
    * @param octetsRouge flux compressé Rouge
    * @param octetsVert flux compressé Vert
    * @param octetsBleu flux compressé Bleu
    * @throws IOException en cas d'erreur d'écriture
    */

    public static void ecrire(String cheminSortie, PIFImage image, Map<Integer, String> codesRouge, Map<Integer, String> codesVert, Map<Integer, String> codesBleu, int nombreBitsRouge, int nombreBitsVert, int nombreBitsBleu, byte[] octetsRouge, byte[] octetsVert, byte[] octetsBleu) throws IOException {

        DataOutputStream sortie = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(cheminSortie)));

        try {
            // --- En-tête ---
            sortie.writeInt(SIGNATURE_PIF);
            sortie.writeInt(image.getLargeur());
            sortie.writeInt(image.getHauteur());

            // --- Tables canoniques (ordre : Rouge, Vert, Bleu) ---
            ecrireTableCanonique(sortie, codesRouge);
            ecrireTableCanonique(sortie, codesVert);
            ecrireTableCanonique(sortie, codesBleu);

            // --- Flux compressés (ordre : Rouge, Vert, Bleu) ---
            ecrireFlux(sortie, nombreBitsRouge, octetsRouge);
            ecrireFlux(sortie, nombreBitsVert, octetsVert);
            ecrireFlux(sortie, nombreBitsBleu, octetsBleu);

            sortie.flush();

        } finally {
            sortie.close();
        }
    }

    /**
     * Écrit une table canonique sous forme :
     * <ul>
     *   <li>int : nombre d'entrées</li>
     *   <li>pour chaque entrée : unsigned byte valeur (0..255), unsigned byte longueur</li>
     * </ul>
     *
     * <p>Le lecteur pourra reconstruire les codes canoniques en triant par
     * (longueur puis valeur), puis en générant les codes.</p>
     *
     * @param sortie flux de sortie
     * @param tableCodes table valeur -> code (le code sert uniquement à connaître la longueur)
     * @throws IOException si erreur d'écriture
     */
    private static void ecrireTableCanonique(DataOutputStream sortie, Map<Integer, String> tableCodes) throws IOException {

        sortie.writeInt(tableCodes.size());

        for (Map.Entry<Integer, String> entree : tableCodes.entrySet()) {

            int valeur = entree.getKey().intValue();
            int longueur = entree.getValue().length();

            // Valeur doit être 0..255 (RGB)
            if (valeur < 0 || valeur > 255) {
                throw new IOException("Valeur RGB invalide dans la table canonique : " + valeur);
            }

            // Longueur tient largement dans 1 octet pour ce projet
            if (longueur < 1 || longueur > 255) {
                throw new IOException("Longueur de code invalide : " + longueur);
            }

            sortie.writeByte(valeur);     // 1 octet
            sortie.writeByte(longueur);   // 1 octet
        }
    }

    /**
     * Écrit un flux compressé sous forme :
     * <ul>
     *   <li>int : nbBits utiles</li>
     *   <li>int : nbOctets</li>
     *   <li>byte[] : données</li>
     * </ul>
     *
     * @param sortie flux de sortie
     * @param nbBits nombre de bits utiles
     * @param octets tableau d'octets compressés
     * @throws IOException si erreur
     */
    private static void ecrireFlux(DataOutputStream sortie, int nbBits, byte[] octets) throws IOException {
        sortie.writeInt(nbBits);
        sortie.writeInt(octets.length);
        sortie.write(octets);
    }
}
