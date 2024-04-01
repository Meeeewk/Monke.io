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

	public Entity(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.setMoveSpeedMod(moveSpeedMod);
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
		if (vectLen > Math.sqrt(162)) {
			this.setxVelocity(Math.sqrt(162) * this.getxVelocity() / vectLen);
			this.setyVelocity(Math.sqrt(162) * this.getyVelocity() / vectLen);
		}
		this.setX(this.getX() + (int) (this.getxVelocity()));
		this.setY(this.getY() + (int) (this.getyVelocity()));
	}
	public void draw(Graphics g, int playerX, int playerY) {
	    
	}
	public double[] getPos() {
		return new double[] { this.getX(), this.getY() };
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
}
