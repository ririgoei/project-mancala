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
	public void update(int a_bowls[]){
		updateGameBoard(a_bowls);
		Evaluate();
	}

	public void Evaluate() {
		// check entire board for extra turns
		initializeArrays();
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


		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (GameBoard[i] == 0 && GameBoard[12 - i] > 0) {
					StealAvailable[i] = true;
				}
			}
		}

		// if a steal is available, check if there are any bowls that can reach
		// it

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
		for (int i = 0; i < 13; i++) {

		}
		// find out where to ruin steal opportunities
		// find out where to ruin extra turns


		// if there are extra turns or steals available, decide whether a steal
		// or an extra turn is more effective
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
	public void displayArrays(){
		// Display Extra Turns Available
		System.out.print("Extra Turns Available: 		");
		displayPlayer1Options(ExtraTurnAvailable);
		System.out.print(" ");
		displayPlayer2Options(ExtraTurnAvailable);
		System.out.println();
		
		// Display Steals Available
		System.out.print("Steals Available: 		");
		displayPlayer1Options(StealAvailable);
		System.out.print(" ");
		displayPlayer2Options(StealAvailable);
		System.out.println();
		
		// Display Steal Opportunities Reachable
		System.out.print("Steal Opportunities Reachable:  ");
		displayPlayer1Options(StealReachable);
		System.out.print(" ");
		displayPlayer2Options(StealReachable);
		System.out.println();
		
		// Display Extra Turn Setup Opportunities
		System.out.print("Extra Turn Setup Opportunities: ");
		displayPlayer1Options(ExtraTurnSetupAvailable);
		System.out.print(" ");
		displayPlayer2Options(ExtraTurnSetupAvailable);
		System.out.println();
	}
	public void displayPlayer1Options(boolean a_array[]){
		for(int i = 0; i < 6; i++){
			int output;
			if(a_array[i]) output = 1; else output = 0;
			System.out.print(output + " ");
		}
	}
	public void displayPlayer2Options(boolean a_array[]){
		for(int i = 12; i > 6; i--){
			int output;
			if(a_array[i]) output = 1; else output = 0;
			System.out.print(output + " ");
		}
	}
	
}
