package de.maikmerten.chainreaction.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maik
 */
public class AIThread extends Thread {

	private long delay;
	private AI ai;
	
	public AIThread(AI ai, long delay) {
		this.ai = ai;
		this.delay = delay;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException ex) {
			Logger.getLogger(AIThread.class.getName()).log(Level.SEVERE, null, ex);
		}
		ai.doMove();
	}
	
}
