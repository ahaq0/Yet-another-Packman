import javax.swing.*;

/**
 * The purpose of this class is to provide a frame upon which the 
 * game will be made via a Jpanel added
 * @author Ammar Haq
 *
 */
public class TheFrame  
{

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setTitle("Packman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new GamePanel());
		frame.pack();
		frame.setVisible(true);
	}
}
