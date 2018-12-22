package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;

public class FinishScene implements Scene {
    private ArrayList<String> imagesDirectories;
    private ArrayList<Rect> imagesRects;

    private ArrayList<Boolean> results;
    private ArrayList<Card> cards;

    private String titleText;
    private Paint titleTextPaint;

    private ButtonManager buttonManager;
    private Button againButton;
    private Button returnButton;

    private ResultButtonList resultButtonList;

    private String startTimerText;
    private Paint startTimerTextPaint;
    private Rect startTimerTextRect;

    private String correctText;
    private Rect correctTextRect;
    private Paint correctTextPaint;

    private String wrongText;
    private Rect wrongTextRect;
    private Paint wrongTextPaint;

    public FinishScene(ArrayList<Boolean> results, ArrayList<Card> cards, int fullTime) {
        imagesDirectories = new ArrayList<>();
        imagesDirectories.add("tick_title_rect.png");
        imagesDirectories.add("cross_title_rect.png");
        imagesDirectories.add("tick_result_button.png");
        imagesDirectories.add("cross_result_button.png");
        imagesDirectories.add("exit.png");
        imagesDirectories.add("settings.png");
        imagesDirectories.add("info.png");
        imagesRects = new ArrayList<>();

        this.results = results;
        this.cards = cards;

        float x = Constants.SCREEN_WIDTH/18f;
        float y = Constants.SCREEN_HEIGHT/18f;

        titleText = "Results";
        titleTextPaint = new Paint();
        titleTextPaint.setColor(Constants.WHITE);
        titleTextPaint.setTextSize(Constants.getTextSize(Constants.TITLE_RECT, titleTextPaint, titleText));

        buttonManager = new ButtonManager(Constants.C2F_PAINT);
        againButton = new Button(buttonManager, new Rect((int) (x*2), (int) (y*16 + y/2f), (int) (x*8), (int) (y*18 - y/2f)), "Again");
        returnButton = new Button(buttonManager, new Rect((int) (x*10), (int) (y*16 + y/2), (int) (x*16), (int) (y*18 - y/2)), "Return");
        buttonManager.setSameFont();

        resultButtonList = new ResultButtonList(cards);
        int index = -1;
        for(Card card: cards) {
            index++;
            new ResultButton(resultButtonList, card, results.get(index));
        }
        resultButtonList.setSameFontSize();

        startTimerTextRect = new Rect(Constants.SCREEN_WIDTH*2/3, (int) (y*3), Constants.SCREEN_WIDTH, (int) (y*5));
        startTimerTextPaint = new Paint();
        startTimerTextPaint.setColor(Constants.WHITE);

        correctTextRect = new Rect(0, (int) (y*3), Constants.SCREEN_WIDTH/6, (int) (y*5));
        imagesRects.add(new Rect(Constants.SCREEN_WIDTH/6, (int) (y*3 + (y*2 - Constants.SCREEN_WIDTH/9f)/2f), (int) (Constants.SCREEN_WIDTH/6f + Constants.SCREEN_WIDTH/9f), (int) (y*4 + (y*2 - Constants.SCREEN_WIDTH/9f)/2f)));
        correctTextPaint = new Paint();
        correctTextPaint.setColor(Constants.WHITE);

        wrongTextRect = new Rect((int) (Constants.SCREEN_WIDTH/6f + Constants.SCREEN_WIDTH/9f), (int) (y*3), (int) (Constants.SCREEN_WIDTH/3f + Constants.SCREEN_WIDTH/9f), (int) (y*5));
        imagesRects.add(new Rect((int) (Constants.SCREEN_WIDTH/3f + Constants.SCREEN_WIDTH/9f), (int) (y*3 + (y*2 - Constants.SCREEN_WIDTH/9f)/2f), (int) (Constants.SCREEN_WIDTH/3f + Constants.SCREEN_WIDTH*2/9f), (int) (y*4 + (y*2 - Constants.SCREEN_WIDTH/9f)/2f)));
        wrongTextPaint = new Paint();
        wrongTextPaint.setColor(Constants.WHITE);

        correctText = Integer.toString(Collections.frequency(results, true));
        wrongText = Integer.toString(Collections.frequency(results, false));
        correctTextPaint.setTextSize(Constants.getTextSize(new Rect(correctTextRect.left, (int) (correctTextRect.top+y/3f), correctTextRect.right, (int) (correctTextRect.bottom-y/3f)), correctTextPaint, correctText));
        wrongTextPaint.setTextSize(Constants.getTextSize(new Rect(wrongTextRect.left, (int) (wrongTextRect.top+y/3f), wrongTextRect.right, (int) (wrongTextRect.bottom-y/3f)), wrongTextPaint, wrongText));

        startTimerText = Integer.toString(fullTime/60) + " : " + ((fullTime%60 < 10) ? "0" : "") + Integer.toString(fullTime%60) + "s";
        startTimerTextPaint.setTextSize(Constants.getTextSize(new Rect(startTimerTextRect.left, (int) (startTimerTextRect.top + y / 3f), startTimerTextRect.right, (int) (startTimerTextRect.bottom - y / 3f)), startTimerTextPaint, startTimerText));
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
            resultButtonList.draw(canvas);

            float y = Constants.SCREEN_HEIGHT / 18f;
            Rect rect = Constants.moveRect(Constants.TITLE_RECT, 0, (int) (y*2));
            canvas.drawRect(rect, Constants.C3F_PAINT);
            Constants.drawTextRight(canvas, startTimerTextRect, startTimerTextPaint, startTimerText, Constants.SCREEN_WIDTH / 18f);
            canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, Constants.BORDER_PAINT);

            Constants.drawTextRight(canvas, correctTextRect, correctTextPaint, correctText, Constants.SCREEN_WIDTH / 27f);
            canvas.drawBitmap(Constants.LOAD_MANAGER.getImage(imagesDirectories.get(0)), null, imagesRects.get(0), null);
            Constants.drawTextRight(canvas, wrongTextRect, wrongTextPaint, wrongText, Constants.SCREEN_WIDTH / 27f);
            canvas.drawBitmap(Constants.LOAD_MANAGER.getImage(imagesDirectories.get(1)), null, imagesRects.get(1), null);

            rect = Constants.TITLE_RECT;
            canvas.drawRect(rect, Constants.C3F_PAINT);
            canvas.drawLine(Constants.SCREEN_WIDTH * 9 / 40f, rect.bottom, Constants.SCREEN_WIDTH * 317 / 400f, rect.bottom, Constants.BORDER_PAINT);
            Constants.drawTextCenter(canvas, rect, titleTextPaint, titleText);
            Constants.TAB.draw(canvas);

            rect = Constants.moveRect(Constants.TITLE_RECT, 0, (int) (y * 15));
            canvas.drawRect(rect, Constants.C3F_PAINT);
            canvas.drawLine(rect.left, rect.top, rect.right, rect.top, Constants.BORDER_PAINT);
            buttonManager.draw(canvas);
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if(Constants.TAB.getExit().recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }
        Constants.TAB.getSettings().recieveTouch(event);
        Constants.TAB.getInfo().recieveTouch(event);

        if (againButton.recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
            Constants.SCENE_MANAGER.add(new CardQuizScene());
        }
        if (returnButton.recieveTouch(event)) {
            Constants.SCENE_MANAGER.removeActive();
        }

        resultButtonList.recieveTouch(event);
    }
}
