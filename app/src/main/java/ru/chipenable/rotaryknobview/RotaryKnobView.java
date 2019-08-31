package ru.chipenable.rotaryknobview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RotaryKnobView extends View {

    private Paint knobPaint;
    private PointF touchPoint;
    private RotationListener rotationListener;
    private float rotationAngle;
    private float angleMin = 135;
    private float angleMax = 45;
    private int minValue = 0;
    private int maxValue = 100;

    private final int MIN_WIDTH = 50;
    private final int DEFAULT_KNOB_COLOR = 0xff888888;

    private static final String TAG = RotaryKnobView.class.getName();
    private static final boolean DBG = true;

    public interface RotationListener {

        void onRotationChangeListener(int position);
    }

    public RotaryKnobView(Context context) {
        super(context);
        init(context, null);
    }

    public RotaryKnobView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotaryKnobView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RotaryKnobView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        touchPoint.x = event.getX();
        touchPoint.y = event.getY();

        float deltaX = event.getX() - getPivotX();
        float deltaY = event.getY() - getPivotY();
        float angle = (float) Math.atan(deltaY/deltaX);

        if (deltaX < 0){
            angle += Math.PI;
        }
        else if (deltaY < 0){
            angle += (2*Math.PI);
        }
        angle = (float)Math.toDegrees(angle);
        if (DBG) {
            Log.d(TAG, String.format("angle: %f", angle));
        }

        if (angle != rotationAngle) {
            rotationAngle = angle;

            if (!isAngleValid(angle)){
                rotationAngle = calcBoundaryAngle(angle);
            }

            invalidate();


            if (rotationListener != null){
                float enabledAngle = angle;
                if (angle >= minValue && angle < 360){
                    enabledAngle = (angle - angleMin) % 360;
                }
                else if (angle >= 0 && angle <= maxValue){

                }

                int range = maxValue - minValue;
                int value = (int)(enabledAngle * range)/360;
                rotationListener.onRotationChangeListener(value);
            }
        }
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {



        float centerX = getPivotX();
        float centerY = getPivotY();
        float knobRadius = (getWidth() - getPaddingStart() - getPaddingEnd())/2;

        canvas.drawLine(centerX, centerY, touchPoint.x, touchPoint.y, knobPaint);

        canvas.rotate(rotationAngle, centerX, centerY);
        canvas.drawCircle(centerX, centerY, knobRadius, knobPaint);
        canvas.drawCircle(centerX + (3*knobRadius)/4, centerY, knobRadius/5, knobPaint);
        canvas.drawLine(centerX, centerY, centerX + knobRadius, centerY, knobPaint);
    }

    public void setOnRotationListener(RotationListener listener){
        this.rotationListener = listener;
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        knobPaint = new Paint();
        knobPaint.setStyle(Paint.Style.STROKE);
        knobPaint.setColor(DEFAULT_KNOB_COLOR);
        knobPaint.setAntiAlias(true);
        knobPaint.setStrokeWidth(2f);

        touchPoint = new PointF();
    }

    private boolean isAngleValid(float angle){
        if (angleMin == 0 && angleMax == 0){
            return true;
        }

        if (angleMin < angleMax){
            return angle >= angleMin && angle <= angleMax;
        }
        else{
            return !(angle < angleMin && angle > angleMax);
        }
    }

    private float calcBoundaryAngle(float angle){
        if (angleMin == 0 && angleMax == 0){
            return angle;
        }

        double angleMinRad = Math.toRadians(angleMin);
        double min1 = Math.sin(angleMinRad);
        double min2 = Math.cos(angleMinRad);

        double angleMaxRad = Math.toRadians(angleMax);
        double max1 = Math.sin(angleMaxRad);
        double max2 = Math.cos(angleMaxRad);

        double angleRad = Math.toRadians(angle);
        double p1 = Math.sin(angleRad);
        double p2 = Math.cos(angleRad);

        double distanceToMin = Math.sqrt(Math.pow(p1 - min1, 2) + Math.pow(p2 - min2, 2));
        double distanceToMax = Math.sqrt(Math.pow(p1 - max1, 2) + Math.pow(p2 - max2, 2));

        if (distanceToMin <= distanceToMax){
            return angleMin;
        }
        else{
            return angleMax;
        }
    }



}
