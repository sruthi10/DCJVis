package ca.corefacility.gview.map.gui.action;

import javax.swing.undo.UndoableEdit;

/**
 * The abstract action class. 
 * 
 * @author Eric Marinier
 *
 */
public abstract class Action implements UndoableEdit, Runnable
{
	public abstract void run();
	
	public class ActionRunException extends Exception
	{
        private static final long serialVersionUID = 1L;

        public ActionRunException(String message)
        {
            super(message);
            // TODO Auto-generated constructor stub
        }

        public ActionRunException(Throwable cause)
        {
            super(cause);
            // TODO Auto-generated constructor stub
        }
	}
	
	@Override
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void die() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean addEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean replaceEdit(UndoableEdit anEdit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSignificant() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUndoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRedoPresentationName() {
		// TODO Auto-generated method stub
		return null;
	}	
}
