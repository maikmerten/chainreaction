package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.StandardAI;

import java.util.LinkedList;
import java.util.List;

public class TournamentRunner {

	private static final int DEFAULT_NUMBER_OF_ROUNDS = 100;

	public static void main(String[] commandLineArguments) {
		int numberOfRounds = DEFAULT_NUMBER_OF_ROUNDS;

		if (commandLineArguments.length == 1) {
			try {
				numberOfRounds = Integer.parseInt(commandLineArguments[0]);
			}
			catch (NumberFormatException nfe) {
				System.out.println("Usage: TournamentRunner [even number of rounds per match]");
				System.exit(0);
			}
		}

		if ((numberOfRounds % 2) != 0) {
			System.err.println("Specified number of rounds per match is not even!");
			System.exit(1);
		}

		final List<AI> ais = new LinkedList<>();
		ais.add(new StandardAI());
		ais.addAll(SettingsLoader.loadAIs());

		if (ais.size() < 2) {
			String aiDir = SettingsLoader.getAIPath().toString();
			System.err.println("Unable to find additional AIs for tournament. Please add them to " + aiDir);
			System.exit(1);
		}

		Tournament tournament = new Tournament(ais, numberOfRounds);
		tournament.startTournament();
	}

}
