package fr.iutfbleau.pif;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * La classe <code>FenetreVisualisateur</code> permet d'afficher une image
 * représentée par un objet <code>PIFImage</code> dans une fenêtre graphique.
 * <p>
 * La taille initiale de la fenêtre est adaptée à la taille de l'image, tout en
 * restant dans une taille maximale raisonnable (pour éviter de dépasser l'écran).
 * La fenêtre est redimensionnable.
 * <p>
 * Lorsque l'image est plus petite que la fenêtre, elle est centrée.
 * Lorsque l'image est plus grande que la fenêtre, elle est partiellement visible
 * et la portion visible peut être déplacée à la souris en maintenant le bouton
 * gauche enfoncé.
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class FenetreVisualisateur{

    /**
     * Affiche une image dans une fenêtre graphique.
     *
     * @param image l'image à afficher
     * @param titre le titre de la fenêtre
     */
    public static void afficher(PIFImage image, String titre) {

        int largeurImage = image.getLargeur();
        int hauteurImage = image.getHauteur();

        BufferedImage imageBuffer = new BufferedImage(largeurImage, hauteurImage, BufferedImage.TYPE_INT_ARGB);

        imageBuffer.setRGB(0, 0, largeurImage, hauteurImage, image.getPixels(), 0, largeurImage);

        JLabel etiquetteImage = new JLabel(new ImageIcon(imageBuffer));

        // Conteneur centré : si la fenêtre est plus grande que l'image, l'image reste au centre
        JPanel conteneurCentre = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.gridx = 0;
        contraintes.gridy = 0;
        conteneurCentre.add(etiquetteImage, contraintes);

        JScrollPane panneauDefilement = new JScrollPane(conteneurCentre);

        // Gestion du drag au clic gauche
        ControleurVisualisateur controleur = new ControleurVisualisateur(panneauDefilement, etiquetteImage);

        etiquetteImage.addMouseListener(controleur);
        etiquetteImage.addMouseMotionListener(controleur);

        JFrame fenetre = new JFrame(titre);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setContentPane(panneauDefilement);

        fenetre.pack();

        // Taille maximum raisonnable : 90% de l'écran
        Dimension ecran = Toolkit.getDefaultToolkit().getScreenSize();
        int largeurMax = (int) (ecran.width * 0.90);
        int hauteurMax = (int) (ecran.height * 0.90);

        if (fenetre.getWidth() > largeurMax || fenetre.getHeight() > hauteurMax) {
            fenetre.setSize(Math.min(fenetre.getWidth(), largeurMax), Math.min(fenetre.getHeight(), hauteurMax));
        } else {
            // rien
        }

        fenetre.setVisible(true);
    }
}
