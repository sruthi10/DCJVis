package jfasta.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 4/21/12
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class FastaAccessionPattern {

    /**
     * pattern to match ids, could be more than one pattern in the same file
     */

    private List<ProteinAccessionPattern> patternIDMap  = null;

    private FastaAccessionPattern(){
        patternIDMap = new ArrayList<ProteinAccessionPattern>();
    }

    /**
	 * @return new instance of this class.
	 */
	public static FastaAccessionPattern newInstance() {
		return new FastaAccessionPattern();
	}

    public void addAccessionFormat(ProteinAccessionPattern proteinHeader){
        patternIDMap.add(proteinHeader);
    }

    public  Pattern getFastaAccessionPattern(String name){
        for (ProteinAccessionPattern proteinAccessionPattern: patternIDMap){
            if(proteinAccessionPattern.getName().compareToIgnoreCase(name) == 0){
                return proteinAccessionPattern.getIdPatternFasta();
            }
        }
        return null;
    }

    public List<ProteinAccessionPattern> getPatternIDMap(){
        return patternIDMap;
    }



}
