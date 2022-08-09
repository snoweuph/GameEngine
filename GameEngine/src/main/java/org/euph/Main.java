package org.euph;

import org.euph.engine.systems.display.DisplayManager;
import org.euph.engine.systems.display.Window;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.util.remotery.Remotery;
import org.lwjgl.util.remotery.RemoteryGL;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private static float x;

    public static void main(String[] args) {

        //Initialize the Window Manager
        DisplayManager.init();

        //Profiler Setup
        PointerBuffer rmt_pointer = PointerBuffer.allocateDirect(1);
        Remotery.rmt_CreateGlobalInstance(rmt_pointer);
        RemoteryGL.rmt_BindOpenGL();

        //Create the Window
        Window win = DisplayManager.createWindow(1280, 720, 0, "Hello World", true, false, true, true);

        //Show the Window
        win.show(true);

        //run the Main Loop
        while (!win.shouldClose()){
            //Start Sampling for Profiler
            Remotery.rmt_BeginCPUSample("CPU Sampling", Remotery.RMTSF_Aggregate, null);
            //Run Main Loop
            mainLoop(win);

            //Log Delta Time to Profiler
            Remotery.rmt_LogText("Main Window Delta Time is:" + win.getDelta());
            //End Sampling
            Remotery.rmt_EndCPUSample();
        }

        //Cleanup everything after closing
        DisplayManager.cleanUp();
        Remotery.rmt_DestroyGlobalInstance(rmt_pointer.get(0));
    }

    public static void mainLoop(Window win) {
        //Update Inputs, Events and Callbacks
        glfwPollEvents();

        GL30C.glClear(GL30C.GL_COLOR_BUFFER_BIT | GL30C.GL_DEPTH_BUFFER_BIT);
        GL30C.glClearColor((float) Math.sin(x) / 2f + 0.5f, (float) Math.cos(x) / 2f + 0.5f, (((float) Math.sin(x * 2f) / 2f + 0.5f) +  ((float) Math.cos(x * 0.5f) / 2f + 0.5f)) / 2f , 1.0f);
        x += win.getDelta();

        //Update the Window
        win.update();
    }
}