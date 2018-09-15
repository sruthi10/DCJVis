/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.cebitec.gi.unimog.algorithms;

import de.unibi.cebitec.gi.unimog.datastructure.Chromosome;
import de.unibi.cebitec.gi.unimog.datastructure.Data;
import de.unibi.cebitec.gi.unimog.datastructure.Genome;
import de.unibi.cebitec.gi.unimog.datastructure.IAdditionalData;
import de.unibi.cebitec.gi.unimog.datastructure.OperationList;
import de.unibi.cebitec.gi.unimog.datastructure.Pair;
import de.unibi.cebitec.gi.unimog.datastructure.SplayNode;
import de.unibi.cebitec.gi.unimog.datastructure.SplayTree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/** In this class the restricted DCJ is implemented. 
 *  If a circulare intermediate is created in a step in the DCJ sorting it is
 *  reincorporated immediatly in the next step.
 *
 * @author Corinna Sickinger
 */
public class SortingRestrictedDCJ implements ISorting {

    private SplayTree splaytree;
    private int genomelenght;
    private ArrayList<Integer> myGenomeAList;
    private ArrayList<Integer> myGenomeBList;
//    private int[] posInBList;
    private int lenghtforAdjacency;

    @Override
    public OperationList findOptSortSequence(Data data, IAdditionalData additionalData, HashMap<Integer, Integer> chromMap) {
        final OperationList operationslist = new OperationList();
        int[] adjacenciesGenomeB = data.getAdjGraph().getAdjacenciesGenome2();
        Genome genomeA = data.getGenomeA();
        Genome genomeB = data.getGenomeB();
        splaytree = new SplayTree(genomeA);
        genomelenght = genomeA.getNumberOfGenes();
        splaytree.initialize(genomelenght);
        lenghtforAdjacency = 2 * genomeA.getNumberOfGenes() + 1;
        int sumChromosomsA = genomeA.getNumberOfChromosomes();


        myGenomeAList = new ArrayList<Integer>(Arrays.asList(0));
        for (int j = 0; j < genomeA.getNumberOfChromosomes(); j++) {
            Integer[] bnodes = splaytree.intToInteger(genomeA.getChromosome(j).getGenes());
            myGenomeAList.addAll(Arrays.asList(bnodes));
            myGenomeAList.add(0);
        }

        myGenomeBList = new ArrayList<Integer>(Arrays.asList(0));
        for (int i = 0; i < genomeB.getNumberOfChromosomes(); i++) {
            Integer[] forNode = splaytree.intToInteger(genomeB.getChromosome(i).getGenes());
            myGenomeBList.addAll(Arrays.asList(forNode));
            myGenomeBList.add(0);
        }
        
//        this.posInBList = new int[myGenomeBList.size()];
//        for (int i=0; i<myGenomeBList.size(); ++i) {
//            int gene = myGenomeBList.get(i);
//            if (gene != 0) {
//                this.posInBList[gene] = i;
//            }
//        }


        ArrayList<Integer> myFirstgenomeList = splaytree.traverseTree();
        boolean myFirstchanged = this.changeChromosomOrientation(myFirstgenomeList);
        if (myFirstchanged) {
            myFirstgenomeList = splaytree.traverseTree();
        }
        myGenomeAList = (ArrayList<Integer>) myFirstgenomeList.clone();

        //First Operation to get Node with Marker 1
        SplayNode firstNode = splaytree.getNode(1);
        if (splaytree.getChromosome(firstNode) != 1) {
            //Translokate
            int chromOfOne = splaytree.getChromosome(firstNode);
            SplayNode rightChromNode = splaytree.getNode(genomelenght + chromOfOne);
            int rChromPos = rightChromNode.getPosition();
            SplayNode rightNode = splaytree.findIthNode(rChromPos);
            rightNode.splay();
            int rightTrans = rightNode.getMarker();
            int markerforTrans = splaytree.findIthNode(1).getMarker();
            translocate(markerforTrans, 2, rightTrans, splaytree.getChromosome(firstNode) + 1);
            myGenomeAList = splaytree.traverseTree();
        }

        if (myGenomeAList.get(1) != 1) {
            if (myGenomeAList.get(1) == -1) {
                splaytree.getNode(1).flipMarker();
                int rightOfi = myGenomeAList.get(2);
                int secondOpsecond;
                if (rightOfi < 0) {
                    secondOpsecond = Math.abs(rightOfi) * 2;
                } else {
                    secondOpsecond = rightOfi * 2 - 1;
                }
                operationslist.addOperation(new Pair<Integer, Integer>(2, secondOpsecond));
                operationslist.addOperation(new Pair<Integer, Integer>(1, 1));
            } else {
                if (firstNode.getMarker() < 0) {
                    //Revision
                    int inversestart = Math.abs(splaytree.findIthNode(1).getMarker());
                    if (inversestart == 0) {
                        int teloPos = splaytree.getNode(genomelenght + 1).getPosition();
                        inversestart = splaytree.findIthNode(teloPos).getMarker();
                    }
                    int firstPosition = firstNode.getPosition();
                    SplayNode rightofI = splaytree.findIthNode(firstPosition);
                    int markerRightofI = rightofI.getMarker();
                    int secondOpfirst;
                    if (inversestart < 0) {
                        secondOpfirst = Math.abs(inversestart) * 2;
                    } else {
                        secondOpfirst = inversestart * 2 - 1;
                    }
                    int secondOpsecond;
                    if (markerRightofI == 0) {
                        secondOpsecond = secondOpfirst;
                    } else if (markerRightofI < 0) {
                        secondOpsecond = Math.abs(markerRightofI) * 2;
                    } else {
                        secondOpsecond = markerRightofI * 2 - 1;
                    }

                    this.inverse(inversestart, 1);
                    operationslist.addOperation(new Pair<Integer, Integer>(secondOpfirst, secondOpsecond));
                    operationslist.addOperation(new Pair<Integer, Integer>(1, 1));

                } else {
                    //Blockexchange
                    firstNode.splay();
                    int biggest = 0;
                    if (firstNode.isReverseFlag()) {
                        biggest = firstNode.getRightChild().getBiggestChild();
                    } else {
                        biggest = firstNode.getLeftChild().getBiggestChild();
                    }

                    SplayNode biggestNode = splaytree.getNode(biggest);
                    int bChromNum = splaytree.getChromosome(biggestNode);

                    SplayNode bplusOneNode;
                    int bplusChromNum;
                    int bplusOneMarker = 0;

                    if (Math.abs(biggestNode.getMarker()) != genomelenght) {
                        bplusOneNode = splaytree.getNode(biggest + 1);
                        bplusChromNum = splaytree.getChromosome(bplusOneNode);
                        bplusOneMarker = bplusOneNode.getMarker();
                    } else {
                        bplusOneNode = null;
                        bplusChromNum = bChromNum;
                    }

                    //Two Translokations to get i+1 next to i
                    if (bChromNum != bplusChromNum) {
                        int posOfb = biggestNode.getPosition();
                        int markerRightOfB = splaytree.findIthNode(posOfb).getMarker();

                        int posOfBplusOne = bplusOneNode.getPosition();
                        SplayNode leftOfBplusOne = splaytree.findIthNode(posOfBplusOne - 2);
                        int markerLeftOfBplusOne = leftOfBplusOne.getMarker();

                        this.translocate(markerRightOfB, bChromNum + 1, biggest + 1, bplusChromNum + 1);

                        int firstOpfirst;
                        if (biggest < 0) {
                            firstOpfirst = Math.abs(biggest) * 2 - 1;
                        } else {
                            firstOpfirst = biggest * 2;
                        }
                        int firstOpsecond;
                        if (bplusOneMarker < 0) {
                            firstOpsecond = Math.abs(bplusOneMarker) * 2;
                        } else {
                            firstOpsecond = bplusOneMarker * 2 - 1;
                        }
                        int secondOpsecond;
                        if (markerRightOfB < 0) {
                            secondOpsecond = Math.abs(markerRightOfB) * 2;
                        } else {
                            secondOpsecond = markerRightOfB * 2 - 1;
                        }
                        int secondOpfirst;
                        if (markerLeftOfBplusOne == 0) {
                            secondOpfirst = secondOpsecond;
                        } else if (markerLeftOfBplusOne < 0) {
                            secondOpfirst = Math.abs(markerLeftOfBplusOne) * 2 - 1;
                        } else {
                            secondOpfirst = markerLeftOfBplusOne * 2;
                        }
                        ArrayList<Integer> genomeList = splaytree.traverseTree();
                        myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                        genomeList.remove(0);
                        int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                        operationslist.addOperation(new Pair<Integer, Integer>(firstOpfirst, firstOpsecond));
                        operationslist.addOperation(new Pair<Integer, Integer>(secondOpsecond, secondOpsecond));
                        operationslist.trimAdjArrayList();
                        operationslist.addAdjacencyArrayG1(adjacenciesG1);

                        int markerForTrans = splaytree.findIthNode(1).getMarker();

                        int leftPos = firstNode.getPosition();
                        SplayNode leftOfIplusOne = splaytree.findIthNode(leftPos - 2);
                        leftOfIplusOne.splay();
                        int markerLeftofIplusOne = leftOfIplusOne.getMarker();

                        int secopsnd;
                        if (markerForTrans < 0) {
                            secopsnd = Math.abs(markerForTrans) * 2;
                        } else {
                            secopsnd = markerForTrans * 2 - 1;
                        }
                        int secopfst;
                        if (markerLeftofIplusOne == 0) {
                            secopfst = secopsnd;
                        } else if (markerLeftofIplusOne < 0) {
                            secopfst = Math.abs(markerLeftofIplusOne) * 2 - 1;
                        } else {
                            secopfst = markerLeftofIplusOne * 2;
                        }

                        operationslist.addOperation(new Pair<Integer, Integer>(secopfst, secopsnd));
                        operationslist.addOperation(new Pair<Integer, Integer>(1, 1));
                        this.translocate(markerForTrans, 2, 1, splaytree.getChromosome(firstNode) + 1);
                    } else {
                        biggestNode.splay();
                        int bMarker = biggestNode.getMarker();
                        //bplusOne == null, m is highest Marker in the whole genome

                        //Two Revisions
                        if ((bMarker > 0) && (bplusOneMarker < 0)) {
                            int posB = biggestNode.getPosition();
                            SplayNode rightofBNode = splaytree.findIthNode(posB);
                            rightofBNode.splay();
                            int markerRightOfM = rightofBNode.getMarker();

                            int posBplusOne = bplusOneNode.getPosition();
                            SplayNode rightOfBplusOne = splaytree.findIthNode(posBplusOne);
                            rightOfBplusOne.splay();
                            int markerRightOfBplusOne = rightOfBplusOne.getMarker();

                            int secondOpfirst;
                            int secondOpsecond;
                            if (markerRightOfM < 0) {
                                secondOpfirst = Math.abs(markerRightOfM) * 2;
                            } else {
                                secondOpfirst = markerRightOfM * 2 - 1;
                            }

                            if (markerRightOfBplusOne == 0) {
                                secondOpsecond = secondOpfirst;
                            } else if (markerRightOfBplusOne < 0) {
                                secondOpsecond = Math.abs(markerRightOfBplusOne) * 2;
                            } else {
                                secondOpsecond = markerRightOfBplusOne * 2 - 1;
                            }
                            operationslist.addOperation(new Pair<Integer, Integer>(bMarker * 2, Math.abs(bplusOneMarker) * 2 - 1));
                            operationslist.addOperation(new Pair<Integer, Integer>(secondOpfirst, secondOpsecond));
                            this.inverse(markerRightOfM, bplusOneMarker);

                            ArrayList<Integer> genomeList = splaytree.traverseTree();
                            myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                            genomeList.remove(0);
                            int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                            operationslist.addAdjacencyArrayG1(adjacenciesG1);

                            SplayNode inversNode = splaytree.findIthNode(1);
                            inversNode.splay();
                            int inversMarker = inversNode.getMarker();

                            int posofI = firstNode.getPosition();
                            SplayNode rightOfINode = splaytree.findIthNode(posofI);
                            rightOfINode.splay();
                            int markerRightOfI = rightOfINode.getMarker();

                            int secOpfst;
                            int secOpsnd;

                            if (inversMarker < 0) {
                                secOpfst = Math.abs(inversMarker) * 2;
                            } else {
                                secOpfst = inversMarker * 2 - 1;
                            }

                            if (markerRightOfI == 0) {
                                secOpsnd = secOpfst;
                            } else if (markerRightOfI < 0) {
                                secOpsnd = Math.abs(markerRightOfI) * 2;
                            } else {
                                secOpsnd = markerRightOfI * 2 - 1;
                            }
                            operationslist.addOperation(new Pair<Integer, Integer>(secOpfst, secOpsnd));
                            operationslist.addOperation(new Pair<Integer, Integer>(1, 1));
                            this.inverse(inversMarker, 1);

                        } else if ((bMarker < 0) && (bplusOneMarker) > 0) {
                            int posB = biggestNode.getPosition();
                            SplayNode leftofBNode = splaytree.findIthNode(posB - 2);
                            leftofBNode.splay();
                            int markerLeftOfM = leftofBNode.getMarker();

                            int posBplusOne = bplusOneNode.getPosition();
                            SplayNode leftOfBplusOne = splaytree.findIthNode(posBplusOne - 2);
                            leftOfBplusOne.splay();
                            int markerLeftOfBplusOne = leftOfBplusOne.getMarker();

                            int firstOpfirst;
                            int firstOpsecond;
                            if (markerLeftOfBplusOne < 0) {
                                firstOpsecond = Math.abs(markerLeftOfBplusOne) * 2 - 1;
                            } else {
                                firstOpsecond = markerLeftOfBplusOne * 2;
                            }

                            if (markerLeftOfM == 0) {
                                firstOpfirst = firstOpsecond;
                            } else if (markerLeftOfM < 0) {
                                firstOpfirst = Math.abs(markerLeftOfM) * 2 - 1;
                            } else {
                                firstOpfirst = markerLeftOfM * 2;
                            }

                            operationslist.addOperation(new Pair<Integer, Integer>(firstOpfirst, firstOpsecond));
                            operationslist.addOperation(new Pair<Integer, Integer>(bMarker * 2, bplusOneMarker * 2 - 1));
                            this.inverse(Math.abs(bMarker), markerLeftOfBplusOne);

                            ArrayList<Integer> genomeList = splaytree.traverseTree();
                            myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                            genomeList.remove(0);
                            int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                            operationslist.addAdjacencyArrayG1(adjacenciesG1);

                            SplayNode inversNode = splaytree.findIthNode(1);
                            inversNode.splay();
                            int inversMarker = inversNode.getMarker();

                            int posofI = firstNode.getPosition();
                            SplayNode rightOfINode = splaytree.findIthNode(posofI);
                            rightOfINode.splay();
                            int markerRightOfI = rightOfINode.getMarker();

                            int secondOpfst;
                            int secondOpsnd;

                            if (inversMarker < 0) {
                                secondOpfst = Math.abs(inversMarker) * 2;
                            } else {
                                secondOpfst = inversMarker * 2 - 1;
                            }

                            if (markerRightOfI < 0) {
                                secondOpsnd = Math.abs(markerRightOfI) * 2;
                            } else {
                                secondOpsnd = markerRightOfI * 2 - 1;
                            }

                            operationslist.addOperation(new Pair<Integer, Integer>(secondOpfst, secondOpsnd));
                            operationslist.addOperation(new Pair<Integer, Integer>(1, 1));
                            this.inverse(inversMarker, 1);
                        } else if (bMarker > 0) {

                            int posB = biggestNode.getPosition();
                            int posIplusOne = firstNode.getPosition();

                            int startA = splaytree.findIthNode(1).getMarker();

                            int startB = 0;
                            int stoppB = 0;

                            if (!(posIplusOne == posB + 1)) {
                                startB = splaytree.findIthNode(posB).getMarker();
                                stoppB = splaytree.findIthNode(posIplusOne - 2).getMarker();
                            }

                            int secondOpfirst;
                            int secondOpsecond;
                            if (startA < 0) {
                                secondOpfirst = Math.abs(startA) * 2;
                            } else {
                                secondOpfirst = startA * 2 - 1;
                            }

                            if (stoppB == 0) {
                                secondOpsecond = bMarker * 2;
                            } else if (stoppB < 0) {
                                secondOpsecond = Math.abs(stoppB) * 2 - 1;
                            } else {
                                secondOpsecond = stoppB * 2;
                            }


                            operationslist.addOperation(new Pair<Integer, Integer>(secondOpfirst, secondOpsecond));
                            operationslist.addOperation(new Pair<Integer, Integer>(1, 1));
                            operationslist.trimAdjArrayList();
                            Pair<SplayNode, SplayNode> firstex = blockexchangePart1(startA, 1);


                            //create genome for operationslist, blockexchange takes two DCJ operations.
                            ArrayList<Integer> lineargenomeList = firstex.getFirst().traverse(firstex.getFirst().isReverseFlag());
                            ArrayList<Integer> circIntermediate = firstex.getSecond().traverse(firstex.getSecond().isReverseFlag());

                            lineargenomeList.remove(0);
                            int[] adjacenciesG1 = this.createAdjacencyGraph(lineargenomeList, circIntermediate);

                            operationslist.addAdjacencyArrayG1(adjacenciesG1);

                            SplayNode stoppCNode;
                            int stoppCMarker;
                            int biggestplusOne;
                            if (bMarker == genomelenght) {
                                biggestplusOne = genomelenght + bChromNum + 1;
                                SplayNode chromafterC = splaytree.getNode(genomelenght + bplusChromNum + 1);
                                int chrompos = chromafterC.getPosition();
                                stoppCNode = splaytree.findIthNode(chrompos - 2);
                                stoppCNode.splay();
                                stoppCMarker = stoppCNode.getMarker();
                            } else {
                                int bplusOnepos = bplusOneNode.getPosition();
                                stoppCNode = splaytree.findIthNode(bplusOnepos - 2);
                                stoppCNode.splay();
                                stoppCMarker = stoppCNode.getMarker();
                                biggestplusOne = biggest + 1;
                            }

                            int firstOpfst;
                            int firstOpsnd;

                            int secOpsnd;
                            if (stoppCMarker < 0) {
                                firstOpfst = Math.abs(stoppCMarker) * 2 - 1;
                            } else {
                                firstOpfst = stoppCMarker * 2;
                            }

                            if (startB == 0) {
                                firstOpsnd = secondOpfirst;
                            } else if (startB < 0) {
                                firstOpsnd = Math.abs(startB) * 2;
                            } else {
                                firstOpsnd = startB * 2 - 1;
                            }

                            if (bMarker == genomelenght) {
                                secOpsnd = bMarker * 2;
                            } else if (bplusOneMarker < 0) {
                                secOpsnd = Math.abs(bplusOneMarker) * 2;
                            } else {
                                secOpsnd = bplusOneMarker * 2 - 1;
                            }

                            operationslist.addOperation(new Pair<Integer, Integer>(firstOpfst, firstOpsnd));
                            operationslist.addOperation(new Pair<Integer, Integer>(bMarker * 2, secOpsnd));

                            //second part of the blockexchange, the circulare intermidiate is reincorporated into the chromosoms
                            blockExchangePart2(startA, startB, stoppB, biggestplusOne);

                        } else {

                            if (bMarker == -genomelenght) {

                                int positionM = biggestNode.getPosition();
                                SplayNode leftOfMNode = splaytree.findIthNode(positionM - 2);
                                leftOfMNode.splay();
                                int markerLeftOfM = leftOfMNode.getMarker();


                                int chromPosition = splaytree.getNode(genomelenght + bChromNum + 1).getPosition();
                                SplayNode chromMinusOne = splaytree.findIthNode(chromPosition - 2);
                                int chromMarker = Math.abs(chromMinusOne.getMarker());

                                int firstOpfirst;
                                int firstOpsecond;

                                if (markerLeftOfM < 0) {
                                    firstOpfirst = Math.abs(markerLeftOfM) * 2 - 1;
                                } else {
                                    firstOpfirst = markerLeftOfM * 2;
                                }

                                if (chromMarker < 0) {
                                    firstOpsecond = Math.abs(chromMarker) * 2 - 1;
                                } else {
                                    firstOpsecond = chromMarker * 2;
                                }

                                operationslist.addOperation(new Pair<Integer, Integer>(firstOpfirst, firstOpsecond));
                                operationslist.addOperation(new Pair<Integer, Integer>(Math.abs(bMarker) * 2, Math.abs(bMarker) * 2));
                                operationslist.trimAdjArrayList();
                                this.inverse(bMarker, chromMarker);

                                ArrayList<Integer> genomeList = splaytree.traverseTree();
                                myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                                genomeList.remove(0);
                                int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                                operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                SplayNode inverseStartNode = splaytree.findIthNode(1);
                                inverseStartNode.splay();
                                int inversefrom = inverseStartNode.getMarker();
                                int secPos = firstNode.getPosition();
                                SplayNode rightofSecond = splaytree.findIthNode(secPos);
                                rightofSecond.splay();
                                int rightofSecMarker = rightofSecond.getMarker();

                                int secOpfst;
                                if (inversefrom < 0) {
                                    secOpfst = Math.abs(inversefrom) * 2;
                                } else {
                                    secOpfst = inversefrom * 2 - 1;
                                }
                                int secOpsnd;
                                if (rightofSecMarker < 0) {
                                    secOpsnd = Math.abs(rightofSecMarker) * 2 - 1;
                                } else {
                                    secOpsnd = rightofSecMarker * 2;
                                }

                                operationslist.addOperation(new Pair<Integer, Integer>(secOpfst, secOpsnd));
                                operationslist.addOperation(new Pair<Integer, Integer>(1, 1));

                                this.inverse(inversefrom, 1);
                            } else {
                                int posB = biggestNode.getPosition();
                                int posIplusOne = firstNode.getPosition();

                                SplayNode leftOfM = splaytree.findIthNode(posB - 2);
                                leftOfM.splay();
                                int markerleftOfM = leftOfM.getMarker();


                                int startA = splaytree.findIthNode(1).getMarker();

                                int startB = 0;
                                int stoppB = 0;

                                if (!(posB == 2)) {
                                    startB = biggestNode.getMarker();
                                    stoppB = splaytree.findIthNode(posIplusOne - 2).getMarker();
                                }

                                int positionC = bplusOneNode.getPosition();
                                int stoppCplus = splaytree.findIthNode(positionC).getMarker();

                                int secondOpfirst;
                                if (startA < 0) {
                                    secondOpfirst = Math.abs(startA) * 2;
                                } else {
                                    secondOpfirst = startA * 2 - 1;
                                }
                                int secondOpsecond;
                                if (stoppB == 0) {
                                    secondOpsecond = Math.abs(bMarker) * 2 - 1;
                                } else if (stoppB < 0) {
                                    secondOpsecond = Math.abs(stoppB) * 2 - 1;
                                } else {
                                    secondOpsecond = stoppB * 2;
                                }

                                operationslist.addOperation(new Pair<Integer, Integer>(secondOpfirst, secondOpsecond));
                                operationslist.addOperation(new Pair<Integer, Integer>(1, 1));

                                Pair<SplayNode, SplayNode> firstex = blockexchangePart1(startA, 1);

                                //create genome for operationslist, blockexchange takes two DCJ operations.
                                ArrayList<Integer> lineargenomeList = firstex.getFirst().traverse(firstex.getFirst().isReverseFlag());
                                lineargenomeList.remove(0);
                                ArrayList<Integer> circIntermediate = firstex.getSecond().traverse(firstex.getSecond().isReverseFlag());

                                int[] adjacenciesG1 = this.createAdjacencyGraph(lineargenomeList, circIntermediate);
                                operationslist.trimAdjArrayList();
                                operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                int secOpfst;
                                int secOpsnd;
                                if (markerleftOfM == 0) {
                                    if (stoppB == 0) {
                                        secOpfst = Math.abs(bMarker) * 2 - 1;
                                    } else if (stoppB < 0) {
                                        secOpfst = Math.abs(stoppB) * 2 - 1;
                                    } else {
                                        secOpfst = stoppB * 2;
                                    }
                                } else if (markerleftOfM < 0) {
                                    secOpfst = Math.abs(markerleftOfM) * 2 - 1;
                                } else {
                                    secOpfst = markerleftOfM * 2;
                                }

                                if (stoppCplus == 0) {
                                    int chromOfC = splaytree.getChromosome(bplusOneNode);
                                    stoppCplus = chromOfC + genomelenght + 1;
                                    secOpsnd = secOpfst;
                                } else if (stoppCplus < 0) {
                                    secOpsnd = Math.abs(stoppCplus) * 2;
                                } else {
                                    secOpsnd = stoppCplus * 2 - 1;
                                }

                                Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(bplusOneMarker * 2 - 1, bMarker * 2); 
                                Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secOpfst, secOpsnd);
                                if (fstPair.getFirst() == fstPair.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair;
                                    fstPair = scndPair;
                                    scndPair = intermedPair;
                                }
                                operationslist.addOperation(fstPair);
                                operationslist.addOperation(scndPair);

                                //second part of the blockexchange, the circulare intermidiate is reincorporated into the chromosoms
                                blockExchangePart2(startA, startB, stoppB, stoppCplus);

                            }

                        }
                    }
                }
            }

            ArrayList<Integer> firstgenomeList = splaytree.traverseTree();

            boolean firstchanged = this.changeChromosomOrientation(firstgenomeList);
            if (firstchanged) {
                firstgenomeList = splaytree.traverseTree();
            }

            myGenomeAList = (ArrayList<Integer>) firstgenomeList.clone();
            firstgenomeList.remove(0);

            int[] firstadjacenciesG1 = this.createAdjacencyGraph(firstgenomeList, null);
            operationslist.trimAdjArrayList();
            operationslist.addAdjacencyArrayG1(firstadjacenciesG1);

        }


        //Tells an which chromsom in the genome we are at the moment
        int currentChromosome = 0;
        // boolean firstRun = true;

        for (int i = 1; i < myGenomeBList.size() - 1; i++) {

            if ((myGenomeAList.get(i) == 0) && (myGenomeBList.get(i) == 0)) {

                currentChromosome++;

                int lastchangedMarker = myGenomeAList.get(i - 1);
                SplayNode leftofTelo = splaytree.getNode(lastchangedMarker);
                SplayNode rightofTelo = splaytree.getNode(lastchangedMarker + 1);

                int leftChrom = splaytree.getChromosome(leftofTelo);
                int rightChrom = splaytree.getChromosome(rightofTelo);

                if ((rightChrom - leftChrom) != 1) {

                    SplayNode leftChromNode = splaytree.getNode(genomelenght + leftChrom + 1);
                    int leftChromPos = leftChromNode.getPosition();
                    SplayNode leftNode = splaytree.findIthNode(leftChromPos);
                    leftNode.splay();
                    int leftTrans = leftNode.getMarker();

                    SplayNode rightChromNode = splaytree.getNode(genomelenght + rightChrom);
                    int rChromPos = rightChromNode.getPosition();
                    SplayNode rightNode = splaytree.findIthNode(rChromPos);
                    rightNode.splay();
                    int rightTrans = rightNode.getMarker();

                    this.translocate(leftTrans, leftChrom + 2, rightTrans, rightChrom + 1);
                    myGenomeAList = splaytree.traverseTree();
                }

            }
            //Fission only at the end of the sorting scenario!
            if ((myGenomeBList.get(i + 1) == 0) && (myGenomeAList.get(i + 1) != 0)  ) {
//                   && myGenomeBList.get(this.posInBList[myGenomeAList.get(i+1)-1]) == 0) {

                SplayNode secNode = splaytree.getNode(i + 1 - currentChromosome);
                secNode.splay();
                int secMarker = secNode.getMarker();
                int secOp;
                if (secMarker < 0) {
                    secOp = secMarker * 2;
                } else {
                    secOp = secMarker * 2 - 1;
                }
                this.performFission(splaytree.getNode(i - currentChromosome));

                ArrayList<Integer> genomeList = splaytree.traverseTree();

                boolean changed = this.changeChromosomOrientation(genomeList);
                if (changed) {
                    genomeList = splaytree.traverseTree();
                }

                myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                genomeList.remove(0);

                int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);

                Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>((i - currentChromosome) * 2, (i - currentChromosome) * 2);
                Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secOp, secOp);
                if (fstPair.getFirst() == fstPair.getSecond()) {
                    Pair<Integer, Integer> intermedPair = fstPair;
                    fstPair = scndPair;
                    scndPair = intermedPair;
                }
                
                operationslist.addOperation(fstPair);
                operationslist.addOperation(scndPair);
                operationslist.trimAdjArrayList();
                operationslist.addAdjacencyArrayG1(adjacenciesG1);

//            } else //special fission case, no fission here!
//                if (myGenomeBList.get(i + 1) == 0 && myGenomeAList.get(i + 1) != 0) {

            } else if (!(myGenomeAList.get(i + 1).equals(myGenomeBList.get(i + 1)))) {

                int markertoChange = i - currentChromosome;
                SplayNode first = splaytree.getNode(markertoChange);
                SplayNode second = splaytree.getNode(markertoChange + 1);

                second.splay();
                int secondMarker = second.getMarker();

                int firstChromNum = splaytree.getChromosome(first);
                int secondChromNum = splaytree.getChromosome(second);
                if ((myGenomeAList.get(i) == 0) && (secondChromNum - firstChromNum == 1)) {
                    firstChromNum = secondChromNum;
                }
                //Translokation
                if (firstChromNum != secondChromNum) {
                    int transPos = first.getPosition();
                    SplayNode transNode = splaytree.findIthNode(transPos);
                    transNode.splay();
                    int transmarker = transNode.getMarker();
                    int secondPos = second.getPosition();
                    int secondleftMarker = splaytree.findIthNode(secondPos - 2).getMarker();

                    //negativ translokation
                    if (secondMarker < 0) {
                        int secondOpfirst;
                        int secondOpsecond;

                        SplayNode rightOfSecond = splaytree.findIthNode(secondPos);
                        rightOfSecond.splay();
                        int rightofSec = rightOfSecond.getMarker();

                        if (transmarker < 0) {
                            secondOpfirst = Math.abs(transmarker) * 2;
                        } else {
                            secondOpfirst = transmarker * 2 - 1;
                        }
                        if (rightofSec < 0) {
                            secondOpsecond = Math.abs(rightofSec) * 2;
                        } else {
                            secondOpsecond = rightofSec * 2 - 1;
                        }
                        
                        Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(markertoChange * 2, Math.abs(secondMarker) * 2 - 1);
                        Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                        if (fstPair.getFirst() == fstPair.getSecond()) {
                            Pair<Integer, Integer> intermedPair = fstPair;
                            fstPair = scndPair;
                            scndPair = intermedPair;
                        }
                        
                        operationslist.addOperation(fstPair);
                        operationslist.addOperation(scndPair);
                        this.negativTranslocate(transmarker, firstChromNum + 1, markertoChange + 1, secondChromNum);
                        //Fusion
                    } else if ((transmarker == 0 && secondleftMarker == 0) && (this.myGenomeBList.get(i) != 0)) {

                        int lastchangedMarker = myGenomeAList.get(i);
                        SplayNode leftofTelo = splaytree.getNode(lastchangedMarker);
                        SplayNode rightofTelo = splaytree.getNode(lastchangedMarker + 1);

                        int leftChrom = splaytree.getChromosome(leftofTelo);
                        int rightChrom = splaytree.getChromosome(rightofTelo);

                        if ((rightChrom - leftChrom) != 1) {

                            SplayNode leftChromNode = splaytree.getNode(genomelenght + leftChrom + 1);
                            int leftChromPos = leftChromNode.getPosition();
                            SplayNode leftNode = splaytree.findIthNode(leftChromPos);
                            leftNode.splay();
                            int leftTrans = leftNode.getMarker();

                            SplayNode rightChromNode = splaytree.getNode(genomelenght + rightChrom);
                            int rChromPos = rightChromNode.getPosition();
                            SplayNode rightNode = splaytree.findIthNode(rChromPos);
                            rightNode.splay();
                            int rightTrans = rightNode.getMarker();

                            this.translocate(leftTrans, leftChrom + 2, rightTrans, rightChrom + 1);

                        }

                        int secondOp;
                        if (secondMarker < 0) {
                            secondOp = Math.abs(secondMarker) * 2;
                        } else {
                            secondOp = secondMarker * 2 - 1;
                        }
                        operationslist.addOperation(new Pair<Integer, Integer>(markertoChange * 2, secondOp));
                        operationslist.addOperation(new Pair<Integer, Integer>(0, 0));
                        this.performFusion(first.getMarker(), secondMarker);
                        //normal translokation
                    } else {
                        int secondOpFirst;
                        int secondOpSecond;
                        if (secondleftMarker < 0) {
                            secondOpFirst = Math.abs(secondleftMarker) * 2 - 1;
                        } else {
                            secondOpFirst = secondleftMarker * 2;
                        }
                        if (transmarker < 0) {
                            secondOpSecond = Math.abs(transmarker) * 2;
                        } else {
                            secondOpSecond = transmarker * 2 - 1;
                        }
                                  
                        Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(markertoChange * 2, secondMarker * 2 - 1);
                        Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpFirst, secondOpSecond);
                        if (fstPair.getFirst() == fstPair.getSecond()) {
                            Pair<Integer, Integer> intermedPair = fstPair;
                            fstPair = scndPair;
                            scndPair = intermedPair;
                        }
                        operationslist.addOperation(fstPair);
                        operationslist.addOperation(scndPair);
                        translocate(transmarker, firstChromNum + 1, markertoChange + 1, secondChromNum + 1);
                    }
                } else {
                    // 15 14 13 -> 13 14 15, change Direction of chromosom in tree to -13 -14 -15
                    int secondPos = second.getPosition();
                    SplayNode rightOfSecondNode = splaytree.findIthNode(secondPos);
                    rightOfSecondNode.splay();
                    int rightMarker = rightOfSecondNode.getMarker();
                    if ((rightMarker == 0) && (myGenomeBList.get(i) == 0)) {
                        SplayNode secChromosom = splaytree.getNode(genomelenght + secondChromNum);
                        int chromosomPosition = secChromosom.getPosition();
                        SplayNode rightOfChromosom = splaytree.findIthNode(chromosomPosition);
                        rightOfChromosom.splay();
                        int inversefromPos = rightOfChromosom.getMarker();
                        if (inversefromPos != secondMarker) {
                            this.inverse(inversefromPos, secondMarker);
                        }
                    }
                    //Revision
                    second.splay();
                    secondMarker = second.getMarker();
                    if (secondMarker < 0) {
                        int inversPos = first.getPosition();
                        SplayNode nodeInfrom = splaytree.findIthNode(inversPos);
                        nodeInfrom.splay();
                        int inversefrom = Math.abs(nodeInfrom.getMarker());
                        int firstOpfirst;
                        int secondOpfirst;
                        int secondOpsecond;
                        int secPos = second.getPosition();
                        SplayNode rightofSecond = splaytree.findIthNode(secPos);
                        rightofSecond.splay();
                        int rightofSecMarker = rightofSecond.getMarker();
                        if (inversefrom == 0) {
                            firstOpfirst = (markertoChange + 1) * 2 - 1;
                            int telo = splaytree.getChromosome(second) + genomelenght;
                            int teloPos = splaytree.getNode(telo).getPosition();
                            inversefrom = splaytree.findIthNode(teloPos).getMarker();
                        } else {
                            firstOpfirst = markertoChange * 2;
                        }
                        if (inversefrom < 0) {
                            secondOpfirst = Math.abs(inversefrom) * 2;
                        } else {
                            secondOpfirst = inversefrom * 2 - 1;
                        }
                        if (rightofSecMarker == 0) {
                            secondOpsecond = secondOpfirst;
                        } else if (rightofSecMarker < 0) {
                            secondOpsecond = Math.abs(rightofSecMarker) * 2;
                        } else {
                            secondOpsecond = rightofSecMarker * 2 - 1;
                        }
                             
                        Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, (markertoChange + 1) * 2 - 1);
                        Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                        if (fstPair.getFirst() == fstPair.getSecond()) {
                            Pair<Integer, Integer> intermedPair = fstPair;
                            fstPair = scndPair;
                            scndPair = intermedPair;
                        }
                        operationslist.addOperation(fstPair);
                        operationslist.addOperation(scndPair);

                        this.inverse(inversefrom, markertoChange + 1);
                    } else {
                        //nicht groesstes Kind, nur groesstes vor dem Marker.
                        int m = 0;
                        if (second.isReverseFlag()) {
                            m = second.getRightChild().getBiggestChild();
                        } else {
                            m = second.getLeftChild().getBiggestChild();
                        }
                        SplayNode mNode = splaytree.getNode(m);
                        int mNodeChromNum = splaytree.getChromosome(mNode);

                        mNode.splay();
                        int mMarker = mNode.getMarker();

                        SplayNode mNodeTelomere = splaytree.getNode(genomelenght + mNodeChromNum + 1);
                        mNodeTelomere.splay();
                        int biggestChild;
                        if (mNodeTelomere.isReverseFlag()) {
                            biggestChild = mNodeTelomere.getRightChild().getBiggestChild();
                        } else {
                            biggestChild = mNodeTelomere.getLeftChild().getBiggestChild();
                        }

                        SplayNode mplusOneNode;
                        int mplusChromNum;
                        int mplusOneMarker = 0;

                        if (Math.abs(mMarker) != biggestChild) {
                            mplusOneNode = splaytree.getNode(m + 1);
                            mplusChromNum = splaytree.getChromosome(mplusOneNode);
                            mplusOneNode.splay();
                            mplusOneMarker = mplusOneNode.getMarker();
                        } else {
                            mplusOneNode = null;
                            mplusChromNum = mNodeChromNum;
                        }


                        //Two Translokations, to move m+1 next to m and afterwards to move i+1 next to i
                        if (mNodeChromNum != mplusChromNum) {
                            int posOfM = mNode.getPosition();
                            SplayNode rightOfMNode = splaytree.findIthNode(posOfM);
                            rightOfMNode.splay();
                            int markerRightOfM = rightOfMNode.getMarker();

                            int posOfMplusOne = mplusOneNode.getPosition();
                            SplayNode leftOfMplusOne = splaytree.findIthNode(posOfMplusOne - 2);
                            leftOfMplusOne.splay();
                            int markerLeftOfMplusOne = leftOfMplusOne.getMarker();

                            int firstOpfirst;
                            if (mMarker < 0) {
                                firstOpfirst = Math.abs(mMarker) * 2 - 1;
                            } else {
                                firstOpfirst = mMarker * 2;
                            }
                            int firstOpsecond;
                            if (mplusOneMarker < 0) {
                                firstOpsecond = Math.abs(mplusOneMarker) * 2;
                            } else {
                                firstOpsecond = mplusOneMarker * 2 - 1;
                            }
                            int secondOpsecond;
                            if (markerRightOfM < 0) {
                                secondOpsecond = Math.abs(markerRightOfM) * 2;
                            } else {
                                secondOpsecond = markerRightOfM * 2 - 1;
                            }
                            int secondOpfirst;
                            if (markerLeftOfMplusOne == 0) {
                                secondOpfirst = secondOpsecond;
                            } else if (markerLeftOfMplusOne < 0) {
                                secondOpfirst = Math.abs(markerLeftOfMplusOne) * 2 - 1;
                            } else {
                                secondOpfirst = markerLeftOfMplusOne * 2;
                            }

                            Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, firstOpsecond);
                            Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                            if (fstPair.getFirst() == fstPair.getSecond()) {
                                Pair<Integer, Integer> intermedPair = fstPair;
                                fstPair = scndPair;
                                scndPair = intermedPair;
                            }
                            operationslist.addOperation(fstPair);
                            operationslist.addOperation(scndPair);
                            this.translocate(markerRightOfM, mNodeChromNum + 1, m + 1, mplusChromNum + 1);


                            ArrayList<Integer> genomeList = splaytree.traverseTree();
                            myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                            genomeList.remove(0);

                            int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                            operationslist.trimAdjArrayList();
                            operationslist.addAdjacencyArrayG1(adjacenciesG1);

                            int secChromNum = splaytree.getChromosome(second);
                            int transPos = first.getPosition();
                            SplayNode rightOfI = splaytree.findIthNode(transPos);
                            rightOfI.splay();
                            int transmarker = rightOfI.getMarker();

                            int firstOpfst;

                            //For the case, that the gene to translocate is left of a telomere
                            //For example A: 1 2 3 | 5 6 | 4 7 B: 1 2 3 | 4 5 6 7 

                            if (transmarker == 0) {
                                firstOpfst = (markertoChange + 1) * 2 - 1;
                                SplayNode newRightofI = splaytree.findIthNode(transPos + 1);
                                newRightofI.splay();
                                transmarker = newRightofI.getMarker();
                            } else {
                                firstOpfst = markertoChange * 2;
                            }


                            int leftPos = second.getPosition();
                            SplayNode leftOfIplusOne = splaytree.findIthNode(leftPos - 2);
                            leftOfIplusOne.splay();
                            int markerLeftofIplusOne = leftOfIplusOne.getMarker();

                            int secopsnd;
                            if (transmarker < 0) {
                                secopsnd = Math.abs(transmarker) * 2;
                            } else {
                                secopsnd = transmarker * 2 - 1;
                            }
                            int secopfst;
                            if (markerLeftofIplusOne == 0) {
                                secopfst = secopsnd;
                            } else if (markerLeftofIplusOne < 0) {
                                secopfst = Math.abs(markerLeftofIplusOne) * 2 - 1;
                            } else {
                                secopfst = markerLeftofIplusOne * 2;
                            }

                            Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(firstOpfst, (markertoChange + 1) * 2 - 1);
                            Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(secopfst, secopsnd);
                            if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                Pair<Integer, Integer> intermedPair = fstPair2;
                                fstPair2 = scndPair2;
                                scndPair2 = intermedPair;
                            }
                            operationslist.addOperation(fstPair2);
                            operationslist.addOperation(scndPair2);
                            this.translocate(transmarker, mNodeChromNum + 1, markertoChange + 1, secChromNum + 1);

                        } else {
                            //m and m+1 have a different orientation
                            //Two Reversals
                            if ((mMarker > 0) && (mplusOneMarker < 0)) {
                                int positionM = mNode.getPosition();
                                SplayNode rightOfMNode = splaytree.findIthNode(positionM);
                                rightOfMNode.splay();
                                int markerRightOfM = rightOfMNode.getMarker();

                                int mplusPos = mplusOneNode.getPosition();
                                SplayNode rightOfMplusOne = splaytree.findIthNode(mplusPos);
                                rightOfMplusOne.splay();
                                int markerRightOfMplusOne = rightOfMplusOne.getMarker();

                                int secondOpfirst;
                                int secondOpsecond;

                                if (markerRightOfM < 0) {
                                    secondOpfirst = Math.abs(markerRightOfM) * 2;
                                } else {
                                    secondOpfirst = markerRightOfM * 2 - 1;
                                }

                                if (markerRightOfMplusOne == 0) {
                                    secondOpsecond = secondOpfirst;
                                } else if (markerRightOfMplusOne < 0) {
                                    secondOpsecond = Math.abs(markerRightOfMplusOne) * 2 - 1;
                                } else {
                                    secondOpsecond = markerRightOfMplusOne * 2;
                                }

                                Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(mMarker * 2, Math.abs(mplusOneMarker) * 2 - 1);
                                Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                                if (fstPair.getFirst() == fstPair.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair;
                                    fstPair = scndPair;
                                    scndPair = intermedPair;
                                }
                                operationslist.addOperation(fstPair);
                                operationslist.addOperation(scndPair);
                                this.inverse(markerRightOfM, mplusOneMarker);

                                ArrayList<Integer> genomeList = splaytree.traverseTree();
                                myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                                genomeList.remove(0);
                                int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                                operationslist.trimAdjArrayList();
                                operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                int secPos = second.getPosition();
                                SplayNode rightofSecond = splaytree.findIthNode(secPos);
                                rightofSecond.splay();
                                int rightofSecMarker = rightofSecond.getMarker();

                                int firstOpfst;
                                int inversPos = first.getPosition();
                                SplayNode nodeInfrom = splaytree.findIthNode(inversPos);
                                if (nodeInfrom.getMarker() == 0) {
                                    nodeInfrom = splaytree.findIthNode(inversPos + 1);
                                    firstOpfst = (markertoChange + 1) * 2 - 1;
                                } else {
                                    firstOpfst = markertoChange * 2;
                                }
                                nodeInfrom.splay();
                                int inversefrom = nodeInfrom.getMarker();

                                int secOpfst;
                                if (inversefrom < 0) {
                                    secOpfst = Math.abs(inversefrom) * 2;
                                } else {
                                    secOpfst = inversefrom * 2 - 1;
                                }
                                int secOpsnd;
                                if (rightofSecMarker == 0) {
                                    secOpsnd = secOpfst;
                                } else if (rightofSecMarker < 0) {
                                    secOpsnd = Math.abs(rightofSecMarker) * 2 - 1;
                                } else {
                                    secOpsnd = rightofSecMarker * 2;
                                }

                                Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(firstOpfst, (markertoChange + 1) * 2 - 1);
                                Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(secOpfst, secOpsnd);
                                if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair2;
                                    fstPair2 = scndPair2;
                                    scndPair2 = intermedPair;
                                }
                                operationslist.addOperation(fstPair2);
                                operationslist.addOperation(scndPair2);
                                this.inverse(inversefrom, markertoChange + 1);
                            } else if ((mMarker < 0) && (mplusOneMarker > 0)) {
                                int positionM = mNode.getPosition();
                                SplayNode leftOfMNode = splaytree.findIthNode(positionM - 2);
                                leftOfMNode.splay();
                                int markerLeftOfM = leftOfMNode.getMarker();

                                int mplusPos = mplusOneNode.getPosition();
                                SplayNode mMinusOne = splaytree.findIthNode(mplusPos - 2);
                                mMinusOne.splay();
                                int mMinusMarker = mMinusOne.getMarker();

                                int firstOpsecond;
                                if (mMinusMarker < 0) {
                                    firstOpsecond = Math.abs(mMinusMarker) * 2 - 1;
                                } else {
                                    firstOpsecond = mMinusMarker * 2;
                                }
                                int firstOpfirst;
                                if (markerLeftOfM == 0) {
                                    firstOpfirst = firstOpsecond;
                                } else if (markerLeftOfM < 0) {
                                    firstOpfirst = Math.abs(markerLeftOfM) * 2 - 1;
                                } else {
                                    firstOpfirst = markerLeftOfM * 2;
                                }

                                int secondOpfirst;
                                if (mMarker < 0) {
                                    secondOpfirst = Math.abs(mMarker) * 2;
                                } else {
                                    secondOpfirst = mMarker * 2 - 1;
                                }
                                int secondOpsecond;
                                if (mplusOneMarker < 0) {
                                    secondOpsecond = Math.abs(mplusOneMarker) * 2;
                                } else {
                                    secondOpsecond = mplusOneMarker * 2 - 1;
                                }
                              
                                Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, firstOpsecond);
                                Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                                if (fstPair.getFirst() == fstPair.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair;
                                    fstPair = scndPair;
                                    scndPair = intermedPair;
                                }
                                operationslist.addOperation(fstPair);
                                operationslist.addOperation(scndPair);

                                this.inverse(mMarker, mMinusMarker);

                                ArrayList<Integer> genomeList = splaytree.traverseTree();
                                myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                                genomeList.remove(0);
                                int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                                operationslist.trimAdjArrayList();
                                operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                int secPos = second.getPosition();
                                SplayNode rightofSecond = splaytree.findIthNode(secPos);
                                rightofSecond.splay();
                                int rightofSecMarker = rightofSecond.getMarker();

                                int firstOpfst;
                                int inversPos = first.getPosition();
                                SplayNode nodeInfrom = splaytree.findIthNode(inversPos);
                                if (nodeInfrom.getMarker() == 0) {
                                    nodeInfrom = splaytree.findIthNode(inversPos + 1);
                                    firstOpfst = (markertoChange + 1) * 2 - 1;
                                } else {
                                    firstOpfst = markertoChange * 2;
                                }
                                nodeInfrom.splay();
                                int inversefrom = nodeInfrom.getMarker();

                                int secOpfst;
                                if (inversefrom < 0) {
                                    secOpfst = Math.abs(inversefrom) * 2;
                                } else {
                                    secOpfst = inversefrom * 2 - 1;
                                }
                                int secOpsnd;
                                if (rightofSecMarker < 0) {
                                    secOpsnd = Math.abs(rightofSecMarker) * 2 - 1;
                                } else {
                                    secOpsnd = rightofSecMarker * 2;
                                }
                                
                                Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(firstOpfst, (markertoChange + 1) * 2 - 1);
                                Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(secOpfst, secOpsnd);
                                if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair2;
                                    fstPair2 = scndPair2;
                                    scndPair2 = intermedPair;
                                }
                                operationslist.addOperation(new Pair<Integer, Integer>(firstOpfst, (markertoChange + 1) * 2 - 1));
                                operationslist.addOperation(new Pair<Integer, Integer>(secOpfst, secOpsnd));
                                this.inverse(inversefrom, markertoChange + 1);
                                //m and m+1 both have a positive orientation
                                // Blockexchange
                            } else if (mMarker > 0) {

                                int positionM = mNode.getPosition();
                                int positionIplusOne = second.getPosition();

                                // position rechts von i
                                int positionI = first.getPosition();
                                int startBlockA;
                                SplayNode leftOfI;
                                int firstOpfirst;
                                if (myGenomeBList.get(i) == 0) {
                                    leftOfI = splaytree.findIthNode(positionI + 1);
                                    leftOfI.splay();
                                    startBlockA = leftOfI.getMarker();
                                    firstOpfirst = (markertoChange + 1) * 2 - 1;
                                } else {
                                    leftOfI = splaytree.findIthNode(positionI);
                                    leftOfI.splay();
                                    startBlockA = leftOfI.getMarker();
                                    firstOpfirst = markertoChange * 2;
                                }

                                int startBlockB = 0;
                                int stoppBlockB = 0;

                                if ((positionIplusOne != positionM + 1)) {
                                    int posM = mNode.getPosition();
                                    SplayNode nextToMNode = splaytree.findIthNode(posM);
                                    nextToMNode.splay();
                                    startBlockB = nextToMNode.getMarker();

                                    SplayNode nextToIplusOne = splaytree.findIthNode(positionIplusOne - 2);
                                    nextToIplusOne.splay();
                                    stoppBlockB = nextToIplusOne.getMarker();
                                }

                                int cplusOne = 0;
                                SplayNode stoppCNode = null;
                                int stoppCMarker = 0;
                                if (mMarker == biggestChild) {
                                    cplusOne = genomelenght + mNodeChromNum + 1;
                                    SplayNode chromafterC = splaytree.getNode(genomelenght + mplusChromNum + 1);
                                    int chromPos = chromafterC.getPosition();
                                    stoppCNode = splaytree.findIthNode(chromPos - 2);
                                    stoppCNode.splay();
                                    stoppCMarker = stoppCNode.getMarker();
                                } else {
                                    int mplusOnepos = mplusOneNode.getPosition();
                                    stoppCNode = splaytree.findIthNode(mplusOnepos - 2);
                                    stoppCNode.splay();
                                    stoppCMarker = stoppCNode.getMarker();
                                    cplusOne = m + 1;
                                }

                                int secondOpfirst;
                                int secondOpsecond;
                                if (startBlockA < 0) {
                                    secondOpfirst = Math.abs(startBlockA) * 2;
                                } else {
                                    secondOpfirst = startBlockA * 2 - 1;
                                }

                                if (stoppBlockB == 0) {
                                    secondOpsecond = mMarker * 2;
                                } else if (stoppBlockB < 0) {
                                    secondOpsecond = Math.abs(stoppBlockB) * 2 - 1;
                                } else {
                                    secondOpsecond = stoppBlockB * 2;
                                }

                                Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, (markertoChange + 1) * 2 - 1);
                                Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                                if (fstPair.getFirst() == fstPair.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair;
                                    fstPair = scndPair;
                                    scndPair = intermedPair;
                                }
                                operationslist.addOperation(fstPair);
                                operationslist.addOperation(scndPair);

                                Pair<SplayNode, SplayNode> firstex = blockexchangePart1(startBlockA, markertoChange + 1);


                                //create genome for operationslist, blockexchange takes two DCJ operations.
                                ArrayList<Integer> lineargenomeList = firstex.getFirst().traverse(firstex.getFirst().isReverseFlag());
                                lineargenomeList.remove(0);
                                ArrayList<Integer> circIntermediate = firstex.getSecond().traverse(firstex.getSecond().isReverseFlag());

                                int[] adjacenciesG1 = this.createAdjacencyGraph(lineargenomeList, circIntermediate);
                                operationslist.trimAdjArrayList();
                                operationslist.addAdjacencyArrayG1(adjacenciesG1);


                                int firstOpfst;
                                int firstOpsnd;

                                int secOpsnd;
                                if (stoppCMarker < 0) {
                                    firstOpfst = Math.abs(stoppCMarker) * 2 - 1;
                                } else {
                                    firstOpfst = stoppCMarker * 2;
                                }

                                if (startBlockB == 0) {
                                    firstOpsnd = secondOpfirst;
                                } else if (startBlockB < 0) {
                                    firstOpsnd = Math.abs(startBlockB) * 2;
                                } else {
                                    firstOpsnd = startBlockB * 2 - 1;
                                }

                                if (mMarker == biggestChild) {
                                    secOpsnd = mMarker * 2;
                                } else if (mplusOneMarker < 0) {
                                    secOpsnd = Math.abs(mplusOneMarker) * 2;
                                } else {
                                    secOpsnd = mplusOneMarker * 2 - 1;
                                }

                                //second part of the blockexchange, the circulare intermidiate is reincorporated into the chromosoms
                                Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(firstOpfst, firstOpsnd);
                                Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(mMarker * 2, secOpsnd);
                                if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                    Pair<Integer, Integer> intermedPair = fstPair2;
                                    fstPair2 = scndPair2;
                                    scndPair2 = intermedPair;
                                }
                                operationslist.addOperation(fstPair2);
                                operationslist.addOperation(scndPair2);

                                blockExchangePart2(startBlockA, startBlockB, stoppBlockB, cplusOne);

                                //m and m+1 both have a negative marker
                            } else {
                                if (mMarker == -biggestChild) {
                                    //two Inversions
                                    int positionM = mNode.getPosition();
                                    SplayNode leftOfMNode = splaytree.findIthNode(positionM - 2);
                                    leftOfMNode.splay();
                                    int markerLeftOfM = leftOfMNode.getMarker();

                                    SplayNode chromNode = splaytree.getNode(genomelenght + mplusChromNum + 1);
                                    int chromPos = chromNode.getPosition();
                                    SplayNode leftOfChromNode = splaytree.findIthNode(chromPos - 2);
                                    leftOfChromNode.splay();
                                    int markerLeftOfChrom = leftOfChromNode.getMarker();

                                    int firstOpfirst;
                                    int firstOpsecond;

                                    if (markerLeftOfM < 0) {
                                        firstOpfirst = Math.abs(markerLeftOfM) * 2 - 1;
                                    } else {
                                        firstOpfirst = markerLeftOfM * 2;
                                    }

                                    if (markerLeftOfChrom < 0) {
                                        firstOpsecond = Math.abs(markerLeftOfChrom) * 2 - 1;
                                    } else {
                                        firstOpsecond = markerLeftOfChrom * 2;
                                    }
                                    
                                    Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, firstOpsecond);
                                    Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(Math.abs(mMarker) * 2, Math.abs(mMarker) * 2);
                                    if (fstPair.getFirst() == fstPair.getSecond()) {
                                        Pair<Integer, Integer> intermedPair = fstPair;
                                        fstPair = scndPair;
                                        scndPair = intermedPair;
                                    }
                                    operationslist.addOperation(fstPair);
                                    operationslist.addOperation(scndPair);
                                    int chromPosition = splaytree.getNode(genomelenght + mNodeChromNum + 1).getPosition();
                                    SplayNode chromMinusOne = splaytree.findIthNode(chromPosition - 2);
                                    int chromMarker = Math.abs(chromMinusOne.getMarker());

                                    this.inverse(mMarker, chromMarker);

                                    ArrayList<Integer> genomeList = splaytree.traverseTree();
                                    myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                                    genomeList.remove(0);
                                    int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                                    operationslist.trimAdjArrayList();
                                    operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                    int inversPos = first.getPosition();
                                    SplayNode nodeInfrom = splaytree.findIthNode(inversPos);
                                    if (nodeInfrom.getMarker() == 0) {
                                        nodeInfrom = splaytree.findIthNode(inversPos + 1);
                                    }
                                    nodeInfrom.splay();
                                    int inversefrom = nodeInfrom.getMarker();

                                    int secPos = second.getPosition();
                                    SplayNode rightofSecond = splaytree.findIthNode(secPos);
                                    rightofSecond.splay();
                                    int rightofSecMarker = rightofSecond.getMarker();

                                    int secOpfst;
                                    if (inversefrom < 0) {
                                        secOpfst = Math.abs(inversefrom) * 2;
                                    } else {
                                        secOpfst = inversefrom * 2 - 1;
                                    }
                                    int secOpsnd;
                                    if (rightofSecMarker < 0) {
                                        secOpsnd = Math.abs(rightofSecMarker) * 2 - 1;
                                    } else {
                                        secOpsnd = rightofSecMarker * 2;
                                    }

                                    Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(markertoChange * 2, (markertoChange + 1) * 2 - 1);
                                    Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(secOpfst, secOpsnd);
                                    if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                        Pair<Integer, Integer> intermedPair = fstPair2;
                                        fstPair2 = scndPair2;
                                        scndPair2 = intermedPair;
                                    }
                                    operationslist.addOperation(fstPair2);
                                    operationslist.addOperation(scndPair2);
                                    int posOfI = first.getPosition();
                                    SplayNode rightOfINode = splaytree.findIthNode(posOfI);
                                    if (rightOfINode.getMarker() == 0) {
                                        rightOfINode = splaytree.findIthNode(posOfI + 1);
                                    }
                                    rightOfINode.splay();
                                    this.inverse(rightOfINode.getMarker(), markertoChange + 1);

                                } else {
                                    //normal
                                    int positionM = mNode.getPosition();
                                    int positionIplusOne = second.getPosition();

                                    SplayNode leftOfM = splaytree.findIthNode(positionM - 2);
                                    leftOfM.splay();
                                    int markerleftOfM = leftOfM.getMarker();

                                    int positionI = first.getPosition();
                                    int startBlockA;
                                    int firstOpfirst;
                                    if (myGenomeBList.get(i) == 0) {
                                        startBlockA = splaytree.findIthNode(positionI + 1).getMarker();
                                        firstOpfirst = (markertoChange + 1) * 2 - 1;
                                    } else {
                                        startBlockA = splaytree.findIthNode(positionI).getMarker();
                                        firstOpfirst = markertoChange * 2;
                                    }

                                    int startBlockB = 0;
                                    int stoppBlockB = 0;

                                    //startB und StopB
                                    if (!(positionM == positionI + 1)) {
                                        startBlockB = mMarker;
                                        int posIplusOne = second.getPosition();
                                        stoppBlockB = splaytree.findIthNode(posIplusOne - 2).getMarker();
                                    }

                                    int posC = mplusOneNode.getPosition();
                                    SplayNode stoppCplusOneNode = splaytree.findIthNode(posC);
                                    stoppCplusOneNode.splay();
                                    int markerstoppCplusOne = stoppCplusOneNode.getMarker();

                                    int secondOpfirst;
                                    if (startBlockA < 0) {
                                        secondOpfirst = Math.abs(startBlockA) * 2;
                                    } else {
                                        secondOpfirst = startBlockA * 2 - 1;
                                    }
                                    int secondOpsecond;
                                    if (stoppBlockB == 0) {
                                        secondOpsecond = Math.abs(mMarker) * 2 - 1;
                                    } else if (stoppBlockB < 0) {
                                        secondOpsecond = Math.abs(stoppBlockB) * 2 - 1;
                                    } else {
                                        secondOpsecond = stoppBlockB * 2;
                                    }

                                    Pair<Integer, Integer> fstPair = new Pair<Integer, Integer>(firstOpfirst, (markertoChange + 1) * 2 - 1);
                                    Pair<Integer, Integer> scndPair = new Pair<Integer, Integer>(secondOpfirst, secondOpsecond);
                                    if (fstPair.getFirst() == fstPair.getSecond()) {
                                        Pair<Integer, Integer> intermedPair = fstPair;
                                        fstPair = scndPair;
                                        scndPair = intermedPair;
                                    }
                                    operationslist.addOperation(fstPair);
                                    operationslist.addOperation(scndPair);

                                    Pair<SplayNode, SplayNode> firstex = blockexchangePart1(startBlockA, markertoChange + 1);
                                    //create genome for operationslist, blockexchange takes two DCJ operations.
                                    ArrayList<Integer> lineargenomeList = firstex.getFirst().traverse(firstex.getFirst().isReverseFlag());
                                    lineargenomeList.remove(0);
                                    ArrayList<Integer> circIntermediate = firstex.getSecond().traverse(firstex.getSecond().isReverseFlag());

                                    int[] adjacenciesG1 = this.createAdjacencyGraph(lineargenomeList, circIntermediate);

                                    operationslist.trimAdjArrayList();
                                    operationslist.addAdjacencyArrayG1(adjacenciesG1);

                                    int secOpfst;
                                    int secOpsnd;
                                    if ((markerleftOfM == markertoChange) || (markerleftOfM == 0)) {
                                        if (stoppBlockB == 0) {
                                            secOpfst = Math.abs(mMarker) * 2 - 1;
                                        } else if (stoppBlockB < 0) {
                                            secOpfst = Math.abs(stoppBlockB) * 2 - 1;
                                        } else {
                                            secOpfst = stoppBlockB * 2;
                                        }
                                    } else if (markerleftOfM < 0) {
                                        secOpfst = Math.abs(markerleftOfM) * 2 - 1;
                                    } else {
                                        secOpfst = markerleftOfM * 2;
                                    }

                                    if (markerstoppCplusOne == 0) {
                                        int chromOfC = splaytree.getChromosome(mplusOneNode);
                                        markerstoppCplusOne = chromOfC + genomelenght + 1;
                                        secOpsnd = secOpfst;
                                    } else if (markerstoppCplusOne < 0) {
                                        secOpsnd = Math.abs(markerstoppCplusOne) * 2;
                                    } else {
                                        secOpsnd = markerstoppCplusOne * 2 - 1;
                                    }

                                    Pair<Integer, Integer> fstPair2 = new Pair<Integer, Integer>(Math.abs(mplusOneMarker) * 2 - 1, Math.abs(mMarker) * 2);
                                    Pair<Integer, Integer> scndPair2 = new Pair<Integer, Integer>(secOpfst, secOpsnd);
                                    if (fstPair2.getFirst() == fstPair2.getSecond()) {
                                        Pair<Integer, Integer> intermedPair = fstPair2;
                                        fstPair2 = scndPair2;
                                        scndPair2 = intermedPair;
                                    }
                                    operationslist.addOperation(fstPair2);
                                    operationslist.addOperation(scndPair2);
                                    // second part of the blockexchange, the circulare intermediate is reincorporated into the chromosoms
                                    blockExchangePart2(startBlockA, startBlockB, stoppBlockB, markerstoppCplusOne);

                                }
                            }
                        }
                    }
                }

                ArrayList<Integer> genomeList = splaytree.traverseTree();
                boolean changed = this.changeChromosomOrientation(genomeList);
                if (changed) {
                    genomeList = splaytree.traverseTree();
                }

                myGenomeAList = (ArrayList<Integer>) genomeList.clone();
                genomeList.remove(0);

                int[] adjacenciesG1 = this.createAdjacencyGraph(genomeList, null);
                operationslist.trimAdjArrayList();
                operationslist.addAdjacencyArrayG1(adjacenciesG1);
            }
        }
        
//        int[] adjacenciesG1 = operationslist.getAdjacencyArrayListG1().get(operationslist.getAdjacencyArrayListG1().size()-1);
//        int[] adjacenciesG2 = data.getAdjGraph().getAdjacenciesGenome2();
//        int adj1;
//        for (int i = 1; i < adjacenciesG2.length; ++i) {
//            if (i == adjacenciesG2[i] && i != adjacenciesG1[i]) {
//                adj1 = adjacenciesG1[i];
//                //f.e. B: (1,1), A:(4,1) = (1,1), (4,4)
//                //Replace old adjacencies
//                adjacenciesG1[i] = i;
//                adjacenciesG1[adj1] = adj1;
//                operationslist.addOperation(new Pair<Integer, Integer>(i, i));
//                operationslist.addOperation(new Pair<Integer, Integer>(adj1, adj1));
//                operationslist.addAdjacencyArrayG1(adjacenciesG1.clone());
//            }
//        }

        return operationslist;
    }

    /**
     * Method to perform a fission of a chromosom.
     * 
     * @param node node representing the start of a chromosom.
     */
    private void performFission(SplayNode node) {

        node.splay();
        Pair<SplayNode, SplayNode> fissionSplit = splaytree.split(node, false);

        int chromNum = splaytree.getChromosome(node);
        SplayNode telomere = new SplayNode(chromNum + 1, splaytree);

        splaytree.insertNode(chromNum + 1, telomere);

        splaytree.merge(fissionSplit.getFirst(), telomere, false);
        telomere.splay();
        SplayNode fissionmerge = splaytree.merge(telomere, fissionSplit.getSecond(), false);

        splaytree.setRoot(fissionmerge);
        splaytree.getRoot().setSize();
        splaytree.getRoot().initializeBiggestChild();
    }

    /**
     * Method to perform a fusion between two chromosoms.
     * @param markerA last marker of the left chromosom.
     * @param markerB first marker of the right chromosom.
     */
    private void performFusion(int markerA, int markerB) {

        SplayNode toFuse = splaytree.getNode(markerA);
        int chrom = splaytree.getChromosome(toFuse);
        SplayNode telomere = splaytree.getNode(genomelenght + chrom + 1);
        telomere.splay();

        Pair<SplayNode, SplayNode> firstsplit = splaytree.split(telomere, true);
        splaytree.split(telomere, false);

        SplayNode nodeB = splaytree.getNode(markerB);
        nodeB.splay();
        SplayNode fusioncomplete = null;
        if (nodeB.getLeftChild() == null) {
            fusioncomplete = splaytree.merge(firstsplit.getFirst(), nodeB, true);
        } else {
            Pair<SplayNode, SplayNode> thirdsplit = splaytree.split(nodeB, true);
            SplayNode fusionNode = splaytree.merge(firstsplit.getFirst(), thirdsplit.getSecond(), true);

            SplayNode biggestNode = fusionNode.getPositionBiggestNode(fusionNode.isReverseFlag());
            biggestNode.splay();
            fusioncomplete = splaytree.merge(biggestNode, thirdsplit.getFirst(), false);
        }
        splaytree.deleteNode(chrom);

        splaytree.setRoot(fusioncomplete);
        splaytree.getRoot().setSize();
        splaytree.getRoot().initializeBiggestChild();
    }

    /** Method for the translocation.
     * 
     */
    private void translocate(int markerA, int chromosomA, int markerB, int chromosomB) {
        //splayA == null for example: 1 2 3 | 6 7 4 5 6

        SplayNode splayA = splaytree.getNode(markerA);
        SplayNode chromA = splaytree.getNode(genomelenght + chromosomA);
        SplayNode splayB = splaytree.getNode(markerB);
        SplayNode chromB = splaytree.getNode(genomelenght + chromosomB);

        SplayNode treeT1 = null;
        if (splayA != null) {
            //Here the first tree with nodes smaller then markerA is created
            splayA.splay();
            Pair<SplayNode, SplayNode> firstsplit = splaytree.split(splayA, true);
            treeT1 = firstsplit.getFirst();
        }

        //The second tree with nodes from markerA to smaller chromosomA
        //This is the first part, that should be translocated
        chromA.splay();
        Pair<SplayNode, SplayNode> secondsplit = splaytree.split(chromA, true);
        SplayNode treeT2 = secondsplit.getFirst();

        //Tree 3 with nodes between chromA and smaller than markerB
        splayB.splay();
        Pair<SplayNode, SplayNode> thirdsplit = splaytree.split(splayB, true);
        thirdsplit.getFirst();

        //Here Tree 4 and 5 are created. Tree4 is the second part, that should
        // be translocated. Tree5 with all the nodes equal and more to chromB
        chromB.splay();
        Pair<SplayNode, SplayNode> forthsplit = splaytree.split(chromB, true);

        SplayNode treeT5 = forthsplit.getSecond();

        //Now T4 and T2 are exchanged and the trees are merged.
        //First merge T2 and T5, because there is no splaying involved
        if (splayA != null) {
            splaytree.merge(treeT2, treeT5, true);
        } else {
            chromB.splay();
            splaytree.merge(thirdsplit.getFirst(), chromB, true);
        }

        // T1 and T4 are merged
        splayB.splay();
        SplayNode tree1Merge4;
        if (splayA != null) {
            tree1Merge4 = splaytree.merge(treeT1, splayB, true);
        } else {
            tree1Merge4 = splaytree.merge(treeT2, splayB, true);
        }
        // merge T1merge4 with T3
        chromA.splay();
        SplayNode tree143 = splaytree.merge(tree1Merge4, chromA, true);

        //finally merge T143 with T2merge5
        SplayNode lastmerge;
        if (splayA != null) {
            splayA.splay();
            lastmerge = splaytree.merge(tree143, splayA, true);
        } else {
            lastmerge = tree143;
        }

        splaytree.setRoot(lastmerge);
        splaytree.getRoot().setSize();
        splaytree.getRoot().initializeBiggestChild();
    }

    private void negativTranslocate(int markerA, int chromosomA, int markerB, int chromosomB) {
        //splay A == null for example: 1 2 3 | -5 -4 6 7 8

        SplayNode splayA = splaytree.getNode(markerA);
        SplayNode chromA = splaytree.getNode(genomelenght + chromosomA);
        SplayNode splayB = splaytree.getNode(markerB);
        SplayNode chromB = splaytree.getNode(genomelenght + chromosomB);

        SplayNode treeT1 = null;
        if (splayA != null) {
            splayA.splay();
            Pair<SplayNode, SplayNode> firstsplit = splaytree.split(splayA, true);
            treeT1 = firstsplit.getFirst();
        }

        chromA.splay();
        Pair<SplayNode, SplayNode> secondsplit = splaytree.split(chromA, true);
        SplayNode treeT2 = secondsplit.getFirst();

        chromB.splay();
        Pair<SplayNode, SplayNode> thirdsplit = splaytree.split(chromB, false);
        SplayNode uneffectedTree = thirdsplit.getFirst();

        splayB.splay();
        Pair<SplayNode, SplayNode> forthsplit = splaytree.split(splayB, false);
        SplayNode treeT4 = forthsplit.getFirst();
        SplayNode treeT5 = forthsplit.getSecond();

        //Set reverse
        if (splayA != null) {
            treeT2.splay();
            treeT2.flipFlag();
        }
        treeT4.flipFlag();

        SplayNode firstMerge;
        if (splayA != null) {
            SplayNode biggestT1 = treeT1.getPositionBiggestNode(treeT1.isReverseFlag());
            biggestT1.splay();
            firstMerge = splaytree.merge(biggestT1, treeT4, false);
        } else {
            SplayNode biggestT2 = treeT2.getPositionBiggestNode(treeT2.isReverseFlag());
            biggestT2.splay();
            firstMerge = splaytree.merge(biggestT2, treeT4, false);
        }

        SplayNode smallest = treeT5.getPositionSmallestNode(treeT5.isReverseFlag());
        SplayNode secondMerge;
        if (splayA != null) {
            treeT2.splay();
            secondMerge = splaytree.merge(treeT2, smallest, true);
        } else {
            secondMerge = splaytree.merge(uneffectedTree, smallest, true);
        }

        chromA.splay();
        SplayNode mergeTree = splaytree.merge(firstMerge, chromA, true);

        SplayNode lastmerge;
        if (splayA != null) {
            SplayNode biggest = chromA.getPositionBiggestNode(chromA.isReverseFlag());
            biggest.splay();
            lastmerge = splaytree.merge(biggest, secondMerge, false);
        } else {
            lastmerge = mergeTree;
        }

        splaytree.setRoot(lastmerge);
        splaytree.getRoot().setSize();
        splaytree.getRoot().initializeBiggestChild();

    }

    /** Method for the inversion.
     * With split three trees are created, T<i, Ti-j, T>j.
     * Then the reverseflag of Ti-j is flipped, and the trees are merged.
     */
    private void inverse(int from, int to) {
        if (splaytree.getNode(from).equals(splaytree.getNode(to))) {
            splaytree.getNode(from).flipMarker();
        } else {
            SplayNode fromsplay = splaytree.getNode(from);
            fromsplay.splay();
            Pair<SplayNode, SplayNode> firstsplit = splaytree.split(fromsplay, true);
            SplayNode tosplay = splaytree.getNode(to);
            tosplay.splay();
            Pair<SplayNode, SplayNode> secondsplit = splaytree.split(tosplay, false);
            secondsplit.getFirst().flipFlag();

            SplayNode biggest = firstsplit.getFirst().getPositionBiggestNode(firstsplit.getFirst().isReverseFlag());
            biggest.splay();

            SplayNode node0toj = splaytree.merge(biggest, secondsplit.getFirst(), false);

            SplayNode smallest = secondsplit.getSecond().getPositionSmallestNode(secondsplit.getSecond().isReverseFlag());
            smallest.splay();

            //Hier fehlt noch ein merge
            SplayNode lastmerge = splaytree.merge(node0toj, smallest, true);
            splaytree.setRoot(lastmerge);
            splaytree.getRoot().setSize();
            splaytree.getRoot().initializeBiggestChild();
        }


    }

    /**
     * Cut a Block out of the Genome, return two parts the Genome without the
     * block and the part that is a circulare intermediate.
     * 
     * We have a chromosome with blocks a,b,c in this order.
     * After the first part we get a tree without the blocks a,b and a tree
     * with only a and b. The second tree is the part that creates the
     * circulare intermediate.
     */
    private Pair<SplayNode, SplayNode> blockexchangePart1(int firstA, int firstC) {

        SplayNode firstElemA = splaytree.getNode(firstA);
        SplayNode firstElemC = splaytree.getNode(firstC);

        firstElemA.splay();
        Pair<SplayNode, SplayNode> firstsplit = splaytree.split(firstElemA, true);
        SplayNode TreeT1 = firstsplit.getFirst();


        firstElemC.splay();
        Pair<SplayNode, SplayNode> secondsplit = splaytree.split(firstElemC, true);
        SplayNode treeT2 = secondsplit.getSecond();
        SplayNode intermediate = secondsplit.getFirst();

        //merge T1 and T2, last part of the tree stays alone
        SplayNode treewithout = splaytree.merge(TreeT1, treeT2, true);

        return new Pair<SplayNode, SplayNode>(treewithout, intermediate);
        //treewithout.traverse(treewithout.isReverseFlag());
        //intermediate.traverse(intermediate.isReverseFlag());

    }

    /**
     * In  this part the real block exchange happens.
     * First we had a chromosome with this layout: abc.
     * After this step the layout will be cba. The blocks a and c will have
     * changed their places.
     */
    private void blockExchangePart2(int firstA, int firstB, int lastB, int lastCplusOne) {

        SplayNode aAndB = null;

        if ((firstB != 0) && (lastB != 0) && (firstA != firstB)) {
            SplayNode firstElemA = splaytree.getNode(firstA);
            SplayNode firstElemB = splaytree.getNode(firstB);
            SplayNode lastElemB = splaytree.getNode(lastB);
            //First nodes A and B are exchanged
            firstElemB.splay();
            splaytree.split(firstElemB, true);

            firstElemA.splay();
            lastElemB.splay();
            aAndB = splaytree.merge(lastElemB, firstElemA, true);

        }

        aAndB = splaytree.getNode(firstA);

        //Now the tree without A and B is split and A and B are reincorporated after C
        SplayNode elemAfterC = splaytree.getNode(lastCplusOne);

        elemAfterC.splay();
        Pair<SplayNode, SplayNode> splitCandCplusOne = splaytree.split(elemAfterC, true);
        SplayNode splitC = splitCandCplusOne.getFirst();

        aAndB.splay();
        splaytree.merge(aAndB, splitCandCplusOne.getSecond(), true);

        SplayNode lastmerge = null;
        if ((firstB != 0) && (lastB != 0)) {
            SplayNode firstElemB = splaytree.getNode(firstB);
            firstElemB.splay();
            lastmerge = splaytree.merge(splitC, firstElemB, true);
        } else {
            aAndB.splay();
            lastmerge = splaytree.merge(splitC, aAndB, true);
        }

        splaytree.setRoot(lastmerge);
        splaytree.getRoot().setSize();
        splaytree.getRoot().initializeBiggestChild();

    }

    /**
     * Calculates the adjacencies that are cut by a DCJ operation.
     * The adjacencies are later added to the operationslist.
     * 
     * @param firstMarker
     *          marker right2 of the first adjacency to cut.
     * @param secondMarker
     *          marker right of the second adjacency to cut.
     * @return array with all the adjacenies to cut
     */
    private int[] adjacenciesToCut(int firstMarker, int secondMarker) {

        int[] adjacencies = new int[4];

        SplayNode first = splaytree.getNode(firstMarker);
        int firstPos = first.getPosition();
        int firstMar = first.getMarker();

        SplayNode right = splaytree.findIthNode(firstPos);
        right.splay();
        int rightMarker = right.getMarker();
        if (rightMarker == 0) {
            if (firstMar < 0) {
                adjacencies[0] = Math.abs(firstMar) * 2 - 1;
                adjacencies[1] = Math.abs(firstMar) * 2 - 1;
            } else {
                adjacencies[0] = firstMar * 2;
                adjacencies[1] = firstMar * 2;
            }
        } else {
            if (firstMar < 0) {
                adjacencies[0] = Math.abs(firstMar) * 2 - 1;
                if (rightMarker < 0) {
                    adjacencies[1] = Math.abs(rightMarker) * 2;
                } else {
                    adjacencies[1] = rightMarker * 2 - 1;
                }
            } else {
                adjacencies[0] = firstMar * 2;
                if (rightMarker < 0) {
                    adjacencies[1] = Math.abs(rightMarker) * 2;
                } else {
                    adjacencies[1] = rightMarker * 2 - 1;
                }
            }
        }

        SplayNode second = splaytree.getNode(secondMarker);
        int secondPos = second.getPosition();
        int secMar = second.getMarker();

        SplayNode right2 = splaytree.findIthNode(secondPos - 2);
        right2.splay();
        int right2Marker = right2.getMarker();


        if (right2Marker == 0) {
            if (secMar < 0) {
                adjacencies[2] = Math.abs(secMar) * 2;
                adjacencies[3] = Math.abs(secMar) * 2;

            } else {
                adjacencies[2] = 0;
                adjacencies[3] = 0;
            }
        } else {
            if (secMar < 0) {
                adjacencies[2] = Math.abs(secMar) * 2 - 1;
                if (right2Marker < 0) {
                    adjacencies[3] = Math.abs(right2Marker) * 2;
                } else {
                    adjacencies[3] = right2Marker * 2 - 1;
                }
            } else {
                adjacencies[2] = secMar * 2;
                if (right2Marker < 0) {
                    adjacencies[3] = Math.abs(right2Marker) * 2;
                } else {
                    adjacencies[3] = right2Marker * 2 - 1;
                }
            }
        }

        return adjacencies;
    }

    /**
     * Method to change the orientation of a chromosom if the last gene
     * has a negativ orientation and has a smaller marker than the first
     * gene on the chromosome.
     * @param genome
     *      The genome on which the change operation is performed.
     * @return true if a orientation was changed, false otherwise.
     */
    private boolean changeChromosomOrientation(ArrayList<Integer> genome) {

        boolean back = false;
        int start = genome.get(1);
        int ende = 0;
        for (int i = 1; i < genome.size(); i++) {

            if (genome.get(i) == 0) {
                ende = genome.get(i - 1);
                if ((ende < 0) && (Math.abs(ende) < Math.abs(start)) || ((ende < 0) && (Math.abs(ende) == Math.abs(start)))) {
                    this.inverse(Math.abs(start), Math.abs(ende));
                    back = true;
                }
                if (i != genome.size() - 1) {
                    start = genome.get(i + 1);
                }
            }
        }
        return back;
    }

    private int[] createAdjacencyGraph(ArrayList<Integer> linear, ArrayList<Integer> circular) {

        ArrayList<Integer> lineargenomeList = linear;


        Genome genomforAdjList = new Genome();
        ArrayList<Integer> chromosom = new ArrayList<Integer>();
        for (int j = 0; j < lineargenomeList.size(); j++) {
            if (lineargenomeList.get(j) != 0) {
                chromosom.add(lineargenomeList.get(j));
            } else {
                Chromosome chromtoAdd = new Chromosome(chromosom, false);
                genomforAdjList.addChromosome(chromtoAdd);
                chromosom = new ArrayList<Integer>();
            }
        }

        if (circular != null) {
            ArrayList<Integer> circIntermediate = circular;
            Chromosome circ = new Chromosome(circIntermediate, true);
            genomforAdjList.addChromosome(circ);
        }

        int[] adjacenciesG1 = parseAdjacencies(genomforAdjList, lenghtforAdjacency);
        return adjacenciesG1;
    }

    /* This Method was taken from AdjacencyGraph.java. It is needed to transform a
     * SplayTree to a AdjacencyGraph.
     */
    /**
     * Parses the adjacency graph. The result contains at each index (which represents
     * a head or tail of a gene) its neighbor and if there it is a telomere it contains
     * itself.
     * @param genome Genome to parse
     * @param genomeLength Length of the genome as int value
     */
    private int[] parseAdjacencies(final Genome genome, int genomeLength) {

        // genome: -1 3 4|  2 5| -6,-7| array from 1-14 for 7 genes!
        // head = odd, tail = even, entry 0 is empty!
        final int[] genomeAdjacencies = new int[genomeLength];

        for (int i = 0; i < genome.getNumberOfChromosomes(); ++i) {
            int[] chromosome = genome.getChromosome(i).getGenes();
            int length = chromosome.length;
            int geneNumberLeft;
            int geneNumberRight;

            //Part for processing telomeres, thus we choose "false" as input for "abstractGene"
            geneNumberLeft = this.abstractGene(chromosome[0], false);
            geneNumberRight = this.abstractGene(chromosome[length - 1], true); //Right telomere is right in adjacency: true

            if (genome.getChromosome(i).isCircular()) {
                genomeAdjacencies[geneNumberLeft] = geneNumberRight; // save
                genomeAdjacencies[geneNumberRight] = geneNumberLeft;
                // can already save telomere entries
            } else {
                genomeAdjacencies[geneNumberLeft] = geneNumberLeft;
                genomeAdjacencies[geneNumberRight] = geneNumberRight;
            }

            for (int j = 1; j < chromosome.length; ++j) {

                geneNumberLeft = this.abstractGene(chromosome[j - 1], true);
                geneNumberRight = this.abstractGene(chromosome[j], false);

                genomeAdjacencies[geneNumberLeft] = geneNumberRight; // save
                genomeAdjacencies[geneNumberRight] = geneNumberLeft;
            }
        }
        return genomeAdjacencies;
    }

    /**
     * Abstracts a gene to a positive value of 2*gene or 2*gene-1. If the gene is a right telomere
     * then its position is right2 in the adjacency and right for a right2 telomere. For all inner 
     * adjacencies the right gene is on the right side of the adjacency and therefore a head if
     * the gene is in reverse orientation and a tail otherwise. The same holds for the right2 gene
     * of an adjacency just in the other way round.
     * @param gene the gene to be abstracted
     * @param right if the gene is the right or right2 one in adjacency
     * @return the abstracted gene
     */
    public int abstractGene(final int gene, final boolean left) {
        /* Entry is either right2, thus a tail if > 0 or right, thus a tail if < 0 */
        if (gene <= 0 && !left || gene >= 0 && left) {
            return 2 * Math.abs(gene);
        } else { /* Entry is either right2, thus a tail if < 0 or right, thus a tail if > 0 */
            return 2 * Math.abs(gene) - 1;
        }
    }

    private void findFstPair() {
        
    }
}
