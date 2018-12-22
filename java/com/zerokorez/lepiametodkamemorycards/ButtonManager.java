package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class ButtonManager {
    private ArrayList<Button> buttons;
    private ArrayList<ImageButton> imageButtons;
    private Boolean state;

    private Paint basePaint;
    private int textColor;

    public ButtonManager(Paint paint) {
        buttons = new ArrayList<>();
        imageButtons = new ArrayList<>();
        state = true;

        basePaint = paint;
        textColor = Constants.WHITE;
    }

    public Paint getBasePaint() {
        return basePaint;
    }

    public int getTextColor() {
        return textColor;
    }

    public ArrayList<Button> getButtons() {
        return buttons;
    }

    public ArrayList<ImageButton> getImageButtons() {
        return imageButtons;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean newState) {
        state = newState;
    }

    public void setSameFont() {
        ArrayList<Float> sizes = new ArrayList<>();
        for (Button button: buttons) {
            sizes.add(button.getPaint().getTextSize());
        }
        float min = Constants.getMinFloat(sizes);
        for (Button button: buttons) {
            button.getPaint().setTextSize(min);
        }
    }

    public void add(Button button) {
        buttons.add(button);
    }

    public void add(ImageButton imageButton) {
        imageButtons.add(imageButton);
    }

    public void draw(Canvas canvas) {
        for (Button button: buttons) {
            button.draw(canvas);
        }
        for (ImageButton imageButton: imageButtons) {
            imageButton.draw(canvas);
        }
    }
}
