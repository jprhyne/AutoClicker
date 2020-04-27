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

public class AutoClickerGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static JButton start;
	
	private static JButton quit;
	
	static JButton pause;
	
	private static JLabel delayLabel;
	
	private static JLabel repetitionsLabel;
	
	private static JTextField delay;
	
	private static JTextField repetitions;
	
	private static Robot clicker;
	
	private static JFrame f;
	
	JPanel[][] panelHolder;
	
	
	@SuppressWarnings("rawtypes")
	static SwingWorker worker;
	

	public static void main(String[] args) {
		try {
			clicker = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new AutoClickerGUI();

	}
	public AutoClickerGUI() {
		super();
		f = new JFrame();
		f.setSize(500, 100);
		f.setLocation(900, 200);
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
		initializeGUI();
		f.setVisible(true);
		f.setAlwaysOnTop(true);
		
	}
	private void initializeGUI() {
		int i = 2;
		int j = 5;
		panelHolder = new JPanel[i][j];    
		f.setLayout(new GridLayout(i,j));

		for(int m = 0; m < i; m++) {
		   for(int n = 0; n < j; n++) {
		      panelHolder[m][n] = new JPanel();
		      f.add(panelHolder[m][n]);
		   }
		}
		//makes the interactible buttons and text fields
		start = new JButton("Start");
		pause = new JButton("Pause");
		quit = new JButton("Quit");
		repetitions = new JTextField(7);
		delay = new JTextField(7);
		
		delay.setText("32");
		repetitions.setText("1000");
		start.addActionListener(this);
		
		pause.addActionListener(this);
		quit.addActionListener(this);
		
		//now creates the labels
		repetitionsLabel = new JLabel("Repetitions");
		delayLabel = new JLabel("Delay (ms) ");
		
		//adds everything to the 2 panels, and then to the display
		newWorker();
		panelHolder[0][3].add(repetitionsLabel);
		panelHolder[0][4].add(delayLabel);
		panelHolder[1][0].add(start);
		panelHolder[1][1].add(pause);
		panelHolder[1][2].add(quit);
		panelHolder[1][3].add(repetitions);
		panelHolder[1][4].add(delay);
		
		
	}
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
				int delayNum = 0;
				long max = 0;
				try {
					delayNum = Integer.parseInt(delay.getText());
					max = Integer.parseInt(repetitions.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Only enter Integer values", "Error", JOptionPane.ERROR_MESSAGE);
					newWorker();
				}
				if (max < 0)
					max = Long.MAX_VALUE;
				int counter = 0;
				clicker.mouseMove(200, 400);
				while (!isCancelled()) {
					try {
						Thread.sleep(delayNum);
						clicker.mousePress( InputEvent.BUTTON1_DOWN_MASK);
						clicker.mouseRelease( InputEvent.BUTTON1_DOWN_MASK);
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
	
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == pause) {
			worker.cancel(true);
		} else if (a.getSource() == quit) {	
			worker.cancel(true);
			System.exit(0);
		} else if (a.getSource() == start) {
			if (worker.isCancelled()) {
				newWorker();
			}
			worker.execute();
		}
		
	}
}


