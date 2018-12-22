package com.zerokorez.lepiametodkamemorycards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Constants {
    //*****DEVICE*****
    //layout
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    //application
    public static Activity ACTIVITY;
    public static Context CURRENT_CONTEXT;
    public static SceneManager SCENE_MANAGER;
    public static Random RANDOM;

    //memory
    public static LoadManager LOAD_MANAGER;

    //*****GRAPHICS*****
    //default colours
    public static int BLACK = Color.BLACK;
    public static int WHITE = Color.rgb(205, 205, 205);
    public static int RED = Color.rgb(160, 0, 0);
    public static int GREEN = Color.rgb(0, 128, 0);

    //custom colours
    public static int C8F = Color.rgb(255, 88, 88);
    public static int C7F = Color.rgb(224, 77, 77);
    public static int C6F = Color.rgb(192, 66, 66);
    public static int C5F = Color.rgb(160, 55, 55);
    public static int C4F = Color.rgb(128, 44, 44);
    public static int C3F = Color.rgb(96, 33, 33);
    public static int C2F = Color.rgb(64, 22, 22);
    public static int C1F = Color.rgb(32, 11, 11);

    //Tab
    public static Tab TAB;

    //Rects
    public static Rect FULL_SCREEN_RECT;
    public static Rect TITLE_RECT;
    public static Rect LIST_ITEM_RECT;
    public static Rect PARAGRAPH_BOUNDS;

    //Paints
    public static Paint FADE_PAINT;
    public static Paint SHADE_PAINT;
    public static Paint C2F_PAINT;
    public static Paint C3F_PAINT;
    public static Paint C4F_PAINT;
    public static Paint C5F_PAINT;
    public static Paint BORDER_PAINT;
    public static Paint CORRECT_PAINT;
    public static Paint WRONG_PAINT;

    //*****LANGUAGE*****
    //Characters
    public static ArrayList<Character> CONSONANTS;
    public static ArrayList<Character> VOWELS;
    public static ArrayList<Character> LR;

    public static ArrayList<String> DOUBLE_CONSONANTS;
    public static ArrayList<String> DOUBLE_VOWELS;

    //*****UTILITIES*****
    public static Rect moveRect(Rect rect, int x, int y) {
        return new Rect(rect.left + x, rect.top + y, rect.right + x, rect.bottom + y);
    }
    public static float getTextSize(Rect rect, Paint paint, String string) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.setTextSize(Constants.SCREEN_WIDTH);

        paint.getTextBounds(string, 0, string.length(), bounds);
        float width = bounds.width();

        paint.getTextBounds("Gg", 0, 2, bounds);
        float height = bounds.height();

        if(width < rect.width() && height < rect.height()) {
            return paint.getTextSize();
        } else {
            return 4 * (paint.getTextSize() / Math.max(width / (float) rect.width(), height / (float) rect.height())) / 5f;
        }
    }
    public static void drawTextCenter(Canvas canvas, Rect rect, Paint paint, String string) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.getTextBounds(string, 0, string.length(), bounds);
        float x = rect.left + rect.width()/2f - bounds.width()/2f;

        paint.getTextBounds("0", 0, 1, bounds);
        float y = rect.top + rect.height()/2f + bounds.height()/2f;

        canvas.drawText(string, x, y, paint);
    }
    public static void drawTextRight(Canvas canvas, Rect rect, Paint paint, String string, float odd) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.getTextBounds(string, 0, string.length(), bounds);
        float x = rect.right - odd - bounds.width();

        paint.getTextBounds("0", 0, 1, bounds);
        float y = rect.top + rect.height()/2f + bounds.height()/2f;

        canvas.drawText(string, x, y, paint);
    }
    public static void drawTextLeft(Canvas canvas, Rect rect, Paint paint, String string, float odd) {
        paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();

        paint.getTextBounds(string, 0, string.length(), bounds);
        float x = rect.left + odd;

        paint.getTextBounds("0", 0, 1, bounds);
        float y = rect.top + rect.height() / 2f + bounds.height() / 2f;

        canvas.drawText(string, x, y, paint);
    }
    public static Integer getMinInteger(ArrayList<Integer> numbers) {
        Integer min = numbers.get(0);
        for (Integer number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }
    public static Float getMinFloat(ArrayList<Float> numbers) {
        Float min = numbers.get(0);
        for (Float number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }
    public static ArrayList cloneArrayList(ArrayList arrayList) {
        ArrayList newArrayList = new ArrayList();
        newArrayList.addAll(arrayList);
        return newArrayList;
    }
    public static ArrayList shuffleArrayList(ArrayList arrayList) {
        ArrayList oldList = cloneArrayList(arrayList);
        ArrayList newList = new ArrayList<>();
        int size = oldList.size();
        for (int index = 0; index < size; index++) {
            Object item = oldList.get(RANDOM.nextInt(oldList.size()));
            newList.add(item);
            oldList.remove(item);
        }
        return newList;
    }
    public static ArrayList getMostVariedArrayList(ArrayList arrayList, int number) {
        ArrayList newList = new ArrayList<>();
        int index = 1;
        while (newList.size() < number) {
            for (Object object : arrayList) {
                if (newList.size() < number) {
                    if (Collections.frequency(newList, object) < index) {
                        newList.add(object);
                    }
                }
            }
            index++;
        }
        return newList;
    }
    public static Integer[] range(Integer limit) {
        Integer[] array = new Integer[limit];
        for (Integer index = 0; index < limit; index++) {
            array[index] = index;
        }
        return array;
    }

    public static void drawImage(Canvas canvas, String imageDirectory, Rect rect) {
        if (Constants.LOAD_MANAGER.getImage(imageDirectory) != null) {
            if (!Constants.LOAD_MANAGER.getImage(imageDirectory).isRecycled()) {
                canvas.drawBitmap(Constants.LOAD_MANAGER.getImage(imageDirectory), null, rect, null);
            } else {
                System.out.println("Image is recycled!");
                LOAD_MANAGER.addImage(imageDirectory);
            }
        } else {
            System.out.println("Image is not found!");
            LOAD_MANAGER.addImage(imageDirectory);
        }
    }

    public static void print(Object object) {
        System.out.println(object);
    }
}
