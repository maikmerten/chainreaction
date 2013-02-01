package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.ai.AI;

class PairOfAis {

	private final AI firstAI;
	private final AI secondAI;

	private int gamesWonFirstAI;
	private int gamesWonSecondAI;

	public PairOfAis(AI firstAI, AI secondAI) {
		if (firstAI == null || secondAI == null) {
			throw new IllegalArgumentException("AIs must not be null");
		}

		this.firstAI = firstAI;
		this.secondAI = secondAI;
	}

	public AI getAI(Player player) {
		if (player == Player.FIRST) {
			return firstAI;
		}
		else if (player == Player.SECOND) {
			return secondAI;
		}
		else {
			throw new IllegalArgumentException("Unknown player "+ player);
		}
	}

	public int getGamesWonForAI(Player player) {
		if (player == Player.FIRST) {
			return gamesWonFirstAI;
		}
		else if (player == Player.SECOND) {
			return gamesWonSecondAI;
		}
		else {
			throw new IllegalArgumentException("Unknown player "+ player);
		}
	}

	public void setGamesWonForAI(Player player, int gamesWon) {
		if (player == Player.FIRST) {
			gamesWonFirstAI = gamesWon;
		}
		else if (player == Player.SECOND) {
			gamesWonSecondAI = gamesWon;
		}
		else {
			throw new IllegalArgumentException("Unknown player "+ player);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!(obj instanceof PairOfAis)) {
			return false;
		}

		PairOfAis that = (PairOfAis) obj;

		boolean exactMatch = this.firstAI.equals(that.firstAI) && this.secondAI.equals(that.secondAI);
		boolean reversedMatch = this.firstAI.equals(that.secondAI) && this.secondAI.equals(that.firstAI);

		return exactMatch || reversedMatch;
	}

	@Override
	public int hashCode() {
		int result = firstAI.hashCode();
		result = 31 * result + secondAI.hashCode();
		return result;
	}
}
