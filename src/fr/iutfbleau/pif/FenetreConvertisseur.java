package fr.iutfbleau.pif;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Fenêtre graphique du convertisseur PIF.
 *
 * <p>
 * Cette fenêtre affiche :
 * </p>
 * <ul>
 *   <li>à gauche : l’image à convertir (redimensionnée si nécessaire),</li>
 *   <li>à droite : les tables de fréquences, de codes Huffman
 *       et de codes canoniques, organisées sous forme d’onglets.</li>
 * </ul>
 *
 * <p>
 * Cette classe est responsable uniquement de l’affichage graphique.
 * Elle ne réalise aucun calcul de compression.
 * </p>
 *
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class FenetreConvertisseur extends JFrame {

    /**
     * Construit la fenêtre du convertisseur et initialise
     * tous les composants graphiques.
     *
     * @param image image à convertir
     * @param hR objet Huffman associé à la composante rouge
     * @param hV objet Huffman associé à la composante verte
     * @param hB objet Huffman associé à la composante bleue
     */

    public FenetreConvertisseur(PIFImage image, Huffman hR, Huffman hV, Huffman hB) {

        super("Convertisseur PIF");

        /* =============================
         * Image affichée à gauche
         * ============================= */

        // Construction de l'image RGB puis réduction si nécessaire
        Image imageSwing = OutilsImageSwing.reduireSiNecessaire(OutilsImageSwing.construireImageRGB(image));

        JLabel etiquetteImage = new JLabel(new ImageIcon(imageSwing));

        /* =============================
         * Onglets à droite
         * ============================= */

        JTabbedPane ongletsPrincipaux = new JTabbedPane();

        /* ----- Onglet Fréquences ----- */
        JTabbedPane ongletsFrequences = new JTabbedPane();
        ongletsFrequences.add("Rouge",creerZoneTexte(FormateurTexte.frequences(hR.getTableFrequences(),"Fréquences - Rouge")));
        ongletsFrequences.add("Vert",creerZoneTexte(FormateurTexte.frequences(hV.getTableFrequences(),"Fréquences - Vert")));
        ongletsFrequences.add("Bleu",creerZoneTexte(FormateurTexte.frequences(hB.getTableFrequences(),"Fréquences - Bleu")));
        ongletsPrincipaux.add("Fréquences", ongletsFrequences);

        /* ----- Onglet Codes Huffman ----- */
        JTabbedPane ongletsHuffman = new JTabbedPane();
        ongletsHuffman.add("Rouge",creerZoneTexte(FormateurTexte.codes(hR.getTableCodesHuffman(),"Codes Huffman - Rouge")));
        ongletsHuffman.add("Vert",creerZoneTexte(FormateurTexte.codes(hV.getTableCodesHuffman(),"Codes Huffman - Vert")));
        ongletsHuffman.add("Bleu",creerZoneTexte(FormateurTexte.codes(hB.getTableCodesHuffman(),"Codes Huffman - Bleu")));
        ongletsPrincipaux.add("Codes Huffman", ongletsHuffman);

        /* ----- Onglet Codes canoniques ----- */
        JTabbedPane ongletsCanoniques = new JTabbedPane();
        ongletsCanoniques.add("Rouge",creerZoneTexte(FormateurTexte.codes(hR.getTableCodesCanoniques(),"Codes canoniques - Rouge")));
        ongletsCanoniques.add("Vert",creerZoneTexte(FormateurTexte.codes(hV.getTableCodesCanoniques(),"Codes canoniques - Vert")));
        ongletsCanoniques.add("Bleu",creerZoneTexte(FormateurTexte.codes(hB.getTableCodesCanoniques(),"Codes canoniques - Bleu")));
        ongletsPrincipaux.add("Codes canoniques", ongletsCanoniques);

        /* =============================
         * Séparation gauche / droite
         * ============================= */

        JPanel panneauImage = new JPanel(new BorderLayout());
        panneauImage.add(etiquetteImage, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panneauImage,ongletsPrincipaux);
        split.setResizeWeight(0.7);
        split.setDividerLocation(800);

        /* =============================
         * Paramètres de la fenêtre
         * ============================= */

        this.setLayout(new BorderLayout());
        this.add(split, BorderLayout.CENTER);

        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setVisible(true);
    }

    /**
     * Crée une zone de texte non modifiable pour afficher des tableaux.
     *
     * <p>
     * Une police monospace est utilisée afin d’aligner correctement
     * les colonnes (valeur → fréquence ou valeur → code).
     * </p>
     *
     * @param texte contenu à afficher
     * @return un composant JScrollPane contenant la zone de texte
     */

    private JScrollPane creerZoneTexte(String texte) {

        JTextArea zone = new JTextArea();
        zone.setEditable(false);
        zone.setFont(new Font("Monospaced", Font.PLAIN, 12));
        zone.setText(texte);
        zone.setCaretPosition(0);

        return new JScrollPane(zone);
    }
}
