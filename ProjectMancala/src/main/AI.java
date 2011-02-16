package main;

public class AI {

	private boolean ExtraTurnAvailable[];
	private boolean StealAvailable[];
	private boolean StealReachable[];
	private boolean ExtraTurnSetupAvailable[];
	private boolean LargestBowl[];
	private int DecisionPriority[];
	private boolean StealRuin[];
	private int P1Decision, P2Decision;
	private int GameBoard[];

	public AI(int a_bowls[]) {
		ExtraTurnAvailable = new boolean[14];
		StealAvailable = new boolean[14];
		StealReachable = new boolean[14];
		ExtraTurnSetupAvailable = new boolean[14];
		LargestBowl = new boolean[14];
		StealRuin = new boolean[14];
		DecisionPriority = new int[14];
		initializeArrays();
		P1Decision = 0;
		P2Decision = 7;

		GameBoard = new int[14];
		updateGameBoard(a_bowls);
	}

	public void initializeArrays() {
		for (int i = 0; i < ExtraTurnAvailable.length; i++) {
			ExtraTurnAvailable[i] = false;
			StealAvailable[i] = false;
			StealReachable[i] = false;
			ExtraTurnSetupAvailable[i] = false;
			LargestBowl[i] = false;
			StealRuin[i] = false;
			DecisionPriority[i] = 0;
		}
	}

	public void updateGameBoard(int a_bowls[]) {
		for (int i = 0; i < a_bowls.length; i++) {
			GameBoard[i] = a_bowls[i];
		}
	}

	public void update(int a_bowls[]) {
		updateGameBoard(a_bowls);
		Evaluate();
		makeDecision();
	}

	private void Evaluate() {
		// check entire board for extra turns
		initializeArrays();
		// check entire board for extra turns
		EvaluateExtraTurns();
		// check row for steals
		EvaluateSteals();
		// if a steal is available, check if there are any bowls that can reach
		// it
		EvaluateReachableSteals();
		// find out which bowls set up extra turns
		EvaluateExtraTurnSetupOpportunities();
		// find out which bowl sets up an optimum steal
		EvaluateLargestBowl();
		// find out where to ruin steal opportunities
		EvaluateStealRuinOpportunities();
		// find out where to ruin extra turns
		EvaluateExtraTurnRuinOpportunities();

		// if there are extra turns or steals available, decide whether a steal
		// or an extra turn is more effective
		// if choosing an extra turn start from bowl closest to pool
		// if choosing to steal steal from bowl with greatest return value
		// if neither check if there are bowls that set up extra turns
		// if there are bowls that set up an extra turn, choose closest one to
		// pool
		// last resort: get rid of bowl with largest amount of beads
	}

	private void EvaluateExtraTurns() {
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
	}

	private void EvaluateSteals() {
		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (GameBoard[i] == 0 && GameBoard[12 - i] > 0) {
					StealAvailable[i] = true;
				}
			}
		}
	}

	private void EvaluateReachableSteals() {
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
	}

	private void EvaluateExtraTurnSetupOpportunities() {
		for (int i = 0; i < 13; i++) {
			if (i != 6 && i != 13) {
				if (i < 6) {
					for (int c = 0; c < 6; c++) {
						if (i < c && GameBoard[c] == 5 - c
								&& GameBoard[i] >= c - i) {
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
	}

	private void EvaluateLargestBowl() {
		// find the bowl with the greatest number of beads on both sides
		int LargestValueIndex1 = 0, LargestValueIndex2 = 7;

		for (int i = 0; i < 6; i++) {
			if (GameBoard[i] >= GameBoard[LargestValueIndex1])
				LargestValueIndex1 = i;
		}
		for (int i = 7; i < 13; i++) {
			if (GameBoard[i] >= GameBoard[LargestValueIndex2])
				LargestValueIndex2 = i;
		}
		LargestBowl[LargestValueIndex1] = true;
		LargestBowl[LargestValueIndex2] = true;
	}

	private void EvaluateStealRuinOpportunities() {
		// check if any bowls can reach a steal reachable bowl
		for (int i = 0; i < 13; i++) {
			if (StealReachable[i] == true) {
				if (i > 6) {
					for (int c = i; c >= 0; c--) {
						if (GameBoard[c] == i - c && GameBoard[c] != 0) {
							StealRuin[c] = true;
						}
					}
				}
				if (i < 6) {
					for (int c = 7; c < 12; c++) {
						if (GameBoard[c] + c > 13 && GameBoard[c] != 0) {
							if (GameBoard[c] + c - 13 == i) {
								StealRuin[GameBoard[c] + c - 13] = true;
							}
						}
					}
				}
			}
		}
	}

	private void EvaluateExtraTurnRuinOpportunities() {
		// check if any bowls can reach a extra turn bowl
	}

	private void makeDecision() {
		for (int i = 0; i < 13; i++) {
			// add 4 kudos if extra turns and steals are possible
			if (ExtraTurnAvailable[i])
				DecisionPriority[i] += 4;
			if (StealReachable[i])
				DecisionPriority[i] += 3;

			if (ExtraTurnSetupAvailable[i])
				DecisionPriority[i]++;
			if (LargestBowl[i])
				DecisionPriority[i]++;
		}
		// add one kudos if the steal reachable is greater than one
		for (int i = 0; i < 13; i++) {
			if (i < 6) {
				if (StealReachable[i]) {
					for (int c = i + 1; c < 6; c++) {
						if (GameBoard[c] == i - c && StealAvailable[c]
								&& GameBoard[c] > 1) {
							DecisionPriority[i]++;
						}
					}
				}
			}
			if (i > 6) {
				for (int c = i + 1; c < 13; c++) {
					if (StealReachable[i]) {
						if (GameBoard[c] == i - c && StealAvailable[c]
								&& GameBoard[c] > 1) {
							DecisionPriority[i]++;
						}
					}
				}
			}
		}
		// remove kudos if they interfere with each other
		for (int i = 0; i < 13; i++) {
			if (i < 6) {
				// remove kudos if steals interfere with extra turns
				for (int c = i + 1; c < 6; c++) {
					if (StealReachable[i]) {
						if (GameBoard[c] >= i - c && ExtraTurnAvailable[c]) {
							DecisionPriority[i]--;
						}
					}
				}
				// remove kudos if extra turns interfere with steals
				for (int c = i + 1; c < 6; c++) {
					if (ExtraTurnAvailable[i]) {
						if (GameBoard[c] >= i - c && StealReachable[c]) {
							DecisionPriority[i]--;
						}
					}
				}
			}
			if (i > 6) {
				// remove kudos if steals interfere with extra turns
				for (int c = i + 1; c < 13; c++) {
					if (StealReachable[i]) {
						if (GameBoard[c] >= i - c && ExtraTurnAvailable[c]) {
							DecisionPriority[i]--;
						}
					}
				}
				// remove kudos if extra turns interfere with steals
				for (int c = i + 1; c < 13; c++) {
					if (ExtraTurnAvailable[i]) {
						if (GameBoard[c] >= i - c && StealReachable[c]) {
							DecisionPriority[i]--;
						}
					}
				}
			}
		}

		if (ExtraTurnAvailable[5])
			P1Decision = 5;
		else {
			for (int i = 0; i < 6; i++) {
				if (DecisionPriority[i] > DecisionPriority[P1Decision])
					P1Decision = i;
			}
		}
		if (ExtraTurnAvailable[12])
			P2Decision = 12;
		else {
			for (int i = 7; i < 13; i++) {
				if (DecisionPriority[i] > DecisionPriority[P2Decision])
					P2Decision = i;
			}
		}
	}

	public int getPlayer1Decision() {
		return P1Decision;
	}

	public int getPlayer2Decision() {
		return P2Decision;
	}

	public void displayArrays() {
		// Display Extra Turns Available
		System.out.print("Extra Turns Available: 		");
		displayPlayerOptions(ExtraTurnAvailable);

		// Display Steals Available
		System.out.print("Steals Available: 		");
		displayPlayerOptions(StealAvailable);

		// Display Steal Opportunities Reachable
		System.out.print("Steal Opportunities Reachable:  ");
		displayPlayerOptions(StealReachable);

		// Display Extra Turn Setup Opportunities
		System.out.print("Extra Turn Setup Opportunities: ");
		displayPlayerOptions(ExtraTurnSetupAvailable);

		// Display Steal Priorities
		System.out.print("Largest Bowls:                  ");
		displayPlayerOptions(LargestBowl);

		// Display Steal Priorities
		System.out.print("Steal Ruin Opportunities:       ");
		displayPlayerOptions(StealRuin);

		// Display DecisionPriority
		System.out.print("Decision Priorities:            ");
		displayPlayerOptionsInt(DecisionPriority);
		
		// Display P1 Decision
		System.out.println("Player 1 Decision: "+P1Decision);
		// Display P2 Decision
		System.out.println("Player 2 Decision: "+P2Decision);
	}

	private void displayPlayer1Options(boolean a_array[]) {
		for (int i = 0; i < 6; i++) {
			int output;
			if (a_array[i])
				output = 1;
			else
				output = 0;
			System.out.print(output + " ");
		}
	}

	private void displayPlayer2Options(boolean a_array[]) {
		for (int i = 12; i > 6; i--) {
			int output;
			if (a_array[i])
				output = 1;
			else
				output = 0;
			System.out.print(output + " ");
		}
	}

	private void displayPlayerOptions(boolean a_array[]) {
		displayPlayer1Options(a_array);
		System.out.print(" ");
		displayPlayer2Options(a_array);
		System.out.println();
	}

	private void displayPlayer1OptionsInt(int a_array[]) {
		for (int i = 0; i < 6; i++) {
			System.out.print(a_array[i] + " ");
		}
	}

	private void displayPlayer2OptionsInt(int a_array[]) {
		for (int i = 12; i > 6; i--) {
			System.out.print(a_array[i] + " ");
		}
	}

	private void displayPlayerOptionsInt(int a_array[]) {
		displayPlayer1OptionsInt(a_array);
		System.out.print(" ");
		displayPlayer2OptionsInt(a_array);
		System.out.println();
	}
}
