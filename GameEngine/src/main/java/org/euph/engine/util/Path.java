package org.euph.engine.util;

/** This Class holds all the different Paths that will be needed by the engine,
 * this ensures that the place where files are located can be easily changed by
 * the user of the Engine to change the structure least a little to their liking
 * or to lookup where what files where located again.
 *
 * @author snoweuph
 * @version 1.0
 */
public class Path {
    public static final String ROOT = "/";
    public static final String RESOURCES = ROOT + "res/";
    public static final String SCENES = RESOURCES + "scenes/";
    public static final String SHADERS = RESOURCES + "shaders/";
    //Default Paths
    public static final String DEFAULT_RESOURCES = RESOURCES + "default/";
    public static final String DEFAULT_SHADERS = DEFAULT_RESOURCES + "shaders/";

}
