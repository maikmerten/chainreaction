package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.ai.AI;

import java.util.EnumMap;
import java.util.Map;

public class Match {

	final Map<Player, AI> ais;
	final Settings settings;

	Match(Map<Player, AI> ais, Settings settings) {
		this.ais = ais;
		this.settings = settings;
	}

	public Map<Player, Integer> run(int numberOfRounds) {
		Map<Player, Integer> results = new EnumMap<>(Player.class);
		results.put(Player.FIRST, 0);
		results.put(Player.SECOND, 0);

		while (numberOfRounds > 0) {
			Player winner = evaluateOneGame();
			int currentlyWonGames = results.get(winner) + 1;
			results.put(winner, currentlyWonGames);
			numberOfRounds--;
		}

		return results;
	}


	private Player evaluateOneGame() {
		Game game = new Game(6, 5, settings);
		Player currentPlayer = Player.FIRST;

		while (game.getWinner() == Player.NONE) {
			AI currentAI = ais.get(currentPlayer);
			currentAI.setGame(game);
			currentAI.doMove();
		}

		return game.getWinner();
	}

}
