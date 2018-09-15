package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.gui.editor.ScoreToOpacityMapperListItem;
import ca.corefacility.gview.map.gui.editor.ScoreToOpacityPanel;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility.ConversionException;

/**
 * A subclass of JDialog, used for returning a new PropertyMapperListItem.
 * 
 * Intended to be called from PropertyMapperPanel.
 * 
 * @author Eric Marinier
 * 
 */
public class PropertyMapperChooserDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L; // requested by java

	private static final String TITLE = "Choose a Mapper:";
	private static final String OK = "OK";
	private static final String CANCEL = "Canel";

	private static final String PROPERTY_MAPPER_COMBO_BOX = "Property Mapper Combo Box";

	private final JButton okButton;
	private final JButton cancelButton;

	private final JPanel contentPanel;
	private final JPanel specificContent;

	// allows the user to choose their property mapper
	private final JComboBox propertyMapperComboBox;

	private final ScoreToOpacityPanel scoreToOpacityPanel = new ScoreToOpacityPanel();
	private final COGToColorPanel cogToColorPanel = new COGToColorPanel();

	// the associated property mapper panel; expected that this panel called /
	// created this dialog.
	private final PropertyMapperPanel propertyMapperPanel;

	private PropertyMapperListItem listItem = null; // the item to modify on
													// 'OK' click; if null, make
													// a new list item

	/**
	 * Creates the dialog.
	 * 
	 * @param propertyMapperPanel
	 *            The property mapper panel that is using the dialog.
	 */
	public PropertyMapperChooserDialog(PropertyMapperPanel propertyMapperPanel)
	{
		if (propertyMapperPanel == null)
			throw new IllegalArgumentException("PropertyMapperPanel is null.");

		this.propertyMapperPanel = propertyMapperPanel;

		this.setTitle(TITLE);

		// initialize
		this.okButton = createOKButton();
		this.cancelButton = createCancelButton();
		this.propertyMapperComboBox = createPropertyMapperComboBox();

		this.specificContent = createSpecificContentPanel();
		this.contentPanel = createContentPanel();

		layoutComponents();

		this.setLocation(this.propertyMapperPanel.getX(), this.propertyMapperPanel.getY());
		this.setModal(true);

		updateSpecificContent();
	}

	/**
	 * Layout the components of the dialog.
	 */
	private void layoutComponents()
	{
		JPanel comboBoxPanel = createComboBoxPanel();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.contentPanel, BorderLayout.CENTER);

		this.contentPanel.add(comboBoxPanel, BorderLayout.NORTH);
		this.contentPanel.add(this.specificContent, BorderLayout.SOUTH);

		this.getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
	}

	/**
	 * 
	 * @return A new JPanel for the specific content panel variable.
	 */
	private JPanel createSpecificContentPanel()
	{
		JPanel specificContentPanel = new JPanel();

		specificContentPanel.setLayout(new BoxLayout(specificContentPanel, BoxLayout.Y_AXIS));

		return specificContentPanel;
	}

	/**
	 * 
	 * @return A new JPanel for the combo box panel variable.
	 */
	private JPanel createComboBoxPanel()
	{
		JPanel comboBoxPanel = new JPanel();

		comboBoxPanel.setBorder(new TitledBorder(null, "Mapper Type", TitledBorder.LEADING, TitledBorder.ABOVE_TOP,
				null, null));
		FlowLayout flowLayout = (FlowLayout) comboBoxPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		comboBoxPanel.add(this.propertyMapperComboBox);

		return comboBoxPanel;
	}

	/**
	 * 
	 * @return A new JComboBox for the property mapper combo box variable.
	 */
	private JComboBox createPropertyMapperComboBox()
	{
		JComboBox propertyMapperComboBox = new JComboBox(StyleEditorUtility.PROPERTY_MAPPERS);

		propertyMapperComboBox.setActionCommand(PROPERTY_MAPPER_COMBO_BOX);
		propertyMapperComboBox.addActionListener(this);

		return propertyMapperComboBox;
	}

	/**
	 * 
	 * @return A new JPanel for the content panel variable.
	 */
	private JPanel createContentPanel()
	{
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(new BorderLayout(0, 0));

		return contentPanel;
	}

	/**
	 * 
	 * @return A new JButton - ok button.
	 */
	private JButton createOKButton()
	{
		JButton okButton = new JButton(OK);
		okButton.setActionCommand(OK);
		okButton.addActionListener(this);

		return okButton;
	}

	/**
	 * 
	 * @return A new JButton - cancel button.
	 */
	private JButton createCancelButton()
	{
		JButton cancelButton = new JButton(CANCEL);
		cancelButton.setActionCommand(CANCEL);
		cancelButton.addActionListener(this);

		return cancelButton;
	}

	/**
	 * 
	 * @return A new JPanel - the button panel for the buttons.
	 */
	private JPanel createButtonPanel()
	{
		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(this.okButton);
		buttonPanel.add(this.cancelButton);

		getRootPane().setDefaultButton(okButton);

		return buttonPanel;
	}

	/**
	 * Updates the content specific portion of the panel with GUI components
	 * that are appropriate to the current combo box selection.
	 */
	private void updateSpecificContent()
	{
		this.specificContent.removeAll();

		if (this.propertyMapperComboBox.getSelectedItem().equals(StyleEditorUtility.COG_TO_COLOR_MAPPER))
		{
			addCOGToColorComponents();
		}
		else if (this.propertyMapperComboBox.getSelectedItem().equals(StyleEditorUtility.SCORE_TO_OPACITY))
		{
			addScoreToOpacityComponents();
		}

		this.pack();
	}

	/**
	 * Adds the appropriate components for a COG to Color mapper.
	 */
	private void addCOGToColorComponents()
	{
		this.specificContent.add(this.cogToColorPanel);
	}

	/**
	 * Adds the appropriate components for a score to opacity mapper.
	 */
	private void addScoreToOpacityComponents()
	{
		this.specificContent.add(this.scoreToOpacityPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// OK button clicked
		if (e.getActionCommand().equals(OK))
		{
			try
			{
				// making (a) new property mapper(s)
				if (this.listItem == null)
				{
					// auto populate cogs
					if (this.propertyMapperComboBox.getSelectedItem().equals(
							StyleEditorUtility.AUTO_POPULATE_COG_CATEGORIES))
					{
						this.propertyMapperPanel.autoPopulateCogsToColors(); // forward
																				// the
																				// responsibility
					}
					// others
					else
					{
						this.propertyMapperPanel.addPropertyMapper(createPropertyMapper());
					}
				}
				// modifying
				else
				{
					modifyPropertyMapper();
				}

				this.setVisible(false);
				this.propertyMapperPanel.repaint();
			}
			catch (ConversionException e1)
			{
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		// Cancel button clicked
		else if (e.getActionCommand().equals(CANCEL))
		{
			this.setVisible(false);
		}
		// Property mapper combo box changed
		if (e.getActionCommand().equals(PROPERTY_MAPPER_COMBO_BOX))
		{
			updateSpecificContent();
		}
	}

	/**
	 * Modifies the property mapper by creating a new property mapper, given the
	 * GUI components and tells the panel to replace the passed item with the
	 * new one.
	 * 
	 * @throws ConversionException
	 */
	private void modifyPropertyMapper() throws ConversionException
	{
		this.propertyMapperPanel.modifyListItem(createPropertyMapper(), this.listItem);
	}

	/**
	 * Creates a property mapper list item based on the content of the GUI
	 * components.
	 * 
	 * @return A newly created property mapper list item. Will return null if
	 *         the creation was unsuccessful.
	 * @throws ConversionException
	 */
	private PropertyMapperListItem createPropertyMapper() throws ConversionException
	{
		PropertyMapperListItem item = null;

		// COG to color
		if (this.propertyMapperComboBox.getSelectedItem().equals(StyleEditorUtility.COG_TO_COLOR_MAPPER))
		{
			item = new COGToColorMapperListItem(this.cogToColorPanel.getCOGCategory(), this.cogToColorPanel.getPaint());
		}
		// Score to opacity
		else if (this.propertyMapperComboBox.getSelectedItem().equals(StyleEditorUtility.SCORE_TO_OPACITY))
		{
			try
			{
				item = new ScoreToOpacityMapperListItem(Float.parseFloat(this.scoreToOpacityPanel.getMin()),
						Float.parseFloat(this.scoreToOpacityPanel.getMax()));
			}
			catch (NumberFormatException nfe)
			{
				throw new StyleEditorUtility.ConversionException("Invalid number.");
			}
		}

		return item;
	}

	/**
	 * Displays the add dialog.
	 */
	public void displayAddDialog()
	{
		this.propertyMapperComboBox.setEnabled(true);

		this.setLocationRelativeTo(this.propertyMapperPanel);
		this.setVisible(true);
	}

	/**
	 * Displays the modify dialog to for modifying the passed list item. The
	 * list item may be null, but it really shouldn't, because it wont do
	 * anything.
	 * 
	 * Will load up the panel appropriately with the list item's current
	 * properties.
	 * 
	 * @param listItem
	 *            The item to modify.
	 */
	public void displayModifyDialog(PropertyMapperListItem listItem)
	{
		this.listItem = listItem; // can handle being null

		if (listItem instanceof ScoreToOpacityMapperListItem)
		{
			this.propertyMapperComboBox.setSelectedItem(StyleEditorUtility.SCORE_TO_OPACITY);

			initializePanel((ScoreToOpacityMapperListItem) listItem);
			displayModifyDialog();

		}
		else if (listItem instanceof COGToColorMapperListItem)
		{
			this.propertyMapperComboBox.setSelectedItem(StyleEditorUtility.COG_TO_COLOR_MAPPER);

			initializePanel((COGToColorMapperListItem) listItem);
			displayModifyDialog();
		}
		else
		{
			// unrecognized or can't modify
		}
	}

	/**
	 * Initializes the panel for the COG to color mapper list item.
	 * 
	 * @param item
	 *            The list item to initialize for.
	 */
	private void initializePanel(COGToColorMapperListItem item)
	{
		this.cogToColorPanel.setCOGCategory(item.getCOGCategory());
		this.cogToColorPanel.setPaint(item.getPaint());
	}

	/**
	 * Initializes the panel for score to opacity mapper list item.
	 * 
	 * @param item
	 *            The list item to initialize for.
	 */
	private void initializePanel(ScoreToOpacityMapperListItem item)
	{
		this.scoreToOpacityPanel.setMin(item.getMin());
		this.scoreToOpacityPanel.setMax(item.getMax());
	}

	/**
	 * Displays the dialog given that it will be for modifying.
	 */
	private void displayModifyDialog()
	{
		this.propertyMapperComboBox.setEnabled(false); // otherwise just remove
														// it and add a new one
		this.setLocationRelativeTo(this.propertyMapperPanel);
		this.setVisible(true);
	}
}
