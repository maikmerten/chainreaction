package de.freewarepoint.cr.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.retrofont.RetroFont;


public class RetroListCellRenderer extends JLabel implements ListCellRenderer<AI> {

	private static final long serialVersionUID = -765971287802452365L;
	private final RetroFont retroFont;
	
	public RetroListCellRenderer() {
		retroFont = new RetroFont();
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends AI> list, AI value, int index,
			boolean isSelected, boolean cellHasFocus) {
		setOpaque(true);
		final Color color;
		if(isSelected) {
			setBackground(Color.WHITE);
			setForeground(Color.BLACK);
			color = Color.BLACK;
		}
		else {
			setBackground(Color.BLACK);
			setForeground(Color.WHITE);
			color = Color.WHITE;
		}
		
		this.setIcon(new ImageIcon(retroFont.getRetroString(value.getName(), color, 16)));
		
		return this;
	}
}
