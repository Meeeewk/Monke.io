import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class Player extends MovingEntity {
    private int xp = 0;
    private int xpGoal = 200;
    private int xpBarWidth = 640;
    private int energyBarWidth = 480;
    private int healthBarHeight = 610;
    private int frames = 0;
    private double xRoam = Math.random() * 200 - 100;
    private double yRoam = Math.random() * 200 - 100;
    private int jumpingFrames = 0;
    private double dmgIncrease = 1;
    private int dashingFrames = 0;
    private int throwingFrames = 0;
    private ArrayList<Character> currentAbilityKeys = new ArrayList<>();
    private ArrayList<String> currentAbilities = new ArrayList<>();
    private ArrayList<Character> availableAbilityKeys = new ArrayList<>(Arrays.asList(' ', 'd', 'f'));
    private ArrayList<String> availableAbilities = new ArrayList<>(Arrays.asList("JUMP", "DASH", "THROW"));
    public Entity target;
    private boolean showEvoOption = false;
    private GamePanel gamePanel;
    private ActionListener listener;
    private ArrayList<JButton> evoBtns = new ArrayList<JButton>();
    private double viewPOV = 55; // Default value

    public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight, String playerImage, double maxSpeed, double sprintSpeed) {
        super(x, y, width, height, moveSpeedMod, playerImage, drawWidth, drawHeight, maxSpeed, sprintSpeed);
    }

    public Player() {
        this(0.0, 0.0, 400, 400, 60, 100, 100, "elephant.png", Math.sqrt(162), Math.sqrt(243));
//        super.setZ(5);
       this.listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ting = e.getActionCommand();
				
				gamePanel.pause(false);
				showEvoOption = false;
				for (Component cmp : gamePanel.getComponents()) {
					if (cmp instanceof JButton) {
						gamePanel.remove(cmp);
					}
				}
				handleLevelUp(ting);
			}
		};
		for (int i = 0; i < this.availableAbilities.size(); i++) {
			JButton btn = new JButton(this.availableAbilities.get(i));
			btn.setBackground(Color.orange);
			btn.addActionListener(listener);
			this.evoBtns.add(btn);
		}
    }
    
    public Player(GamePanel gamePanel) {
    	this();
    	this.gamePanel = gamePanel;
    	
    }
    
    private void handleLevelUp(String ting) {
    	try {
        	int index = this.availableAbilities.indexOf(ting);
        	this.evoBtns.remove(index);
        	this.currentAbilities.add(ting);
        	this.currentAbilityKeys.add(this.availableAbilityKeys.get(index));
        	this.availableAbilities.remove(index);
        	this.availableAbilityKeys.remove(index);
    	} catch (Exception e) {
    		// If caught then it isnt a new ability, but a stat boost
    		int index = -1;
    		for (JButton btn : this.evoBtns) {
    			if (btn.getActionCommand().equals(ting)) {
    				index = this.evoBtns.indexOf(btn);
    			}
    		}
    		this.evoBtns.remove(index);
    		switch (ting) {
    			case "DMG":
    				this.dmgIncrease += 0.05;
    				break;
    			case "SPEED":
    				this.setMaxSpeed(this.getMaxSpeed() * 1.05);
    				break;
    			case "HEALTH":
    				this.setHealth(this.getHealth() + 15);
    				this.setMaxHealth(this.getMaxHealth() + 15);
    				break;
    			case "ENERGY":
    				this.setMaxSprintEndurance(this.getMaxSprintEndurance() + 45);
    				break;
    			case "HCD":
    				this.setHitCooldown(this.getHitCooldown() + 1);
    				break;
    			case "EVO":
    				this.setSkin(selectRandomSkin(this.getSkin()));
    				try {
    					this.setPlayerImage(ImageIO.read(new File(skins[this.getSkin()])));
    				} catch (IOException ex) {
    					// TODO Auto-generated catch block
    					ex.printStackTrace();
    				}
//    				this.setDrawHeight(drawHeight);
//    				this.setDrawWidth(drawWidth);
    				this.setDrawHeight((int)(this.skinSize[this.getSkin()]+ this.getZ()*10));
    				this.setDrawWidth((int)(this.skinSize[this.getSkin()]+ this.getZ()*10));
    		}
    	}
    }

    @Override
    public void draw(Graphics g, int cursorX, int cursorY) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        double angle = 0;
        double vect_len = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
        if (getyVelocity() * -1 < 0) {
            angle = Math.PI * 2 - Math.acos(this.getxVelocity() / vect_len);
        } else {
            angle = Math.acos(getxVelocity() / vect_len);
        }
        this.setFacingDir(3 * Math.PI / 2 - angle);
        g2d.rotate(this.getFacingDir(), this.getWidth() / 2, this.getHeight() / 2);
        g2d.drawImage(this.getPlayerImage(), this.getWidth() / 2 - this.getDrawWidth() / 2, this.getHeight() / 2 - this.getDrawHeight() / 2, this.getDrawWidth(), this.getDrawHeight(), null);
        g2d.setColor(new Color(255, 0, 0, (int) this.getHitCooldown() * 3));
        g2d.fillOval((int) (this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int) (this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2), (int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
        g2d.setColor(Color.black);
//        g2d.drawString("z: " + this.getZ(), (int) (this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int) (this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2) - 30);
//        g2d.drawString("chunk: " + Arrays.toString(getChunk()), (int) (this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int) (this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2) - 30);
        g2d.setColor(Color.red);
	    g2d.drawString("kill count: "+this.getKillCount(), (int) (this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int) (this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2) - 30);
        this.setHitCooldown(this.getHitCooldown() <= 0 ? 0 : this.getHitCooldown() - 0.5);
        g2d.setTransform(old);
        
        g2d.fillOval((int) (this.getxVelocity() * 10 + getWidth() / 2),(int) (this.getyVelocity() * 10 + getHeight() / 2), 40, 40);
    }

    public void drawUI(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.gray);
        g2d.fillRect((int) (this.getWidth() / 2 - xpBarWidth / 2), 80, (int) (this.xpGoal)  * xpBarWidth / xpGoal, 20);
        g2d.setColor(Color.yellow);
        g2d.fillRect((int) (this.getWidth() / 2 - xpBarWidth / 2), 80, (int) (this.getXp())  * xpBarWidth / xpGoal, 20);
        g2d.setColor(Color.gray);
        g2d.fillRect((int) (this.getWidth() / 2 - energyBarWidth / 2), this.getHeight() - 80, (int) ((this.getMaxSprintEndurance()) * energyBarWidth / this.getMaxSprintEndurance()), 20);
        if (this.getSprintingDisabled() > 0) {
            g2d.setColor(Color.red);
        } else {
            g2d.setColor(Color.orange);
        }
        g2d.fillRect((int) (this.getWidth() / 2 - energyBarWidth / 2), this.getHeight() - 80, (int) ((this.getSprintEndurance()) * energyBarWidth / this.getMaxSprintEndurance()), 20);
        g2d.setColor(Color.gray);
        g2d.fillRect(65, this.getHeight() / 2 - 300 - 5, 30, healthBarHeight);
        g2d.setColor(this.healthToColor(this.getHealth() / this.getMaxHealth()));
        g2d.fillRect(70, (int) (this.getHeight() / 2 - 300 + 5 + (healthBarHeight * (1 - (this.getHealth() / this.getMaxHealth())))), 20, (int) (this.getHealth() * healthBarHeight / this.getMaxHealth()) - 15);
    	if (this.showEvoOption) {
    		if (this.evoBtns.size() < 3) {
				while (this.evoBtns.size() < 9) {
					double rand = Math.random();
					JButton btn = new JButton();
					if (rand < 0.2) {
						btn.setText("<html><center>"+"Increase"+"<br>"+"Damage"+"</center></html>");
						btn.setActionCommand("DMG");
					} else if (rand < 0.4) {
						btn.setText("<html><center>"+"Increase"+"<br>"+"Speed"+"</center></html>");
						btn.setActionCommand("SPEED");
					} else if (rand < 0.6) {
						btn.setText("<html><center>"+"Increase"+"<br>"+"Health"+"</center></html>");
						btn.setActionCommand("HEALTH");
					} else if (rand < 0.8) {
						btn.setText("<html><center>"+"Increase"+"<br>"+"Energy"+"</center></html>");
						btn.setActionCommand("ENERGY");
					} else if (rand < 0.96) {
						btn.setText("<html><center>"+"Increase"+"<br>"+"Hit-cooldown"+"</center></html>");
						btn.setActionCommand("HCD");
					} else {
						btn.setText("EVOLVE");
						btn.setActionCommand("EVO");
					}
					btn.addActionListener(this.listener);
					this.evoBtns.add(btn);
				}
			}
    		int rnd1 = (int) (Math.random() * this.evoBtns.size());
    		int rnd2 = (int) (Math.random() * this.evoBtns.size());
    		int rnd3 = (int) (Math.random() * this.evoBtns.size());
    		while (rnd2 == rnd1) {
    			rnd2 = (int) (Math.random() * this.evoBtns.size());
    		}
    		while (rnd3 == rnd2 || rnd3 == rnd1) {
    			rnd3 = (int) (Math.random() * this.evoBtns.size());
    		}
    		int[] rands = {rnd1, rnd2, rnd3};
    		for (int i = 0; i < 3; i++) {
    			JButton rnd = this.evoBtns.get(rands[i]);
    			rnd.setBounds(this.getWidth() * (i + 1) / 4 - this.getWidth() / 16, this.getHeight() / 3, this.getWidth() / 8, this.getHeight() / 12);
    			this.gamePanel.add(rnd);
    		}
    		this.showEvoOption = false;
    	}
    }

    public void move() {
        double t = 0.1; // Adjust this value for the desired speed of movement

        double botX = ((Bot) target).getX();
        double botY = ((Bot) target).getY();

        double newX = this.getX() + (botX - this.getX()) * t;
        double newY = this.getY() + (botY - this.getY()) * t;

        this.setX(newX);
        this.setY(newY);
    }

    public void move(double cursorX, double cursorY, boolean isDead) {
        cursorX -= this.getWidth() / 2;
        cursorY -= this.getHeight() / 2;
        if (jumpingFrames > 0) {
        	this.setZ(this.getZ() + 0.8);
        	jumpingFrames--;
        }
        if (dashingFrames > 0) {
			dashingFrames--;
		}
        if (throwingFrames > 0) {
        	throwingFrames--;
        }
        super.move(cursorX, cursorY, this.getZ());
    }
    
    @Override
    public void setWidth(int width) {
    	super.setWidth(width);
    	if (this.evoBtns == null) {
    		return;
    	}
    	int numSections = this.evoBtns.size() + 1;
    	for (int i = 0; i < this.evoBtns.size(); i++) {
    		JButton btn = this.evoBtns.get(i);
    		btn.setBounds(((i + 1) * this.getWidth() / numSections) - (this.getWidth() / (4 * numSections)), this.getHeight() / 3, (this.getWidth() / (2 * numSections)), this.getHeight() / 6);
    	}
    }
    
    @Override
    public void setHeight(int height) {
    	super.setHeight(height);
    	if (this.evoBtns == null) {
    		return;
    	}
    	int numSections = this.evoBtns.size() + 1;
    	for (int i = 0; i < this.evoBtns.size(); i++) {
    		JButton btn = this.evoBtns.get(i);
    		btn.setBounds((this.getWidth() / numSections) - (this.getWidth() / (4 * numSections)), this.getHeight() / 3, (this.getWidth() / (2 * numSections)), this.getHeight() / 6);
    	}
    }

    public void setTarget(Entity ent) {
        this.target = ent;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        if (this.xp >= this.xpGoal) {
        	this.gamePanel.pause(true);
        	this.showEvoOption = true;
        	this.xp %= this.xpGoal;
        	this.xpGoal *= 1.03;
        }
    }
    
    @Override
    public double getDamage() {
    	return super.getDamage() * this.dmgIncrease;
    }

    public double getViewPOV() {
        return viewPOV;
    }

    public void setViewPOV(double viewPOV) {
        this.viewPOV = viewPOV;
    }

	public ArrayList<Character> getCurrentAbilityKeys() {
		return this.currentAbilityKeys;
	}

	public void activateAbility(int index) {
		String ability = this.currentAbilities.get(index);
		switch (ability) {
		case "JUMP":
			if (jumpingFrames == 0 && this.getSprintEndurance() > 70 && this.getSprintingDisabled() == 0) {
				this.jumpingFrames = 20;
				this.setSprintEndurance(this.getSprintEndurance() - 70);
				this.setSprintingDisabled(25);
			}
			break;
		case "DASH":
			if (getDashingFrames() == 0 && this.getSprintEndurance() > 70 && this.getSprintingDisabled() == 0) {
				this.setDashingFrames(12);
				if (this.getSprintEndurance() < 75 && this.getSprintEndurance() > 65) {
					this.setSprintEndurance(0);
				} else {
					this.setSprintEndurance(this.getSprintEndurance() - 70);
				}
				this.setSprintingDisabled(25);
			} 
			break;
		case "THROW":
			if (throwingFrames == 0 && this.getSprintEndurance() > 70 && this.getSprintingDisabled() == 0) {
				throwingFrames = 45;
				if (this.getSprintEndurance() < 75 && this.getSprintEndurance() > 65) {
					this.setSprintEndurance(0);
				} else {
					this.setSprintEndurance(this.getSprintEndurance() - 70);
				}
				this.setSprintingDisabled(30);
				ThrownParticle thrown = new ThrownParticle(this.getX() + this.getxVelocity() * 10 + this.getWidth() / 2, this.getY() + this.getyVelocity() * 10 + this.getHeight() / 2, this.getZ(), this.getxVelocity() * 4, this.getyVelocity() * 4, 25, 40, "rock.png",this);
				this.gamePanel.addEntity(thrown);
			}
			break;
		}
	}

	public int getDashingFrames() {
		return dashingFrames;
	}

	public void setDashingFrames(int dashingFrames) {
		this.dashingFrames = dashingFrames;
	}
}
