package com.zerokorez.lepiametodkamemorycards;

public class SideThread extends Thread {
    private boolean running;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public SideThread() {
        super();
    }

    @Override
    public void run() {
        while (running) {
            Constants.LOAD_MANAGER.manageImages();
        }
    }
}
