package de.maikmerten.chainreaction.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.FieldListener;
import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;


/**
 * @author jonny
 *
 */
public class UIField extends JPanel implements Runnable, FieldListener, MoveListener, UIDrawable {

	static final int CELL_SIZE = 64;
	private static final long serialVersionUID = 6726303350914771035L;
	private MoveListener moveListener;
	private UICell[][] cells;
	private final int DELAY = 25;
	private Game game;
	
	public UIField(MoveListener listener, Game game) {
		this.moveListener = listener;
		setGame(game);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		initField();
		
		// Get to know as a cell is clicked.
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				final int x = (e.getX()/(CELL_SIZE*2)) % getField().getWidth();
				final int y = (e.getY()/(CELL_SIZE*2)) % getField().getWidth();
				moveListener.onMoveSelected(x, y);
				e.consume();
			}
			
		});
		
	}

	private void initField() {
		setPreferredSize(new Dimension(getField().getWidth()*CELL_SIZE, getField().getHeight()*CELL_SIZE));
		cells = new UICell[getField().getWidth()][getField().getHeight()];
		
		for(int x = 0; x < getField().getWidth(); x++) {
			for(int y = 0; y < getField().getHeight(); y++) {
				
				cells[x][y] = new UICell(
						(x * CELL_SIZE * 2), 
						(y * CELL_SIZE * 2));
			}
		}
	}
	
	public final void setGame(Game game) {
		this.game = game;
		getField().addFieldListener(this);
		initField();
	}

	public Field getField() {
		return game.getField();
	}

	@Override
	public void onAtomAdded(final byte player, final int x, final int y) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				if(cells[x][y].isEmpty()) {
					cells[x][y].setOwner(player);
				}
				cells[x][y].addAdtom();
			}
		});
	}

	@Override
	public void onAtomMoved(final int x1, final int y1, 
			final int x2, final int y2) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				cells[x1][y1].moveTo(cells[x2][y2]);
			}
		});
	}

	@Override
	public void onOwnerChanged(final byte player, final int x, final int y) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				cells[x][y].setOwner(player);
			}
		});
	}

	// draw the grid.
	@Override
	public void draw(Graphics2D g2d) {
		RenderingHints rh =
				new RenderingHints(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.darkGray);
		
		final int fieldWidth = getField().getWidth();
		final int fieldHeight = getField().getHeight();
		
		// draw vertical lines (fine)
		for(int i = 0; i <= (fieldWidth*2); i++) {
			g2d.drawLine(i*CELL_SIZE, 0, i*CELL_SIZE, 2*fieldHeight*CELL_SIZE);
		}
		// draw horizontal lines (fine)
		for(int i = 0; i <= (fieldHeight*2); i++) {
			g2d.drawLine(0, i*CELL_SIZE, 2*fieldWidth*CELL_SIZE, i*CELL_SIZE);
		}
		
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.gray);
		
		// draw vertical lines (coarse)
		for(int i = 0; i <= (fieldWidth); i++) {
			g2d.drawLine(2*i*CELL_SIZE, 0, 2*i*CELL_SIZE, 2*fieldHeight*CELL_SIZE);
		}
		// draw horizontal lines (coarse)
		for(int i = 0; i <= (fieldHeight); i++) {
			g2d.drawLine(0, 2*i*CELL_SIZE, 2*fieldWidth*CELL_SIZE, 2*i*CELL_SIZE);
		}
	}

	@Override
	public void onMoveSelected(int x, int y) {
		moveListener.onMoveSelected(x, y);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		// start animation cycle.
		new Thread(this).start();
	}

	// render all objects on the screen.
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		
		// draw grid
		draw(g2d);
		
		// draw elements
		for(final UIDrawable[] drawableRow : cells) {
			for(final UIDrawable drawable : drawableRow) {
				if(drawable != null) {
					drawable.draw(g2d);
				}
			}
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void run() {

		while(true) {
			long beforeTime, timeDiff, sleep;

	        beforeTime = System.currentTimeMillis();

	        while (true) {
	            repaint();
	            
	            timeDiff = System.currentTimeMillis() - beforeTime;
	            sleep = DELAY - timeDiff;

	            if (sleep >= 2) {
		            try {
		                Thread.sleep(sleep);
		            } catch (InterruptedException e) {
		                System.out.println("interrupted");
		            }
	            } 

	            beforeTime = System.currentTimeMillis();
	        }
		}
	}
}
