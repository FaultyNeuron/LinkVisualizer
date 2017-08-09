import javax.swing.*;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class VisualizerFrame extends JFrame{
    public static final String DEFAULT_TITLE = "link visualizer";
    public VisualizerFrame() throws IOException {
        resetTitle();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ContentPanel contentPanel = new ContentPanel(this);
        add(contentPanel);
        contentPanel.focusTextField();
        pack();
    }
    public void resetTitle(){
        setTitle(DEFAULT_TITLE);
    }
}
