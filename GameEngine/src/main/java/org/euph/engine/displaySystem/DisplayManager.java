package org.euph.engine.displaySystem;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

//TODO: JavaDoc

public class DisplayManager {

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
            //Can't use Window.destroy directly here, because that would modify the ArrayList directly while iterating
            glfwFreeCallbacks(window.getWindowID());
            glfwDestroyWindow(window.getWindowID());
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
        //Throw error if the DisplayManager is not initialized
        if(!initialized)throw new IllegalStateException("Window can't be created if the DisplayManager is not initialized");

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

        //Enable Vsync and Bind Window if wanted
        if(vsync)window.vsync(true);
        if(bind)window.bind();

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        window.makePrimary();

        //Return Window
        return window;
    }
    public static Window createWindow(int width, int height, String title, boolean resizable, boolean hide, boolean vsync, boolean bind){
        //Throw error if the DisplayManager is not initialized
        if(!initialized)throw new IllegalStateException("Window can't be created if the DisplayManager is not initialized");

        //Configure the Window with the Base parameters
        glfwDefaultWindowHints();
        //Hide Window as default
        glfwWindowHint(GLFW_VISIBLE, hide ? GLFW_FALSE : GLFW_TRUE);
        //Make it no resizable as default;
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        //Create the Window and Throw an Error if that failed
        long windowID = glfwCreateWindow(width, height, title, 0, 0);
        if(windowID == 0)throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, title, width, height, 0, 0);

        //Enable Vsync and Bind Window if wanted
        if(vsync)window.vsync(true);
        if(bind)window.bind();

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        window.makePrimary();

        //Return Window
        return window;
    }
    public static Window createWindow(int width, int height, String title, GLFWImage.Buffer icon, boolean resizable, boolean hide, boolean vsync, boolean bind){
        //Throw error if the DisplayManager is not initialized
        if(!initialized)throw new IllegalStateException("Window can't be created if the DisplayManager is not initialized");

        //Configure the Window with the Base parameters
        glfwDefaultWindowHints();
        //Hide Window as default
        glfwWindowHint(GLFW_VISIBLE, hide ? GLFW_FALSE : GLFW_TRUE);
        //Make it no resizable as default;
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        //Create the Window and Throw an Error if that failed
        long windowID = glfwCreateWindow(width, height, title, 0, 0);
        if(windowID == 0)throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, title, width, height, 0, 0);

        //Enable Vsync and Bind Window if wanted
        if(vsync)window.vsync(true);
        if(bind)window.bind();

        //Add Window Icon
        window.setIcon(icon);

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        window.makePrimary();

        //Return Window
        return window;
    }

    //Window Removal
    public static void removeWindow(Window window){
        windows.remove(window);
    }

    //TODO: Primary Window Functions

    //Getter
    public static ArrayList<Window> getWindows() {
        return windows;
    }
    public static boolean isInitialized() {
        return initialized;
    }

    //Setter
    public static void setPrimary(Window primary) {
        DisplayManager.primary = primary;
    }
}
