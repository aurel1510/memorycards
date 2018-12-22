package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class StudyCardsScene implements Scene {
    private ArrayList<String> imagesDirectories;

    private CardButton cardButton;

    private ButtonManager buttonManager;
    private ImageButton backButton;
    private ImageButton starButton;
    private ImageButton nextButton;

    private Rect bottomRect;

    public StudyCardsScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("left_arrow.png");
        imagesDirectories.add("white_star.png");
        imagesDirectories.add("yellow_star.png");
        imagesDirectories.add("right_arrow.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");

        float x = Constants.SCREEN_WIDTH/33f;
        float top = Constants.SCREEN_HEIGHT - x*11;
        float right = Constants.SCREEN_WIDTH - x*2;

        buttonManager = new ButtonManager(Constants.C5F_PAINT);
        backButton = new ImageButton(buttonManager, new Rect((int) (x*2), (int) top, (int) (right - x*20), (int) (top + x*9)), imagesDirectories.get(0));
        starButton = new ImageButton(buttonManager, new Rect((int) (x*12), (int) top, (int) (right - x*10), (int) (top + x*9)), imagesDirectories.get(1));
        nextButton = new ImageButton(buttonManager, new Rect((int) (x*22), (int) top, (int) right, (int) (top + x*9)), imagesDirectories.get(3));

        cardButton = new CardButton(Constants.LOAD_MANAGER.getActiveGroup(), starButton);

        bottomRect = new Rect((int) (x*2), (int) top, (int) right, (int) (top + x*9));
    }

    @Override
    public ArrayList<String> getImagesDirectories() {
        return imagesDirectories;
    }

    public void setNumber(int number) {
        cardButton.setNumber(number);
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

            cardButton.draw(canvas);

            Constants.TAB.draw(canvas);
            canvas.drawRect(bottomRect, Constants.C4F_PAINT);
            canvas.drawRect(bottomRect, Constants.BORDER_PAINT);
            buttonManager.draw(canvas);
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        Constants.TAB.getInfo().recieveTouch(event);
        Constants.TAB.getSettings().recieveTouch(event);
        if (Constants.TAB.getExit().recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }

        cardButton.recieveTouch(event);

        if (backButton.recieveTouch(event)) {
            cardButton.nextNumber(false);
        }
        if (starButton.recieveTouch(event)) {
            cardButton.getCard().toggleKnown();
            updateStarButton();
        }
        if (nextButton.recieveTouch(event)) {
            cardButton.nextNumber(true);
        }
    }

    public void updateStarButton() {
        if (cardButton.getCard().getKnown()) {
            starButton.changeImage(imagesDirectories.get(2));
        } else {
            starButton.changeImage(imagesDirectories.get(1));
        }
    }
}
