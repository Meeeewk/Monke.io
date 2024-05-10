import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;

public class Player extends MovingEntity {
    private int xp = 0;
    private int xpGoal = 200;
    private int frames = 0;
    private double xRoam = Math.random() * 200 - 100;
    private double yRoam = Math.random() * 200 - 100;
    private int jumpingFrames = 0;
    private int dmgIncrease = 0;
    private char[] abilityKeys = {' '};
    private String[] abilities = {"JUMP"};
    public Entity target;
    private boolean showEvoOption = false;
    private GamePanel gamePanel;
    private ArrayList<JButton> evoBtns = new ArrayList<JButton>();
    private double viewPOV = 55; // Default value

    public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight, String playerImage, double maxSpeed, double sprintSpeed) {
        super(x, y, width, height, moveSpeedMod, playerImage, drawWidth, drawHeight, maxSpeed, sprintSpeed);
    }

    public Player() {
        this(0.0, 0.0, 400, 400, 60, 100, 100, "elephant.png", Math.sqrt(162), Math.sqrt(243));
//        super.setZ(5);
        JButton speedBtn = new JButton("Add speed");
		JButton dmgBtn = new JButton("Add damage");
		speedBtn.setBounds(this.getWidth() / 6, this.getHeight() / 3, this.getWidth() / 6, this.getHeight() / 6);
		dmgBtn.setBounds(this.getWidth() * 2 / 3, this.getHeight() / 3, this.getWidth() / 6, this.getHeight() / 6);
		speedBtn.setBackground(Color.orange);
		dmgBtn.setBackground(Color.red);
		ActionListener listen = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ting = e.getActionCommand();
				if (ting.equals("Add speed")) {
					setMaxSpeed(getMaxSpeed() * 1.08);
				} else if (ting.equals("Add damage")) {
					dmgIncrease += 10;
				}
				gamePanel.pause(false);
				showEvoOption = false;
				gamePanel.remove(speedBtn);
				gamePanel.remove(dmgBtn);
			}
		};
		speedBtn.addActionListener(listen);
		dmgBtn.addActionListener(listen);
		this.evoBtns.add(speedBtn);
		this.evoBtns.add(dmgBtn);
    }
    
    public Player(GamePanel gamePanel) {
    	this();
    	this.gamePanel = gamePanel;
    	
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
        g2d.drawString("chunk: " + Arrays.toString(getChunk()), (int) (this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int) (this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2) - 30);
        g2d.setColor(Color.red);
        this.setHitCooldown(this.getHitCooldown() <= 0 ? 0 : this.getHitCooldown() - 0.5);
        g2d.setTransform(old);
    }

    public void drawUI(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.gray);
        g2d.fillRect((int) (this.getWidth() / 2 - xpGoal * 3.2 / 2), 80, (int) (this.xpGoal * 3.2), 20);
        g2d.setColor(Color.yellow);
        g2d.fillRect((int) (this.getWidth() / 2 - xpGoal * 3.2 / 2), 80, (int) (this.getXp() * 3.2), 20);
        g2d.setColor(Color.gray);
        g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()), this.getHeight() - 80, (int) (this.getMaxSprintEndurance() * 2), 20);
        if (this.getSprintingDisabled() > 0) {
            g2d.setColor(Color.red);
        } else {
            g2d.setColor(Color.orange);
        }
        g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()), this.getHeight() - 80, (int) (this.getSprintEndurance() * 2), 20);
        g2d.setColor(Color.gray);
        g2d.fillRect(65, this.getHeight() / 2 - 300 - 5, 30, 610);
        g2d.setColor(this.healthToColor(this.getHealth() / 100.0));
        g2d.fillRect(70, (int) (this.getHeight() / 2 + 300 - (this.getHealth() * 6)), 20, (int) this.getHealth() * 6);
    	if (this.showEvoOption) {
    		for (JButton btn : this.evoBtns) {
    			this.gamePanel.add(btn);
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
        System.out.println(this.getZ());
        if (jumpingFrames > 0) {
        	this.setZ(this.getZ() + 0.8);
        	jumpingFrames--;
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

	public char[] getAbilityKeys() {
		return this.abilityKeys;
	}

	public void activateAbility(int index) {
		String ability = this.abilities[index];
		switch (ability) {
		case "JUMP":
			if (jumpingFrames == 0 && this.getSprintEndurance() > 70 && this.getSprintingDisabled() == 0) {
				this.jumpingFrames = 20;
				this.setSprintEndurance(this.getSprintEndurance() - 70);
				this.setSprintingDisabled(20);
			}
		}
	}
}
