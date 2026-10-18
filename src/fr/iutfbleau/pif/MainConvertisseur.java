package fr.iutfbleau.pif;

import java.io.File;

/**
 * Le programme <code>MainConvertisseur</code> permet de convertir une image
 * classique (PNG, JPG, etc.) en un fichier au format <code>.pif</code>.
 *
 * Le programme réalise les étapes suivantes :
 * <ul>
 *   <li>sélection d’un fichier image (argument ou JFileChooser),</li>
 *   <li>lecture de l’image et récupération des pixels,</li>
 *   <li>séparation des composantes Rouge / Vert / Bleu,</li>
 *   <li>compression Huffman séparée sur chaque composante,</li>
 *   <li>génération des codes canoniques,</li>
 *   <li>affichage d’une fenêtre Swing (image + tables),</li>
 *   <li>écriture du fichier PIF dans un chemin de sortie.</li>
 * </ul>
 * 
 * @version 1.0
 * @author Aylane Sehl
 * @author Jenson Val
 * @author Séri-khane Yolou
 */

public class MainConvertisseur {

    /**
    * Point d’entrée du convertisseur PIF.
    * <p>
    * Si un chemin d’image est fourni en argument, il est utilisé comme fichier
    * d’entrée. Sinon, une fenêtre de sélection de fichier est ouverte.
    *
    * Le fichier PIF de sortie est automatiquement créé dans le dossier
    * <code>res</code> du projet, qui est généré s’il n’existe pas.
    *
    * @param args arguments de la ligne de commande
    */

    public static void main(String[] args) {

        try {
            /* ===============================
             * 1) Sélection du fichier d'entrée
             * =============================== */
            File fichierEntree = SelecteurFichier.getFichier(args);

            if (fichierEntree == null) {
                System.out.println("Aucun fichier sélectionné.");
                return;
            }

            /* ===============================
             * 2) Chargement de l'image en mémoire
             * =============================== */
            PIFImage image = PIFImage.chargerDepuisImage(fichierEntree.getAbsolutePath());

            System.out.println("Image chargée : " + fichierEntree.getName());
            System.out.println("Dimensions : " + image.getLargeur() + " x " + image.getHauteur());

            /* ===============================
             * 3) Extraction des composantes RGB
             * ===============================
             * On sépare les pixels en 3 tableaux :
             * - un tableau pour le Rouge (0..255)
             * - un tableau pour le Vert  (0..255)
             * - un tableau pour le Bleu  (0..255)
             */
            int[] pixels = image.getPixels();

            int[] pixelsRouge = CodageRGB.extraireRouge(pixels);
            int[] pixelsVert  = CodageRGB.extraireVert(pixels);
            int[] pixelsBleu  = CodageRGB.extraireBleu(pixels);

            /* ===============================
             * 4) Compression Huffman (R, V, B)
             * ===============================
             * Chaque composante est compressée séparément :
             * - table de fréquences
             * - arbre de Huffman
             * - codes Huffman
             * - transformation en codes canoniques
             */
            Huffman huffmanRouge = new Huffman();
            Huffman huffmanVert  = new Huffman();
            Huffman huffmanBleu  = new Huffman();

            huffmanRouge.construireTableFrequences(pixelsRouge);
            huffmanVert.construireTableFrequences(pixelsVert);
            huffmanBleu.construireTableFrequences(pixelsBleu);

            huffmanRouge.construireArbre();
            huffmanVert.construireArbre();
            huffmanBleu.construireArbre();

            huffmanRouge.construireTableCodes();
            huffmanVert.construireTableCodes();
            huffmanBleu.construireTableCodes();

            huffmanRouge.rendreCodesCanoniques();
            huffmanVert.rendreCodesCanoniques();
            huffmanBleu.rendreCodesCanoniques();

            /* ===============================
             * 5) Affichage des informations (Swing)
             * ===============================
             * La fenêtre montre :
             * - l'image à convertir
             * - la table des fréquences
             * - la table Huffman initiale
             * - la table canonique finale
             */
            new FenetreConvertisseur(image, huffmanRouge, huffmanVert, huffmanBleu);

            /* ===============================
             * 6) Détermination du chemin de sortie
             * ===============================
             * - si l'utilisateur donne args[1], on l'utilise (et on force .pif)
             * - sinon on génère automatiquement un chemin dans le dossier res/
             */
            String cheminSortie;

            if (args.length >= 2) {
                // Sortie imposée par l'utilisateur
                cheminSortie = GestionSortiePIF.forcerExtensionPIF(args[1]);
            
            } else if (args.length == 1) {
                // 1 seul argument → sortie auto dans res/output/
                cheminSortie = GestionSortiePIF.cheminSortieAutoDansResSortie(fichierEntree);
            
            } else {
                // Aucun argument → on demande où enregistrer
                String nomBase = fichierEntree.getName();
                int point = nomBase.lastIndexOf('.');
                if (point != -1) {
                    nomBase = nomBase.substring(0, point);
                }
            
                cheminSortie = GestionSortiePIF.choisirCheminSortieAvecFenetre(nomBase + ".pif");
            
                if (cheminSortie == null) {
                    System.out.println("Enregistrement annulé.");
                    return;
                }
            }
            

            /* ===============================
             * 7) Encodage des composantes en octets
             * ===============================
             * Chaque composante produit :
             * - un tableau d’octets compressé
             * - un nombre total de bits utiles (pour ignorer le padding)
             */
            int[] nbBitsR = new int[1];
            int[] nbBitsV = new int[1];
            int[] nbBitsB = new int[1];

            byte[] octetsR = huffmanRouge.encoderPixelsEnOctets(pixelsRouge, nbBitsR);
            byte[] octetsV = huffmanVert.encoderPixelsEnOctets(pixelsVert, nbBitsV);
            byte[] octetsB = huffmanBleu.encoderPixelsEnOctets(pixelsBleu, nbBitsB);

            /* ===============================
             * 8) Écriture du fichier PIF
             * ===============================
             * On écrit :
             * - les dimensions
             * - les tables canoniques (R, V, B) sous forme (valeur, longueur)
             * - les 3 flux compressés (bits + octets)
             */
            EcrivainPIF.ecrire(cheminSortie, image, huffmanRouge.getTableCodesCanoniques(), huffmanVert.getTableCodesCanoniques(), huffmanBleu.getTableCodesCanoniques(), nbBitsR[0], nbBitsV[0], nbBitsB[0], octetsR, octetsV, octetsB);

            System.out.println("Conversion terminée : " + cheminSortie);

        } catch (Exception e) {
            System.err.println("Erreur lors de la conversion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
