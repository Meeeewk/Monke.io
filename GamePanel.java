import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class GamePanel extends AnimatedPanel {
	private ArrayList<Entity> entities = new ArrayList<>();
	private ArrayList<Consumable> consumables = new ArrayList<>();
	private Set<Entity>[][] chunkedEntities;
	private Player player;
	private int mouseX;
	private int mouseY;
	private int boundingX = 2000;
	private int boundingY = 2000;
	private boolean paused = false;
	private ArrayList<Entity> shuffledEntities = new ArrayList<>();

	@Override
	public void updateAnimation() {
		this.requestFocusInWindow();
	}

	public GamePanel() {
		addEventHandlers();
		this.setBackground(Color.GREEN);
		createObjects();
		this.chunkedEntities = new Set[2 * (boundingX / 500)][2 * (boundingY / 500)];
		this.entities.sort((o1, o2) -> o1.getDrawHeight() - o2.getDrawHeight());
		for (int i = 0; i < this.chunkedEntities.length; i++) {
			for (int j = 0; j < this.chunkedEntities[0].length; j++) {
				this.chunkedEntities[i][j] = new HashSet<Entity>();
			}
		}
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
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_Z) {
					player.setIsUp(true);
				}
				if (player.target != null && keyCode == KeyEvent.VK_RIGHT) {
					int currentIndex = shuffledEntities.indexOf(player.target);
					for (int i = currentIndex + 1; i < shuffledEntities.size(); i++) {
						Entity entity = shuffledEntities.get(i);
						if (entity instanceof MovingEntity) {
							player.target = entity;
							break;
						}
					}
				}
				if (player.target != null && keyCode == KeyEvent.VK_LEFT) {
					int currentIndex = shuffledEntities.indexOf(player.target);
					for (int i = currentIndex - 1; i >= 0; i--) {
						Entity entity = shuffledEntities.get(i);
						if (entity instanceof MovingEntity) {
							player.target = entity;
							break;
						}
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					player.setSprinting(false);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				ArrayList<Character> abilityKeys = player.getCurrentAbilityKeys();
				for (int i = 0; i < abilityKeys.size(); i++) {
					if (abilityKeys.get(i) == e.getKeyChar()) {
						player.activateAbility(i);
						break;
					}
				}
			}
		});
	}

	private double random(int bounding) {
		return Math.random() * 2 * bounding - bounding;
	}
	
	public void pause(boolean b) {
		this.paused = b;
	}
	private void createRandomWater(ArrayList<Entity> entities) {
		double randomX=random(boundingX);
		double randomY=random(boundingY);
		randomX=300;
		randomY=300;
		int mainSize=(int)(Math.random()*100+1000);
		double mainSize2=(Math.random()*100+1000)/1.3;
		entities.add(new Obstacle(randomX, randomY, "water", "water",mainSize,0));
		for(int i=0;i<10;i++) {
		entities.add(new Obstacle(randomX-(Math.random()*mainSize2-mainSize2/2), randomY-(Math.random()*mainSize2-mainSize2/2), "water", "water",(int)(Math.random()*20*i+250),0));
		}
		}
	public void createObjects() {
		this.player = new Player(this);
		this.entities.add(this.player);
//		for (int i = 0; i < (boundingX + boundingY) / 200; i++) {
//			this.entities.add(new Bot(random(boundingX), random(boundingY), null, boundingX, boundingY));
//		}
//		
//
//		for (int j = 0; j < (boundingX+boundingY)/30; j++) {
//			this.entities.add(new Consumable(random(boundingX),random(boundingY), "watermelon",60, 0, 15));
//			this.entities.add(new Consumable(random(boundingX),random(boundingY), "banana",40, 45, 0));
//		}
//		for (int i = 0; i < (boundingX+boundingY)/200; i++) {
//			this.entities.add(new Obstacle(random(boundingX), random(boundingY), "rock", "moveable",(int)(Math.random()*100+50),1));
//		}
//		for (int i = 0; i < (boundingX+boundingY)/500; i++) {
//			this.entities.add(new Obstacle(random(boundingX), random(boundingY), "tree", "non-moveable",(int)(Math.random()*500+300),2));
//		}
		createRandomWater(entities);
		shuffledEntities = new ArrayList<>(this.entities);
		Collections.shuffle(shuffledEntities);
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
		return (zDiff == 0) && distanceSquared < (radius1 + radius2) * (radius1 + radius2); // Adjust the condition to
																							// // consider
																							// the Z-coordinate
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
		double offset = 0;
		if (e instanceof MovingEntity || e2 instanceof MovingEntity) {
			offset = 1;
		}
		if (e2 instanceof Player || e instanceof Player) {
			offset = 1.5;
		}
		if (e instanceof Obstacle && e2 instanceof Obstacle) {
			offset = 1;
		}
		if (e2 instanceof Obstacle && ((Obstacle) e2).getState().equals("non-moveable")) {
			offset = 2;
		} else if (e instanceof Obstacle && ((Obstacle) e).getState().equals("non-moveable")) {
			offset = 0;
		}
		if (e instanceof Obstacle && ((Obstacle) e).getState().equals("moveable")) {
			offset = e.getDrawWidth() / 100.0 - e2.getDrawWidth() / 200.0;
		}
		if (e2 instanceof Obstacle && ((Obstacle) e2).getState().equals("moveable")) {
			offset = 2 - e.getDrawWidth() / 100.0 + e2.getDrawWidth() / 200.0;
		}
		if (e instanceof Consumable) {
			offset = 0;
		}
		if (e2 instanceof Consumable) {
			offset = 2;
		}
		if (e instanceof Consumable && e2 instanceof Consumable) {
			offset = 1;
		}
		if (isTouching(e, e2)) {
			if (e2 instanceof Obstacle && ((Obstacle) e2).getState().equals("non-moveable")) {
				double j = (value(e2, e, false) - (value(e, e2, false) + value(e2, e, false)) / 2);
				double t = (value(e2, e, true) - (value(e, e2, true) + value(e2, e, true)) / 2);
//				if(Math.abs((2 - offset) * j)>14||Math.abs(offset * j)>14) {
//					j/=9;
//					t/=9;
//				}
				e.setX(e.getX() + offset * j);
				e.setY(e.getY() + offset * t);
				e2.setX(e2.getX() - (2 - offset) * j);
				e2.setY(e2.getY() - (2 - offset) * t);
				if(isPastBounding(e2)) {
					e.setX(e.getX() - offset * j);
					e.setY(e.getY() - offset * t);
					e2.setX(e2.getX() + (2 - offset) * j);
					e2.setY(e2.getY() + (2 - offset) * t);
					e.setX(e.getX() + 2 * j);
					e.setY(e.getY() + 2 * t);
				}if(isPastBounding(e)) {
					e.setX(e.getX() - offset * j);
					e.setY(e.getY() - offset * t);
					e2.setX(e2.getX() + (2 - offset) * j);
					e2.setY(e2.getY() + (2 - offset) * t);
					e2.setX(e2.getX() - 2 * j);
					e2.setY(e2.getY() - 2 * t);
				}
				
			} else {
				double j = (value(e, e2, false) - (value(e2, e, false) + value(e, e2, false)) / 2);
				double t = (value(e, e2, true) - (value(e2, e, true) + value(e, e2, true)) / 2);
				//Checks if it is moving entity too far and then makes it go slower bit by bit
//				if(Math.abs((2 - offset) * j)>14||Math.abs(offset * j)>14) {
//					j/=9;
//					t/=9;
//				}
				e2.setX(e2.getX() + offset * j);
				e2.setY(e2.getY() + offset * t);
				e.setX(e.getX() - (2 - offset) * j);
				e.setY(e.getY() - (2 - offset) * t);
				if(isPastBounding(e2)) {
					e2.setX(e2.getX() - offset * j);
					e2.setY(e2.getY() - offset * t);
					e.setX(e.getX() + (2 - offset) * j);
					e.setY(e.getY() + (2 - offset) * t);
					e.setX(e.getX() - 2 * j);
					e.setY(e.getY() - 2 * t);
				}if(isPastBounding(e)) {
					e2.setX(e2.getX() - offset * j);
					e2.setY(e2.getY() - offset * t);
					e.setX(e.getX() + (2 - offset) * j);
					e.setY(e.getY() + (2 - offset) * t);
					e2.setX(e2.getX() + 2 * j);
					e2.setY(e2.getY() + 2 * t);
				}
			}
		}
	}

	private boolean isPastBounding(Entity e) {
		int offset = e.getDrawWidth() / 3;
		if (e.getX() > this.getBoundingX() - offset || e.getX() < -this.getBoundingX() + offset) {
			return true;
		} else if (e.getY() > this.getBoundingY() - offset || e.getY() < -this.getBoundingY() + offset) {
			return true;
		}
		return false;
	}

	private void movePastBounding(Entity e) {
		int offset = e.getDrawWidth() / 3;
		if (e.getX() > this.getBoundingX() - offset) {
			e.setX(this.getBoundingX() - offset);
		} else if (e.getX() < -this.getBoundingX() + offset) {
			e.setX(-this.getBoundingX() + offset);
		}
		if (e.getY() > this.getBoundingY() - offset) {
			e.setY(this.getBoundingY() - offset);
		} else if (e.getY() < -this.getBoundingY() + offset) {
			e.setY(-this.getBoundingY() + offset);
		}
	}

	private double getBoundingX() {
		return this.boundingX;
	}

	private double getBoundingY() {
		return this.boundingY;
	}

	private boolean angleCollide(Entity ent2, MovingEntity ent, int range) {
		//ent2 facing on ent
		double entFacing = ent.getFacingDir();
		double angle = 0;
		double vect_len = Math.sqrt(Math.pow(ent2.getX() - ent.getX(), 2) + Math.pow(ent2.getY() - ent.getY(), 2));
		if ((ent2.getY() - ent.getY()) * -1 < 0) {
			angle = Math.PI * 2 - Math.acos((ent2.getX() - ent.getX()) / vect_len);
		} else {
			angle = Math.acos((ent2.getX() - ent.getX()) / vect_len);
		}
		double facingOther = 3 * Math.PI / 2 - angle;
		return Math.abs(entFacing - facingOther) < Math.toRadians(range / 2);
	}

	private void collideLogic(double[] playerPos, double playerZ, Graphics g) {
		//basically everything for each frame
		int height = getHeight();
		int width = getWidth();
		ArrayList<Entity> delete = new ArrayList<>();
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
			if (!paused) {
				boolean zSet = false; // Flag to track if Z-coordinate has been set for the current entity
				int[] chunk = ent.getChunk();
				// Check for collision and set Z-coordinate
				for (int i = Math.max(chunk[0] - 1, 0); i < Math.min(chunk[0] + 2, this.chunkedEntities.length); i++) {
					for (int j = Math.max(chunk[1] - 1, 0); j < Math.min(chunk[1] + 2,
							this.chunkedEntities[0].length); j++) {
						for (Entity ent2 : this.chunkedEntities[i][j]) {
							if (this.entities.indexOf(ent2) != -1 && !(ent instanceof Obstacle&&((Obstacle)ent).getState()=="water")) {
								if (ent != ent2 && isTouching2(ent, ent2)) {
									if (((ent instanceof Obstacle && ((Obstacle) ent).getState().equals("non-moveable")
											&& (ent2 instanceof MovingEntity && ent2.getIsUp()
													&& ent2.getDrawWidth() < 160))
											|| (ent2 instanceof Obstacle
													&& ((Obstacle) ent2).getState().equals("non-moveable") && (ent.getIsUp()
															&& ent instanceof MovingEntity && ent.getDrawWidth() < 160)))) {

										if (ent instanceof Obstacle) {
											ent2.setIsUp(true);
											ent2.setZ(3.0);
										} else {
											zSet = true;
											ent.setIsUp(true);
											ent.setZ(3.0);
										}
										zSet = true; // Set flag to true after setting Z-coordinate
									} else if (((ent instanceof Obstacle
											&& ((Obstacle) ent).getState().equals("non-moveable")
											&& (ent2 instanceof Bot && ent2.getDrawWidth() < 160))
											|| (ent2 instanceof Obstacle
													&& ((Obstacle) ent2).getState().equals("non-moveable")
													&& (ent instanceof Bot && ent.getDrawWidth() < 160)))) {
										if ((ent instanceof Obstacle && (((Bot) ent2).getTarget() == null
												|| ((Bot) ent2).getTarget().getZ() == 3))
												|| (ent2 instanceof Obstacle && (((Bot) ent).getTarget() == null
														|| ((Bot) ent).getTarget().getZ() == 3))) {
											//CHECKME: Moving stuff up trees i think
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
											if (ent2 instanceof Player) {
												collide(ent2, ent);
											} else if (ent instanceof Player) {
												collide(ent, ent2);
											} else {
												collide(ent, ent2);
											}
											if (angleCollide((MovingEntity) ent, (MovingEntity) ent2, 90) || (ent2 instanceof Player && ((Player) ent2).getDashingFrames() > 0 && angleCollide((MovingEntity) ent2, (MovingEntity) ent, 160))) {
												if (ent instanceof Bot) {
													((Bot) ent).setTarget((MovingEntity) ent2);
												}
												//damage being added
												if (((MovingEntity) ent).getHitCooldown() == 0) {


													((MovingEntity) ent).setHitCooldown(30);
													if (ent2 instanceof Player && ((Player) ent2).getDashingFrames() > 0) {
														((MovingEntity) ent).setHealth(((MovingEntity) ent).getHealth()
																- ((MovingEntity) ent2).getDamage() * 2.2);
													}
													((MovingEntity) ent).setHealth(((MovingEntity) ent).getHealth()
															- ((MovingEntity) ent2).getDamage());

												}

												if (((MovingEntity) ent).getHealth() <= 0) {
													delete.add(ent);
													if (this.player.target == ent || ent instanceof Player) {
														this.player.setTarget(ent2);
													}
													((MovingEntity) ent).setHealth(0);
												}
											}
										} else {
											if (ent instanceof Consumable && ent2 instanceof MovingEntity
													&& angleCollide((Consumable) ent, (MovingEntity) ent2, 90)) {
												((Consumable) ent).consume((MovingEntity) ent2);
												delete.add(ent);
											} else {
												collide(ent, ent2);
											}
										}
									}
								}
								if (ent2 instanceof Obstacle && ent instanceof MovingEntity && ent.getZ() > 1.0
										&& !((MovingEntity) ent).getIsUp()) {
									double newZ = ent.getZ() + (0.5 - ent.getZ()) / 10;
									if (ent.getZ() >= (ent2.getZ() + 1) && newZ < (ent2.getZ() + 1)) {
										ent.setIsUp(true);
										ent.setZ((ent2.getZ() + 1));
									}
								}
							}
						}
					}
				}
				if (ent instanceof MovingEntity && ent.getIsUp() == true) {
					boolean isUp = false;
					for (int i = Math.max(chunk[0] - 1, 0); i < Math.min(chunk[0] + 2, this.chunkedEntities.length); i++) {
						for (int j = Math.max(chunk[1] - 1, 0); j < Math.min(chunk[1] + 2,
								this.chunkedEntities[0].length); j++) {
							for (Entity ent2 : this.chunkedEntities[i][j]) {
								if (ent2 instanceof Obstacle && isTouching2(ent2, ent)
										&& ((Obstacle) ent2).getState().equals("non-moveable")) {
									isUp = true;
								}
							}
						}
					}
					ent.setIsUp(isUp);
				}
				if (ent.getZ() < 2) {
					ent.setIsUp(false);
				}
				if (ent instanceof MovingEntity && !((MovingEntity) ent).getIsUp() && ent.getZ() > 1.0) {
					double newZ = ent.getZ() + (0.5 - ent.getZ()) / 10; // Adjust the descent speed as needed
					if (newZ < 1.0) {
						newZ = 1.0; // Ensure Z does not go below ground level (Z = 1)
					}
					ent.setZ(newZ);
				}
				if (!zSet && !(ent instanceof Obstacle)) {
					if (ent.getZ() > 1.0) {
						ent.setIsUp(false);
					}
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
							if (ent instanceof Bot) {
								((Bot) ent).move();
							}
						} else {
							((Bot) ent).move();
						}
					}
				}
				// Draw player UI over everything else
				if (ent instanceof Player && this.player.getHealth() > 0) {
					this.player.drawUI(g);
				}
				// Check if entity is past bounding and adjust position if necessary
				if (isPastBounding(ent)) {
					movePastBounding(ent);
				}

				// Update entity dimensions
				ent.setHeight(height);
				ent.setWidth(width);

			}
			else {
				if (ent instanceof Player) {
					// Adjust Z-coordinate for player entity when near edges of other entities or
					// objects
					this.player.draw(g, this.mouseX, this.mouseY);
				} else {
					ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
				}
				
				
			}

		}
		// Draw player UI over everything else

		if (this.entities.contains(this.player)) {
			this.player.drawUI(g);
			if (!paused) {
				this.player.move(mouseX, mouseY, !this.entities.contains(this.player));
			}

		} else {
			if (this.player.target == null || !this.entities.contains(this.player.target)) {
				int index = 0;
				while (true) {
					if (shuffledEntities.get(index) instanceof MovingEntity) {
						this.player.target = this.shuffledEntities.get(index);
						break;
					}
					index++;
				}
			}
			this.player.move();
		}
		for (int i = 0; i < delete.size(); i++) {
			try {
				if (delete.get(i) instanceof MovingEntity) {
					Bot newBot=new Bot(random(boundingX), random(boundingY),null, boundingX, boundingY);
					this.entities.add(newBot);
					this.shuffledEntities.add(newBot);
				}
				this.entities.remove(delete.get(i));
				this.shuffledEntities.remove(delete.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		delete.clear();
		this.entities.sort((o1, o2) -> o1.getDrawHeight() - o2.getDrawHeight());
	}

	private void displayBorders(double[] playerPos, Graphics g) {
		// Drawing map borders
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

	private void drawGrid(double[] playerPos, Graphics g, int width) {
		g.setColor(new Color(0, 210, 0));
		for (int i = -boundingX; i <= boundingX * 2; i += width) {
			for (int j = (int) (-boundingY - width * 1.1); j <= boundingY; j += width) {
				g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1] + getWidth() / 2, width * 3, width * 3);
			}
		}
	}

	private void calcChunks() {
		for (Entity ent : this.entities) {
			double[] pos = ent.getPos();
			int[] calculatedChunk = {
					Math.max(0,
							Math.min(this.chunkedEntities.length - 1,
									(int) (pos[0] / 500 + this.chunkedEntities.length / 2))),
					Math.max(0, Math.min(this.chunkedEntities[0].length - 1,
							(int) (pos[1] / 500 + this.chunkedEntities.length / 2))) };
			int[] entChunk = ent.getChunk();
			if (!calculatedChunk.equals(entChunk)) {
				this.chunkedEntities[entChunk[0]][entChunk[1]].remove(ent);
				this.chunkedEntities[calculatedChunk[0]][calculatedChunk[1]].add(ent);
				ent.setChunk(calculatedChunk);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		calcChunks();
		double[] playerPos = player.getPos();
		double playerZ = player.getZ();
		drawGrid(playerPos, g, 100);
		displayBorders(playerPos, g);
		g.setColor(Color.BLACK);
		collideLogic(playerPos, playerZ, g);
		// scawy batel woyal
//		if(Math.random()>0.1&&this.boundingX>200&&this.boundingY>200) {
//		this.boundingX-=19;
//		this.boundingY-=19;
//		}
	}
}
