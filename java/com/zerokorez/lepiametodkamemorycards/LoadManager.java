package com.zerokorez.lepiametodkamemorycards;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.*;

import java.io.*;
import java.util.*;

public class LoadManager {
    //generic
    private AssetManager assetManager;

    //images
    private String imagesDirectory = "drawable/";

    private HashMap<String,Bitmap> loadedImages;

    //groups
    private ArrayList<Group> loadedGroups;
    private int activeGroup ;

    public LoadManager() {
        //generic
        assetManager = Constants.CURRENT_CONTEXT.getAssets();

        //images
        if (Constants.SCREEN_WIDTH > 720 || Constants.SCREEN_HEIGHT > 1280) {
            imagesDirectory += "r1080/";
        } else {
            imagesDirectory += "r720/";
        }
        loadedImages = new HashMap<>();

        //groups
        loadedGroups = new ArrayList<>();
        activeGroup = -1;
    }

    //****GETTERS*SETTERS*****
    public void setActiveGroup(int index) {
        activeGroup = index;
    }

    public ArrayList<Group> getLoadedGroups() {
        return loadedGroups;
    }

    public Group getActiveGroup() {
        return loadedGroups.get(activeGroup);
    }

    public Bitmap getImage(String imageDirectory) {
        if (loadedImages.containsKey(imageDirectory)) {
            return loadedImages.get(imageDirectory);
        }
        return null;
    }

    //*****GENERIC*****
    public void setUpConstants() {
        //Application
        Constants.RANDOM = new Random(System.currentTimeMillis());

        //Tab
        Constants.TAB = new Tab();

        //Rects
        Constants.FULL_SCREEN_RECT = new Rect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        Constants.TITLE_RECT = new Rect(0, Constants.SCREEN_HEIGHT/18, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT/6);
        Constants.LIST_ITEM_RECT = new Rect(Constants.SCREEN_WIDTH/40, 0, Constants.SCREEN_WIDTH*38/40, Constants.SCREEN_HEIGHT/6);
        Constants.PARAGRAPH_BOUNDS = new Rect();

        //Paints
        Constants.FADE_PAINT = new Paint();
        Constants.FADE_PAINT.setColor(Constants.BLACK);
        Constants.FADE_PAINT.setAlpha(160);

        Constants.C2F_PAINT = new Paint();
        Constants.C2F_PAINT.setColor(Constants.C2F);

        Constants.C3F_PAINT = new Paint();
        Constants.C3F_PAINT.setColor(Constants.C3F);

        Constants.C4F_PAINT = new Paint();
        Constants.C4F_PAINT.setColor(Constants.C4F);

        Constants.C5F_PAINT = new Paint();
        Constants.C5F_PAINT.setColor(Constants.C5F);

        Constants.CORRECT_PAINT = new Paint();
        Constants.CORRECT_PAINT.setColor(Constants.GREEN);
        Constants.CORRECT_PAINT.setAlpha(128);

        Constants.WRONG_PAINT = new Paint();
        Constants.WRONG_PAINT.setColor(Constants.RED);
        Constants.WRONG_PAINT.setAlpha(128);

        Constants.BORDER_PAINT = new Paint();
        Constants.BORDER_PAINT.setColor(Constants.WHITE);
        Constants.BORDER_PAINT.setStyle(Paint.Style.STROKE);
        Constants.BORDER_PAINT.setStrokeWidth(2);

        Constants.SHADE_PAINT = new Paint();
        Constants.SHADE_PAINT.setColor(Constants.WHITE);
        Constants.SHADE_PAINT.setAlpha(64);

        //Characters
        Constants.CONSONANTS = new ArrayList<>();
        Collections.addAll(Constants.CONSONANTS, 'b', 'c', 'č', 'd', 'ď', 'f', 'g', 'h', 'j', 'k', 'ľ', 'm', 'n', 'ň', 'p', 'q', 's', 'š', 't', 'ť', 'v', 'w', 'x', 'z', 'ž', 'B', 'C', 'Č', 'D', 'Ď', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'Ň', 'P', 'Q', 'S', 'Š', 'T', 'Ť', 'V', 'W', 'X', 'Z', 'Ž');
        Constants.VOWELS = new ArrayList<>();
        Collections.addAll(Constants.VOWELS, 'a', 'e', 'i', 'o', 'u', 'y', 'á', 'é', 'í', 'ó' ,'ú' ,'ý', 'ä', 'ô', 'A', 'E', 'I', 'O', 'U', 'Y', 'Á', 'É', 'Í', 'Ó' ,'Ú' ,'Ý', 'Ä', 'Ô');
        Constants.LR = new ArrayList<>();
        Collections.addAll(Constants.LR, 'l', 'ĺ', 'r', 'ŕ', 'L', 'Ĺ', 'R', 'Ŕ');

        Constants.DOUBLE_CONSONANTS = new ArrayList<>();
        Collections.addAll(Constants.DOUBLE_CONSONANTS, "ch", "dz", "dž", "Ch", "Dz", "Dž", "CH", "DZ", "DŽ");
        Constants.DOUBLE_VOWELS = new ArrayList<>();
        Collections.addAll(Constants.DOUBLE_VOWELS, "ia", "ie", "iu", "au", "Ia", "Ie", "Iu", "Au", "IA", "IE", "IU", "AU");
    }

    //*****IMAGES*****
    public void addImage(String imageDirectory) {
        if (loadedImages.containsKey(imageDirectory)) {
            if (loadedImages.get(imageDirectory) != null) {
                if (!loadedImages.get(imageDirectory).isRecycled()) {
                    System.out.print("Image is already loaded!");
                } else {
                    loadImage(imageDirectory);
                }
            } else {
                loadImage(imageDirectory);
            }
        } else {
            loadImage(imageDirectory);
        }
    }
    public void manageImages() {
        //remove from memory
        Collection<String> collection = new ArrayList<>();
        loadedImages.keySet().addAll(collection);

        for (String imageDirectory : collection) {
            System.out.println(imageDirectory);
            if (!Constants.SCENE_MANAGER.getActive().getImagesDirectories().contains(imageDirectory)) {
                loadedImages.get(imageDirectory).recycle();
            }
        }
    }
    public void loadImage(String imageDirectory) {
        try {
            loadedImages.put(imageDirectory, BitmapFactory.decodeStream(assetManager.open(imagesDirectory + imageDirectory)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //*****GROUPS*****
    public void loadGroups() {
        loadedGroups = new ArrayList<>();
        for (String line : readAssets("default_list")) {
            if (!line.contains("#")) {
                Group group = new Group(readAssets(line + "/info").get(0));

                for (int index = 0; index < Integer.valueOf(readAssets(line + "/info").get(1)); index++) {
                    Card card = new Card(group);
                    ArrayList<String> data = readAssets(line + "/cards/card_" + Integer.toString(index));
                    card.setIndex(data.get(0));
                    card.setValue(data.get(1));
                    card.setText(data.get(2));
                    card.setNote(data.get(3));
                    group.addCard(card);
                }
            }
        }
    }
    public ArrayList<String> readAssets(String path) {
        ArrayList<String> lines = new ArrayList<>();
        AssetManager assetManager = Constants.CURRENT_CONTEXT.getAssets();

        try {
            InputStream inputStream = assetManager.open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    //by R9J
    private void writeToFile(String path, ArrayList<String> data) {
        String string = "";
        int index = -1;
        for (String line : data) {
            string += line;
            if (++index < data.size()-1) {
                string += "\n";
            }
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Constants.CURRENT_CONTEXT.openFileOutput(path, Context.MODE_PRIVATE));
            outputStreamWriter.write(string);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    //by R9J
    private ArrayList<String> readFromFile(String path) {
        ArrayList<String> data = new ArrayList<>();

        try {
            InputStream inputStream = Constants.CURRENT_CONTEXT.openFileInput(path);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                Collections.addAll(data, stringBuilder.toString().split("\n"));
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}