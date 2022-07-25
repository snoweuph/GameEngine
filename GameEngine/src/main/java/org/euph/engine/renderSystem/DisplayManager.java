package org.euph.engine.renderSystem;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class DisplayManager {

    private static long window;

    public  static void createDisplay(int width, int height, String title) {
        //Setup error Callback to use error messages
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to Initialize GLFW");
        }

        //configure the Window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //create the Window
        window = glfwCreateWindow(width, height, title, 0, 0);
        if(window == 0){
            throw  new RuntimeException("Failed to create the GLFW Window");
        }

        //Set up Callback to Close the Window when the user presses the close button
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(window, true);
            }
        });

        //Get the resolution of the primary Monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //Ceter the window
        glfwSetWindowPos(
                window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

        //set the opengl Context to the current window
        glfwMakeContextCurrent(window);
        //enable v-sync
        glfwSwapInterval(1);
        //make the Window Visible
        glfwShowWindow(window);
        //Bin glfw window context to lwjgl
        GL.createCapabilities();
        //TODO: Start the Delta Time
    }

    public static boolean isCloseRequested() {
        return  glfwWindowShouldClose(window);
    }

    public static  void UpdateDisplay(){
        //update the window
        glfwSwapBuffers(window);
        //process Inputs
        glfwPollEvents();

        //TODO: Update the Delta Time
    }

    public static void destroyDisplay() {
        //Cleanup
        cleanUp();
        //terminate GFLW amd free the error Callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private static  void cleanUp() {
        //free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    //getter
    public static long getWindow() {
        return window;
    }
}
