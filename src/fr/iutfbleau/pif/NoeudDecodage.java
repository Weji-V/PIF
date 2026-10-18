package fr.iutfbleau.pif;

/**
 * Représente un nœud d’un arbre de décodage Huffman.
 *
 * <p>Cet arbre est utilisé lors de la lecture d’un fichier PIF afin de
 * décoder un flux binaire et de reconstruire les valeurs originales
 * des composantes RGB.</p>
 *
 * <p>Un nœud peut être :</p>
 * <ul>
 *   <li>un nœud interne, possédant un fils gauche et/ou un fils droit ;</li>
 *   <li>une feuille, contenant une valeur décodée.</li>
 * </ul>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class NoeudDecodage {

    /**
     * Fils gauche du nœud.
     * <p>Correspond au bit 0 lors du décodage.</p>
     */
    private NoeudDecodage gauche;

    /**
     * Fils droit du nœud.
     * <p>Correspond au bit 1 lors du décodage.</p>
     */
    private NoeudDecodage droite;

    /**
     * Valeur associée au nœud.
     * <p>Cette valeur n’a de sens que si le nœud est une feuille.</p>
     */
    private int valeur;

    /**
     * Indique si le nœud est une feuille de l’arbre.
     */
    private boolean estFeuille;

    /**
     * Construit un nœud de décodage vide.
     *
     * <p>Par défaut, le nœud :</p>
     * <ul>
     *   <li>n’a pas de fils,</li>
     *   <li>ne contient aucune valeur significative,</li>
     *   <li>n’est pas une feuille.</li>
     * </ul>
     */
    public NoeudDecodage() {
        this.gauche = null;
        this.droite = null;
        this.valeur = 0;
        this.estFeuille = false;
    }

    /**
     * Renvoie le fils gauche du nœud.
     *
     * @return le nœud gauche, ou {@code null} s’il n’existe pas
     */
    public NoeudDecodage getGauche() {
        return this.gauche;
    }

    /**
     * Définit le fils gauche du nœud.
     *
     * @param gauche nœud à placer à gauche
     */
    public void setGauche(NoeudDecodage gauche) {
        this.gauche = gauche;
    }

    /**
     * Renvoie le fils droit du nœud.
     *
     * @return le nœud droit, ou {@code null} s’il n’existe pas
     */
    public NoeudDecodage getDroite() {
        return this.droite;
    }

    /**
     * Définit le fils droit du nœud.
     *
     * @param droite nœud à placer à droite
     */
    public void setDroite(NoeudDecodage droite) {
        this.droite = droite;
    }

    /**
     * Renvoie la valeur stockée dans le nœud.
     *
     * @return la valeur décodée
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Définit la valeur stockée dans le nœud.
     *
     * @param valeur valeur décodée à associer au nœud
     */
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    /**
     * Indique si le nœud est une feuille.
     *
     * @return {@code true} si le nœud est une feuille, {@code false} sinon
     */
    public boolean isEstFeuille() {
        return this.estFeuille;
    }

    /**
     * Définit si le nœud est une feuille.
     *
     * @param estFeuille {@code true} pour marquer le nœud comme feuille
     */
    public void setEstFeuille(boolean estFeuille) {
        this.estFeuille = estFeuille;
    }
}
