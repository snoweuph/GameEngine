package org.euph.engine.displaySystem;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

/** The Manager for Windows. It Wraps GLFW in a way to Integrate better into this Project.
 *  It provides some basic functionality, stores the Windows and has a reference to a Primary Window
 *  the Primary is an easy way to get some basic Window functionality faster and even if you don't have a direct reference to a window.
 *
 * @author snoweuph
 * @version 1.0
 */
public class DisplayManager {

    //Make a boolean to see if the DisplayManager is Initialized
    private static boolean initialized = false;

    private static ArrayList<Window> windows;
    private static Window primary;

    //Initializing and Cleanup
    /** Initializes the Display Manager and GLFW inside it.
     *
     * @return 0 no problems. 1 already initialized (did nothing).
     *
     * @author snoweuph
     */
    public static int init(){
        //Return 1 if already initialized
        if(initialized) return 1;

        //Setup Error Callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()) throw new IllegalStateException("Unable to Initialize GLFW");

        //Initialize the array
        windows = new ArrayList<>();

        //Set Initialized to true
        initialized = true;

        return 0;
    }
    /** Unbinds and Destroys all Window. Clears the List of Windows. Terminates GLFW and frees all other Callbacks.
     *
     * @author snoweuph
     */
    public static  void cleanUp() {
        //destroy all windows
        for (Window window : windows) {
            //Can't use Window.destroy directly here, because that would modify the ArrayList directly while iterating
            glfwFreeCallbacks(window.getWindowID());
            glfwDestroyWindow(window.getWindowID());
        }
        windows = new ArrayList<>();
        //Terminate GLFW
        glfwTerminate();
        //Free Error Callback
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

        //Set Initialized to false
        initialized = false;
    }

    //Window creation functions
    /** Creates a new Window with the minimal amount of parameters.
     *
     * @param width the window width.
     * @param height the window height.
     * @param vsync Whether Vsync should be enabled.
     * @param bind Whether this window should be bound directly.
     *
     * @return a new {@link org.euph.engine.displaySystem.Window Window} Object.
     *
     * @see #createWindow(int, int, long, String, boolean, boolean, boolean, boolean) createWindow medium parameters
     * @see #createWindow(int, int, long, String, GLFWImage.Buffer, boolean, boolean, boolean, boolean) createWindow maximum parameters
     *
     * @author snoweuph
     */
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
        if(windowID == 0) throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, "new Window", width, height, 0, 0);

        //Enable Vsync and Bind Window if wanted
        if(vsync) window.vsync(true);
        if(bind) window.bind();

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        if(primary == null) window.makePrimary();

        //Return Window
        return window;
    }
    /** Creates a new Window with a medium amount of parameters
     *
     * @param width the window width.
     * @param height the window height.
     * @param fullscreenMonitor the id of the monitor on which it should be fullscreen. 0 for windowed mode.
     * @param title the window title.
     * @param resizable Whether the Window should be resizable.
     * @param vsync Whether Vsync should be enabled.
     * @param bind Whether this window should be bound directly.
     *
     * @return a new {@link org.euph.engine.displaySystem.Window Window} Object.
     *
     * @see #createWindow(int, int, boolean, boolean) createWindow minimal parameters
     * @see #createWindow(int, int, long, String, GLFWImage.Buffer, boolean, boolean, boolean, boolean) createWindow maximum parameters
     *
     * @author snoweuph
     */
    public static Window createWindow(int width, int height, long fullscreenMonitor, String title, boolean resizable, boolean hide, boolean vsync, boolean bind){
        //Throw error if the DisplayManager is not initialized
        if(!initialized) throw new IllegalStateException("Window can't be created if the DisplayManager is not initialized");

        //Configure the Window with the Base parameters
        glfwDefaultWindowHints();
        //Hide Window as default
        glfwWindowHint(GLFW_VISIBLE, hide ? GLFW_FALSE : GLFW_TRUE);
        //Make it no resizable as default;
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        //Create the Window and Throw an Error if that failed
        long windowID = glfwCreateWindow(width, height, title, fullscreenMonitor, 0);
        if(windowID == 0) throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, title, width, height, 0, 0);

        //Enable Vsync and Bind Window if wanted
        if(vsync) window.vsync(true);
        if(bind) window.bind();

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        if(primary == null) window.makePrimary();

        //Return Window
        return window;
    }
    /** Creates a new Window with the maximum amount of parameters
     *
     * @param width the window width.
     * @param height the window height.
     * @param fullscreenMonitor the id of the monitor on which it should be fullscreen. 0 for windowed mode.
     * @param title the window title.
     * @param icon the Icon for the Window.
     * @param resizable Whether the Window should be resizable.
     * @param hide Whether the Window should be hidden on startup or directly be shown.
     * @param vsync Whether Vsync should be enabled.
     * @param bind Whether this window should be bound directly.
     *
     * @return a new {@link org.euph.engine.displaySystem.Window Window} Object.
     *
     * @see #createWindow(int, int, long, String, boolean, boolean, boolean, boolean) createWindow medium parameters
     * @see #createWindow(int, int, long, String, GLFWImage.Buffer, boolean, boolean, boolean, boolean) createWindow maximum parameters
     *
     * @author snoweuph
     */
    public static Window createWindow(int width, int height, long fullscreenMonitor, String title, GLFWImage.Buffer icon, boolean resizable, boolean hide, boolean vsync, boolean bind){
        //Throw error if the DisplayManager is not initialized
        if(!initialized) throw new IllegalStateException("Window can't be created if the DisplayManager is not initialized");

        //Configure the Window with the Base parameters
        glfwDefaultWindowHints();
        //Hide Window as default
        glfwWindowHint(GLFW_VISIBLE, hide ? GLFW_FALSE : GLFW_TRUE);
        //Make it no resizable as default;
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);

        //Create the Window and Throw an Error if that failed
        long windowID = glfwCreateWindow(width, height, title, fullscreenMonitor, 0);
        if(windowID == 0) throw new RuntimeException("Failed to create the GLFW Window");

        //Create Window Object
        Window window = new Window(windowID, title, width, height, 0, 0);

        //Enable Vsync and Bind Window if wanted
        if(vsync) window.vsync(true);
        if(bind) window.bind();

        //Add Window Icon
        window.setIcon(icon);

        //Append to array-list of windows
        windows.add(window);

        //make Primary
        if(primary == null) window.makePrimary();

        //Return Window
        return window;
    }

    //Window Removal
    /** Removes a single Window from the Windows list. is needed for Windows to destroy themselves.
     *
     * @param window the Window to remove from the list.
     *
     * @author snoweuph
     */
    public static void removeWindow(Window window){
        windows.remove(window);
    }

    //Primary Window Fast Access Functions

    /** Binds the Primary Window.
     *
     * @author snoweuph
     */
    public void pBind() {
        if(primary == null) return;
        primary.bind();
    }
    /** Shows the Primary Window.
     *
     * @author snoweuph
     */
    public void pShow(){
        if(primary == null) return;
        primary.show(true);
    }
    /** Hides the Primary Window.
     *
     * @author snoweuph
     */
    public void pHide(){
        if(primary == null) return;
        primary.show(false);
    }

    /** @return the Window ID of the Primary Window.
     *
     * @author snoweuph
     */
    public long pID(){
        if(primary == null) return 0L;
        return primary.getWindowID();
    }

    /** @return the Delta Time of the Primary Window.
     *
     * @author snoweuph
     */
    public float pDelta(){
        if(primary == null) return 0;
        return primary.getDelta();
    }
    /** @return the Scaled Delta Time of the Primary Window.
     *
     * @author snoweuph
     */
    public float pDeltaScaled(){
        if(primary == null) return 0;
        return primary.getScaledDelta();
    }

    //Getter
    /** @return the list of all windows.
     *
     * @author snoweuph
     */
    public static ArrayList<Window> getWindows() {
        return windows;
    }
    /** @return whether the DisplayManager is Initialized or not.
     *
     * @author snoweuph
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /** @return the current Primary Window.
     *
     * @author snoweuph
     */
    public static Window getPrimary() {
        return primary;
    }

    //Setter
    /** Sets the Primary Window.
     *
     * @param window the Window which should be the new Primary.
     *
     * @author snoweuph
     */
    public static void setPrimary(Window window) {
        DisplayManager.primary = window;
    }
}
