import java.awt.Color;
import javax.swing.JPanel;

/**
 * 
 * @author Matthew Kokhan
 * <br>
 * <b>Extends: </b> JPanel
 * Base for all our panels
 * 
 *
 */
public abstract class AnimatedPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    /**
     * Empty
     */
    public AnimatedPanel() {
        // JPanel constructor with true says to use DoubleBuffering
        super(false);
    }

    // All child classes must implement this method
    public abstract void updateAnimation();

    /**
     * 
     * @return A color with random RGB values
     */
    public static Color getRandColor() {
        int r = (int) (Math.random() * 255);
        int rgbInc = (int) (Math.random() * 255);
        int g = (r + rgbInc) % 256;
        int b = (g + rgbInc) % 256;
        return new Color(r, g, b);
    }
}
