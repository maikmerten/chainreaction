package de.maikmerten.chainreaction.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author maik
 */
public class UISettings extends JPanel {
	
	private JCheckBox checkBoxAI;

    public UISettings(final UIGame game) {
		super();
		this.setMinimumSize(new Dimension(640, 30));
		this.setLayout(new GridLayout(1, 1));
		
		checkBoxAI = new JCheckBox("Play against AI");
		this.add(checkBoxAI);

        JButton restartButton = new JButton("New Game");
        this.add(restartButton);

        ActionListener al = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                game.startNewGame();
            }
        };

        restartButton.addActionListener(al);
	}
	
	public boolean againstAI() {
		return checkBoxAI.isSelected();
	}
	
	
}
