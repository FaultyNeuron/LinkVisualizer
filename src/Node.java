import at.plaz.tuples.DefaultDoubleTupleF;
import at.plaz.tuples.DoubleTuple;
import at.plaz.tuples.DoubleTuples;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static at.plaz.tuples.DoubleTuples.*;

/**
* Created by Georg Plaz.
*/
public class Node implements HasCenter {

    public static final String TOP_LEVEL_DOMAIN_SEPARATOR = ".";
    public static final String LINK_SEPARATOR = "/";
    private final int depth;
    private final double dimension;
    private Image image;
    private Node parent;
    private String link;
    private DoubleTuple topLeft;
    private File folder;
    private Map<String, Node> children = new HashMap<>();
    private boolean active;

    public Node(Image image, Node parent, DoubleTuple topLeft, File folder) {
        this(image, parent, topLeft, folder, null);
    }

    public Node(Image image, Node parent, DoubleTuple topLeft, File folder, String link) {
        this.image = image;
        this.parent = parent;
        this.topLeft = topLeft;
        this.folder = folder;
        if(parent!=null){
            parent.children.put(folder.getName(), this);
            depth = parent.getDepth()+1;
        }else{
            depth = 0;
        }
        if(link == null){
            this.link = retrieveLink();
        }else{
            this.link = link;
        }
        this.dimension = 100./(depth+1);
    }

    public String getLink(){
        return link;
    }
    private String retrieveLink(){
        return getLink(new StringBuilder());
    }

    private String getLink(StringBuilder builder){
        if(parent==null){
            builder.insert(0, TOP_LEVEL_DOMAIN_SEPARATOR);
            builder.insert(0, getImage().getImageSource());
            builder.replace(builder.length()-1,builder.length(),"");
            return builder.toString();
        }else{
            builder.insert(0, LINK_SEPARATOR);
            builder.insert(0, getImage().getImageSource());
            return parent.getLink(builder);
        }
    }

    public Node(NodeInfo nodeInfo, Node parent, DoubleTuple topLeft, File folder) {
        this(new Image(nodeInfo.getImageSource()), parent, topLeft, folder, nodeInfo.getLink());
    }

    public void paintBackgroundGraphics(Graphics g, DoubleTuple delta, double scale, Settings settings){
        DoubleTuple newTopLeft = DoubleTuples.add(multiply(topLeft, scale), delta);
//        DoubleTupel dimensionDouble = new DoubleTupel(dimension*scale);
        if(parent!=null){
            Shape.drawLine(g, DoubleTuples.add(multiply(getCenter(), scale), delta), DoubleTuples.add(multiply(parent.getCenter(), scale), delta), Color.BLACK);
        }
        if(settings.isDrawingText()){
            Shape.drawString(g, toShortString(image.getImageSource()), DoubleTuples.add(newTopLeft, 0, dimension*scale + 11));
        }
    }

    public DoubleTuple getCenter(){
        return DoubleTuples.add(topLeft, dimension/2);
    }

    private final static int MAX_TEXT_LENGTH = 15;
    private String toShortString(String toShorten){
        if(toShorten.length()>MAX_TEXT_LENGTH){
            return toShorten.substring(0, MAX_TEXT_LENGTH-3)+"...";
        }
        return toShorten;
    }

    public void paint(Graphics g, DoubleTuple delta, double scale){
        DoubleTuple newTopLeft = DoubleTuples.add(multiply(topLeft, scale), delta);
        DoubleTuple dimensionDouble = new DefaultDoubleTupleF(dimension*scale);
        Shape.fillRect(g, newTopLeft, dimensionDouble, Color.WHITE);
        Shape.drawRect(g, newTopLeft, dimensionDouble, Color.BLACK, active?3:1);
        image.paint(g, newTopLeft, dimension*scale);
    }

    public DoubleTuple getTopLeft() {
        return topLeft;
    }

    public void update(List<Node> others, Settings settings) {
        DoubleTuple delta = ZEROS;
        for(Node other : others){
            if(other!=this) {
                DoubleTuple vec = subtract(topLeft, other.topLeft);
                double distance = vec.length();
                if(distance > 0){
                    double val;
                    double factor = 100/(dimension+other.dimension);
//                    System.out.println(factor);
                    if (other.parent == this || parent == other) {
                        val = -adjacent(factor*distance/50, dimension+other.dimension);
                    } else {
                        val = apart(settings.getRepulsiveness()*factor*distance/50, dimension+other.dimension);
                    }
                    delta = add(delta, multiply(normalize(vec), val*(0.2)));
                }
            }
        }
        move(delta);
    }

    private double adjacent(double distance, double dimensionSum){
        return 2*Math.log(distance/1);
    }

    private double apart(double distance, double dimensionSum){
        return 1/(distance*distance);
    }

    public Node getChild(String id) {
        return children.get(id);
    }

    public Collection<Node> getChildren() {
        return children.values();
    }

    public Collection<String> getChildrenFolderNames() {
        List<String> childNames = new LinkedList<>();
        for(Node node : getChildren()){
            childNames.add(node.getFolder().getName());
        }
        return childNames;
    }

    public NodeInfo toNodeInfo(){
        return new NodeInfo(image.getImageSource(), getLink(), getChildrenFolderNames());
    }

    public File getFolder() {
        return folder;
    }

    public void toJson() throws IOException {
        toJson(NodeInfo.NODE_INFO_NAME);
    }
    public void toJson(String fileName) throws IOException {
        toNodeInfo().toJson(folder, fileName);
    }

    public double getDimension() {
        return dimension;
    }

    public int getDepth() {
        return depth;
    }

    public boolean liesIn(DoubleTuple position){
//        DoubleTupel newTopLeft = topLeft.mult(scalar).add(delta);
//        DoubleTupel dimensionDouble = new DoubleTupel(dimension*scalar);
//        DoubleTupel topLeft = newTopLeft.sub(dimension/2);
        return position.bothGreaterEqual(topLeft) && DoubleTuples.add(topLeft, dimension).bothGreaterEqual(position);
    }

    public Image getImage() {
        return image;
    }

    public void paintFocused(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void move(DoubleTuple delta) {
        topLeft = DoubleTuples.add(topLeft, delta);
    }

    public void setCenter(DoubleTuple mousePoint) {
        move(subtract(mousePoint, getCenter()));
    }

    public Node getParent() {
        return parent;
    }

    public void removeChild(Node toDelete) {
        children.remove(toDelete.getFolder().getName());
    }
}
