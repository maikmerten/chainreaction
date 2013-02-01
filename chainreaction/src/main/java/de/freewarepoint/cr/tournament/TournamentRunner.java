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
			numberOfRounds = Integer.parseInt(commandLineArguments[0]);
		}

		final List<AI> ais = new LinkedList<>();
		ais.add(new StandardAI());
		ais.addAll(SettingsLoader.loadAIs());

		Tournament tournament = new Tournament(ais, numberOfRounds);
		tournament.startTournament();
	}

}
