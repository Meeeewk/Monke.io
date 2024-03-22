import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
	private double x;
	private double y;
	private int width;
	private int height;
	private int moveSpeedMod = 12;
	private int[][] pastMovement = new int[20][2];
	private double xVelocity = 0;
	private double yVelocity = 0;
	private double trueXVelocity = 0;
	private double trueYVelocity = 0;
	private Image playerImage;
	private int drawWidth;
	private int drawHeight;
	//Direction the player icon should be facing in, measured in radians
	private double facingDir;

	public Player(int x, int y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.moveSpeedMod = moveSpeedMod;
		try {
			playerImage = ImageIO.read(new File("Hyena-S2.png")); // Replace "player.png" with the path to your image file
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.drawWidth = drawWidth;
		this.drawHeight = drawHeight;
	}

	public Player() {
		this.x = 0;
		this.y = 0;
		this.width = 400;
		this.height = 400;
		this.moveSpeedMod = 60;
		this.drawWidth = 100;
		this.drawHeight = 100;
		try {
			playerImage = ImageIO.read(new File("Hyena-S2.png")); // Replace "player.png" with the path to your image file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void move(double x, double y) {
		this.xVelocity += x / this.moveSpeedMod;
		this.yVelocity += y / this.moveSpeedMod;
		this.x += (int) this.xVelocity;
		this.y += (int) this.yVelocity;
		this.xVelocity *= 5.0 / 6;
		this.yVelocity *= 5.0 / 6;
		this.trueXVelocity = this.xVelocity;
		this.trueYVelocity = this.yVelocity;
		if (this.xVelocity > 6) {
			this.xVelocity = 6;
		} else if (this.xVelocity < -6) {
			this.xVelocity = -6;
		}
		if (this.yVelocity > 6) {
			this.yVelocity = 6;
		} else if (this.yVelocity < -6) {
			this.yVelocity = -6;
		}
	}

	public double[] getPos() {
		return new double[] { this.x, this.y };
	}

	public void draw(Graphics g, int cursorX, int cursorY) {
	    Graphics2D g2d = (Graphics2D) g;
	    AffineTransform old = g2d.getTransform();
	    System.out.println(xVelocity + "   " );
	    double angle = 0;
	    cursorX -= this.width / 2;
	    cursorY -= this.height / 2;
	    cursorY *= -1;
	    double vect_len = Math.sqrt(Math.pow(this.trueXVelocity, 2) + Math.pow(this.trueYVelocity, 2));
	    if (trueYVelocity * -1 < 0) {
	    	angle = Math.PI * 2 - Math.acos(this.trueXVelocity / vect_len);
	    } else {
	    	angle = Math.acos(trueXVelocity / vect_len);
	    }
	    this.facingDir = 3 * Math.PI / 2 - angle;
	    g2d.rotate(this.facingDir, this.width / 2, this.height / 2);
	    g2d.drawImage(this.playerImage, this.width / 2 - this.drawWidth / 2, this.width / 2 - this.drawHeight / 2, this.drawWidth, this.drawHeight, null);
	    
	    g2d.setTransform(old);
	}
}