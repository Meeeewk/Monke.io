import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Arrays;

public class Bot extends MovingEntity {
	private double lazyLength;
	private int frames = 0;
	private MovingEntity target;
	private double xRoam = Math.random() * 200 - 100;
	private double yRoam = Math.random() * 200 - 100;
	private double relX =9999;
	private double relY =9999;
	private int boundingX;
	private int boundingY;
	public Bot() {
		this(0.0, 0.0, 400, 400, 120, "Hyena-S2.png", 100, 100, Math.sqrt(100), 400, Math.sqrt(192));
	}

	public Bot(double x, double y, int width, int height, int moveSpeedMod, String imgPath, int drawWidth,
			int drawHeight, double maxSpeed, double lazyLength, double sprintSpeed) {
		super(x, y, width, height, moveSpeedMod, imgPath, drawWidth, drawHeight, maxSpeed, sprintSpeed);
		this.lazyLength = 400/this.getDrawWidth()*220;
	}
	
	public Bot(double x, double y,MovingEntity target, int boundingX, int boundingY) {
		this(x, y, 400, 400, 30, "Hyena-S2.png", 100, 100, Math.sqrt(100), 400, Math.sqrt(192));
//		int rnd = (int) (Math.random() * 20 + 100);
//		this.setDrawHeight(rnd);
//		this.setDrawWidth(rnd);
		this.setTarget(target);
		this.boundingX = boundingX;
		this.boundingY = boundingY;
	}

	public void move() {
		double targetX;
		double targetY;
		double vectLen = 99999;
		double z = 1;
		// If target is dead target gets set to null
		if (target != null && target.getHealth() <= 0) {
			target = null;
		}
		if (target != null) {
			targetX=target.getPos()[0];
			targetY=target.getPos()[1];
			setRelX(targetX - this.getX());
			setRelY(targetY - this.getY());
			z = target.getZ();
			vectLen = Math.sqrt(Math.pow(getRelX(), 2) + Math.pow(getRelY(), 2));
		}
		
		// Roaming mode if doesn't have a target, or if target is too far away / too high away
		if (target == null || vectLen > (lazyLength*this.getDrawHeight()/200) + this.getDrawHeight() / 4 || Math.abs(z - this.getZ()) > 1) {
			if (frames > 120) {
				frames = 0;
				xRoam = Math.random() * 200 - 100;
				yRoam = Math.random() * 200 - 100;
				
				
			} else {
				setRelX(xRoam);
				setRelY(yRoam);
				frames++;
				// Keeping bots away from walls
				double proposedX = xRoam * 3.5 + this.getX();
				double proposedY = yRoam * 3.5 + this.getY();
				// Regenerate if the bot is going to roam into/too close to a wall
				if (proposedX > boundingX - 100 || proposedX < -boundingX + 100) {
					xRoam = Math.random() * 200 - 100;
				}
				if (proposedY > boundingY - 100 || proposedY < -boundingY + 100) {
					yRoam = Math.random() * 200 - 100;
				}
			}
		} else {
			frames = 0;
		}
		super.move(getRelX(), getRelY(), z);
	}
	@Override
	public void draw(Graphics g, int playerX, int playerY) {
		// EVERYTHING GETS DRAWN RELATIVE TO THE PLAYER DO NOT CHANGE
	    Graphics2D g2d = (Graphics2D) g;
//	    // View where bot is roaming to
//	    g2d.setColor(Color.ORANGE);
//	    g2d.fillOval((int) (xRoam * 3.5 + this.getX() - playerX + this.getWidth() / 2), (int) (yRoam * 3.5 + this.getY() - playerY + this.getHeight() / 2),30,30);
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
	    //		Predator red circle behind
	    g2d.drawImage(this.getPlayerImage(),(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2),(int) (this.getY() - playerY - this.getDrawHeight() / 2.0 + this.getHeight() / 2), this.getDrawWidth(), this.getDrawHeight(), null);
	    g2d.setTransform(old);
g2d.setColor(new Color(255, 0, 0, (int)this.getHitCooldown()*3));
	    g2d.fillOval((int) (this.getX() - playerX - (this.getDrawWidth() * 0.72) / 2.0 + this.getWidth() / 2), (int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2), (int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
		g2d.drawOval((int) (this.getX() - playerX - (this.getDrawWidth() * 0.72) / 2.0 + this.getWidth() / 2), (int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2), (int) (this.getDrawWidth() * 0.72), (int) (this.getDrawHeight() * 0.72));
	    g2d.setColor(this.healthToColor(this.getHealth() / 100));
	    int x=(int) (this.getX() - playerX - this.getDrawWidth() / 2.0 + this.getWidth() / 2);
	    int y=(int) (this.getY() - playerY - (this.getDrawHeight() * 0.72) / 2.0 + this.getHeight() / 2)-20;
	    g2d.fillRect(x, y,(int) (this.getHealth() * this.getDrawWidth()/100), 10);
//	    g2d.setColor(Color.red);
//	    g2d.drawString("z: "+this.getZ(), x, y-30);
//	    g2d.setColor(Color.black);
//	    g2d.drawString("chunk: " + Arrays.toString(getChunk()), x, y-30);
	    g2d.setColor(Color.gray);
	    g2d.fillRect(x+((int) (this.getHealth() * this.getDrawWidth()/100)), y,(int) ((100-this.getHealth()) * this.getDrawWidth()/100), 10);
	    this.setHitCooldown(this.getHitCooldown()<=0?0:this.getHitCooldown() - 0.5);
		this.setMoveSpeedMod(this.getHitCooldown()==0?90:30);
	}

	public void setTarget(MovingEntity target) {
		this.target=target;
	}
	public Entity getTarget() {
		return this.target;
	}

	public double getRelX() {
		return relX;
	}

	public void setRelX(double relX) {
		this.relX = relX;
	}

	public double getRelY() {
		return relY;
	}

	public void setRelY(double relY) {
		this.relY = relY;
	}
}
