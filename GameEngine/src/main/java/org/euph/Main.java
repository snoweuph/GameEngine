package org.euph;

import org.euph.engine.displaySystem.DisplayManager;
import org.euph.engine.displaySystem.Window;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public static void main(String[] args) {

        //Initialize the Window Manager
        DisplayManager.init();

        //Create the Window
        Window win = DisplayManager.createWindow(1280, 720, 0, "Hello World", true, false, true, true);

        //Show the Window
        win.show(true);

        //run the Main Loop
        while (!win.shouldClose()){
            mainLoop(win);
        }

        //Cleanup everything after closing
        DisplayManager.cleanUp();
    }

    public static void mainLoop(Window win) {
        glfwPollEvents();

        win.setTitle("DeltaTime is: " + win.getDelta());

        win.update();
    }
}