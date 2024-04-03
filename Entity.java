import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
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
	public void move(double x, double y) {
		this.setxVelocity(this.getxVelocity() + x / this.getMoveSpeedMod());
		this.setyVelocity(this.getyVelocity() + y / this.getMoveSpeedMod());
		double vectLen = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
		double evaluatingMax = sprinting ? this.getSprintSpeed() : this.getMaxSpeed();
		if (vectLen > evaluatingMax) {
			this.setxVelocity(evaluatingMax * this.getxVelocity() / vectLen);
			this.setyVelocity(evaluatingMax * this.getyVelocity() / vectLen);
		}
		this.setX(this.getX() + (int) (this.getxVelocity()));
		this.setY(this.getY() + (int) (this.getyVelocity()));
	}
	public void draw(Graphics g, int playerX, int playerY) {
	    
	}
	public void setSprinting(boolean b) {
		this.sprinting = b;
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
}
