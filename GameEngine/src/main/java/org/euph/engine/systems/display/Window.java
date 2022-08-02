package org.euph.engine.systems.display;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

/** An Object that defines a Window and is a wrapper for GLFW.
 * @author snoweuph
 * @version 1.0
 */
public class Window {
    private final long windowID;
    private String title;
    private int width;
    private int height;
    private int posX;
    private int posY;
    private long lastFrameTime;
    private float delta;

    /** Creates a new Window Object to wrap a GLFW Window.
     *
     * @param windowID the ID of the GLFW Window this will be used to wrap.
     * @param title the current Title of the GLFW Window.
     * @param width the current Width of the GLFW Window.
     * @param height the current Height of the GLFW Window.
     * @param posX the current X Position of the GLFW Window.
     * @param posY the current Y Position of the GLFW Window.
     *
     * @author snoweuph
     */
    public Window(long windowID, String title, int width, int height, int posX, int posY) {
        //Set start Values
        this.windowID = windowID;
        this.title = title;
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        //Bind Callbacks
        glfwSetWindowSizeCallback(windowID, c_windowSize);
        glfwSetWindowPosCallback(windowID, c_windowPosition);
        glfwSetWindowCloseCallback(windowID, c_windowClose);

        //Start the Delta Time
        lastFrameTime = getCurrentTime();
    }

    /** Binds this Window in GLFW for the current Thread. The Limitations are the same as {@link org.lwjgl.glfw.GLFW#glfwMakeContextCurrent(long window)}.
     *
     * @author snoweuph
     */
    public void bind(){
        glfwMakeContextCurrent(windowID);
        GL.createCapabilities();
    }

    /** Hides or shows the Window.
     *
     * @param enabled Whether the Window should be hidden or shown.
     *
     * @author snoweuph
     */
    public void show(boolean enabled){
        if(enabled){
            glfwShowWindow(windowID);
            return;
        }
        glfwHideWindow(windowID);
    }

    /** Makes this Window the Primary Window of the {@link DisplayManager}.
     *
     * @author snoweuph
     */
    public void makePrimary(){
        DisplayManager.setPrimary(this);
    }

    /** Enables or Disables VSync. Defaults is decided by {@link DisplayManager#createWindow(int, int, boolean, boolean) CreateWindow}.
     * 
     * @param enabled Whether VSync should be enabled or not.
     *
     * @see DisplayManager#createWindow(int, int, boolean, boolean) createWindow minimal parameters
     * @see DisplayManager#createWindow(int, int, long, String, boolean, boolean, boolean, boolean) createWindow medium parameters
     * @see DisplayManager#createWindow(int, int, long, String, GLFWImage.Buffer, boolean, boolean, boolean, boolean) createWindow maximum parameters
     * 
     * @author snoweuph
     */
    public void vsync(boolean enabled){
        long oldContext = glfwGetCurrentContext();
        glfwMakeContextCurrent(windowID);
        if(enabled){
            glfwSwapInterval(1);
            glfwMakeContextCurrent(oldContext);
            return;
        }
        glfwSwapInterval(0);
        glfwMakeContextCurrent(oldContext);
    }

    /** Destroys this Window, unbinds all callbacks and removes itself from {@link DisplayManager#windows}.
     *
     * @author snoweuph
     */
    public void destroy(){
        //Unbind all Callbacks before Destroying the Window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        DisplayManager.removeWindow(this);
    }

    /** Updates this window. It swaps the Buffers and updates the {@link #getDelta() DeltaTime}.
     *
     * @author snoweuph
     */
    public void update(){
        glfwSwapBuffers(windowID);
        //Update the delta Time (the time that was needed to render the last frame)
        updateDeltaTime();
    }
    private void updateDeltaTime(){
        long currentFrameTime = getCurrentTime();
        delta = (float)(currentFrameTime - lastFrameTime) / GLFW.glfwGetTimerFrequency();
        lastFrameTime = currentFrameTime;
    }
    public long getCurrentTime(){
        return (long) (glfwGetTime() * GLFW.glfwGetTimerFrequency());
    }

    //Callbacks
    private final GLFWWindowSizeCallback c_windowSize = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int _width, int _height) {
            width = _width;
            height = _height;
        }
    };
    private final GLFWWindowPosCallback c_windowPosition = new GLFWWindowPosCallback() {
        @Override
        public void invoke(long window, int _posX, int _posY) {
            posX = _posX;
            posY = _posY;
        }
    };
    public final GLFWWindowCloseCallback c_windowClose = new GLFWWindowCloseCallback() {
        @Override
        public void invoke(long window) {
            glfwSetWindowShouldClose(windowID, true);
        }
    };

    //Getter
    /** @return the GLFW Window ID.
     *
     * @author snoweuph
     */
    public long getWindowID(){
        return windowID;
    }
    /** @return the current Title of the Window.
     *
     * @author snoweuph
     */
    public String getTitle() {
        return title;
    }
    /** @return the current Width of the Window.
     *
     * @author snoweuph
     */
    public int getWidth() {
        return width;
    }
    /** @return the current Height of the Window.
     *
     * @author snoweuph
     */
    public int getHeight() {
        return height;
    }
    /** @return the current X Position of the Window.
     *
     * @author snoweuph
     */
    public int getPosX() {
        return posX;
    }
    /** @return the current Y Position of the Window.
     *
     * @author snoweuph
     */
    public int getPosY() {
        return posY;
    }
    /** @return the Time needed to draw the last Frame.
     *
     * @author snoweuph
     */
    public float getDelta() {
        return delta;
    }
    /** @return the Time needed to draw the last Frame scaled by a big constant.
     *
     * @author snoweuph
     */
    public float getScaledDelta(){
        return delta*100f;
    }
    /** @return whether the Window should be closed or not.
     *
     * @author snoweuph
     */
    public boolean shouldClose(){
        return glfwWindowShouldClose(windowID);
    }

    //Setter
    /** Set the Title of the Window.
     *
     * @param title the new Window title.
     *
     * @author snoweuph
     */
    public void setTitle(String title) {
        glfwSetWindowTitle(windowID, title);
        this.title = title;
    }
    /** Set the Size of the Window.
     *
     * @param width the new Width.
     * @param height the new Height.
     *
     * @author snoweuph
     */
    public void setSize(int width, int height) {
        glfwSetWindowSize(windowID, width, height);
        //Info: no need to update Variables here, because of the Callback
    }
    /** Set the Position of the Window.
     *
     * @param posX the new X Position.
     * @param posY the new Y Position.
     *
     * @author snoweuph
     */
    public void setPosition(int posX, int posY) {
        glfwSetWindowPos(windowID, posX, posY);
        //Info: no need to update Variables here, because of the Callback
    }
    /** Set the Icon of the Window.
     *
     * @param icon the new Window Icon.
     *
     * @author snoweuph
     */
    public void setIcon(GLFWImage.Buffer icon){
        glfwSetWindowIcon(windowID, icon);
    }

}
