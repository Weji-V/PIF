package fr.iutfbleau.pif;

/**
 * La classe <code>NoeudFeuille</code> représente une feuille
 * dans un arbre de Huffman.
 *
 * <p>Une feuille correspond à une valeur concrète (par exemple
 * une composante de pixel) associée à une fréquence d'apparition.
 * Elle ne possède pas d'enfants.</p>
 *
 * <p>Les feuilles sont utilisées comme points de départ lors
 * de la construction de l'arbre de Huffman.</p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class NoeudFeuille extends Noeud {

    /**
     * Valeur portée par la feuille.
     * Il s'agit du symbole à encoder (par exemple une valeur RGB).
     */
    protected int valeur;

    /**
     * Construit une feuille de l'arbre de Huffman.
     *
     * <p>La fréquence représente le nombre d'occurrences de la valeur
     * dans les données à compresser.</p>
     *
     * @param valeur valeur du symbole (pixel ou composante)
     * @param frequence fréquence d'apparition de cette valeur
     */
    public NoeudFeuille(int valeur, int frequence) {
        super(0);

        if (frequence >= 0) {
            this.frequence = frequence;
        } else {
            this.frequence = 0;
        }

        this.valeur = valeur;
    }
}
