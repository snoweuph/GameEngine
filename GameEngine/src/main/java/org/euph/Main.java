package org.euph;

import org.euph.engine.windowSystem.WindowManager;
import org.euph.engine.windowSystem.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class Main {
    public static void main(String[] args) {

        WindowManager.init();

        Window win = WindowManager.createWindow(512, 512, true, true);

        win.show(true);

        while (!win.shouldClose()){
            glfwPollEvents();
            win.setTitle("DeltaTime is: " + win.getDelta());
            win.update();
        }
        WindowManager.cleanUp();
    }
}