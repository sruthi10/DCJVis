package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import ca.corefacility.gview.map.controllers.SlotStyleController;
import ca.corefacility.gview.map.controllers.SlotStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility.ConversionException;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.slotEvent.SlotThicknessEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxySlotPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.utils.Util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for slot styles.
 * 
 * @author Eric Marinier
 * 
 */
public class SlotPanel extends StylePanel implements DocumentListener, ProxiablePanel
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String SLOT_STYLE_TEXT = "Slot Style";

	private static final String THICKNESS_LABEL_TEXT = "Thickness:";

	private static final String INVALID_THICKNESS = "Invalid thickness.";
	private static final String SLOT_STYLE_CONTROLLER_NULL = "SlotStyleController is null.";
	private static final String SLOT_STYLE_TOKEN_NULL = "SlotStyleToken is null.";

	private static final String THICKNESS_HINT = "The thickness of the slot.";

	private final Document thicknessDocument;
	private boolean isDocumentUpdating = false;

	private final JTextField thickness;

	private final SlotStyleController slotController;
	private final SelectionController selectionController;
	private final SlotStyleToken slotStyle;

	/**
	 * Create the panel.
	 */
	public SlotPanel(SlotStyleController controller, SlotStyleToken slotStyle, SelectionController selectionController)
	{
		super();

		if (controller == null)
		{
			throw new IllegalArgumentException(SLOT_STYLE_CONTROLLER_NULL);
		}
		if (slotStyle == null)
		{
			throw new IllegalArgumentException(SLOT_STYLE_TOKEN_NULL);
		}

		this.slotController = controller;
		this.selectionController = selectionController;
		this.slotStyle = slotStyle;

		this.addGUIEventBroadcaster(new SelectionEventBroadcaster(selectionController));

		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), SLOT_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// THICKNESS
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblThickness = new JLabel(THICKNESS_LABEL_TEXT);
		panel_4.add(lblThickness, BorderLayout.EAST);

		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new GridLayout());

		// thickness text field
		this.thickness = new JTextField();
		panel.add(this.thickness);
		this.thickness.setColumns(10);
		this.thickness.getDocument().addDocumentListener(this);
		this.thicknessDocument = this.thickness.getDocument();

		// hint
		inner.add(new HintLabel(THICKNESS_HINT), "10, 2");

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.thickness.setText(null);

		this.setUpdating(false);
	}

	@Override
	public SlotPanel createProxy()
	{
		ProxySlotPanel panel = new ProxySlotPanel(this.slotController, this.slotStyle, this.selectionController);

		((SlotPanel) panel).proxy();

		return panel;
	}

	/**
	 * 
	 * @return The text of the thickness field.
	 */
	private String getThicknessText()
	{
		return this.thickness.getText();
	}

	private double getThickness() throws ConversionException
	{
		double thickness = Double.parseDouble(getThicknessText());

		if (thickness < 0)
		{
			throw new StyleEditorUtility.ConversionException("Slot Thickness value must be non-negative.");
		}

		return thickness;
	}

	/**
	 * Sets the text of the thickness field.
	 * 
	 * @param d
	 */
	private void setThicknessText(double d)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.thickness.setText(Double.toString(d));
	}

	/**
	 * The response to the thickness being changed.
	 */
	private void thicknessAction()
	{
		// Is the content a double?
		if (Util.isDouble(this.getThicknessText()))
		{
			try
			{
				this.broadcastEvent(new SlotThicknessEvent(getThickness()));
			}
			catch (ConversionException e)
			{
			}
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		updateThickness();
	}

	/**
	 * Updates the thickness.
	 */
	private void updateThickness()
	{
		setThicknessText(this.slotController.getThickness(this.slotStyle));
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyThickness();
	}

	/**
	 * Applies the thickness.
	 */
	private void applyThickness()
	{
		try
		{
			this.slotController.setThickness(this.slotStyle, this.getThickness());
		}
		catch (NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(this, INVALID_THICKNESS);

			this.updateThickness();
		}
		catch (StyleEditorUtility.ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());

			this.updateThickness();
		}
	}

	/**
	 * 
	 * @return The slot style as a token.
	 */
	public SlotStyleToken getSlotStyle()
	{
		return this.slotStyle;
	}

	/**
	 * 
	 * @return The slot style controller.
	 */
	public SlotStyleController getSlotStyleController()
	{
		return this.slotController;
	}

	@Override
	public void insertUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e)
	{
		textChanged(e);
	}

	/**
	 * A single method for responding to a JComboBox being changed.
	 * 
	 * @param e
	 */
	private void textChanged(DocumentEvent e)
	{
		if (e == null || e.getDocument() == null || this.isUpdating())
		{
			return;
		}

		this.isDocumentUpdating = true;

		if (e.getDocument().equals(this.thicknessDocument))
		{
			thicknessAction();
		}

		this.isDocumentUpdating = false;
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof SlotThicknessEvent)
		{
			this.setThicknessText(((SlotThicknessEvent) event).getData());
		}
	}
}
