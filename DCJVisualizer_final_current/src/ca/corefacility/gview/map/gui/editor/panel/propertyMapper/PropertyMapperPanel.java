package ca.corefacility.gview.map.gui.editor.panel.propertyMapper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ca.corefacility.gview.map.controllers.PropertyMappableToken;
import ca.corefacility.gview.map.controllers.PropertyMapperController;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.ScoreToOpacityMapperListItem;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.UnknownMapperListItem;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.propertyMapperEvent.PropertyMapperStyleMapperEvent;
import ca.corefacility.gview.map.gui.editor.panel.StylePanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.style.datastyle.mapper.COGMapper;
import ca.corefacility.gview.style.datastyle.mapper.ContinuousPropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.ContinuousStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscretePaintMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscretePropertyMapper;
import ca.corefacility.gview.style.datastyle.mapper.DiscreteStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.OpacityFeatureStyleMapper;
import ca.corefacility.gview.style.datastyle.mapper.PropertyMapperScore;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleContinuous;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleDiscrete;
import ca.corefacility.gview.style.datastyle.mapper.PropertyStyleMapper;

/**
 * The property style mapper panel.
 * 
 * Intended to be used within property mapper node.
 * 
 * @author Eric Marinier
 * 
 */
public class PropertyMapperPanel extends StylePanel implements ActionListener
{
	private static final long serialVersionUID = 1L; // request by java

	private static final String MODIFY = "Modify";
	private static final String MODIFY_TEXT = MODIFY;
	private static final String REMOVE = "Remove";
	private static final String REMOVE_TEXT = REMOVE;
	private static final String ADD = "Add";
	private static final String ADD_TEXT = ADD;

	private static final String PROPERTY_MAPPER_HINT = "Score to Opacity Mapper: maps the score of each individual feature to a specified range of opacities. COG Category to Color Mapper: maps a specific COG category to a color.";

	private final PropertyMapperController controller;
	private final PropertyMappableToken propertyMappable;

	private final PropertyMapperList propertyMapperList; // the list of property
															// mappers

	private final JButton modifyButton; // the modify button
	private final JButton removeButton; // the remove button
	private final JButton addButton; // the add button

	public PropertyMapperPanel(PropertyMapperController controller, PropertyMappableToken propertyMappable,
			SelectionController selectionController)
	{
		if (propertyMappable == null)
			throw new IllegalArgumentException("PropertyMappableToken is null.");

		this.controller = controller;
		this.propertyMappable = propertyMappable;

		this.addGUIEventBroadcaster(new SelectionEventBroadcaster(selectionController));

		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Property Mapper",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);
		inner.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		inner.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		// list
		this.propertyMapperList = new PropertyMapperList();
		JScrollPane listScroller = new JScrollPane(this.propertyMapperList);
		panel_1.add(listScroller, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout());
		panel.add(panel_2, BorderLayout.SOUTH);

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.CENTER);

		// Add button
		this.addButton = createAddButton();
		panel_3.add(this.addButton);

		// modify button
		this.modifyButton = createModifyButton();
		panel_3.add(this.modifyButton);

		// remove button
		this.removeButton = createRemoveButton();
		panel_3.add(this.removeButton);

		// hint
		JPanel panel_4 = new JPanel();
		panel_2.add(panel_4, BorderLayout.EAST);

		panel_4.add(new HintLabel(PROPERTY_MAPPER_HINT));
	}

	/**
	 * 
	 * @return The add button.
	 */
	private JButton createAddButton()
	{
		JButton addButton = new JButton(ADD_TEXT);
		addButton.setActionCommand(ADD);
		addButton.addActionListener(this);

		return addButton;
	}

	/**
	 * 
	 * @return The modify button.
	 */
	private JButton createModifyButton()
	{
		JButton modifyButton = new JButton(MODIFY_TEXT);
		modifyButton.setActionCommand(MODIFY);
		modifyButton.addActionListener(this);

		return modifyButton;
	}

	/**
	 * 
	 * @return The remove button.
	 */
	private JButton createRemoveButton()
	{
		JButton removeButton = new JButton(REMOVE_TEXT);
		removeButton.setActionCommand(REMOVE);
		removeButton.addActionListener(this);

		return removeButton;
	}

	@Override
	public void update()
	{
		updateList();
	}

	/**
	 * Updates the property mapper list.
	 */
	private void updateList()
	{
		PropertyStyleMapper mapper = this.controller.getPropertyStyleMapper(this.propertyMappable);

		setPropertyMapper(mapper);
	}

	private void setPropertyMapper(PropertyStyleMapper mapper)
	{
		this.propertyMapperList.removeAllElements();

		if (mapper != null)
		{
			// continuous
			if (mapper instanceof PropertyStyleContinuous)
			{
				updateListContinousMapper((PropertyStyleContinuous) mapper);
			}
			// discrete
			else if (mapper instanceof PropertyStyleDiscrete)
			{
				updateListDiscreteMapper((PropertyStyleDiscrete) mapper);
			}
			// unknown
			else
			{
				updateListUnknownMapper();
			}
		}
		else
		// The property mapper is null.
		{
			// This makes sense when the user has added the property mapper
			// panel, but hasn't anything to it that
			// would actually create a property mapper... so show nothing.
		}
	}

	/**
	 * Updates (adds to) the list with an unknown mapper.
	 */
	private void updateListUnknownMapper()
	{
		this.propertyMapperList.addElement(new UnknownMapperListItem());
	}

	/**
	 * Updates (adds to) the list with an discrete mapper.
	 */
	private void updateListDiscreteMapper(PropertyStyleDiscrete discreteMapper)
	{
		DiscretePropertyMapper propertyMapper = discreteMapper.getPropertyMapper();
		DiscreteStyleMapper styleMapper = discreteMapper.getStyleMapper();

		// cog to color
		if (propertyMapper instanceof COGMapper && styleMapper instanceof DiscretePaintMapper)
		{
			String[] cogCategories = ((COGMapper) propertyMapper).getCategories();
			Paint[] colors = ((DiscretePaintMapper) styleMapper).getColors();

			// add all the cog to color items in the mapper as individual list
			// items.. not all as one!
			for (int i = 0; i < cogCategories.length && i < colors.length; i++)
			{
				this.propertyMapperList.addElement(new COGToColorMapperListItem(cogCategories[i], colors[i]));
			}
		}
		// all others or unknown
		else
		{
			this.propertyMapperList.addElement(new DiscreteMapperListItem(propertyMapper, styleMapper));
		}
	}

	/**
	 * Updates (adds to) the list with an continuous mapper.
	 */
	private void updateListContinousMapper(PropertyStyleContinuous continuousMapper)
	{
		ContinuousPropertyMapper propertyMapper = continuousMapper.getPropertyMapper();
		ContinuousStyleMapper styleMapper = continuousMapper.getStyleMapper();

		// score to opacity
		if (propertyMapper instanceof PropertyMapperScore && styleMapper instanceof OpacityFeatureStyleMapper)
		{
			this.propertyMapperList.addElement(new ScoreToOpacityMapperListItem((PropertyMapperScore) propertyMapper));
		}
		// all others or unknown
		else
		{
			this.propertyMapperList.addElement(new ContinuousMapperListItem(propertyMapper, styleMapper));
		}
	}

	@Override
	protected void doApply()
	{
		applyList();
	}

	private PropertyStyleMapper getPropertyMapper()
	{
		PropertyStyleMapper propertyStyleMapper = null;
		/*
		 * Currently only allows for application of the one property mapper, so
		 * we will throw an error if there are different types of property
		 * mappers.
		 */

		Class<?> listClassType;
		PropertyMapperListItem current;

		if (!this.propertyMapperList.isEmpty())
		// if not empty
		{
			listClassType = this.propertyMapperList.get(0).getClass();
			current = this.propertyMapperList.get(0);

			// check consistency
			if (this.propertyMapperList.getListConsistency(listClassType))
			{
				// continuous
				if (current instanceof ContinuousMapperListItem)
				{
					propertyStyleMapper = getContinuousPropertyMapper(current);
				}
				// discrete
				else if (current instanceof DiscreteMapperListItem)
				{
					propertyStyleMapper = getDiscretePropertyMapper();
				}
				// unknown
				else
				{
					throw new IllegalArgumentException("Unrecognized property mapper in PropertyMapperList.");
				}
			}
			// list not consistent
			else
			{
				throw new IllegalArgumentException("PropertyMapperList does not have consistent classes.");
			}
		}

		return propertyStyleMapper;
	}

	/**
	 * Applies the items in the list to the internal property mapper.
	 */
	private void applyList()
	{
		/*
		 * Currently only allows for application of the one property mapper, so
		 * we will throw an error if there are different types of property
		 * mappers.
		 */
		try
		{
			PropertyStyleMapper propertyStyleMapper = getPropertyMapper();
			this.controller.setPropertyStyleMapper(propertyStyleMapper, this.propertyMappable);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return The discrete property mapper.
	 */
	private PropertyStyleMapper getDiscretePropertyMapper()
	{
		if (this.propertyMapperList.get(0) instanceof COGToColorMapperListItem)
		{
			return getCOGToColorMapper(this.propertyMapperList);
		}
		else if (!this.propertyMapperList.getListConsistency(DiscreteMapperListItem.class))
		{
			throw new IllegalArgumentException("Property mapper list not consistent.");
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * @param propertyMapperList
	 * @return The COG to colour mapper.
	 */
	private PropertyStyleMapper getCOGToColorMapper(PropertyMapperList propertyMapperList)
	{
		COGMapper cogMapper;
		DiscretePaintMapper paintMapper;

		// used to collect the cog categories and colors
		ArrayList<String> cogCategories = new ArrayList<String>();
		ArrayList<Paint> colors = new ArrayList<Paint>();

		// used in the actual creation
		String[] cogCategoriesArray = new String[0];
		Paint[] colorsArray = new Paint[0];

		COGToColorMapperListItem current;

		// populate array lists
		for (int i = 0; i < propertyMapperList.getListSize(); i++)
		{
			if (propertyMapperList.get(i) instanceof COGToColorMapperListItem)
			{
				current = (COGToColorMapperListItem) propertyMapperList.get(i);

				cogCategories.add(current.getCOGCategory());
				colors.add(current.getPaint());
			}
		}

		// convert array lists to arrays
		cogCategoriesArray = cogCategories.toArray(cogCategoriesArray);
		colorsArray = colors.toArray(colorsArray);

		// create mappers
		cogMapper = new COGMapper(cogCategoriesArray);
		paintMapper = new DiscretePaintMapper(colorsArray);

		return new PropertyStyleDiscrete(cogMapper, paintMapper);
	}

	/**
	 * 
	 * @param item
	 * @return The continuous property mapper.
	 */
	private PropertyStyleMapper getContinuousPropertyMapper(PropertyMapperListItem item)
	{
		if (item instanceof ScoreToOpacityMapperListItem)
		{
			return getScoreToOpacityMapper(item);
		}
		else if (!this.propertyMapperList.getListConsistency(ContinuousMapperListItem.class))
		{
			throw new IllegalArgumentException("Property mapper list not consistent.");
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * @param item
	 * @return The score to opacity mapper.
	 */
	private PropertyStyleMapper getScoreToOpacityMapper(PropertyMapperListItem item)
	{
		PropertyStyleContinuous mapper;

		PropertyMapperScore propertyMapper;
		OpacityFeatureStyleMapper styleMapper;

		ScoreToOpacityMapperListItem current;

		if (item instanceof ScoreToOpacityMapperListItem)
		{
			current = (ScoreToOpacityMapperListItem) item;

			propertyMapper = new PropertyMapperScore(current.getMin(), current.getMax());
			styleMapper = new OpacityFeatureStyleMapper();

			mapper = new PropertyStyleContinuous(propertyMapper, styleMapper);
		}
		else
		{
			throw new ClassCastException("item " + item + " is not of type ScoreToOpacityMapperListItem");
		}

		return mapper;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		// modify button
		if (e.getActionCommand().equals(MODIFY))
		{
			modifyAction();
		}
		// remove button
		else if (e.getActionCommand().equals(REMOVE))
		{
			removeAction();
		}
		// add button
		else if (e.getActionCommand().equals(ADD))
		{
			addAction();
		}
	}

	/**
	 * Modify list item action.
	 */
	private void modifyAction()
	{
		Object current = getCurrentSelection();

		if (current instanceof PropertyMapperListItem)
		{
			(new PropertyMapperChooserDialog(this)).displayModifyDialog((PropertyMapperListItem) current);
		}

		this.propertyMapperList.repaint(); // to refresh after modifications
	}

	/**
	 * Add list item action.
	 */
	private void addAction()
	{
		(new PropertyMapperChooserDialog(this)).displayAddDialog();
	}

	/**
	 * Remove list item action.
	 */
	private void removeAction()
	{
		Object current = getCurrentSelection();

		if (current instanceof PropertyMapperListItem)
		{
			this.propertyMapperList.removeElement((PropertyMapperListItem) current);

			this.broadcastEvent(new PropertyMapperStyleMapperEvent(getPropertyMapper()));

			if (this.propertyMapperList.getListSize() > 0)
				this.propertyMapperList.setSelectedIndex(0);
		}
	}

	/**
	 * Gets the current list selection.
	 * 
	 * @return The current list selection.
	 */
	private Object getCurrentSelection()
	{
		int selected = this.propertyMapperList.getSelectedIndex();
		Object current = null;

		if (selected >= 0 && selected < this.propertyMapperList.getListSize())
			current = this.propertyMapperList.get(selected);

		return current;
	}

	/**
	 * Adds the list item property mapper to the list.
	 * 
	 * @param listItem
	 *            The list item property mapper to add to the list.
	 */
	public void addPropertyMapper(PropertyMapperListItem listItem)
	{
		if (listItem != null)
		{
			this.propertyMapperList.addElement(listItem);

			this.broadcastEvent(new PropertyMapperStyleMapperEvent(getPropertyMapper()));
		}
		else
		{
			throw new IllegalArgumentException("PropertyMapperListItem is null.");
		}
	}

	/**
	 * Automatically fills the list with any missing COG categories with default
	 * colours if possible.
	 */
	public void autoPopulateCogsToColors()
	{
		COGToColorMapperListItem item;
		boolean exists = false;

		if (this.propertyMapperList.isEmpty()
				|| this.propertyMapperList.getListConsistency(COGToColorMapperListItem.class))
		{
			for (int i = 0; i < StyleEditorUtility.COG_CATEGORIES.length; i++)
			{
				// see if the COG category already exists in the list
				for (int j = 0; j < this.propertyMapperList.getListSize() && !exists; j++)
				{
					if (((COGToColorMapperListItem) this.propertyMapperList.get(j)).getCOGCategory().equals(
							StyleEditorUtility.COG_CATEGORIES[i]))
					{
						exists = true;
					}
				}

				// if it doesn't, add it
				if (!exists)
				{
					item = new COGToColorMapperListItem(StyleEditorUtility.COG_CATEGORIES[i],
							StyleEditorUtility.COG_COLORS[i]);
					this.propertyMapperList.addElement(item);
				}

				// reset exists
				exists = false;
			}

			this.broadcastEvent(new PropertyMapperStyleMapperEvent(getPropertyMapper()));
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Cannot auto populate COG categories.");
		}
	}

	/**
	 * Modifies by replacing the target with the source.
	 * 
	 * Will display a message if unable to modify.
	 * 
	 * @param source
	 *            The new list item.
	 * @param target
	 *            The target list item.
	 */
	public void modifyListItem(PropertyMapperListItem source, PropertyMapperListItem target)
	{
		if (!this.propertyMapperList.replace(source, target))
		{
			JOptionPane.showMessageDialog(this, "Unable to modify.");
		}
		else
		{
			this.broadcastEvent(new PropertyMapperStyleMapperEvent(getPropertyMapper()));
		}
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof PropertyMapperStyleMapperEvent)
		{
			this.setPropertyMapper(((PropertyMapperStyleMapperEvent) event).getData());
		}
	}
}
