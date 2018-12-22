package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

import java.util.ArrayList;

public class GroupButton implements GameObject {
    private ArrayList<String> imagesDirectories;
    private ArrayList<Rect> imagesRects;

    private GroupButtonList list;
    private Group group;

    private String fullNameText;
    private String nameText;
    private Rect nameTextRect;
    private Paint nameTextPaint;

    private String lastStudyText;
    private Rect lastStudyTextRect;
    private Paint lastStudyTextPaint;

    private String numberText;
    private Rect numberTextRect;
    private Paint numberTextPaint;

    private String valueLastStudyText;
    private Rect valueLastStudyTextRect;
    private Paint valueLastStudyTextPaint;

    private String valueNumberText;
    private Rect valueNumberTextRect;
    private Paint valueNumberTextPaint;

    private boolean onTouch;
    private boolean onClick;

    private float additionalY;

    public GroupButton(GroupButtonList groupButtonList, Group group) {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("default.png");

        float left = Constants.SCREEN_WIDTH/40f;
        float right = Constants.SCREEN_WIDTH*38/40f;
        float x = Constants.SCREEN_WIDTH*37/40f;

        imagesRects = new ArrayList<>();
        imagesRects.add(new Rect((int) (Constants.SCREEN_WIDTH*38/40f - Constants.SCREEN_HEIGHT/6f), 0, Constants.SCREEN_WIDTH*38/40, Constants.SCREEN_HEIGHT/6));

        list = groupButtonList;
        list.add(this);
        this.group = group;

        nameTextRect = new Rect((int) left, 0, (int) (right - Constants.SCREEN_HEIGHT/6f), Constants.SCREEN_HEIGHT/12);
        nameTextPaint = new Paint();
        nameTextPaint.setColor(Constants.WHITE);
        nameTextPaint.setTextSize(Constants.getTextSize(nameTextRect, nameTextPaint, "Coming soon!"));
        fullNameText = group.getName();
        nameText = "";

        Rect bounds = new Rect();
        nameTextPaint.getTextBounds("Coming soon!", 0, 12, bounds);
        boolean finished = false;
        float maxTextWidth = bounds.width();
        int index = -1;
        for (char character : fullNameText.toCharArray()) {
            index++;
            nameTextPaint.getTextBounds(nameText + character + ((index+1 < fullNameText.length()) ? fullNameText.toCharArray()[index+1] : ""), 0, nameText.length()+1, bounds);
            if (bounds.width() <= maxTextWidth && !finished) {
                nameText += character;
            } else {
                if (!finished) {
                    nameText += "..";
                    finished = true;
                }
            }
        }

        lastStudyText = "Last study";
        lastStudyTextRect = new Rect((int) left, Constants.SCREEN_HEIGHT/12, (int) ((x - Constants.SCREEN_HEIGHT/6f)/2f + left), Constants.SCREEN_HEIGHT*3/24);
        lastStudyTextPaint = new Paint();
        lastStudyTextPaint.setColor(Constants.WHITE);
        lastStudyTextPaint.setTextSize(Constants.getTextSize(lastStudyTextRect, lastStudyTextPaint, lastStudyText));

        numberText = "Number";
        numberTextRect = new Rect((int) left, Constants.SCREEN_HEIGHT*3/24, (int) ((x - Constants.SCREEN_HEIGHT/6f)/2f + left), Constants.SCREEN_HEIGHT*4/24);
        numberTextPaint = new Paint();
        numberTextPaint.setColor(Constants.WHITE);
        numberTextPaint.setTextSize(Constants.getTextSize(numberTextRect, numberTextPaint, lastStudyText));

        valueLastStudyText = "DD.MM.YYYY";
        valueLastStudyTextRect = new Rect((int) ((x - Constants.SCREEN_HEIGHT/6f)/2f + left), Constants.SCREEN_HEIGHT/12, (int) (right - Constants.SCREEN_HEIGHT/6f), Constants.SCREEN_HEIGHT*3/24);
        valueLastStudyTextPaint = new Paint();
        valueLastStudyTextPaint.setColor(Constants.WHITE);
        valueLastStudyTextPaint.setTextSize(Constants.getTextSize(valueLastStudyTextRect, valueLastStudyTextPaint, valueLastStudyText));

        valueNumberText = Integer.toString(group.getCards().size());
        valueNumberTextRect = new Rect((int) ((x - Constants.SCREEN_HEIGHT/6f)/2f + left), Constants.SCREEN_HEIGHT*3/24, (int) (right - Constants.SCREEN_HEIGHT/6f), Constants.SCREEN_HEIGHT*4/24);
        valueNumberTextPaint = new Paint();
        valueNumberTextPaint.setColor(Constants.WHITE);
        valueNumberTextPaint.setTextSize(Constants.getTextSize(valueNumberTextRect, valueNumberTextPaint, valueNumberText));

        additionalY = 0;
    }

    public Group getGroup() {
        return group;
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
            Rect movedRect = Constants.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY);

            switch (action) {
                case MotionEvent.ACTION_UP:
                    if (movedRect.contains((int) point[0], (int) point[1]) && onTouch) {
                        onClick = true;
                        onTouch = false;
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (movedRect.contains((int) point[0], (int) point[1])) {
                        onTouch = true;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (!movedRect.contains((int) point[0], (int) point[1]) || !onTouch) {
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
        canvas.drawRect(Constants.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY), Constants.C4F_PAINT);
        canvas.drawRect(Constants.moveRect(Constants.LIST_ITEM_RECT, 0, (int) additionalY), Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, Constants.moveRect(nameTextRect, 0, (int) additionalY), nameTextPaint, nameText);
        canvas.drawRect(Constants.moveRect(nameTextRect, 0, (int) additionalY), Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, Constants.moveRect(lastStudyTextRect, 0, (int) additionalY), lastStudyTextPaint, lastStudyText);

        Rect rect = Constants.moveRect(numberTextRect, 0, (int) additionalY);
        Constants.drawTextCenter(canvas, rect, numberTextPaint, numberText);
        canvas.drawRect(rect, Constants.BORDER_PAINT);

        rect = Constants.moveRect(lastStudyTextRect, 0, (int) additionalY);
        canvas.drawRect(rect, Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, Constants.moveRect(valueLastStudyTextRect, 0, (int) additionalY), valueLastStudyTextPaint, valueLastStudyText);
        Constants.drawTextCenter(canvas, Constants.moveRect(valueNumberTextRect, 0, (int) additionalY), valueNumberTextPaint, valueNumberText);

        rect = Constants.moveRect(imagesRects.get(0), 0, (int) additionalY);
        Constants.drawImage(canvas, imagesDirectories.get(0), rect);
        canvas.drawRect(rect, Constants.BORDER_PAINT);
    }

    @Override
    public void update() {
    }
}
