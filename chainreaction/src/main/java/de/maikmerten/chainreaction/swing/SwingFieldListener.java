package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.FieldListener;
import de.maikmerten.chainreaction.Move;
import de.maikmerten.chainreaction.Player;
import de.maikmerten.chainreaction.Settings;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author jonny
 *
 */
public class SwingFieldListener implements FieldListener {
	private final FieldListener listener;
	private final ExecutorService execService;
	// TODO ability to change delay in order to accelerate animation.
	private final int delay;

	public SwingFieldListener(FieldListener listener, Settings settings) {
		this.listener = listener;
		this.delay = settings.getReactionDely();
		this.execService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void onAtomAdded(final Player player, final int x, final int y) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onAtomAdded(player, x, y);
			}

		}, delay);
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		executeDelayed(new Runnable() {
			@Override
			public void run() {
				listener.onAtomsMoved(moves);
			}
		}, delay);
	}

	@Override
	public void onOwnerChanged(final Player player, final int x, final int y) {
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
		}, delay);
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