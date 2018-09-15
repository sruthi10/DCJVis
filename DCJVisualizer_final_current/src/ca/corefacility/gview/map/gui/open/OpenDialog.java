package ca.corefacility.gview.map.gui.open;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.corefacility.gview.data.readers.GFF3Reader;
import ca.corefacility.gview.data.readers.GViewDataParseException;
import ca.corefacility.gview.data.readers.GViewFileReader;
import ca.corefacility.gview.layout.sequence.LayoutFactory;
import ca.corefacility.gview.layout.sequence.circular.LayoutFactoryCircular;
import ca.corefacility.gview.layout.sequence.linear.LayoutFactoryLinear;
import ca.corefacility.gview.map.gui.GUIManager;
import ca.corefacility.gview.map.gui.GViewGUIFrame;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class OpenDialog extends JPanel implements ActionListener, WindowListener
{
	private static final long serialVersionUID = 1L;
	private static final String OPEN_SEQUENCE = "Open Sequence";
	private static final String OPEN_STYLE = "Open Style";
	private static final String OPEN_GFF = "Open GFF";
	private static final String BUILD_MAP = "Build Map";
	
	private static final String LAYOUT_CIRCULAR = "Circular";
	private static final String LAYOUT_LINEAR = "Linear";
	
	private static final String LAYOUT_BOX = "Layout box";
	
	private static final LayoutFactory circularLayout = new LayoutFactoryCircular();
	private static final LayoutFactory linearLayout = new LayoutFactoryLinear();
	
	private static final GFF3Reader gffReader = new GFF3Reader();
	
	private static JFrame dialog;
	private static OpenDialog instance;
	
	private JFileChooser openSequenceChooser;
	private JTextField openSequenceTextField;
	
	private JFileChooser openStyleChooser;
	private JTextField openStyleTextField;
	
	private JFileChooser openGFFChooser;
	private JTextField openGFFTextField;
	
	private JComboBox layoutBox;
	
	private LayoutFactory currLayout = circularLayout;
		
	public OpenDialog()
	{		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layoutPanel(this, layout);
		
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.addWindowListener(this);
	}
	
	private void layoutPanel(JPanel panel, GroupLayout layout)
	{			
		JPanel descriptionPanel = makeDescriptionPanel();
		JPanel openPanel = makeOpenPanel();
		JPanel optionsPanel = makeOptionsPanel();
		JButton buildMap = new JButton(BUILD_MAP);
		buildMap.addActionListener(this);
		
		GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();
		horizontalGroup.addGroup(layout.createParallelGroup()
				.addComponent(descriptionPanel)
				.addComponent(openPanel)
				.addComponent(optionsPanel)
				.addComponent(buildMap, GroupLayout.Alignment.TRAILING));
		layout.setHorizontalGroup(horizontalGroup);
		
		GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
		verticalGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(descriptionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		verticalGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(openPanel));
		verticalGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(optionsPanel));
		verticalGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(buildMap));
		layout.setVerticalGroup(verticalGroup);		
	}
	
	private JPanel makeDescriptionPanel()
	{
		JPanel descriptionPanel = new JPanel(new BorderLayout());
		descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
		
		JLabel description = new JLabel("<html><p>Select the two input files.<br/>");
                
		
		JPanel labels = new JPanel();
		labels.add(description);
		
		descriptionPanel.add(labels, BorderLayout.LINE_START);
		
		return descriptionPanel;
	}
	
	private JPanel makeOpenPanel()
	{
		JPanel openPanels = new JPanel();
		openPanels.setBorder(BorderFactory.createTitledBorder("Files"));
		
		JLabel sequenceLabel = new JLabel("Sequence Data: ");
		
		openSequenceChooser = new JFileChooser(GUIManager.getInstance().getCurrentDirectory());
		openSequenceChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		openSequenceChooser.setFileFilter(new SequenceDataFilter());

		openSequenceTextField = new JTextField(20);
		openSequenceTextField.setText(openSequenceChooser.getCurrentDirectory().getAbsolutePath());
		
		JButton openSequenceButton = new JButton("Browse ...");
		openSequenceButton.setActionCommand(OPEN_SEQUENCE);
		openSequenceButton.addActionListener(this);
		
		openPanels.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		openPanels.add(sequenceLabel, "2, 2, left, default");
		
		openPanels.add(openSequenceTextField, "4, 2, fill, default");
		openSequenceTextField.setColumns(10);
		
		openPanels.add(openSequenceButton, "6, 2");
		
		return openPanels;
	}
	
	private JPanel makeOptionsPanel()
	{
		JPanel options = new JPanel();
		options.setBorder(BorderFactory.createTitledBorder("Options"));

		options.setLayout(new BorderLayout());
		
		JLabel layoutLabel = new JLabel("Layout: ");
		JLabel styleLabel = new JLabel("GView Style Sheet: ");
		JLabel gffLabel = new JLabel("GFF File: ");
		
		layoutBox = new JComboBox();
		layoutBox.setActionCommand(LAYOUT_BOX);
		layoutBox.addItem(LAYOUT_LINEAR);
		layoutBox.addItem(LAYOUT_CIRCULAR);
		layoutBox.addActionListener(this);		
		layoutBox.setSelectedItem(LAYOUT_CIRCULAR);		
		
		openStyleChooser = new JFileChooser(GUIManager.getInstance().getCurrentDirectory());
		openStyleChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		openStyleChooser.setFileFilter(new StyleDataFilter());
		
		openStyleTextField = new JTextField(20);
		
		openGFFChooser = new JFileChooser(GUIManager.getInstance().getCurrentDirectory());
		openGFFChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		openGFFChooser.setFileFilter(new GFFFilter());
				
		openGFFTextField = new JTextField(20);
		
		JButton openStyleButton = new JButton("Browse ...");
		openStyleButton.setActionCommand(OPEN_STYLE);
		openStyleButton.addActionListener(this);
		
		JButton openGFFButton = new JButton("Browse ...");
		openGFFButton.setActionCommand(OPEN_GFF);
		openGFFButton.addActionListener(this);

		options.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		options.add(styleLabel, "2, 4, left, default");
		
		openStyleTextField.setColumns(10);
		options.add(openStyleTextField, "4, 4, fill, default");
		
		options.add(openStyleButton, "6, 4");		
		options.add(layoutLabel, "2, 2, left, default");		
		options.add(layoutBox, "4, 2, 3, 1, fill, default");		
		options.add(gffLabel, "2, 6, left, default");
		
		openGFFTextField.setColumns(10);
		options.add(openGFFTextField, "4, 6, fill, default");
		
		options.add(openGFFButton, "6, 6");		
		
		return options;
	}
	
	private void performOpenAction(JFileChooser chooser, JTextField textField)
	{
		int state;
		boolean finished = false;
		
		chooser.setCurrentDirectory(GUIManager.getInstance().getCurrentDirectory());
		
		do
		{
			state = chooser.showOpenDialog(this);
			if (state == JFileChooser.APPROVE_OPTION)
			{
				File chosenFile = chooser.getSelectedFile();
				
				if (chosenFile != null && !chosenFile.exists())
				{
					JOptionPane.showMessageDialog(dialog, "File \"" + chosenFile.getAbsolutePath() + "\" does not exist.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					finished = true;
					GUIManager.getInstance().setCurrentDirectory(chooser.getCurrentDirectory());
					
					if (chosenFile != null)
					{
						textField.setText(chosenFile.getAbsolutePath());
					}
				}
			}
			else
			{
				finished = true;
			}
		} while (!finished);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (OPEN_SEQUENCE.equals(e.getActionCommand()))
		{
			performOpenAction(openSequenceChooser, openSequenceTextField);
		}
		else if (OPEN_STYLE.equals(e.getActionCommand()))
		{
			performOpenAction(openStyleChooser, openStyleTextField);
		}
		else if (OPEN_GFF.equals(e.getActionCommand()))
		{
			performOpenAction(openGFFChooser, openGFFTextField);
		}
		else if (BUILD_MAP.equals(e.getActionCommand()))
		{
			doBuild();
		}
		else if (LAYOUT_BOX.equals(e.getActionCommand()))
		{
			Object o = layoutBox.getSelectedItem();
			if (o.equals(LAYOUT_CIRCULAR))
			{
				currLayout = circularLayout;
			}
			else if (o.equals(LAYOUT_LINEAR))
			{
				currLayout = linearLayout;
			}
		}
	}
	
	private void doBuild()
	{		
		File sequenceFile = this.getSequenceFile();
		File styleFile = this.getStyleFile();
		File gffFile = this.getGFFFile();
				
		try
		{
			if (sequenceFile == null || !sequenceFile.isFile() || !GViewFileReader.canRead(sequenceFile.toURI().toURL()))
			{
				JOptionPane.showMessageDialog(dialog, "Sequence file \"" + sequenceFile + "\" is unknown." +
						"\nShould be of type Genbank, EMBL, FASTA, or CGView XML.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return;
		}
		
		if(!styleFile.isFile())
		{
			styleFile = null;
		}
		
		if (gffFile != null)
		{
			if (!gffFile.isFile() && !gffReader.canRead(gffFile.toURI()))
			{
				JOptionPane.showMessageDialog(dialog, "GFF file \"" + gffFile.getAbsolutePath() + "\" is invalid." +
						"\nShould be of type GFF.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		Thread mapBuilder = new GViewMapBuilder(sequenceFile, styleFile, gffFile, currLayout);
	
		hideOpenDialog();
		
		mapBuilder.start();
	}

	private File getSequenceFile()
	{
		File f = new File(openSequenceTextField.getText());
		
		return f;
	}
	
	private File getStyleFile()
	{
		File f = new File(openStyleTextField.getText());
		
		return f;
	}
	
	private File getGFFFile()
	{
		if (openGFFTextField.getText() == null || "".equals(openGFFTextField.getText()))
		{
			return null;
		}
		else
		{
			return new File(openGFFTextField.getText());
		}
	}
	
	private static void createInstances()
	{
		dialog = new JFrame("GView: Open Files");
		instance = new OpenDialog();
		
		dialog.getContentPane().add(instance);
		
		dialog.pack();
	}
	
	public static void showOpenDialog()
	{
		if (dialog == null)
		{
			createInstances();
		}
		
		dialog.setVisible(true);
	}
	
	public static void hideOpenDialog()
	{
		if (dialog != null)
		{
			dialog.setVisible(false);
		}
	}
	
	public static OpenDialog getInstance()
	{
		if (dialog == null)
		{
			createInstances();
		}
		
		return instance;
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		close();
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}
	
	private void close()
	{
		if (GUIManager.getInstance().getNumGUIInstances() <= 0)
		{
			System.exit(0);
		}
		else
		{
			hideOpenDialog();
		}
	}
	
	private class GViewMapBuilder extends Thread
	{
		private File sequenceFile;
		private File styleFile;
		private File gffFile;
		private LayoutFactory currLayout;
		
		public GViewMapBuilder(File sequenceFile, File styleFile, File gffFile, LayoutFactory currLayout)
		{
			this.sequenceFile = sequenceFile;
			this.styleFile = styleFile;
			this.gffFile = gffFile;
			this.currLayout = currLayout;
		}

		@Override
		public void run()
		{
			GUIManager.getInstance().getSplashScreen().showSplash(true);
			
			try
			{
				//Ready the input:
				URI sequence = null;
				URI style = null;
				URI gff = null;
				
				if(this.sequenceFile != null)
				{
					sequence = this.sequenceFile.toURI();
				}
				
				if(this.styleFile != null)
				{
					style = this.styleFile.toURI();
				}
				
				if(this.gffFile != null)
				{
					gff = this.gffFile.toURI();
				}
				
				//Create the frame:
				GViewGUIFrame guiFrame = GUIManager.getInstance().buildGViewGUIFrameFromInput(sequence, style, gff, this.currLayout);
				guiFrame.setVisible(true);			
			}
			catch (IOException e)
			{
				e.printStackTrace();
				
				close();
			}
			catch (GViewDataParseException e)
			{
				e.printStackTrace();
				
				close();
			}
			
			GUIManager.getInstance().getSplashScreen().showSplash(false);
		}
	}
}
