package org.euph;

import org.euph.engine.windowSystem.WindowManager;
import org.euph.engine.windowSystem.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Main {
    public static void main(String[] args) {

        //Initialize the Window Manager
        WindowManager.init();

        //Create the Window
        Window win = WindowManager.createWindow(512, 512, true, true);

        //Show the Window
        win.show(true);

        //run the Main Loop
        while (!win.shouldClose()){
            mainLoop(win);
        }

        //Cleanup everything after closing
        WindowManager.cleanUp();
    }

    public static void mainLoop(Window win) {
        glfwPollEvents();
        win.setTitle("DeltaTime is: " + win.getDelta());
        win.update();
    }
}