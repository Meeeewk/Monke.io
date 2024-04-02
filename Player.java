import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
	//Direction the player icon should be facing in, measured in radians

	public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight,String playerImage, double maxSpeed) {
		super(x,y,width,height,moveSpeedMod,playerImage,drawWidth,drawHeight,maxSpeed);
	}

	public Player() {
		this(0.0,0.0,400,400,60,100,100,"Hyena-S2.png", Math.sqrt(162));
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
	    g2d.setTransform(old);
	}
	
	@Override
	public void move(double cursorX, double cursorY) {
		cursorX -= this.getWidth() / 2;
	    cursorY -= this.getHeight() / 2;
		super.move(cursorX, cursorY);
	}
}