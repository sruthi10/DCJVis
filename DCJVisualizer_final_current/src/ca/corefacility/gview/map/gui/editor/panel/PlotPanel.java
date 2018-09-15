package ca.corefacility.gview.map.gui.editor.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

import ca.corefacility.gview.map.controllers.PlotStyleController;
import ca.corefacility.gview.map.controllers.PlotStyleToken;
import ca.corefacility.gview.map.controllers.selection.SelectionController;
import ca.corefacility.gview.map.gui.editor.StyleColoredButton;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility;
import ca.corefacility.gview.map.gui.editor.StyleEditorUtility.ConversionException;
import ca.corefacility.gview.map.gui.editor.TriStateCheckBox;
import ca.corefacility.gview.map.gui.editor.communication.GUIEvent;
import ca.corefacility.gview.map.gui.editor.communication.SelectionEventBroadcaster;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotAutoScaleEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotDataEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotDataFileTextEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotGridLinesColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotGridLinesEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotLowerColorEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotRangeMaximumEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotRangeMinimumEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotTypeEvent;
import ca.corefacility.gview.map.gui.editor.communication.plotEvent.PlotUpperColorEvent;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxiablePanel;
import ca.corefacility.gview.map.gui.editor.panel.proxy.ProxyPlotPanel;
import ca.corefacility.gview.map.gui.hint.HintLabel;
import ca.corefacility.gview.style.datastyle.PlotBuilderType;
import ca.corefacility.gview.style.datastyle.PlotDrawerType;
import ca.corefacility.gview.style.io.PlotIO;
import ca.corefacility.gview.style.io.gss.exceptions.MalformedPlotDataException;
import ca.corefacility.gview.utils.Util;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * The panel for a plot.
 * 
 * @author Eric Marinier
 * 
 */
public class PlotPanel extends StylePanel implements ActionListener, DocumentListener, ItemListener, ProxiablePanel
{
	private static final long serialVersionUID = 1L; // requested by java
	private static final int paintArraySize = 2;

	private static final String PLOT_COLOR_UPPER = "Upper Plot Color";
	private static final String PLOT_COLOR_LOWER = "Lower Plot Color";
	private static final String GRID_COLOR = "Grid Color";
	private static final String TYPE = "Type";
	private static final String DATA = "Data";

	private static final String AUTO_SCALE_SELECTED = "Auto-Scale Selected";

	private static final String INVALID_GRID_LINES = "Invalid number of grid lines.";
	private static final String INVALID_MINIMUM = "Invalid minimum plot value.";
	private static final String INVALID_MAXIMUM = "Invalid maximum plot value.";
	private static final String PLOT_STYLE_TOKEN_NULL = "PlotStyleToken is null.";

	private static final String DATA_LABEL_TEXT = "Data:";
	private static final String TYPE_LABEL_TEXT = "Type:";
	private static final String FILE_LABEL_TEXT = "File:";
	private static final String GRID_LINES_LABEL_TEXT = "Grid Lines:";
	private static final String GRID_COLOR_LABEL_TEXT = "Grid Color:";
	private static final String UPPER_COLOR_LABEL_TEXT = "Upper Color:";
	private static final String LOWER_COLOR_LABEL_TEXT = "Lower Color:";
	private static final String AUTO_SCALE_TEXT = "Auto Scale:";
	private static final String MINIMUM_TEXT = "Minimum:";
	private static final String MAXIMUM_TEXT = "Maximum:";

	private static final String BROWSE = "Browse ...";
	private static final String PLOT_STYLE_TEXT = "Plot Style";

	private static final String GC_CONTENT = "GC Content";
	private static final String GC_SKEW = "GC Skew";
	private static final String FILE_RANGE = "File (range)";
	private static final String FILE_POINT = "File (point)";

	private static final String[] DATA_TYPES = { GC_CONTENT, GC_SKEW, FILE_RANGE, FILE_POINT };

	private static final String LINE = "line";
	private static final String BAR = "bar";
	private static final String CENTER = "center";

	private static final String[] PLOT_TYPES = { LINE, BAR, CENTER };

	private static final String MALFORMED_PLOT_DATA_EXCEPTION = "Malformed Plot Data Exception";
	private static final String IO_EXCEPTION = "IO Exception";

	private static final String DATA_HINT = "The data type of the plot. If the plot type selected is a GC Skew or GC Content plot, the data for the plot will be generated by GView. For any other type of plot, the data for the plot points will be taken from a Comma Separated Values (CSV) file that must be supplied.";
	private static final String TYPE_HINT = "The type of plot to draw.";
	private static final String GRID_LINES_HINT = "The number of grid lines to draw in the plot.";
	private static final String GRID_COLOR_HINT = "The color of the grid lines.";
	private static final String UPPER_COLOR_HINT = "The upper color of the plot. For GC Content and GC Skew \"center\" type plots, this represents areas in which the local GC content or skew is above average for the entire sequence. For the other plot types, this is the only color used in the plot.";
	private static final String LOWER_COLOR_HINT = "The lower color of the plot. For GC Content and GC Skew \"center\" type plots, this represents areas in which the local GC content or skew is below average for the entire sequence. This is not used by \"line\" or \"bar\" type plots or by file plots.";
	private static final String AUTO_SCALE_HINT = "Whether or not to automatically scale the plot. This will make the minimum value of the plot the minimum possible value in data set, and similar for the maximum value.";
	private static final String MAXIMUM_HINT = "The maximum value of the plot as an integer. All higher values will be displayed as the maximum value.";
	private static final String MINIMUM_HINT = "The minimum value of the plot as an integer. All lower values will be displayed as the minimum value.";

	private final StyleColoredButton plotColorUpper;
	private final StyleColoredButton plotColorLower;
	private final StyleColoredButton gridColor;

	private final JButton browseFileButton;

	private final Document dataFileTextDocument;
	private final Document gridLinesDocument;
	private final Document minDocument;
	private final Document maxDocument;
	private boolean isDocumentUpdating = false;

	private final JTextField dataFileTextField;
	private final JTextField gridLines;
	private final JTextField minimum;
	private final JTextField maximum;

	private final JComboBox dataType;
	private final JComboBox type;

	private final JLabel lblFile;

	private final TriStateCheckBox autoScale;

	private final PlotStyleController plotController;
	private final SelectionController selectionController;

	private final PlotStyleToken plotStyle;

	private final JFileChooser fileChooser;
	private File currentDirectory = null;
	private File chosenDataFile;

	/**
	 * Create the panel.
	 */
	public PlotPanel(PlotStyleController plotController, PlotStyleToken plotStyle,
			SelectionController selectionController)
	{
		super();

		if (plotStyle == null)
			throw new IllegalArgumentException(PLOT_STYLE_TOKEN_NULL);

		this.plotController = plotController;
		this.selectionController = selectionController;
		this.plotStyle = plotStyle;

		this.addGUIEventBroadcaster(new SelectionEventBroadcaster(selectionController));

		// Layout
		setBorder(new EmptyBorder(10, 10, 10, 10));
		FormLayout formLayout = new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:default"), FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("max(165dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC, FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC, },
				new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("15dlu"),
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, });
		formLayout.setRowGroups(new int[][] { new int[] { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20 } });

		JPanel inner = new JPanel();
		inner.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), PLOT_STYLE_TEXT,
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		add(inner, BorderLayout.NORTH);

		inner.setLayout(formLayout);

		// DATA TYPE
		JPanel panel_4 = new JPanel();
		inner.add(panel_4, "2, 2, fill, fill");
		panel_4.setLayout(new BorderLayout(0, 0));

		JLabel lblThickness = new JLabel(DATA_LABEL_TEXT);
		panel_4.add(lblThickness, BorderLayout.EAST);

		// data overall panel
		JPanel panel = new JPanel();
		inner.add(panel, "6, 2, fill, fill");
		panel.setLayout(new GridLayout());

		// data combo box
		this.dataType = new JComboBox(DATA_TYPES);
		this.dataType.addActionListener(this);
		this.dataType.setActionCommand(DATA);
		panel.add(this.dataType);

		// hint
		inner.add(new HintLabel(DATA_HINT), "10, 2");

		// FILE
		JPanel filePanel = new JPanel();
		inner.add(filePanel, "2, 4, fill, fill");
		filePanel.setLayout(new BorderLayout(0, 0));

		this.lblFile = new JLabel(FILE_LABEL_TEXT);
		filePanel.add(this.lblFile, BorderLayout.EAST);

		JPanel fileSubPanel = new JPanel();
		inner.add(fileSubPanel, "6, 4, fill, fill");
		fileSubPanel.setLayout(new BoxLayout(fileSubPanel, BoxLayout.LINE_AXIS));

		// data text field
		this.dataFileTextField = new JTextField();
		this.dataFileTextField.setColumns(10);
		this.dataFileTextField.getDocument().addDocumentListener(this);
		this.dataFileTextDocument = this.dataFileTextField.getDocument();
		fileSubPanel.add(this.dataFileTextField);

		fileSubPanel.add(new Box.Filler(new Dimension(2, 0), new Dimension(2, 0), new Dimension(2, 0)));

		// browse file button
		this.browseFileButton = new JButton(BROWSE);
		this.browseFileButton.setActionCommand(BROWSE);
		this.browseFileButton.addActionListener(this);
		fileSubPanel.add(this.browseFileButton);

		// PLOT TYPE
		JPanel panel_5 = new JPanel();
		inner.add(panel_5, "2, 6, fill, fill");
		panel_5.setLayout(new BorderLayout(0, 0));

		JLabel lblType = new JLabel(TYPE_LABEL_TEXT);
		panel_5.add(lblType, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		inner.add(panel_1, "6, 6, fill, fill");
		panel_1.setLayout(new GridLayout());

		// plot type combo box
		this.type = new JComboBox(PLOT_TYPES);
		this.type.addActionListener(this);
		this.type.setActionCommand(TYPE);
		panel_1.add(type);

		// hint
		inner.add(new HintLabel(TYPE_HINT), "10, 6");

		// GRID LINES
		JPanel panel_6 = new JPanel();
		inner.add(panel_6, "2, 8, fill, fill");
		panel_6.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureShape = new JLabel(GRID_LINES_LABEL_TEXT);
		panel_6.add(lblFeatureShape, BorderLayout.EAST);

		JPanel panel_2 = new JPanel();
		inner.add(panel_2, "6, 8, fill, fill");
		panel_2.setLayout(new GridLayout());

		// grid lines text field
		this.gridLines = new JTextField();
		this.gridLines.setColumns(10);
		this.gridLines.getDocument().addDocumentListener(this);
		this.gridLinesDocument = this.gridLines.getDocument();
		panel_2.add(this.gridLines);

		// hint
		inner.add(new HintLabel(GRID_LINES_HINT), "10, 8");

		// GRID COLOR
		JPanel panel_7 = new JPanel();
		inner.add(panel_7, "2, 10, fill, fill");
		panel_7.setLayout(new BorderLayout(0, 0));

		JLabel lblFeatureEffect = new JLabel(GRID_COLOR_LABEL_TEXT);
		panel_7.add(lblFeatureEffect, BorderLayout.EAST);

		JPanel panel_3 = new JPanel();
		inner.add(panel_3, "6, 10, fill, fill");
		panel_3.setLayout(new BorderLayout(0, 0));

		// grid color button
		this.gridColor = new StyleColoredButton();
		this.gridColor.setActionCommand(GRID_COLOR);
		this.gridColor.addActionListener(this);
		panel_3.add(this.gridColor, BorderLayout.WEST);

		// hint
		inner.add(new HintLabel(GRID_COLOR_HINT), "10, 10");

		// UPPER PLOT COLOR
		JPanel panel_8 = new JPanel();
		inner.add(panel_8, "2, 12, fill, fill");
		panel_8.setLayout(new BorderLayout(0, 0));

		JLabel lblColor = new JLabel(UPPER_COLOR_LABEL_TEXT);
		panel_8.add(lblColor, BorderLayout.EAST);

		JPanel panel_9 = new JPanel();
		inner.add(panel_9, "6, 12, fill, fill");
		panel_9.setLayout(new BorderLayout(0, 0));

		// upper plot color
		this.plotColorUpper = new StyleColoredButton();
		this.plotColorUpper.setActionCommand(PLOT_COLOR_UPPER);
		this.plotColorUpper.addActionListener(this);
		panel_9.add(this.plotColorUpper, BorderLayout.WEST);

		// hint
		inner.add(new HintLabel(UPPER_COLOR_HINT), "10, 12");

		// LOWER PLOT COLOR
		JPanel panel_10 = new JPanel();
		inner.add(panel_10, "2, 14, fill, fill");
		panel_10.setLayout(new BorderLayout(0, 0));

		JLabel lblColor2 = new JLabel(LOWER_COLOR_LABEL_TEXT);
		panel_10.add(lblColor2, BorderLayout.EAST);

		JPanel panel_11 = new JPanel();
		inner.add(panel_11, "6, 14, fill, fill");
		panel_11.setLayout(new BorderLayout(0, 0));

		// lower plot color
		this.plotColorLower = new StyleColoredButton();
		this.plotColorLower.setActionCommand(PLOT_COLOR_LOWER);
		this.plotColorLower.addActionListener(this);
		panel_11.add(this.plotColorLower, BorderLayout.WEST);

		// hint
		inner.add(new HintLabel(LOWER_COLOR_HINT), "10, 14");

		// AUTO SCALE
		JPanel panel_12 = new JPanel();
		inner.add(panel_12, "2, 16, fill, fill");
		panel_12.setLayout(new BorderLayout(0, 0));

		JLabel lblAutoScale = new JLabel(AUTO_SCALE_TEXT);
		panel_12.add(lblAutoScale, BorderLayout.EAST);

		JPanel panel_13 = new JPanel();
		inner.add(panel_13, "6, 16, fill, fill");
		panel_13.setLayout(new BorderLayout(0, 0));

		// auto scale
		this.autoScale = new TriStateCheckBox();
		panel_13.add(autoScale, BorderLayout.WEST);
		this.autoScale.setSelected(true);
		this.autoScale.setActionCommand(AUTO_SCALE_SELECTED);
		this.autoScale.addItemListener(this);

		// hint
		inner.add(new HintLabel(AUTO_SCALE_HINT), "10, 16");

		// MINIMUM
		JPanel panel_14 = new JPanel();
		inner.add(panel_14, "2, 18, fill, fill");
		panel_14.setLayout(new BorderLayout(0, 0));

		JLabel lblMinimum = new JLabel(MINIMUM_TEXT);
		panel_14.add(lblMinimum, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		inner.add(panel_15, "6, 18, fill, fill");
		panel_15.setLayout(new GridLayout());

		// minimum text field
		this.minimum = new JTextField();
		this.minimum.setColumns(10);
		this.minimum.getDocument().addDocumentListener(this);
		this.minDocument = this.minimum.getDocument();
		panel_15.add(this.minimum);

		// hint
		inner.add(new HintLabel(MINIMUM_HINT), "10, 18");

		// MAXIMUM
		JPanel panel_16 = new JPanel();
		inner.add(panel_16, "2, 20, fill, fill");
		panel_16.setLayout(new BorderLayout(0, 0));

		JLabel lblMaximum = new JLabel(MAXIMUM_TEXT);
		panel_16.add(lblMaximum, BorderLayout.EAST);

		JPanel panel_17 = new JPanel();
		inner.add(panel_17, "6, 20, fill, fill");
		panel_17.setLayout(new GridLayout());

		// maximum text field
		this.maximum = new JTextField();
		this.maximum.setColumns(10);
		this.maximum.getDocument().addDocumentListener(this);
		this.maxDocument = this.maximum.getDocument();
		panel_17.add(this.maximum);

		// hint
		inner.add(new HintLabel(MAXIMUM_HINT), "10, 20");

		// OTHER
		// file chooser
		this.fileChooser = new JFileChooser(currentDirectory);
		this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		this.update();
	}

	/**
	 * Applies GUI changes to create the proxy version of the panel.
	 */
	private void proxy()
	{
		this.setUpdating(true);

		this.dataType.addItem(StyleEditorUtility.PROXY_VALUE);
		this.dataType.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.dataFileTextField.setText(null);

		this.type.addItem(StyleEditorUtility.PROXY_VALUE);
		this.type.setSelectedItem(StyleEditorUtility.PROXY_VALUE);

		this.gridLines.setText(null);

		this.gridColor.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.gridColor.setNotify(true);

		this.plotColorUpper.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.plotColorUpper.setNotify(true);

		this.plotColorLower.setBackground(StyleEditorUtility.DEFAULT_COLOR);
		this.plotColorLower.setNotify(true);

		this.autoScale.setSelected(false);
		this.autoScale.setMixed(true);

		this.minimum.setText(null);

		this.maximum.setText(null);

		this.setUpdating(false);
	}

	@Override
	public PlotPanel createProxy()
	{
		ProxyPlotPanel panel = new ProxyPlotPanel(plotController, plotStyle, selectionController);

		((PlotPanel) panel).proxy();

		return panel;
	}

	/**
	 * The response to the date file text being changed.
	 */
	private void dataFileTextAction()
	{
		this.broadcastEvent(new PlotDataFileTextEvent(this.getFileText()));
	}

	/**
	 * The response to the date type being changed.
	 */
	private void dataTypeAction()
	{
		updateDataGUIElements();

		this.broadcastEvent(new PlotDataEvent(this.getDataType()));
	}

	/**
	 * The response to the plot type being changed.
	 */
	private void typeAction()
	{
		this.broadcastEvent(new PlotTypeEvent(this.getType()));
	}

	/**
	 * The response to the number of grid lines being changed.
	 */
	private void gridLinesAction()
	{
		// Is the content an integer?
		if (Util.isInteger(this.getGridLinesText()))
		{
			this.broadcastEvent(new PlotGridLinesEvent(Integer.parseInt(this.getGridLinesText())));
		}
	}

	/**
	 * The response to the grid color being changed.
	 */
	private void gridColorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.gridColor.getBackground());

		if (c != null)
		{
			this.gridColor.setBackground(c);
			this.broadcastEvent(new PlotGridLinesColorEvent(c));
		}
	}

	/**
	 * The response to the upper plot color being changed.
	 */
	private void upperColorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.plotColorUpper.getBackground());

		if (c != null)
		{
			this.plotColorUpper.setBackground(c);
			this.broadcastEvent(new PlotUpperColorEvent(c));
		}
	}

	/**
	 * The response to the lower plot color being changed.
	 */
	private void lowerColorAction()
	{
		Color c = StyleEditorUtility.showColorPicker(this, this.plotColorLower.getBackground());

		if (c != null)
		{
			this.plotColorLower.setBackground(c);
			this.broadcastEvent(new PlotLowerColorEvent(c));
		}
	}

	/**
	 * The response to the auto scale option being changed.
	 */
	private void autoScaleAction()
	{
		updateAutoScaleGUIElements();

		this.broadcastEvent(new PlotAutoScaleEvent(this.getAutoScale()));
	}

	/**
	 * The response to the minimum plot value being changed.
	 */
	private void minimumAction()
	{
		// Is the content an integer?
		if (Util.isInteger(this.getMinimumText()))
		{
			try
			{
				this.broadcastEvent(new PlotRangeMinimumEvent(this.getMinimum()));
			}
			catch (ConversionException e)
			{
			}
		}
	}

	/**
	 * The response to the maximum plot value being changed.
	 */
	private void maximumAction()
	{
		// Is the content an integer?
		if (Util.isInteger(this.getMaximumText()))
		{
			try
			{
				this.broadcastEvent(new PlotRangeMaximumEvent(this.getMaximum()));
			}
			catch (ConversionException e)
			{
			}
		}
	}

	/**
	 * 
	 * @return The color of the upper portion of the plot.
	 */
	private Paint getPlotUpperPaint()
	{
		Paint p = this.plotColorUpper.getPaint();

		return p;
	}

	/**
	 * Sets the color of the upper portion of the plot.
	 * 
	 * @param c
	 */
	private void setPlotUpperPaint(Paint p)
	{
		this.plotColorUpper.setPaint(p);
	}

	/**
	 * 
	 * @return The color of the lower portion of the slot.
	 */
	private Paint getPlotLowerPaint()
	{
		Paint p = this.plotColorLower.getPaint();

		return p;
	}

	/**
	 * Sets the color of the lower portion of the plot.
	 * 
	 * @param c
	 */
	private void setPlotLowerPaint(Paint p)
	{
		this.plotColorLower.setPaint(p);
	}

	/**
	 * 
	 * @return The color of the grid.
	 */
	private Paint getGridPaint()
	{
		Paint p = this.gridColor.getPaint();

		return p;
	}

	/**
	 * Sets the color of the grid.
	 * 
	 * @param c
	 */
	private void setGridPaint(Paint p)
	{
		this.gridColor.setPaint(p);
	}

	/**
	 * Gets the text in the file text field.
	 * 
	 * @return The text in the file text field.
	 */
	private String getFileText()
	{
		return this.dataFileTextField.getText();
	}

	/**
	 * Sets the file text field text.
	 * 
	 * @param text
	 */
	private void setFileText(String text)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.dataFileTextField.setText(text);
	}

	/**
	 * 
	 * @return The text in the number of grid lines field.
	 */
	private String getGridLinesText()
	{
		return this.gridLines.getText();
	}

	/**
	 * Sets the text in the grid lines field.
	 * 
	 * @param i
	 *            The number of grid lines.
	 */
	private void setGridLinesText(int i)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.gridLines.setText(Integer.toString(i));
	}

	/**
	 * 
	 * @return The text in the plot type combo box.
	 */
	private String getType()
	{
		Object temp;
		String result;

		temp = this.type.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Type is not a String");
		}

		return result;
	}

	/**
	 * Sets the text in the plot type combo box.
	 * 
	 * @param shape
	 *            The shape to using when drawing the graph.
	 */
	private void setType(String shape)
	{
		this.type.setSelectedItem(shape);
	}

	/**
	 * Updates the GUI elements associated with the Data object.
	 */
	private void updateDataGUIElements()
	{
		if (this.dataType.getSelectedItem().equals(FILE_RANGE) || this.dataType.getSelectedItem().equals(FILE_POINT))
		{
			this.dataFileTextField.setEnabled(true);
			this.browseFileButton.setEnabled(true);
			this.lblFile.setEnabled(true);
		}
		else
		{
			this.dataFileTextField.setEnabled(false);
			this.browseFileButton.setEnabled(false);
			this.lblFile.setEnabled(false);
		}
	}

	/**
	 * Sets the data type.
	 * 
	 * @param s
	 *            The data type.
	 */
	private void setDataType(String s)
	{
		this.dataType.setSelectedItem(s);

		updateDataGUIElements();
	}

	/**
	 * 
	 * @return The selected data type.
	 */
	private String getDataType()
	{
		Object temp;
		String result;

		temp = this.dataType.getSelectedItem();

		if (temp instanceof String)
		{
			result = (String) temp;
		}
		else
		{
			throw new IllegalArgumentException("Data Type is not a String");
		}

		return result;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getActionCommand().equals(PLOT_COLOR_UPPER))
		{
			this.upperColorAction();
		}
		else if (e.getActionCommand().equals(PLOT_COLOR_LOWER))
		{
			this.lowerColorAction();
		}
		else if (e.getActionCommand().equals(GRID_COLOR))
		{
			this.gridColorAction();
		}
		else if (e.getActionCommand().equals(DATA))
		{
			this.dataTypeAction();
		}
		else if (e.getActionCommand().equals(TYPE))
		{
			this.typeAction();
		}
		else if (e.getActionCommand().equals(BROWSE))
		{
			this.fileChooser.setCurrentDirectory(this.currentDirectory);
			this.fileChooser.showOpenDialog(this);
			this.currentDirectory = this.fileChooser.getCurrentDirectory();

			this.chosenDataFile = fileChooser.getSelectedFile();

			if (this.chosenDataFile != null)
			{
				setFileText(this.chosenDataFile.getAbsolutePath());
			}
		}
	}

	@Override
	/**
	 * Updates the panel.
	 */
	public void update()
	{
		// Order is important:
		updateData();
		updateType();
		updateGridLines();
		updateGridColor();
		updateUpperColor();
		updateLowerColor();
		updateAutoScale();
		updateMinimum();
		updateMaximum();
	}

	/**
	 * 
	 * @return The plot builder.
	 * 
	 * @throws ConversionException
	 */
	private PlotBuilderType getPlotBuilderType() throws ConversionException
	{
		String dataType = getDataType();
		PlotBuilderType plotBuilder = null;
		File file;

		int min;
		int max;

		if (dataType.equals(GC_CONTENT))
		{
			plotBuilder = new PlotBuilderType.GCContent();
		}
		else if (dataType.equals(GC_SKEW))
		{
			plotBuilder = new PlotBuilderType.GCSkew();
		}
		else if (dataType.equals(FILE_POINT))
		{
			file = new File(getFileText());

			try
			{
				plotBuilder = PlotIO.readPointFile(file.toURI());
			}
			catch (MalformedPlotDataException e)
			{
				throw new StyleEditorUtility.ConversionException(MALFORMED_PLOT_DATA_EXCEPTION);
			}
			catch (IOException e)
			{
				throw new StyleEditorUtility.ConversionException(IO_EXCEPTION);
			}
		}
		else if (dataType.equals(FILE_RANGE))
		{
			file = new File(getFileText());

			try
			{
				plotBuilder = PlotIO.readRangeFile(file.toURI());
			}
			catch (MalformedPlotDataException e)
			{
				throw new StyleEditorUtility.ConversionException(MALFORMED_PLOT_DATA_EXCEPTION);
			}
			catch (IOException e)
			{
				throw new StyleEditorUtility.ConversionException(IO_EXCEPTION);
			}
		}

		if (getAutoScale())
		{
			applyAutoScale(plotBuilder);
		}
		else
		{
			min = this.getMinimum();
			max = this.getMaximum();

			plotBuilder.setScale(min, max);
		}

		return plotBuilder;
	}

	/**
	 * Updates the data components.
	 */
	private void updateData()
	{
		PlotBuilderType plotBuilder = this.plotController.getPlotBuilderType(this.plotStyle);

		if (plotBuilder instanceof PlotBuilderType.GCContent)
		{
			setDataType(GC_CONTENT);
		}
		else if (plotBuilder instanceof PlotBuilderType.GCSkew)
		{
			setDataType(GC_SKEW);
		}
		else if (plotBuilder instanceof PlotBuilderType.Points)
		{
			setDataType(FILE_POINT);
		}
		else if (plotBuilder instanceof PlotBuilderType.Range)
		{
			setDataType(FILE_RANGE);
		}

		URI uri = plotBuilder.getURI();

		if (uri != null)
		{
			setFileText(uri.getPath());
		}
	}

	/**
	 * Updates the type.
	 */
	private void updateType()
	{
		PlotDrawerType plotDrawer = this.plotController.getPlotDrawerType(this.plotStyle);

		if (plotDrawer instanceof PlotDrawerType.Line)
		{
			setType(LINE);
		}
		else if (plotDrawer instanceof PlotDrawerType.Bar)
		{
			setType(BAR);
		}
		else if (plotDrawer instanceof PlotDrawerType.Center)
		{
			setType(CENTER);
		}
	}

	/**
	 * Updates the grid lines.
	 */
	private void updateGridLines()
	{
		setGridLinesText(this.plotController.getGridLines(this.plotStyle));
	}

	/**
	 * Updates the grid color.
	 */
	private void updateGridColor()
	{
		Paint tempPaint = this.plotController.getGridColor(this.plotStyle);

		setGridPaint(tempPaint);
	}

	/**
	 * Updates the upper grid color.
	 */
	private void updateUpperColor()
	{
		Paint tempPaint = this.plotController.getUpperColor(this.plotStyle);

		setPlotUpperPaint(tempPaint);
	}

	/**
	 * Updates the lower grid color.
	 */
	private void updateLowerColor()
	{
		Paint tempPaint = this.plotController.getLowerColor(this.plotStyle);

		setPlotLowerPaint(tempPaint);
	}

	/**
	 * Updates the minimum value of the plot.
	 */
	private void updateMinimum()
	{
		this.setMinimum(this.plotController.getMinimum(this.plotStyle));
	}

	/**
	 * Updates the maximum value of the plot.
	 */
	private void updateMaximum()
	{
		this.setMaximum(this.plotController.getMaximum(this.plotStyle));
	}

	/**
	 * Updates the auto-scale.
	 */
	private void updateAutoScale()
	{
		this.setAutoScale(this.plotController.getAutoScale(this.plotStyle));
	}

	@Override
	/**
	 * Applies the style.
	 */
	protected void doApply()
	{
		applyData();
		applyType();
		applyGridLines();
		applyGridColor();
		applyUpperAndLowerColor();
	}

	/**
	 * Applies the data components.
	 */
	private void applyData()
	{
		PlotBuilderType plotBuilder;
		try
		{
			plotBuilder = getPlotBuilderType();
			this.plotController.setPlotBuilderType(plotBuilder, this.plotStyle);
		}
		catch (ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private boolean getAutoScale()
	{
		return this.autoScale.isSelected();
	}

	/**
	 * Updates the GUI elements associated with the auto scale object.
	 */
	private void updateAutoScaleGUIElements()
	{
		if (!this.getAutoScale())
		{
			this.minimum.setEnabled(true);
			this.maximum.setEnabled(true);
		}
		else
		{
			this.minimum.setEnabled(false);
			this.maximum.setEnabled(false);
		}
	}

	private void setAutoScale(boolean auto)
	{
		this.autoScale.setSelected(auto);

		updateAutoScaleGUIElements();
	}

	/**
	 * Applies the auto scale or scales to the plots max and min
	 * 
	 * @param plotBuilder
	 *            The plot builder to auto scale or not.
	 */
	private void applyAutoScale(PlotBuilderType plotBuilder)
	{
		if (plotBuilder != null)
		{
			if (getAutoScale())
			{
				plotBuilder.autoScale(true);
			}
			else
			{
				plotBuilder.autoScale(false);
			}
		}
	}

	/**
	 * Applies the type.
	 */
	private void applyType()
	{
		try
		{
			this.plotController.setPlotDrawerType(getPlotDrawerType(), this.plotStyle);
		}
		catch (ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private PlotDrawerType getPlotDrawerType() throws ConversionException
	{
		String type = getType();

		// type
		if (type.equals(LINE))
		{
			return new PlotDrawerType.Line();
		}
		else if (type.equals(BAR))
		{
			return new PlotDrawerType.Bar();
		}
		else if (type.equals(CENTER))
		{
			return new PlotDrawerType.Center();
		}
		else
		{
			throw new StyleEditorUtility.ConversionException("Invalid Plot type " + type);
		}
	}

	/**
	 * Applies the grid lines.
	 */
	private void applyGridLines()
	{
		try
		{
			int gridLines = getGridLines();
			this.plotController.setGridLines(gridLines, this.plotStyle);
		}
		catch (StyleEditorUtility.ConversionException e)
		{
			JOptionPane.showMessageDialog(this, e.getMessage());

			updateGridLines();
		}
	}

	/**
	 * 
	 * @return The number of grid lines.
	 * @throws StyleEditorUtility.ConversionException
	 */
	private int getGridLines() throws StyleEditorUtility.ConversionException
	{
		int lines = -1;

		try
		{
			lines = Integer.parseInt(getGridLinesText());

			if (lines < 0)
			{
				throw new StyleEditorUtility.ConversionException("Grid Lines value must be non-negative.");
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new StyleEditorUtility.ConversionException(INVALID_GRID_LINES);
		}

		return lines;
	}

	/**
	 * Applies the grid color.
	 */
	private void applyGridColor()
	{
		this.plotController.setGridColor(getGridPaint(), this.plotStyle);
	}

	/**
	 * Applies the upper and lower grid color.
	 */
	private void applyUpperAndLowerColor()
	{
		Paint[] paints = getUpperAndLowerColors();

		this.plotController.setColors(paints, this.plotStyle);
	}

	/**
	 * 
	 * @return The upper and lower colors as a two item Paint array.
	 */
	private Paint[] getUpperAndLowerColors()
	{
		Paint[] paints = new Paint[PlotPanel.paintArraySize];

		paints[0] = getPlotUpperPaint();
		paints[1] = getPlotLowerPaint();

		return paints;
	}

	/**
	 * 
	 * @return The plot style as a token.
	 */
	public PlotStyleToken getPlotStyle()
	{
		return this.plotStyle;
	}

	/**
	 * 
	 * @return The plot controller.
	 */
	public PlotStyleController getPlotController()
	{
		return this.plotController;
	}

	/**
	 * 
	 * @return The plot minimum value.
	 * @throws ConversionException
	 */
	public int getMinimum() throws ConversionException
	{
		int min;

		try
		{
			min = Integer.parseInt(getMinimumText());
		}
		catch (NumberFormatException nfe)
		{
			throw new StyleEditorUtility.ConversionException(INVALID_MINIMUM);
		}

		return min;
	}

	/**
	 * 
	 * @return The plot maximum value.
	 * @throws ConversionException
	 */
	public int getMaximum() throws ConversionException
	{
		int max;

		try
		{
			max = Integer.parseInt(getMaximumText());
		}
		catch (NumberFormatException nfe)
		{
			throw new StyleEditorUtility.ConversionException(INVALID_MAXIMUM);
		}

		return max;
	}

	/**
	 * Sets the minimum GUI element.
	 * 
	 * @param min
	 */
	private void setMinimum(int min)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.minimum.setText(Integer.toString(min));
	}

	/**
	 * Sets the maximum GUI element.
	 * 
	 * @param max
	 */
	private void setMaximum(int max)
	{
		if (this.isDocumentUpdating)
		{
			return;
		}

		this.maximum.setText(Integer.toString(max));
	}

	/**
	 * 
	 * @return The plot minimum as a string.
	 */
	public String getMinimumText()
	{
		return this.minimum.getText();
	}

	/**
	 * 
	 * @return The plot maximum as a string.
	 */
	public String getMaximumText()
	{
		return this.maximum.getText();
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

		if (e.getDocument().equals(this.dataFileTextDocument))
		{
			this.dataFileTextAction();
		}
		if (e.getDocument().equals(this.gridLinesDocument))
		{
			this.gridLinesAction();
		}
		if (e.getDocument().equals(this.minDocument))
		{
			this.minimumAction();
		}
		if (e.getDocument().equals(this.maxDocument))
		{
			this.maximumAction();
		}

		this.isDocumentUpdating = false;
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		if (this.isUpdating())
		{
			return;
		}

		if (e.getSource().equals(this.autoScale))
		{
			this.autoScaleAction();
		}
	}

	@Override
	public void executeGUIEvent(GUIEvent event)
	{
		if (event instanceof PlotAutoScaleEvent)
		{
			this.setAutoScale(((PlotAutoScaleEvent) event).getData());
		}
		else if (event instanceof PlotDataEvent)
		{
			this.setDataType(((PlotDataEvent) event).getData());
		}
		else if (event instanceof PlotDataFileTextEvent)
		{
			this.setFileText(((PlotDataFileTextEvent) event).getData());
		}
		else if (event instanceof PlotGridLinesColorEvent)
		{
			this.setGridPaint(((PlotGridLinesColorEvent) event).getData());
		}
		else if (event instanceof PlotGridLinesEvent)
		{
			this.setGridLinesText(((PlotGridLinesEvent) event).getData());
		}
		else if (event instanceof PlotLowerColorEvent)
		{
			this.setPlotLowerPaint(((PlotLowerColorEvent) event).getData());
		}
		else if (event instanceof PlotUpperColorEvent)
		{
			this.setPlotUpperPaint(((PlotUpperColorEvent) event).getData());
		}
		else if (event instanceof PlotRangeMaximumEvent)
		{
			this.setMaximum(((PlotRangeMaximumEvent) event).getData());
		}
		else if (event instanceof PlotRangeMinimumEvent)
		{
			this.setMinimum(((PlotRangeMinimumEvent) event).getData());
		}
		else if (event instanceof PlotTypeEvent)
		{
			this.setType(((PlotTypeEvent) event).getData());
		}
	}
}
