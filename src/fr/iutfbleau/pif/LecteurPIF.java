package fr.iutfbleau.pif;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe <code>LecteurPIF</code> permet de lire un fichier <code>.pif</code>
 * et de reconstruire l'image originale.
 *
 * <p>Le format PIF contient :</p>
 * <ul>
 *   <li>une signature qui identifie le format,</li>
 *   <li>la largeur et la hauteur de l'image,</li>
 *   <li>trois tables de codes canoniques (Rouge, Vert, Bleu) stockées sous forme (valeur, longueur),</li>
 *   <li>trois flux compressés correspondant aux composantes Rouge, Vert et Bleu.</li>
 * </ul>
 *
 * <p>Les codes canoniques sont reconstruits uniquement à partir des longueurs
 * puis un arbre de décodage est construit pour décompresser chaque flux.</p>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class LecteurPIF {

    /** Signature "PIF" en int (0x50 0x49 0x46 0x32). */
    private static final int SIGNATURE_PIF = 0x50494632;

    /**
    * Lit un fichier PIF et reconstruit l'image en mémoire.
    *
    * <p>Étapes principales :</p>
    * <ol>
    *   <li>vérification de la signature,</li>
    *   <li>lecture des dimensions,</li>
    *   <li>lecture des 3 tables canoniques, puis reconstruction des codes,</li>
    *   <li>construction des arbres de décodage,</li>
    *   <li>décodage des 3 flux compressés (R,V,B),</li>
    *   <li>recomposition des pixels RGB.</li>
    * </ol>
    *
    * @param chemin chemin du fichier <code>.pif</code>
    * @return l'image reconstruite
    * @throws IOException si le fichier ne peut pas être lu ou si le format est invalide
    */

public static PIFImage lire(String chemin) throws IOException {


        DataInputStream entree = new DataInputStream(new BufferedInputStream(new FileInputStream(chemin)));

        try {
            int signature = entree.readInt();
            if (signature != SIGNATURE_PIF) {
                throw new IOException("Format PIF non reconnu (signature incorrecte).");
            }

            int largeur = entree.readInt();
            int hauteur = entree.readInt();
            int nbPixels = largeur * hauteur;

            // 1) Lire les 3 tables canoniques (valeur + longueur) puis reconstruire les codes
            Map<Integer, String> codesR = lireTableCanonique(entree);
            Map<Integer, String> codesV = lireTableCanonique(entree);
            Map<Integer, String> codesB = lireTableCanonique(entree);

            // 2) Construire 3 arbres de décodage
            NoeudDecodage racineR = construireArbreDecodage(codesR);
            NoeudDecodage racineV = construireArbreDecodage(codesV);
            NoeudDecodage racineB = construireArbreDecodage(codesB);

            // 3) Lire et décoder 3 flux compressés
            int[] rouge = decoderFlux(entree, racineR, nbPixels);
            int[] vert  = decoderFlux(entree, racineV, nbPixels);
            int[] bleu  = decoderFlux(entree, racineB, nbPixels);

            // 4) Recomposer les pixels RGB 
            int[] pixels = new int[nbPixels];
            for (int i = 0; i < nbPixels; i = i + 1) {
                int r = rouge[i] & 0xFF;
                int v = vert[i] & 0xFF;
                int b = bleu[i] & 0xFF;

                pixels[i] = (0xFF << 24) | (r << 16) | (v << 8) | b;
            }

            return new PIFImage(largeur, hauteur, pixels);

        } finally {
            entree.close();
        }
    }

    /**
    * Lit une table canonique dans le fichier PIF.
    *
    * <p>Dans le fichier, la table est stockée sous forme :</p>
    * <ul>
    *   <li>int : nombre d'entrées</li>
    *   <li>pour chaque entrée : (valeur, longueur) sur 2 octets</li>
    * </ul>
    *
    * <p>À partir des longueurs, on reconstruit les codes canoniques en :</p>
    * <ol>
    *   <li>triant par longueur puis par valeur,</li>
    *   <li>générant les codes binaires dans l'ordre canonique.</li>
    * </ol>
    *
    * @param entree flux d'entrée
    * @return table des codes canoniques (valeur -> code binaire)
    * @throws IOException si une lecture échoue
    */

    private static Map<Integer, String> lireTableCanonique(DataInputStream entree) throws IOException{


        int nbEntrees = entree.readInt();
        List<EntreeLongueur> liste = new ArrayList<EntreeLongueur>();

        for (int i = 0; i < nbEntrees; i = i + 1) {
            int valeur = entree.readUnsignedByte();
            int longueur = entree.readUnsignedByte();
            liste.add(new EntreeLongueur(valeur, longueur));
        }

        // Trier comme demandé : longueur puis valeur
        Collections.sort(liste);

        // Reconstruire les codes canoniques
        Map<Integer, String> table = new HashMap<Integer, String>();

        int code = 0;
        int longueurPrecedente = 0;

        for (int i = 0; i < liste.size(); i = i + 1) {
            EntreeLongueur e = liste.get(i);

            if (i == 0) {
                // premier code : tout à zéro avec la bonne longueur
                longueurPrecedente = e.getLongueur();
                code = 0;
            } else {
                // on incrémente puis on ajuste la longueur
                code = code + 1;
                if (e.getLongueur() > longueurPrecedente) {
                    code = code << (e.getLongueur() - longueurPrecedente);
                    longueurPrecedente = e.getLongueur();
                }
            }

            String binaire = Integer.toBinaryString(code);
            while (binaire.length() < e.getLongueur()) {
                binaire = "0" + binaire;
            }

            table.put(Integer.valueOf(e.getValeur()), binaire);
        }

        return table;
    }

    /**
    * Construit un arbre binaire de décodage à partir d'une table de codes.
    *
    * <p>Chaque code binaire est inséré dans l'arbre :
    * 0 signifie "aller à gauche" et 1 signifie "aller à droite".</p>
    *
    * @param tableCodes table (valeur -> code binaire)
    * @return racine de l'arbre de décodage
    */

    private static NoeudDecodage construireArbreDecodage(Map<Integer, String> tableCodes) {

        NoeudDecodage racine = new NoeudDecodage();

        for (Map.Entry<Integer, String> entree : tableCodes.entrySet()) {
            int valeur = entree.getKey().intValue();
            String code = entree.getValue();
            insererCode(racine, code, valeur);
        }

        return racine;
    }

    /**
    * Insère un code binaire dans l'arbre de décodage.
    *
    * <p>À la fin du parcours, le nœud atteint devient une feuille
    * et on y stocke la valeur associée.</p>
    *
    * @param racine racine de l'arbre
    * @param code chaîne binaire composée de '0' et '1'
    * @param valeur valeur associée au code
    */
   
    private static void insererCode(NoeudDecodage racine, String code, int valeur) {

        NoeudDecodage courant = racine;

        for (int i = 0; i < code.length(); i = i + 1) {
            char bit = code.charAt(i);

            if (bit == '0') {
                if (courant.getGauche() == null) {
                    courant.setGauche(new NoeudDecodage());
                }
                courant = courant.getGauche();
            } else { // '1'
                if (courant.getDroite() == null) {
                    courant.setDroite(new NoeudDecodage());
                }
                courant = courant.getDroite();
            }
        }

        courant.setEstFeuille(true);
        courant.setValeur(valeur);
    }

    /**
    * Décode un flux compressé avec Huffman à l'aide d'un arbre de décodage.
    *
    * <p>Le flux est stocké sous forme :</p>
    * <ul>
    *   <li>int : nbBits utiles</li>
    *   <li>int : nbOctets</li>
    *   <li>byte[] : données</li>
    * </ul>
    *
    * <p>On lit les bits un par un, on traverse l'arbre, et à chaque feuille
    * on produit une valeur décodée.</p>
    *
    * @param entree flux d'entrée
    * @param racine racine de l'arbre de décodage
    * @param nbValeurs nombre de valeurs attendues (souvent largeur * hauteur)
    * @return tableau de valeurs décodées
    * @throws IOException si le flux est invalide ou incomplet
    */

    private static int[] decoderFlux(DataInputStream entree, NoeudDecodage racine, int nbValeurs) throws IOException {

        int nbBits = entree.readInt();
        int nbOctets = entree.readInt();

        byte[] octets = new byte[nbOctets];
        entree.readFully(octets);

        int[] resultat = new int[nbValeurs];

        int indexValeur = 0;
        int indexBit = 0;

        NoeudDecodage courant = racine;

        while (indexBit < nbBits && indexValeur < nbValeurs) {

            int bit = lireBit(octets, indexBit);
            indexBit = indexBit + 1;

            if (bit == 0) {
                courant = courant.getGauche();
            } else {
                courant = courant.getDroite();
            }

            if (courant == null) {
                throw new IOException("Flux invalide : parcours Huffman impossible (arbre incomplet).");
            }

            if (courant.isEstFeuille()) {
                resultat[indexValeur] = courant.getValeur();
                indexValeur = indexValeur + 1;
                courant = racine;
            }
        }

        if (indexValeur != nbValeurs) {
            throw new IOException("Flux invalide : pas assez de valeurs décodées (" + indexValeur + "/" + nbValeurs + ").");
        }

        return resultat;
    }

    /**
    * Lit un bit dans un tableau d'octets.
    * Le bit 0 correspond au bit de poids fort du premier octet.
    *
    * @param octets tableau d'octets
    * @param position position du bit à lire (à partir de 0)
    * @return 0 ou 1 selon la valeur du bit
    */

    private static int lireBit(byte[] octets, int position) {

        int indexOctet = position / 8;
        int positionDansOctet = position % 8;

        int masque = 1 << (7 - positionDansOctet);

        if ((octets[indexOctet] & masque) != 0) {
        return 1;
        
        } else {

        return 0;

        }
}

}
