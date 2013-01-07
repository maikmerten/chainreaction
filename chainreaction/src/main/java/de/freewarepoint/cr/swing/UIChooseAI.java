package de.freewarepoint.cr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.NoneAI;
import de.freewarepoint.cr.ai.StandardAI;
import de.freewarepoint.retrofont.RetroFont;

/**
 *
 * @author maik
 */
public class UIChooseAI extends JPanel {
	
	private static final long serialVersionUID = -4840442627860470783L;
	private final RetroFont retroFont;
	private final JList<AI> aiList;

    public UIChooseAI(final UIGame uigame) {
		super();
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		
		final BorderLayout layout = new BorderLayout(5, 5);
		layout.setHgap(16);
		
		this.setLayout(layout);
		
		retroFont = new RetroFont();
		
        final JButton chooseButton = new JButton();
        chooseButton.setBorderPainted(false);
        chooseButton.setContentAreaFilled(false);
        chooseButton.setIcon(new ImageIcon(retroFont.getRetroString("OK", Color.WHITE, 32)));
        chooseButton.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                final AI ai = aiList.getSelectedValue();
                if(ai == null || ai.getName().equals(new NoneAI().getName())) {
                	uigame.chooseAI(null);
                }
                else {
                	uigame.chooseAI(ai);
                }
            }
        });
        this.add(chooseButton, BorderLayout.SOUTH);
        
        final Vector<AI> ais = new Vector<>();
        ais.add(new NoneAI());
        ais.add(new StandardAI());
        ais.addAll(SettingsLoader.loadAIs());
        
        aiList = new JList<>(ais);
        aiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aiList.setCellRenderer(new RetroListCellRenderer());
        aiList.setBackground(Color.BLACK);
        aiList.setForeground(Color.WHITE);
        
        final JScrollPane scrollPane = new JScrollPane(aiList);
        scrollPane.setViewportView(aiList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
	}
}
