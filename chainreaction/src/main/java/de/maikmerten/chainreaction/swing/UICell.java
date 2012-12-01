package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Field;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author maik
 */
public class UICell extends JButton implements ActionListener {

	private UIField uifield;
	private int x, y;

	public UICell(UIField uifield, Field field, int x, int y) {
		super();
		this.uifield = uifield;
		this.x = x;
		this.y = y;
		this.addActionListener(this);
	}

	public void updateCell() {
		Field field = uifield.getField();
		String text = "";
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		int count = field.getAtoms(x, y);
		if (count > 0) {
			for (int i = 0; i < count; ++i) {
				text += "O";
			}
			if (field.getOwner(x, y) == 1) {
				setBackground(Color.RED);
			} else if (field.getOwner(x, y) == 2) {
				setBackground(Color.BLUE);
			}
		}
		this.setText(text);

	}

	public void actionPerformed(ActionEvent e) {
		uifield.makeMove(x, y);
	}
}
