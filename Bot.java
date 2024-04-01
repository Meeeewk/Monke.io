import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bot extends Entity {
	public Bot() {
		this(0.0, 0.0, 400, 400, 20, "Hyena-S2.png", 100, 100);
	}

	public Bot(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight) {
		super(x, y, width, height, moveSpeedMod, imgPath, drawWidth, drawHeight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void move(double playerX, double playerY) {
		double relX = playerX - this.getX();
		double relY = playerY - this.getY();
		super.move(relX, relY);
	}
	@Override
	public void draw(Graphics g, int playerX, int playerY) {
		Graphics2D g2d = (Graphics2D) g;
//	    AffineTransform old = g2d.getTransform();
//	    double angle = 0;
//	    double vect_len = Math.sqrt(Math.pow(this.getxVelocity(), 2) + Math.pow(this.getyVelocity(), 2));
//	    if (getyVelocity() * -1 < 0) {
//	    	angle = Math.PI * 2 - Math.acos(this.getxVelocity() / vect_len);
//	    } else {
//	    	angle = Math.acos(getxVelocity() / vect_len);
//	    }
//	    this.setFacingDir(3 * Math.PI / 2 - angle);
//	    g2d.rotate(this.getFacingDir(), this.getWidth() / 2, this.getHeight() / 2);
	    g2d.drawImage(this.getPlayerImage(),(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + 200),(int) (this.getY() - playerY - this.getDrawHeight() / 2.0 + 200), this.getDrawWidth(), this.getDrawHeight(), null);
//	    g2d.setTransform(old);
	}
}
