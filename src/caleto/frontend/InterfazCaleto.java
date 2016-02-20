package caleto.frontend;

import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import caleto.logic.Caleto;

import com.apple.eawt.Application;

public class InterfazCaleto extends JPanel implements ActionListener
{
	/**
	 * Default generation serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The backend connection
	 */
	private static Caleto caleto;
	
	/**
	 * Button to load image
	 */
	protected JButton btnLoadImage;
	
	/**
	 * Button to select file to be hidden
	 */
	protected JButton btnFileToBeHidden;
	
	/**
	 * Button to recover a hidden file
	 */
	protected JButton btnRecoverHidden;
	
	/**
	 * Button to hide the file
	 */
	protected JButton btnHideFile;
	
	/**
	 * Button to select the image to recover the file from
	 */
	protected JButton btnLoadRecoverImage;
	
	/**
	 * Image path
	 */
	private JLabel imagePath;
	
	/**
	 * Hide file path
	 */
	private JLabel hideFilePath;
	
	/**
	 * Recovery image path
	 */
	private JLabel recoveryImagePath;
	
	/**
     * Creates the panel to load the image and do the magic
     */
    public InterfazCaleto()
    {
    	Application application = Application.getApplication();
    	Image image = Toolkit.getDefaultToolkit().getImage("icon.png");
    	application.setDockIconImage(image);
        
    	// Add the button to load the image
	    btnLoadImage = new JButton("Load Image");
	    btnLoadImage.setActionCommand("loadImage");
	    btnLoadImage.addActionListener(this);
	    
	    // Add the button to select the file to be hidden
	    btnFileToBeHidden = new JButton("Select file to be hidden");
	    btnFileToBeHidden.setActionCommand("loadFileToBeHidden");
	    btnFileToBeHidden.addActionListener(this);
	    
	    // Add the button to recover a hidden file
	    btnRecoverHidden = new JButton("Recover hidden file");
	    btnRecoverHidden.setActionCommand("recoverHiddenFile");
	    btnRecoverHidden.addActionListener(this);
	    
	    // Add the button to do the hidding!
	    btnHideFile = new JButton("Hide File");
	    btnHideFile.setActionCommand("hideFile");
	    btnHideFile.addActionListener(this);
	    
	    // Add the button to select the image to recover the file from
	    btnLoadRecoverImage = new JButton("Select recovery image");
	    btnLoadRecoverImage.setActionCommand("loadRecoveryImage");
	    btnLoadRecoverImage.addActionListener(this);
	    
	    // Create our tabbed pane
	    JTabbedPane tabbedPane = new JTabbedPane();
	    
	    // Create the icons
	    ImageIcon hideIcon = new ImageIcon( InterfazCaleto.class.getResource("/images/hide.png") );
	    ImageIcon recoverIcon = new ImageIcon( InterfazCaleto.class.getResource("/images/recover.png") );
	    ImageIcon imageIcon = new ImageIcon( InterfazCaleto.class.getResource("/images/image.png") );
	    ImageIcon fileIcon = new ImageIcon( InterfazCaleto.class.getResource("/images/file.png") );
        
        // Set the button image
        btnHideFile.setIcon(hideIcon);
        btnRecoverHidden.setIcon(recoverIcon);
        btnLoadRecoverImage.setIcon(imageIcon);
        btnLoadImage.setIcon(imageIcon);
        btnFileToBeHidden.setIcon(fileIcon);
        
        // Now our first panel
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(3, 2));
        
        // Add the choosed image
        imagePath = new JLabel("Image: No image has been choosed");
        panel.add(imagePath);
        panel.add(btnLoadImage);
        
        // Add the file to be hidden
        hideFilePath = new JLabel("File to hide: No file has been choosed");
        panel.add(hideFilePath);
        panel.add(btnFileToBeHidden);
        
        // Select the button!
        panel.add(new JLabel(""));
        panel.add(btnHideFile);
        		
        // Now our first tab
        tabbedPane.addTab("Hide", hideIcon, panel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        // Second panel
        JPanel panel2 = new JPanel(false);
        panel2.setLayout(new GridLayout(2, 2));
        
        // Add the choosed image
        recoveryImagePath = new JLabel("Image: No image has been choosed");
        panel2.add(recoveryImagePath);
        panel2.add(btnLoadRecoverImage);
        
        // Select the button!
        panel2.add(new JLabel(""));
        panel2.add(btnRecoverHidden);
         
        // Add our second tab
        tabbedPane.addTab("Recover", recoverIcon, panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
         
        //Add the tabbed pane to this panel.
        add(tabbedPane);
         
        // Scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    /**
     * Creates the panel to load the image and do the magic
     */
    private static void createFrontend()
    {
    	// Set our caleto connection!
    	caleto = new Caleto();
    	
    	// Set some look and feel
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    	
    	// Set dock for Mac
        if ( classExists( "com.apple.eawt.Application" ) )
        {
        	com.apple.eawt.Application.getApplication().setDockIconImage( new ImageIcon( InterfazCaleto.class.getResource("/images/icon_512x512.png") ).getImage() );
        }
    	
    	//Create and set up the window.
        JFrame frame = new JFrame("Caleto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        InterfazCaleto newContentPane = new InterfazCaleto();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * It handles the action according to the click made.
     */
    public void actionPerformed(ActionEvent e)
    {
    	/*
    	 * It loads our file chooser to select our image
    	 */
        if (e.getActionCommand().equals("loadImage"))
        {
        	// Open the dialog
        	FileDialog fd = new FileDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Choose a file", FileDialog.LOAD);
        	fd.setFile("*.bmp");
        	fd.setVisible(true);
        	
        	// We get the image!
        	if (fd.getFile() != null)
        	{
        		// It's an image?
        		if (!fd.getFile().endsWith(".bmp"))
        		{
        			JOptionPane.showMessageDialog(
        					InterfazCaleto.this, 
        					"You must select a BMP file", 
        					"Wrong file type", 
        					JOptionPane.ERROR_MESSAGE
        			);
        		}
        		else
        		{
        			// Set the image
	        		caleto.setImageFile( new File(fd.getDirectory() + "/" + fd.getFile()) );
	        		
	        		// Trim it!
	        		String fileName = fd.getFile();
	        		
	        		if (fileName.length() > 20)
	        		{
	        			fileName = fileName.substring(0, 20);
	        		}
	        		
	        		// Set the file's name
	                imagePath.setText( "Image: " + fileName );
        		}
        	}
        }
        /*
    	 * It loads our file chooser to select file to be hidden!
    	 */
        else if (e.getActionCommand().equals("loadFileToBeHidden"))
        {
        	// Open the dialog
        	FileDialog fd = new FileDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Choose a file", FileDialog.LOAD);
        	fd.setVisible(true);
        	
        	// We get the image!
        	if (fd.getFile() != null)
        	{
        		caleto.setFileToBeHidden( new File(fd.getDirectory() + "/" + fd.getFile()) );
        		
        		// Trim it!
        		String fileName = fd.getFile();
        		
        		if (fileName.length() > 20)
        		{
        			fileName = fileName.substring(0, 20);
        		}
        		
        		hideFilePath.setText( "File to hide: " + fileName );
        	}
        }
        /*
    	 * It loads our file chooser to select recovery image
    	 */
        else if (e.getActionCommand().equals("loadRecoveryImage"))
        {
        	// Open the dialog
        	FileDialog fd = new FileDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Choose a file", FileDialog.LOAD);
        	fd.setFile("*.bmp");
        	fd.setVisible(true);
        	
        	// We get the image!
        	if (fd.getFile() != null)
        	{
        		// It's an image?
        		if (!fd.getFile().endsWith(".bmp"))
        		{
        			JOptionPane.showMessageDialog(
        					InterfazCaleto.this, 
        					"You must select a BMP file", 
        					"Wrong file type", 
        					JOptionPane.ERROR_MESSAGE
        			);
        		}
        		else
        		{
        			caleto.setRecoveryImageFile( new File(fd.getDirectory() + "/" + fd.getFile()) );
        			
        			// Trim it!
            		String fileName = fd.getFile();
            		
            		if (fileName.length() > 20)
            		{
            			fileName = fileName.substring(0, 20);
            		}
        			
            		recoveryImagePath.setText( "Image: " + fileName );
        		}
        	}
        }
        /*
    	 * It recovers a hidden file
    	 */
	    else if (e.getActionCommand().equals("recoverHiddenFile"))
	    {
	    	// Open the dialog
        	FileDialog fd = new FileDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Save file", FileDialog.SAVE);
        	fd.setVisible(true);
        	
        	// We get the image!
        	if (fd.getFile() != null)
        	{
        		JOptionPane.showMessageDialog(InterfazCaleto.this, 
            		caleto.recoverHiddenFile( new File(fd.getDirectory() + "/" + fd.getFile()) )
            	);
        		
        		caleto.setRecoveryImageFile(null);
        		recoveryImagePath.setText( "Image: No image has been choosed" );
        	}
	    }
        /*
    	 * It hides a file
    	 */
        else if (e.getActionCommand().equals("hideFile"))
        {
        	// Open the dialog
        	FileDialog fd = new FileDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Save file", FileDialog.SAVE);
        	fd.setFile("*.bmp");
        	fd.setVisible(true);
        	
        	// We get the image!
        	if (fd.getFile() != null)
        	{
        		JOptionPane.showMessageDialog(InterfazCaleto.this, 
            		caleto.hideFile( new File(fd.getDirectory() + "/" + fd.getFile()) )
            	);
            	
            	caleto.setFileToBeHidden(null);
            	caleto.setImageFile(null);
            	hideFilePath.setText("File to hide: No file has been choosed");
            	imagePath.setText("Image: No image has been choosed");
        	}
        }
    }
    
    /**
     * See if a class exists
     */
    public static boolean classExists(String className)
    {
        try {
            Class.forName( className, false, null );
            return true;
        }
        catch (ClassNotFoundException exception) {
            return false;
        }
    }
    
    /**
     * Creates and shows our GUI
     */
    public static void main(String[] args)
    {
    	System.setProperty("apple.laf.useScreenMenuBar", "true");
    	System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Caleto");
    	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	createFrontend();
            }
        });
    }
}
