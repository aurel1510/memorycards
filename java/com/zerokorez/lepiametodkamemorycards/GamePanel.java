package com.zerokorez.lepiametodkamemorycards;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread mainThread;
    private SideThread sideThread;

    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;
        Constants.LOAD_MANAGER = new LoadManager();
        Constants.LOAD_MANAGER.setUpConstants();
        Constants.SCENE_MANAGER = new SceneManager();

        sideThread = new SideThread();
        mainThread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        sideThread = new SideThread();
        mainThread = new MainThread(getHolder(), this);

        sideThread.setRunning(true);
        sideThread.start();

        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
        retry = true;
        while(retry) {
            try {
                sideThread.setRunning(false);
                sideThread.join();
            } catch(Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Constants.SCENE_MANAGER.recieveTouch(event);

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        Constants.SCENE_MANAGER.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Constants.SCENE_MANAGER.draw(canvas);
    }
}
