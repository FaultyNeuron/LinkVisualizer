import at.plaz.tuples.DoubleTuple;
import at.plaz.tuples.DoubleTuples;

import java.awt.*;

import static at.plaz.tuples.DoubleTuples.*;

/**
* Created by Georg Plaz.
*/
public class Line extends Shape {
    private DoubleTuple start;
    private DoubleTuple end;
    private Color color;

    public Line(DoubleTuple start, DoubleTuple end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    public Line(DoubleTuple start, DoubleTuple end) {
        this(start, end, Color.BLACK);
    }

    public DoubleTuple getStart() {
        return start;
    }

    public DoubleTuple getEnd() {
        return end;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void paint(Graphics graphics, DoubleTuple delta, double scale) {
        DoubleTuple newStart = add(multiply(start, scale), delta);
        DoubleTuple newEnd = add(multiply(end, scale), delta);
        drawLine(graphics, newStart, newEnd, color);
    }
}
