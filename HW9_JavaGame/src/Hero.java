import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Hero extends GameObj{
	
	public static final int HEALTH = 100;
	public static final int ATTACK = 20;
    public static final int INIT_POS_X = 0;
    public static final int INIT_POS_Y = 0;
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    public static final int INIT_VEL = 0;
    
    public static final String IMG_FILE = "files/hero.png";
    private static BufferedImage img;
	
	public Hero(int courtWidth, int courtHeight) {
		super(HEALTH, ATTACK, INIT_POS_X, INIT_POS_Y, INIT_VEL, WIDTH, HEIGHT, 
				courtWidth, courtHeight);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
	}


	public void draw(Graphics g) {
		g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
		g.drawRect(getPx(), getPy() + getHeight()+1, HEALTH/2, 5);
		g.setColor(Color.GREEN);
		g.fillRect(getPx(), getPy() + getHeight() + 2, getHealth()/2 , 4);
		g.setColor(Color.BLACK);
	}
}
