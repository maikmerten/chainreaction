package de.maikmerten.chainreaction.swing;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import de.maikmerten.chainreaction.FieldListener;
import de.maikmerten.chainreaction.Move;


/**
 * @author jonny
 *
 */
public class SwingFieldListener implements FieldListener {
	private FieldListener listener;
	private ExecutorService execService;

	public SwingFieldListener(FieldListener listener) {
		this.listener = listener;
		this.execService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void onAtomAdded(final byte player, final int x, final int y) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onAtomAdded(player, x, y);
			}

		}, 40);
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onAtomsMoved(moves);
			}
		}, 40);
	}

	@Override
	public void onOwnerChanged(final byte player, final int x, final int y) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onOwnerChanged(player, x, y);
			}
		}, 0);
	}

	@Override
	public void onCellCleared(final int x, final int y) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onCellCleared(x, y);
			}
		}, 40);
	}
	
	private void executeDelayed(final Runnable run, final long delay) {
		execService.submit(new Runnable() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(run);
				if(delay > 0) {
					try {
						Thread.sleep(delay);
					}
					catch (InterruptedException e) {
						// do nothing
					}
				}
			}
		});
	}
}
