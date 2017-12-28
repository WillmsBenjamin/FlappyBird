package flappyflappy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	
	public static FlappyBird flappyBird;
	
	public final int WIDTH = 1000, HEIGHT = 800, SPEED = 5;
	
	public Renderer renderer;

	public Rectangle bird;
	public ArrayList<Rectangle> columns;
	public int ticks, ymotion, score;
	public boolean gameOver = false, started = false;
	
	public Random rand;

	public FlappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		
		renderer = new Renderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setResizable(false);
		jframe.setTitle("Flappy Bird");
		jframe.setVisible(true);
		
		bird = new Rectangle((WIDTH/2) - 10, (HEIGHT/2) - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		
		if (start) {
			columns.add(new Rectangle(WIDTH + columns.size() * 200, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + (columns.size() - 1) * 200, 0, width, HEIGHT - height - space));
		} else {
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 400, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);
		g.fillRect(0, HEIGHT-120, WIDTH, 120);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		g.setColor(Color.red);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 1, 100));
		
		if(!started) {
			g.drawString("Click to start!", 200, HEIGHT/2 - 50);
		}
		if(gameOver) {
			g.drawString("Game Over!", 200, HEIGHT/2 - 50);
			g.drawString("Your Score: " + score, 175, HEIGHT/2 + 100);
		}
		if(started && !gameOver) {
			g.setFont(new Font("Arial", 1, 80));
			g.drawString("Score: " + score, 50, HEIGHT - 40);
		}
	}

	public void jump() {
		if(gameOver) {
			gameOver = false;
			started = false;
			
			bird = new Rectangle((WIDTH/2) - 10, (HEIGHT/2) - 10, 20, 20);
			columns.clear();
			ymotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
		} else if(!started) {
			started = true;
		} else if(!gameOver) {
			if(ymotion > 0) {
				ymotion = 0;
			}
			
			ymotion -= 10;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ticks++;
		
		if (started && gameOver == false) {
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= SPEED;
			}
			
			if (ticks % 2 == 0 && ymotion < 15) {
				ymotion += 2;
			}
			
			for (int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				
				if (column.x + column.width < 0) {
					columns.remove(column);
					if (column.y == 0) {
						addColumn(false);
					}
				}
			}
			
			bird.y += ymotion;
			
			for (Rectangle column : columns) {
				if(bird.x > column.x + column.width - SPEED
						&& bird.x < column.x + column.width + SPEED
							&& column.y == 0) {
					score++;
				}
				
				if (column.intersects(bird)) {
					gameOver = true;
				}
			}
			
			if (bird.y + ymotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
				gameOver = true;
			} else if (bird.y + ymotion <= 0) {
				bird.y = 0;
				gameOver = true;
			}
		}
		renderer.repaint();
	}

	public static void main(String[] args) {
		flappyBird = new FlappyBird();
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		jump();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			jump();
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
