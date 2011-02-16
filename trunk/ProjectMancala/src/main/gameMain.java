package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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
public class gameMain extends JPanel implements MouseListener, KeyListener, Runnable
{
		XY m_point = new XY();
		/** whether or not the game is running */
		/** width/height of the map in terms of columns and rows */
		XY m_size = new XY();
		GameBoard game;
		
		int m_input;
		Image m_MancalaBoard;
		
		private boolean game_Running;

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
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			drawBoard(g);
		}
		
		//Place holder board.
		public void drawBoard(Graphics g)
		{
			g.drawLine(0, 0, 0 + 600, 0);
			g.drawLine(0, 0, 0, 200);
			g.drawLine(600, 0, 600, 200);
			g.drawLine(0, 200, 600, 200);
			//Two pools. Top - Player 2, Bottom - Player 1
			g.setColor(Color.red);
			g.fillOval(20, 60, 75, 100);
			g.setColor(Color.orange);
			g.fillOval(450, 60, 75, 100);
			//First player pots.
			g.setColor(Color.red);
			g.fillOval(105, 35, 40, 40);
			g.fillOval(165, 35, 40, 40);
			g.fillOval(225, 35, 40, 40);
			g.fillOval(285, 35, 40, 40);
			g.fillOval(345, 35, 40, 40);
			g.fillOval(405, 35, 40, 40);
			//Second player pots.
			g.setColor(Color.orange);
			g.fillOval(105, 155, 40, 40);
			g.fillOval(165, 155, 40, 40);
			g.fillOval(225, 155, 40, 40);
			g.fillOval(285, 155, 40, 40);
			g.fillOval(345, 155, 40, 40);
			g.fillOval(405, 155, 40, 40);
			g.setColor(Color.black);
			
			
			for(int i=0; i < game.getGameSize(); i++)
			{
				g.drawString(game.getBowl(i), game.getBowlLocationX(i),game.getBowlLocationY(i));
			}
			g.drawImage(m_MancalaBoard, 10, 10, null);
		}
		
		public gameMain()
		{	
			addMouseListener(this);
			addKeyListener(this);
			game_Running = true;
			game = new GameBoard(true, 2);
			m_input = 0;
			Thread t = new Thread(this);
			t.start();
			m_MancalaBoard = Toolkit.getDefaultToolkit().createImage("mancalaBoard.png");
		}
		
		@SuppressWarnings("unused")
		public void run()
		{
			
			long now, then = System.currentTimeMillis(), passed;
			while(game_Running)
			{
				now = System.currentTimeMillis();
				passed = now - then;
				then = now;
				
				//Update.
				game.update(m_input);
				m_input = 0;
				game_Running = game.isRunning();
				//Draw function from paint component.
				repaint();
				//Throttle code in a Try/Catch.
				Sleep(100);
				
				//Input, going to be using mouse, working with that soon.
			}
			game.DisplayWinner();
		}
				
		//All of the input functions. Keyboard input, and Mouse input.
		public void keyPressed(KeyEvent arg0) {
			m_input = arg0.getKeyCode();
		}
		public void keyReleased(KeyEvent arg0) {}
		public void keyTyped(KeyEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
		//Sleep method for Throttle code in a Try/Catch.
		public void Sleep(long SleepTime)
		{
			try {
				Thread.sleep(SleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}

	
