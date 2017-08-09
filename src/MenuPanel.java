import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Georg Plaz.
 */
public class MenuPanel extends JPanel {
    private final VisualizerFrame visualizerFrame;
    private final ContentPanel contentPanel;
    //    private ContentPanel contentPanel;
    private JPanel addImageBar = new JPanel();
    private JPanel nodeDetail = new JPanel();
    private LinkButton link;
    private JTextField userInputField = new JTextField();
    private Node activeNode;
    private JButton deleteButton = new JButton("delete");
    private JButton settingsButton = new JButton("settings");
    private SettingsFrame settingsFrame;

    public MenuPanel(VisualizerFrame visualizerFrame, ContentPanel contentPanel, final Settings settings){
        this.visualizerFrame = visualizerFrame;
        this.contentPanel = contentPanel;
//        this.contentPanel = contentPanel;
        final JLabel label = new JLabel("link:");
        final JButton okButton = new JButton("add");
        final JButton saveButtonOne = new JButton("save");

        double textFieldWidth = DrawingPanel.SIZE-
                (label.getPreferredSize().width+
                okButton.getPreferredSize().width+
                saveButtonOne.getPreferredSize().width+
                settingsButton.getPreferredSize().width+30);
        userInputField.setPreferredSize(new Dimension((int) textFieldWidth, (int) userInputField.getPreferredSize().getHeight()));
        add(addImageBar);
        addImageBar.add(label);
        addImageBar.add(userInputField);
        addImageBar.add(okButton);
        addImageBar.add(settingsButton);
        addImageBar.add(saveButtonOne);

        JButton saveButtonTwo = new JButton("save");
        JLabel linkLabel = new JLabel("link: ");
        double linkTextWidth = DrawingPanel.SIZE -
                (linkLabel.getPreferredSize().width+
                deleteButton.getPreferredSize().width+
                saveButtonTwo.getPreferredSize().width+30);
        try {
            link = new LinkButton("https://en.wikipedia.org");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        link.setPreferredSize(new Dimension((int) linkTextWidth, (int) link.getPreferredSize().getHeight()));
        add(nodeDetail);
        nodeDetail.add(linkLabel);
        nodeDetail.add(link);
        nodeDetail.add(deleteButton);
        nodeDetail.add(saveButtonTwo);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuPanel.this.contentPanel.delete(activeNode);
            }
        });
        ActionListener saveActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(MenuPanel.this.contentPanel.saveScreen()){
                }
            }
        };
        saveButtonOne.addActionListener(saveActionListener);
        saveButtonTwo.addActionListener(saveActionListener);

        closeImage();
        setPreferredSize(new Dimension(DrawingPanel.SIZE, (int) getPreferredSize().getHeight()));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUserInput();
            }
        });
        userInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processUserInput();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(settingsFrame==null || settingsFrame.isDisplayable()){
                    settingsFrame = new SettingsFrame(settings);
                }
            }
        });
    }

    public void focusTextField(){
        userInputField.grabFocus();
        userInputField.requestFocus();
    }

    private void processUserInput(){
        String input = userInputField.getText();
        userInputField.setText("");
        try {
            contentPanel.drawInput(input);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void closeImage(){
        addImageBar.setVisible(true);
        nodeDetail.setVisible(false);
        visualizerFrame.resetTitle();
    }

    public void openImage(Node node){
        visualizerFrame.setTitle("Image: " + node.getImage().getImageSource());
        deleteButton.setEnabled(node.getParent() != null);
        addImageBar.setVisible(false);
        nodeDetail.setVisible(true);
        activeNode = node;
        link.setLink(node.getLink());
    }
}
