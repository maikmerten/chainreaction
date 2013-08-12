package de.freewarepoint.cr.tournament;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.ai.AI;

import java.util.EnumMap;
import java.util.Map;

class Match {

	private final PairOfAis pairOfAis;
	private final Settings settings;
	private Player startingPlayer;

	Match(PairOfAis pairOfAis, Settings settings) {
		this.pairOfAis = pairOfAis;
		this.settings = settings;
		this.startingPlayer = Player.FIRST;
	}

	public void run(int numberOfRounds) {
		Map<Player, Integer> results = new EnumMap<>(Player.class);
		results.put(Player.FIRST, 0);
		results.put(Player.SECOND, 0);

		while (numberOfRounds > 0) {
			Player winner = evaluateOneGame();
			int currentlyWonGames = results.get(winner) + 1;
			results.put(winner, currentlyWonGames);
			numberOfRounds--;
		}

		pairOfAis.setGamesWonForAI(Player.FIRST, results.get(Player.FIRST));
		pairOfAis.setGamesWonForAI(Player.SECOND, results.get(Player.SECOND));
	}


	private Player evaluateOneGame() {
		Game game = new Game(6, 5, settings);
		Player currentPlayer = startingPlayer;

		while (game.getWinner() == Player.NONE) {
			AI currentAI = pairOfAis.getAI(currentPlayer);
			currentAI.setGame(game);
			currentAI.doMove();
		}

		switchPlayers();

		return game.getWinner();
	}

	private void switchPlayers() {
		if (startingPlayer == Player.FIRST) {
			startingPlayer = Player.SECOND;
		}
		else {
			startingPlayer = Player.FIRST;
		}
	}

}
