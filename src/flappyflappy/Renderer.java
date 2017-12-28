package flappyflappy;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel {

	private static final long serialVersionUID = -5054385015185801749L;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		FlappyBird.flappyBird.repaint(g);
	}

}
