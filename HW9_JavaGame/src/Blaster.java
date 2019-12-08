import java.awt.*;

public class Blaster extends GameObj{
	
	public final static int VEL = 10;
	public final static int HEALTH = 0;
	public final static int ATTACK = 0;
	public final static int WIDTH = 3;
	
	private Color color;
	
	public Blaster(int courtWidth, int courtHeight, int px, int py, Color color) {
		super(HEALTH, ATTACK, px, py, VEL, WIDTH, WIDTH, courtWidth, courtHeight);
	    this.color = color;
	}
	
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public Color getColor() {
		return color;
	}


	public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
	}
	
	@Override
	public void moveX() {
		setPx(getPx() + getVel());
	}
	
	public void moveX(int v) {
		setPx(getPx() + v);
	}
	
	
}
