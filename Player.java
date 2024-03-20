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
	private double xDir = 0;
	private double yDir = 0;
	private Image playerImage;

	public Player(int x, int y, int width, int height, int moveSpeedMod) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.moveSpeedMod = moveSpeedMod;
		try {
			playerImage = ImageIO.read(new File("./src/Hyena-S2.png")); // Replace "player.png" with the path to your image file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Player() {
		this.x = 0;
		this.y = 0;
		this.width = 400;
		this.height = 400;
		this.moveSpeedMod = 60;
		File[] files = new File("./src").listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				System.out.print("directory:");
			} else {
				System.out.print("     file:");
			}
			try {
				System.out.println(file.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			playerImage = ImageIO.read(new File("./src/Hyena-S2.png")); // Replace "player.png" with the path to your image file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void move(double xDir, double yDir) {
		this.xDir += xDir / moveSpeedMod;
		this.yDir += yDir / moveSpeedMod;
		this.x += (int) this.xDir;
		this.y += (int) this.yDir;
		this.xDir *= 5.0 / 6;
		this.yDir *= 5.0 / 6;
		if (this.xDir > 6) {
			this.xDir = 6;
		} else if (this.xDir < -6) {
			this.xDir = -6;
		}
		if (this.yDir > 6) {
			this.yDir = 6;
		} else if (this.yDir < -6) {
			this.yDir = -6;
		}
	}

	public double[] getPos() {
		return new double[] { this.x, this.y };
	}

	public void draw(Graphics g, int screenWidth, int screenHeight, int cursorX, int cursorY) {
	    Graphics2D g2d = (Graphics2D) g;
	    AffineTransform old = g2d.getTransform();
	    
	    double centerX = screenWidth / 2 - playerImage.getWidth(null) / 2;
	    double centerY = screenHeight / 2 - playerImage.getHeight(null) / 2;
	    
	    double angle = Math.atan2(cursorY - centerY, cursorX - centerX);
	    
	    g2d.rotate(angle, centerX + playerImage.getWidth(null) / 2, centerY + playerImage.getHeight(null) / 2);
	    g2d.drawImage(playerImage, (int) centerX, (int) centerY, null);
	    
	    g2d.setTransform(old);
	}
}