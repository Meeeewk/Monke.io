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
        this.entities.add(this.player);
        this.entities.add(new Bot());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        double[] playerPos = player.getPos();
        for (int i = -150; i <= 150; i += 50) {
            for (int j = -150; j <= 150; j += 50) {
                g.fillOval(i - (int) playerPos[0], j - (int) playerPos[1], 20, 20);
            }
        }
        
        
        for (Entity ent : this.entities) {
        	ent.draw(g, (int) playerPos[0], (int) playerPos[1]);
        	ent.move(this.mouseX - 200, this.mouseY - 200);
        }
    }
}