package com.zerokorez.lepiametodkamemorycards;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Collections;

public class Paragraph {
    private Paint paint;
    private ArrayList<String> lines;
    private int center;

    public Paragraph(String text, float x, Rect rect) {
        lines = new ArrayList<>();

        paint = new Paint();
        paint.setTextSize(Constants.getTextSize(rect, paint, "Gg"));

        String[] array = text.split("<");
        String[] words = array[0].split(" ");
        ArrayList<String> syllables;

        String line = "";
        for (String word: words) {
            if (word.contains(" ")) {
                word = word.replaceAll(" ", "");
            }
            if (getTextWidth(line + word, paint) < rect.width() - x/2f) {
                line = line + word + " ";
            } else if (getTextWidth(word, paint) < rect.width() - x/2f){
                syllables = divideWord(word);
                Boolean isStopped = false;
                String newWord = "";
                Integer index = 0;
                for (String syllable : syllables) {
                    if (getTextWidth(line + syllable + "-", paint) < rect.width() - x/2f && !isStopped) {
                        line += syllable;
                    } else {
                        if (index != 0 && !isStopped) {
                            line += "-";
                        }
                        isStopped = true;
                        newWord += syllable;
                    }
                    index++;
                }
                lines.add(line);
                line = "" + newWord + " ";
            }
        }
        if (line != "") {
            lines.add(line);
        }
        removeSpace();
        paint.setTextAlign(Paint.Align.LEFT);

        if (array.length > 1) {
            if (array[1].contains("B") && !array[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (!array[1].contains("B") && array[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            } else if (array[1].contains("B") && array[1].contains("I")) {
                paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            }
            if (array[1].contains("r")) {
                paint.setColor(Color.RED);
            } else if (array[1].contains("g")) {
                paint.setColor(Color.GREEN);
            } else if (array[1].contains("b")) {
                paint.setColor(Color.BLUE);
            }
            if (array[1].contains("n") || array[1].contains("N")) {
                lines.add("");
            }
            if (array[1].contains("L")) {
                center = 0;
            } else if (array[1].contains("C")) {
                center = 1;
            } else if (array[1].contains("R")) {
                center = 2;
            } else {
                center = 0;
            }
        }
    }

    public static ArrayList<String> divideWord(String word) {
        ArrayList<String> syllables = new ArrayList<>();
        String syllable = "";
        int index = 0;
        while(true) {
            while(index <= word.length() - 1) {
                if (Constants.CONSONANTS.contains(word.charAt(index))) {
                    syllable = syllable + word.charAt(index++);
                } else {
                    ArrayList result;
                    if (Constants.VOWELS.contains(word.charAt(index))) {
                        result = divideVowel(syllables, syllable, word, index);
                        syllable = (String)result.get(0);
                        word = (String)result.get(1);
                        index = (Integer)result.get(2);
                    } else if (Constants.LR.contains(word.charAt(index))) {
                        syllable = syllable + word.charAt(index++);
                        if (word.length() - 1 < index) {
                            if (word.length() - 1 == index) {
                                syllables.add(syllable + word.charAt(index++));
                                syllable = "";
                            }
                        } else if (Constants.VOWELS.contains(word.charAt(index))) {
                            result = divideVowel(syllables, syllable, word, index);
                            syllable = (String)result.get(0);
                            word = (String)result.get(1);
                            index = (Integer)result.get(2);
                        } else if (word.length() - 1 < index) {
                            syllables.add(syllable);
                            syllable = "";
                        } else if (word.length() - 1 > index + 1) {
                            if (Constants.VOWELS.contains(word.charAt(index + 1))) {
                                syllables.add(syllable);
                                syllable = "";
                            } else {
                                if (Constants.DOUBLE_CONSONANTS.contains(""+word.charAt(index)+word.charAt(index+1))) {
                                    if (Constants.VOWELS.contains(word.charAt(index+2))) {
                                        syllables.add(syllable);
                                        syllable = "";
                                    } else {
                                        syllable = syllable + word.charAt(index++);
                                        syllable = syllable + word.charAt(index++);
                                        syllables.add(syllable);
                                        syllable = "";
                                    }
                                } else {
                                    syllable = syllable + word.charAt(index++);
                                    syllables.add(syllable);
                                    syllable = "";
                                }
                            }
                        } else if (word.length() - 1 != index + 1) {
                            if (word.length() - 1 == index) {
                                syllable = syllable + word.charAt(index++);
                                syllables.add(syllable);
                                syllable = "";
                            }
                        } else if (Constants.VOWELS.contains(word.charAt(index + 1))) {
                            syllables.add(syllable);
                            syllable = "";
                        } else {
                            Integer[] var5 = range(2);
                            int var6 = var5.length;
                            for(int var7 = 0; var7 < var6; ++var7) {
                                int number = var5[var7];
                                syllable = syllable + word.charAt(index++);
                            }
                            syllables.add(syllable);
                            syllable = "";
                        }
                    } else {
                        syllable += word.charAt(index++);
                        if (word.length()-1 < index) {
                            syllables.add(syllable);
                            syllable = "";
                        }
                    }
                }
            }
            return syllables;
        }
    }

    public static ArrayList<Object> divideVowel(ArrayList<String> syllables, String syllable, String word, Integer index) {
        StringBuilder var10000 = (new StringBuilder()).append(syllable);
        Integer var4 = index;
        index = index + 1;
        syllable = var10000.append(word.charAt(var4)).toString();
        Integer var6;
        String lastString;
        if (word.length() - 1 > index + 1) {
            if (Constants.VOWELS.contains(word.charAt(index))) {
                lastString = "" + word.charAt(index - 1) + word.charAt(index);
                if (Constants.DOUBLE_VOWELS.contains(lastString)) {
                    var10000 = (new StringBuilder()).append(syllable);
                    var6 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var6)).toString();
                }
            }
            if (!Constants.CONSONANTS.contains(word.charAt(index)) && !Constants.LR.contains(word.charAt(index)) || !Constants.CONSONANTS.contains(word.charAt(index + 1)) && !Constants.LR.contains(word.charAt(index + 1))) {
                syllables.add(syllable);
                syllable = "";
            } else {
                if (Constants.DOUBLE_CONSONANTS.contains(""+word.charAt(index)+word.charAt(index+1))) {
                    if (Constants.VOWELS.contains(word.charAt(index+2))) {
                        syllables.add(syllable);
                        syllable = "";
                    } else {
                        var10000 = (new StringBuilder()).append(syllable);
                        var6 = index;
                        index = index + 1;
                        syllable = var10000.append(word.charAt(var6)).toString();
                        var10000 = (new StringBuilder()).append(syllable);
                        var6 = index;
                        index = index + 1;
                        syllable = var10000.append(word.charAt(var6)).toString();
                        syllables.add(syllable);
                        syllable = "";
                    }
                } else {
                    var10000 = (new StringBuilder()).append(syllable);
                    var6 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var6)).toString();
                    syllables.add(syllable);
                    syllable = "";
                }
            }
        } else if (word.length() - 1 == index + 1) {
            int var7;
            int var8;
            int number;
            Integer var10;
            Integer[] var13;
            if (Constants.VOWELS.contains(word.charAt(index))) {
                lastString = "" + word.charAt(index - 1) + word.charAt(index);
                String nextString = "" + word.charAt(index) + word.charAt(index + 1);
                if (Constants.DOUBLE_VOWELS.contains(lastString) && !compareStrings(nextString, "um") && !compareStrings(nextString, "us")) {
                    var13 = range(2);
                    var7 = var13.length;
                    for(var8 = 0; var8 < var7; ++var8) {
                        number = var13[var8];
                        var10000 = (new StringBuilder()).append(syllable);
                        var10 = index;
                        index = index + 1;
                        syllable = var10000.append(word.charAt(var10)).toString();
                    }
                    syllables.add(syllable);
                    syllable = "";
                } else if (Constants.DOUBLE_VOWELS.contains(lastString) && (compareStrings(nextString, "um") || compareStrings(nextString, "us"))) {
                    syllables.add(syllable);
                    syllable = "";
                }
            } else if ((Constants.CONSONANTS.contains(word.charAt(index)) || Constants.LR.contains(word.charAt(index))) && (Constants.CONSONANTS.contains(word.charAt(index + 1)) || Constants.LR.contains(word.charAt(index + 1)))) {
                var13 = range(2);
                var7 = var13.length;
                for(var8 = 0; var8 < var7; ++var8) {
                    number = var13[var8];
                    var10000 = (new StringBuilder()).append(syllable);
                    var10 = index;
                    index = index + 1;
                    syllable = var10000.append(word.charAt(var10)).toString();
                }
                syllables.add(syllable);
                syllable = "";
            } else {
                syllables.add(syllable);
                syllable = "";
            }
        } else if (word.length() - 1 == index) {
            var10000 = (new StringBuilder()).append(syllable);
            var6 = index;
            index = index + 1;
            syllable = var10000.append(word.charAt(var6)).toString();
            syllables.add(syllable);
            syllable = "";
        } else {
            syllables.add(syllable);
            syllable = "";
        }
        ArrayList<Object> result = new ArrayList<>();
        Collections.addAll(result, syllable, word, index);
        return result;
    }

    public static Integer[] range(Integer limit) {
        Integer[] array = new Integer[limit];
        for(Integer index = 0; index < limit; index = index + 1) {
            array[index] = index;
        }
        return array;
    }

    public static Boolean compareStrings(String string0, String string1) {
        if (string0.length() != string1.length()) {
            return false;
        } else {
            for(int index = 0; index < string0.length(); ++index) {
                if (string0.charAt(index) != string1.charAt(index)) {
                    return false;
                }
            }
            return true;
        }
    }

    public ArrayList<String> getLines() {
        return lines;
    }
    public Paint getPaint() {
        return paint;
    }
    public int getCenter() {
        return center;
    }

    public int getTextWidth(String text, Paint paint) {
        paint.getTextBounds(text, 0, (text.length() > 0) ? text.length()-1 : text.length(), Constants.PARAGRAPH_BOUNDS);
        return Constants.PARAGRAPH_BOUNDS.width();
    }
    public void removeSpace() {
        ArrayList<String> newLines = new ArrayList<>();
        for (String line : lines) {
            if (line != " " && line != "" && line.length() > 1) {
                while (line.charAt(0) == ' ') {
                    line = line.replace(" ", "");
                }
                newLines.add(line);
            }
        }
        lines = newLines;
    }
}