import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.*;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.event.MouseInputAdapter;

import java.util.Random;


/**
 * The purpose of this class is to provide a primary panel
 * upon which the entire game is built on.
 * @author Ammar Haq
 *
 */
public class GamePanel extends JPanel {
	// Just the mandatory Icons we were supposed to declare.
	private ImageIcon pack;
	private ImageIcon ghost;
	private ImageIcon pell;
	private Point ghostsPoint;
	private Point random;
	private Timer onlyT;
	private Point mouseHere;
	private Point pellPoint;
	
	// Some strange variables
	private boolean possibleToLose;
	private int counter;
	private Rectangle packGuard;
	private Rectangle ghostGuard;
	private Rectangle pelletGuard;
	private boolean hasEntered;
	
	public GamePanel()
	{
		onlyT = new Timer(500, new Actions());
		onlyT.start();
		setIconsAndPointsStart();
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(1000, 1000));
		setFocusable(true);
		this.addKeyListener(new Keys());
		this.addMouseListener(new Mouse());
		this.addMouseMotionListener(new Mouse());
	}
	
    @Override
    /**
     * The purpose of this method is paint out all of the icons currently
     @param	Graphics g g is a objects from the Graphics class needed to paint things.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int pellX = (int) random.getX();
        int pellY = (int) random.getY();
        pellPoint.setLocation(pellX, pellY);
        pelletGuard.setLocation(pellPoint);
        pell.paintIcon(this, g, (int)pellPoint.getX(), (int)pellPoint.getY());
        ghost.paintIcon(this, g, (int)ghostsPoint.getX(), (int)ghostsPoint.getY());
        g2.setColor(Color.white);
        System.out.print(counter);
        if (hasEntered == true) //If the mouse is in inside
        {
        	int locationX = (int) mouseHere.getX() - 50;
        	int locationY = (int) mouseHere.getY() - 50;
        	packGuard.setBounds(locationX, locationY, 98, 105); // Values correspond to size of the image and hence whether it will intersect
        	pack.paintIcon(this, g, locationX, locationY); // Seems centered enough imo
        }
    }

    /**
     * The purpose of this method is to initialize the given Icons and points
     */
    public void setIconsAndPointsStart()
    {
    	pell = new ImageIcon("pellet.png");
    	hasEntered = false;
		// I did 990 since the pellet is kinda big and don't want it hitting the edge
		random = new Point (new Random().nextInt(880) + 1, new Random().nextInt(880) + 1); //Values are so it doesn't overlap and it stays in bounds
		ghostsPoint = new Point(500,500);
		pellPoint = new Point();
		
		pack = new ImageIcon("pacman.png");
		ghost = new ImageIcon("inky.png");
		packGuard = new Rectangle();
		
		ghostGuard = new Rectangle();
		ghostGuard.setBounds((int)ghostsPoint.getX(), (int)ghostsPoint.getY(), 86, 105);
		
		pelletGuard = new Rectangle();
		pelletGuard.setBounds(0,0,57,62); // Disclaimer I got all sizes from MS paint of the images.
		possibleToLose = true;
		counter = 0;
    }
    
    /**
     * This method is just used to set the conditions needed after the Pacman intersects with the pellet.
     */
    void intersectCondition()
    {
    	possibleToLose = false;
    	pell = new ImageIcon();
		ghost = new ImageIcon("blue.png");
		counter++;
    }
    
    /**
     * THe purpose of this method is to make the needed changes if the Pacman fails to get the kill after getting the Pellet
     */
    void livesAnotherDay()
    {
    	possibleToLose = true;
		random = new Point (new Random().nextInt(880) + 1, new Random().nextInt(880) + 1);
		pell = new ImageIcon("pellet.png");
		ghost = new ImageIcon("blinky.png");
		counter = 0;
    }
    
    /**
     * The purpose of this inner class is to provide Timer functionality and act as a Timer Listener
     * functionality to the Pacman game
     * @author Ammar H
     *
     */
    public class Actions implements ActionListener
    {
		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			int xLoc = new Random().nextInt(880) + 1; // 900 is a little arbitrary but it makes sure they fit nice.
			int yLoc = new Random().nextInt(880) + 1;
			if (possibleToLose == false)
			{
				counter++;
			}
			// Change the location of the Ghost and it's rectangle every second.
			ghostsPoint.setLocation(xLoc, yLoc);
			ghostGuard.setLocation(xLoc, yLoc);
			
			if (possibleToLose == true && (ghostGuard.intersects(packGuard) || packGuard.intersects(ghostGuard)))
			{
				onlyT.stop();
		        JOptionPane.showMessageDialog(null, "Well you lost the game....", "Verdict: " + "Grave sadness", JOptionPane.INFORMATION_MESSAGE);		
			}
			if (possibleToLose == false && (ghostGuard.intersects(packGuard) || packGuard.intersects(ghostGuard)))
			{
				onlyT.stop();
		        JOptionPane.showMessageDialog(null, "Well you won the game....", "BBC news breaking news " + "You won an actual lottery", JOptionPane.INFORMATION_MESSAGE);		

			}
			if (pelletGuard.intersects(packGuard) || packGuard.intersects(pelletGuard))
			{
				
				intersectCondition();
			}
			if (counter == 10) // After 10 seconds or 10 * .5 (what it refreshes at)
			{
				livesAnotherDay();
			}
			repaint();	
		}
    }
    /**
     * The purpose of this class is to provide Key Listening functionality 
     * to the class. I could break this up in a separate class file but it would be more lines of code I think. Should've used a lambda expression.
     * @author Ammar H     *
     */
    public class Keys implements KeyListener
    {

		@Override
		// This method provides the ability to read a keypress
		// It is non java doc hence the weird comment type
		public void keyPressed(KeyEvent keypress) {
			switch (keypress.getKeyCode())
			{
				case KeyEvent.VK_P:
				{
					ghost = new ImageIcon("pinky.png");
					break;
				}
				
				case KeyEvent.VK_B:
				{
					ghost = new ImageIcon("blinky.png");
					break;
				}
				case KeyEvent.VK_I:
				{
					ghost = new ImageIcon("inky.png");
					break;
				}
				case KeyEvent.VK_C:
				{
					ghost = new ImageIcon("clyde.png");
					break;
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override
		public void keyTyped(KeyEvent arg0) {}
    	
    }
    /**
     * The purpose of this inner class is to provide the game
     * with thing like the location of Pacman and his current whereabouts
     * @author Ammar haq
     *
     */
      public class Mouse extends MouseInputAdapter
    {
		@Override
		public void mouseEntered(MouseEvent loc) 
		{
			
			hasEntered = true;		// Just to avoid the error of it happening first
			mouseHere = loc.getPoint();
		}
		
		@Override
		public void mouseMoved(MouseEvent locs) 
		{
			mouseHere = locs.getPoint();
		}
    	
    }
}
