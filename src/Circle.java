import at.plaz.tuples.DoubleTuple;
import at.plaz.tuples.DoubleTuples;

import java.awt.*;

import static at.plaz.tuples.DoubleTuples.*;

/**
* Created by Georg Plaz.
*/
public class Circle extends Shape {
    private DoubleTuple center;
    private double radius;
    private Color color;

    public Circle(DoubleTuple center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void paint(Graphics graphics, DoubleTuple delta, double scale) {
        DoubleTuple newCenter = add(multiply(this.center, scale), delta);
        fillCircle(graphics, newCenter,radius*scale, color);
    }

    public DoubleTuple getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public Color getColor() {
        return color;
    }
}
