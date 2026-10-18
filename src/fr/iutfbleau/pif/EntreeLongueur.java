package fr.iutfbleau.pif;

/**
 * Cette classe représente une association entre :<br>
 * - une valeur (ex : un niveau de couleur)<br>
 * - la longueur du code binaire qui lui est associée
 * <br>
 * Elle est utilisée lors de la reconstruction des codes canoniques,
 * quand on connaît uniquement les longueurs des codes.
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class EntreeLongueur implements Comparable<EntreeLongueur> {

    /**
     * Valeur associée au code (ex : intensité d’un pixel).
     */
    private int valeur;

    /**
     * Longueur du code binaire associé à la valeur.
     */
    private int longueur;

    /**
     * Construit une entrée avec une valeur et la longueur de son code.
     *
     * @param valeur la valeur représentée
     * @param longueur la longueur du code binaire
     */
    public EntreeLongueur(int valeur, int longueur) {
        this.valeur = valeur;
        this.longueur = longueur;
    }

    /**
     * Renvoie la valeur associée.
     *
     * @return la valeur
     */
    public int getValeur() {
        return this.valeur;
    }

    /**
     * Renvoie la longueur du code binaire.
     *
     * @return la longueur du code
     */
    public int getLongueur() {
        return this.longueur;
    }

    /**
     * Compare deux entrées pour les trier.
     * - d’abord par longueur de code
     * - puis par valeur si les longueurs sont identiques
     *
     * @param autre l’autre entrée à comparer
     * @return un entier négatif, nul ou positif selon l’ordre
     */
    @Override
    public int compareTo(EntreeLongueur autre) {
        if (this.longueur < autre.longueur) {
            return -1;
        } else {
            if (this.longueur > autre.longueur) {
                return 1;
            } else {
                if (this.valeur < autre.valeur) {
                    return -1;
                } else {
                    if (this.valeur > autre.valeur) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}