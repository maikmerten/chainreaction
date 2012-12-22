package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


/**
 * @author jonny
 *
 */
public class UIField extends JPanel implements Runnable, FieldListener, MoveListener, UIDrawable {

	private static final long serialVersionUID = 6726303350914771035L;
	static final int CELL_SIZE = 64;
	private final int DELAY = 25;
	private MoveListener moveListener;
	private Game game;
	private UICell[][] cells;
	
	public UIField(MoveListener listener) {
		this.moveListener = listener;
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		// Get to know as a cell is clicked.
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				int x = (e.getX()/(CELL_SIZE*2));
				if (x >= getField().getWidth()) {
					e.consume();
					return;
				}
				int y = (e.getY()/(CELL_SIZE*2));
				if (y >= getField().getHeight()) {
					e.consume();
					return;
				}
				
				moveListener.onMoveSelected(x, y);
				e.consume();
			}
			
		});
	}

	private void initField() {
		setPreferredSize(new Dimension(
				(getField().getWidth() * CELL_SIZE * 2) + 3, 
				(getField().getHeight() * CELL_SIZE * 2) + 3));
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
		getField().addFieldListener(new SwingFieldListener(this));
		initField();
	}

	public Field getField() {
		return game.getField();
	}

	@Override
	public void onAtomAdded(final Player player, final int x, final int y) {
		if(cells[x][y].isEmpty()) {
			cells[x][y].setOwner(player);
		}
		cells[x][y].addAdtom();
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		for(final Move move : moves) {
			cells[move.getX1()][move.getY1()].moveTo(cells[move.getX2()][move.getY2()]);
		}
	}

	@Override
	public void onOwnerChanged(final Player player, final int x, final int y) {
		cells[x][y].setOwner(player);
	}

	@Override
	public void onCellCleared(int x, int y) {
		cells[x][y].clear();
		
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
		
		final int fieldWidth = getField().getWidth();
		final int fieldHeight = getField().getHeight();
		
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.gray);
		
		// draw vertical lines (coarse)
		for(int i = 0; i <= (fieldWidth); i++) {
			g2d.drawLine((2*i*CELL_SIZE)+1, 0, (2*i*CELL_SIZE)+1, 2*fieldHeight*CELL_SIZE);
		}
		// draw horizontal lines (coarse)
		for(int i = 0; i <= (fieldHeight); i++) {
			g2d.drawLine(0, (2*i*CELL_SIZE)+1, 2*fieldWidth*CELL_SIZE, (2*i*CELL_SIZE)+1);
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
		
		// draw elements
		for(final UIDrawable[] drawableRow : cells) {
			for(final UIDrawable drawable : drawableRow) {
				if(drawable != null) {
					drawable.draw(g2d);
				}
			}
		}
		
		// draw grid
		draw(g2d);
		
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
		                System.err.println("interrupted");
		            }
	            } 

	            beforeTime = System.currentTimeMillis();
	        }
		}
	}
}
