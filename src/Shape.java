import at.plaz.tuples.DoubleTuple;

import java.awt.*;

/**
* Created by Georg Plaz.
*/
public abstract class Shape {
    public abstract void paint(Graphics graphics, DoubleTuple delta, double scale);

    public static void drawLine(Graphics graphics, DoubleTuple start, DoubleTuple end, Color color){
        graphics.setColor(color);
        graphics.drawLine(
                (int) start.getFirst().doubleValue(),
                (int) start.getSecond().doubleValue(),
                (int) end.getFirst().doubleValue(),
                (int) end.getSecond().doubleValue());
    }

    public static void drawCircle(Graphics graphics, DoubleTuple center, double r, Color color) {
        drawOval(graphics, center, r, r, color);
    }

    public static void drawOval(Graphics graphics, DoubleTuple center, double rx, double ry, Color color) {
        graphics.setColor(color);
        graphics.drawOval(
                (int) (center.getFirst().doubleValue()-rx/2),
                (int) (center.getSecond().doubleValue()-ry/2),
                (int) Math.max(rx, 1),
                (int) Math.max(ry, 1));
    }

    public static void fillCircle(Graphics graphics, DoubleTuple center, double r, Color color) {
        fillOval(graphics, center, r, r, color);
    }

    public static void fillOval(Graphics graphics, DoubleTuple center, double rx, double ry, Color color) {
        graphics.setColor(color);
        graphics.fillOval(
                (int) (center.getFirst().doubleValue()-rx/2),
                (int) (center.getSecond().doubleValue()-ry/2),
                (int) Math.max(rx, 1),
                (int) Math.max(ry, 1));
    }

    public static void drawString(Graphics graphics, String imageSource, DoubleTuple center) {
        graphics.setColor(Color.BLACK);
        graphics.drawString(imageSource, (int) center.getFirst().doubleValue(), (int) center.getSecond().doubleValue());
    }

    public static void drawRect(Graphics graphics, DoubleTuple topLeft, DoubleTuple dimension, Color color) {
        drawRect(graphics, topLeft, dimension, color, 1);
    }

    public static void drawRect(Graphics graphics, DoubleTuple topLeft, DoubleTuple dimension, Color color, int width) {
        graphics.setColor(color);
        for (int i = 0; i < width; i++) {
            graphics.drawRect(
                    (int) topLeft.getFirst().doubleValue()-i, (int) topLeft.getSecond().doubleValue()-i,
                    (int) dimension.getFirst().doubleValue()+(i*2), (int) dimension.getSecond().doubleValue()+(i*2));
        }

    }

    public static void fillRect(Graphics graphics, DoubleTuple topLeft, DoubleTuple dimension, Color color) {
        graphics.setColor(color);
        graphics.fillRect(
                (int) topLeft.getFirst().doubleValue(), (int) topLeft.getSecond().doubleValue(),
                (int) dimension.getFirst().doubleValue(), (int) dimension.getSecond().doubleValue());
    }
}
