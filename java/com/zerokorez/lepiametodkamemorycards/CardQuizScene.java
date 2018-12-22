package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

public class CardQuizScene implements Scene {
    private ArrayList<String> imagesDirectories;
    private ArrayList<Rect> imagesRects;

    private Group group;
    private int number;

    private Rect labelRect;

    private String labelText;
    private Rect labelTextRect;
    private Paint labelTextPaint;

    private ButtonManager buttonManager;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private ButtonManager pauseManager;
    private Button pauseButton;

    private String indicatorText;
    private Paint indicatorTextPaint;
    private Rect indicatorTextRect;

    private long startTime;
    private long lastTime;

    private String startTimerText;
    private Paint startTimerTextPaint;
    private Rect startTimerTextRect;

    private String lastTimerText;
    private Paint lastTimerTextPaint;
    private Rect lastTimerTextRect;

    private String correctText;
    private Rect correctTextRect;

    private String wrongText;
    private Rect wrongTextRect;

    private Paint correctTextPaint;
    private Paint wrongTextPaint;

    private ArrayList<Card> orderedCards;
    private ArrayList<Card> randomCards;
    private ArrayList<Button> randomButtons;

    private ArrayList<Boolean> results;
    private Button currentButton;

    private boolean onTouch;
    private boolean onLabel;
    private boolean onClick;
    private boolean fadeOn;

    private Rect bottomRect;

    private boolean isPaused;
    private long pausedTimeTotal;
    private long pausedTime;

    private Rect pauseRect;

    private String pauseText;
    private Paint pauseTextPaint;
    private Rect pauseTextRect;

    public CardQuizScene() {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("tick_title_rect.png");
        imagesDirectories.add("cross_title_rect.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesRects = new ArrayList<>();

        group = Constants.LOAD_MANAGER.getActiveGroup();
        results = new ArrayList<>();
        number = 0;

        float x = Constants.SCREEN_WIDTH/19f;
        float y = Constants.SCREEN_HEIGHT/45f;
        float y18 = Constants.SCREEN_HEIGHT/18f;

        labelTextRect = new Rect((int) x, (int) (y18*5), (int) (Constants.SCREEN_WIDTH - x), (int) (y18*7));
        labelRect = new Rect((int) x, (int) (y18*9/2), (int) (Constants.SCREEN_WIDTH - x), (int) (y18*7));

        float top = Constants.SCREEN_HEIGHT - y*31/2f;

        Rect button1Rect = new Rect((int) x, (int) (top + y), (int) (x*9), (int) (top + y*5));
        Rect button2Rect = new Rect((int) x, (int) (top + y*6), (int) (x*9), (int) (top + y*10));
        Rect button3Rect = new Rect((int) (x*10), (int) (top + y), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y*5));
        Rect button4Rect = new Rect((int) (x*10), (int) (top + y*6), (int) (Constants.SCREEN_WIDTH - x), (int) (top + y*10));
        Rect pauseButtonRect = new Rect((int) (x*6), (int) (Constants.SCREEN_HEIGHT - y*3), (int) (x*13), (int) (Constants.SCREEN_HEIGHT - y));

        bottomRect = new Rect(0, (int) (Constants.SCREEN_HEIGHT - y*4), Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        buttonManager = new ButtonManager(Constants.C3F_PAINT);
        button1 = new Button(buttonManager, button1Rect, "Index1");
        button2 = new Button(buttonManager, button2Rect, "Index2");
        button3 = new Button(buttonManager, button3Rect, "Index3");
        button4 = new Button(buttonManager, button4Rect, "Index4");
        pauseManager = new ButtonManager(Constants.C2F_PAINT);
        pauseButton = new Button(pauseManager, pauseButtonRect, "Pause");

        randomButtons = new ArrayList<>();
        randomButtons.add(button1);
        randomButtons.add(button2);
        randomButtons.add(button3);
        randomButtons.add(button4);
        currentButton = null;

        onTouch = false;
        onLabel = false;
        onClick = false;

        isPaused = false;
        pausedTime = 0;
        pausedTimeTotal = 0;
        pauseText = "Paused";

        indicatorTextRect = new Rect(0, (int) (y18*3), (int) (Constants.SCREEN_WIDTH/3), (int) (y18*9/2));
        indicatorTextPaint = new Paint();
        indicatorTextPaint.setColor(Constants.WHITE);

        startTimerTextRect = new Rect(Constants.SCREEN_WIDTH*2/3, (int) y18, Constants.SCREEN_WIDTH, (int) (y18*3));
        startTimerTextPaint = new Paint();
        startTimerTextPaint.setColor(Constants.WHITE);

        lastTimerTextRect = new Rect(Constants.SCREEN_WIDTH*2/3, (int) (y18*3), Constants.SCREEN_WIDTH, (int) (y18*9/2));
        lastTimerTextPaint = new Paint();
        lastTimerTextPaint.setColor(Constants.WHITE);

        correctTextRect = new Rect(0, (int) y18, Constants.SCREEN_WIDTH/6, (int) y18*3);
        imagesRects.add(new Rect(Constants.SCREEN_WIDTH/6, (int) (y18 + (y18*2 - Constants.SCREEN_WIDTH/9f)/2f), Constants.SCREEN_WIDTH/6 + Constants.SCREEN_WIDTH/9, (int) (y18*2 + (y18*2 - Constants.SCREEN_WIDTH/9f)/2f)));
        correctTextPaint = new Paint();
        correctTextPaint.setColor(Constants.WHITE);

        wrongTextRect = new Rect(Constants.SCREEN_WIDTH/6 + Constants.SCREEN_WIDTH/9, (int) y18, Constants.SCREEN_WIDTH/3 + Constants.SCREEN_WIDTH/9, (int) (y18*3));
        imagesRects.add(new Rect(Constants.SCREEN_WIDTH/3 + Constants.SCREEN_WIDTH/9, (int) (y18 + (y18*2 - Constants.SCREEN_WIDTH/9)/2), Constants.SCREEN_WIDTH/3 + Constants.SCREEN_WIDTH*2/9, (int) (y18*2 + (y18*2 - Constants.SCREEN_WIDTH/9f)/2f)));
        wrongTextPaint = new Paint();
        wrongTextPaint.setColor(Constants.WHITE);

        labelTextPaint = new Paint();
        labelTextPaint.setColor(Constants.WHITE);

        orderedCards = Constants.shuffleArrayList(group.getCards());

        startTime = System.currentTimeMillis();
        resetCard();
        resetScore();
        lastTime = startTime;

        float bottom = Constants.SCREEN_HEIGHT - y*29/2f;
        top = y18*7f;

        pauseRect = new Rect(Constants.SCREEN_WIDTH/5, (int) ((bottom-top)/2f + top - Constants.SCREEN_HEIGHT/6f/3f), Constants.SCREEN_WIDTH*4/5, (int) ((bottom-top)/2f + top + Constants.SCREEN_HEIGHT/6f/3f));

        pauseTextRect = new Rect(pauseRect.left, pauseRect.top + Constants.SCREEN_HEIGHT/36, pauseRect.right, pauseRect.bottom - Constants.SCREEN_HEIGHT/36);
        pauseTextPaint = new Paint();
        pauseTextPaint.setColor(Constants.WHITE);
        pauseTextPaint.setTextSize(Constants.getTextSize(pauseTextRect, pauseTextPaint, pauseText));
    }

    public void resetScore() {
        float y18 = Constants.SCREEN_HEIGHT/18f;
        correctText = Integer.toString(Collections.frequency(results, true));
        wrongText = Integer.toString(Collections.frequency(results, false));
        correctTextPaint.setTextSize(Constants.getTextSize(new Rect(correctTextRect.left, (int) (correctTextRect.top+y18/3f), correctTextRect.right, (int) (correctTextRect.bottom-y18/3f)), correctTextPaint, correctText));
        wrongTextPaint.setTextSize(Constants.getTextSize(new Rect(wrongTextRect.left, (int) (wrongTextRect.top+y18/3f), wrongTextRect.right, (int) (wrongTextRect.bottom-y18/3f)), wrongTextPaint, wrongText));
    }

    public void resetCard() {
        float y18 = Constants.SCREEN_HEIGHT/18f;
        labelText = orderedCards.get(number).getValue();
        labelTextPaint.setTextSize(Constants.getTextSize(labelTextRect, labelTextPaint, labelText));

        indicatorText = Integer.toString(number + 1) + " / " + Integer.toString(orderedCards.size());
        indicatorTextPaint.setTextSize(Constants.getTextSize(new Rect(indicatorTextRect.left, (int) (indicatorTextRect.top+y18/3f), indicatorTextRect.right, (int) (indicatorTextRect.bottom-y18/3f)), indicatorTextPaint, indicatorText));

        if (!fadeOn) {
            lastTime = System.currentTimeMillis();
        }
        resetTime();

        randomCards = Constants.getMostVariedArrayList(orderedCards, randomButtons.size());
        randomCards.remove(orderedCards.get(number));
        randomCards.add(0, orderedCards.get(number));

        randomButtons = Constants.shuffleArrayList(randomButtons);
        for (int index = 0; index < randomButtons.size(); index++) {
            randomButtons.get(index).resetText(randomCards.get(index).getIndex());
        }
        buttonManager.setSameFont();
    }

    public void resetTime() {
        if (!isPaused) {
            float y18 = Constants.SCREEN_HEIGHT / 18f;
            long time = System.currentTimeMillis();

            startTimerText = Integer.toString((int) (((time - startTime - pausedTimeTotal) / 1000)) / 60) + " : " + (((int) (((time - startTime - pausedTimeTotal) / 1000)) % 60 < 10) ? "0" : "") + Integer.toString((int) (((time - startTime - pausedTimeTotal) / 1000)) % 60) + "s";
            startTimerTextPaint.setTextSize(Constants.getTextSize(new Rect(startTimerTextRect.left, (int) (startTimerTextRect.top + y18 / 3f), startTimerTextRect.right, (int) (startTimerTextRect.bottom - y18 / 3f)), startTimerTextPaint, startTimerText));

            if (!fadeOn) {
                lastTimerText = Integer.toString((int) ((time - lastTime) / 1000)) + "s";
                lastTimerTextPaint.setTextSize(Constants.getTextSize(new Rect(lastTimerTextRect.left, (int) (lastTimerTextRect.top + y18 / 3f), lastTimerTextRect.right, (int) (lastTimerTextRect.bottom - y18 / 3f)), lastTimerTextPaint, lastTimerText));
            }
        }
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
            Constants.TAB.draw(canvas);

            Constants.drawTextRight(canvas, correctTextRect, correctTextPaint, correctText, Constants.SCREEN_WIDTH / 27f);
            Constants.drawImage(canvas, imagesDirectories.get(0), imagesRects.get(0));
            Constants.drawTextRight(canvas, wrongTextRect, wrongTextPaint, wrongText, Constants.SCREEN_WIDTH / 27f);
            Constants.drawImage(canvas, imagesDirectories.get(1), imagesRects.get(1));

            resetTime();
            Constants.drawTextCenter(canvas, indicatorTextRect, indicatorTextPaint, indicatorText);
            Constants.drawTextRight(canvas, startTimerTextRect, startTimerTextPaint, startTimerText, Constants.SCREEN_WIDTH / 18f);
            Constants.drawTextRight(canvas, lastTimerTextRect, lastTimerTextPaint, lastTimerText, Constants.SCREEN_WIDTH / 9f);

            canvas.drawRect(labelRect, Constants.C4F_PAINT);
            canvas.drawRect(labelRect, Constants.BORDER_PAINT);
            Constants.drawTextCenter(canvas, labelRect, labelTextPaint, labelText);

            buttonManager.draw(canvas);
            canvas.drawRect(bottomRect, Constants.C4F_PAINT);
            canvas.drawLine(bottomRect.left, bottomRect.top, bottomRect.right, bottomRect.top, Constants.BORDER_PAINT);
            pauseManager.draw(canvas);

            //if (fadeOn) {}
            if (isPaused) {
                canvas.drawRect(Constants.FULL_SCREEN_RECT, Constants.FADE_PAINT);
                canvas.drawRect(pauseRect, Constants.C5F_PAINT);
                canvas.drawRect(pauseRect, Constants.BORDER_PAINT);
                Constants.drawTextCenter(canvas, pauseRect, pauseTextPaint, pauseText);
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        Constants.TAB.getInfo().recieveTouch(event);
        Constants.TAB.getSettings().recieveTouch(event);
        if (Constants.TAB.getExit().recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }

        if (pauseButton.recieveTouch(event)){
            isPaused = true;
            pausedTime = System.currentTimeMillis();
            buttonManager.setState(false);
            pauseManager.setState(false);
            Constants.TAB.getManager().setState(false);
        }

        for (Button button : randomButtons) {
            if(button.recieveTouch(event)) {
                if (button.getText().equals(orderedCards.get(number).getIndex())) {
                    results.add(true);
                    currentButton = button;
                } else {
                    results.add(false);
                    button.setIsWrong(true);
                    currentButton = button;
                }
                for (Button trueButton : randomButtons) {
                    if (trueButton.getText().equals(orderedCards.get(number).getIndex())) {
                            trueButton.setIsCorrect(true);
                    }
                }
                buttonManager.setState(false);
                pauseManager.setState(false);
                Constants.TAB.getManager().setState(false);
                fadeOn = true;
                resetScore();
            }
        }

        if (fadeOn && !isPaused) {
            int action = event.getAction();
            float[] point = new float[]{event.getX(), event.getY(),};
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (labelRect.contains((int) point[0], (int) point[1])) {
                        onLabel = true;
                    }
                    onTouch = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!labelRect.contains((int) point[0], (int) point[1])) {
                        onLabel = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (onTouch) {
                        onClick = true;
                        onTouch = false;
                        if (!labelRect.contains((int) point[0], (int) point[1])) {
                            onLabel = false;
                        }
                    }
                    break;
            }
        } else if (isPaused) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    onTouch = true;
                case MotionEvent.ACTION_UP:
                    if (onTouch) {
                        onClick = true;
                        onTouch = false;
                    }
            }
        }

        if (onClick && fadeOn && !onLabel) {
            if (number < orderedCards.size()) {
                number += 1;
                for (Button resetButton : randomButtons) {
                    resetButton.setIsCorrect(false);
                    resetButton.setIsWrong(false);
                }

                currentButton = null;
                if (number == orderedCards.size()) {
                    Constants.SCENE_MANAGER.removeActive();
                    Constants.SCENE_MANAGER.add(new FinishScene(results, orderedCards, (int) ((System.currentTimeMillis() - startTime) / 1000)));
                } else {
                    onClick = false;
                    fadeOn = false;
                    buttonManager.setState(true);
                    pauseManager.setState(true);
                    Constants.TAB.getManager().setState(true);
                    resetCard();
                }
            }
        } else if (onClick && fadeOn) {
            onLabel = false;
            onClick = false;
            StudyCardsScene studyCardsScene = new StudyCardsScene();
            studyCardsScene.setNumber(Constants.LOAD_MANAGER.getActiveGroup().getCards().indexOf(orderedCards.get(number)));
            Constants.SCENE_MANAGER.add(studyCardsScene);
        }
        if (onClick && isPaused) {
            onClick = false;
            isPaused = false;
            buttonManager.setState(true);
            pauseManager.setState(true);
            Constants.TAB.getManager().setState(true);
            long time = System.currentTimeMillis();
            pausedTimeTotal += (time-pausedTime);
            lastTime += (time-pausedTime);
        }
    }


}
