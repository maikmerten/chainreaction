package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.StandardAI;

import java.util.LinkedList;
import java.util.List;

public class TournamentRunner {

	public static void main(String[] commandLineArguments) {
		final List<AI> ais = new LinkedList<>();
		ais.add(new StandardAI());
		ais.addAll(SettingsLoader.loadAIs());

		Tournament tournament = new Tournament(ais);
		tournament.startTournament();
	}

}
