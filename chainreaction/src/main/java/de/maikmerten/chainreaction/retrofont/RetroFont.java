package de.maikmerten.chainreaction.retrofont;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author maik
 */
public class RetroFont {
	
	private static final int MINIMAL_FONT_SIZE = 8;
	BufferedImage font;
	
	public RetroFont() {
		try {
			BufferedImage fontimg = ImageIO.read(this.getClass().getResourceAsStream("/retrofont.png"));
			font = new BufferedImage(fontimg.getWidth(), fontimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
			font.getGraphics().drawImage(fontimg, 0, 0, null);
			
			for(int x = 0; x < font.getWidth(); ++x) {
				for(int y = 0; y < font.getHeight(); ++y) {
					// punch all black pixels transparent
					if(font.getRGB(x, y) == (0xFF << 24)) {
						font.setRGB(x, y, 0);
					}
				}
			}
		
		} catch (IOException ex) {
			throw new RuntimeException("cannot read retro font", ex);
		}
	}
	
	
	public BufferedImage getRetroChar(char c, Color color) {
		int code = (int)c;
		if(code < 0 || c > 255) {
			code = (int)'?';
		}
		
		int row = code >> 4;
		int col = code % 16;
		
		BufferedImage result = font.getSubimage(col << 3, row << 3, 8, 8);
		for(int x = 0; x < result.getWidth(); ++x) {
			for(int y = 0; y < result.getHeight(); ++y) {
				// colorize white pixels
				if(result.getRGB(x, y) == 0xFFFFFFFF) {
					result.setRGB(x, y, color.getRGB());
				}
			}
		}
		
		
		return result;
	}
	
	public BufferedImage getRetroChar(char c, Color color, int fontSize) {
		fontSize = fontSize < MINIMAL_FONT_SIZE ? MINIMAL_FONT_SIZE : fontSize;
		
		final Image img = getRetroChar(c, color).getScaledInstance(fontSize, fontSize, Image.SCALE_REPLICATE);
		final BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), font.getType());
		bimg.getGraphics().drawImage(img, 0, 0, null);
		return bimg;
	}
	
	public BufferedImage getRetroString(String str, Color color, int fontSize) {
		fontSize = fontSize < MINIMAL_FONT_SIZE ? MINIMAL_FONT_SIZE : fontSize;
		final BufferedImage bimg = new BufferedImage(fontSize * str.length(), fontSize, font.getType());
		int i = 0;
		for(final char c : str.toCharArray()) {
			final Image img = getRetroChar(c, color, fontSize);
			bimg.getGraphics().drawImage(img, i*fontSize, 0, null);
			i++;
		}
		return bimg;
	}
	
	// TODO move to unit test
	public static void main(String[] args) throws Exception {
		RetroFont rf = new RetroFont();
		ImageIO.write(rf.getRetroChar('A', Color.GREEN, 48), "png", new File("/tmp/test.png"));
		ImageIO.write(rf.getRetroString("Hello World", Color.GREEN, 48), "png", new File("/tmp/hello world.png"));
	}
	
	
}
