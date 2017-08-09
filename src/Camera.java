import at.plaz.tuples.DoubleTuple;

/**
 * Created by Georg Plaz.
 */
public abstract class Camera implements HasCenter{
    private double scalar;
    private double targetScalar;
    private HasCenter targetCenter;
    private DoubleTuple center;

    public abstract void update();
    public abstract void zoomAndPan(HasCenter panCenter, double targetZoom);
    public void zoomAndPan(DoubleTuple panCenter, double targetZoom){
        zoomAndPan(new Point(panCenter), targetZoom);
    }
    public abstract void zoom(double targetZoom);
    public abstract void pan(HasCenter panCenter);
    public void pan(DoubleTuple panCenter){
        pan(new Point(panCenter));
    }


    public double getZoom() {
        return scalar;
    }

    public void setZoom(double scalar) {
        this.scalar = scalar;
    }

    public HasCenter getTarget() {
        return targetCenter;
    }

    public void setTargetPanCenter(HasCenter target) {
        this.targetCenter = target;
    }

    public DoubleTuple getCenter() {
        return center;
    }

    public void setCenter(DoubleTuple center) {
        this.center = center;
    }

    public double getTargetZoom() {
        return targetScalar;
    }

    public void setTargetZoom(double targetScalar) {
        this.targetScalar = targetScalar;
    }

    public abstract void abortZoom();

    public abstract void abortPan();

    public void abortZoomAndPan() {
        abortZoom();
        abortPan();
    }

    public void setTargetPanCenter(DoubleTuple center) {
        setTargetPanCenter(new Point(center));
    }

    public class Point implements HasCenter{
        private DoubleTuple point;

        public Point(DoubleTuple point) {
            this.point = point;
        }

        @Override
        public DoubleTuple getCenter() {
            return point;
        }
    }
}
