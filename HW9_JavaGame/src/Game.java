import java.awt.*;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class Game implements Runnable {
	private GameCanvas court;
	private JLabel stats;
	private JLabel status;
	private JFrame frame;
	
	@Override
	public void run() {
        frame = new JFrame("EVIL DINOS");
        frame.setLocation(500, 200);
        frame.setPreferredSize(new Dimension(500,400));
        
        stats = new JLabel("Level: 0  Score: 0  Killcount: 0");
        status = new JLabel("Running...");
        court = new GameCanvas(stats, status);
        
        final JButton new_game = new JButton("New Game");
        new_game.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
            	frame.setVisible(false);
                runGame();
                court.reset();
            }
        });
        
        final JButton load_game = new JButton("Load Last Save");
        load_game.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		BufferedReader reader;
				try {
					reader = new BufferedReader(new FileReader("files/saves.txt"));
	        		if(reader.readLine() != null) {
		        		frame.setVisible(false);
		        		runGame();
		        		court.load();
	        		}
				} catch (IOException e1) {
					System.out.println("Error trying to load game in menu");
					e1.printStackTrace();
				}
        	}
        });
        
        GridLayout l = new GridLayout(1,2);
        l.setHgap(20);
        final JPanel menu = new JPanel(l);
        menu.add(new_game);
        menu.add(load_game);
        
        frame.add(menu, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
	}
	
	public void runGame() {
        frame = new JFrame("EVIL DINOS");
        frame.setLocation(500, 200);

        // Status panel
        final JPanel status_panel = new JPanel(new GridBagLayout());
        frame.add(status_panel, BorderLayout.SOUTH);
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.CENTER;
        status_panel.add(stats, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.fill = GridBagConstraints.CENTER;
        status_panel.add(status, gc);
        

        // Main playing area
        gc.gridx = 0;
        gc.gridy = 2;
        gc.fill = GridBagConstraints.CENTER;
        final JLabel records = court.getRecords();
        status_panel.add(records,gc);
        
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);
        
        final JButton save = new JButton("Save and Exit Game");
        control_panel.add(save);
        save.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if(court.isPlaying()) {
        			court.save();
        			frame.dispose();
        		}
        	}
        });
        
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

	}
	
	   public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Game());
	    }
}
