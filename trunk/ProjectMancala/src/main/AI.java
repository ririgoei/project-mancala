package main;

public class AI {

	private boolean ExtraTurnAvailable[];
	private boolean StealAvailable[];
	private boolean StealReachable[];
	private boolean ExtraTurnSetupAvailable[];
	private int P1OptimumSteal, P2OptimumSteal;
	private int P1Decision, P2Decision;
	private int GameBoard[];

	public AI(int a_bowls[]) {
		ExtraTurnAvailable = new boolean[14];
		StealAvailable = new boolean[14];
		StealReachable = new boolean[14];
		ExtraTurnSetupAvailable = new boolean[14];
		initializeArrays();
		P1OptimumSteal = 0;
		P2OptimumSteal = 0;
		P1Decision = 0;
		P2Decision = 0;
		
		GameBoard = new int[14];
		updateGameBoard(a_bowls);
	}
	
	public void initializeArrays(){
		for(int i = 0; i < ExtraTurnAvailable.length; i++){
			ExtraTurnAvailable[i] = false;
			StealAvailable[i] = false;
			StealReachable[i] = false;
			ExtraTurnSetupAvailable[i] = false;
		} 
	}
	
	public void updateGameBoard(int a_bowls[]){
		for(int i = 0; i < a_bowls.length; i++){
			GameBoard[i] = a_bowls[i];
		}
	}

	public void Evaluate() {
		// check entire board for extra turns
		//boolean ExtraTurnAvailable[] = new boolean[14];
		for (int i = 0; i < 13; i++) {
			ExtraTurnAvailable[i] = false;
		}

		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (i < 6) {
					if (GameBoard[i] == 5 - i + 1) {
						ExtraTurnAvailable[i] = true;
					}
				}
				if (i > 6) {
					if (GameBoard[i] == 12 - i + 1) {
						ExtraTurnAvailable[i] = true;
					}
				}
			}
		}

		// check row for steals
		// boolean StealAvailable[] = new boolean[14];
		for (int i = 0; i < 13; i++) {
			StealAvailable[i] = false;
		}

		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (GameBoard[i] == 0 && GameBoard[12 - i] > 0) {
					StealAvailable[i] = true;
				}
			}
		}

		// if a steal is available, check if there are any bowls that can reach
		// it
		// boolean StealReachable[] = new boolean[14];
		for (int i = 0; i < 13; i++) {
			StealReachable[i] = false;
		}
		for (int i = 0; i < 13; i++) {
			if (StealAvailable[i] == true) {
				if (i < 6) {
					for (int c = i; c >= 0; c--) {
						if (GameBoard[c] == i - c && GameBoard[c] != 0) {
							StealReachable[c] = true;
						}
					}
				}
				if (i > 6) {
					for (int c = i; c > 6; c--) {
						if (GameBoard[c] == i - c && GameBoard[c] != 0) {
							StealReachable[c] = true;
						}
					}
				}
			}
		}

		// find out which bowls set up extra turns
		// boolean ExtraTurnSetupAvailable[] = new boolean[14];
		for (int i = 0; i < 13; i++) {
			ExtraTurnSetupAvailable[i] = false;
		}

		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (i < 6) {
					for (int c = 0; c < 6; c++) {
						if (i < c && GameBoard[c] == 5 - c && GameBoard[i] >= c - i) {
							ExtraTurnSetupAvailable[i] = true;
						}
					}
					if (i > 6) {
						for (int c = 7; c < 12; c++) {
							if (i < c && GameBoard[c] == 12 - c
									&& GameBoard[i] >= c - i) {
								ExtraTurnSetupAvailable[i] = true;
							}
						}
					}
				}
			}
		}
		// find out which bowl sets up an optimum steal
		int P1OptimumSteal, P2OptimumSteal;
		for (int i = 0; i < 13; i++) {

		}
		// find out where to ruin steal opportunties
		// find out where to ruin extra turns

		// int x = 7;
		// gotoxy(0, 9);
		// printf("Extra: ");
		// gotoxy(0, 10);
		// printf("Steal: ");
		// gotoxy(0, 11);
		// printf("Reach: ");
		// gotoxy(0, 12);
		// printf("Setup: ");
		// for(int i = 0; i < 13; i++)
		// {
		// gotoxy(x, 9);
		//
		// if(i != 6) printf("%i", ExtraTurnAvailable[i]);
		// gotoxy(x, 10);
		// if(i != 6) printf("%i", StealAvailable[i]);
		// gotoxy(x, 11);
		// if(i != 6) printf("%i", StealReachable[i]);
		// gotoxy(x, 12);
		// if(i != 6) printf("%i", ExtraTurnSetupAvailable[i]);
		// x += 2;
		// }

		// if there are extra turns or steals available, decide wether a steal
		// or an extra turn is mroe effective
		// if choosing an extra turn start from bowl closest to pool
		// if choosing to steal steal from bowl with greatest return value
		// if neither check if there are bowls that set up extra turns
		// if there are bowls that set up an extra turn, choose closest one to
		// pool
		// last resort: get rid of bowl with largest amount of beads
	}

	public void makeDecision(){
		
	}
	
	public int getPlayer1Decision(){return P1Decision;}
	public int getPlayer2Decision(){return P2Decision;}
}
