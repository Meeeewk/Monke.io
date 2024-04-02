import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bot extends Entity {
	private double lazyLength;
	private boolean roam = false;
	private int frames = 0;
	private double xRoam = Math.random() * 200 - 100;
	private double yRoam = Math.random() * 200 - 100;
	public Bot() {
		this(0.0, 0.0, 400, 400, 120, "Hyena-S2.png", 100, 100, Math.sqrt(100), 400);
	}

	public Bot(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight, double maxSpeed, double lazyLength) {
		super(x, y, width, height, moveSpeedMod, imgPath, drawWidth, drawHeight, maxSpeed);
		this.lazyLength = lazyLength;
	}
	
	public Bot(double x, double y) {
		this(x, y, 400, 400, 120, "Hyena-S2.png", 100, 100, Math.sqrt(100), 400);
	}

	@Override
	public void move(double playerX, double playerY) {
		double relX = playerX - this.getX();
		double relY = playerY - this.getY();
		double vectLen = Math.sqrt(Math.pow(relX, 2) + Math.pow(relY, 2));
		if (vectLen > lazyLength) {
			if (frames > 120) {
				frames = 0;
				xRoam = Math.random() * 200 - 100;
				yRoam = Math.random() * 200 - 100;
			} else {
				relX = xRoam;
				relY = yRoam;
				frames++;
			}
		} else {
			frames = 0;
		}
		super.move(relX, relY);
	}
	@Override
	public void draw(Graphics g, int playerX, int playerY) {
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
	    g2d.rotate(this.getFacingDir(), (int) (this.getX() - playerX + this.getWidth() / 2), (int) (this.getY() - playerY + this.getHeight() / 2));
	    g2d.drawImage(this.getPlayerImage(),(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2),(int) (this.getY() - playerY - this.getDrawHeight() / 2.0 + this.getHeight() / 2), this.getDrawWidth(), this.getDrawHeight(), null);
	    g2d.setTransform(old);
	}
}
