package fr.iutfbleau.pif;

import java.util.Map;

/**
 * Classe utilitaire chargée de générer des représentations textuelles
 * des différentes tables utilisées dans le projet.
 *
 * <p>
 * Cette classe permet de transformer :
 * </p>
 * <ul>
 *   <li>une table de fréquences (valeur → fréquence),</li>
 *   <li>une table de codes (valeur → code binaire),</li>
 * </ul>
 *
 * <p>
 * en chaînes de caractères lisibles, destinées à être affichées
 * dans l’interface graphique (zones de texte Swing).
 * </p>
 *
 * <p>
 * Elle ne contient aucune logique de compression ou de calcul,
 * uniquement de la mise en forme.
 * </p>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class FormateurTexte {

    /**
     * Génère une représentation textuelle d’une table de fréquences.
     *
     * <p>
     * Chaque ligne associe une valeur (par exemple une composante RGB)
     * à son nombre d’occurrences dans l’image.
     * </p>
     *
     * @param tableFrequences table associant une valeur à sa fréquence
     * @param titre titre affiché en en-tête du tableau
     * @return une chaîne de caractères formatée
     */
    public static String frequences(Map<Integer, Integer> tableFrequences, String titre) {

        String texte = "";
        texte = texte + "=== " + titre + " ===\n";
        texte = texte + "Nombre de symboles distincts : " + tableFrequences.size() + "\n";
        texte = texte + "\n";
        texte = texte + "Valeur -> Frequence\n";
        texte = texte + "-------------------\n";

        for (Map.Entry<Integer, Integer> entree : tableFrequences.entrySet()) {
            texte = texte + entree.getKey() + " -> " + entree.getValue() + "\n";
        }

        return texte;
    }

    /**
     * Génère une représentation textuelle d’une table de codes binaires.
     *
     * <p>
     * Cette méthode est utilisée aussi bien pour :
     * </p>
     * <ul>
     *   <li>les codes Huffman initiaux,</li>
     *   <li>les codes canoniques.</li>
     * </ul>
     *
     * <p>
     * Chaque ligne associe une valeur à son code binaire.
     * </p>
     *
     * @param tableCodes table associant une valeur à un code binaire
     * @param titre titre affiché en en-tête du tableau
     * @return une chaîne de caractères formatée
     */
    public static String codes(Map<Integer, String> tableCodes, String titre) {

        String texte = "";
        texte = texte + "=== " + titre + " ===\n";
        texte = texte + "Nombre de codes : " + tableCodes.size() + "\n";
        texte = texte + "\n";
        texte = texte + "Valeur -> Code\n";
        texte = texte + "--------------\n";

        for (Map.Entry<Integer, String> entree : tableCodes.entrySet()) {
            texte = texte + entree.getKey() + " -> " + entree.getValue() + "\n";
        }

        return texte;
    }
}
