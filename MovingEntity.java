import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.util.Random;

public class MovingEntity extends Entity {
	public static String[] skins = { "eagle.png","falcon.png","bigfoot.png", "gorilla.png", "elephant.png", "croc.png", "orangutan.png" };
	public double[] skinsDamage = { 20,25,15, 10, 20, 30, 10 };
	public int[] skinSize = { 130,150,200, 100, 250, 150, 170 };
	public static double[] skinsRarity = { 0.1,0.2,0.01, 0.1, 0.2, 0.1, 0.4 };
	private double x;
	private double y;
	private int width;
	private int height;
	private int moveSpeedMod = 12;
	private double xVelocity = 0;
	private double yVelocity = 0;
	private int drawWidth;
	private int drawHeight;
	private Image playerImage;
	private double facingDir;
	private double maxSpeed;
	private boolean sprinting = false;
	private double sprintSpeed;
	private double sprintEndurance = 240;
	private double maxSprintEndurance = 240;
	private int sprintingDisabled = 0;
	private double health = 100;
	private double maxHealth = 100;
	private double hitCooldown = 10;
	private int skin;
	private int killCount=0;

	public MovingEntity(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight, double maxSpeed, double sprintSpeed) {
		super(x, y);
		this.setWidth(width);
		this.setHeight(height);
		this.setMaxSpeed(maxSpeed);
		this.setMoveSpeedMod(moveSpeedMod);
		this.setSprintSpeed(sprintSpeed);
		this.setSkin(selectRandomSkin());
		try {
			this.setPlayerImage(ImageIO.read(new File(skins[skin])));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		this.setDrawHeight(drawHeight);
//		this.setDrawWidth(drawWidth);
		this.setDrawHeight((int)(this.skinSize[skin]+ this.getZ()*10));
		this.setDrawWidth((int)(this.skinSize[skin]+ this.getZ()*10));
	}

	public int getKillCount() {
		return this.killCount;
	}
	public void changeKillCount(int count) {
		this.killCount+=count;
	}
	@Override 
	public void setZ(double z) {
		super.setZ(z);
		this.setDrawHeight((int)(this.skinSize[skin]+ z*10));
		this.setDrawWidth((int)(skinSize[skin]+ z*10));
	}

    public int getSkin() {
		return this.skin;
	}
	public void setSkin(int selectRandomSkin) {
		this.skin = selectRandomSkin;
	}

	public static int selectRandomSkin() {
		Random random = new Random();
		double rand = random.nextDouble();
		double cumulativeProbability = 0.0;

		for (int i = 0; i < skins.length; i++) {
			cumulativeProbability += skinsRarity[i];
			if (rand < cumulativeProbability) {
				return i;
			}
		}

		// If no skin is selected, return a default skin or handle it as needed
		return 0;
	}
	
	public static int selectRandomSkin(int notThis) {
		Random random = new Random();
		double rand = random.nextDouble();
		double cumulativeProbability = 0.0;
		int retrn = notThis;
		
		while (retrn == notThis) {
			retrn = (int) (random.nextDouble() * skins.length);
		}
		

		// If no skin is selected, return a default skin or handle it as needed
		return retrn;
	}

	public double getDamage() {
		return this.skinsDamage[skin];
	}

	public double compareTo(Entity e) {
		return Math.sqrt(Math.pow(this.getX() - e.getX(), 2) + Math.pow(this.getY() - e.getY(), 2));
	}

	public Color healthToColor(double percentage) {
		if (percentage > 1) {
			percentage = 1;
		} else if (percentage < 0) {
			percentage = 0;
		}
		int red = (int) ((129.0 * (1 - percentage)) + 125);
		int green = (int) ((204.0 * (percentage)) + 50);
		int blue = 0;
		return new Color(red, green, blue);
	}

	public void move(double x, double y, double z) {
		if (this instanceof Player && ((Player) this).getDashingFrames() > 0) {
			this.setX(this.getX() + (int) (this.getxVelocity() * 5));
			this.setY(this.getY() + (int) (this.getyVelocity() * 5));
			this.setxVelocity(this.getxVelocity() * 5 / 6);
			this.setyVelocity(this.getyVelocity() * 5 / 6);
			return;
		}
		this.setxVelocity(this.getxVelocity() + x / this.getMoveSpeedMod());
		this.setyVelocity(this.getyVelocity() + y / this.getMoveSpeedMod());
		double vectLen = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
		double evaluatingMax = this.getMaxSpeed();
		if(this.getIsDown()) {
			evaluatingMax/=1.5;
		}
		if (sprinting) {
			evaluatingMax = this.getSprintSpeed();
			this.setSprintEndurance(this.getSprintEndurance() - 2);
		} else {
			this.setSprintEndurance(this.getSprintEndurance() + 1);
			this.setSprintEndurance(Math.min(this.getSprintEndurance(), this.getMaxSprintEndurance()));
		}
		if (this.getSprintEndurance() <= 0) {
			this.setSprinting(false);
			this.disableSprinting(180);
		}
		if (this.getSprintingDisabled() > 0) {
			this.setSprintingDisabled(this.getSprintingDisabled() - 1);
		}
		
		if (vectLen > evaluatingMax) {
			this.setxVelocity(evaluatingMax * (this.getxVelocity()) / vectLen);
			this.setyVelocity(evaluatingMax * (this.getyVelocity()) / vectLen);
		}
		this.setX(this.getX() + (int) (this.getxVelocity()));
		this.setY(this.getY() + (int) (this.getyVelocity()));
	}

	public void disableSprinting(int frames) {
		this.setSprintingDisabled(frames);
	}

	public void draw(Graphics g, int playerX, int playerY) {

	}

	public void setSprinting(boolean b) {
		if (this.getSprintingDisabled() > 0) {
			this.sprinting = false;
		} else {
			this.sprinting = b;
		}
	}

	public double[] getPos() {
		return new double[] { this.getX(), this.getY() };
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.setX(this.getX() + -1 * (width - this.getWidth()) / 2);
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.setY(this.getY() + -1 * (height - this.getHeight()) / 2);
		this.height = height;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public double getFacingDir() {
		return facingDir;
	}

	public void setFacingDir(double facingDir) {
		this.facingDir = facingDir;
	}

	public Image getPlayerImage() {
		return playerImage;
	}

	public void setPlayerImage(Image playerImage) {
		this.playerImage = playerImage;
	}

	public int getDrawWidth() {
		return drawWidth;
	}

	public void setDrawWidth(int drawWidth) {
		this.drawWidth = drawWidth;
	}

	public int getDrawHeight() {
		return drawHeight;
	}

	public void setDrawHeight(int drawHeight) {
		this.drawHeight = drawHeight;
	}

	public int getMoveSpeedMod() {
		return moveSpeedMod;
	}

	public void setMoveSpeedMod(int moveSpeedMod) {
		this.moveSpeedMod = moveSpeedMod;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public double getSprintSpeed() {
		return sprintSpeed;
	}

	public void setSprintSpeed(double sprintSpeed) {
		this.sprintSpeed = sprintSpeed;
	}

	public double getMaxSprintEndurance() {
		return maxSprintEndurance;
	}

	public void setMaxSprintEndurance(double maxSprintEndurance) {
		this.maxSprintEndurance = maxSprintEndurance;
	}

	public double getSprintEndurance() {
		return sprintEndurance;
	}

	public void setSprintEndurance(double sprintEndurance) {
		if (sprintEndurance == 0) {
			this.disableSprinting(180);
		}
		this.sprintEndurance = sprintEndurance;
	}

	public int getSprintingDisabled() {
		return sprintingDisabled;
	}

	public void setSprintingDisabled(int sprintingDisabled) {
		this.sprintingDisabled = sprintingDisabled;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double d) {
		this.health = d;
		if (this.health > this.getMaxHealth()) {
			this.health = this.getMaxHealth();
		}
	}

	public double getHitCooldown() {
		return hitCooldown;
	}

	public void setHitCooldown(double hitCooldown) {
		this.hitCooldown = hitCooldown;
	}

	public double getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
}
