package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class Tournament {

	private final List<AI> ais;
	private final int numberOfRounds;

	public Tournament(List<AI> ais, int numberOfRounds) {
		this.ais = ais;
		this.numberOfRounds = numberOfRounds;
	}

	public void startTournament() {
		List<PairOfAis> pairs = determineAiPairs();
		Settings settings = SettingsLoader.loadSettings();
		for (PairOfAis pair : pairs) {
			Match match = new Match(pair, settings);
			match.run(numberOfRounds);
		}

		StatisticsPrinter statisticsPrinter = new StatisticsPrinter(pairs, numberOfRounds);
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
