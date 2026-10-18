package fr.iutfbleau.pif;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * La classe <code>Huffman</code> implémente l'algorithme de compression
 * de Huffman appliqué aux valeurs des pixels d'une image.
 * <p>
 * Elle permet de :
 * <ul>
 *   <li>analyser les pixels afin de construire une table de fréquences,</li>
 *   <li>construire un arbre binaire de Huffman à partir de ces fréquences,</li>
 *   <li>générer les codes binaires associés à chaque valeur,</li>
 *   <li>transformer ces codes en codes canoniques,</li>
 *   <li>encoder les pixels sous forme d’un flux binaire compressé.</li>
 * </ul>
 * <p>
 * Cette classe est utilisée par le programme de conversion afin de
 * produire les données compressées stockées dans le fichier PIF.
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

    public class Huffman {

    private Map<Integer, String> tableCodesHuffman;
    private Map<Integer, String> tableCodesCanoniques;


    /**
     * Table associant chaque valeur de pixel à sa fréquence d'apparition.
     */
    private Map<Integer, Integer> tableFrequences;

    /**
     * Table associant chaque valeur de pixel à son code binaire de Huffman.
     */
    private Map<Integer, String> tableCodes;

    /**
     * Racine de l'arbre de Huffman.
     */
    private Noeud racine;

    /**
     * Construit un objet Huffman vide.
     * <p>
     * Les structures internes seront initialisées lors des différentes
     * étapes de construction.
     */
    public Huffman() {
        this.tableCodesHuffman = new HashMap<Integer, String>();
        this.tableCodesCanoniques = new HashMap<Integer, String>();

        tableFrequences = new HashMap<Integer, Integer>();
        tableCodes = new HashMap<Integer, String>();
        racine = null;
    }

    /**
     * Construit la table des fréquences à partir des valeurs des pixels.
    *
    * Chaque valeur rencontrée dans le tableau est comptée afin de connaître
    * le nombre d'occurrences de chaque symbole.
    * Cette table est utilisée ensuite pour construire l'arbre de Huffman.
    *
    * @param pixels tableau contenant les valeurs à analyser
    */

    public void construireTableFrequences(int[] pixels) {
        tableFrequences.clear();

        for (int valeur : pixels) {
            if (tableFrequences.containsKey(valeur)) {
                tableFrequences.put(valeur, tableFrequences.get(valeur) + 1);
            } else {
                tableFrequences.put(valeur, 1);
            }
        }
    }

    /**
    * Construit l'arbre de Huffman à partir de la table des fréquences.
    *
    * Les nœuds sont organisés dans une file de priorité afin de toujours
    * combiner les deux nœuds de plus faible fréquence.
    * Le résultat est un arbre binaire optimisé pour le codage Huffman.
    */

    public void construireArbre() {

        PriorityQueue<Noeud> file = new PriorityQueue<Noeud>();

        for (Map.Entry<Integer, Integer> entree : tableFrequences.entrySet()) {
            file.add(new NoeudFeuille(entree.getKey(), entree.getValue()));
        }

        if (file.size() == 1) {
            Noeud unique = file.poll();
            racine = new NoeudInterne(unique, null);
            return;
        }

        while (file.size() > 1) {
            Noeud gauche = file.poll();
            Noeud droite = file.poll();

            NoeudInterne parent = new NoeudInterne(gauche, droite);
            file.add(parent);
        }

        racine = file.poll();
    }

    /**
    * Construit la table des codes Huffman à partir de l'arbre.
    *
    * Cette méthode lance le parcours récursif de l'arbre et enregistre
    * les codes binaires associés à chaque valeur.
    * Une copie est conservée afin de garder la version Huffman initiale.
    */

    public void construireTableCodes() {
        tableCodes.clear();
        construireCodesRecursif(racine, "");
    
        tableCodesHuffman.clear();
        tableCodesHuffman.putAll(tableCodes);
    }
    

    /**
    * Transforme la table de codes Huffman en table de codes canoniques.
    */
    public void rendreCodesCanoniques() {
        tableCodesCanoniques =
                TableCodesCanoniques.construire(tableCodesHuffman);
    
        // on encode avec les canoniques
        tableCodes = tableCodesCanoniques;
    }
    

/**
 * Renvoie la table des fréquences (valeur -> fréquence).
 *
 * @return la table des fréquences
 */
    public java.util.Map<Integer, Integer> getTableFrequences() {
    return this.tableFrequences;
}

    /**
    * Renvoie la table des codes actuellement utilisée pour l'encodage.
    *
    * Selon l'étape du traitement, il peut s'agir des codes Huffman
    * ou des codes canoniques.
    *
    * @return table associant chaque valeur à son code binaire
    */

    public java.util.Map<Integer, String> getTableCodes() {
    return this.tableCodes;
}

    /**
    * Parcourt récursivement l'arbre de Huffman pour générer les codes binaires.
    *
    * Lorsqu'un nœud feuille est atteint, le code binaire accumulé correspond
    * à la valeur stockée dans cette feuille.
    *
    * @param noeud nœud courant de l'arbre
    * @param codeActuel code binaire construit jusqu'à ce nœud
    */

    private void construireCodesRecursif(Noeud noeud, String codeActuel) {

        if (noeud == null) {
            return;
        }

        if (noeud instanceof NoeudFeuille) {
            NoeudFeuille feuille = (NoeudFeuille) noeud;

            String codeFinal;
            if (codeActuel.equals("")) {
                codeFinal = "0";
            } else {
                codeFinal = codeActuel;
            }

            tableCodes.put(feuille.valeur, codeFinal);
            return;
        }

        NoeudInterne interne = (NoeudInterne) noeud;

        if (interne.gauche != null) {
            construireCodesRecursif(interne.gauche, codeActuel + "0");
        }

        if (interne.droite != null) {
            construireCodesRecursif(interne.droite, codeActuel + "1");
        }
    }

    /**
    * Encode un tableau de pixels en une suite d’octets en utilisant les codes de Huffman.
    * <p>
    * Chaque pixel est remplacé par son code binaire associé, puis les bits sont
    * regroupés dans un tableau d’octets.
    *
    * @param pixels le tableau des pixels à encoder
    * @param nombreBits tableau de taille 1 permettant de stocker le nombre total de bits encodés
    * @return un tableau d’octets contenant les données compressées
    */
    public byte[] encoderPixelsEnOctets(int[] pixels, int[] nombreBits) {

    /**
     * Capacité initiale du tableau d’octets.
     * On prend une estimation large pour éviter des redimensionnements trop fréquents.
     */
    int capacite = pixels.length * 4;
    byte[] octets = new byte[capacite];

    /**
     * Index de l’octet courant dans le tableau.
     */
    int indexOctet = 0;

    /**
     * Position du bit courant dans l’octet (entre 0 et 7).
     */
    int positionBit = 0;

    /**
     * Compteur du nombre total de bits encodés.
     */
    int totalBits = 0;

    // Parcours de tous les pixels
    for (int i = 0; i < pixels.length; i = i + 1) {
        int valeur = pixels[i];

        /**
         * Code binaire associé à la valeur du pixel.
         */
        String code = tableCodes.get(valeur);

        // Parcours des bits du code binaire
        for (int j = 0; j < code.length(); j = j + 1) {

            /**
             * Si le bit courant vaut 1, on positionne le bit correspondant
             * dans l’octet courant.
             */
            if (code.charAt(j) == '1') {
                octets[indexOctet] =
                    (byte) (octets[indexOctet] | (1 << (7 - positionBit)));
            }

            positionBit = positionBit + 1;
            totalBits = totalBits + 1;

            /**
             * Lorsque l’octet est rempli (8 bits),
             * on passe à l’octet suivant.
             */
            if (positionBit == 8) {
                positionBit = 0;
                indexOctet = indexOctet + 1;

                /**
                 * Si le tableau d’octets est plein,
                 * on l’agrandit en doublant sa taille.
                 */
                if (indexOctet == octets.length) {
                    byte[] nouveau = new byte[octets.length * 2];
                    for (int k = 0; k < octets.length; k = k + 1) {
                        nouveau[k] = octets[k];
                    }
                    octets = nouveau;
                }
            }
        }
    }

    /**
     * Si le dernier octet n’est pas complètement rempli,
     * on le compte tout de même comme valide.
     */
    if (positionBit != 0) {
        indexOctet = indexOctet + 1;
    }

    /**
     * Création du tableau final contenant uniquement
     * les octets réellement utilisés.
     */
    byte[] resultat = new byte[indexOctet];
    for (int i = 0; i < indexOctet; i = i + 1) {
        resultat[i] = octets[i];
    }

    /**
     * Stockage du nombre total de bits encodés.
     */
    nombreBits[0] = totalBits;

    return resultat;
}
    
/**
 * Renvoie la table des codes Huffman initiaux,
 * construite directement à partir de l'arbre de Huffman
 * (avant la transformation canonique).
 *
 * @return table des codes Huffman (valeur -> code binaire)
 */
    public Map<Integer, String> getTableCodesHuffman() {
    return tableCodesHuffman;
}

/**
 * Renvoie la table des codes canoniques,
 * utilisée pour l'encodage final dans le fichier PIF.
 *
 * @return table des codes canoniques (valeur -> code binaire)
 */
    public Map<Integer, String> getTableCodesCanoniques() {
    return tableCodesCanoniques;
}

}
