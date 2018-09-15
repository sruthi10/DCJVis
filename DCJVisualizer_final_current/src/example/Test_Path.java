/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import static ca.corefacility.gview.map.gui.GUIUtility.URL;
import java.io.File;

/**
 *
 * @author chapp
 */
public class Test_Path {
    
    public static void main(String[] args) 
    {
        System.out.println("Test_Path");
        
        System.out.println(Test_Path.class.getResource("C.albicans_Chr_7.fa"));
        File f = new File("test.txt");
        System.out.println(f.getPath());
        System.out.println(f.getAbsolutePath());
        
    }
    
}
