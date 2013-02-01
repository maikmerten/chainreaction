package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.StandardAI;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TournamentRunner {

	private static final int NUMBER_OF_ROUNDS = 100;
	private static final String DELIMITER = "     ";

	public static void main(String[] commandLineArguments) {
		final List<AI> ais = new LinkedList<>();
		ais.add(new StandardAI());
		ais.addAll(SettingsLoader.loadAIs());

		Settings settings = SettingsLoader.loadSettings();

		Map<Player, AI> aiMap = new EnumMap<>(Player.class);
		aiMap.put(Player.FIRST, new StandardAI());
		aiMap.put(Player.SECOND, new StandardAI());

		Match match = new Match(aiMap, settings);
		Map<Player, Integer> results = match.run(NUMBER_OF_ROUNDS);

		printStatistics(aiMap, results);
	}

	private static void printStatistics(Map<Player, AI> ais, Map<Player, Integer> results) {
		String totalGames = "Total Games";
		String firstPlayerName = ais.get(Player.FIRST).getName();
		String secondPlayerName = ais.get(Player.SECOND).getName();

		System.out.println(totalGames + DELIMITER + firstPlayerName + DELIMITER + secondPlayerName);

		String alignedTotalCount = alignValue(NUMBER_OF_ROUNDS, totalGames);
		String alignedFirstPlayerWins = alignValue(results.get(Player.FIRST), firstPlayerName);
		String alignedSecondPlayerWins = alignValue(results.get(Player.SECOND), secondPlayerName);

		System.out.println(alignedTotalCount + DELIMITER + alignedFirstPlayerWins
				+ DELIMITER + alignedSecondPlayerWins);

		System.out.println();

		String overallWinner;

		if (results.get(Player.FIRST) > results.get(Player.SECOND)) {
			overallWinner = ais.get(Player.FIRST).getName();
		}
		else if (results.get(Player.SECOND) > results.get(Player.FIRST)) {
			overallWinner = ais.get(Player.SECOND).getName();
		}
		else {
			overallWinner = "None";
		}

		System.out.println("Tournament Winner: " + overallWinner);
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
