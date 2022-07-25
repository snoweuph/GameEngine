package org.euph;

import org.euph.engine.renderSystem.DisplayManager;

public class Main {
    public static void main(String[] args) {

        DisplayManager.createDisplay(1280, 720, "Test");

        //the main loop
        while(!DisplayManager.isCloseRequested()){
            DisplayManager.UpdateDisplay();
        }

        //destroy the window and cleanup
        DisplayManager.destroyDisplay();

    }
}