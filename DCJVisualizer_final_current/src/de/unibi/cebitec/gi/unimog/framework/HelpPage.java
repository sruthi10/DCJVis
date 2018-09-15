package de.unibi.cebitec.gi.unimog.framework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/***************************************************************************
 *   Copyright (C) 2010 by Rolf Hilker                                     *
 *   rhilker   a t  cebitec.uni-bielefeld.de                               *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
/**
 * @author -Rolf Hilker-
 * 
 * Provides the content of the help page and the "page" itself.
 * It is designed as a JTextPane with text and images.
 */
public class HelpPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private final Dimension standardSize = new Dimension(800, 600);
    private final String newline = "\n";
    private final static String folder = "/helpImages/";
    private final String[] iconPaths = {HelpPage.folder.concat("table1.jpg"),
        HelpPage.folder.concat("result1.jpg"), HelpPage.folder.concat("result2.jpg"),
        HelpPage.folder.concat("result3.jpg"), HelpPage.folder.concat("load.jpg"),
        HelpPage.folder.concat("example.jpg"), HelpPage.folder.concat("scenario.jpg"),
        HelpPage.folder.concat("run.jpg"), HelpPage.folder.concat("genomes.jpg"),
        HelpPage.folder.concat("adj.jpg"), HelpPage.folder.concat("graphic.jpg"),
        HelpPage.folder.concat("steps.jpg"), HelpPage.folder.concat("colors.jpg"),
        HelpPage.folder.concat("slider.jpg"), HelpPage.folder.concat("save.jpg"),
        HelpPage.folder.concat("save2.jpg"), HelpPage.folder.concat("clear.jpg"),
        HelpPage.folder.concat("exit.jpg"), HelpPage.folder.concat("help.jpg")};

    public HelpPage() {
        this.createContent();
    }

    /**
     * Creates the content (text and images) of the pane.
     */
    private void createContent() {

        final String space = "     ";
        final String space2 = "        ";
        final String space3 = "       ";
        String[] initString = {          
            "Startup possibilities".concat(this.newline).concat(this.newline),
            "UniMoG can be started in console modus or with a graphical user interface.".
            concat(this.newline).
            concat("For using the console modus two parameters have to be passed:").
            concat(this.newline).
            concat("-  At first the scenario: 1 = DCJ, 2 = rDCJ, 3 = HP, 4 = Inversion, 5 = Translocatin, 6 = All scenarios at once").
            concat(this.newline).
            concat("-  Second the path(s) to one or more files containing all genomes which should be analyzed").
            concat(this.newline).
            concat("-  Example: java -jar UniMoG 5 path\\to\\genomefile.txt path\\to\\genomefile2.txt").
            concat(this.newline).
            concat("-  If no correct parameters are passed, UniMoG starts with the graphical user interface").
            concat(this.newline).concat(this.newline),
            "Genome Representation".
            concat(this.newline).concat(this.newline), //bold
            "The loaded files have to contain at least two, but can contain several genomes.".
            concat(this.newline).
            concat("Every genome starts with a > followed by the genome name.").
            concat(this.newline).
            concat("In the following line(s) come the genes. They are separated by whitespaces. 'Newlines' do not matter.").
            concat(this.newline).
            concat("Each chromosome is concluded by ) or |. A ) concludes a circular chromosome, a | concludes a linear chromosome "
                + "and can be omitted for"). 
            concat(this.newline).
            concat("the last chromosome of a genome.").
            concat(this.newline).
            concat("For obvious reasons the gene names can contain all signs except whitespaces. A '-' before a gene name means that "
                + "the gene direction is backwards.").
            concat(this.newline).
            concat("Each pairwise comparison only takes genes into account that occur in both genomes of the pair.").
            concat(this.newline).
            concat("Every line starting with '//' has no function and is a comment. Everything before the first genome is a comment "
                + "and leading whitespaces of a ").
            concat(this.newline).
            concat("genome name are ignored, too.").
            concat(this.newline).concat(this.newline).
            concat("As an input example consider the three genomes:").
            concat(this.newline).concat(this.newline), //regular
            " ", //table1 image
            this.newline.concat(this.newline).
            concat("Optimal DCJ, rDCJ, HP, Inversion and Translocation Distance and Sorting").
            concat(this.newline).concat(this.newline), //bold
            "The program can compute five different genomic distances and optimal sorting scenarios: The DCJ, rDCJ, HP "
                + "(Hannenhalli & Pevzner), Inversion".
            concat(this.newline).
            concat("and Translocation distance and an optimal corresponding sorting. All five distance and sorting ").
            concat("algorithms were implemented under the ").
            concat(this.newline).
            concat("aspect of efficiency and feature the most efficient algorithms known until today. Links to the ").
            concat("corresponding papers can be found at the end ").
            concat(this.newline).
            concat("of this section.").
            concat(this.newline).concat(this.newline), //regular
            "Output example:", //underlined
            this.newline.
            concat("The output consists of the three tabs 'Genomes', 'Adjacencies' and 'Graphics'. ").
            concat(this.newline).
            concat("The 'Genomes' tab either starts with informative messages, if a genome comparison fails or singleton genes have to be removed from "). 
            concat(this.newline).
            concat("a genome, or directly starts with the distance results as a distance matrix, if the input consists of more than two genomes. ").
            concat(this.newline). 
            concat("Otherwise the distances are returned in their context:").
            concat(this.newline).concat(this.newline), //regular
            " ", //result1 image
            this.newline.concat(this.newline).
            concat("or if only Genome B and Genome C are compared with the HP model:").
            concat(this.newline).concat(this.newline), //regular
            " ", //result2 image 
            this.newline.concat(this.newline).
            concat("A '-' in the distance results means that the distances for this pair could not be calculated. "
                + "Certain rules for the different distance models, ").
            concat(this.newline).
            concat("explained below under 'Rules', have to be obeyed. ").
            concat(this.newline).concat(this.newline).
            concat("Next, the distance results are also returned as a matrix in PHYLIP format for further analysis.").
            concat(this.newline).
            concat("Below the matrices the single steps of each genome comparison are listed as plain text. ").
            concat(this.newline).concat(this.newline).
            concat("The 'Adjacencies' tab contains the adjacencies of each single step of each genome comparison. ").
            concat(this.newline).
            concat("The 'Graphics' tab shows the graphical output explained below. We recommend to check this tab for closer analysis.").
            concat(this.newline).concat(this.newline).
            concat("The graphical output will look like this for each pairwise comparison:").
            concat(this.newline).concat(this.newline), // regular
            " ", //result3 image
            this.newline.concat(this.newline).concat("The fragments emerging from the two cuts (red vertical ticks in the chromosome) of each step are highlighted by colored lines, ").concat(
            "each with a " + this.newline + "different color. The lines for the current step are located below the chromosome, while the lines depicting the fragments of the last step are " + this.newline).concat(
            "located above the chromosome of the current step.").concat(this.newline).concat(this.newline).concat("Detailed explanations of the DCJ model and how it can be used for ").concat(
            "calculating the rDCJ, HP, inversion and translocation distances are given in " + this.newline + "the following papers:").concat(this.newline).concat(this.newline).concat("•  The DCJ model is explicated in [13] and improved and simplified in [3].").concat(
            this.newline).concat("•  The restricted DCJ model is introduced in [9]").concat(
            this.newline).concat("•  How DCJ serves as a basis for HP, inversion and translocation distances is explained in [4], while [5] provides the correct distance " + this.newline + space + "formula. The running time of all ").concat(
            "distance computations is thus linear.").concat(this.newline).concat(this.newline).concat("The other models and the sorting algorithms are described here:").concat(
            this.newline).concat(this.newline).concat("•  The HP model was first introduced in [6] and further studied in [10]. The sorting algorithm used in this implementation is composed of: " + this.newline + space + "[12] as a basis for the preprocessing ").concat(
            "including the correction provided by [8]. As suggested in [12] the sorting itself is then carried out by " + this.newline + space + "the inversion sorting algorithm referred to in the next item. Therefore, ").concat(
            "the sorting process runs in quadratic time, which is determined by the " + this.newline + space + "used inversion sorting algorithm.").concat(this.newline).concat(
            "•  The inversion model was first solved in polynomial time in [7] and the most efficient, subquadratic sorting algorithm is described in [11]. " + this.newline + space + "Here, we implement the second most efficient quadratic time algorithm, also described in [11], with the aid of the data structures from [1].").concat(
            this.newline).concat("•  The translocation model and the implemented cubic sorting algorithm are described in [2]. This algorithm was chosen because in " + this.newline + space + "practice its running time is almost always linear.").concat(
            this.newline).concat(this.newline), //regular
            "Rules".concat(this.newline).concat(this.newline), //bold
            "For a successful genome comparison the following rules have to be obeyed:".concat(this.newline).concat("•  The DCJ distance calculation can handle all kinds of genomes without restrictions.").concat(
            this.newline).concat("•  The restricted DCJ model works for linear chromosomes and directly reincoporates all emerging ").concat(this.newline).concat(space).concat("circular chromosomes in the next step (modelling block interchanges and transpositions).").concat(
            this.newline).concat("•  The HP distance calculation requires linear chromosomes.").concat(this.newline).concat("•  The translocation distance requires linear chromosomes with the same telomeres. ").concat(
            this.newline).concat("•  The inversion distance requires one linear chromosome with the same telomeres in both genomes.").concat(this.newline).concat(this.newline), // regular
            "Program Usage - Step by Step".concat(this.newline).concat(this.newline), //bold
            "•  First click on ", //regular
            " ", //load icon
            " and choose a file that contains at least two genomes. Or alternatively click ", //regular
            " ", //example icon
            " to work with a " + this.newline + space + "small example file.".concat(this.newline).concat("•  Then choose the genomes to compare in the lists below and select one of the six possible scenarios. " + this.newline + space), //regular
            " ", // scenario icon
            this.newline.concat(space + "The 'All' scenario means that all four scenarios are calculated at once, if applicable.").concat(this.newline).concat(this.newline).concat("•  By pressing the "), //regular
            " ", // run button
            " button the genomes will be compared. The results are shown in the tabs in the lower part of the window.".concat(this.newline).concat("   o  The first tab "), // regular 
            " ", // genomes tab
            " first shows the distance in a matrix for more than two genomes or otherwise in context with the chosen " + this.newline + space2 + "scenario and afterwards the stepwise transformation for all pairs of genomes.".concat(
            this.newline).concat("   o  The second tab "),
            " ", // adj tab
            " shows the adjacencies (junctions) between the genes. The ending '_h' represents the head of a " + this.newline + space2 + "gene and '_t' the tail of a gene. Chromosomes are separated by '{content1}  ,  {content2}' ".concat(
            "including the whitespaces for an easier " + this.newline + space2 + "localization of a chromosome end.").concat(this.newline).concat("   o  The third tab "), //regular
            " ", // graphic tab
            " contains a graphical representation of the transformation of all pairs of genomes with highlighted " + this.newline + space2 + "operations.".concat(this.newline).concat(
            "•  Before running the comparison:").concat(this.newline).concat("   o  You can decide if only the distances are of interest and the sorting scenarios are not needed (makes the calculation also much faster). " + this.newline).concat(
            space2 + "In this case deselect the "), //regular 
            " ", // show steps image
            " checkbox, otherwise keep it selected.".concat(this.newline).concat("   o  You can choose a coloring method for the graphic:").concat(this.newline + space2).concat("If you select "), //regular
            " ", //colors image
            " each chromosome has a distinct color, otherwise they have a white background.".concat(this.newline).concat("•  You can also adjust the size of the ").concat(
            "output using the size slider with three different sizes."), // regular
            " ", //slider image
            this.newline.concat("•  A graphic result can be saved as jpg image by clicking on "), //regular
            " ", //save image
            this.newline.concat("•  The whole textual results can be saved in a txt file by clicking on "), //regular
            " ", // save2 image
            this.newline.concat("•  All values and results can be cleared by the "), // regular
            " ", // clear image
            "button.".concat(this.newline).concat("•  The "), //regular
            " ", // exit image
            " button terminates the program.".concat(this.newline).concat("•  If you want to look at the help directly from the program, click on "),
            " ", // help image
            this.newline.concat(this.newline).concat("References"), //bold
            this.newline.concat(this.newline).concat("1.    A. Bergeron, J. Mixtacki, and J. Stoye, \"The inversion distance problem.,\" Mathematics of Evolution and Phylogeny, O. Gascuel ed., " + this.newline + space3 + "Oxford University Press, 2005, pp. 262–290.").concat(this.newline).concat(
            "2.    A. Bergeron, J. Mixtacki, and J. Stoye, \"On sorting by translocations.,\" Journal of Computational Biology,  vol. 13(2), 2006, pp. 567–578.)").concat(this.newline).concat(
            "3.    A. Bergeron, J. Mixtacki, and J. Stoye, \"A Unifying View of Genome Rearrangements,\" Algorithms in Bioinformatics, Lecture Notes in " + this.newline + space3 + "Computer Science 4175, Springer Berlin / Heidelberg, 2006, pp. 163-173.").concat(this.newline).concat(
            "4.    A. Bergeron, J. Mixtacki, and J. Stoye, \"HP distance via double cut and join distance. ,\" In Proceedings of the 14th Annual Symposium on " + this.newline + space3 + "Combinatorial Pattern Matching (CPM 2008), Springer Verlag, pp. 56–68.").concat(this.newline).concat(
            "5.    P.L. Erdös, L. Soukupa, and J. Stoye, \"Balanced Vertices in Trees and a Simpler Algorithm to Compute the Genomic Distance,\" " + this.newline + space3 + "submitted, 2010.").concat(this.newline).concat(
            "6.    S. Hannenhalli, and P.A. Pevzner, \"Transforming men into mice (polynomial algorithm for genomic distance problem).\" In Proceedings " + this.newline + space3 + "of the 36th IEEE Symposium on Foundations of Computer Science (FOCS 1995), IEEE Computer Society Press, pp. 581–592.").concat(this.newline).concat(
            "7.    S. Hannenhalli, and P.A. Pevzner, \"Transforming cabbage into turnip: Polynomial algorithm for sorting signed permutations by " + this.newline + space3 + "reversals.,\" Journal of the ACM,  vol. 46(1), 1999, pp. 1–27.").concat(this.newline).concat(
            "8.    G. Jean, and M. Nikolski, \"Genome rearrangements: a correct algorithm for optimal capping. ,\" Information Processing Letters, " + this.newline + space3 + "vol. 104, 2007, pp. 14–20.").concat(this.newline).concat(
            "9.    Kováč, J. et al. (2011) Restricted DCJ model: rearrangement problems with chromosome reincorporation. J Comput Biol, 18(9), 1231–1241.").concat(this.newline).concat(
            "10. P.A. Pevzner, and G. Tesler, \"Transforming men into mice: the Nadeau-Taylor chromosomal breakage model revisited.,\" " + this.newline + space3 + "In Proceedings of the Seventh Annual Conference on Computational Molecular Biology (RECOMB 2003), ACM Press, pp. 247–256.").concat(this.newline).concat(
            "11. E. Tannier, A. Bergeron, and M.-F. Sagot, \"Advances in sorting by reversals.,\" Discrete Applied Mathematics,  vol. 155(6-7), 2007, " + this.newline + space3 + "pp. 881–888.").concat(this.newline).concat(
            "12. G. Tesler, \"Efficient algorithms for multichromosomal genome rearrangements.,\" Journal of Computer and System Sciences,  " + this.newline + space3 + "vol. 65(3), 2002, pp. 587–609.").concat(this.newline).concat(
            "13. S. Yancopoulos, O. Attie, and R. Friedberg, \"Efficient sorting of genomic permutations by translocation, inversion and block interchange.,\" " + this.newline + space3 + "Bioinformatics,  vol. 21(16), 2005, pp. 3340–3346.").concat(this.newline)
        };

        String[] initStyles = {"bold", "regular", "bold", "regular", "icon1", "bold", "regular", "underlined",
            "regular", "icon2", "regular", "icon3", "regular", "icon4",
            "regular", "bold", "regular", "bold", "regular", "load",
            "regular", "example", "regular", "scenario", "regular", "run",
            "regular", "genomes", "regular", "adj", "regular", "graphic",
            "regular", "steps", "regular", "colors", "regular", "slider",
            "regular", "save", "regular", "save2", "regular", "clear",
            "regular", "exit", "regular", "help", "bold", "regular"};

        this.setTitle("UniMoG Help Window");
        this.setSize(this.standardSize);
        this.setLayout(new BorderLayout());
        JTextPane helpPane = new JTextPane();
        helpPane.setEditable(false);
        helpPane.setEditorKit(new WrapEditorKit());
        JScrollPane helpScrollPane = new JScrollPane(helpPane);
        this.add(helpScrollPane);

        StyledDocument doc = helpPane.getStyledDocument();
        this.addStylesToDocument(doc);

        try {
            for (int i = 0; i < initString.length; i++) {
                doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        helpPane.setCaretPosition(0); //to start at 0 position of the scrollpane!
    }

    /**
     * Adds the styles to the document.
     * @param doc the document
     */
    private void addStylesToDocument(final StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("underlined", regular);
        StyleConstants.setUnderline(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        s = this.getIcon(doc, regular, "icon1", 0);
        s = this.getIcon(doc, regular, "icon2", 1);
        s = this.getIcon(doc, regular, "icon3", 2);
        s = this.getIcon(doc, regular, "icon4", 3);
        s = this.getIcon(doc, regular, "load", 4);
        s = this.getIcon(doc, regular, "example", 5);
        s = this.getIcon(doc, regular, "scenario", 6);
        s = this.getIcon(doc, regular, "run", 7);
        s = this.getIcon(doc, regular, "genomes", 8);
        s = this.getIcon(doc, regular, "adj", 9);
        s = this.getIcon(doc, regular, "graphic", 10);
        s = this.getIcon(doc, regular, "steps", 11);
        s = this.getIcon(doc, regular, "colors", 12);
        s = this.getIcon(doc, regular, "slider", 13);
        s = this.getIcon(doc, regular, "save", 14);
        s = this.getIcon(doc, regular, "save2", 15);
        s = this.getIcon(doc, regular, "clear", 16);
        s = this.getIcon(doc, regular, "exit", 17);
        s = this.getIcon(doc, regular, "help", 18);

    }

    /**
     * Returns the wanted icon.
     * @param doc doucment to add the icon
     * @param regular style which should be updated
     * @param styleName name of the new stlye
     * @param iconIndex the index of the icon in the array "iconPaths"
     * @return the new stlye
     */
    private Style getIcon(StyledDocument doc, final Style regular, final String styleName, final int iconIndex) {
        Style s = doc.addStyle(styleName, regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon tableIcon = this.createImageIcon(this.iconPaths[iconIndex]);
        if (tableIcon != null) {
            StyleConstants.setIcon(s, tableIcon);
        }
        return s;
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid. 
     * @param path the path of the icon
     * @return the icon or null if the path was invalid
     */
    private ImageIcon createImageIcon(final String path) {
        URL imgURL = this.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
