package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Button implements GameObject {
    private ButtonManager buttonManager;
    private String chainedDirectory;
    private Rect rectangle;

    private String text;
    private Paint paint;

    private boolean onTouch;
    private boolean onHover;
    private boolean onClick;

    private boolean isComingSoon;
    private boolean isCorrect;
    private boolean isWrong;

    public Button(ButtonManager buttonManager, Rect rectangle, String text) {
        this.buttonManager = buttonManager;
        buttonManager.add(this);

        this.rectangle = rectangle;
        this.text = text;

        onTouch = false;
        onHover = false;
        onClick = false;

        isComingSoon = false;
        isCorrect = false;
        isWrong = false;

        paint = new Paint();
        paint.setColor(buttonManager.getTextColor());
        paint.setTextSize(Constants.getTextSize(this.rectangle, paint, this.text));
    }

    public Paint getPaint() {
        return paint;
    }

    public String getText() {
        return text;
    }

    public boolean getIsComingSoon() {
        return isComingSoon;
    }

    public void setIsComingSoon(boolean isComingSoon, String chainedDirectory) {
        this.isComingSoon = isComingSoon;
        this.chainedDirectory = chainedDirectory;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setIsWrong(boolean isWrong) {
        this.isWrong = isWrong;
    }

    public void resetText(String text) {
        this.text = text;
        paint.setTextSize(Constants.getTextSize(rectangle, paint, this.text));
    }

    public boolean recieveTouch(MotionEvent event) {
        if (buttonManager.getState()) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onHover = false;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rectangle.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                        onHover = true;
                    } else {
                        onHover = false;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (rectangle.contains((int) point[0], (int) point[1]) && onTouch) {
                        onHover = true;
                    } else {
                        onHover = false;
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            return onClick;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rectangle.left, rectangle.top, rectangle.right, rectangle.bottom, buttonManager.getBasePaint());
        if(onHover) {
            canvas.drawRect(rectangle, Constants.SHADE_PAINT);
        }
        canvas.drawRect(rectangle, Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, rectangle, paint, text);

        if (isComingSoon) {
            Constants.drawImage(canvas, chainedDirectory, rectangle);
        }
        if (isCorrect) {
            canvas.drawRect(rectangle, Constants.CORRECT_PAINT);
        } else if (isWrong) {
            canvas.drawRect(rectangle, Constants.WRONG_PAINT);
        }
    }

    @Override
    public void update() {
    }
}
