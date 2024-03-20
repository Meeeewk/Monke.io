import java.awt.event.InputEvent;
import javax.swing.*;

class Main extends JFrame{
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
        this.setTitle("walk around");
        this.setSize(400, 400);
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
                this.gamePanel.updateAnimation();

                repaint();

                Thread.sleep(34);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}