package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

public class TextWindow implements GameObject {
    private Rect rect;
    private Paint borderPaint;

    private float moveX;
    private float moveY;

    private Rect textRect;
    private ArrayList<Paragraph> paragraphs;

    private static Rect bounds;
    private float x;

    private Rect scrollerRect;

    private float columnHeight;
    private float rectHeight;

    private float scrollerSizeRatio;
    private float scrollerMoveRatio;

    public TextWindow(Rect rect, float x, float top) {
        this.rect = rect;
        moveX = 0;
        moveY = 0;

        borderPaint = new Paint();
        borderPaint.setColor(Constants.C2F);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(2);

        textRect = new Rect((int) (rect.left + x), 0, (int) (rect.right - x), (rect.bottom-rect.top)/16);

        bounds = new Rect();
        this.x = x;

        scrollerRect = new Rect((int) (Constants.SCREEN_WIDTH - x*4), (int) (top + x*8), (int) (Constants.SCREEN_WIDTH - x*7/2f), (int) (Constants.SCREEN_HEIGHT - x*18));
    }

    public void setMoveX(float moveX) {
        this.moveX = moveX;
    }

    public void setText(String text) {
        paragraphs = new ArrayList<>();

        String[] lineList = text.split(">");
        for (String line : lineList) {
            paragraphs.add(new Paragraph(line, x, textRect));
        }

        columnHeight = textRect.height() * getLinesNumber() + x*2f/3f;
        rectHeight = rect.height();

        scrollerSizeRatio = rectHeight/columnHeight;
        if(scrollerSizeRatio <= 0) {
            scrollerSizeRatio = 1;
        } else if (scrollerSizeRatio > 1) {
            scrollerSizeRatio = 1;
        }
        scrollerMoveRatio = 0;
    }

    public Rect getRect() {
        return rect;
    }

    public float getColumnHeight() {
        return columnHeight;
    }

    public float getRectHeight() {
        return rectHeight;
    }

    public void setScrollerMoveRatio(float moveY) {
        this.moveY = moveY;
        scrollerMoveRatio = -(moveY/columnHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        int index = 0;
        for (Paragraph paragraph : paragraphs) {
            for (String line : paragraph.getLines()) {
                if (paragraph.getCenter() == 0) {
                    Constants.drawTextLeft(canvas, new Rect((int) (textRect.left + moveX), (int) (rect.top + textRect.height() * index + x + moveY), (int) (textRect.right + moveX), (int) (rect.top + textRect.bottom + textRect.height() * index - x / 2f + moveY)), paragraph.getPaint(), line, 0);
                } else if (paragraph.getCenter() == 1) {
                    Constants.drawTextCenter(canvas, new Rect((int) (textRect.left + moveX), (int) (rect.top + textRect.height() * index + x + moveY), (int) (textRect.right + moveX), (int) (rect.top + textRect.bottom + textRect.height() * index - x / 2f + moveY)), paragraph.getPaint(), line);
                } else if (paragraph.getCenter() == 2) {
                    Constants.drawTextRight(canvas, new Rect((int) (textRect.left + moveX), (int) (rect.top + textRect.height() * index + x + moveY), (int) (textRect.right + moveX), (int) (rect.top + textRect.bottom + textRect.height() * index - x / 2f + moveY)), paragraph.getPaint(), line, 0);
                }
                index++;
            }
        }
    }
    public void drawBorders(Canvas canvas) {
        canvas.drawRect(Constants.moveRect(rect, (int) moveX, 0), borderPaint);
    }
    public void drawScroller(Canvas canvas) {
        canvas.drawRect(Constants.moveRect(new Rect(scrollerRect.left, (int) (scrollerRect.top + scrollerMoveRatio*scrollerRect.height()), scrollerRect.right, (int) ((scrollerRect.bottom-scrollerRect.top)*scrollerSizeRatio + scrollerRect.top + scrollerMoveRatio*scrollerRect.height())), (int) moveX, 0), Constants.C4F_PAINT);
    }

    @Override
    public void update() {
    }

    public int getLinesNumber() {
        int number = 0;
        for (Paragraph paragraph : paragraphs) {
            number += paragraph.getLines().size();
        }
        return number;
    }
}
