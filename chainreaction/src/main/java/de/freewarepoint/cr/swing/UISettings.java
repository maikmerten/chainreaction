package de.freewarepoint.cr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.retrofont.RetroFont;

/**
 *
 * @author maik
 * @author jonny
 */
public class UISettings extends JPanel implements Runnable {
	
	private static final long serialVersionUID = -4840442627860470783L;
	private final JButton chooseAI1Button;
	private final JButton chooseAI2Button;
	
	private Game game;
	private final RetroFont retroFont;

    public UISettings(final UIGame game) {
		super();
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		final BorderLayout borderLayout = new BorderLayout(16, 16);
		
		this.setLayout(borderLayout);
		
		retroFont = new RetroFont();
		
		final JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.BLACK);
		logoPanel.setDoubleBuffered(true);
		logoPanel.setLayout(new BorderLayout());
		
		{
			final JLabel logo = new JLabel();
			logo.setIcon(new ImageIcon(retroFont.getRetroString("ChainReaction", Color.WHITE, 32)));
			logoPanel.add(logo);
		}
		
		this.add(logoPanel, BorderLayout.WEST);
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setDoubleBuffered(true);
		final GridLayout gridLayout = new GridLayout(2, 2);
		gridLayout.setHgap(16);
		buttonPanel.setLayout(gridLayout);
		
		{
			chooseAI1Button = new JButton();
	        chooseAI1Button.setBorderPainted(false);
	        chooseAI1Button.setFocusPainted(false);
	        chooseAI1Button.setContentAreaFilled(false);
	        final String str = "Player 1 (Human)";
			chooseAI1Button.setIcon(new ImageIcon(retroFont.getRetroString(str , UIPlayer.getPlayer(Player.FIRST).getForeground(), 16)));
	        chooseAI1Button.setPressedIcon(new ImageIcon(retroFont.getRetroString(str, Color.BLACK, UIPlayer.getPlayer(Player.FIRST).getForeground(), 16)));
	        chooseAI1Button.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	            	game.showChooseAI(Player.FIRST);
	            }
	        });
	        buttonPanel.add(chooseAI1Button);
		}
		
		{
			final JButton restartButton = new JButton();
	        restartButton.setBorderPainted(false);
	        restartButton.setFocusPainted(false);
	        restartButton.setContentAreaFilled(false);
			final String str = "New Game";
			restartButton.setIcon(new ImageIcon(retroFont.getRetroString(str, Color.WHITE, 16)));
			restartButton.setPressedIcon(new ImageIcon(retroFont.getRetroString(str, Color.BLACK, Color.WHITE, 16)));
	        restartButton.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	                game.startNewGame();
	            }
	        });
	        buttonPanel.add(restartButton);
		}
		
		{
			chooseAI2Button = new JButton();
	        chooseAI2Button.setBorderPainted(false);
	        chooseAI2Button.setFocusPainted(false);
	        chooseAI2Button.setContentAreaFilled(false);
	        final String str = "Player 2 (Human)";
			chooseAI2Button.setIcon(new ImageIcon(retroFont.getRetroString(str, UIPlayer.getPlayer(Player.SECOND).getForeground(), 16)));
			chooseAI2Button.setPressedIcon(new ImageIcon(retroFont.getRetroString(str, Color.BLACK, UIPlayer.getPlayer(Player.SECOND).getForeground(), 16)));
	        chooseAI2Button.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	            	game.showChooseAI(Player.SECOND);
	            }
	        });
	        buttonPanel.add(chooseAI2Button);
		}

		{
			final JButton exitButton = new JButton();
	        exitButton.setBorderPainted(false);
	        exitButton.setFocusPainted(false);
	        exitButton.setContentAreaFilled(false);
	        final String str = "Exit";
			exitButton.setIcon(new ImageIcon(retroFont.getRetroString(str, Color.WHITE, 16)));
	        exitButton.setPressedIcon(new ImageIcon(retroFont.getRetroString(str, Color.BLACK, Color.WHITE, 16)));
	        exitButton.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	                game.dispatchEvent(new WindowEvent(game, WindowEvent.WINDOW_CLOSING));
	            }
	        });
	        buttonPanel.add(exitButton);
		}
		
		this.add(buttonPanel, BorderLayout.CENTER);
	}
    
    public void setGame(Game game) {
    	this.game = game;
    }
    
    // This runnable should be used in the SwingEventQueue, only!
 	@Override
 	public void run() {
 		if(game != null) {
 			chooseName(Player.FIRST, chooseAI1Button);
 			chooseName(Player.SECOND, chooseAI2Button);
 		}
 	}

	private void chooseName(final Player player, JButton button) {
		final String name = game.getPlayerStatus(player).isAIPlayer() ? game.getPlayerStatus(player).getAI().getName() : "Human";
		final String str = "Player " + (player.ordinal() + 1) +  " (" + name + ")";
		button.setIcon(
				new ImageIcon(retroFont.getRetroString(str, UIPlayer.getPlayer(player).getForeground(), 16)));
		button.setPressedIcon(
				new ImageIcon(retroFont.getRetroString(str, Color.BLACK, UIPlayer.getPlayer(player).getForeground(), 16)));
	}
}
