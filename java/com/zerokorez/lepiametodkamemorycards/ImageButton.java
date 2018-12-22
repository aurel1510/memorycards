package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

public class ImageButton implements GameObject {
    private ButtonManager buttonManager;
    private Rect rectangle;
    private String directory;

    private boolean onTouch = false;
    private boolean onHover = false;
    private boolean onClick = false;

    public ImageButton(ButtonManager buttonManager, Rect rectangle, String imageDirectory) {
        this.buttonManager = buttonManager;
        buttonManager.add(this);
        this.rectangle = rectangle;
        directory = imageDirectory;
    }

    public void changeImage(String imageDirectory) {
        this.directory = imageDirectory;
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
        Constants.drawImage(canvas, directory, rectangle);
        if(onHover) {
            canvas.drawRect(rectangle, Constants.SHADE_PAINT);
        }
    }

    @Override
    public void update() {
    }
}
