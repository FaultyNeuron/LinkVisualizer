import at.plaz.tuples.DefaultDoubleTupleF;
import at.plaz.tuples.DoubleTuple;
import at.plaz.tuples.DoubleTuples;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static at.plaz.tuples.DoubleTuples.*;

/**
* Created by Georg Plaz.
*/
public class DrawingPanel extends JPanel implements HasCenter {
    public static final int SIZE = 500;
    public static final DoubleTuple SCREEN_DIMENSIONS = new DefaultDoubleTupleF(SIZE);
    private final Node root;
    private final List<Node> nodes;
    private final MenuPanel menuPanel;
    private final Settings settings;
    private Node lockedNode = null;
    private Node mouseOverNode = null;
    private DoubleTuple dragPoint = null;
    private int dragCounter = 0;
    private Dragging dragging = Dragging.NONE;
    private Node draggingNode = null;
    private StepCamera camera = new StepCamera(new DefaultDoubleTupleF(SIZE/2),1, 100);
    boolean lockOnClick = true;
    private boolean draggingActive = false;
    public static final int MAX_DRAG_COUNTER = 20;
    public static final int MAX_DRAG_DISTANCE = 20;

    public DrawingPanel(final Node root, final List<Node> nodes, MenuPanel menuPanel, final Settings settings){
        this.root = root;
        this.nodes = nodes;
        this.menuPanel = menuPanel;
        this.settings = settings;
        camera.zoomAndPan(root, 1, 0);
        new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        List<Node> tempList = new LinkedList<>(nodes);
                        for (Node node : tempList) {
                            node.update(tempList, settings);
                        }
                        Point mouseAwtPoint = getMousePosition();
                        if(mouseAwtPoint!=null && dragPoint!=null) {
                            DoubleTuple mousePoint = new DefaultDoubleTupleF(mouseAwtPoint);
                            if (!draggingActive) {
                                double dist = mousePoint.distance(dragPoint);
                                if (dragCounter++ > MAX_DRAG_COUNTER || dist > MAX_DRAG_DISTANCE) {
                                    draggingActive = true;
                                }
                            }
                            if (draggingActive) {
                                if (dragging != Dragging.NONE) {
                                    lock(null);
                                    if (dragging == Dragging.PANE) {
                                        DoubleTuple delta = divide(subtract(dragPoint, mousePoint), camera.getZoom());
                                        camera.pan(DoubleTuples.add(camera.getCenter(), delta), 0);
                                        dragPoint = mousePoint;
                                    } else {
                                        draggingNode.setCenter(screenToCoord(mousePoint));
                                    }
                                }
                            }
                        }
                        camera.update();
                        DrawingPanel.this.repaint();
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                lock(null);
                double targetScalar = camera.getTargetZoom()*(1+0.2*e.getUnitsToScroll());
                camera.zoom(targetScalar, 50);
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mouseOverNode!=null) {
                    if (lockOnClick) {
                        lock(mouseOverNode);
                    } else {
                        if (camera.getTarget() != DrawingPanel.this) {
                            resetView();
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(mouseOverNode==null){
                    dragging = Dragging.PANE;
                }else{
                    draggingNode = mouseOverNode;
                    dragging = Dragging.NODE;
                }
                dragPoint = new DefaultDoubleTupleF(e.getX(), e.getY());
                dragCounter=0;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = Dragging.NONE;
                dragCounter = 0;
                dragPoint = null;
                draggingActive = false;
            }
        });
    }

    private void resetView() {
        lock(null);
        camera.abortPan();
        camera.zoom(1);
    }

    public DoubleTuple screenToCoord(DoubleTuple toTransform){
        DoubleTuple screenTopLeft = subtract(camera.getCenter(), SIZE/(2* camera.getZoom()));
        return DoubleTuples.add(divide(toTransform, camera.getZoom()), screenTopLeft);
    }

    public void lock(Node toLock){
        if(toLock==null){
            lockOnClick = true;
            camera.abortZoomAndPan();
            menuPanel.closeImage();
        }else{
            lockOnClick = false;
            camera.zoomAndPan(mouseOverNode, SIZE / mouseOverNode.getDimension(), 100);
            menuPanel.openImage(toLock);
        }
        lockedNode = toLock;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        java.util.List<Node> toDraw = new LinkedList<>(nodes);
        java.awt.Point mousePoint = getMousePosition();
        boolean hasMouse = mousePoint!=null;
        DoubleTuple mousePosition = null;
        if(hasMouse){
            mousePosition = new DefaultDoubleTupleF(mousePoint.getX(), mousePoint.getY());
        }
        DoubleTuple delta = multiply(subtract(camera.getCenter(), SIZE/(2* camera.getZoom())), -camera.getZoom());

        Node topNode = null;
        if(dragging==Dragging.NONE) {
            for (Node node : toDraw) {
                if (hasMouse && node.liesIn(screenToCoord(mousePosition))) {
                    topNode = node;
                }
            }
            if (mouseOverNode != topNode) {
                if (mouseOverNode != null) {
                    mouseOverNode.paintFocused(false);
                }
                if (topNode != null) {
                    topNode.paintFocused(true);
                }
                mouseOverNode = topNode;
            }
        }else if(dragging==Dragging.NODE){
            if (mouseOverNode != null) {
                mouseOverNode.paintFocused(false);
            }
            mouseOverNode = draggingNode;
            draggingNode.paintFocused(true);
        }
//        if(lockedNode==null && getMouse){
//
//        }
        for(Node node : toDraw){
            node.paintBackgroundGraphics(g, delta, camera.getZoom(), settings);
        }
        for(Node node : toDraw){
            node.paint(g, delta, camera.getZoom());
        }
    }

    public DoubleTuple getCenter() {
        return new DefaultDoubleTupleF(SIZE / 2);
    }

    public void delete(Node toDelete) {
        boolean hadChildren = !toDelete.getChildren().isEmpty();
        List<Node> toDeleteList = new LinkedList<>();
        deleteRec(toDelete, toDeleteList);
        nodes.removeAll(toDeleteList);
        toDelete.getParent().removeChild(toDelete);
        toDelete.getFolder().delete();
        try {
            toDelete.getParent().toJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resetView();
        ToastMessage deleteNotification = new ToastMessage(getToastPosition(), "deleted "+toDelete.getLink()+(hadChildren?" and all child nodes":""), 3000);
        deleteNotification.setVisible(true);
    }

    public boolean save(File folder){
        folder.mkdirs();
        BufferedImage bImg = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        paintAll(cg);
        try {
            int i = 0;
            File output;
            if(lockedNode!=null) {
                output = new File(folder, ContentPanel.cleanFileName(lockedNode.getImage().getImageSource(), false)+".png");
            }else {
                String baseName = "output";
                while ((output = new File(folder, baseName + "_" + i + ".png")).exists()) {
                    i++;
                }
            }

            ToastMessage toastMessage = new ToastMessage(getToastPosition(), "saved image to \""+output.getPath()+"\"", 4000);
            toastMessage.setVisible(true);
            if (ImageIO.write(bImg, "png", output)) {
                System.out.println("saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public DoubleTuple getToastPosition(){
        return DoubleTuples.add(new DefaultDoubleTupleF(getLocationOnScreen()), divide(new DefaultDoubleTupleF(getSize()), 2));
    }

    private void deleteRec(Node toDelete, List<Node> toDeleteList){
        for(Node child : toDelete.getChildren()){
            deleteRec(child, toDeleteList);
        }
        toDeleteList.add(toDelete);
    }

    public enum Dragging{
        NONE, PANE, NODE;
    }

}
