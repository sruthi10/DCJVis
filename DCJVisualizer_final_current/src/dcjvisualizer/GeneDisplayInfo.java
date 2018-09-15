/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcjvisualizer;

import java.awt.Color;

/**
 *Basic structure to represent every gene information
 * 
 */
public class GeneDisplayInfo 
{
    String name;
    String id;
    int Location1;
    int Location2;
    public int origLoc1;
    public int origLoc2;
    boolean Complement;
    int index ;
    Color col;
    
    public GeneDisplayInfo(String Name,String id,int Location1,int Location2,boolean c,int ind)
    {
        this.name = Name;
        this.id = id;
       origLoc1 =  this.Location1 = Location1;
        origLoc2 = this.Location2 = Location2;
        this.Complement = c;
        index = ind;
    }
    
    public String getName()
    {
        return this.name;
    }
     public String getId()
    {
        return this.id;
    }
      public int getLocation1()
    {
        return this.Location1;
    }
       public int getLocation2()
    {
        return this.Location2;
    }
     
    
}
