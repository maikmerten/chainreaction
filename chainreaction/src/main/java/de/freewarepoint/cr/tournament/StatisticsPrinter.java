package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.ai.AI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StatisticsPrinter {

	private static final String DELIMITER = "     ";

	private final List<PairOfAis> pairs;
	private final int numberOfRounds;

	public StatisticsPrinter(List<PairOfAis> pairs, int numberOfRounds) {
		this.pairs = pairs;
		this.numberOfRounds = numberOfRounds;
	}

	void printStatistics() {
		Map<AI, Integer> totalWins = new HashMap<>();

		for (PairOfAis pair : pairs) {
			printStatisticsForPair(pair);

			final AI firstAi = pair.getAI(Player.FIRST);
			final AI secondAi = pair.getAI(Player.SECOND);

			if (!totalWins.containsKey(firstAi)) {
				totalWins.put(firstAi, 0);
			}
			if (!totalWins.containsKey(secondAi)) {
				totalWins.put(secondAi, 0);
			}

			int currentlyWonGamesFirstAi = totalWins.get(firstAi) + pair.getGamesWonForAI(Player.FIRST);
			int currentlyWonGamesSecondAi = totalWins.get(secondAi) + pair.getGamesWonForAI(Player.SECOND);

			totalWins.put(firstAi, currentlyWonGamesFirstAi);
			totalWins.put(secondAi, currentlyWonGamesSecondAi);
		}

		AI overallWinner = totalWins.keySet().iterator().next();

		for (AI possibleWinner : totalWins.keySet()) {
			if (totalWins.get(possibleWinner) > totalWins.get(overallWinner)) {
				overallWinner = possibleWinner;
			}
		}

		System.out.println("Tournament Winner: " + overallWinner.getName());
	}

	private void printStatisticsForPair(PairOfAis pair) {
		String totalGames = "Total Games";

		String firstPlayerName = pair.getAI(Player.FIRST).getName();
		String secondPlayerName = pair.getAI(Player.SECOND).getName();

		Player firstDisplayedPlayer;
		Player secondDisplayedPlayer;

		if (firstPlayerName.compareTo(secondPlayerName) > 0) {
			firstDisplayedPlayer = Player.SECOND;
			secondDisplayedPlayer = Player.FIRST;
		}
		else {
			firstDisplayedPlayer = Player.FIRST;
			secondDisplayedPlayer = Player.SECOND;
		}

		String firstDisplayedPlayerName = pair.getAI(firstDisplayedPlayer).getName();
		String secondDisplayedPlayerName = pair.getAI(secondDisplayedPlayer).getName();

		System.out.println(totalGames + DELIMITER + firstDisplayedPlayerName + DELIMITER + secondDisplayedPlayerName);

		String alignedTotalCount = alignValue(numberOfRounds, totalGames);
		String alignedFirstPlayerWins =
				alignValue(pair.getGamesWonForAI(firstDisplayedPlayer), firstDisplayedPlayerName);
		String alignedSecondPlayerWins =
				alignValue(pair.getGamesWonForAI(secondDisplayedPlayer), secondDisplayedPlayerName);

		System.out.println(alignedTotalCount + DELIMITER + alignedFirstPlayerWins
				+ DELIMITER + alignedSecondPlayerWins);

		System.out.println();
	}

	private static String alignValue(int value, String compareString) {
		String str = String.valueOf(value);
		int targetLength = compareString.length();

		if (str.length() >= targetLength) {
			return str;
		}

		int padding = (targetLength - str.length()) / 2;

		while (padding > 0) {
			str = " ".concat(str);
			padding--;
		}

		while (str.length() < targetLength) {
			str = str.concat(" ");
		}

		return str;
	}
}
