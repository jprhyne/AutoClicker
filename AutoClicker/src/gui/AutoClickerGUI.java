package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JComboBox;

public class AutoClickerGUI extends JFrame implements ActionListener{

	/**
	 * Button that starts the program
	 */
	private static JButton start;
	
	/**
	 * Button that quits (terminates) the program
	 */
	private static JButton quit;
	
	/**
	 * Button that pauses the program (Stops clicking)
	 */
	private static JButton pause;
	
	/**
	 * Label that displays goes over the delay textfield
	 */
	private static JLabel delayLabel;
	
	/**
	 * Label that displays goes over the repetitions textfield
	 */
	private static JLabel repetitionsLabel;

	/**
	 * Label that displays goes over the xCord textfield
	 */
	private static JLabel xCordLabel;

	/**
	 * Label that displays goes over the yCord textfield
	 */
	private static JLabel yCordLabel;
	
	/**
	 * Text field that reads in the delay between clicks in milliseconds
	 */
	private static JTextField delay;
	
	/**
	 * Text field that reads in the number of repetitions (-1 for no limit)
	 */
	private static JTextField repetitions;

	/**
	 * Text field that reads in the x coordinate to start clicking at (default is 200)
	 */
	private static JTextField xCord;

	/**
	 * Text field that reads in the y coordinate to start clicking at (default is 400)
	 */
	private static JTextField yCord;
	
	/**
	 * Robot worker that represents the mouse and does the clicking
	 */
	private static Robot clicker;
	
	/**
	 * The frame that holds all our panels to be displayed
	 */
	private static JFrame f;
	
	/**
	 * 2D array that will hold all the panels to be displayed and easier to reference
	 */
	private JPanel[][] panelHolder;

	/**
	 * String Array that holds the options for the type of clicking to be selected
	 */
	private String[] clickingTypeOptions = {"Left Click", "Middle Click", "Right Click"};

	/**
	 * Drop down box that holds the clicking type options from above
	 */
	private JComboBox<String> clickingDropDownBox = new JComboBox<>(clickingTypeOptions);

	/**
	 * Swing worker that clicks the clicker (See above)
	 */
	private static SwingWorker worker;
	
	/**
	 * Starts execution here
	 * @param args command line arguments. These are ignored
	 */
	public static void main(String[] args) {
		try {
			clicker = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.out.println("If you reach here, please open a bug report and copy the above stack trace");
			System.exit(1);
		}
		new AutoClickerGUI();

	}

	/**
	 * Class that handles all our functionality
	 */
	public AutoClickerGUI() {
		super();
		f = new JFrame();
		f.setLocation(900, 200); // Determines the starting location of the GUI
		f.setDefaultCloseOperation(EXIT_ON_CLOSE); //Makes sure that the program terminates on close
		initializeGUI(); 
		f.pack(); //Dynamically determines the size of the GUI box so that everything is readable
		f.setVisible(true);
		f.setAlwaysOnTop(true);
		
	}
	/**
	 * This method actually starts and creates the GUI that is displayed
	 */
	private void initializeGUI() {
		// Determines the number of panels in the GUI
		int rows = 2;
		int cols = 7;
		panelHolder = new JPanel[rows][cols];    
		f.setLayout(new GridLayout(rows, cols));
		// Adding the, currently, blank panels to the enclosing frame
		for(int m = 0; m < rows; m++) {
		   for(int n = 0; n < cols; n++) {
		      panelHolder[m][n] = new JPanel();
		      f.add(panelHolder[m][n]);
		   }
		}
		// Initialize the intractable buttons
		start = new JButton("Start");
		pause = new JButton("Pause");
		quit = new JButton("Quit");

		// Initialize the text fields we read from
		repetitions = new JTextField(7);
		delay = new JTextField(7);
		xCord = new JTextField(7);
		yCord = new JTextField(7);
		
		// Sets the default values. These are the values I use for cookie clicker
		delay.setText("32");
		repetitions.setText("1000");
		xCord.setText("200");
		yCord.setText("400");

		// Makes the buttons actually do something when clicked
		start.addActionListener(this);
		pause.addActionListener(this);
		quit.addActionListener(this);
		
		//now creates the labels
		repetitionsLabel = new JLabel("Repetitions");
		delayLabel = new JLabel("Delay (ms) ");
		xCordLabel = new JLabel("X Coordinate");
		yCordLabel = new JLabel("Y Coordinate");
		
		// Now, we add everything to our panel holder, which is also in our frame from the beginning of this method
		newWorker();
		panelHolder[0][2].add(clickingDropDownBox);
		panelHolder[0][3].add(repetitionsLabel);
		panelHolder[0][4].add(delayLabel);
		panelHolder[0][5].add(xCordLabel);
		panelHolder[0][6].add(yCordLabel);
		panelHolder[1][0].add(start);
		panelHolder[1][1].add(pause);
		panelHolder[1][2].add(quit);
		panelHolder[1][3].add(repetitions);
		panelHolder[1][4].add(delay);
		panelHolder[1][5].add(xCord);
		panelHolder[1][6].add(yCord);
		
		
	}
	/**
	 * Method that defines how a new worker is created
	 */
	private void newWorker() {
		worker = new SwingWorker<Void, Void>(){
			@Override
			public Void doInBackground() {
				try {
					clicker = new Robot();
				} catch (AWTException e1) {
					e1.printStackTrace();
					System.exit(1);
				}
				// Declare variables to be read from the text fields
				int delayNum = 0;
				int xCordNum = 0;
				int yCordNum = 0;
				long max = 0;
				// Get the time between clicks, max number of clicks, and the coordinates of the mouse from the GUI panels
				try {
					delayNum = Integer.parseInt(delay.getText());
					max = Integer.parseInt(repetitions.getText());
					xCordNum = Integer.parseInt(xCord.getText());
					yCordNum = Integer.parseInt(yCord.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Only enter Integer values", "Error", JOptionPane.ERROR_MESSAGE);
					newWorker();
				}
				// Get the type of mouse click the user wants
				String selectedValue = clickingDropDownBox.getItemAt(clickingDropDownBox.getSelectedIndex());
				int clickMask = 0;
				switch (selectedValue) {
					case "Left Click":
						clickMask = InputEvent.BUTTON1_DOWN_MASK;
						break;
					case "Middle Click":
						clickMask = InputEvent.BUTTON2_DOWN_MASK;
						break;
					case "Right Click":
						clickMask = InputEvent.BUTTON3_DOWN_MASK;
						break;
				}

				// If the user inputs a negative number, we assume they want to do this functionally forever. So we make it be Long.MAX_VALUE
				// which is 2^{63} - 1 (A very big number)
				if (max < 0)
					max = Long.MAX_VALUE;
				int counter = 0;
				// Puts mouse in the determined spot
				clicker.mouseMove(xCordNum, yCordNum);
				// Edge case mainly for testing mouse placement
				if (max == 0) {
					this.cancel(true);
				}
				// Loop until the mouse is clicked ```max``` number of times
				while (!isCancelled()) {
					try {
						Thread.sleep(delayNum); // Sleep for delayNum milliseconds
						clicker.mousePress(clickMask);	// Click down
						clicker.mouseRelease(clickMask); // Release button
						// Cancel operation once we click ```max``` number of times. 
						if (counter >= max) 
							this.cancel(true);
					} catch (InterruptedException e) {
							//do nothing
					}
					counter++;
				}
				return null;
			}
		
		};
	}
	
	/**
	 * Determines what happens when we click each of the three buttons on the GUI
	 */
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == pause) {
			// Pause execution (stop clicking but do not close) Note: This will reset the click counter
			worker.cancel(true);
		} else if (a.getSource() == quit) {	
			// Terminate execution after stopping the clicking
			worker.cancel(true);
			System.exit(0);
		} else if (a.getSource() == start) {
			// Starts execution of the clicking
			// If the worker is canceled, then we need to create a new worker
			// otherwise, we just restart execution
			if (worker.isCancelled()) {
				newWorker();
			}
			worker.execute();
		}
		
	}
}


