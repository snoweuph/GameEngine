package org.euph.engine.windowSystem;

import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class WindowManager {

    //Make a boolean to see if the DisplayManager is Initialized
    private static boolean initialized = false;

    private static ArrayList<Window> windows;
    private static Window primary;

    //Initializing and Cleanup
    public static int init(){
        //Return 1 if already initialized
        if(initialized) return 1;

        //Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()) throw new IllegalStateException("Unable to Initialize GLFW");

        //Initialize the array
        windows = new ArrayList<Window>();

        //Set Initialized to true
        initialized = true;

        return 0;
    }
    public static  void cleanUp() {
        //destroy all windows
        for (Window window : windows) {
            window.destroy();
        }
        windows = new ArrayList<Window>();
        //Terminate GLFW
        glfwTerminate();
        //Free Error Callback
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

        //Set Initialized to false
        initialized = false;
    }

    //Window creation functions
    public static Window createWindow(int width, int height, boolean vsync, boolean bind){
        //Throw error if the WindowManager is not initialized
        if(!initialized)throw new IllegalStateException("Window can't be created if the WindowManager is not initialized");

        //Configure the Window with the Base parameters
        glfwDefaultWindowHints();
        //Hide Window as default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        //Make it no resizable as default;
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        //Create the Window and Throw an Error if that failed
        long windowID = glfwCreateWindow(width, height, "new Window", 0, 0);
        if(windowID == 0)throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, "new Window", width, height, 0, 0);

        glfwMakeContextCurrent(windowID);

        //enable Vsync and Bind Window if wanted
        if(vsync)window.vsync(true);
        if(bind)window.bind();

        //Append to array-list of windows
        windows.add(window);

        return window;
    }

    //TODO: Windows Array functions

    //TODO: Primary Window Functions

    //TODO: Getter and Setter ->

    //Getter
    public static boolean isInitialized() {
        return initialized;
    }
}
