package de.freewarepoint.cr.swing;

import de.freewarepoint.cr.Field;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.FieldListener;
import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Move;
import de.freewarepoint.cr.MoveListener;
import de.freewarepoint.cr.PlayerStatus;
import de.freewarepoint.retrofont.RetroFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jonny
 */
public class UIField extends JPanel implements Runnable, FieldListener, MoveListener, UIDrawable {

	private static final long serialVersionUID = 6726303350914771035L;

	static final int CELL_SIZE = 64;

	private final int DELAY = 25;

	private Game game;

	private UICell[][] cells;

	private UICellBG[][] cellBGs;
	
	private UIAnimation moveAnim = null, 
			leaveMoveAnim = null, 
			won = null, 
			newGame = null; 

	private double xRoot, yRoot;

	private SwingFieldListener fieldListener;
	private Map<Player, Image> winImgs;

	public UIField(final UIGame uiGame) {
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		setIgnoreRepaint(true);

		// Get to know as a cell is clicked.
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				double x = ((e.getX() - xRoot) / (CELL_SIZE * 2));
				if (x < 0 || x >= getField().getWidth()) {
					e.consume();
					return;
				}
				double y = ((e.getY() - yRoot) / (CELL_SIZE * 2));
				if (y < 0 || y >= getField().getHeight()) {
					e.consume();
					return;
				}

				uiGame.selectMove((int)x, (int)y);
				e.consume();
			}

		});
		
		final RetroFont retroFont = new RetroFont();
		winImgs = new HashMap<Player, Image>();
		winImgs.put(Player.FIRST, createWinImage(retroFont, Player.FIRST));
		winImgs.put(Player.SECOND, createWinImage(retroFont, Player.SECOND));
	}

	private BufferedImage createWinImage(final RetroFont retroFont, final Player player) {
		return createTextImgWithSubtitle(retroFont, 
				UIPlayer.getPlayer(player).getForeground(), 
				"Player " + (player.ordinal() + 1) + " WINS!", 
				"Click into the game field to start the next game.");
	}
	
	private BufferedImage createTextImgWithSubtitle(final RetroFont retroFont, final Color foreground, final String title, final String subTitle) {
		final BufferedImage winStr = retroFont.getRetroString(title, 
				foreground, 64);
		final BufferedImage clickStr = retroFont.getRetroString(subTitle, 
				foreground, 16);
		final BufferedImage bimg = new BufferedImage(winStr.getWidth(), winStr.getHeight() + clickStr.getHeight() + 2, 
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = bimg.createGraphics();
		g2d.drawImage(winStr, 0, 0, null);
		g2d.drawImage(clickStr, (winStr.getWidth()/2) - (clickStr.getWidth()/2), winStr.getHeight() + 2, null);
		return bimg;
	}

	private void initField() {
		setPreferredSize(new Dimension((getField().getWidth() * CELL_SIZE * 2) + 3,
				(getField().getHeight() * CELL_SIZE * 2) + 3));
		cells = new UICell[getField().getWidth()][getField().getHeight()];
		cellBGs = new UICellBG[getField().getWidth()][getField().getHeight()];

		for (int x = 0; x < getField().getWidth(); x++) {
			for (int y = 0; y < getField().getHeight(); y++) {
				cells[x][y] = new UICell(x, y, getField().getWidth(), getField().getHeight());
				cellBGs[x][y] = new UICellBG(x, y, Player.NONE);
			}
		}
	}

	public final void setGame(Game game) {
		this.game = game;
		if(fieldListener != null) {
			fieldListener.shutDown();
		}
		fieldListener = new SwingFieldListener(this, game.getSettings());
		getField().addFieldListener(fieldListener);
		game.addMoveListener(this);
		initField();
	}

	Field getField() {
		return game.getField();
	}

	@Override
	public void onAtomAdded(final Player player, final int x, final int y) {
		if (cells[x][y].isEmpty()) {
			cells[x][y].setOwner(player);
			cellBGs[x][y].changeOwner(player);
		}
		cells[x][y].addAtom(0);
	}

	@Override
	public void onAtomsMoved(final List<Move> moves) {
		for (final Move move : moves) {
			cells[move.getX1()][move.getY1()].moveTo(cells[move.getX2()][move.getY2()]);
		}
	}

	@Override
	public void onOwnerChanged(final Player player, final int x, final int y) {
		cells[x][y].setOwner(player);
		cellBGs[x][y].changeOwner(player);
	}

	@Override
	public void onCellCleared(int x, int y) {
		cells[x][y].clear();

	}

	// draw the grid.
	@Override
	public void draw(Graphics2D g2d) {
		final int fieldWidth = getField().getWidth();
		final int fieldHeight = getField().getHeight();

		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.gray);

		// draw vertical lines (coarse)
		for (int i = 0; i <= (fieldWidth); i++) {
			g2d.drawLine((2 * i * CELL_SIZE), 0, (2 * i * CELL_SIZE), 2 * fieldHeight * CELL_SIZE);
		}
		// draw horizontal lines (coarse)
		for (int i = 0; i <= (fieldHeight); i++) {
			g2d.drawLine(0, (2 * i * CELL_SIZE), 2 * fieldWidth * CELL_SIZE, (2 * i * CELL_SIZE));
		}
	}

	@Override
	public void onMove(Player p, int x, int y) {
		moveAnim = new UIMoveAnim(x, y, p);
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

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		xRoot = (getSize().getWidth() - ((getField().getWidth() * 2 * CELL_SIZE) + 3)) / 2;
		yRoot = (getSize().getHeight() - ((getField().getHeight() * 2 * CELL_SIZE) + 3)) / 2;
		if (xRoot < 0) {
			xRoot = 0;
		}
		if (yRoot < 0) {
			yRoot = 0;
		}

		g2d.translate(xRoot, yRoot);

		// draw backgrounds
		drawCells(g2d, cellBGs);

		// draw atoms
		drawCells(g2d, cells);

		// draw grid
		draw(g2d);
		
		// draw move anim
		drawMoveAnim(g2d);
		
		// draw 'player has won'
		if(won != null && !won.isFinished()) {
			won.draw(g2d);
		}
		else {
			won = null;
		}
		
		// draw 'new game'
		if(newGame != null && !newGame.isFinished()) {
			newGame.draw(g2d);
		}
		else {
			newGame = null;
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private void drawMoveAnim(final Graphics2D g2d) {
		if(moveAnim != null) {
			if(moveAnim.isFinished()) {
				leaveMoveAnim = new UILeaveAnim(moveAnim, 0);
				moveAnim = null;
			}
			else {
				moveAnim.draw(g2d);
			}
		}
		if(leaveMoveAnim != null) {
			if(leaveMoveAnim.isFinished()) {
				leaveMoveAnim = null;
			}
			else {
				leaveMoveAnim.draw(g2d);
			}
		}
	}

	private void drawCells(final Graphics2D g2d, final UIDrawable[][] drawables) {
		for (final UIDrawable[] drawableRow : drawables) {
			for (final UIDrawable drawable : drawableRow) {
				if (drawable != null) {
					drawable.draw(g2d);
				}
			}
		}
	}
	
	public void setWon(final Player player) {
		won = new UIEnterAnim(new UIImgDrawable(winImgs.get(player), 
				(getField().getWidth() * 2 * CELL_SIZE), 
				(getField().getHeight() * 2 * CELL_SIZE)), 0);
	}
	
	public void setNewGame(Game game) {
		if(won != null) {
			won = new UILeaveAnim(won, 0);
		}
		final BufferedImage newGameTextImg = createTextImgWithSubtitle(new RetroFont(), 
				Color.WHITE, 
				"New Game", 
				getPlayerName(game, Player.FIRST) + " vs. " + getPlayerName(game, Player.SECOND));
		newGame = new UILeaveAnim(new UIEnterAnim(new UIImgDrawable(
				newGameTextImg, 
				(getField().getWidth() * 2 * CELL_SIZE), 
				(getField().getHeight() * 2 * CELL_SIZE)), 0), 2500);
	}
	
	private String getPlayerName(Game game, Player player) {
		final PlayerStatus playerStatus = game.getPlayerStatus(player);
		return playerStatus.isAIPlayer() ? playerStatus.getAI().getName() : "Human";
	}

	@Override
	public void run() {

		while (true) {
			long beforeTime, timeDiff, sleep;

			beforeTime = System.currentTimeMillis();
			
			while (true) {
				repaint();
				
				timeDiff = System.currentTimeMillis() - beforeTime;
				sleep = DELAY - timeDiff;

				if (sleep >= 2) {
					try {
						Thread.sleep(sleep);
					}
					catch (InterruptedException e) {
						System.err.println("interrupted");
					}
				}

				beforeTime = System.currentTimeMillis();
			}
		}
	}
}
