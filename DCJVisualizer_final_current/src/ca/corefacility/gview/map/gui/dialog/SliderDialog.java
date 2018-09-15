package ca.corefacility.gview.map.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.corefacility.gview.map.controllers.GUIController;

/**
 * Abstract custom slider dialog.
 * 
 * @author Eric Marinier
 *
 */
public abstract class SliderDialog extends JDialog implements ChangeListener, ActionListener
{
	private static final long serialVersionUID = 1L; //requested by java
	
	protected final int MIN;	//the min value of the slider
	protected final int MAX;	//the max value of the slider
	protected final int INVALID;	//an invalid value on the slider
	
	protected final GUIController guiController;
	
	protected final JPanel CONTENT_PANEL;	//the panel the dialog box 	
	protected final JTextField TEXT_FIELD;
	protected final JSlider SLIDER;
	
	protected final String DIALOG_MESSAGE;
	
	protected final JButton OK_BUTTON;
	protected final JButton CANCEL_BUTTON;
	
	protected final String OK = "OK";
	protected final String CANCEL = "Cancel";
	
	protected String textFieldMessage;

	SliderDialog(GUIController guiController, int min, int max, int invalid, String message)
	{
		super(guiController.getGViewGUIFrame());
		
		this.guiController = guiController;
		
		MIN = min;
		MAX = max;
		INVALID = invalid;
		
		CONTENT_PANEL = new JPanel();
		TEXT_FIELD = new JTextField("");
		SLIDER = new JSlider(MIN, MAX, MIN);
		
		DIALOG_MESSAGE = message;
		textFieldMessage = MIN + "";
		
		OK_BUTTON = new JButton(OK);
		CANCEL_BUTTON = new JButton(CANCEL);
		
		createDialog();
		
		//Hit escape to close the dialog:
		final JDialog dialog = this;
		
		Action dispatchClosing = new AbstractAction() 
		{ 
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent event) 
	        { 
	        	dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
	        	TEXT_FIELD.requestFocus();
	        } 
	    };
		
	    final String DIALOG_CLOSING = "DIALOG CLOSING EVENT";
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, DIALOG_CLOSING);
		this.getRootPane().getActionMap().put(DIALOG_CLOSING, dispatchClosing);
	}	
	
	/**
	 * Creates the dialog. Much of the code was generated using WindowBuilderPro.
	 */
	private void createDialog()
	{	
		setVisible(false);		
		setResizable(false);
		setBounds(100, 100, 394, 165);
		getContentPane().setLayout(new BorderLayout());
		CONTENT_PANEL.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(CONTENT_PANEL, BorderLayout.CENTER);
		CONTENT_PANEL.setLayout(new BoxLayout(CONTENT_PANEL, BoxLayout.X_AXIS));
		{
			JPanel panel = new JPanel();
			CONTENT_PANEL.add(panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.PAGE_START);
				panel_1.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JLabel lblEnterAScale = new JLabel(DIALOG_MESSAGE);
					panel_1.add(lblEnterAScale);
				}
				{
					TEXT_FIELD.setText(textFieldMessage);
					panel_1.add(TEXT_FIELD);
					TEXT_FIELD.setColumns(10);
				}
			}
			{
				Box horizontalBox = Box.createHorizontalBox();
				panel.add(horizontalBox, BorderLayout.PAGE_END);
				{
					Component verticalStrut = Box.createVerticalStrut(20);
					verticalStrut.setMinimumSize(new Dimension(0, 30));
					horizontalBox.add(verticalStrut);
				}
				{
					JPanel panel_1 = new JPanel();
					horizontalBox.add(panel_1);
					panel_1.setLayout(new BorderLayout(0, 0));
					{
						SLIDER.addChangeListener(this);
						panel_1.add(SLIDER, BorderLayout.SOUTH);
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				OK_BUTTON.setActionCommand(OK);
				buttonPane.add(OK_BUTTON);
				getRootPane().setDefaultButton(OK_BUTTON);
				OK_BUTTON.addActionListener(this);
			}
			{
				CANCEL_BUTTON.setActionCommand(CANCEL);
				buttonPane.add(CANCEL_BUTTON);
				CANCEL_BUTTON.addActionListener(this);
			}
		}
	}
	
	/**
	 * Displays the dialog.
	 */
	public void showDialog()
	{
		TEXT_FIELD.selectAll();
		this.setVisible(true);
	}
	
	/**
	 * Listens for the buttons being pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{		
		System.out.println(e);
		System.out.println(e.getActionCommand());
		
		//OK button pressed.
		if(OK.equals(e.getActionCommand()))
		{	
			setVisible(false);
		}
		//Cancel button pressed.
		else if(CANCEL.equals(e.getActionCommand()))
		{
			setVisible(false);
		}
	}
	
	@Override
	/**
	 * Listens for the slider; updates the text field.
	 */
	public void stateChanged(ChangeEvent e) 
	{
		TEXT_FIELD.setText(SLIDER.getValue() + "");		
	}
	
	/**
	 * 
	 * @return The input without any leading or trailing whitespace and without any commas in the String.
	 */
	public String getCleanInput()
	{
		String input = TEXT_FIELD.getText();
		input = input.trim();
		input = input.replace(",", "");
		
		return input;
	}
}
