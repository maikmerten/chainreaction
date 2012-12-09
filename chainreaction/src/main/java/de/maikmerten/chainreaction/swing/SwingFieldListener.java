package de.maikmerten.chainreaction.swing;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.maikmerten.chainreaction.FieldListener;


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
	public void onAtomMoved(final int x1, final int y1, 
			final int x2, final int y2) {
		if(SwingUtilities.isEventDispatchThread()) {
			listener.onAtomMoved(x1, y1, x2, y2);
			return;
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					listener.onAtomMoved(x1, y1, x2, y2);
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
