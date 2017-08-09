import at.plaz.tuples.DoubleTuple;
import at.plaz.tuples.DoubleTuples;

import static at.plaz.tuples.DoubleTuples.*;

/**
 * Created by Georg Plaz.
 */
public class StepCamera extends Camera {
//    private int maxSteps;
    private int zoomStepsLeft;
    private int panStepsLeft;
    private int defaultSteps = 100;

    public StepCamera(DoubleTuple center, double scalar, int defaultSteps) {
        this.defaultSteps = defaultSteps;
        setCenter(center);
        setZoom(scalar);
    }

    public void zoomAndPan(HasCenter panCenter, double targetZoom){
        zoomAndPan(panCenter, targetZoom, defaultSteps);
    }

    public void zoomAndPan(HasCenter panCenter, double targetZoom, int steps){
        pan(panCenter, steps);
        zoom(targetZoom, steps);
    }

    @Override
    public void zoom(double targetZoom) {
        zoom(targetZoom, defaultSteps);
    }

    public void zoom(double targetZoom, int steps) {
        setTargetZoom(targetZoom);
        this.zoomStepsLeft = steps;
    }

    @Override
    public void pan(HasCenter panCenter) {
        pan(panCenter, defaultSteps);
    }

    @Override
    public void abortZoom() {
        setTargetZoom(getZoom());
        zoomStepsLeft = 0;
    }

    @Override
    public void abortPan() {
        setTargetPanCenter(getCenter());
        panStepsLeft = 0;
    }



    public void pan(HasCenter panCenter, int steps) {
        setTargetPanCenter(panCenter);
        this.panStepsLeft = steps;
    }

    public void pan(DoubleTuple panCenter, int steps) {
        pan(new Point(panCenter), steps);
    }

    public void update(){
        if(panStepsLeft > 0) {
            setCenter(add(getCenter(), divide(subtract(getTarget().getCenter(), getCenter()), panStepsLeft)));
            panStepsLeft--;
        }else{
            if(hasTarget()) {
                setCenter(getTarget().getCenter());
            }
        }
        if(zoomStepsLeft > 0) {
            setZoom(getZoom() + (getTargetZoom() - getZoom()) / (zoomStepsLeft));
            zoomStepsLeft--;
        }else{
            setZoom(getTargetZoom());
        }
    }

    private boolean hasTarget() {
        return getTarget()!=null;
    }


}
