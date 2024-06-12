import java.awt.event.InputEvent;
import javax.swing.*;
import java.util.Timer;

class Main extends JFrame{
	public static boolean showHitBoxes=false;
    private static volatile boolean done = false;
    private AnimatedPanel gamePanel;
    public static void main(String[] args) throws InterruptedException{
        Main theGUI = new Main();

        SwingUtilities.invokeLater( () -> theGUI.createFrame(theGUI) );

        synchronized (theGUI) {
            theGUI.wait();
        }

        while (true) {
            theGUI.startAnimation();
        }
    }

    public void createFrame(Object semaphore) {
        this.setTitle("Monke.io");
        this.setSize(1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setVisible(true);
        gamePanel.setVisible(true);
        synchronized (semaphore) {
            semaphore.notify();
        }
    }

    public void startAnimation() {
        Main.done = false;
        try {
            while (!Main.done) {
            	long startTime = System.currentTimeMillis();
                this.gamePanel.updateAnimation();
                
                repaint();
                long endTime = System.currentTimeMillis();
                Thread.sleep(Math.max(33 - (endTime - startTime), 0));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
