import at.plaz.tuples.DefaultDoubleTupleF;
import at.plaz.tuples.DoubleTuple;

import javax.swing.*;
import java.awt.*;

/**
* Created by Georg Plaz.
*/
public class ToastMessage extends JDialog {
    int miliseconds;
    public ToastMessage(String toastString, int time) {
        this(null, toastString, time);
    }
    public ToastMessage(DoubleTuple centerPos, String toastString, int time) {
        this.miliseconds = time;
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
//        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        getContentPane().add(panel, BorderLayout.CENTER);

        JLabel toastLabel = new JLabel("");
        toastLabel.setText(toastString);
        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        toastLabel.setForeground(Color.WHITE);
        setBounds(100, 100, toastLabel.getPreferredSize().width+20, 31);

        setAlwaysOnTop(true);
        if(centerPos==null){
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            centerPos = new DefaultDoubleTupleF(dim.width/2, dim.height*2./3);
        }
        setLocation((int) centerPos.getFirst().doubleValue()-getSize().width/2,
                (int) centerPos.getSecond().doubleValue()-getSize().height/2);
        panel.add(toastLabel);
        setVisible(false);

        new Thread(){
            public void run() {
                try {
                    Thread.sleep(miliseconds);
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
