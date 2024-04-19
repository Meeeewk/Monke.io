import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Collections;

public class GamePanel extends AnimatedPanel {
	private ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Consumable> consumables = new ArrayList<>();
	private Player player;
	private int mouseX;
	private int mouseY;
	private int boundingX = 1000;
	private int boundingY = 1000;

	@Override
	public void updateAnimation() {
		this.requestFocusInWindow();
	}

	public GamePanel() {
		addEventHandlers();
		this.setBackground(Color.GREEN);
		createObjects();
		this.entities.sort((o1, o2) -> o1.getDrawHeight() - o2.getDrawHeight());
	}

	public void addEventHandlers() {
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					player.setSprinting(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					player.setSprinting(false);
				}
			}
		});
	}

	public void createObjects() {
		this.player = new Player();
		this.entities.add(this.player);
		for (int i = 0; i < 3; i++) {
			this.entities.add(new Bot(Math.random() * 900, Math.random() * 900));
		}
		for (int j = 0; j < 200; j++) {
			this.entities.add(new Consumable(Math.random() * 9000, Math.random() * 9000, "banana"));
		}
	}

	private boolean isTouching(Entity e, Entity e2) {
		double x1 = e2.getX();
		double y1 = e2.getY();
		double x2 = e.getX();
		double y2 = e.getY();
		// radius is sketchy, change the 28 to different numbers and see
		int radius1 = (e2.getDrawWidth()) / 2;
		radius1 = (int) (radius1 * 0.72);
		int radius2 = (e.getDrawWidth()) / 2;
		radius2 = (int) (radius2 * 0.72);
		double xDif = x1 - x2;
		double yDif = y1 - y2;
		double distanceSquared = xDif * xDif + yDif * yDif;
		return distanceSquared < (radius1 + radius2) * (radius1 + radius2);
	}

	private double value(Entity e, Entity e2, boolean isY) {
		double x1 = e.getX();
		double y1 = e.getY();
		double x2 = e2.getX();
		double y2 = e2.getY();
		int radius1 = (e.getDrawWidth()) / 2;
		radius1 = (int) (radius1 * 0.72);
		if (isY) {
			return y1 + (e.compareTo(e2) == 0 ? 0 : radius1 * (y2 - y1) / e.compareTo(e2));
		} else {
			return x1 + (e.compareTo(e2) == 0 ? 0 : radius1 * (x2 - x1) / e.compareTo(e2));
		}
	}

	private void collide(Entity e, Entity e2) {
		if (isTouching(e, e2)) {
			double j = 2 * (value(e, e2, false) - (value(e2, e, false) + value(e, e2, false)) / 2);
			double t = 2 * (value(e, e2, true) - (value(e2, e, true) + value(e, e2, true)) / 2);
			e2.setX(e2.getX() + j);
			e2.setY(e2.getY() + t);
		}
	}

	private boolean isPastBounding(Entity e) {
		if (e.getX() > this.getBoundingX() || e.getX() < -this.getBoundingX()) {
			return true;
		} else if (e.getY() > this.getBoundingY() || e.getY() < -this.getBoundingY()) {
			return true;
		}
		return false;
	}

	private void movePastBounding(Entity e) {
		if (e.getX() > this.getBoundingX()) {
			e.setX(this.getBoundingX());
		} else if (e.getX() < -this.getBoundingX()) {
			e.setX(-this.getBoundingX());
		}
		if (e.getY() > this.getBoundingY()) {
			e.setY(this.getBoundingY());
		} else if (e.getY() < -this.getBoundingY()) {
			e.setY(-this.getBoundingY());
		}
	}

	private double getBoundingX() {
		return this.boundingX;
	}

	private double getBoundingY() {
		return this.boundingY;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		double[] playerPos = player.getPos();
		g.setColor(Color.CYAN);
		System.out.println(getWidth());
		g.fillRect(boundingX- (int) playerPos[0]+getWidth()-450, boundingY-(int) playerPos[1]-getHeight()-530, 10,boundingY*2);
		g.fillRect(-boundingX- (int) playerPos[0]+getWidth()-450, boundingY-(int) playerPos[1]-getHeight()-530, 10,boundingY*2);
		g.setColor(Color.BLACK);
		for (int i = -boundingX; i <= boundingX; i += 100) {
			for (int j = -boundingY; j <= boundingY; j += 100) {
				g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1], 1, 200);
				g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1], 200, 1);
			}
		}

		int height = getHeight();
		int width = getWidth();
		ArrayList<Integer> delete = new ArrayList<>();
		for (Entity ent : this.entities) {
			for (Entity ent2 : this.entities) {
				if (ent != ent2 && isTouching(ent, ent2)) {
					if (ent2 instanceof MovingEntity&&ent instanceof MovingEntity) {
						collide(ent2, ent);
						if(((MovingEntity) ent).getHitCooldown()==0) {
							((MovingEntity) ent).setHitCooldown(30);
							((MovingEntity) ent).setHealth(((MovingEntity) ent).getHealth()-10);
						}
						if(((MovingEntity) ent).getHealth()<=0){
							delete.add(this.entities.indexOf(ent));
							((MovingEntity) ent).setHealth(0);
						}
					} else {
						if(ent instanceof Consumable) {
							collide(ent2, ent);
						}
						else {
							collide(ent, ent2);
						}
					}
				}
			}
			if (ent instanceof Player) {
				this.player.draw(g, this.mouseX, this.mouseY);
			} else {
				// draw health bar
				
				ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
				if (ent instanceof MovingEntity) {
					if(this.entities.contains(this.player)) {
					((MovingEntity) ent).move((int) playerPos[0], (int) playerPos[1]);
					}
					else {
						((MovingEntity) ent).move(ent.getX() + Math.random() * 2000 - 1000, ent.getY() + Math.random() * 2000 - 1000);
					}
				}
			}
			if (isPastBounding(ent)) {
				movePastBounding(ent);
			}
			ent.setHeight(height);
			ent.setWidth(width);
		}

		if(this.entities.contains(this.player)) {
		this.player.move(mouseX, mouseY,!this.entities.contains(this.player));
		}
		else {
			this.player.move(0, 0,!this.entities.contains(this.player));
		}
		Collections.sort(delete);
		for (int i = 0; i < delete.size(); i++) {
			try {
				this.entities.remove(this.entities.get(delete.get(i) - i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		delete.clear();
	}
}