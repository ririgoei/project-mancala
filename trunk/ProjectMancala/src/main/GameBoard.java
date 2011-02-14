package main;

import java.util.Random;
/*TODO:
 * 
 * In this class, we should build the gameboard and all of it's properties. So things we should include in here are:
 * 
 * Each player's pots, with player ownership (although we can do this in the LinkList/Array classes.
 * We should also have win conditions, pot placement in the window, and pool placement.
 * Possibly we should have the score and what not placed in here too referenced from the LinkList/Array classes.
 * 
 */

public class GameBoard {
	int STANDARDBEADS = 3;
	private int m_bowls[] = new int[14];
	private int player1Pool;
	private int player2Pool;
	private int selectron, currentPlayer;
	private boolean running;
	Random rand = new Random();

	// Initializer 
	public GameBoard(boolean randomize)
		{
			initializeGame(randomize);
			player1Pool = 6;
			player2Pool = 13;
		}
	public void initializeGame(boolean randomize)
		{
			if(randomize) RandomizeBowls();
			else fillBowls();
			currentPlayer = 1;
			selectron = 0;
			running = true;
		}
	// Reset Game board
	public void ResetGameBoard()
		{
			for(int i = 0; i < 14; i++)
			{
				m_bowls[i] = 0;
			}
			selectron = 0;
			currentPlayer = 0;
			running = false;
		}

	// Fills all bowls with a standard number of beads 
	public void fillBowls()
		{
			for(int i = 0; i < 14; i++)
			{
				if((i+1) % 7 == 0) m_bowls[i] = 0;
				else m_bowls[i] = STANDARDBEADS; // 3 beads per each bowl is the standard
			}
		}
	
	// Fill all bowls with at least one bead, and add to them randomly to fill all bowls with random beads
	public void RandomizeBowls()
		{
			// keep track of total beads
			int totalBeads = 0;
			// fill all bowls with at least one bead
			for(int i = 0; i < 14; i++)
			{
				if((i+1) % 7 == 0) m_bowls[i] = 0;
				else 
				{
					m_bowls[i] = 1;
					totalBeads++;
				}
			}
			// fill random bowls with more beads
			int index;
			while(totalBeads < STANDARDBEADS * 12)
			{
				index = rand.nextInt() % 14;
				if(!((index+1) % 7 == 0))
				{
					m_bowls[index]++;
					totalBeads++;
				}
			}
		}
	
	// distribute all the beads from one bowl to the rest of the board while it still has any
	// also checks for steals and extra turns
	public void distribute(int index)
		{
			// distribute beads through out the board
			int nextBowl = index;
			for(int i = m_bowls[index]; i > 0; i--)
			{
				nextBowl ++; 
				if(nextBowl >= 14)
				{
					nextBowl = 0;
				}
				m_bowls[index]--;
				m_bowls[nextBowl]++;
				draw();
				Sleep(500); 
			}
			// check if last bowl was empty
			boolean steal = false;
			boolean GoAgain = false;
			if(nextBowl == 6 || nextBowl == 13) GoAgain = true;
			if(m_bowls[nextBowl] == 1 && nextBowl != 6 && nextBowl!= 13) steal = true;
			
			// prevent player from stealing from himself
			if(((nextBowl > 6 && currentPlayer == 1) || (nextBowl < 6 && currentPlayer == 2))) steal = false;

			// if true find opposite bowl and steal pieces in that bowl, and reward them to the current player
			if (steal)
			{
				int oppositeBowl = 12 - nextBowl;
				if(m_bowls[oppositeBowl] != 0)
				{
					if(oppositeBowl < 6)
					{
						m_bowls[13] += m_bowls[oppositeBowl];
					}
					else if (oppositeBowl > 6)
					{
						m_bowls[6] += m_bowls[oppositeBowl];
					}				
					m_bowls[oppositeBowl] = 0;
					//gotoxy(1, 5);
					System.out.println("STEAL!");
					Sleep(1500); 
					//gotoxy(1, 5);
					//clearPrompts();
				}
			}
			if (GoAgain) 				
			{
				//gotoxy(1, 5);
				System.out.println("Extra Turn!");
				Sleep(1500);
				//gotoxy(1, 5);
				//clearPrompts();
			}
			else SwitchCurrentPlayer();
		}

	public void draw()
		{
			// set y location for printing
			//int y = 1;

			// set color atributes
			//setWhiteColor();

			// print player 2 bowls on top
			int x = 0;
			for(int i = 12; i > 6; i--)
			{
				//gotoxy(x * 4 +5, y);
				// check for selectron
				if(selectron == i)
				{
					// print selectron
//					setSelectronColor();
					System.out.printf("(%2i) ", m_bowls[i]);
//					setWhiteColor();
				}
				else System.out.printf("%2i ", m_bowls[i]);
				x++;
			}
			System.out.printf("/n");
			
			// print player pools
			//y = 2;
			//gotoxy(1, y);
			System.out.printf("%2i", m_bowls[13]); // print player 2 pool on the left
			//gotoxy(29, y);
			System.out.printf("/t/t");
			System.out.printf("%2i", m_bowls[6]); // print player 1 pool on the right
			System.out.printf("/n");

			// print player 1 bowls on the bottom
			//y = 3;
			for(int i = 0; i < 6; i++)
			{
				//gotoxy(i * 4 +5, y);
				// check for selectron
				if(selectron == i)
				{
					// print selectron
					//setSelectronColor();
					System.out.printf("(%2i) ", m_bowls[i]);
					//setWhiteColor();
				}
				else System.out.printf("%2i ", m_bowls[i]);
			}
			System.out.printf("/n");
		}
//	public void setSelectronColor(void)
//		{
//			int color_attribute = FOREGROUND_GREEN | FOREGROUND_INTENSITY;
//			SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE),color_attribute);
//		}
//	public void setGreenColor(void)
//		{
//			int color_attribute = 3;
//			SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE),color_attribute);
//		}
//	public void setWhiteColor(void)
//		{
//			int color_attribute = FOREGROUND_GREEN | FOREGROUND_BLUE | FOREGROUND_RED | FOREGROUND_INTENSITY;
//			SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE),color_attribute);
//		}
	public void MoveSelectronRight()
		{
			if(currentPlayer == 1) 
			{
				if(selectron < 5) selectron++;
			}
			else if (currentPlayer == 2)
			{
				if(selectron > 7) selectron--;
			}
		}
	public void MoveSelectronLeft()
		{
			if(currentPlayer == 1) 
			{
				if(selectron > 0) selectron--;
			}
			else if (currentPlayer == 2)
			{
				if(selectron < 12) selectron++;
			}
		}
	public void SwitchCurrentPlayer()
		{
			if(currentPlayer == 1) 
			{
				currentPlayer = 2;
				selectron = 7;
			}
			else if (currentPlayer == 2) 
			{
				currentPlayer = 1;
				selectron = 0;
			}
		}
	public int getSelectron() {return selectron;}
	
	public boolean checkGameOverCondition()
		{
			// check player 1
			boolean GameOver =true, flag = false;
			for(int i = 0; i < 6; i++)
			{
				if(m_bowls[i] != 0)
				{
					flag = true; // if an bowls contain a number that is not 0 return false
				}
				if(flag) GameOver = false;
			}
			if(GameOver) return GameOver;
			
			// Check player 2
			GameOver = true;
			flag = false;
			for(int i = 7; i < 13; i++)
			{
				if(m_bowls[i] != 0)
				{
					flag = true;
				}
				if(flag) GameOver = false;
			}
			return GameOver; // if no bowls contained a non-zero number return true
		}
	
	public boolean isRunning() {return running;}
	
	public void update(int input)
		{
			handleInput(input);
			draw();
			running = !checkGameOverCondition();
			AIPlayer();
			if(currentPlayer ==2) AIMove(11);
		}
	public void handleInput(int input)
		{
			switch(input)
			{	
			case 'a': MoveSelectronLeft(); break;
			case 'd': MoveSelectronRight(); break;
			case ' ': if(m_bowls[getSelectron()] != 0) distribute(getSelectron()); break;
			}
		}
	public boolean DisplayWinner()
		{
			int winner;
			if(m_bowls[player1Pool] > m_bowls[player2Pool]) winner = 1;
			else winner = 2;
			//gotoxy(1, 5);
			System.out.printf("Player %i Wins! \n \n", winner);
			//gotoxy(1, 6);
			System.out.printf(" Play Again?");		

//			boolean flag = true;
//			while (flag)
//			{		
//				int input = _getch();
//				input = toupper(input);
//				flag = false;
//				switch(input)
//				{
//				case 'Y':  clearPrompts(); return true; break;
//				case 'N':  clearPrompts(); return false; break;
//				default: gotoxy(1, 7);
//					printf("Invalid entry");
//					flag = true;
//					break;
//				}
//			}
			return true;
		}
//	public void clearPrompts()
//		{
//			for(int y = 5; y <= 8; y++)
//			{
//				for(int x = 0; x < 33; x++)
//				{
//					gotoxy(x, y);
//					printf(" ");
//				}
//			}
//			gotoxy(0, 5);
//		}
	public void AIPlayer()
		{
			// check entire board for extra turns
			boolean ExtraTurnAvailable[] = new boolean[14];
			for(int i = 0; i < 13; i++)
			{
				ExtraTurnAvailable[i] = false;
			}

			for(int i = 0; i <13; i++)
			{
				if(i != 6 && i!= 13)
				{
					if(i < 6)
					{
						if(m_bowls[i] == 5 - i+1)
						{
							ExtraTurnAvailable[i] = true;
						}
					}
					if(i > 6)
					{
						if(m_bowls[i] == 12 - i +1)
						{
							ExtraTurnAvailable[i] = true;
						}
					}
				}
			}


			// check row for steals
			boolean StealAvailable[] = new boolean[14];
			for(int i = 0; i < 13; i++)
			{
				StealAvailable[i] = false;
			}
			
			for(int i = 0; i <13; i++)
			{
				if(i != 6 && i!= 13)
				{
					if(m_bowls[i] == 0 && m_bowls[12-i] > 0)
					{
						StealAvailable[i] = true;
					}
				}
			}

			// if a steal is available, check if there are any bowls that can reach it
			boolean StealReachable[] = new boolean[14];
			for(int i = 0; i < 13; i++)
			{
				StealReachable[i] = false;
			}
			for(int i = 0; i < 13; i++)
			{
				if(StealAvailable[i] == true)
				{
					if(i < 6)
					{
						for(int c = i; c >= 0; c--)
						{
							if(m_bowls[c] == i - c && m_bowls[c] != 0)
							{
								StealReachable[c] = true;
							}
						}
					}
					if(i > 6)
					{
						for(int c = i; c > 6; c--)
						{
							if(m_bowls[c] == i - c && m_bowls[c] != 0)
							{
								StealReachable[c] = true;
							}
						}
					}
				}	
			}

			// find out which bowls set up extra turns
			boolean ExtraTurnSetupAvailable[] = new boolean[14];
			for(int i = 0; i < 13; i++)
			{
				ExtraTurnSetupAvailable[i] = false;
			}

			for(int i = 0; i <13; i++)
			{
				if(i != 6 && i!= 13)
				{
					if(i < 6)
					{
						for(int c = 0; c < 6; c++)
						{
							if(i < c && m_bowls[c] == 5 - c && m_bowls[i] >= c-i)
							{
								ExtraTurnSetupAvailable[i] = true;
							}
						}
						if(i > 6)
						{
							for(int c = 7; c < 12; c++)
							{
								if(i < c && m_bowls[c] == 12 - c && m_bowls[i] >= c-i)
								{
									ExtraTurnSetupAvailable[i] = true;
								}
							}
						}
					}
				}
			}
			// find out which bowl sets up an optimum steal
			int P1OptimumSteal, P2OptimumSteal;
			for(int i = 0; i < 13; i++)
			{

			}
			// find out where to ruin steal opportunties
			// find out where to ruin extra turns
		
//			int x = 7;
//			gotoxy(0, 9);
//			printf("Extra: ");
//			gotoxy(0, 10); 
//			printf("Steal: ");
//			gotoxy(0, 11); 
//			printf("Reach: ");
//			gotoxy(0, 12); 
//			printf("Setup: ");
//			for(int i = 0; i < 13; i++)
//			{
//				gotoxy(x, 9);
//
//				if(i != 6) printf("%i", ExtraTurnAvailable[i]);
//				gotoxy(x, 10);
//				if(i != 6) printf("%i", StealAvailable[i]);
//				gotoxy(x, 11);
//				if(i != 6) printf("%i", StealReachable[i]);
//				gotoxy(x, 12);
//				if(i != 6) printf("%i", ExtraTurnSetupAvailable[i]);
//				x += 2;
//			}

			// if there are extra turns or steals available, decide wether a steal or an extra turn is mroe effective
				// if choosing an extra turn start from bowl closest to pool
				// if choosing to steal steal from bowl with greatest return value
			// if neither check if there are bowls that set up extra turns
				// if there are bowls that set up an extra turn, choose closest one to pool
			// last resort: get rid of bowl with largest amount of beads
		}
	public void AIMove(int index)
		{
			while(selectron < index)
			{
				selectron++;
				draw();
				Sleep(500);
			}
		}
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

