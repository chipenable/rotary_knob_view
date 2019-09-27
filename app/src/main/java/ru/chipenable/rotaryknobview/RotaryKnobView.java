package ru.chipenable.rotaryknobview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RotaryKnobView extends View {

    private Paint knobPaint;
    private PointF touchPoint;
    private RotationListener rotationListener;
    private float rotationAngle;
    private float angleMin = 135;
    private float angleMax = 350;
    private int minValue = 0;
    private int maxValue = 100;
    private int progress = 0;
    private Drawable rotaryKnobDrawable;
    private Scale scale;

    private final int MIN_WIDTH = 50;
    private final int DEFAULT_KNOB_COLOR = 0xff888888;

    private static final String TAG = RotaryKnobView.class.getName();
    private static final boolean DBG = false;

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

    public void setDrawable(@DrawableRes int resId){
        rotaryKnobDrawable = getResources().getDrawable(resId);
    }

    public void setAnglesLimit(float angleMin, float angleMax){
        this.angleMin = angleMin;
        this.angleMax = angleMax;
    }

    public void setMarkAmount(int bigMarkAmount, int smallMarkAmount){
        scale.bigMarks = bigMarkAmount;
        scale.smallMarks = smallMarkAmount;
    }

    public void setValueLimit(int minValue, int maxValue){
        if (minValue > maxValue){
            throw new IllegalArgumentException(
                    String.format("minValue (%d) must be less than maxValue (%d)", minValue, maxValue));
        }

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getProgress(){
        return progress;
    }

    public void setProgress(int value){
        if (value < minValue){
            progress = minValue;
        }
        else if (value > maxValue){
            progress = maxValue;
        }
        else{
            progress = value;
        }

        rotationAngle = progressToAngle(progress);
        invalidate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.angle = rotationAngle;
        savedState.progress = progress;
        savedState.minValue = minValue;
        savedState.maxValue = maxValue;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        rotationAngle = savedState.angle;
        progress = savedState.progress;
        minValue = savedState.minValue;
        maxValue = savedState.maxValue;

        notifyListener();
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
            Log.d(TAG, String.format("raw angle: %f", angle));
        }

        if (!isFloatEqual(angle, rotationAngle)) {
            rotationAngle = angle;

            if (!isAngleValid(angle)){
                rotationAngle = calcBoundaryAngle(angle);
            }

            invalidate();
            progress = angleToProgress(rotationAngle);
            notifyListener();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float centerX = getPivotX();
        float centerY = getPivotY();
        float width = getWidth() - getPaddingStart() - getPaddingEnd();
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        float minSize = width < height? width : height;
        float knobRadius = minSize/2;

        if (DBG) {
            canvas.drawLine(centerX, centerY, touchPoint.x, touchPoint.y, knobPaint);
        }

        scale.setBounds(angleMin, angleMax);
        scale.draw(canvas, centerX, centerY, knobRadius, rotationAngle);

        canvas.rotate(rotationAngle, centerX, centerY);
        if (rotaryKnobDrawable != null) {
            rotaryKnobDrawable.setBounds((int)(centerX - knobRadius), (int)(centerY - knobRadius),
                    (int)(centerX + knobRadius), (int)(centerY + knobRadius));
            rotaryKnobDrawable.draw(canvas);
        }
        else{
            canvas.drawCircle(centerX, centerY, knobRadius, knobPaint);
            canvas.drawCircle(centerX + (3*knobRadius)/4, centerY, knobRadius/5, knobPaint);
        }

        if (DBG){
            canvas.drawLine(centerX, centerY, centerX + knobRadius, centerY, knobPaint);
        }

    }

    public void setOnRotationListener(RotationListener listener){
        this.rotationListener = listener;
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        knobPaint = new Paint();
        knobPaint.setStyle(Paint.Style.STROKE);
        knobPaint.setColor(DEFAULT_KNOB_COLOR);
        knobPaint.setAntiAlias(true);
        knobPaint.setStrokeWidth(3f);

        touchPoint = new PointF();
        rotationAngle = angleMin;

        scale = new Scale();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RotaryKnobView,
                0, 0);

        try {
            rotaryKnobDrawable = a.getDrawable(R.styleable.RotaryKnobView_rotaryKnobDrawable);
            angleMin = a.getInteger(R.styleable.RotaryKnobView_angleMin, 135);
            angleMax = a.getInteger(R.styleable.RotaryKnobView_angleMax, 45);
            minValue = a.getInteger(R.styleable.RotaryKnobView_minValue, 0);
            maxValue = a.getInteger(R.styleable.RotaryKnobView_maxValue, 100);

            int progress = a.getInteger(R.styleable.RotaryKnobView_progress, 0);
            setProgress(progress);

        } finally {
            a.recycle();
        }

    }

    private boolean isAngleValid(float angle){
        if (isFloatZero(angleMin) && isFloatZero(angleMax)){
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
        if (isFloatZero(angleMin) && isFloatZero(angleMax)){
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

        if (distanceToMin < distanceToMax){
            return angleMin;
        }
        else{
            return angleMax;
        }
    }

    private int angleToProgress(float angle){
        if (isFloatEqual(angleMin, angle)){
            return minValue;
        }
        else if (isFloatEqual(angleMax, angle)){
            return maxValue;
        }

        float angleRange;
        if (angleMax > angleMin){
            angle -= angleMin;
            angleRange = angleMax - angleMin;
        }
        else{
            if (angle >= angleMin && angle < 360){
                angle -= angleMin;
            }
            else if (angle >= 0 && angle <= angleMax){
                angle = 360 - angleMin + angle;
            }

            angleRange = 360 - angleMin + angleMax;
        }

        if (DBG){
            Log.d(TAG, String.format("calc progress. angle: %f", angle));
        }

        int valueRange = maxValue - minValue;
        return (int)((angle * valueRange)/angleRange) + minValue;
    }

    private float progressToAngle(int progress){
        float angleRange;
        if (isFloatZero(angleMin) && isFloatZero(angleMax)){
            angleRange = 360;
        }
        else if (angleMin < angleMax){
            angleRange = angleMax - angleMin;
        }
        else{
            angleRange = 360 - angleMin + angleMax;
        }

        int valueRange = maxValue - minValue;
        return ((progress - minValue) * angleRange)/valueRange + angleMin;
    }

    private void notifyListener(){
        if (rotationListener != null){
            rotationListener.onRotationChangeListener(progress);
        }
    }

    private boolean isFloatEqual(float f1, float f2){
        return Math.abs(f1 - f2) < 0.00001f;
    }

    private boolean isFloatZero(float f1){
        return isFloatEqual(f1, 0);
    }

    private class Scale {

        private Paint scalePaint;
        int smallMarks;
        int bigMarks;
        float angleRange;
        float angleMin;
        float angleMax;
        int smallMarkLength;
        int bigMarkLength;

        public Scale(){
            scalePaint = new Paint();
            scalePaint.setAntiAlias(true);
            scalePaint.setStrokeWidth(3f);

            smallMarks = 10;
            bigMarks = 10;
            angleMin = 0;
            angleMax = 0;
            angleRange = range();
            smallMarkLength = 20;
            bigMarkLength = 40;
        }

        public void setBounds(float angleMin, float angleMax){
            this.angleMin = angleMin;
            this.angleMax = angleMax;
            this.angleRange = range();
        }

        public void draw(Canvas canvas, float centerX, float centerY, float radius, float rotationAngle){
            int amount = smallMarks * bigMarks;
            float delta = angleRange/amount;

            for(int i = 0; i <= amount; i++){
                float markAngle = angleMin + (delta * i);
                double angleRad = Math.toRadians(markAngle);
                int markLength = i % smallMarks == 0? bigMarkLength : smallMarkLength;
                float y = (float)Math.sin(angleRad)*(radius + markLength);
                float x = (float)Math.cos(angleRad)*(radius + markLength);

                boolean beforeRotationAngle;
                if (angleMin < angleMax){
                    beforeRotationAngle = markAngle <= rotationAngle;
                }
                else {
                    if (rotationAngle >= angleMin && rotationAngle < 360){
                        beforeRotationAngle = markAngle < rotationAngle;
                    }
                    else{
                        beforeRotationAngle = markAngle <= 360 + rotationAngle;
                    }
                }

                int color = beforeRotationAngle? 0xff000000 : 0xffcccccc;
                scalePaint.setColor(color);
                canvas.drawLine(x + centerX, y + centerY, centerX, centerY, scalePaint);
            }

            int color = scalePaint.getColor();
            scalePaint.setColor(0xffffffff);
            scalePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centerX, centerY, radius + 5, scalePaint);
            scalePaint.setColor(color);
            scalePaint.setStyle(Paint.Style.STROKE);
        }

        private float range(){
            float result;
            if (angleMin < angleMax){
                result = angleMax - angleMin;
            }
            else{
                result = (360 - angleMin) + angleMax;
            }
            return result;
        }

    }

    private static class SavedState extends BaseSavedState{

        float angle;
        int progress;
        int minValue;
        int maxValue;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            angle = in.readFloat();
            progress = in.readInt();
            minValue = in.readInt();
            maxValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(maxValue);
            out.writeInt(minValue);
            out.writeInt(progress);
            out.writeFloat(angle);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

    }

}
