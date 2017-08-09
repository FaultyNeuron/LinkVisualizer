import at.plaz.tuples.DefaultDoubleTupleF;
import at.plaz.tuples.DoubleTuples;
import com.google.gson.Gson;
import at.plaz.tuples.DoubleTuple;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Georg Plaz.
 */
public class ContentPanel extends JPanel{
    public static final String ROOT_FOLDER_NAME = "root";
    public static final File ROOT_FOLDER = new File(ROOT_FOLDER_NAME);
    public static final String ROOT_NODE_CONTENT = "www";
    public static final String ROOT_NODE_LINK = "https://en.wikipedia.org/wiki/Internet";
    public static final int MAX_LINK_LENGTH = 20;
    //    public static final int SIZE = 500;
    private Node root;
    private Gson gson = new Gson();
    private List<Node> nodes = new LinkedList<>();
    private DrawingPanel drawingPanel;
    private MenuPanel menuPanel;
    private Settings settings;
;

    public ContentPanel(VisualizerFrame visualizerFrame) throws IOException {
        if(!ROOT_FOLDER.exists()){
            root = createNode(ROOT_FOLDER, null, ROOT_NODE_CONTENT, ROOT_NODE_LINK);
        }else{
            ROOT_FOLDER.mkdirs();
            root = readNode(ROOT_FOLDER, null);
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        settings = Settings.read(null);
        menuPanel = new MenuPanel(visualizerFrame, this, settings);
        drawingPanel = new DrawingPanel(root, nodes, menuPanel, settings);
        add(drawingPanel);
        add(menuPanel);
    }

    public Node createNode(File nodeFolder, Node parentNode, String imageSource, String link) throws IOException {
        DoubleTuple center;
        if(parentNode==null){
            center = new DefaultDoubleTupleF(DrawingPanel.SIZE/2);
        }else{
            center = new DefaultDoubleTupleF(new Random(),
                    drawingPanel.screenToCoord(DoubleTuples.ZEROS),
                    drawingPanel.screenToCoord(DrawingPanel.SCREEN_DIMENSIONS));
        }
        nodeFolder = createFolder(nodeFolder.getParentFile(), nodeFolder.getName());
        Node toReturn = new Node(new Image(imageSource), parentNode, center, nodeFolder, link);
        nodes.add(toReturn);
        NodeInfo nodeInfo = toReturn.toNodeInfo();
        nodeInfo.toJson(nodeFolder, NodeInfo.NODE_INFO_NAME);

        if(parentNode!=null){
            parentNode.toJson();
        }
        return toReturn;
    }

    public static String cleanFileName(String toClean, boolean lowerCase){
        String cleaned = toClean.replaceAll("[^a-zA-Z0-9._!§$%&()=]", "%").replaceAll("\\s+", "_");
        if(cleaned.length() > MAX_LINK_LENGTH){
            cleaned = cleaned.substring(0, MAX_LINK_LENGTH);
        }
        if(!cleaned.equals(toClean)){
            Random random = new Random(Image.toLong(toClean.getBytes()));
            cleaned+="_"+(Math.round(random.nextInt(2<<10))+2<<9);
        }
        if(lowerCase){
            return cleaned.toLowerCase();
        }else{
            return cleaned;
        }
    }

    private File createFolder(File parent, String name){
        File toReturn = new File(parent, cleanFileName(name, true));
        toReturn.mkdirs();
        return toReturn;
    }

    public Node readNode(File parentFolder, Node parentNode) throws FileNotFoundException {
//        BufferedReader br = new BufferedReader(new FileReader(new File(parentFolder, NodeInfo.NODE_INFO_NAME+".json")));
        NodeInfo nodeInfo = NodeInfo.read(parentFolder);
        Node toReturn = new Node(nodeInfo, parentNode, new DefaultDoubleTupleF(new Random(), DrawingPanel.SIZE), parentFolder);
        nodes.add(toReturn);
        for(String childFolder : nodeInfo.getChildrenFolder()){
            readNode(new File(parentFolder, childFolder), toReturn);
        }
        return toReturn;
    }

    public void drawInput(String input) throws IOException {
        StringBuilder splitCollector = new StringBuilder();
        String[] prefixes = new String[]{"http://", "https://", "www."};
        boolean found = true;
        while(found){
            found = false;
            for(String s : prefixes){
                if(input.startsWith(s)){
                    found = true;
                    splitCollector.append(input.substring(0, s.length()));
                    input = input.substring(s.length());
                }
            }
        }
        if(input.isEmpty()){
            return;
        }
//        DrawingPanel drawingPanel = new DrawingPanel(nodes);
//        removeAll();
//        add(drawingPanel);
//        revalidate();
        File lastFolder = root.getFolder();
        Node lastNode = root;
        for(String split : input.split("/")) {
            splitCollector.append(split);
            File currentFolder = new File(lastFolder, split);
            Node currentNode = lastNode.getChild(currentFolder.getName());
            if(currentNode==null){
                currentFolder.mkdirs();
                currentNode = createNode(currentFolder, lastNode, split, splitCollector.toString());
            }
            splitCollector.append("/");
            lastFolder = currentNode.getFolder();
            lastNode = currentNode;
        }
//        save(drawingPanel);
    }

//    private static void save(JPanel jPanel){
//        BufferedImage bImg = new BufferedImage(jPanel.getWidth(), jPanel.getWidth(), BufferedImage.TYPE_INT_RGB);
//        Graphics2D cg = bImg.createGraphics();
//        jPanel.paintAll(cg);
//        try {
//            int i = 0;
//            String baseName = "output";
//            File output;
//            while((output = new File(baseName+"_"+i+".png")).exists()){
//                i++;
//            }
//            if (ImageIO.write(bImg, "png", output)){
//                System.out.println("-- saved");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void delete(Node toDelete) {
        drawingPanel.delete(toDelete);
    }

    public boolean saveScreen() {
        return drawingPanel.save(new File("images"));
    }

    public void focusTextField() {
        menuPanel.focusTextField();
    }
}
