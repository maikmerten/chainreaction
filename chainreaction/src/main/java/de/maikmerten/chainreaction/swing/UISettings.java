package de.maikmerten.chainreaction.swing;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author maik
 */
public class UISettings extends JPanel {
	
	private JCheckBox checkBoxAI;
	
	public UISettings() {
		super();
		this.setMinimumSize(new Dimension(640, 30));
		this.setLayout(new GridLayout(1, 1));
		
		checkBoxAI = new JCheckBox("Play against AI");
		this.add(checkBoxAI);
	}
	
	public boolean againstAI() {
		return checkBoxAI.isSelected();
	}
	
	
}
