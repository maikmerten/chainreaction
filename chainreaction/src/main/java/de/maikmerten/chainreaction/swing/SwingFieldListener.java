package de.maikmerten.chainreaction.swing;

import java.util.List;

import javax.swing.SwingUtilities;

import de.maikmerten.chainreaction.FieldListener;
import de.maikmerten.chainreaction.Move;


/**
 * @author jonny
 *
 */
public class SwingFieldListener implements FieldListener {
	private FieldListener listener;


	public SwingFieldListener(FieldListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAtomAdded(final byte player, final int x, final int y) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				listener.onAtomAdded(player, x, y);
			}
		});
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				listener.onAtomsMoved(moves);
			}
		});
	}

	@Override
	public void onOwnerChanged(final byte player, final int x, final int y) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				listener.onOwnerChanged(player, x, y);
			}
		});
	}
}
