package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public interface Scene {
    ArrayList<String> getImagesDirectories();
    void focusOn();
    void focusOff();
    void update();
    void draw(Canvas canvas);
    void receiveTouch(MotionEvent event);
}
