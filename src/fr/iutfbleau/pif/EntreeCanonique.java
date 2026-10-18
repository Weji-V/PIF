package fr.iutfbleau.pif;

/**
 * Représente une entrée de la table des codes canoniques.
 * Une entrée associe une valeur à la longueur de son code binaire.
 *
 * Cette classe est utilisée pour trier les symboles avant
 * la génération des codes canoniques.
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class EntreeCanonique implements Comparable<EntreeCanonique> {

    /** Valeur du symbole */
    private int valeur;

    /** Longueur du code associé */
    private int longueur;

    /**
     * Construit une entrée canonique.
     *
     * @param valeur valeur du symbole
     * @param longueur longueur du code Huffman
     */
    public EntreeCanonique(int valeur, int longueur) {
        this.valeur = valeur;
        this.longueur = longueur;
    }

    /**
     * Renvoie la valeur du symbole.
     *
     * @return valeur du symbole
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Renvoie la longueur du code.
     *
     * @return longueur du code
     */
    public int getLongueur() {
        return this.longueur;
    }

    /**
     * Compare deux entrées canoniques :
     * - d'abord par longueur de code
     * - puis par valeur si les longueurs sont identiques
     *
     * @param autre autre entrée à comparer
     * @return ordre de comparaison
     */
    @Override
    public int compareTo(EntreeCanonique autre) {
        if (this.longueur < autre.longueur) {
            return -1;
        } else if (this.longueur > autre.longueur) {
            return 1;
        } else {
            return Integer.compare(this.valeur, autre.valeur);
        }
    }
}