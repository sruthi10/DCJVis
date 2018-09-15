package de.unibi.cebitec.gi.unimog.framework;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import de.unibi.cebitec.gi.unimog.algorithms.IntermediateGenomesGenerator;
import de.unibi.cebitec.gi.unimog.datastructure.ChromFragments;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.utils.Constants;
import de.unibi.cebitec.gi.unimog.utils.Toolz;

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
 * Panel represents a single chromosome and speeds up the painting process,
 * since only components have to be repainted which intersect the visible rectangle.
 * An operation stores one gene of each cut in the current genome. That means in the next genome
 * the two genes of an operation are located in the same cut!
 * Note that all pairs of operations in the operation list have to contain the pair with two
 * different extremities at the first index! 
 *
 */
public class ChromosomePanel extends JPanel {

	
	private static final long serialVersionUID = 1L;
	
	private static final int VERTICAL_FONT_SP = 75;
	private static final int CENTER_FONT = 		3;
	private static final int NB_POINTS = 		3;
	private static final int GAP = 				10;
	
	private Color chromBackgColor;
	private Color chromColorColor;
	private double zoomF;
	private int comparisonGap = 30;
	private String[] genes;
	private boolean circular;
	private int fstExtremity;
	private int scndExtremity;
	private String gene = "";
	private String neighbour = "";
	private String gene2 = "";
	private String neighbour2 = "";
	private HashMap<Integer, String> backMap;
	private int[] beforeGenes = {-1, -1};
	private boolean fstCutWas = false;
	private boolean oneGeneMissing = false;
	String lastCutGene = "";
	
	private ChromFragments lastChromFragments;
	private ArrayList<Color> cutColors = new ArrayList<Color>();
	private ArrayList<Color> lastStepsColors = new ArrayList<Color>();
	
	private RoundRectangle2D chromBackgRect;
	private ArrayList<Ellipse2D> ovals = new ArrayList<Ellipse2D>();
	private ArrayList<Ellipse2D> ovalsCut = new ArrayList<Ellipse2D>();
	private ArrayList<Rectangle2D> rectCut = new ArrayList<Rectangle2D>();
	private ArrayList<int[]> arrowsXpos = new ArrayList<int[]>();
	private ArrayList<int[]> arrowsYpos = new ArrayList<int[]>();
	private ArrayList<Pair<String, Color>> fragmentColors = new ArrayList<Pair<String,Color>>();
	private Line2D line;
	private String geneNames = "";
	private Line2D circLine;
	private Arc2D circArc1;
	private Arc2D circArc2;
	private ArrayList<Rectangle2D> cutMarkRect = new ArrayList<Rectangle2D>();
	private ArrayList<Rectangle2D> lastCutMarkRect = new ArrayList<Rectangle2D>();

	private int width1;
	private int height1;
	private boolean fstPaint = true;
	private boolean highlightCutRects = false;

	
	private HashMap<String, Color> currentColorMap; //true means in left direction, false in right direction
	private HashMap<String, Color> lastColorMap = new HashMap<String, Color>();

	
	/**
	 * Constructs a new chromosome panel. 
	 * @param genes The genes belonging to this chromosome
	 * @param circular true if the chromosome is circular
	 * @param fstExtremity the first extremity of the first cut
	 * @param scndExtremity the first extremity of the second cut
	 * @param chromFragments data holder containing all data corresponding to last step's fragments
	 * @param backMap the mapping of the internal integer gene names to the original String ones
	 * @param zoomF the current zoom factor
	 * @param backgColor background color of the chromosome
	 */
	public ChromosomePanel(final String[] genes, final boolean circular, final int fstExtremity, 
			final int scndExtremity, final ChromFragments chromFragments, 
			final HashMap<Integer, String> backMap, final double zoomF, final Color backgColor){
			
		this.genes = genes;
		this.circular = circular;
		this.fstExtremity = fstExtremity;
		this.scndExtremity = scndExtremity;
		this.lastChromFragments = chromFragments;
		this.zoomF = zoomF;
		this.comparisonGap *= this.zoomF;
		this.backMap = backMap;
		this.chromColorColor = backgColor;
		this.setOpaque(true);
	}
	
    @Override
	public void paint (Graphics graphic1D){
		this.setDoubleBuffered(true);
		
		Graphics2D graphic = (Graphics2D) graphic1D;
		graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphic.setFont(Toolz.getFontOutput());
		graphic.setBackground(this.getParent().getBackground());
		graphic.clearRect(0, 0, this.getSize().width, this.getSize().height);
		graphic.scale(this.zoomF, this.zoomF);
		
//		if (graphic.getClip() != null){
//		Rectangle r = graphic.getClip().getBounds();
//		//graphic.clipRect(r.x, r.y, r.width, r.height);
//		graphic.setClip(r.x, r.y, r.width, r.height);
//	} // setting the clip rectangle...
		if (this.fstPaint){
			this.initialPaint(graphic);
		} else {
			graphic.setColor(this.chromBackgColor);
			graphic.fill(this.chromBackgRect);
			
			for (int i=0; i<this.cutMarkRect.size(); ++i){
				if (i < this.cutColors.size()){
					if (!(this.cutMarkRect.get(i).getWidth() < 22)){
						graphic.setColor(this.cutColors.get(i));
						graphic.fill(this.cutMarkRect.get(i));	
					} else {
						System.out.println("Chrom Panel: still possible cutMarkRect");
						graphic.setColor(this.cutColors.get(i));
						graphic.fill(this.cutMarkRect.get(++i));
					}
					if (this.highlightCutRects){ //in case chroms have background color highlight the cut rects
						graphic.setColor(Color.BLACK);
						graphic.draw(this.cutMarkRect.get(i));
					}
				}
			}
			
			for (int i=0; i<this.lastCutMarkRect.size(); ++i){
				if (i < this.lastStepsColors.size()){
					if (!(this.lastCutMarkRect.get(i).getWidth() < 0)){
						graphic.setColor(this.lastStepsColors.get(i));
						graphic.fill(this.lastCutMarkRect.get(i));
					} else {
						graphic.setColor(this.lastStepsColors.get(i));
						if (i<this.lastCutMarkRect.size()-1){
							graphic.fill(this.lastCutMarkRect.get(++i));
						} else {
							graphic.fill(this.lastCutMarkRect.get(i));
						}
					}
					if (this.highlightCutRects){ //in case chroms have background color highlight the cut rects
						graphic.setColor(Color.BLACK);
						graphic.draw(this.lastCutMarkRect.get(i));
					}
				}
				
			}
						
			graphic.setColor(Color.BLACK);
			graphic.draw(this.line);
			graphic.drawString(this.geneNames, this.comparisonGap+ChromosomePanel.CENTER_FONT, this.comparisonGap);
			
			for (int i = 0; i < this.genes.length; ++i){
				graphic.drawPolyline(this.arrowsXpos.get(i), this.arrowsYpos.get(i), ChromosomePanel.NB_POINTS);
			}
			
			for (int i = 0; i < this.ovals.size(); ++i){
				graphic.fill(this.ovals.get(i));
				graphic.draw(this.ovals.get(i));
			}
			
			for (int i = 0; i < this.rectCut.size(); ++i){
				graphic.setColor(Color.RED);
				graphic.draw(this.rectCut.get(i));
				graphic.fill(this.rectCut.get(i));
				graphic.setColor(Color.BLACK);
				graphic.draw(this.ovalsCut.get(i));
				graphic.fill(this.ovalsCut.get(i));
			}
			
			if (this.circular){
				graphic.draw(this.circLine);
				graphic.draw(this.circArc1);
				graphic.draw(this.circArc2);
			}
			
		}
		this.width1 = (int) (this.chromBackgRect.getMaxX()*this.zoomF+ChromosomePanel.GAP);
		this.height1 = (int) (this.chromBackgRect.getMaxY()*this.zoomF+ChromosomePanel.GAP);
		this.setPreferredSize(new Dimension(this.width1, this.height1));
		this.setSize(new Dimension(this.width1, this.height1));
	}
	
	/**
	 * Method for the initial painting. Carries out all necessary calculations for painting.
	 * @param graphic the 2d graphics object of the panel
	 */
	public void initialPaint(Graphics2D graphic){
		
		this.currentColorMap = new HashMap<String, Color>();
		this.lastColorMap = this.lastChromFragments.getColorMap();
		
		if (MainFrame.colorMode == Constants.OUTPUT_1){
			this.chromBackgColor = Toolz.colorBackg;
			this.highlightCutRects = false;
		} else {
			this.chromBackgColor = chromColorColor;
			this.highlightCutRects = true;
		}
		
		final int fontSpacing = 	10;
		final int fontSpacing2 = 	55;
		final int minGeneLength = 	23;
		final int ovalSize = 		6;
		final int rectSize = 		14;
		final int offsetCut = 		6;
		final int offsetVertical = 	3;
		final int offsetVertical3 = 7;
		final int offsetCircular = 	35;
		final int offsetArc = 		15;
		final int offsetArc2 = 		20;
		final int offsetVertical2 = 10;
		final int widthArc = 		35;
		final int heightArc = 		25;
		final int arcRect =			10;
		final int cutWidth = 		2;
		
		int x = this.comparisonGap;
		int y = this.comparisonGap;
		int xChromStart = x;
		int posY = y+fontSpacing;
		
		if (!this.circular){
			this.ovals.add(new Ellipse2D.Double(x-ovalSize+1, posY-offsetVertical, ovalSize, ovalSize));
		}
	
		boolean geneOrient = true; //true if forward, false if reversed
		int xFstCut = 0;
		int xFstCut2 = 0;
		String gene;
		int geneWidth = 0;
		boolean beforeGene = true;
		boolean fstCut = false;
		final String lastGene = this.lastChromFragments.getGene();
		final String lastNeighbour = this.lastChromFragments.getNeighbour();
		final boolean[] beforeLastGenes = this.lastChromFragments.getBeforeGenes();
		int xValue = 0;
		for (int l = 0; l < this.genes.length; ++l){
			//Single gene level
			gene = this.genes[l];
			geneWidth = graphic.getFontMetrics().stringWidth(gene);
			geneWidth = geneWidth > minGeneLength ? geneWidth : minGeneLength;

			if (gene.length() == 1){
				this.geneNames = this.geneNames.concat(gene.concat("   "));
			} else
			if (gene.length() == 2){
					this.geneNames = this.geneNames.concat(gene.concat("  "));
			} else 
			if (gene.length() == 3){
				this.geneNames = this.geneNames.concat(gene.concat("  "));
				geneWidth += 7;
			} else {
				this.geneNames = this.geneNames.concat(gene.concat("  "));
				geneWidth += 9;
			}
			this.ovals.add(new Ellipse2D.Double(x+geneWidth-1, posY-offsetVertical, ovalSize, ovalSize));
			
			//mark cut positions with red ovals
			String unorientGene = this.unsignGene(gene);
			if (this.fstExtremity != 0 || this.scndExtremity != 0){
				String fstCutGene = IntermediateGenomesGenerator.getSignedGeneName((this.fstExtremity+1)/2, this.backMap);
				String scndCutGene = IntermediateGenomesGenerator.getSignedGeneName((this.scndExtremity+1)/2, this.backMap);
				String fstCutGeneUnor = this.unsignGene(fstCutGene);
                                String scndCutGeneUnor = this.unsignGene(scndCutGene);
				if ((fstCut = unorientGene.equals(fstCutGeneUnor)) || 
					(unorientGene.equals(scndCutGeneUnor))){
					xValue = 0;
					geneOrient = this.getGeneOrientation(gene, unorientGene, fstCutGene, scndCutGene, fstCutGeneUnor);
					beforeGene = (unorientGene.equals(fstCutGeneUnor)) ? this.getCutPos(geneOrient, this.fstExtremity) : 
						this.getCutPos(geneOrient, this.scndExtremity);
					if (beforeGene){
						xValue = x-offsetCut+2;
						this.rectCut.add(new Rectangle2D.Double(xValue, posY-offsetVertical3, cutWidth, rectSize));
						this.ovalsCut.add(new Ellipse2D.Double(x-offsetCut, posY-offsetVertical, ovalSize, ovalSize));
						if (xFstCut == 0){////////////////////// mark current fragments /////////////////////////////////////////
							if (xValue-xChromStart > 0){
								this.cutMarkRect.add(new Rectangle2D.Double(xChromStart, posY+fontSpacing+5, xValue-xChromStart, 6));
							}
						} else {
							this.cutMarkRect.add(new Rectangle2D.Double(xFstCut, posY+fontSpacing+5, xValue-xFstCut, 6));
						}
						if (fstCutGene.equals(scndCutGene) && this.fstExtremity != this.scndExtremity){
							this.rectCut.add(new Rectangle2D.Double(x+geneWidth+1, posY-offsetVertical3, cutWidth, rectSize));
							this.ovalsCut.add(new Ellipse2D.Double(x+geneWidth-1, posY-offsetVertical, ovalSize, ovalSize));
						}//////////////////////////////////////////////////////////////////////////////////////////
					} else {
						xValue = x+geneWidth+1;
						this.rectCut.add(new Rectangle2D.Double(xValue, posY-offsetVertical3, cutWidth, rectSize));
						this.ovalsCut.add(new Ellipse2D.Double(x+geneWidth-1, posY-offsetVertical, ovalSize, ovalSize));
						if (xFstCut == 0){///////////////////// mark current fragments //////////////////////////////////////////
							if (xValue-xChromStart > 0){
								this.cutMarkRect.add(new Rectangle2D.Double(xChromStart, posY+fontSpacing+5, xValue-xChromStart, 6));
							}
						} else {
							this.cutMarkRect.add(new Rectangle2D.Double(xFstCut, posY+fontSpacing+5, xValue-xFstCut, 6));
						}
						if (fstCutGene.equals(scndCutGene) && this.fstExtremity != this.scndExtremity){
							this.rectCut.add(new Rectangle2D.Double(x-offsetCut+2, posY-offsetVertical3, cutWidth, rectSize));
							this.ovalsCut.add(new Ellipse2D.Double(x-offsetCut, posY-offsetVertical, ovalSize, ovalSize));
						} //////////////////////////////////////////////////////////////////////////////////////////
					}
					
					//////////////////// get cutting information for current fragment coloring ////////////////	
					boolean inserted = false;
					if (fstCut){
						this.beforeGenes[0] = beforeGene ? Constants.TRUE : Constants.FALSE;	
						this.gene = gene;
						if (beforeGene && l-1 >= 0){ 
							this.addColor(this.genes[l-1], l-1);
							this.addColor(this.gene, l);
							inserted = true;
						}		
						if (!beforeGene && l+1 < this.genes.length){
							this.addColor(this.gene, l);
							this.addColor(this.genes[l+1], l+1);
						} else 
						if (this.circular && !beforeGene && l+1 == this.genes.length){
							this.addColor(this.gene, l);
							this.addColor(this.genes[0], 0);
						} else
						if (!(beforeGene && l-1 >= 0)){
							this.oneGeneMissing = true;
							this.addColor(this.gene, l);
						} else 
						if (!inserted){
							this.addColor(this.gene, l);
						}
						
					} else {
						this.neighbour2 = gene;
						if (beforeGene && l-1 >= 0){
							this.gene2 = this.genes[l-1];
							this.addColor(this.gene2, l-1);
							this.addColor(this.neighbour2, l);
							inserted = true;
						}
						if (!beforeGene && l+1 < this.genes.length) {
							this.gene2 = this.genes[l+1];
							this.addColor(this.neighbour2, l);
							this.addColor(this.gene2, l+1);							
						} else 
						if (this.circular && !beforeGene && l+1 == this.genes.length){
							this.addColor(this.neighbour2, l);
							this.addColor(this.genes[0], 0); //in case of wrong coloring test commenting this line out
						} else
						if (!(beforeGene && l-1 >= 0)){
							this.oneGeneMissing = true;
							this.addColor(this.neighbour2, l);
						} else 
						if (!inserted){
							this.addColor(this.neighbour2, l);
						}
					}
					
					if (this.neighbour.equals("")){ //stores neighbour of gene, if it exists, otherwise sets the
						if (beforeGene && l-1 >= 0){ //other gene as neighbour which was the single scnd cut gene
							this.neighbour = this.genes[l-1]; //since gene = scndCutGene (which moves to fstCut now ->  
							this.beforeGenes[1] = Constants.FALSE; //storing its neighbour results in a gene remaining
						} else 										// in scnd cut
						if (!beforeGene && l+1 < this.genes.length){
							this.neighbour = this.genes[l+1];
							this.beforeGenes[1] = Constants.TRUE;
						}
						
					}
					
					xFstCut = xValue+1 > xChromStart ? xValue+1 : xChromStart;
					this.fstCutWas = true;
				} ///////////////////////////////////////////////////////////////////////////////////////////
			}
			///////////////////////// calculations for coloring rectangles marking last step fragments ///////////////////
			boolean increase = false;
			if (!this.lastChromFragments.isEmpty()){
				xValue = 0;
				
				for (String geneName : this.lastColorMap.keySet()){
					if (this.unsignGene(geneName).equals(unorientGene)){
						this.lastStepsColors.add(this.lastColorMap.get(geneName));
					}
				}
				if (!this.circular){
					for (int i=0; i<this.lastStepsColors.size()-1; ++i){
						if (this.lastStepsColors.get(i) == this.lastStepsColors.get(i+1)){
							this.lastStepsColors.remove(i);
						}
					}					
				}
				///////////////////////////////////////////////////////////////////////////////////////////
				//////////////////////////////creating last steps rectangles //////////////////////////////////
				
				if (this.unsignGene(lastGene).equals(unorientGene)){
					if (!lastGene.equals(gene)){ // orientation is swapped
						beforeLastGenes[0] = beforeLastGenes[0] ? false : true;
					} else
					if (lastGene.equals(lastNeighbour)){ // && beforeGene == beforeLastGenes[0]
						beforeLastGenes[0] = beforeLastGenes[0] ? false : true;
						increase = true;
					}
					if (beforeLastGenes[0]){
						xValue = x-offsetCut+2;
						if (xValue > xChromStart){
							if (xFstCut2 == 0){
								this.lastCutMarkRect.add(new Rectangle2D.Double(xChromStart, y+2-fontSpacing*2, xValue-xChromStart, 6));
							} else {
								this.lastCutMarkRect.add(new Rectangle2D.Double(xFstCut2, y+2-fontSpacing*2, xValue-xFstCut2, 6));
							}
						}
					} else {
						xValue = x+geneWidth+1;
						if (xFstCut2 == 0){
							if (xValue-xChromStart > 0){
								this.lastCutMarkRect.add(new Rectangle2D.Double(xChromStart, y+2-fontSpacing*2, xValue-xChromStart, 6));
							}
						} else {
							this.lastCutMarkRect.add(new Rectangle2D.Double(xFstCut2, y+2-fontSpacing*2, xValue-xFstCut2, 6));
						}
					}
					xFstCut2 = xValue+1;
					
				}
				if (this.unsignGene(lastNeighbour).equals(unorientGene)){
					if (!lastNeighbour.equals(gene)){ // orientation is swapped
						beforeLastGenes[1] = beforeLastGenes[1] ? false : true;
					}
					if (beforeLastGenes[1]){
						xValue = x-offsetCut+2;
						if (increase){
							xValue += geneWidth;
						}
						if (xValue > xChromStart){
							if (xFstCut2 == 0){
								//if (xValue-xChromStart > 0){
									this.lastCutMarkRect.add(new Rectangle2D.Double(xChromStart, y+2-fontSpacing*2, xValue-xChromStart, 6));
								//}
							} else {
								this.lastCutMarkRect.add(new Rectangle2D.Double(xFstCut2, y+2-fontSpacing*2, xValue-xFstCut2, 6));
							}
						}
						
					} else {
						xValue = x+geneWidth+1;
						if (xFstCut2 == 0){
							if (xValue-xChromStart > 0){
								this.lastCutMarkRect.add(new Rectangle2D.Double(xChromStart, y+2-fontSpacing*2, xValue-xChromStart, 6));
							}
						} else {
							this.lastCutMarkRect.add(new Rectangle2D.Double(xFstCut2, y+2-fontSpacing*2, xValue-xFstCut2, 6));
						}
					}
					xFstCut2 = xValue+1;
				}
			} /////////////////////////////////////////////////////////////////////////////////////////
			
			///////////////////// add arrowheads to current gene /////////////////////////////////////////////
			final int lengthArrowhead = 5;
			geneOrient = gene.charAt(0) != '-';
			if (geneOrient){
				int[] xCoords = {x+geneWidth-lengthArrowhead, x+geneWidth, x+geneWidth-lengthArrowhead};
				int[] yCoords = {posY-lengthArrowhead, posY, posY+lengthArrowhead};
				this.arrowsXpos.add(xCoords);
				this.arrowsYpos.add(yCoords);
			} else {
				int[] xCoords = {x+lengthArrowhead, x, x+lengthArrowhead};
				int[] yCoords = {posY-lengthArrowhead, posY, posY+lengthArrowhead};
				this.arrowsXpos.add(xCoords);
				this.arrowsYpos.add(yCoords);
			}
			x += geneWidth+ovalSize-1;
		} ////////////////////////////////////////////////////////////////////////////////////////////
		
		this.line = new Line2D.Double(xChromStart, posY, x-(ovalSize-1), posY);
		this.chromBackgRect = new RoundRectangle2D.Double(xChromStart-fontSpacing*2, y-fontSpacing*2, x-(geneWidth+ovalSize)+(geneWidth+fontSpacing*2), fontSpacing*6, arcRect, arcRect);
		
		/////////////////// create last rectangle for coloring the current fragments /////////////////////////
		if (this.fstCutWas){
			double xVal;
			if (this.cutMarkRect.size() > 0){
				Rectangle2D rect = this.cutMarkRect.get(this.cutMarkRect.size()-1);
				xVal = rect.getX()+rect.getWidth()+1;
			} else {
				xVal = xChromStart;
			}
			if (xVal < x-10){
				this.cutMarkRect.add(new Rectangle2D.Double(xVal, posY+fontSpacing+5, x-xVal, 6));
			}
		} ///////////////////////////////////////////////////////////////////////////////////////////////////
		
		//////////////////// create last rectangle for last step's fragments //////////////////////////////
		if (this.lastStepsColors.size() > 0){
			double xVal;
			if (this.lastCutMarkRect.size() > 0){
				Rectangle2D rect = this.lastCutMarkRect.get(this.lastCutMarkRect.size()-1);
				xVal = rect.getX()+rect.getWidth()+1;
			} else {
				xVal = xChromStart;
			}
			if (xVal < x-10){
				this.lastCutMarkRect.add(new Rectangle2D.Double(xVal, y+2-fontSpacing*2, x-xVal, 6));
			}
		} ////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		if (this.circular){ //add handling for circular chromosomes
			this.circLine = new Line2D.Double(xChromStart, y+offsetCircular, x, y+offsetCircular);
			this.circArc1 = new Arc2D.Double(xChromStart-offsetArc, y+offsetVertical2, widthArc, heightArc, 90, 180, Arc2D.OPEN);
			this.circArc2 = new Arc2D.Double(x-offsetArc2, y+offsetVertical2, widthArc, heightArc, 270, 180, Arc2D.OPEN);
		}
		x += fontSpacing2;
		this.width1 = (int) (x*this.zoomF);
		this.height1 = (int) (ChromosomePanel.VERTICAL_FONT_SP*this.zoomF);
		this.fstPaint = false;
		
		this.trimArrays();
	}

	
	/**
	 * Generates and stores a color for each gene involved in a cut.
	 * The color is stored internal in this object for painting this chromosome
	 * and additionally stored in the "fragmentColors" variable which is handed
	 * over to next step's genome.
	 * @param geneName the name of the gene involved in a cut
	 * @param pos position of the neighboring cut gene (might also be at chromosome end, that's actually what we're looking for)
	 */
	private void addColor(final String geneName, int pos) {
		Color color;
		if (this.fstCutWas){ //we are in scnd cut in this chromosome
			if (this.fragmentColors.size() == 2){ //
				if (this.oneGeneMissing){
					if (!this.circular && pos >= this.genes.length-1){
						color = this.fragmentColors.get(this.fragmentColors.size()-1).getSecond();
					} else {
						color = Toolz.getColor();
					}
					this.cutColors.add(color);
				} else
				if (this.circular){
					if (pos >= this.genes.length){
						color = Toolz.getColor();
						this.cutColors.set(1, color);
					} else {
						color = Toolz.getColor(); //replace color which is identical with entry [0]
						this.fragmentColors.set(1, new Pair<String, Color>(this.fragmentColors.get(1).getFirst(), color));
						this.currentColorMap.put(this.fragmentColors.get(1).getFirst(), color);
						this.cutColors.set(1, color);
						color = this.fragmentColors.get(1).getSecond();	
					}
				} else {
					color = this.fragmentColors.get(1).getSecond();	
				}
			} else
			if (this.circular && this.fragmentColors.size() == 3){			
				color = this.fragmentColors.get(0).getSecond(); 
				this.cutColors.add(color);
			} else
			if (this.fragmentColors.size() == 1){
				color = this.fragmentColors.get(0).getSecond();
			} else {
				color = Toolz.getColor();
				this.cutColors.add(color);
			}
		} else {
			if (this.circular && this.fragmentColors.size() == 1){ //two cuts in circular chrom at diff. pos.
				color = this.fragmentColors.get(0).getSecond();
				this.cutColors.add(this.cutColors.size()-1, color);
			} else { //fst cut in lin or circ chrom needs new color
				color = Toolz.getColor();
				this.cutColors.add(color);
			}
		}
		this.fragmentColors.add(new Pair<String, Color>(geneName, color));
		this.currentColorMap.put(geneName, color);
	}
	

	/**
	 * Returns the gene orientation.
	 * @param gene current gene
	 * @param unorientedGene current unsigned gene
	 * @param fstCutGene gene at first cut
	 * @param scndCutGene gene at second cut
	 * @param fstCutGeneUnor unoriented gene at first cut
	 * @return true if gene & cutGene are identical, false if not
	 */
	private boolean getGeneOrientation(final String gene, final String unorientedGene, final String fstCutGene, 
			final String scndCutGene, final String fstCutGeneUnor) {
		String cutGene = (unorientedGene.equals(fstCutGeneUnor)) ? fstCutGene : scndCutGene;
		if (cutGene.equals(gene)){
			return true;
		}
		return false;
	}

	/**
	 * Calculates & returns if the cut position at "gene" is located in front or after the gene.
	 * @param geneOrient the orientation of the gene: true if forward, false if reversed
	 * @param extremity the extremity of the gene which is involved in the cut
	 * @return <code>true</code> if it located in front of the gene, <code>false</code> otherwise
	 */
	private boolean getCutPos(final boolean geneOrient, final int extremity) {
		if (geneOrient){
			if (extremity%2 == 1){
				return true;
			} else {
				return false;
			}
		} else {
			if (extremity%2 == 1){
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Sets the zoom factor of this panel.
	 * @param zoomF the zoom factor to set
	 * @param intersects 
	 */
	public void setZoomF(final double zoomF, final boolean intersects) {
		final double oldZoomF = this.zoomF;
		this.zoomF = zoomF;
		if (intersects){
			this.paint(this.getGraphics());
		} else {
			if (oldZoomF == Constants.ZOOM_FACTOR1){
				this.width1 *= 1.4;
			} else 
			if (oldZoomF == Constants.ZOOM_FACTOR2){
				this.width1 *= this.zoomF;
			} else {
				this.width1 *= 0.64;
			}
			this.height1 = (int) (ChromosomePanel.VERTICAL_FONT_SP*this.zoomF);
			this.setPreferredSize(new Dimension(this.width1, this.height1));
			this.setSize(new Dimension(this.width1, this.height1));
		}
	}
	
	
	/**
	 * Trims all used arrays to their used size.
	 */
	private void trimArrays() {
		this.cutColors.trimToSize();
		this.lastStepsColors.trimToSize();
		this.ovals.trimToSize();
		this.ovalsCut.trimToSize();
		this.rectCut.trimToSize();
		this.arrowsXpos.trimToSize();
		this.arrowsYpos.trimToSize();
		this.fragmentColors.trimToSize();
		this.cutMarkRect.trimToSize();
		this.lastCutMarkRect.trimToSize();
	}
	
	/**
	 * Sets a new backgroundColor.
	 * @param colorMode the color mode
	 */
	public void setChromBackgColor(final int colorMode) {
		if (MainFrame.colorMode == Constants.OUTPUT_1){
			this.chromBackgColor = Toolz.colorBackg;
			this.highlightCutRects = false;
		} else {
			this.chromBackgColor = chromColorColor;
			this.highlightCutRects = true;
		}
	}

	/**
	 * Sets if the panel is painted the first time after pressing the "run" button.
	 * That means new data has been passed to the panel and a new image has to be
	 * created.
	 * @param fstPaint the fstPaint to set
	 */
	public void setFstPaint(final boolean fstPaint) {
		this.fstPaint = fstPaint;
	}
	
	/**
	 * Returns the boolean array indicating if the first and second cut extremities
	 * are cut before or after their gene.
	 * @return the boolean array
	 */
	public int[] getBeforeGenes(){
		return this.beforeGenes;
	}
	
	/**
	 * Returns the boolean value indicating if the first cut extremity
	 * is cut before or after its gene.
	 * @return the boolean array
	 */
	public String getNeighbour(){
		return this.neighbour;
	}

	/**
	 * Returns the fst cut gene if it is located in this chromosome.
	 * @return the fst cut gene
	 */
	public String getGene() {
		return this.gene;
	}


	/**
	 * Returns the neighbour of 'gene', which belongs to the first cut
	 * @return the gene2
	 */
	public String getGene2() {
		return this.gene2;
	}

	/**
	 * Returns the neighbour of 'neighbour' which belongs to the second cut
	 * @return the neighbour2
	 */
	public String getNeighbour2() {
		return this.neighbour2;
	}

	/**
	 * Returns the array of pairs of cut gene names and corresponding colors.
	 * @return the fragmentColors
	 */
	public ArrayList<Pair<String, Color>> getFragmentColors() {
		return this.fragmentColors;
	}
	
	public HashMap<String, Color> getCurrentColorMap(){
		return this.currentColorMap;
	}
	
	/**
	 * Returns if at least one cut occured in this chromosome.
	 * If thats the case true is returned, false otherwise.
	 * @return true if a cut occured in the chromosome, false if not
	 */
	public boolean hadACut(){
		return this.fstCutWas;
	}
        
	/**
	 * Removes the sign of a gene, if it starts with a minus.
	 * @param gene gene to "unsign"
	 * @return unsigned gene
	 */
	private String unsignGene(String gene){
		return gene.startsWith("-") ? gene.substring(1) : gene;
    }
	
}
