

package ca.corefacility.gview.map.gui.editor.featureFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.biojava.bio.seq.FeatureFilter;
import org.biojava.bio.seq.StrandedFeature;
import org.biojava.bio.symbol.RangeLocation;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.Parser;

import ca.corefacility.gview.map.controllers.GenomeDataController;
import ca.corefacility.gview.map.gui.editor.StyleEditorTree;
import ca.corefacility.gview.map.gui.editor.node.FeatureContainerNode;
import ca.corefacility.gview.map.gui.editor.node.SetNode;
import ca.corefacility.gview.style.io.gss.FeatureFilterHandler;
import ca.corefacility.gview.utils.AnnotationValueContains;
import ca.corefacility.gview.utils.SequenceNameFilter;

/**
 * A subclass of JDialog, used for returning a new FeatureFilter. 
 * 
 * Intended to be called from a StyleEditorTree.
 * 
 * @author Eric Marinier
 *
 */
public class FeatureFilterChooserDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;	//requested by java	
	
	public static final String FEATURE_ALL = "All";
	public static final String FEATURE_AND = "And";
	public static final String FEATURE_ANNOTATION_VALUE_CONTAINS = "Annotation Value Contains";
	public static final String FEATURE_ANNOTATION_VALUE_EQUALS = "Annotation Value Equals";
	public static final String FEATURE_BY_TYPE = "By Type";
	public static final String FEATURE_HAS_ANNOTATION = "Has Annotation";
	public static final String FEATURE_NOT = "Not";
	public static final String FEATURE_OR = "Or";
	public static final String FEATURE_OVERLAPS_LOCATION = "Overlaps Location";
	public static final String FEATURE_SEQUENCE_NAME = "Sequence Name";
	public static final String FEATURE_STRANDED = "Stranded";
	public static final String FEATURE_GSS_STRING = "GSS String";
	
	public static final String[] FEATURE_FILTERS = {FEATURE_ALL, FEATURE_BY_TYPE, FEATURE_ANNOTATION_VALUE_CONTAINS, FEATURE_ANNOTATION_VALUE_EQUALS,
		FEATURE_HAS_ANNOTATION, FEATURE_OVERLAPS_LOCATION, FEATURE_SEQUENCE_NAME, FEATURE_STRANDED};
	
	private static final String TITLE = "Set Chooser";
	private static final String CREATE = "Create";
	private static final String CANCEL = "Canel";
	private static final String REFINE = "Refine";
	
	public static final String POSITIVE = "Positive";
	public static final String NEGATIVE = "Negative";
	public static final String UNKNOWN = "Unknown";
	public static final String[] STRANDED = {POSITIVE, NEGATIVE, UNKNOWN};
	
	private static final String FEATURE_FILTER_COMBO_BOX = "Feature Filter Combo Box";
	
	private final JButton createButton;
	private final JButton refineButton;
	private final JButton cancelButton;
	
	private final JPanel contentPanel;
	private final JPanel specificContent;
	
	//allows the user to choose their feature filter
	private final JComboBox featureFilterComboBox;
	
	private final AnnotationValueContainsPanel annotationValueContainsPanel;
	private final AnnotationValueEqualsPanel annotationValueEqualsPanel;
	private final ByTypePanel byTypePanel;
	private final GSSStringPanel gssStringPanel = new GSSStringPanel();
	private final HasAnnotationPanel hasAnnotationPanel;
	private final OverlapsLocationPanel overlapsLocationPanel = new OverlapsLocationPanel();
	private final SequenceNamePanel sequenceNamePanel = new SequenceNamePanel();
	private final StrandedPanel strandedPanel = new StrandedPanel();
	
	private final StyleEditorTree styleTree;
	private final FeatureContainerNode parentNode;
	
	private final DIALOG_TYPE type;
	
	public enum DIALOG_TYPE
	{
		CREATE_SET,
		REFINE_SET;
	}

	/**
	 * 
	 * @param styleEditorTree The related style editor tree.
	 * @param parentNode The parent node to create the set under.
	 */
	public FeatureFilterChooserDialog(StyleEditorTree styleEditorTree, GenomeDataController controller, FeatureContainerNode parentNode, DIALOG_TYPE type)
	{		
		if(styleEditorTree == null)
		{
			throw new IllegalArgumentException("StyleEditorTree is null.");
		}
		
		if(parentNode == null)
		{
			throw new IllegalArgumentException("FeatureContainerNode is null.");
		}
		
		this.styleTree = styleEditorTree;
		this.parentNode = parentNode;
		this.type = type;
		
		this.setTitle(TITLE);
		
		//initialize
		this.createButton = buildCreateButton();
		this.cancelButton = buildCancelButton();
		this.refineButton = buildRefineButton();
		
		this.featureFilterComboBox = buildFeatureFilterComboBox();
		
		this.byTypePanel = new ByTypePanel(controller);
		this.annotationValueContainsPanel = new AnnotationValueContainsPanel(controller);
		this.annotationValueEqualsPanel = new AnnotationValueEqualsPanel(controller);
		this.hasAnnotationPanel = new HasAnnotationPanel(controller);
		
		this.specificContent  = buildSpecificContentPanel();
		this.contentPanel = buildContentPanel();	
		
		layoutComponents();
		
		this.setModal(true);	//blocks interaction with GUI components underneath.
		
		updateSpecificContent();
		
		if(type == DIALOG_TYPE.CREATE_SET)
		{
			this.getRootPane().setDefaultButton(this.createButton);
		}
		else if(type == DIALOG_TYPE.REFINE_SET)
		{
			this.getRootPane().setDefaultButton(this.refineButton);
		}
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
	private JPanel buildSpecificContentPanel()
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
		
		comboBoxPanel.setBorder(new TitledBorder(null, "Type", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, null));
		FlowLayout flowLayout = (FlowLayout) comboBoxPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		comboBoxPanel.add(this.featureFilterComboBox);
		
		return comboBoxPanel;
	}
	
	/**
	 * 
	 * @return A new JComboBox for the feature filter combo box variable.
	 */
	private JComboBox buildFeatureFilterComboBox()
	{
		JComboBox propertyMapperComboBox = new JComboBox(FEATURE_FILTERS);
		
		propertyMapperComboBox.setActionCommand(FEATURE_FILTER_COMBO_BOX);
		propertyMapperComboBox.addActionListener(this);
		
		return propertyMapperComboBox;
	}
	
	/**
	 * 
	 * @return A new JPanel for the content panel variable.
	 */
	private JPanel buildContentPanel()
	{
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));		
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		return contentPanel;
	}
	
	/**
	 * 
	 * @return A new JButton - create button.
	 */
	private JButton buildCreateButton()
	{
		JButton createButton = new JButton(CREATE);
		createButton.setActionCommand(CREATE);
		createButton.addActionListener(this);
		
		return createButton;
	}
	
	/**
	 * 
	 * @return A new JButton - refine button.
	 */
	private JButton buildRefineButton()
	{
		JButton refineButton = new JButton(REFINE);
		refineButton.setActionCommand(REFINE);
		refineButton.addActionListener(this);
		
		return refineButton;
	}
	
	/**
	 * 
	 * @return A new JButton - cancel button.
	 */
	private JButton buildCancelButton()
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
		
		if(this.type == DIALOG_TYPE.CREATE_SET)
		{
			buttonPanel.add(this.createButton);
		}
		else if(this.type == DIALOG_TYPE.REFINE_SET)
		{
			buttonPanel.add(this.refineButton);
		}
		
		buttonPanel.add(this.cancelButton);		
		getRootPane().setDefaultButton(this.cancelButton);
		
		return buttonPanel;
	}
	
	/**
	 * Updates the content specific portion of the panel with GUI components that are appropriate to the current
	 * combo box selection.
	 */
	private void updateSpecificContent()
	{
		this.specificContent.removeAll();
		
		if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ALL))
		{			
			//nothing
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_AND))
		{			
			this.specificContent.add(this.gssStringPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ANNOTATION_VALUE_CONTAINS))
		{			
			this.specificContent.add(this.annotationValueContainsPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ANNOTATION_VALUE_EQUALS))
		{			
			this.specificContent.add(this.annotationValueEqualsPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_BY_TYPE))
		{			
			this.specificContent.add(this.byTypePanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_GSS_STRING))
		{			
			this.specificContent.add(this.gssStringPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_HAS_ANNOTATION))
		{			
			this.specificContent.add(this.hasAnnotationPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_NOT))
		{			
			this.specificContent.add(this.gssStringPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_OR))
		{			
			this.specificContent.add(this.gssStringPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_OVERLAPS_LOCATION))
		{			
			this.specificContent.add(this.overlapsLocationPanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_SEQUENCE_NAME))
		{			
			this.specificContent.add(this.sequenceNamePanel);
		}
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_STRANDED))
		{			
			this.specificContent.add(this.strandedPanel);
		}		
		
		this.pack();
	}
	
	/**
	 * Shows the dialog over the passed frame.
	 * 
	 * @param frame The frame to show the dialog over.
	 */
	public void showDialog(JFrame frame)
	{
		if(frame == null)
			throw new IllegalArgumentException("JFrame is null.");
		
		this.setLocationRelativeTo(frame);	
		this.setVisible(true);		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		FeatureFilter featureFilter = null;
		
		//OK
		if(this.type == DIALOG_TYPE.CREATE_SET && e.getActionCommand().equals(CREATE))
		{
			featureFilter = createFeatureFilter();
			
			if(featureFilter != null)
			{
				this.styleTree.createSetNodeFromFeatureFilter(this.parentNode, featureFilter);				
				this.setVisible(false);
			}				
		}
		//REFINE
		else if(this.type == DIALOG_TYPE.REFINE_SET && e.getActionCommand().equals(REFINE) && this.parentNode instanceof SetNode)
		{
			featureFilter = createFeatureFilter();
			
			if(featureFilter != null)
			{
				this.styleTree.refineSetNodeFromFeatureFilter((SetNode)this.parentNode, featureFilter);				
				this.setVisible(false);
			}	
		}
		//CANCEL
		else if(e.getActionCommand().equals(CANCEL))
		{			
			this.setVisible(false);
		}
		//COMBO BOX
		else if(e.getActionCommand().equals(FEATURE_FILTER_COMBO_BOX))
		{
			updateSpecificContent();
		}
	}	

	/**
	 * Creates and returns a feature filter depending on the user input.
	 * 
	 * @return The user defined FeatureFilter object.
	 */
	private FeatureFilter createFeatureFilter()
	{
		FeatureFilter featureFilter = null;		
		
		//ALL
		if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ALL))
		{
			featureFilter = createFeatureFilterAll();
		}
		//ANNOTATION VALUE CONTAINS
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ANNOTATION_VALUE_CONTAINS))
		{
			featureFilter = createFeatureFilterAnnotationValueContains();
		}
		//ANNOTATION VALUE EQUALS
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_ANNOTATION_VALUE_EQUALS))
		{
			featureFilter = createFeatureFilterAnnotationValueEquals();
		}
		//BY TYPE
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_BY_TYPE))
		{
			featureFilter = createFeatureFilterByType();
		}
		//HAS ANNOTATION
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_HAS_ANNOTATION))
		{
			featureFilter = createFeatureFilterHasAnnotation();
		}
		//OVERLAPS LOCATION
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_OVERLAPS_LOCATION))
		{
			featureFilter = createFeatureFilterOverlapsLocation();		
		}
		//SEQUENCE NAME
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_SEQUENCE_NAME))
		{
			featureFilter = createFeatureFilterSequenceName();
		}
		//STRANDED
		else if(this.featureFilterComboBox.getSelectedItem().equals(FEATURE_STRANDED))
		{
			featureFilter = createFeatureFilterStranded();		
		}
		//TRY TO PARSE IT
		else
		{	
			featureFilter = createFeatureFilterParsed();
		}
		
		return featureFilter;
	}
	
	/**
	 * 
	 * @return An 'all' feature filter.
	 */
	private FeatureFilter createFeatureFilterAll()
	{
		return FeatureFilter.all;
	}
	
	/**
	 * 
	 * @return An annotation value contains feature filter.
	 */
	private FeatureFilter createFeatureFilterAnnotationValueContains()
	{		
		return new AnnotationValueContains(this.annotationValueContainsPanel.getAnnotation(), this.annotationValueContainsPanel.getValue());
	}
	
	/**
	 * 
	 * @return An annotation value equals feature filter.
	 */
	private FeatureFilter createFeatureFilterAnnotationValueEquals()
	{		
		return new FeatureFilter.ByAnnotation(this.annotationValueEqualsPanel.getAnnotation(), this.annotationValueEqualsPanel.getValue());
	}
	
	/**
	 * 
	 * @return A by type feature filter.
	 */
	private FeatureFilter createFeatureFilterByType()
	{
		return new FeatureFilter.ByType(this.byTypePanel.getType());
	}
	
	/**
	 * 
	 * @return A has annotation feature filter.
	 */
	private FeatureFilter createFeatureFilterHasAnnotation()
	{
		return new FeatureFilter.HasAnnotation(this.hasAnnotationPanel.getAnnotation());
	}
	
	/**
	 * 
	 * @return A overlaps location feature filter.
	 */
	private FeatureFilter createFeatureFilterOverlapsLocation()
	{	
		FeatureFilter result = null;
		
		try
		{
			result = new FeatureFilter.OverlapsLocation(new RangeLocation(Integer.parseInt(this.overlapsLocationPanel.getStart()), 
					Integer.parseInt(this.overlapsLocationPanel.getEnd())));
		}
		catch (NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(this, "Invalid numbers.");
		}
		
		return result;		
	}
	
	/**
	 * 
	 * @return A sequence name feature filter.
	 */
	private FeatureFilter createFeatureFilterSequenceName()
	{
		return new SequenceNameFilter(this.sequenceNamePanel.getSequenceName());
	}
	
	/**
	 * 
	 * @return A stranded feature filter.
	 */
	private FeatureFilter createFeatureFilterStranded()
	{
		FeatureFilter featureFilter = null;
		
		String stranded = this.strandedPanel.getStranded();
		
		if(stranded.equalsIgnoreCase(POSITIVE))
		{
			featureFilter = new FeatureFilter.StrandFilter(StrandedFeature.POSITIVE);
		}
		else if(stranded.equalsIgnoreCase(NEGATIVE))
		{
			featureFilter = new FeatureFilter.StrandFilter(StrandedFeature.NEGATIVE);
		}
		else if(stranded.equalsIgnoreCase(UNKNOWN))
		{
			featureFilter = new FeatureFilter.StrandFilter(StrandedFeature.UNKNOWN);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Unrecognized input. (Expects '" + POSITIVE 
					+ "', '" + NEGATIVE +"' or '" + UNKNOWN + "')");
		}
		
		return featureFilter;
	}
	
	/**
	 * 
	 * @return A feature filter determined by parsing.
	 */
	private FeatureFilter createFeatureFilterParsed()
	{
		FeatureFilter featureFilter = null;
		
		Parser parser = new com.steadystate.css.parser.SACParserCSS2();
		InputSource inputSource;
		LexicalUnit lexicalUnit;		
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(250, 150));
		
		
		try
		{
			inputSource = new InputSource(new StringReader(this.gssStringPanel.getGSS()));
			lexicalUnit = parser.parsePropertyValue(inputSource);
			
			featureFilter = FeatureFilterHandler.decode(lexicalUnit);
		}
		catch (CSSException e)
		{
			textArea.setText(e.toString());
			JOptionPane.showMessageDialog(this, scrollPane, "Parse Error", JOptionPane.WARNING_MESSAGE);			
		}
		catch (IOException e)
		{
			textArea.setText(e.toString());
			JOptionPane.showMessageDialog(this, scrollPane, "Parse Error", JOptionPane.WARNING_MESSAGE);	
		}
		
		return featureFilter;
	}
}
