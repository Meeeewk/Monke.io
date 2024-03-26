import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bot extends Entity {
	public Bot() {
		this(0.0, 0.0, 400, 400, 60, "Hyena-S2.png", 100, 100);
	}

	public Bot(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight) {
		super(x, y, width, height, moveSpeedMod, imgPath, drawWidth, drawHeight);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void move(double x, double y) {

	}
	@Override
	public void draw(Graphics g, int cursorX, int cursorY) {
	    Graphics2D g2d = (Graphics2D) g;
	    AffineTransform old = g2d.getTransform();

	    g2d.setTransform(old);
	}
}
