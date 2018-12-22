package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager() {
        add(new MenuScene());
    }

    public void add(Scene scene) {
        if (scenes.size() > 0) {
            scenes.get(ACTIVE_SCENE).focusOff();
        }
        scenes.add(scene);
        ACTIVE_SCENE = scenes.size() - 1;
        scenes.get(ACTIVE_SCENE).focusOn();
        Constants.TAB.getManager().setState(true);
    }

    public void removeActive() {
        if (scenes.size() > 0) {
            scenes.get(ACTIVE_SCENE).focusOff();
        }
        scenes.remove(ACTIVE_SCENE);
        ACTIVE_SCENE = scenes.size() - 1;
        if (scenes.size() > 0) {
            scenes.get(ACTIVE_SCENE).focusOn();
        }
        Constants.TAB.getManager().setState(true);
    }

    public void recieveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    public  boolean amActive(Scene scene) {
        return scenes.get(ACTIVE_SCENE) == scene;
    }

    public  Scene getActive() {
        return scenes.get(ACTIVE_SCENE);
    }

    public  int getActiveIndex() {
        return ACTIVE_SCENE;
    }
}
