import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Player extends Entity {
	private int xp = 0;
	private int xpGoal = 200;


	public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight,String playerImage, double maxSpeed, double sprintSpeed) {
		super(x,y,width,height,moveSpeedMod,playerImage,drawWidth,drawHeight,maxSpeed, sprintSpeed);
	}

	public Player() {
		this(0.0,0.0,400,400,60,100,100,"Hyena-S2.png", Math.sqrt(162), Math.sqrt(243));
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
	    g2d.setColor(Color.gray);
	    g2d.fillRect((int) (this.getWidth() / 2 - xpGoal * 3.2 / 2), 80,(int) (this.xpGoal * 3.2), 20);
	    g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()), this.getHeight() - 80, (int) (this.getMaxSprintEndurance() * 2), 20);
	    g2d.setColor(Color.yellow);
	    g2d.fillRect((int) (this.getWidth() / 2 - xpGoal * 3.2 / 2), 80,(int) (this.xp * 3.2), 20);
	    if (this.getSprintingDisabled() > 0) {
	    	g2d.setColor(Color.red);
	    } else {
		    g2d.setColor(Color.orange);
	    }
	    g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()) + 50, this.getHeight() - 80, (int) (this.getSprintEndurance() * 2), 20);
	    this.xp++;
	    this.xp%= this.xpGoal;
	    g2d.setColor(Color.gray);
	    g2d.fillRect(70, this.getHeight() / 2 - 300, 70, 600);
	    g2d.setColor(Player.healthToColor(this.getHealth() / 100.0));
	    g2d.fillRect(70, this.getHeight() / 2 + 300 - (this.getHealth() * 6), 70, this.getHealth() * 6);
	    this.setHealth((this.getHealth() + 1) % 100);
	}
	public static Color healthToColor(double percentage) {
	    if (percentage > 1) {
	        percentage = 1;
	    }
	    else if (percentage < 0) {
	        percentage = 0;
	    }
	    int red = (int)(255.0 * (1 - percentage));
	    int green = (int)(255.0 * (percentage));
	    int blue = 0;
	    return new Color(red, green, blue);
	}
	@Override
	public void move(double cursorX, double cursorY) {
		cursorX -= this.getWidth() / 2;
	    cursorY -= this.getHeight() / 2;
		super.move(cursorX, cursorY);
	}
}