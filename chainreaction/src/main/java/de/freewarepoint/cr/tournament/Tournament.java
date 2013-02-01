package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class Tournament {

	private static final int NUMBER_OF_ROUNDS = 100;

	private final List<AI> ais;

	public Tournament(List<AI> ais) {
		this.ais = ais;
	}

	public void startTournament() {
		List<PairOfAis> pairs = determineAiPairs();
		Settings settings = SettingsLoader.loadSettings();
		for (PairOfAis pair : pairs) {
			Match match = new Match(pair, settings);
			match.run(NUMBER_OF_ROUNDS);
		}

		StatisticsPrinter statisticsPrinter = new StatisticsPrinter(pairs, NUMBER_OF_ROUNDS);
		statisticsPrinter.printStatistics();
	}

	private List<PairOfAis> determineAiPairs() {
		Set<AI> aiPool = new HashSet<>(ais);
		List<PairOfAis> pairs = new LinkedList<>();

		while (!aiPool.isEmpty()) {
			AI firstOpponent = aiPool.iterator().next();
			aiPool.remove(firstOpponent);
			for (AI secondOpponent : aiPool) {
				pairs.add(new PairOfAis(firstOpponent, secondOpponent));
			}
		}

		return pairs;
	}

}
