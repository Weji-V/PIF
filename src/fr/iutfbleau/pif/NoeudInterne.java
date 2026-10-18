package fr.iutfbleau.pif;

/**
 * La classe <code>NoeudInterne</code> représente un nœud interne
 * de l'arbre de Huffman.
 * <p>
 * Un nœud interne relie deux sous-arbres (gauche et droit) et sa
 * fréquence correspond à la somme des fréquences de ses enfants.
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class NoeudInterne extends Noeud {

    /**
     * Sous-arbre gauche du nœud.
     */
    protected Noeud gauche;

    /**
     * Sous-arbre droit du nœud.
     */
    protected Noeud droite;

    /**
     * Construit un nœud interne à partir de deux nœuds enfants.
     * <p>
     * La fréquence du nœud interne est calculée comme la somme
     * des fréquences de ses enfants.
     *
     * @param gauche le sous-arbre gauche
     * @param droite le sous-arbre droit
     */

    public NoeudInterne(Noeud gauche, Noeud droite) {
        super(0);

        if (gauche != null && droite != null) {
            this.frequence = gauche.frequence + droite.frequence;
        } else if (gauche != null) {
            this.frequence = gauche.frequence;
        } else if (droite != null) {
            this.frequence = droite.frequence;
        } else {
            this.frequence = 0;
        }

        this.gauche = gauche;
        this.droite = droite;
    }
}
