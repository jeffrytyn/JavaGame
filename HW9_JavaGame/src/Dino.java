import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Dino extends GameObj {
	
	private boolean boss;
	public static final int INIT_ATTACK = 2;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 50;
    public static final int VEL = 0;
    
    public static final String IMG_FILE = "files/dino.png";
    private static BufferedImage img;
	
	public Dino(int health, int px, int py, int courtWidth, int courtHeight, boolean boss) {
		super(health, INIT_ATTACK, px, py, VEL, WIDTH, HEIGHT, courtWidth, courtHeight);
		this.boss = boss;
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
	}
	
	public boolean isBoss() {
		return boss;
	}

	public void draw(Graphics g, int maxHealth) {
		g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
		g.drawRect(getPx(), getPy() + getHeight()+1, 50, 5);
		g.setColor(Color.GREEN);
		g.fillRect(getPx() + 1, getPy() + getHeight() + 2, getHealth() * 50 / maxHealth, 4);
		g.setColor(Color.BLACK);
	}

}
