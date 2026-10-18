package fr.iutfbleau.pif;

/**
 * La classe abstraite <code>Noeud</code> représente un nœud générique
 * de l’arbre de Huffman.
 *
 * <p>Chaque nœud possède une fréquence d’apparition, utilisée lors de la
 * construction de l’arbre afin d’ordonner les nœuds.</p>
 *
 * <p>Cette classe est la super-classe de :</p>
 * <ul>
 *   <li>{@link NoeudFeuille} : nœud contenant une valeur réelle (pixel)</li>
 *   <li>{@link NoeudInterne} : nœud intermédiaire reliant deux sous-arbres</li>
 * </ul>
 *
 * <p>Les nœuds sont comparables entre eux afin de pouvoir être stockés
 * dans une file de priorité lors de l’algorithme de Huffman.</p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public abstract class Noeud implements Comparable<Noeud> {

    /**
     * Fréquence d’apparition associée au nœud.
     */
    protected int frequence;

    /**
     * Construit un nœud avec une fréquence donnée.
     *
     * <p>Si la fréquence fournie est négative, elle est ramenée à 0
     * afin de garantir une valeur valide.</p>
     *
     * @param frequence fréquence associée au nœud
     */
    public Noeud(int frequence) {
        if (frequence >= 0) {
            this.frequence = frequence;
        } else {
            this.frequence = 0;
        }
    }

    /**
     * Compare deux nœuds selon leur fréquence.
     *
     * <p>Cette méthode permet d’ordonner les nœuds dans une file de priorité,
     * ce qui est indispensable pour la construction de l’arbre de Huffman.</p>
     *
     * @param autre le nœud à comparer
     * @return un entier négatif, nul ou positif selon l’ordre des fréquences
     */
    @Override
    public int compareTo(Noeud autre) {
        if (this.frequence < autre.frequence) {
            return -1;
        } else if (this.frequence > autre.frequence) {
            return 1;
        } else {
            return 0;
        }
    }
}
