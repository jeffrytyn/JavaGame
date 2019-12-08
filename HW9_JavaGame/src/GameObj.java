

public abstract class GameObj {
	
	private int px;
	private int py;
	private int width;
	private int height;
	
	private int health;
	private int attack;
	
	private int maxX;
	private int maxY;
	
	private int velocity;
	
	public GameObj(int health, int attack, int px, int py, int v, int width, int height, 
			int courtWidth, int courtHeight) {
		this.health = health;
		this.attack = attack;
		this.px = px;
		this.py = py;
		this.velocity = v;
		this.width = width;
		this.height = height;
		this.maxX = courtWidth - width;
		this.maxY = courtHeight - height;
	}

	public int getPx() {
		return px;
	}

	public void setPx(int px) {
		this.px = px;
		clip();
	}

	public int getPy() {
		return py;
	}

	public void setPy(int py) {
		this.py = py;
		clip();
	}
	
	public int getVel() {
		return velocity;
	}
	
	public void setVel(int n) {
		this.velocity = n;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getAttack() {
		return attack;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public void setMaxX(int x) {
		maxX = x;
	}

	public int getMaxY() {
		return maxY;
	}
	
	public void setMaxY(int y) {
		maxY = y;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}

    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }
    
    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px
            && this.py + this.height >= that.py
            && that.px + that.width >= this.px 
            && that.py + that.height >= this.py);
    }
    
    public boolean willIntersect(GameObj that) {
        int thisNext = this.px + this.velocity;
        int thatNext = that.py + that.velocity;
    
        return (thisNext + this.width >= thatNext
            && thatNext + that.width >= thisNext);
    }
    
    public void moveY() {
		setPy(getPy() + getVel());
		clip();
	}
    
    public void moveX() {
 		setPx(getPx() + getVel());
 		clip();
 	}
	
    
}
