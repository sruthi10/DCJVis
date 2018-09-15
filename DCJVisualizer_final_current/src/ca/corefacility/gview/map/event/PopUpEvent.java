package ca.corefacility.gview.map.event;

import java.awt.Component;

public class PopUpEvent extends GViewEvent
{
    private static final long serialVersionUID = 1L;
    
    private int x,y;
    
    public PopUpEvent(Component source, int x, int y)
    {
        super(source);
        this.x = x;
        this.y = y;
    }
    
    public Component getComponent()
    {
        return (Component)source;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
}
