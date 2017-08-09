import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Georg Plaz.
 */
public class SettingsFrame extends JFrame{
    private Settings settings;
    private JLabel drawTextLabel = new JLabel("draw text:");
    private JLabel repulsivenessLabel = new JLabel("repulsiveness:");
    private JCheckBox drawTextCheckBox = new JCheckBox();
    private JSlider repulsivenessSlider = new JSlider(0, 100);
    public SettingsFrame(final Settings settings){
        this.settings = settings;
        JPanel container = new JPanel();
        add(container);
        container.add(drawTextLabel);
        container.add(drawTextCheckBox);
        drawTextCheckBox.setSelected(settings.isDrawingText());
        drawTextCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setDrawText(drawTextCheckBox.isSelected());
                updatedSettings();
            }
        });

        container.add(repulsivenessLabel);
        container.add(repulsivenessSlider);
        repulsivenessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                settings.setRepulsiveness(toValue(repulsivenessSlider.getValue()));
                updatedSettings();
            }
        });
        repulsivenessSlider.setValue(toSliderValue((float) settings.getRepulsiveness()));

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }

    public void updatedSettings(){
        try {
            settings.toJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int toSliderValue(float value){
        return 101-Math.round(value*20);
    }

    private float toValue(int value){
        return (101-value)/20.f;
    }

}
