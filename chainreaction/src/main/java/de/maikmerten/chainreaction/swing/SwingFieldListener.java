package de.maikmerten.chainreaction.swing;

import java.lang.reflect.InvocationTargetException;
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
		if(SwingUtilities.isEventDispatchThread()) {
			listener.onAtomAdded(player, x, y);
			return;
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					listener.onAtomAdded(player, x, y);
				}
			});
		}
		catch (InterruptedException e) {
			// TODO logging?
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO logging?
			e.printStackTrace();
		}
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		if(SwingUtilities.isEventDispatchThread()) {
			listener.onAtomsMoved(moves);
			return;
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					listener.onAtomsMoved(moves);
				}
			});
		}
		catch (InterruptedException e) {
			// TODO logging?
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO logging?
			e.printStackTrace();
		}
	}

	@Override
	public void onOwnerChanged(final byte player, final int x, final int y) {
		if(SwingUtilities.isEventDispatchThread()) {
			listener.onOwnerChanged(player, x, y);
			return;
		}
		try {

			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					listener.onOwnerChanged(player, x, y);
				}
			});
		}
		catch (InterruptedException e) {
			// TODO logging?
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			// TODO logging?
			e.printStackTrace();
		}
	}
}
