package ipump;

import javax.swing.*;

/**
 * This class serves to prepare the drawing window.
 */
public class Window extends JFrame {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * 
     * @param controller Blood sugar level monitor
     * @param Controls connected to the screen
     */
    public Window(Controller controller, Input contr) {
        // The window with the title Insulinska Pump is made
        JFrame f = new JFrame("Insulin-Pump");
        // Use the appearance of the windows from the operating system
        JFrame.setDefaultLookAndFeelDecorated(false);
        // Sets the window size
        f.setSize(960, 540);
        //The program exits when the window is closed
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // The window can not be resized
        f.setResizable(false);
        // Output class will work on the screen
        f.setContentPane(new Output(controller));
        // The window is visible
        f.setVisible(true);

     // Bind the window controls
        f.addKeyListener(contr);
    }
}