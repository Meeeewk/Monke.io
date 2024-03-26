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
		this.x = x;
		this.y = y;
		this.setWidth(width);
		this.setHeight(height);
		this.moveSpeedMod = moveSpeedMod;
		try {
			this.setPlayerImage(ImageIO.read(new File(imgPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDrawHeight(drawHeight);
		this.setDrawWidth(drawWidth);
	}
	public void move(double x, double y) {
		this.setxVelocity(this.getxVelocity() + x / this.moveSpeedMod);
		this.setyVelocity(this.getyVelocity() + y / this.moveSpeedMod);
		this.setxVelocity(this.getxVelocity() * (5.0 / 6));
		this.setyVelocity(this.getyVelocity() * (5.0 / 6));
		double vectLen = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
		this.x += (int) (this.getxVelocity() * 10 / vectLen);
		this.y += (int) (this.getyVelocity() * 10 / vectLen);
	}
	public void draw(Graphics g, int cursorX, int cursorY) {
	    Graphics2D g2d = (Graphics2D) g;
	    AffineTransform old = g2d.getTransform();
	    double angle = 0;
	    cursorX -= this.getWidth() / 2;
	    cursorY -= this.getHeight() / 2;
	    cursorY *= -1;
	    double vect_len = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
	    if (getyVelocity() * -1 < 0) {
	    	angle = Math.PI * 2 - Math.acos(this.getxVelocity() / vect_len);
	    } else {
	    	angle = Math.acos(getxVelocity() / vect_len);
	    }
	    this.setFacingDir(3 * Math.PI / 2 - angle);
	    g2d.rotate(this.getFacingDir(), this.getWidth() / 2, this.getHeight() / 2);
	    g2d.drawImage(this.getPlayerImage(), this.getWidth() / 2 - this.getDrawWidth() / 2, this.getWidth() / 2 - this.getDrawHeight() / 2, this.getDrawWidth(), this.getDrawHeight(), null);
	    System.out.println(this.getWidth() / 2 - this.getDrawWidth() / 2);
	    g2d.setTransform(old);
	}
	public double[] getPos() {
		return new double[] { this.x, this.y };
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
}
