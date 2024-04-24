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
	public Entity target;
	public Player(double x, double y, int width, int height, int moveSpeedMod, int drawWidth, int drawHeight,String playerImage, double maxSpeed, double sprintSpeed) {
		super(x,y,width,height,moveSpeedMod,playerImage,drawWidth,drawHeight,maxSpeed, sprintSpeed);
	}

	public Player() {
		this(0.0,0.0,400,400,60,100,100,"elephant.png", Math.sqrt(162), Math.sqrt(243));		
		this.setHealth(1);
//		int rnd = 250;
//		this.setDrawHeight(rnd);
//		this.setDrawWidth(rnd);
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
	    g2d.setColor(new Color(255, 0, 0, (int)this.getHitCooldown()*3));
	    g2d.fillOval((int)(this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2),(int)( this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2), (int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
	    g2d.setColor(Color.red);
	    g2d.drawString("z: "+this.getZ(), (int)(this.getWidth() / 2 - (this.getDrawWidth() * 0.72) / 2), (int)( this.getHeight() / 2 - (this.getDrawHeight() * 0.72) / 2)-30);
	    g2d.setColor(Color.gray);
	    g2d.setTransform(old);

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
	    g2d.setColor(this.healthToColor(this.getHealth() / 100.0));
	    this.setHitCooldown(this.getHitCooldown()<=0?0:this.getHitCooldown() - 0.5);
	    g2d.fillRect(70, (int) (this.getHeight() / 2 + 300 - (this.getHealth() * 6)), 20, (int) this.getHealth() * 6);
	    if(this.getHealth()>0&&this.getHealth()<100) {
	    	this.setHealth(this.getHealth()+0.01);
	    }
	}

	public void move() {
		double t = 0.1; // Adjust this value for the desired speed of movement
	    
	    double botX = ((Bot) target).getX();
	    double botY = ((Bot) target).getY();
	    
	    double newX = this.getX() + (botX - this.getX()) * t;
	    double newY = this.getY() + (botY - this.getY()) * t;
	    
	    this.setX(newX);
	    this.setY(newY);
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
		super.move(cursorX, cursorY, this.getZ());
	}

	public void setTarget(Entity ent) {
		this.target=ent;
		
	}
}