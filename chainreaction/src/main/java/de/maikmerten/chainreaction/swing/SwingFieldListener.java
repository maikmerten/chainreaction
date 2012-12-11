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

	private int counter = 0;
	private int swingCounter = 0;

	public SwingFieldListener(FieldListener listener) {
		this.listener = listener;
	}

	@Override
	public void onAtomAdded(final byte player, final int x, final int y) {
		final int localCounter = ++counter;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				swingCounter++;
				if(localCounter != swingCounter) {
					throw new IllegalStateException(localCounter + ", " + swingCounter);
				}
				listener.onAtomAdded(player, x, y);
			}
		});
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		final int localCounter = ++counter;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				swingCounter++;
				if(localCounter != swingCounter) {
					throw new IllegalStateException(localCounter + ", " + swingCounter);
				}
				listener.onAtomsMoved(moves);
			}
		});
	}

	@Override
	public void onOwnerChanged(final byte player, final int x, final int y) {
		final int localCounter = ++counter;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				swingCounter++;
				if(localCounter != swingCounter) {
					throw new IllegalStateException(localCounter + ", " + swingCounter);
				}
				listener.onOwnerChanged(player, x, y);
			}
		});
	}

	@Override
	public void onCellCleared(final int x, final int y) {
		final int localCounter = ++counter;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				swingCounter++;
				if(localCounter != swingCounter) {
					throw new IllegalStateException(localCounter + ", " + swingCounter);
				}
				listener.onCellCleared(x, y);
			}
		});
	}
}
