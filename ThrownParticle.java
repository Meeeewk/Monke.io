import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ThrownParticle extends Entity {
	private double xVelocity;
	private double yVelocity;
	private int damage;
	private int lifespan;
	private MovingEntity origin;
	public ThrownParticle(double x, double y, double z, double xVelocity, double yVelocity, int damage, int lifespan, String imgPath, MovingEntity origin) {
		super(x, y);
		this.setZ(z);
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.damage = damage;
		this.lifespan = lifespan;
		this.origin = origin;
		try {
			this.setPlayerImage(ImageIO.read(new File(imgPath)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDrawWidth(50);
		this.setDrawHeight(50);
	}

	public void move() {
		this.setX(this.getX() + this.xVelocity);
		this.setY(this.getY() + this.yVelocity);
		this.lifespan--;
	}
	
	@Override
	public void draw(Graphics g, int playerX, int playerY) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.drawImage(this.getPlayerImage(),(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2),(int) (this.getY() - playerY - this.getDrawHeight() / 2.0 + this.getHeight() / 2), this.getDrawWidth(), this.getDrawHeight(), null);
		if(Main.showHitBoxes) {
			g2d.drawOval((int) (this.getX() - playerX - (this.getDrawWidth() * 0.72) / 2.0 + this.getWidth() / 2), (int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2), (int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
		}
	}
	
	public void hit (MovingEntity ent) {
		if (ent != origin) {
			ent.setHealth(ent.getHealth() - damage);
		}
	}
	
	public MovingEntity getOrigin() {
		return this.origin;
	}
	
	public int getLifeFrames() {
		return this.lifespan;
	}
}