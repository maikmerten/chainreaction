package de.freewarepoint.cr.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		
		final GridLayout gridLayout = new GridLayout(1, 4);
		gridLayout.setHgap(16);
		
		this.setLayout(gridLayout);
		
		final RetroFont retroFont = new RetroFont();
		final JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(retroFont.getRetroString("ChainReaction", Color.WHITE, 32)));
		this.add(logo);

        final JButton restartButton = new JButton();
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
		restartButton.setIcon(new ImageIcon(retroFont.getRetroString("New Game", Color.WHITE, 32)));
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                game.startNewGame();
            }
        });
        this.add(restartButton);
        
        final JButton chooseAIButton = new JButton();
        chooseAIButton.setBorderPainted(false);
        chooseAIButton.setContentAreaFilled(false);
        chooseAIButton.setIcon(new ImageIcon(retroFont.getRetroString("Choose AI", Color.WHITE, 32)));
        chooseAIButton.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
            	game.showChooseAI();
            }
        });
        this.add(chooseAIButton);
        
        final JButton exitButton = new JButton();
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setIcon(new ImageIcon(retroFont.getRetroString("Exit", Color.WHITE, 32)));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                game.dispatchEvent(new WindowEvent(game, WindowEvent.WINDOW_CLOSING));
            }
        });
        this.add(exitButton);
	}
}
