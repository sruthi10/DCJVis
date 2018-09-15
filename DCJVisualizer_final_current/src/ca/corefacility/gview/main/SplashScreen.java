package ca.corefacility.gview.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import ca.corefacility.gview.map.gui.ProgressNotifier;
import ca.corefacility.gview.utils.GViewInformation;
import ca.corefacility.gview.utils.ProgressHandler;

public class SplashScreen implements ProgressNotifier
{
	private JFrame splashWindow = null;
	private BufferedImage image;
	
	private JProgressBar progressBar;
	
	public SplashScreen(BufferedImage image)
	{
		this.image = image;
	}
	
	private JProgressBar createProgressBar()
	{
		JProgressBar bar = new JProgressBar(0, 100)
		{
            private static final long serialVersionUID = 1L;

            @Override
			public void paint(Graphics g)
			{
				Graphics2D g2 = (Graphics2D)g;
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				super.paint(g);
			}
		};
		bar.setValue(0);
		bar.setIndeterminate(false);
		bar.setStringPainted(true);
		bar.setString("Loading ...");
		
		return bar;
	}
	
	private JPanel createBackgroundPanel(BufferedImage image, int width, int height)
	{
		JPanel backgroundPanel = new JPanel();
		
		if (image != null)
		{
			ImageIcon icon = new ImageIcon(image);
			
			backgroundPanel.add(new JLabel(icon), BorderLayout.CENTER);
		}
		
		backgroundPanel.setBackground(Color.white);
		backgroundPanel.setBounds(0,0,width,height);

		return backgroundPanel;
	}
	
	private JFrame buildSplash()
	{		
		JFrame window = new JFrame();
		window.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closing gView!");
				System.exit(0);					
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("Closing gView!");
				System.exit(0);				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		JPanel content = (JPanel)window.getContentPane();
				
		JLabel title = new AntaliasJLabel("GView", JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 26));
		
		String versionStr = (GViewInformation.instance().getVersion() != null) ? "v " + GViewInformation.instance().getVersion() : null;
		JLabel version = null;
		if (versionStr != null && !versionStr.equals(""))
		{
			version = new AntaliasJLabel(versionStr, JLabel.CENTER);
			version.setFont(new Font("SansSerif", Font.ITALIC, 12));
		}
		
		JPanel text = new JPanel();

		if (version != null)
		{
			text.setLayout(new GridLayout(2,1));
			text.setBackground(Color.white);
			text.add(title);
			text.add(version);
		}
		else
		{
			text.setLayout(new GridLayout(1,1));
			text.setBackground(Color.white);
			text.add(title);
		}
		
		JProgressBar bar = createProgressBar();
		this.progressBar = bar;
		
		JPanel progressBarPanel = new JPanel();
		progressBarPanel.setLayout(new GridLayout(1,1));
		progressBarPanel.setBorder(new LineBorder(Color.white, 5));
		bar.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		progressBarPanel.add(bar);
		content.add(progressBarPanel, BorderLayout.SOUTH);
				
		// get screen size for displaying splash screen
		// assumes possibility of more than one display
		int screenWidth,screenHeight;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (gd == null || gd.length == 0)
		{
			System.err.println("[warning] - could not get screen resolution");
			screenWidth = 800;
			screenHeight = 600;
		}
		else
		{
			DisplayMode d = gd[0].getDisplayMode();
			
			if(d != null)
			{
				screenWidth = d.getWidth();
				screenHeight = d.getHeight();	
			}
			else
			{
				System.err.println("[warning] - could not get screen resolution");
				screenWidth = 800;
				screenHeight = 600;
			}					
		}
		
		int width, height;
		
		if (image == null)
		{
			width = 400; height = 400;
		}
		else
		{
			width = image.getWidth() + 150;
			height = image.getHeight() + 150;
		}
		
		int x = (screenWidth-width)/2;
		int y = (screenHeight-height)/2;
		window.setBounds(x,y,width,height);
		
		JPanel backgroundPanel = createBackgroundPanel(image, width, height);

		window.setBackground(Color.white);
		content.setBackground(Color.white);
		content.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		
		content.add(backgroundPanel, BorderLayout.CENTER);
		content.add(text, BorderLayout.NORTH);
		
		ProgressHandler.initialize(this);
		
		return window;
	}
	
	private class AntaliasJLabel extends JLabel
	{
        private static final long serialVersionUID = 1L;

        public AntaliasJLabel()
		{
			super();
		}

		public AntaliasJLabel(Icon image, int horizontalAlignment)
		{
			super(image, horizontalAlignment);
		}

		public AntaliasJLabel(Icon image)
		{
			super(image);
		}

		public AntaliasJLabel(String text, Icon icon, int horizontalAlignment)
		{
			super(text, icon, horizontalAlignment);
		}

		public AntaliasJLabel(String text, int horizontalAlignment)
		{
			super(text, horizontalAlignment);
		}

		public AntaliasJLabel(String text)
		{
			super(text);
		}

		@Override
		public void paint(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paint(g);
		}
	}
	
	private class BackgroundPanel extends JPanel
	{
		private BufferedImage image;
		
		public BackgroundPanel(BufferedImage image)
		{
			this.image = image;
		}
		
		public void paint(Graphics g)
		{
			super.paint(g);
			
			int width = getWidth();
			int height = getHeight();
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			int x = (width-imageWidth)/2;
			int y = (height-imageHeight)/2;
			x = (x < 0) ? 0 : x;
			y = (y < 0) ? 0 : y;
			
			g.drawImage(image, x, y, null);
		}
	}
	
	public void showSplash(boolean show)
	{
		if (show)
		{
			if (splashWindow == null)
			{
				splashWindow = buildSplash();
				splashWindow.setVisible(true);
			}
		}
		else
		{
			if (splashWindow != null)
			{
				splashWindow.setVisible(false);
				splashWindow = null;
			}
		}
	}
	
	public boolean isVisible()
	{
		return splashWindow != null && splashWindow.isVisible();
	}
	
	public void setMessage(String message)
	{
		progressBar.setString(message);
	}
	
	public void setProgress(int progress)
	{
		progressBar.setValue(progress);
	}
}
