package de.unibi.cebitec.gi.unimog.framework;

import de.unibi.cebitec.gi.unimog.utils.Constants;

/**
 * Provides all types of Scenarios that can be selected as well as a String to be shown instead in the GUI.
 * 
 * @author eyla
 */
public enum Scenario {

    /** DCJ distance model. */
    DCJ(Constants.DCJ_ST),
    
    /** Restricted DCJ (minimal use of circular intermediates. */
    DCJ_RESTRICTED(Constants.RDCJ_ST),
    
    /** HP distance model. */
    HP(Constants.HP_ST),

    /** Inversion distance model. */
    INVERSION(Constants.INV_ST),
    
    /** Translocation distance model. */
    TRANSLOCATION(Constants.TRANS_ST),
    
    /** All currently available models should be chosen. */
    ALL("All"),
    
    //	/** DCJ with insertions and deletions. */
    //	DCJ_INDEL(Constants.DCJ_INDEL_ST, Constants.DCJ_INDEL),

    /** No model selected */
    NONE("Unknown");
    
    private final String guiName;

    /**
     * Creates new type of Scenario including a gui string.
     * 
     * @param guiName String to be shown in GUI.
     */
    private Scenario(final String guiName) {
        this.guiName = guiName;
    }

    /**
     * Returns Name of Annotation Type for GUI.
     * 
     * @return Annotation String for GUI
     */
    public String getGuiName() {
        return this.guiName;
    }

    /**
     * Returns the selected {@link Scenario} depending on the id given by the user.
     * 
     * @param id from console input
     * @return {@link Scenario} according to currently selected id
     */
    public static Scenario getScenario(final int id) {
        switch (id) {
            case 1:
                return Scenario.DCJ;
            case 2:
                return Scenario.DCJ_RESTRICTED;
            case 3:
                return Scenario.HP;
            case 4:
                return Scenario.INVERSION;
            case 5:
                return Scenario.TRANSLOCATION;
            case 6:
                return Scenario.ALL;
//		case 7:
//			return Scenario.DCJ_INDEL;
            default:
                return Scenario.NONE;
        }
    }
}