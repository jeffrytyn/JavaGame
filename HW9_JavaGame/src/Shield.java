import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Shield extends GameObj{
	public static final int ATTACK = 0;
	public static final int WIDTH = 6;
	public static final int HEIGHT = 40;
	private BufferedImage canvas;
	
	private Color[][] pixels;
	public static final int COST = 400;
	
	public Shield(int health, int px, int py, int v, int courtWidth,
			int courtHeight) {
		super(health, ATTACK, px, py, v, WIDTH, HEIGHT, courtWidth, courtHeight);
		
		pixels = new Color[WIDTH][HEIGHT];
		for(int row = 0; row < WIDTH; row++) {
			for(int col = 0; col < HEIGHT; col++) {
				pixels[row][col] = Color.MAGENTA;
			}
		}
		canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void draw(Graphics g) {
		for(int row = 0; row < WIDTH; row++) {
			for(int col = 0; col < HEIGHT; col++) {
				Color c = pixels[row][col];
				int color = c.getRGB();
				if(this.getHealth() <= 7) {
					if(col == row+6 || col == 30 - row) {
					c = Color.BLACK;
					color = c.getRGB();
					}
				}
				canvas.setRGB(row, col, color);
			}
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(canvas, getPx(), getPy(), null);
		
	}
}
