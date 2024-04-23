import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends AnimatedPanel {
	private ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Consumable> consumables = new ArrayList<>();
	private Player player;
	private int mouseX;
	private int mouseY;
	private int boundingX = 500;
	private int boundingY = 500;
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
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			    int keyCode = e.getKeyCode();
			    if (keyCode == KeyEvent.VK_Z) {
			      player.setIsUp(true);
			    }
			  }
			});
	}

	public void createObjects() {
		this.player = new Player();
		this.entities.add(this.player);
		for (int i = 0; i < 2; i++) {
		}
		this.entities.add(new Bot(Math.random() * boundingX, Math.random() * boundingY,this.player));
		this.entities.add(new Bot(Math.random() * boundingX, Math.random() * boundingY,this.entities.get(1)));
//		for (int j = 0; j < 200; j++) {
//			this.entities.add(new Consumable(Math.random() * boundingX, Math.random() * boundingY, "banana"));
//		}
//		for (int i = 0; i < 10; i++) {
//			this.entities.add(new Obstacle(Math.random() * boundingX, Math.random() * boundingY, "tree", "non-moveable"));
//		}
	}

	private boolean isTouching(Entity e, Entity e2) {
		double x1 = e2.getX();
		double y1 = e2.getY();
		double x2 = e.getX();
		double y2 = e.getY();
		int radius1 = (e2.getDrawWidth()) / 2;
		radius1 = (int) (radius1 * 0.72);
		int radius2 = (e.getDrawWidth()) / 2;
		radius2 = (int) (radius2 * 0.72);
		double xDif = x1 - x2;
		double yDif = y1 - y2;
		double distanceSquared = xDif * xDif + yDif * yDif;
		double zDiff = Math.abs(e.getZ() - e2.getZ()); // Calculate the absolute difference in Z-coordinates
//		double rotDiff=e.getFacingDir()-e2.getFacingDir();
//		if(e instanceof Player&&rotDiff>2.5&&rotDiff<4.5) {
//		System.out.println(rotDiff);
//		}
		return zDiff < 1 && distanceSquared < (radius1 + radius2) * (radius1 + radius2); // Adjust the condition to																		// consider the Z-coordinate
																							// difference
	}

	private boolean isTouching2(Entity e, Entity e2) {
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
			if (e2 instanceof Obstacle && ((Obstacle) e2).getState().equals("non-moveable")
//					&&(e2.getDrawHeight()/2)<e.getDrawHeight()
			) {
				double j = 2 * (value(e2, e, false) - (value(e, e2, false) + value(e2, e, false)) / 2);
				double t = 2 * (value(e2, e, true) - (value(e, e2, true) + value(e2, e, true)) / 2);
				e.setX(e.getX() + j);
				e.setY(e.getY() + t);
			} else {
				double j = 2 * (value(e, e2, false) - (value(e2, e, false) + value(e, e2, false)) / 2);
				double t = 2 * (value(e, e2, true) - (value(e2, e, true) + value(e, e2, true)) / 2);
				e2.setX(e2.getX() + j);
				e2.setY(e2.getY() + t);
			}
		}
	}
	
	
	
	private boolean isPastBounding(Entity e) {
		int offset=e.getDrawWidth()/3;
		if (e.getX() > this.getBoundingX()-offset || e.getX() < -this.getBoundingX()+offset) {
			return true;
		} else if (e.getY() > this.getBoundingY()-offset || e.getY() < -this.getBoundingY()+offset) {
			return true;
		}
		return false;
	}

	private void movePastBounding(Entity e) {
		int offset=e.getDrawWidth()/3;
		if (e.getX() > this.getBoundingX()-offset) {
			e.setX(this.getBoundingX()-offset);
		} else if (e.getX() < -this.getBoundingX()+offset) {
			e.setX(-this.getBoundingX()+offset);
		}
		if (e.getY() > this.getBoundingY()-offset) {
			e.setY(this.getBoundingY()-offset);
		} else if (e.getY() < -this.getBoundingY()+offset) {
			e.setY(-this.getBoundingY()+offset);
		}
	}

	private double getBoundingX() {
		return this.boundingX;
	}

	private double getBoundingY() {
		return this.boundingY;
	}
	private void collideLogic(double[] playerPos, double playerZ, Graphics g) {
	    int height = getHeight();
	    int width = getWidth();
	    ArrayList<Integer> delete = new ArrayList<>();
	    Collections.sort(this.entities, new Comparator<>() {
	        public int compare(Entity e, Entity e2) {
	            if (e.getZ() - e2.getZ() > 0) {
	                return 1;
	            } else if (e.getZ() - e2.getZ() < 0) {
	                return -1;
	            }
	            return 0;
	        }
	    });
	    for (Entity ent : this.entities) {
	        boolean zSet = false; // Flag to track if Z-coordinate has been set for the current entity

	        // Check for collision and set Z-coordinate
	        for (Entity ent2 : this.entities) {
	            if (ent != ent2 && isTouching2(ent, ent2)) {
	                if (((ent instanceof Obstacle && ((Obstacle) ent).getState().equals("non-moveable")
	                        && (ent2 instanceof MovingEntity &&ent2.getIsUp()&& ent2.getDrawWidth() < 120))
	                        || (ent2 instanceof Obstacle && ((Obstacle) ent2).getState().equals("non-moveable")
	                                && (ent.getIsUp()&&ent instanceof MovingEntity && ent.getDrawWidth() < 120)))) {

	                    if (ent instanceof Obstacle) {
	                        ent2.setZ(3.0);
	                    } else {
	                        zSet = true;
	                        ent.setZ(3.0);
	                    }
	                    zSet = true; // Set flag to true after setting Z-coordinate
	                } else if(((ent instanceof Obstacle && ((Obstacle) ent).getState().equals("non-moveable")
	                        && (ent2 instanceof Bot && ent2.getDrawWidth() < 120))
	                        || (ent2 instanceof Obstacle && ((Obstacle) ent2).getState().equals("non-moveable")
	                                && (ent instanceof Bot && ent.getDrawWidth() < 120)))) {
	                	if(Math.random()>0.9 ) {
	                		if (ent instanceof Obstacle) {
		                        ent2.setIsUp(true);
		                        ent2.setZ(3.0);
		                    } else {
		                        zSet = true;
		                        ent.setIsUp(true);
		                        ent.setZ(3.0);
		                    }
	                	}
	                }
	                // Handle collision effects
	                if (isTouching(ent, ent2)) {
	                    if (ent2 instanceof MovingEntity && ent instanceof MovingEntity) {
	                        collide(ent2, ent);
	                        if(ent instanceof Bot) {
	                        	((Bot) ent).setTarget(ent2);
	                        }
	                        if(ent2 instanceof Bot) {
	                        	((Bot) ent2).setTarget(ent);
	                        }
	                        if (((MovingEntity) ent).getHitCooldown() == 0) {
	                            ((MovingEntity) ent).setHitCooldown(30);
	                            ((MovingEntity) ent).setHealth(((MovingEntity) ent).getHealth() - 10);
	                        }
	                        if (((MovingEntity) ent).getHealth() <= 0) {
	                            delete.add(this.entities.indexOf(ent));
	                            ((MovingEntity) ent).setHealth(0);
	                        }
	                    } else {
	                        if (ent instanceof Consumable) {
	                            collide(ent2, ent);
	                        } else {
	                            collide(ent, ent2);
	                        }
	                    }
	                }
	            }
	        }
	        if (!zSet && !(ent instanceof Obstacle)) {
	        	if(ent.getZ()>1.0) {
	        		ent.setIsUp(false);
	        	}
	            ent.setZ(1.0);
	        }
	        // Draw entities and handle movement
	        if (ent instanceof Player) {
	            // Adjust Z-coordinate for player entity when near edges of other entities or
	            // objects
	            this.player.draw(g, this.mouseX, this.mouseY);
	        } else {
	            ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
	            if (ent instanceof MovingEntity) {
	                if (this.entities.contains(this.player)) {
	                    ((MovingEntity) ent).move((int) playerPos[0], (int) playerPos[1], playerZ);
	                } else {
	                    double x = ent.getX() + Math.random() * 2000 - 1000;
	                    double y = ent.getY() + Math.random() * 2000 - 1000;
	                    if (y > boundingY) {
	                        y = boundingY;
	                    } else if (y < -boundingY) {
	                        y = -boundingY;
	                    }
	                    if (y < boundingX) {
	                        y = boundingX;
	                    } else if (y < -boundingX) {
	                        y = -boundingX;
	                    }
	                    ((MovingEntity) ent).move(x, y, playerZ);
	                }
	            }
	        }

	        // Check if entity is past bounding and adjust position if necessary
	        if (isPastBounding(ent)) {
	            movePastBounding(ent);
	        }

	        // Update entity dimensions
	        ent.setHeight(height);
	        ent.setWidth(width);

	    }

	    if (this.entities.contains(this.player)) {
	        this.player.move(mouseX, mouseY, !this.entities.contains(this.player));
	    } else {
	        this.player.move(0, 0, !this.entities.contains(this.player));
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
	private void displayBorders(double[] playerPos,Graphics g) {
		// Drawing map borders
		g.setColor(Color.CYAN);
		// Top
		g.fillRect(-boundingX - (int) playerPos[0] - 100, -boundingY - (int) playerPos[1] - getHeight() / 2 - 100,
				boundingX * 2 + getHeight() + 200, this.getHeight() + 100);
		// Left
		g.fillRect(-boundingX - (int) playerPos[0] - 100, -boundingY - (int) playerPos[1] - 100, getWidth() / 2 + 100,
				boundingY * 2 + getHeight() + 200);
		// Right
		g.fillRect(boundingX - (int) playerPos[0] + getWidth() / 2, -boundingY - (int) playerPos[1] - 100,
				this.getWidth(), boundingY * 2 + getHeight() + 200);
		// Bottom
		g.fillRect(-boundingX - (int) playerPos[0] - 100, boundingY - (int) playerPos[1] + getHeight() / 2,
				boundingX * 2 + getHeight() + 200, this.getHeight() + 100);
		//
	}
	private void drawGrid(double[] playerPos,Graphics g,int width) {
		g.setColor(new Color(0,200,0));
		for (int i = -boundingX; i <= boundingX*2; i += width) {
			for (int j = -boundingY-width; j <= boundingY; j += width) {
				g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1] + getWidth() / 2, width*3, width*3);
			}
		}
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		double[] playerPos = player.getPos();
		double playerZ = player.getZ();
		drawGrid(playerPos,g,100);
		displayBorders(playerPos,g);
		g.setColor(Color.BLACK);
		collideLogic(playerPos, playerZ,g);
	}
}
