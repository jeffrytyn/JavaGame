import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("serial")
public class GameCanvas extends JPanel {
	
	private Hero hero;
	private List<Blaster> heroAttacks;
	private List<Dino> dinos;
	private List<Blaster> dinoAttacks;
	private List<Blaster> cloneHAttacks;
	private List<Dino> cloneDinos;
	private List<Blaster> cloneDinoAttacks;
	private Shield shield = null;
	private int shieldHealth = 15;
	
	private Color heroColor = Color.BLUE;
	private Color dinoColor = Color.RED;
	
	private JLabel stats;
	private JLabel records;
	private Integer level;
	private Integer score;
	private Integer killcount;
	private int attack_chance;
	
	private Integer HLevel;
	private Integer HScore;
	private Integer HKillcount;
	
	private static final String FILEPATH = "files/records.txt";
	private static final String SAVES = "files/saves.txt";
	
	public static final int INTERVAL = 35;
	
	private JLabel status;
	private boolean playing = false;
	
	private static final int CANVAS_WIDTH = 500;
	private static final int CANVAS_HEIGHT = 200;
	private static final int HERO_VEL = 8;
	private static final int DINO_HEALTH = 50;
	private static final int HEALTH_UP = 3;
	private static final int SCORE_UP = 100;
	private static final int SCORE_BOSS = 300;

	public GameCanvas(JLabel stats, JLabel status) {
		File f1 = new File(FILEPATH);
		if(!f1.exists()) {
			try {
				f1.createNewFile();
			} catch (IOException e1) {
				System.out.println("Error creating text file for records");
				e1.printStackTrace();
			}
		}
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(FILEPATH));
			String line = reader.readLine();
			if(line != null) {
				HLevel = Integer.parseInt(line);
				HScore = Integer.parseInt(reader.readLine());
				HKillcount = Integer.parseInt(reader.readLine());
			}else {
				HLevel = 0;
				HScore = 0;
				HKillcount = 0;
				newRecords(HLevel, HScore, HKillcount);
			}
		} catch (FileNotFoundException e3) {
			System.out.println("Error creating reader for records, file not found");
			e3.printStackTrace();
		}catch(NumberFormatException n) {
			System.out.println("Non-numerical data in records, wiping data");
			HLevel = 0;
			HScore = 0;
			HKillcount = 0;
			newRecords(HLevel, HScore, HKillcount);
		}
		catch (IOException e2) {
			System.out.println("Error reading records file");
			e2.printStackTrace();
		}
		
		File f = new File(SAVES);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				System.out.println("Error creating text file for saves");
				e1.printStackTrace();
			}
		}

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            @Override
			public void keyPressed(KeyEvent e) {
            	if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    hero.setVel(HERO_VEL);
                    if(shield != null) {
                    	shield.setVel(HERO_VEL);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    hero.setVel(-HERO_VEL);
                    if(shield != null) {
                    	shield.setVel(-HERO_VEL);
                    }
                }else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                	Blaster b = new Blaster(CANVAS_WIDTH, CANVAS_HEIGHT, 
                	hero.getPx() + hero.getWidth(), hero.getPy() + hero.getHeight()/2, heroColor);
                	heroAttacks.add(b);
                	cloneHAttacks.add(b);
                }else if(e.getKeyCode() == KeyEvent.VK_B) {
                	if(shield == null && score >= Shield.COST) {
                		Shield s = new Shield(shieldHealth, hero.getPx() + hero.getWidth()+1, 
                			hero.getPy(), 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                		shield = s;
                		score -= Shield.COST;
                	}
                }
            }

            @Override
			public void keyReleased(KeyEvent e) {
                hero.setVel(0);
                if(shield != null) {
                	shield.setVel(0);
                }
            }
        });
        
        this.stats = stats;
        this.status = status;
        this.setRecords(new JLabel("Highest Level: " + HLevel +"  High Score: " + 
				HScore + "  Most Kills: " + HKillcount));
    }
	
	public void generateDinos(int level) {
		int px = CANVAS_WIDTH - 50;
		int inc = (CANVAS_HEIGHT - (3*50))/4;
		dinos = new ArrayList<Dino>();
		cloneDinos = new ArrayList<Dino>();
		if(level%5 != 0) {
			Dino d1 = new Dino(DINO_HEALTH + level*HEALTH_UP, px, inc, 
					CANVAS_WIDTH, CANVAS_HEIGHT,false);
			Dino d2 = new Dino(DINO_HEALTH + level*HEALTH_UP, px, inc+ d1.getHeight() + inc, 
					CANVAS_WIDTH, CANVAS_HEIGHT,false);
			Dino d3 = new Dino(DINO_HEALTH + level*HEALTH_UP, px, inc + 2*(d1.getHeight() + inc), 
					CANVAS_WIDTH, CANVAS_HEIGHT,false);
			dinos.add(d1);
			cloneDinos.add(d1);
			dinos.add(d2);
			cloneDinos.add(d2);
			dinos.add(d3);
			cloneDinos.add(d3);
		}
		else {
			Dino boss = new Dino(level / 5 * DINO_HEALTH, px, CANVAS_HEIGHT/2 - 25, 
					CANVAS_WIDTH, CANVAS_HEIGHT, true);
			boss.setAttack(boss.getAttack() * level/3);
			dinos.add(boss);
			cloneDinos.add(boss);
		}
	}
	
	public void hitDino(Blaster b) {
    	for(Dino d: dinos) {
    		if (b.intersects(d)) { 
    			d.setHealth(d.getHealth()-hero.getAttack());
    			if(d.getHealth() <= 0) {
    				cloneDinos.remove(d);
    				killcount ++;
    				if(d.isBoss()) {
    					setScore(getScore() + SCORE_BOSS);
    				}else setScore(getScore() + SCORE_UP);
    			}
    			cloneHAttacks.remove(b);
    		}
    	}

	}
	
	public void hitHero(Blaster b) {
		int damage = dinos.get(0).getAttack();
		if(shield != null && b.intersects(shield)) {
			shield.setHealth(shield.getHealth() - damage);
			cloneDinoAttacks.remove(b);
			if(shield.getHealth() <= 0) {
				shield = null;
				shieldHealth = 15;
			}
		}else if (b.intersects(hero)) {
			hero.setHealth(hero.getHealth() - damage);
			cloneDinoAttacks.remove(b);
		}
	}
	
	public <T> List<T> cleanList(List<T> b) {
		List<T> copy = new ArrayList<T>();
		for(T s: b) {
			copy.add(s);
		}
		return copy;
	}
	
	public void tick() {
        if (isPlaying()) {
            hero.moveY();
            
            if(shield != null) {
            	shield.moveY();
            }
            
            int random = (int)(Math.random() * 250 + 1);
        	int size = dinos.size();
            if(random <= attack_chance && size != 0) {
                int random_dino = (int)(Math.random() * size);
                Dino d = dinos.get(random_dino);
                Blaster b = new Blaster(CANVAS_WIDTH, CANVAS_HEIGHT, 
                  	  d.getPx(), d.getPy() + d.getHeight()/2, dinoColor);
                b.setWidth(5);
                b.setHeight(5);
                if(d.isBoss()) {
                	b.setWidth(13);
                	b.setHeight(13);
                }
            	dinoAttacks.add(b);
            	cloneDinoAttacks.add(b);
            }
            
            for(Blaster b: heroAttacks) {
        		b.moveX();
            	if(b.getPx() == b.getMaxX()) {
            		cloneHAttacks.remove(b);
            	}
        		hitDino(b);
            }
            
        	for(Blaster b: dinoAttacks) {
        		b.moveX(-b.getVel());
            	if(b.getPx() <= b.getWidth()) {
            		cloneDinoAttacks.remove(b);
            	}
        		hitHero(b);
        		if(hero.getHealth() <= 0) {
        			setPlaying(false);
        			if(score > HScore) {
        				newRecords(level, score, killcount);
        				getRecords().setText("Highest Level: " + level +"  High Score: " + 
        									  score + "  Most Kills: " + killcount);
        			}
        			status.setText("GAME OVER");
					try {
						BufferedWriter w = new BufferedWriter(new FileWriter(SAVES));
						w.write("");
						w.close();
					} catch (IOException e) {
						System.out.println("Error deleting save after death");
						e.printStackTrace();
					}
        			
        		}
        	}
        	
    		if(cloneDinos.size() == 0) {
    			setLevel(getLevel() + 1);
    			setAttack_chance(getAttack_chance() +1);
    			cloneHAttacks = new ArrayList<Blaster>();
    			generateDinos(level);
    		}
        	
        	dinos = cleanList(cloneDinos);
        	heroAttacks = cleanList(cloneHAttacks);
        	dinoAttacks = cleanList(cloneDinoAttacks);
            // update the display
            stats.setText("Level: " + level + "  Score: " + score + "  Killcount: " + killcount);
            repaint();
        }
    }
	
	public void reset() {
		hero = new Hero(CANVAS_WIDTH, CANVAS_HEIGHT);
		shield = null;
		dinos = new ArrayList<Dino>();
		cloneDinos = new ArrayList<Dino>();
		heroAttacks = new ArrayList<Blaster>();
		cloneHAttacks = new ArrayList<Blaster>();
		dinoAttacks = new ArrayList<Blaster>();
		cloneDinoAttacks = new ArrayList<Blaster>();
		
        setPlaying(true);
        level = 1;
        score = 0;
        killcount = 0;
        attack_chance = 4;
        stats.setText("Level: 0  Score: 0  Killcount: 0");
        status.setText("Running...");
        generateDinos(level);

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
	}
	
	public void newRecords(Integer level, Integer score, Integer killcount) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH));
			writer.write(level.toString());
			writer.newLine();
			writer.write(score.toString());
			writer.newLine();
			writer.write(killcount.toString());
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.out.println("Error writing new records");
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(SAVES));
			writer.write(level.toString());
			writer.newLine();
			writer.write(score.toString());
			writer.newLine();
			writer.write(killcount.toString());
			writer.newLine();
			Integer health = hero.getHealth();
			writer.write(health.toString());
			writer.newLine();
			Integer chance = attack_chance;
			writer.write(chance.toString());
			for(Dino d: cloneDinos) {
				Integer h = d.getHealth();
				Integer px = d.getPx();
				Integer py = d.getPy();
				writer.newLine();
				writer.write(h.toString());
				writer.newLine();
				writer.write(px.toString());
				writer.newLine();
				writer.write(py.toString());
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Error saving game");
			e.printStackTrace();
		}
	}
	
	/* Invariant that the file will always have at least 3 lines, and if there are more it means
	 * the player must have saved and there are exactly 7 lines.
	 */
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(SAVES));
			String line = reader.readLine();
			if(line != null) {
				this.setLevel(Integer.parseInt(line));
				this.setScore(Integer.parseInt(reader.readLine()));
				this.setKillcount(Integer.parseInt(reader.readLine()));
				Hero hero = new Hero(CANVAS_WIDTH, CANVAS_HEIGHT);
				hero.setHealth(Integer.parseInt(reader.readLine()));
				this.setHero(hero);
				this.setShield(null);
				this.setAttack_chance(Integer.parseInt(reader.readLine()));
				this.setHeroAttacks(new ArrayList<Blaster>());
				this.setDinoAttacks(new ArrayList<Blaster>());
				this.setDinos(new ArrayList<Dino>());
				line = reader.readLine();
				while(line != null) {
					int h = Integer.parseInt(line);
					int px = Integer.parseInt(reader.readLine());
					int py = Integer.parseInt(reader.readLine());
					Dino d1 = new Dino(h, px, py, 
								CANVAS_WIDTH, CANVAS_HEIGHT, this.getLevel() % 5 == 0);
					cloneDinos.add(d1);
					line = reader.readLine();
				}
				reader.close();
				this.setDinos(cloneDinos);
				this.stats.setText("Level: " + 
						level + "  Score: " + score + "  Killcount: " + killcount);
				this.status.setText("Running...");
				this.setPlaying(true);
				
				requestFocusInWindow();
			}else {
				reset();
			}
		}catch (NumberFormatException e) {
			System.out.println("Non-numeric data present, wiping and resetting game");
			try {
				BufferedWriter w = new BufferedWriter(new FileWriter(SAVES));
				w.write("");
				w.close();
			} catch (IOException e1) {
				System.out.println("Error wiping data after non numeric data found");
				e1.printStackTrace();
			}finally {
				reset();
			}
		}catch(NoSuchElementException e) {
			System.out.println("Missing data from previous save, resetting game");
			BufferedWriter w;
			try {
				w = new BufferedWriter(new FileWriter(SAVES));
				w.write("");
				w.close();
			} catch (IOException e1) {
				System.out.println("Error wiping data after missing save data");
				e1.printStackTrace();
			}finally {
				reset();
			}
		}catch (IOException e) {
			System.out.println("Error with loading game, resetting game");
			BufferedWriter w;
			try {
				w = new BufferedWriter(new FileWriter(SAVES));
				w.write("");
				w.close();
			} catch (IOException e1) {
				System.out.println("Error wiping data after error loading game");
				e1.printStackTrace();
			}finally {
				reset();
			}
		}
	}
	
    public int getShieldHealth() {
		return shieldHealth;
	}

	public void setShieldHealth(int shieldHealth) {
		this.shieldHealth = shieldHealth;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public List<Blaster> getHeroAttacks() {
		return heroAttacks;
	}

	public void setHeroAttacks(List<Blaster> heroAttacks) {
		this.cloneHAttacks = heroAttacks;
		List<Blaster> a = new ArrayList<Blaster>();
		for(Blaster b: heroAttacks) {
			a.add(b);
		}
		this.heroAttacks = a;
	}

	public List<Dino> getDinos() {
		return dinos;
	}

	public void setDinos(List<Dino> dinos) {
		this.cloneDinos = dinos;
		List<Dino> a = new ArrayList<Dino>();
		for(Dino d: dinos) {
			a.add(d);
		}
		this.dinos = a;
	}

	public List<Blaster> getDinoAttacks() {
		return dinoAttacks;
	}

	public void setDinoAttacks(List<Blaster> dinoAttacks) {
		this.cloneDinoAttacks = dinoAttacks;
		List<Blaster> a = new ArrayList<Blaster>();
		for(Blaster b: dinoAttacks) {
			a.add(b);
		}
		this.dinoAttacks = a;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getKillcount() {
		return killcount;
	}

	public void setKillcount(Integer killcount) {
		this.killcount = killcount;
	}

	public int getAttack_chance() {
		return attack_chance;
	}

	public void setAttack_chance(int attack_chance) {
		this.attack_chance = attack_chance;
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        hero.draw(g);
        if(shield != null) {
        	shield.draw(g);
        }
        for(Blaster b: heroAttacks) {
        	b.draw(g);
        }
        for(Dino d: dinos) {
        	if (d.isBoss()) {
        		d.draw(g, getLevel() / 5 * DINO_HEALTH);
        	}else d.draw(g, DINO_HEALTH + getLevel()*HEALTH_UP);
        }
        for(Blaster b: dinoAttacks){
        	b.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT);
    }

	public JLabel getRecords() {
		return records;
	}

	public void setRecords(JLabel records) {
		this.records = records;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
}
