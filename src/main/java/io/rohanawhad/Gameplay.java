package io.rohanawhad;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay  extends JPanel implements KeyListener , ActionListener {

	// Variables
	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	private Timer timer;
	private int delay = 8;
	
	private int playerX = 310;
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	
	//Protected Setters for variables
	protected void setBallposX(int value) {
		ballposX = value;
	}
	
	protected void setBallposY(int value) {
		ballposY = value;
	}
	
	protected void setBallXdir(int value) {
		ballXdir = value;
	}
	
	protected void setBallYdir(int value) {
		ballYdir = value;
	}
	
	protected void setPlayerX(int value) {
		playerX = value;
	}
	
	protected void setPlay(boolean value) {
		play = value;
	}
	
	protected void setTotalBricks(int value) {
		totalBricks = value;
		if (totalBricks == 0) play = false;
	}
	
	// Getters for variables
	public int getTotalBricks() {
		return totalBricks;
	}
	
	public int getBallposX() {
		return ballposX;
	}
	
	public int getBallposY() {
		return ballposY;
	}
	
	public int getBallXdir() {
		return ballXdir;
	}
	
	public int getBallYdir() {
		return ballYdir;
	}
	
	public int getplayerX() {
		return playerX;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean isGameOn() {
		return play;
	}
	
	
	
	private MapGenerator map;
	
	public Gameplay() {
		// TODO Auto-generated constructor stub
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		timer = new Timer(delay, this);
		timer.start();
		
		map = new MapGenerator(3, 7);
	
	}
	
	public void paint(Graphics g) {
		//bg
		g.setColor(Color.black);
		g.fillRect(1, 1, 700, 592);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 700, 3);
		g.fillRect(697, 0, 3, 592);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550,  100,  8);
		
		// ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//drawing map
		map.draw((Graphics2D) g);
		
		//score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
		
		if(isGameWon()) {
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won", 250, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter To Restart", 230, 350);
		}
		
		if (isGameLost()) {
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter To Restart", 230, 350);
		}
		
		g.dispose();
		
	}
	
	public boolean isGameWon() {
		if (totalBricks > 0) return false;
		
		play = false;
		ballXdir = 0;
		ballYdir = 0;
		
		return true;		
	}
	
	public boolean isGameLost() {
		if (ballposY < 570) return false;
		
		play = false;
		ballXdir = 0;
		ballYdir = 0;
		
		return true;		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		timer.start();
		
		if (play) {
			
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			
			A:
				for (int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						
						Rectangle bricRect = rect;
						
						if(ballRect.intersects(bricRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if (ballposX + 19 <= bricRect.x || ballposX + 1 >= bricRect.x + bricRect.width)
								ballXdir = -ballXdir;
							else 
								ballYdir = -ballYdir;
							
							break A;
							
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if (ballposX <= 0) ballXdir = -ballXdir;
			if (ballposX >= 677) ballXdir = -ballXdir;
			
			if (ballposY <= 0) ballYdir = -ballYdir;
		}
		
		repaint();
				
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveRight();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			moveLeft();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				
				playerX = 310;
				score = 0;
				totalBricks = 21;
				
				map = new MapGenerator(3,  7);
				
				repaint();
			}
		}
	}

	private void moveRight() {
		// TODO Auto-generated method stub
		play = true;
		if (playerX >= 597) playerX = 597;
		else playerX += 20;
	}

	private void moveLeft() {
		// TODO Auto-generated method stub
		play = true;
		if (playerX <= 10) playerX = 10;
		else playerX -= 20;
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	
	
}
