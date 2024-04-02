import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class GamePanel extends AnimatedPanel {
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private int x = 10;
    private int y = 10;
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
    }

    public void addEventHandlers() {
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
    }

    public void createObjects() {
    	this.player = new Player();
        for (int i = 0; i < 5; i++) {
        	this.entities.add(new Bot(Math.random() * 1000 - 500, Math.random() * 1000 - 500));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double[] playerPos = player.getPos();
        for (int i = -2000; i <= 2000; i += 100) {
            for (int j = -2000; j <= 2000; j += 100) {
                g.fillOval(i - (int) playerPos[0], j - (int) playerPos[1], 20, 20);
            }
        }
        
        int height = getHeight();
        int width = getWidth();
        this.player.setHeight(height);
        this.player.setWidth(width);
        this.player.draw(g, this.mouseX, this.mouseY);
        this.player.move(mouseX, mouseY);
        
        for (Entity ent : this.entities) {
        	ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
        	ent.move((int) playerPos[0], (int) playerPos[1]);
        	ent.setHeight(height);
        	ent.setWidth(width);
        }
    }
}