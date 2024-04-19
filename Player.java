import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Player extends MovingEntity {
	private int xp = 0;
	private int xpGoal = 200;
	private int frames = 0;
	private double xRoam = Math.random() * 200 - 100;
	private double yRoam = Math.random() * 200 - 100;

	public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight,String playerImage, double maxSpeed, double sprintSpeed) {
		super(x,y,width,height,moveSpeedMod,playerImage,drawWidth,drawHeight,maxSpeed, sprintSpeed);
	}

	public Player() {
		this(0.0,0.0,400,400,60,100,100,"Hyena-S2.png", Math.sqrt(162), Math.sqrt(243));		
		int rnd = (int) (Math.random() * 100 + 100);
		this.setDrawHeight(rnd);
		this.setDrawWidth(rnd);
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
	   
	    g2d.setColor(Color.yellow);
	    g2d.fillRect((int) (this.getWidth() / 2 - xpGoal * 3.2 / 2), 80,(int) (this.xp * 3.2), 20);
	    g2d.setColor(Color.gray);
	    g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()), this.getHeight() - 80, (int) (this.getMaxSprintEndurance() * 2), 20);
	    if (this.getSprintingDisabled() > 0) {
	    	g2d.setColor(Color.red);
	    } else {
		    g2d.setColor(Color.orange);
	    }
	    g2d.fillRect((int) (this.getWidth() / 2 - this.getMaxSprintEndurance()), this.getHeight() - 80, (int) (this.getSprintEndurance() * 2), 20);
	    this.xp++;
	    this.xp%= this.xpGoal;
	    g2d.setColor(Color.gray);
	    g2d.fillRect(65, this.getHeight() / 2 - 300 - 5, 30, 610);
	    g2d.setColor(Player.healthToColor(this.getHealth() / 100.0));
	    this.setHitCooldown(this.getHitCooldown()<=0?0:this.getHitCooldown() - 0.5);
	    g2d.fillRect(70, (int) (this.getHeight() / 2 + 300 - (this.getHealth() * 6)), 20, (int) this.getHealth() * 6);
	    if(this.getHealth()>0&&this.getHealth()<100) {
	    	this.setHealth(this.getHealth()+0.01);
	    }
	}
	public static Color healthToColor(double percentage) {
	    if (percentage > 1) {
	        percentage = 1;
	    }
	    else if (percentage < 0) {
	        percentage = 0;
	    }
	    int red = (int)((129.0 * (1 - percentage)) + 125) ;
	    int green = (int)((204.0 * (percentage)) + 50) ;
	    int blue = 0;
	    return new Color(red, green, blue);
	}
	
	public void move(double cursorX, double cursorY,boolean isDead) {
		if(isDead) {
			if (frames > 120) {
				frames = 0;
				xRoam = Math.random() * 200 - 100;
				yRoam = Math.random() * 200 - 100;
			} else {
				cursorX = xRoam;
				cursorY = yRoam;
				frames++;
			}
		}
		else {
			frames=0;
			cursorX -= this.getWidth() / 2;
		    cursorY -= this.getHeight() / 2;
		}
		super.move(cursorX, cursorY);
	}
}