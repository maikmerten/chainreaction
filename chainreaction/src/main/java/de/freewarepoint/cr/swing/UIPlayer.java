package de.freewarepoint.cr.swing;

import java.awt.Color;
import java.util.EnumMap;
import java.util.Map;

import de.freewarepoint.cr.Player;

public class UIPlayer {
//	private final static Properties props;
	private final static Map<Player, UIPlayer> players;
	private final static Map<Player, Color> bgColors;
	private final static Map<Player, Color> fgColors;
	
	static {
//		props = new Properties();
//		try {
//			props.load(UIPlayer.class.getResourceAsStream("/player.properties"));
//		}
//		catch (IOException e) {
//			System.err.println("could not load player properties");
//		}
		players = new EnumMap<>(Player.class);
		
		bgColors = new EnumMap<>(Player.class);
		bgColors.put(Player.FIRST, new Color(21, 39, 99));
		bgColors.put(Player.SECOND, new Color(120, 0, 0));
		bgColors.put(Player.NONE, Color.BLACK);
		
		fgColors = new EnumMap<>(Player.class);
		fgColors.put(Player.FIRST, new Color(111, 129, 189));
		fgColors.put(Player.SECOND, new Color(210, 90, 90));
		fgColors.put(Player.NONE, Color.WHITE);
	}
	
	private final Player player;
	
	private UIPlayer(Player player) {
		this.player = player;
	}

	public UIAtom createAtom(int x, int y, int width, int height, int pos, long delay) {
		switch(player) {
			case FIRST:
			case SECOND:
				return new UIAtom("/atom/atom.properties", x, y, width, height, pos, delay, player);
			case NONE:
				throw new UnsupportedOperationException("You cannot create an atom for no player");
			default:
				throw new IllegalStateException("This should never happen");
		}
	}
	
	public Color getBackground() {
		return bgColors.get(player);
	}
	
	public Color getForeground() {
		return fgColors.get(player);
	}
	
	public static UIPlayer getPlayer(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new UIPlayer(player));
		}
		return players.get(player);
	}
}
