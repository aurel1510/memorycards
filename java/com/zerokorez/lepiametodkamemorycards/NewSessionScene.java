package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

import java.util.ArrayList;

public class NewSessionScene implements Scene {
    private ArrayList<String> imagesDirectories;

    private String titleText;
    private Paint titleTextPaint;

    private GroupButtonList groupButtonList;

    private boolean fadeOn;
    private boolean onTouch;

    private Rect menuRect;

    private ButtonManager menuButtonManager;
    private Button menuStudyCardsButton;
    private Button menuCardQuizButton;

    public NewSessionScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("chained_new_session_scene.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesDirectories.add("default.png");

        titleText = "Card Groups";
        titleTextPaint = new Paint();
        titleTextPaint.setColor(Constants.WHITE);
        titleTextPaint.setTextSize(Constants.getTextSize(Constants.TITLE_RECT, titleTextPaint, titleText));

        fadeOn = false;
        onTouch = false;

        float x = Constants.SCREEN_WIDTH*3/5f/10f;
        float y = Constants.SCREEN_HEIGHT/7f/9f;
        float left = Constants.SCREEN_WIDTH*2/10f;
        float top = Constants.SCREEN_HEIGHT*5/14f + y;

        menuRect = new Rect((int) left, (int) top, (int) (left + x*10), (int) (top + y*18 - y*2));

        menuButtonManager = new ButtonManager(Constants.C4F_PAINT);
        menuStudyCardsButton = new Button(menuButtonManager, new Rect((int) (left + x), (int) (top + y*3/2f), (int) (left + x*9), (int) (top + y*14/2f)), "Study Cards");
        menuCardQuizButton = new Button(menuButtonManager, new Rect((int) (left + x), (int) (top + y*17/2f), (int) (left + x*9), (int) (top + y*29/2f)), "Card Quiz");
        menuButtonManager.setSameFont();
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    @Override
    public void focusOn() {
        Constants.LOAD_MANAGER.loadGroups();

        groupButtonList = new GroupButtonList();
        for(Group group: Constants.LOAD_MANAGER.getLoadedGroups()) {
            new GroupButton(groupButtonList, group);
        }
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
            groupButtonList.draw(canvas);

            canvas.drawRect(Constants.TITLE_RECT, Constants.C3F_PAINT);
            canvas.drawLine(Constants.TITLE_RECT.left, Constants.TITLE_RECT.bottom, Constants.TITLE_RECT.right, Constants.TITLE_RECT.bottom, Constants.BORDER_PAINT);
            Constants.drawTextCenter(canvas, Constants.TITLE_RECT, titleTextPaint, titleText);

            Constants.TAB.draw(canvas);

            if (fadeOn) {
                canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
                canvas.drawRect(menuRect, Constants.C5F_PAINT);
                canvas.drawRect(menuRect, Constants.BORDER_PAINT);

                menuButtonManager.draw(canvas);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(Constants.TAB.getExit().recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }
        Constants.TAB.getSettings().recieveTouch(event);
        Constants.TAB.getInfo().recieveTouch(event);

        if(groupButtonList.recieveTouch(event)) {
            groupButtonList.setState(false);
            menuButtonManager.setState(true);
            fadeOn = true;
            if (Constants.LOAD_MANAGER.getActiveGroup().getCards().size() < 4) {
                menuCardQuizButton.setIsComingSoon(true, "chained_new_session_scene.png");
                if (Constants.LOAD_MANAGER.getActiveGroup().getCards().size() == 0) {
                    menuStudyCardsButton.setIsComingSoon(true, "chained_new_session_scene.png");
                } else {
                    menuStudyCardsButton.setIsComingSoon(false, "");
                }
            } else {
                menuCardQuizButton.setIsComingSoon(false, "");
                menuStudyCardsButton.setIsComingSoon(false, "");
            }
        } else if (fadeOn) {
            if (menuStudyCardsButton.recieveTouch(event)) {
                if (!menuStudyCardsButton.getIsComingSoon()) {
                    groupButtonList.setState(true);
                    menuButtonManager.setState(false);
                    fadeOn = false;
                    Constants.SCENE_MANAGER.add(new StudyCardsScene());
                }
            }
            if (menuCardQuizButton.recieveTouch(event)) {
                if (!menuCardQuizButton.getIsComingSoon()) {
                    groupButtonList.setState(true);
                    menuButtonManager.setState(false);
                    fadeOn = false;
                    Constants.SCENE_MANAGER.add(new CardQuizScene());
                }
            }
            if (cancelFade(event)) {
                Constants.LOAD_MANAGER.setActiveGroup(-1);
                groupButtonList.setState(true);
                menuButtonManager.setState(false);
                fadeOn = false;
            }
        }
    }

    public boolean cancelFade(MotionEvent event) {
        float[] point = new float[]{event.getX(), event.getY(),};
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (!menuRect.contains((int) point[0], (int) point[1]) && onTouch) {
                    onTouch = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (!menuRect.contains((int) point[0], (int) point[1])) {
                    onTouch = true;
                }
                break;
        }
        return false;
    }
}
