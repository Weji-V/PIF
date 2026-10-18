package fr.iutfbleau.pif;

import java.io.File;
import javax.swing.JFileChooser;

/**
 * La classe <code>SelecteurFichier</code> permet de choisir un fichier
 * soit à partir des arguments de la ligne de commande,
 * soit à l'aide d'une fenêtre graphique.
 *
 * <p>Elle est utilisée par les classes principales du projet
 * afin de centraliser la logique de sélection de fichiers
 * et d'éviter de dupliquer ce code.</p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class SelecteurFichier {

    /**
     * Retourne un fichier sélectionné par l'utilisateur.
     *
     * <p>Deux cas sont possibles :</p>
     * <ul>
     *   <li>Si un chemin est fourni en argument, ce fichier est utilisé.</li>
     *   <li>Sinon, une fenêtre graphique s'ouvre pour permettre à
     *       l'utilisateur de choisir un fichier.</li>
     * </ul>
     *
     * @param args arguments de la ligne de commande
     * @return le fichier sélectionné, ou <code>null</code> si aucun fichier
     *         valide n'a été choisi
     */
    public static File getFichier(String[] args) {

        File fichier = null;

        // Cas où un chemin est fourni en argument
        if (args.length > 0) {
            fichier = new File(args[0]);

            if (!fichier.exists() || !fichier.isFile()) {
                System.err.println("Le chemin fourni n'est pas valide : " + args[0]);
                return null;
            }

        } else {
            // Cas où aucun argument n'est fourni : ouverture d'une fenêtre
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Sélectionnez un fichier");

            int resultat = chooser.showOpenDialog(null);

            if (resultat == JFileChooser.APPROVE_OPTION) {
                fichier = chooser.getSelectedFile();
            } else {
                return null;
            }
        }

        return fichier;
    }
}
