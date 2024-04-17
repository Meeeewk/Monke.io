import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Entity {
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
	private int health = 100;
	
	public Entity(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight, double maxSpeed, double sprintSpeed) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setMaxSpeed(maxSpeed);
		this.setMoveSpeedMod(moveSpeedMod);
		this.setSprintSpeed(sprintSpeed);
		try {
			this.setPlayerImage(ImageIO.read(new File(imgPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDrawHeight(drawHeight);
		this.setDrawWidth(drawWidth);
	}
	public double compareTo(Entity e) {
		return Math.sqrt(Math.pow(this.getX()-e.getX(),2)+Math.pow(this.getY()-e.getY(),2));
	}
	public void move(double x, double y) {
		this.setxVelocity(this.getxVelocity() + x / this.getMoveSpeedMod());
		this.setyVelocity(this.getyVelocity() + y / this.getMoveSpeedMod());
		double vectLen = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
		double evaluatingMax = this.getMaxSpeed();
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
			this.setxVelocity(evaluatingMax * this.getxVelocity() / vectLen);
			this.setyVelocity(evaluatingMax * this.getyVelocity() / vectLen);
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
		this.sprintEndurance = sprintEndurance;
	}
	public int getSprintingDisabled() {
		return sprintingDisabled;
	}
	public void setSprintingDisabled(int sprintingDisabled) {
		this.sprintingDisabled = sprintingDisabled;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
}
