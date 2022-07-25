package org.euph.engine.windowSystem;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private final long windowID;
    private String title;
    private int width;
    private int height;
    private int posX;
    private int posY;
    private long lastFrameTime;
    private float delta;

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

    public void bind(){
        glfwMakeContextCurrent(windowID);
        GL.createCapabilities();
    }
    public void show(boolean enabled){
        if(enabled){
            glfwShowWindow(windowID);
            return;
        }
        glfwHideWindow(windowID);
    }
    public void makePrimary(){
        WindowManager.setPrimary(this);
    }
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
    public void destroy(){
        //Unbind all Callbacks before Destroying the Window
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);
        WindowManager.removeWindow(this);
    }
    public void update(){
        glfwSwapBuffers(windowID);
        //Update the delta Time (the time that was needed to render the last frame)
        updateDeltaTime();
    }
    private void updateDeltaTime(){
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
    }
    private long getCurrentTime(){
        return (long) (glfwGetTime() * 1000L);
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
    public long getWindowID(){
        return windowID;
    }
    public String getTitle() {
        return title;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public float getDelta() {
        return delta;
    }
    public float getScaledDelta(){
        return delta*100f;
    }
    public boolean shouldClose(){
        return glfwWindowShouldClose(windowID);
    }

    //Setter
    public void setTitle(String title) {
        glfwSetWindowTitle(windowID, title);
        this.title = title;
    }
    public void setSize(int width, int height) {
        glfwSetWindowSize(windowID, width, height);
        //Info: no need to update Variables here, because of the Callback
    }
    public void setPosition(int posX, int posY) {
        glfwSetWindowPos(windowID, posX, posY);
        //Info: no need to update Variables here, because of the Callback
    }
    public void setIcon(GLFWImage.Buffer icon){
        glfwSetWindowIcon(windowID, icon);
    }

}
