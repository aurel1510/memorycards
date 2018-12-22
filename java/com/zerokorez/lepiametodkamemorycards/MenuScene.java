package com.zerokorez.lepiametodkamemorycards;

import android.graphics.*;
import android.view.MotionEvent;

import java.util.ArrayList;

public class MenuScene implements Scene {
    private ArrayList<String> imagesDirectories;

    private ButtonManager buttonManager;
    private Button newSessionButton;
    private Button createGroupButton;
    private Button managerButton;
    private Button settingsButton;
    private Button quitButton;

    private boolean fadeOn;

    private Rect quitRect;

    private String quitText;
    private Paint quitTextPaint;
    private Rect quitTextRect;

    private ButtonManager quitButtonManager;
    private Button quitYesButton;
    private Button quitNoButton;

    private String comingSoonText;
    private Paint comingSoonTextPaint;

    private ButtonManager comingSoonButtonManager;
    private Button comingSoonOkButton;
    private boolean comingSoonFadeOn;

    private boolean onTouch;

    public MenuScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("title_screen.png");
        imagesDirectories.add("chained_menu_scene.png");

        float top = Constants.SCREEN_HEIGHT * 9 / 15f;
        float x = Constants.SCREEN_WIDTH / 5f;
        float y = Constants.SCREEN_HEIGHT / 55f;
        float space = (Constants.SCREEN_HEIGHT/55*19-y*3*5)/4;

        buttonManager = new ButtonManager(Constants.C4F_PAINT);
        newSessionButton = new Button(buttonManager, new Rect((int) x, (int) top, (int) (Constants.SCREEN_WIDTH - x), (int) (top + y * 3)), "New Session");
        createGroupButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 3 + space), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y * 6 + space)), "Create Group");
        createGroupButton.setIsComingSoon(true, "chained_menu_scene.png");
        managerButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 6 + space*2), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y * 9 + space*2)), "Manager");
        managerButton.setIsComingSoon(true, "chained_menu_scene.png");
        settingsButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 9 + space*3), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y * 12 + space*3)), "Settings");
        quitButton = new Button(buttonManager, new Rect((int) x, (int) (top + y * 12 + space*4), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y * 15 + space*4)), "Quit");

        fadeOn = false;

        float left = Constants.SCREEN_WIDTH/5f;
        top = Constants.SCREEN_HEIGHT/3f + y*11/20f;
        x = Constants.SCREEN_WIDTH*3/45f;
        y = Constants.SCREEN_HEIGHT/48f;

        quitRect = new Rect((int) left, (int) top, (int) (left + x*9), (int) (top + y*8));

        quitText = "Are you sure?";
        quitTextRect = new Rect((int)left, (int) (top + y),(int) (left + x*9),(int) (top + y*3));
        quitTextPaint = new Paint();
        quitTextPaint.setColor(Constants.WHITE);
        quitTextPaint.setTextSize(Constants.getTextSize(quitTextRect, quitTextPaint, quitText));

        quitButtonManager = new ButtonManager(Constants.C4F_PAINT);
        quitYesButton = new Button(quitButtonManager, new Rect((int) (left + x), (int) (top + y*45/10f), (int) (left + x*4), (int) (top + y*65/10f)), "Yes");
        quitNoButton = new Button(quitButtonManager, new Rect((int) (left + x*5), (int) (top + y*45/10f), (int) (left + x*8), (int) (top + y*65/10f)), "No");

        comingSoonText = "Coming soon!";
        comingSoonTextPaint = new Paint();
        comingSoonTextPaint.setColor(Constants.WHITE);
        comingSoonTextPaint.setTextSize(Constants.getTextSize(quitTextRect, comingSoonTextPaint, comingSoonText));

        comingSoonButtonManager = new ButtonManager(Constants.C4F_PAINT);
        comingSoonOkButton = new Button(comingSoonButtonManager, new Rect((int) (left + x*3), (int) (top + y*45/10f), (int) (left + x*6), (int) (top + y*65/10f)), "Ok");
        comingSoonFadeOn = false;

        onTouch = false;
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

            Constants.drawImage(canvas, imagesDirectories.get(0), Constants.FULL_SCREEN_RECT);

            buttonManager.draw(canvas);

            if (fadeOn) {
                quitButton(canvas);
            }
            if (comingSoonFadeOn) {
                comingSoonButton(canvas);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(newSessionButton.recieveTouch(event)) {
            Constants.SCENE_MANAGER.add(new NewSessionScene());
        }
        if(createGroupButton.recieveTouch(event) || managerButton.recieveTouch(event)) {
            buttonManager.setState(false);
            comingSoonFadeOn = true;
        } else if (settingsButton.recieveTouch(event)) {
            Constants.SCENE_MANAGER.add(new MainSettingsScene());
        } else if(comingSoonFadeOn) {
            if(comingSoonOkButton.recieveTouch(event)) {
                buttonManager.setState(true);
                comingSoonFadeOn = false;
            }
            if (cancelFade(event)) {
                buttonManager.setState(true);
                comingSoonFadeOn = false;
            }
        }
        if(quitButton.recieveTouch(event)) {
            buttonManager.setState(false);
            fadeOn = true;
        } else if(fadeOn) {
            if(quitYesButton.recieveTouch(event)) {
                Constants.ACTIVITY.finish();
                System.exit(0);
            }
            if(quitNoButton.recieveTouch(event)) {
                buttonManager.setState(true);
                fadeOn = false;
            }
            if (cancelFade(event)) {
                buttonManager.setState(true);
                fadeOn = false;
            }
        }
    }

    public boolean cancelFade(MotionEvent event) {
        float[] point = new float[]{event.getX(), event.getY(),};
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (!quitRect.contains((int) point[0], (int) point[1]) && onTouch) {
                    onTouch = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (!quitRect.contains((int) point[0], (int) point[1])) {
                    onTouch = true;
                }
                break;
        }
        return false;
    }

    public void quitButton(Canvas canvas) {
        canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
        canvas.drawRect(quitRect, Constants.C5F_PAINT);
        canvas.drawRect(quitRect, Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, quitTextRect, quitTextPaint, quitText);
        quitButtonManager.draw(canvas);
    }

    public void comingSoonButton(Canvas canvas) {
        canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
        canvas.drawRect(quitRect, Constants.C5F_PAINT);
        canvas.drawRect(quitRect, Constants.BORDER_PAINT);

        Constants.drawTextCenter(canvas, quitTextRect, comingSoonTextPaint, comingSoonText);
        comingSoonButtonManager.draw(canvas);
    }
}
