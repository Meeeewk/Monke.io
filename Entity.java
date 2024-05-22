import java.awt.Graphics;
import java.awt.Image;
public class Entity {
	private double x;
	private double y;
	private int width;
	private int height;
	private int drawWidth;
	private int drawHeight;
	private Image playerImage;
	private double facingDir;
	private int[] chunk = new int[2];
	private double z;
	private boolean isUp=false;
	private boolean isDown=false;
	public Entity(double x, double y) {
		this.setX(x);
		this.setY(y);
		this.z=1;
	}
	public double getZ() {
		return this.z;
	}
	public void setIsUp(boolean isUp) {
		this.isUp=isUp;
	}
	public boolean getIsUp() {
		return this.isUp;
	}
	public void setZ(double z) {
		this.z=z;
	}
	public double compareTo(Entity e) {
		return Math.sqrt(Math.pow(this.getX()-e.getX(),2)+Math.pow(this.getY()-e.getY(),2));
	}
	public void draw(Graphics g, int playerX, int playerY) {
	    
	}
	public double[] getPos() {
		return new double[] { this.getX(), this.getY() };
	}
	public int getWidth() {
		return width;
	}
	public int[] getChunk() {
		return this.chunk;
	}
	public void setChunk(int[] chnk) {
		this.chunk = chnk;
	}
	public void setWidth(int width) {
		this.setX(this.getX() + -1 * (width - this.getWidth()) / 2);
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.setY(this.getY() + -1 * (height - this.getHeight()) / 2);
		this.height = height;
	}
	public double getFacingDir() {
		return facingDir;
	}
	public void setFacingDir(double facingDir) {
		this.facingDir = facingDir;
	}
	public Image getPlayerImage() {
		return playerImage;
	}
	public void setPlayerImage(Image playerImage) {
		this.playerImage = playerImage;
	}
	public int getDrawWidth() {
		return drawWidth;
	}
	public void setDrawWidth(int drawWidth) {
		this.drawWidth = drawWidth;
	}
	public int getDrawHeight() {
		return drawHeight;
	}
	public void setDrawHeight(int drawHeight) {
		this.drawHeight = drawHeight;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public void setIsDown(boolean b) {
		this.isDown=b;
		
	}
	public boolean getIsDown() {
		return this.isDown;
	}
}
