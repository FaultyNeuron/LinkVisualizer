import com.sun.jndi.toolkit.url.Uri;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Georg Plaz.
 */
public class LinkButton extends JButton{
    private URI uri;

    public LinkButton(String uri) throws URISyntaxException {
        this(new URI(uri));
    }
    public LinkButton(URI uri){
        setLink(uri);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorderPainted(false);
        setOpaque(false);
        setBackground(Color.WHITE);
    }

    public void setLink(URI uri){
        this.uri = uri;
        setText("<HTML><FONT color=\"#000099\"><U>"+formatForDisplay(uri.toString())+"</U></FONT></HTML>");
        setToolTipText(uri.toString());
        removeActionListeners();
        addActionListener(new OpenUrlAction(uri));
        setEnabled(true);
    }
    private void removeActionListeners(){
        for(ActionListener actionListener : getActionListeners()){
            removeActionListener(actionListener);
        }
    }

    public void setLink(String link){
        try {
            setLink(new URI(link));
        } catch (URISyntaxException e) {
            setTextLink(link);
        }
    }

    private void setTextLink(String textLink){
        setText(textLink);
        setToolTipText("");
        setEnabled(false);
        removeActionListeners();
    }

    private String formatForDisplay(String link){
//        for(String prefix : new String[]{"https://", "http://", "www."}){
//            if(link.startsWith(prefix)){
//                link = link.substring(prefix.length());
//            }
//        }
        return link;
    }

    class OpenUrlAction implements ActionListener {
        private URI uri;
        OpenUrlAction(URI uri) {
            this.uri = uri;
        }

        @Override public void actionPerformed(ActionEvent e) {
            open(uri);
        }
    }

    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                if(!uri.toString().startsWith("http://")){
                    try {
                        open(new URI("http://"+uri.toString()));
                    } catch (URISyntaxException e1) {
                        cantOpenLink();
                    }
                }else{
                    cantOpenLink();
                }
            }
        } else {
            cantOpenLink();
        }
    }

    private void cantOpenLink(){
        ToastMessage message = new ToastMessage("couldn't open link!", 3000);
        message.setVisible(true);
    }
}
