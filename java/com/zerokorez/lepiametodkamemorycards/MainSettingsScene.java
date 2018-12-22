package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

import java.util.ArrayList;

public class MainSettingsScene implements Scene {
    private ArrayList<String> imagesDirectories;

    private String titleText;
    private Paint titleTextPaint;

    public MainSettingsScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");

        titleText = "Settings";
        titleTextPaint = new Paint();
        titleTextPaint.setColor(Constants.WHITE);
        titleTextPaint.setTextSize(Constants.getTextSize(Constants.TITLE_RECT, titleTextPaint, titleText));
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    @Override
    public void focusOn() {
    }

    @Override
    public void focusOff() {
    }

    @Override
    public void update() { }

    @Override
    public void draw(Canvas canvas) {
        if (Constants.SCENE_MANAGER.amActive(this)) {
            canvas.drawColor(Constants.C5F);

            canvas.drawRect(Constants.TITLE_RECT, Constants.C3F_PAINT);
            canvas.drawLine(Constants.TITLE_RECT.left, Constants.TITLE_RECT.bottom, Constants.TITLE_RECT.right, Constants.TITLE_RECT.bottom, Constants.BORDER_PAINT);
            Constants.drawTextCenter(canvas, Constants.TITLE_RECT, titleTextPaint, titleText);

            Constants.TAB.draw(canvas);
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(Constants.TAB.getExit().recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }
        Constants.TAB.getSettings().recieveTouch(event);
        Constants.TAB.getInfo().recieveTouch(event);
    }
}
