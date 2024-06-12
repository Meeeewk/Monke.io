package src;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Consumable extends Entity {
	private double x;
	private double y;
	private int width;
	private int height;
	private int drawWidth;
	private int drawHeight;
	private Image playerImage;
	private double facingDir;
	private String name;
	private int xp;
	private int health;

	public Consumable(double x, double y, int width, int height, String imgPath, int drawWidth, int drawHeight,
			String name, int xp) {
		super(x, y);
		this.setXp(xp);
		this.setName(name);
		this.width=width;
		this.setHeight(height);
		try {
			this.setPlayerImage(ImageIO.read(new File(imgPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setOrigDrawHeight(drawHeight);
		this.setOrigDrawWidth(drawWidth);
		this.setDrawHeight((int)(getOrigDrawHeight()+ getZ()*10));
		this.setDrawWidth((int)(getOrigDrawWidth()+ getZ()*10));
	}

	public Consumable(double x, double y, String name, int size, int xp, int health) {
		this(x, y, name, size);

		this.setOrigDrawHeight(size);
		this.setOrigDrawWidth(size);
		this.setDrawHeight((int)(getOrigDrawHeight()+ getZ()*10));
		this.setDrawWidth((int)(getOrigDrawWidth()+ getZ()*10));
		this.setZ(3);
		this.setXp(xp);
		this.setHealth(health);
	}

	public Consumable(double x, double y, String name, int size, int xp, int health,int z) {
		this(x, y, name, size,xp,health);
		this.setZ(z);
	}

	public Consumable(double x, double y, String name, int size) {
		super(x, y);
		this.setZ(3);
		this.setXp(50);
		this.setName(name);
		try {
			this.setPlayerImage(ImageIO.read(new File("images/" + name + ".png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDrawHeight(size);
		this.setDrawWidth(size);
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getHealth() {
		return this.health;
	}

	public int getXp() {
		return this.xp;
	}

	private void setXp(int xp) {
		this.xp = xp;
	}

	public int getSize() {
		return this.getDrawHeight();
	}

	public String getName() {
		return this.name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public void consume(MovingEntity ent) {
		ent.setHealth(ent.getHealth() + this.health);
		if (ent instanceof Player) {
			Player entity = (Player) ent;
			entity.setXp(entity.getXp() + this.xp);
		}
	}

	public void draw(Graphics g, int playerX, int playerY) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		double angle = 0;
		this.setFacingDir(3 * Math.PI / 2 - angle);
		int x = (int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2);
		int y = (int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2) - 20;
		g2d.setColor(Color.black);
//	    g2d.drawString("chunk: " + Arrays.toString(getChunk()), x, y-30);
		g2d.rotate(this.getFacingDir(), (int) (this.getX() - playerX + this.getWidth() / 2),
				(int) (this.getY() - playerY + this.getHeight() / 2));
		// Predator red circle behind
		// g2d.fillOval((int) (this.getX() - playerX - (this.getDrawWidth() * 0.72) /
		// 2.0 + this.getWidth() / 2), (int) (this.getY() - playerY -
		// (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2), (int)
		// (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
		g2d.drawImage(this.getPlayerImage(),
				(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2),
				(int) (this.getY() - playerY - this.getDrawHeight() / 2.0 + this.getHeight() / 2), this.getDrawWidth(),
				this.getDrawHeight(), null);
		g2d.setTransform(old);

		if (Main.showHitBoxes) {
			g2d.drawOval((int) (this.getX() - playerX - (this.getDrawWidth() * 0.72) / 2.0 + this.getWidth() / 2),
					(int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2),
					(int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));

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
