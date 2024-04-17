import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class GamePanel extends AnimatedPanel {
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private Player player;
    private int mouseX;
    private int mouseY;
    
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
    	for(int i=0;i<5;i++) {
        	this.entities.add(new Bot(Math.random()*1000, Math.random()*1000));
    	}
    	this.entities.add(this.player);
    }
    private boolean isTouching(Entity e,Entity e2) {
    	double x1 = e2.getX();
    	double y1 = e2.getY();
    	double x2 = e.getX();
    	double y2 = e.getY();
    	//radius is sketchy, change the 28 to different numbers and see
    	int radius1 = (e2.getDrawWidth())/2;
    	radius1 = (int) (radius1 * 0.72);
    	int radius2 = (e.getDrawWidth())/2;
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
    	int radius1 = (e.getDrawWidth())/2;
    	radius1 = (int) (radius1 * 0.72);
    	if(isY) {
    		return y1+(e.compareTo(e2)==0?0:radius1*(y2-y1)/e.compareTo(e2));
    	}
    	else {
    		return x1+(e.compareTo(e2)==0?0:radius1*(x2-x1)/e.compareTo(e2));
    	}
    }
    private void collide(Entity e,Entity e2) {
    	if (isTouching(e,e2)) {
    		double j = 2*(value(e,e2,false)-(value(e2,e,false)+value(e,e2,false))/2);
    		double t = 2*(value(e,e2,true)-(value(e2,e,true)+value(e,e2,true))/2);
    		e2.setX(e2.getX()+j);
			e2.setY(e2.getY()+t);
		}
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double[] playerPos = player.getPos();
        for (int i = -2000; i <= 2000; i += 100) {
            for (int j = -2000; j <= 2000; j += 100) {
                g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1], 1, 200);
                g.drawRect(i - (int) playerPos[0], j - (int) playerPos[1], 200, 1);
            }
        }
        
        int height = getHeight();
        int width = getWidth();

        
        for (Entity ent : this.entities) {
        	if(isTouching(ent,this.player)) {
        		collide(this.player,ent);
        	}
            for (Entity ent2 : this.entities) {
        	if(ent!=ent2&&isTouching(ent,ent2)) {
        		collide(ent,ent2);
        	}
            }
            if (ent instanceof Player) {
            	this.player.draw(g, this.mouseX, this.mouseY);
                this.player.move(mouseX, mouseY);
            } else {
            	ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
            	ent.move((int) playerPos[0], (int) playerPos[1]);
            }
        	ent.setHeight(height);
        	ent.setWidth(width);
        }
    }
}