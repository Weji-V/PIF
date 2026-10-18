package fr.iutfbleau.pif;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

/**
 * La classe <code>ControleurVisualisateur</code> gère les interactions
 * de la souris pour permettre le déplacement d'une image affichée
 * dans un <code>JScrollPane</code>.
 *
 * Le déplacement se fait en maintenant le bouton gauche de la souris
 * et en la faisant glisser.
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class ControleurVisualisateur implements MouseListener, MouseMotionListener {

    /**
     * Panneau de défilement contenant l'image.
     */
    private JScrollPane panneauDefilement;

    /**
     * Composant affichant l'image.
     */
    private JLabel etiquetteImage;

    /**
     * Indique si l'utilisateur est en train de déplacer l'image.
     */
    private boolean glisser;

    /**
     * Dernière position connue de la souris (coordonnées écran).
     */
    private Point dernierPointEcran;

    /**
     * Construit le contrôleur de déplacement de l'image.
     *
     * @param panneauDefilement panneau de défilement
     * @param etiquetteImage composant contenant l'image
     */
    public ControleurVisualisateur(JScrollPane panneauDefilement, JLabel etiquetteImage) {
        this.panneauDefilement = panneauDefilement;
        this.etiquetteImage = etiquetteImage;
        this.glisser = false;
        this.dernierPointEcran = null;
    }

    /**
     * Détecte l'appui sur le bouton gauche de la souris
     * et initialise le déplacement.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            this.glisser = true;
            this.dernierPointEcran = e.getLocationOnScreen();
        }
    }

    /**
     * Arrête le déplacement lorsque le bouton de la souris est relâché.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        this.glisser = false;
        this.dernierPointEcran = null;
    }

    /**
     * Déplace la zone visible de l'image en fonction du mouvement
     * de la souris.
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (!this.glisser || this.dernierPointEcran == null) {
            return;
        }

        Point positionCourante = e.getLocationOnScreen();

        int deplacementX = positionCourante.x - this.dernierPointEcran.x;
        int deplacementY = positionCourante.y - this.dernierPointEcran.y;

        JViewport viewport = this.panneauDefilement.getViewport();
        Point positionVue = viewport.getViewPosition();

        positionVue.x = positionVue.x - deplacementX;
        positionVue.y = positionVue.y - deplacementY;

        int maxX = this.etiquetteImage.getWidth() - viewport.getWidth();
        int maxY = this.etiquetteImage.getHeight() - viewport.getHeight();

        if (maxX < 0) {
            maxX = 0;
        }

        if (maxY < 0) {
            maxY = 0;
        }

        if (positionVue.x < 0) {
            positionVue.x = 0;
        }

        if (positionVue.y < 0) {
            positionVue.y = 0;
        }

        if (positionVue.x > maxX) {
            positionVue.x = maxX;
        }

        if (positionVue.y > maxY) {
            positionVue.y = maxY;
        }

        viewport.setViewPosition(positionVue);
        this.dernierPointEcran = positionCourante;
    }

    /* Méthodes inutilisées mais obligatoires */

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseMoved(MouseEvent e) { }
}