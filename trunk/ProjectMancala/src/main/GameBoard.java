package main;

import java.util.Random;
import java.awt.Graphics;
/*TODO:
 * 
 * In this class, we should build the gameboard and all of it's properties. So things we should include in here are:
 * 
 * Each player's pots, with player ownership (although we can do this in the LinkList/Array classes.
 * We should also have win conditions, pot placement in the window, and pool placement.
 * Possibly we should have the score and what not placed in here too referenced from the LinkList/Array classes.
 * 
 */

import javax.swing.JPanel;

public class GameBoard {
	private int STANDARDBEADS = 3;
	private int m_bowls[];
	private int player1Pool;
	private int player2Pool;
	private int selectron, currentPlayer;
	private boolean running;
	Random rand = new Random();
	private XY[] m_bowlLocations;
	private XY m_tileSize;
	private String gameState;
	// variables for distributing
	private int nextBowl, distributionCounter;
	private boolean steal, extraTurn;
	private AI ArtificalInteligence;

	// Initializer
	public GameBoard(boolean randomize) {
		initializeGame(randomize);
		player1Pool = 6;
		player2Pool = 13;
	}

	public void initializeGame(boolean randomize) {
		m_bowls = new int[14];
		m_bowlLocations = new XY[14];
		m_tileSize = new XY();
		if (randomize)
			RandomizeBowls();
		else
			fillBowls();
		initializeBowlPoisitions();
		currentPlayer = 1;
		selectron = 0;
		running = true;
		gameState = "Playing";
		ArtificalInteligence = new AI(m_bowls);
	}

	// Reset Game board
	public void ResetGameBoard() {
		for (int i = 0; i < 14; i++) {
			m_bowls[i] = 0;
		}
		selectron = 0;
		currentPlayer = 0;
		running = false;
	}

	// Fills all bowls with a standard number of beads
	public void fillBowls() {
		for (int i = 0; i < 14; i++) {
			if ((i + 1) % 7 == 0)
				m_bowls[i] = 0;
			else
				m_bowls[i] = STANDARDBEADS; // 3 beads per each bowl is the
											// standard
		}
	}

	// Fill all bowls with at least one bead, and add to them randomly to fill
	// all bowls with random beads
	public void RandomizeBowls() {
		// keep track of total beads
		int totalBeads = 0;
		// fill all bowls with at least one bead
		for (int i = 0; i < 14; i++) {
			if ((i + 1) % 7 == 0)
				m_bowls[i] = 0;
			else {
				m_bowls[i] = 1;
				totalBeads++;
			}
		}
		// fill random bowls with more beads
		int index;
		while (totalBeads < STANDARDBEADS * 12) {
			index = rand.nextInt(14);
			if (!((index + 1) % 7 == 0)) {
				m_bowls[index]++;
				totalBeads++;
			}
		}
	}

	public void initializeBowlPoisitions() {
		m_tileSize.set(60, 60);
		// initialize all XY locations for bowls
		for (int i = 0; i < m_bowlLocations.length; i++) {
			m_bowlLocations[i] = new XY();
		}
		int x = m_tileSize.getX() * 2;
		int y = m_tileSize.getY();
		for (int i = 12; i > 6; i--) {
			m_bowlLocations[i].set(x, y);
			x += m_tileSize.getX();
		}
		x = m_tileSize.getX() * 2;
		y = m_tileSize.getX() * 3;
		for (int i = 0; i < 6; i++) {
			m_bowlLocations[i].set(x, y);
			x += m_tileSize.getX();
		}
		m_bowlLocations[13].set(m_tileSize.getX(), m_tileSize.getY() * 2);
		m_bowlLocations[6].set(m_tileSize.getX() * 8, m_tileSize.getY() * 2);
	}

	// distribute all the beads from one bowl to the rest of the board while it
	// still has any
	// also checks for steals and extra turns
	public void distribute(int index) {
		// distribute beads through out the board
		int nextBowl = index;
		for (int i = m_bowls[index]; i > 0; i--) {
			nextBowl++;
			if (nextBowl >= 14) {
				nextBowl = 0;
			}
			m_bowls[index]--;
			m_bowls[nextBowl]++;
			gameState = "Disributing";
		}
		// check if last bowl was empty
		boolean steal = false;
		boolean extraTurn = false;
		if (nextBowl == 6 || nextBowl == 13)
			extraTurn = true;
		if (m_bowls[nextBowl] == 1 && nextBowl != 6 && nextBowl != 13)
			steal = true;

		// prevent player from stealing from himself
		if (((nextBowl > 6 && currentPlayer == 1) || (nextBowl < 6 && currentPlayer == 2)))
			steal = false;

		// if true find opposite bowl and steal pieces in that bowl, and reward
		// them to the current player
		if (steal) {
			int oppositeBowl = 12 - nextBowl;
			if (m_bowls[oppositeBowl] != 0) {
				if (oppositeBowl < 6) {
					m_bowls[13] += m_bowls[oppositeBowl];
				} else if (oppositeBowl > 6) {
					m_bowls[6] += m_bowls[oppositeBowl];
				}
				m_bowls[oppositeBowl] = 0;
				System.out.println("STEAL!");
			}
		}
		if (extraTurn) {
			System.out.println("Extra Turn!");
		} else {
			SwitchCurrentPlayer();
			gameState = "Playing";
			System.out.println("Players switched");
		}

	}

	public boolean distributeOnce(int index) {
		nextBowl++;
		// if the next bowl is greater than the size of the array, then loop to
		// the beginning of the array
		if (nextBowl >= getGameSize()) {
			nextBowl = 0;
		}
		m_bowls[index]--;
		m_bowls[nextBowl]++;
		distributionCounter--;

		if (distributionCounter <= 0) {
			gameState = "Playing";
			return true;
		} else {
			gameState = "Distributing";
			return false;
		}
	}

	public void initiateDistributionInstance(int index) {
		nextBowl = index;
		distributionCounter = m_bowls[index];
	}

	public void GameLogic() {
		// check if last bowl was empty
		steal = false;
		extraTurn = false;
		if (nextBowl == 6 || nextBowl == 13)
			extraTurn = true;
		if (m_bowls[nextBowl] == 1 && nextBowl != 6 && nextBowl != 13)
			steal = true;

		// prevent player from stealing from himself
		if (((nextBowl > 6 && currentPlayer == 1) || (nextBowl < 6 && currentPlayer == 2)))
			steal = false;
		
		// prevent player from getting an extra when landing in opposing pool
		if (((nextBowl == 6 && currentPlayer == 2) || (nextBowl == 13 && currentPlayer == 1)))
			extraTurn = false;

		// if true find opposite bowl and steal pieces in that bowl, and reward
		// them to the current player
		if (steal) {
			int oppositeBowl = 12 - nextBowl;
			if (m_bowls[oppositeBowl] != 0) {
				if (oppositeBowl < 6) {
					m_bowls[13] += m_bowls[oppositeBowl];
				} else if (oppositeBowl > 6) {
					m_bowls[6] += m_bowls[oppositeBowl];
				}
				m_bowls[oppositeBowl] = 0;
				System.out.println("STEAL!");

			}
		}
		if (extraTurn) {
			System.out.println("Extra Turn!");
		} else {
			SwitchCurrentPlayer();
			gameState = "Playing";
			System.out.println("Players switched");
		}

	}

	public void MoveSelectronRight() {
		if (currentPlayer == 1) {
			if (selectron < 5)
				selectron++;
		} else if (currentPlayer == 2) {
			if (selectron > 7)
				selectron--;
		}
	}

	public void MoveSelectronLeft() {
		if (currentPlayer == 1) {
			if (selectron > 0)
				selectron--;
		} else if (currentPlayer == 2) {
			if (selectron < 12)
				selectron++;
		}
	}

	public void SwitchCurrentPlayer() {
		if (currentPlayer == 1) {
			currentPlayer = 2;
			selectron = 7;
		} else if (currentPlayer == 2) {
			currentPlayer = 1;
			selectron = 0;
		}
	}

	public int getSelectron() {
		return selectron;
	}

	public boolean checkGameOverCondition() {
		// check player 1
		boolean GameOver = true, flag = false;
		for (int i = 0; i < 6; i++) {
			if (m_bowls[i] != 0) {
				flag = true; // if any bowls contain a number that is not 0
								// return false
			}
			if (flag)
				GameOver = false;
		}
		if (GameOver)
			return GameOver;

		// Check player 2
		GameOver = true;
		flag = false;
		for (int i = 7; i < 13; i++) {
			if (m_bowls[i] != 0) {
				flag = true;
			}
			if (flag)
				GameOver = false;
		}
		return GameOver; // if no bowls contained a non-zero number return true
	}

	public boolean isRunning() {
		return running;
	}

	public void update(int input) {
		if (gameState == "Playing") {
			handleInput(input);
		} else if (gameState == "Distributing") {
			handleDistribution(getSelectron());
		}

		running = !checkGameOverCondition();
		// AIPlayer();
		// if(currentPlayer ==2) AIMove(11);
	}

	public void handleDistribution(int index) {
		boolean flag;
		System.out.println(gameState);
		flag = distributeOnce(index);
		if (flag)
			GameLogic();
		Sleep(500);
	}

	public void handleInput(int input) {
		switch (input) {
		case 'A':
			MoveSelectronLeft();
			break;
		case 'D':
			MoveSelectronRight();
			break;
		case ' ':
			if (m_bowls[getSelectron()] != 0) {
				gameState = "Distributing";
				initiateDistributionInstance(getSelectron());
			}
			break;
		}
	}

	public void DisplayWinner() {
		if (m_bowls[player1Pool] == m_bowls[player2Pool])
			System.out.println("Tie Game!");
		else {
			int winner;
			if (m_bowls[player1Pool] > m_bowls[player2Pool])
				winner = 1;
			else
				winner = 2;
			System.out.println("Player " + winner + " Wins!");
		}
	}

	public void AIMove(int index) {
		while (selectron < index) {
			selectron++;
			// draw(g);
			Sleep(500);
		}
	}

	public void Sleep(long SleepTime) {
		try {
			Thread.sleep(SleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getBowl(int index) {
		if (index == selectron)
			return "(" + m_bowls[index] + ")";
		else
			return "" + m_bowls[index];
	}

	public int getBowlLocationX(int index) {
		return m_bowlLocations[index].getX();
	}

	public int getBowlLocationY(int index) {
		return m_bowlLocations[index].getY();
	}

	public int getGameSize() {
		return m_bowls.length;
	}

	public String getGameState() {
		return gameState;
	}
}
