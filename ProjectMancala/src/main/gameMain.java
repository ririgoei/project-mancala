package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * This is the gameMain, where we'll do our game loop and all of our execution using the other classes.
 * 
 * We've currently got these classes:
 * 
 * ArrayMethod: For utilizing Arrays for game logic.
 * LinkList: For utilizing Link lists for game logic.
 * GameBoard: Where we'll be doing things involving the game board (possibly drawing it).
 * 
 * I'm thinking I'll create a Window/JavaApp class to possibly build the window where we'll be hosting our game.
 * 
 * For reference, I'm looking at Game Loop material on this site to help out:
 * 
 * http://www3.ntu.edu.sg/home/ehchua/programming/java/J8d_Game_Framework.html
 * 
 */



	@SuppressWarnings("serial")
public class gameMain extends JPanel implements MouseListener, KeyListener
{
		GameBoard Game = new GameBoard(true);
		private boolean game_Running;
		int input;

		//Builds the window YAY!
		public static void main(String[] args)
		{
			
			JFrame jf = new JFrame("Mancala");
			gameMain gm = new gameMain();
			jf.setSize(640, 480);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.add(gm);
			jf.setVisible(true);
			gm.requestFocus();
			
		}
		
		public void paint(Graphics g)
		{
			drawBoard(g);
		}
		
		//Place holder board.
		public void drawBoard(Graphics g)
		{
			g.drawLine(0, 0, 0 + 600, 0);
			g.drawLine(0, 0, 0, 200);
			g.drawLine(600, 0, 600, 200);
			g.drawLine(0, 200, 600, 200);
			g.setColor(Color.blue);
			//Two pools. Top - Player 2, Bottom - Player 1
			g.fillOval(5, 30, 75, 125);
			g.fillOval(520, 30, 75, 125);
			//First player pots.
			g.fillOval(75, 10, 75, 75);
			g.fillOval(150, 10, 75, 75);
			g.fillOval(225, 10, 75, 75);
			g.fillOval(300, 10, 75, 75);
			g.fillOval(375, 10, 75, 75);
			g.fillOval(450, 10, 75, 75);
			//Second player pots.
			g.fillOval(75, 110, 75, 75);
			g.fillOval(150, 110, 75, 75);
			g.fillOval(225, 110, 75, 75);
			g.fillOval(300, 110, 75, 75);
			g.fillOval(375, 110, 75, 75);
			g.fillOval(450, 110, 75, 75);
			g.setColor(Color.black);
			
			
			
			
		}
		
		public gameMain()
		{
			@SuppressWarnings("unused")
			long now, then = System.currentTimeMillis(), passed;
			while(game_Running)
			{
				
				now = System.currentTimeMillis();
				passed = now - then;
				then = now;
				
				//Update.
				update((int)(passed));
				
				//Draw function from paint component.
				repaint();
				//Throttle code in a Try/Catch.
				try{Thread.sleep(100);}catch(Exception e){}
				
				//Input, going to be using mouse, working with that soon.
			}
			
			
			
		}
		

		Point p = null;
		
		//All of the input functions. Keyboard input, and Mouse input.
		public void keyPressed(KeyEvent arg0) {
			input = arg0.getKeyCode();
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			System.out.println(e.getX()+" "+ e.getY());
			p = e.getPoint();
			repaint();
		}
		public void mouseReleased(MouseEvent e) {}
		
		public void update(int a_ms)
		{
			switch(input)
			{
			case 'A': Game.handleInput(input);
			case 'D': Game.handleInput(input);
			}
			input = 0;
		}
		
}

	
