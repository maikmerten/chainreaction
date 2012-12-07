package de.maikmerten.chainreaction.swing;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.FieldListener;
import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;

/**
 *
 * @author maik
 */
public class UIField extends JPanel implements FieldListener, MoveListener {

	private MoveListener moveListener;
	private Game game;
	private List<UICell> cells = new ArrayList<UICell>();

	public UIField(MoveListener listener, Game game) {
		super();
		this.moveListener = listener;

		this.setLayout(new GridLayout(game.getField().getHeight(), game.getField().getWidth()));

		for (int y = 0; y < game.getField().getHeight(); ++y) {
			for (int x = 0; x < game.getField().getWidth(); ++x) {
				UICell cell = new UICell(this, x, y);
				cells.add(cell);
				this.add(cell);
			}
		}
		setGame(game);
	}

	public final void setGame(Game game) {
		this.game = game;
		game.getField().addFieldListener(this);
		onFieldChange(game.getField());
	}

	public Field getField() {
		return game.getField();
	}

	public void onFieldChange(Field f) {

		if (game.getField() != f) {
			return;
		}

		Runnable updateRunner = new Runnable() {
			public void run() {
				for (UICell c : cells) {
					c.updateCell();
				}
			}
		};
		
		SwingUtilities.invokeLater(updateRunner);
	}

	public void onMoveSelected(int x, int y) {
		moveListener.onMoveSelected(x, y);
	}

	@Override
	public void onAtomAdded(byte player, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAtomMoved(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOwnerChanged(byte player, int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
