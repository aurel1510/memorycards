package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

public class ResultButton implements GameObject {
    private ResultButtonList list;
    private Card card;

    private String indexText;
    private Rect indexTextRect;
    private Paint indexTextPaint;

    private String valueText;
    private Rect valueTextRect;
    private Paint valueTextPaint;

    private boolean onTouch;
    private boolean onClick;

    private String imageDirectory;
    private Rect imageRect;

    private float additionalY;

    public ResultButton(ResultButtonList resultButtonList, Card card, boolean result) {
        list = resultButtonList;
        list.add(this);
        this.card = card;

        onTouch = false;
        onClick = false;

        float left = Constants.SCREEN_WIDTH/40f;
        float right = Constants.SCREEN_WIDTH*38/40f;
        float y = Constants.SCREEN_HEIGHT/18f;

        indexText = card.getIndex();
        indexTextRect = new Rect((int) (left + y*3), 0, (int) right, (int) y);
        indexTextPaint = new Paint();
        indexTextPaint.setColor(Constants.WHITE);
        indexTextPaint.setTextSize(Constants.getTextSize(indexTextRect, indexTextPaint, indexText));

        valueText = card.getValue();
        valueTextRect = new Rect((int) (left + y*3), (int) y, (int) right, (int) (y*3));
        valueTextPaint = new Paint();
        if (result) {
            valueTextPaint.setColor(Constants.GREEN);
        } else {
            valueTextPaint.setColor(Constants.RED);
        }
        valueTextPaint.setTextSize(Constants.getTextSize(valueTextRect, valueTextPaint, valueText));

        imageRect = new Rect((int) left, 0, (int) (left + y*3), (int) (y*3));
        if (result) {
            imageDirectory = "tick_result_button.png";
        } else {
            imageDirectory = "cross_result_button.png";
        }
        additionalY = 0;
    }

    public Card getCard() {
        return card;
    }

    public float getTop() {
        return Constants.LIST_ITEM_RECT.top + additionalY;
    }

    public float getBottom() {
        return Constants.LIST_ITEM_RECT.bottom + additionalY;
    }

    public boolean recieveTouch(MotionEvent event) {
        if (list.getState()) {
            float[] point = new float[]{event.getX(), event.getY()};
            int action = event.getAction();
            Rect rect = Constants.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY);

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (rect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (rect.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (!rect.contains((int) point[0], (int) point[1]) || !onTouch) {
                        onTouch = false;
                    }
                default:
                    onClick = false;
                    break;
            }
            if (Math.abs(list.getMovementY()) < Constants.SCREEN_HEIGHT/48f) {
                return onClick;
            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    public void draw(Canvas canvas, float newY) {
        additionalY = newY;
        Rect rect = Constants.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY);
        canvas.drawRect(rect, Constants.C4F_PAINT);
        canvas.drawRect(rect, Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, Constants.moveRect(new Rect(indexTextRect.left, indexTextRect.top, indexTextRect.right, indexTextRect.bottom + Constants.SCREEN_HEIGHT/36), 0, (int) additionalY), indexTextPaint, indexText);
        Constants.drawTextCenter(canvas, Constants.moveRect(valueTextRect, 0, (int) additionalY), valueTextPaint, valueText);

        rect = Constants.moveRect(imageRect, 0, (int) additionalY);
        canvas.drawRect(rect, Constants.C3F_PAINT);

        float x = Constants.SCREEN_HEIGHT/48f;
        Constants.drawImage(canvas, imageDirectory, new Rect((int) (rect.left + x), (int) (rect.top + x), (int) (rect.right - x), (int) (rect.bottom - x)));
        canvas.drawRect(rect, Constants.BORDER_PAINT);
    }

    @Override
    public void update() {
    }

    public Paint getIndexTextPaint() {
        return indexTextPaint;
    }

    public Paint getValueTextPaint() {
        return valueTextPaint;
    }
}
