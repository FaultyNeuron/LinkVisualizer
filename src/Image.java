import at.plaz.tuples.DefaultDoubleTupleF;
import at.plaz.tuples.DoubleTuple;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Georg Plaz.
 */
public class Image {
    public static final int CHAR_COUNT = 2;
//    private DoubleTupel center;
//    private String title;
    private String imageSource;
//    private String link;
    private java.util.List<Shape> shapes = new LinkedList<Shape>();

    public Image(String imageSource) {
        this.imageSource = imageSource;
//        this.link = link;

        byte[] data = new byte[0];
        try {
            data = imageSource.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
        }
//        pack();

        int i=0;
        while(i < data.length-(CHAR_COUNT-1)) {
            byte[] ba = new byte[CHAR_COUNT];
            for (int j = 0; j < CHAR_COUNT; j++) {
                ba[j] = data[i+j];
            }
            draw(ba);
            i+=CHAR_COUNT;
        }
        if(data.length!=i){
            draw(new byte[]{data[data.length-1]});
        }
    }

    public void drawRandomCircle(Random random){
        DoubleTuple center = getPoint(random);
        double xDist = Math.abs(0.5 - Math.abs(0.5 - center.getFirst()));
        double yDist = Math.abs(0.5 - Math.abs(0.5 - center.getSecond()));
        double radius = random.nextDouble()*Math.min(xDist, yDist);
        draw(new Circle(center, radius, getColor(random)));
    }

    public void drawRandomLine(Random random){
        DoubleTuple start = getPoint(random);
        DoubleTuple end;
        while((end=getPoint(random)).distance(start)<0.1){
            System.out.println("redraw!");
        }
        draw(new Line(start, end, getColor(random)));
    }

    public void drawRandomTriangle(Random random){
        DoubleTuple first = getPoint(random);
        DoubleTuple second = getPoint(random);
        DoubleTuple third = getPoint(random);
        Color color = getColor(random);
        draw(new Triangle(first, second, third, color));
    }

    private DoubleTuple getPoint(Random random){
        return new DefaultDoubleTupleF(random);
    }

    private Color getColor(Random random){
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
    }

    public void draw(Shape shape){
        shapes.add(shape);
    }

    public void drawRandomShape(long seed){
        Random random = new Random(seed);

        random.nextInt();
        int type = random.nextInt(3);
        switch (type){
            case 0:
                drawRandomLine(random);
                break;
            case 1:
                drawRandomCircle(random);
                break;
            case 2:
                drawRandomTriangle(random);
                break;
        }
    }

    public void paint(Graphics graphics, DoubleTuple topLeft, double dimension) {
        java.util.List<Shape> toDraw = new LinkedList<>(shapes);

        for(Shape shape : toDraw){
            shape.paint(graphics, topLeft, dimension);
        }
    }

    private void draw(byte[] b){
        drawRandomShape(toLong(b));
    }

    public static long toLong(byte[] b){
        boolean[] booleans = byteArray2BitArray(b);
        long n = 0, l = booleans.length;
        for (int i = 0; i < l; ++i) {
            n = (n << 1) + (booleans[i] ? 1 : 0);
        }
        return n;
    }

    public static boolean[] byteArray2BitArray(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
                bits[i] = true;
        }
        return bits;
    }

    public String getImageSource() {
        return imageSource;
    }
}
