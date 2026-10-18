package fr.iutfbleau.pif;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * La classe <code>TableCodesCanoniques</code> sert à fabriquer une table
 * de codes canoniques à partir d'une table de codes Huffman.
 *
 * <p>Idée :</p>
 * <ul>
 *   <li>Avec Huffman, chaque valeur (0..255) possède un code binaire.</li>
 *   <li>Pour passer en canonique, on ne garde pas les bits exacts,
 *       seulement la <strong>longueur</strong> de chaque code.</li>
 *   <li>Ensuite, on recrée des codes binaires dans un ordre fixe :
 *       d'abord par longueur, puis par valeur.</li>
 * </ul>
 *
 * <p>Intérêt :</p>
 * <ul>
 *   <li>La table est plus simple à stocker (valeur + longueur).</li>
 *   <li>Le lecteur peut reconstruire exactement les mêmes codes
 *       sans avoir besoin de stocker tous les bits.</li>
 * </ul>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

    public class TableCodesCanoniques {
        
    /**
     * Construit une table de codes canoniques (valeur -> code binaire)
     * à partir d'une table Huffman (valeur -> code binaire).
     *
     * <p>Étapes :</p>
     * <ol>
     *   <li>On récupère, pour chaque valeur, la longueur de son code Huffman.</li>
     *   <li>On trie les entrées par longueur puis par valeur.</li>
     *   <li>On attribue des codes canoniques dans cet ordre
     *       (en incrémentant et en décalant quand la longueur augmente).</li>
     * </ol>
     *
     * @param tableHuffman table des codes Huffman initiaux (valeur -> code binaire)
     * @return table des codes canoniques (valeur -> code binaire)
     */

    public static Map<Integer, String> construire(Map<Integer, String> tableHuffman) {

        List<EntreeCanonique> entrees = new ArrayList<EntreeCanonique>();

        // 1) On conserve valeur + longueur du code
        for (Map.Entry<Integer, String> entree : tableHuffman.entrySet()) {
            entrees.add(new EntreeCanonique(entree.getKey(), entree.getValue().length()));
        }

        // 2) Tri canonique : longueur puis valeur
        Collections.sort(entrees);

        // 3) Attribution des codes canoniques
        Map<Integer, String> tableCanonique = new HashMap<Integer, String>();

        int code = 0;
        int longueurPrecedente = entrees.get(0).getLongueur();

        for (int i = 0; i < entrees.size(); i++) {

            EntreeCanonique e = entrees.get(i);
            int longueurActuelle = e.getLongueur();

            // Décalage si la longueur augmente
            code = code << (longueurActuelle - longueurPrecedente);

            // Conversion en binaire avec padding
            String binaire = Integer.toBinaryString(code);
            while (binaire.length() < longueurActuelle) {
                binaire = "0" + binaire;
            }

            tableCanonique.put(e.getValeur(), binaire);

            code = code + 1;
            longueurPrecedente = longueurActuelle;
        }

        return tableCanonique;
    }

}
