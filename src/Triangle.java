import at.plaz.tuples.DoubleTuple;

import java.awt.*;

/**
* Created by Georg Plaz.
*/
public class Triangle extends Shape {
    private Line first;
    private Line second;
    private Line third;
    private Color color;

    public Triangle(DoubleTuple first, DoubleTuple second, DoubleTuple third, Color color) {
        this.first = new Line(first, second, color);
        this.second = new Line(second, third, color);
        this.third = new Line(third, first, color);
        this.color = color;
    }

    @Override
    public void paint(Graphics graphics, DoubleTuple delta, double scale) {
        first.paint(graphics, delta, scale);
        second.paint(graphics, delta, scale);
        third.paint(graphics, delta, scale);
    }

}
