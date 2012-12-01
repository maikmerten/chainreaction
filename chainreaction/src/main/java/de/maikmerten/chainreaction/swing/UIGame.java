package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.AI;
import de.maikmerten.chainreaction.Field;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author maik
 */
public class UIGame implements MoveListener {

	private JFrame frame;
	private JLabel status;
	private Field field;
	private UIField uifield;
	private UISettings uisettings;
	private AI ai = new AI();
	private byte player = 1;
	private byte winner = 0;

	public UIGame() {
		field = new Field(6, 5);
		frame = new JFrame("Chain reaction");
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		uifield = new UIField(this, field);
		frame.add(uifield, BorderLayout.CENTER);
		status = new JLabel();
		frame.add(status, BorderLayout.SOUTH);
		uisettings = new UISettings();
		frame.add(uisettings, BorderLayout.NORTH);

		startGame();
		
		frame.setVisible(true);
	}

	private void startGame() {
		player = 1;
		winner = 0;
		field = new Field(6,5);
		uifield.setField(field);
		updateStatus();
	}
	

	public void onMoveSelected(int x, int y) {
		
		if(winner != 0) {
			startGame();
			return;
		}
		
		if (field.getOwner(x, y) != 0 && field.getOwner(x, y) != player) {
			return;
		}

		field.putAtom(player, x, y);
		field.react();
		player = player == 1 ? (byte) 2 : (byte) 1;
		winner = field.getWinner();
		updateStatus();
		
		if(player == 2 && uisettings.againstAI()) {
			int[] aicoords = ai.thinkAI(field, (byte)2, (byte)1);
			onMoveSelected(aicoords[0], aicoords[1]);
		}
	}
	
	
	private void updateStatus() {
		if(winner != 0) {
			status.setText("Player " + winner + " won!");
		} else {
			status.setText("Active player: " + player);
		}
	}

	public static void main(String[] args) {
		UIGame g = new UIGame();
	}

}
