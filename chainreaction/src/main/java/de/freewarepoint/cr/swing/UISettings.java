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

import de.freewarepoint.cr.Player;
import de.freewarepoint.retrofont.RetroFont;

/**
 *
 * @author maik
 * @author jonny
 */
public class UISettings extends JPanel {
	
	private static final long serialVersionUID = -4840442627860470783L;

    public UISettings(final UIGame game) {
		super();
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		final BorderLayout borderLayout = new BorderLayout(16, 16);
		
		this.setLayout(borderLayout);
		
		final RetroFont retroFont = new RetroFont();
		
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
			final JButton restartButton = new JButton();
	        restartButton.setBorderPainted(false);
	        restartButton.setContentAreaFilled(false);
			restartButton.setIcon(new ImageIcon(retroFont.getRetroString("New Game", Color.WHITE, 16)));
	        restartButton.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	                game.startNewGame();
	            }
	        });
	        buttonPanel.add(restartButton);
		}
		
		{
			final JButton exitButton = new JButton();
	        exitButton.setBorderPainted(false);
	        exitButton.setContentAreaFilled(false);
	        exitButton.setIcon(new ImageIcon(retroFont.getRetroString("Exit", Color.WHITE, 16)));
	        exitButton.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	                game.dispatchEvent(new WindowEvent(game, WindowEvent.WINDOW_CLOSING));
	            }
	        });
	        buttonPanel.add(exitButton);
		}
		
		{
			final JButton chooseAI1Button = new JButton();
	        chooseAI1Button.setBorderPainted(false);
	        chooseAI1Button.setContentAreaFilled(false);
	        chooseAI1Button.setIcon(new ImageIcon(retroFont.getRetroString("Choose Player 1", UIPlayer.getPlayer(Player.FIRST).getForeground(), 16)));
	        chooseAI1Button.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	            	game.showChooseAI(Player.FIRST);
	            }
	        });
	        buttonPanel.add(chooseAI1Button);
		}
		
		{
			final JButton chooseAI2Button = new JButton();
	        chooseAI2Button.setBorderPainted(false);
	        chooseAI2Button.setContentAreaFilled(false);
	        chooseAI2Button.setIcon(new ImageIcon(retroFont.getRetroString("Choose Player 2", UIPlayer.getPlayer(Player.SECOND).getForeground(), 16)));
	        chooseAI2Button.addActionListener(new ActionListener() {
	            public void actionPerformed( ActionEvent e ) {
	            	game.showChooseAI(Player.SECOND);
	            }
	        });
	        buttonPanel.add(chooseAI2Button);
		}
		
		this.add(buttonPanel, BorderLayout.CENTER);
	}
}
