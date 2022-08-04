package org.euph.engine.systems.render;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

/** The Definition of a Shader Program.
 * it has at least the Vertex and Fragment Shader Phase, but also supports tesselation and geometry shading.
 *
 * @author snoweuph
 * @version 1.0
 */
public abstract class ShaderProgram {
    private final int programId;
    private final int vertexShaderId;
    /** Null if this Stage doesn't Exist
     */
    private final Integer tesselationControlShaderId;
    /** Null if this Stage doesn't Exist
     */
    private final Integer tesselationEvaluationShaderId;
    /** Null if this Stage doesn't Exist
     */
    private final Integer geometryShaderId;
    public final int fragmentShaderId;
    /** Used for Loading Matrix4x4 to Uniform Locations
     */
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    //Constructor
    /** Creates a new Shader Program with specified Shaders.
     *
     * @param vertexFile can't be null.
     * @param tesselationControlFile can be null.
     * @param tesselationEvaluationFile can be null.
     * @param geometryFile can be null.
     * @param fragmentFile can't be null.
     *
     * @author snoweuph
     */
    public ShaderProgram(String vertexFile, String tesselationControlFile,String tesselationEvaluationFile, String geometryFile, String fragmentFile){
        //load Shaders
        vertexShaderId = loadShader(vertexFile, GL30C.GL_VERTEX_SHADER);
        tesselationControlShaderId = tesselationControlFile != null ? loadShader(tesselationControlFile, GL40C.GL_TESS_CONTROL_SHADER): null;
        tesselationEvaluationShaderId = tesselationEvaluationFile != null ? loadShader(tesselationEvaluationFile, GL40C.GL_TESS_EVALUATION_SHADER): null;
        geometryShaderId = geometryFile != null ? loadShader(geometryFile, GL32C.GL_GEOMETRY_SHADER) : null;
        fragmentShaderId = loadShader(fragmentFile, GL30C.GL_FRAGMENT_SHADER);
        //create Shader Program
        programId = GL30C.glCreateProgram();
        //Attach Shaders to the Shader Program
        GL30C.glAttachShader(programId, vertexShaderId);
        if(tesselationControlShaderId != null) GL30C.glAttachShader(programId, tesselationControlShaderId);
        if(tesselationEvaluationShaderId != null) GL30C.glAttachShader(programId, tesselationEvaluationShaderId);
        if(geometryShaderId != null) GL30C.glAttachShader(programId, geometryShaderId);
        GL30C.glAttachShader(programId, fragmentShaderId);
        //Bind Attributes
        bindAttributes();
        //Link This Shader Program and Validate it
        GL30C.glLinkProgram(programId);
        GL30C.glValidateProgram(programId);
        //Get All Uniform Locations
        getAllUniformLocations();
    }
    /** Creates a new Shader Program with specified Shaders.
     *
     * @param vertexFile can't be null.
     * @param fragmentFile can't be null.
     *
     * @author snoweuph
     */
    public ShaderProgram(String vertexFile, String fragmentFile){
        //Set unused Shaders to null
        tesselationControlShaderId = null;
        tesselationEvaluationShaderId = null;
        geometryShaderId = null;
        //load Shaders
        vertexShaderId = loadShader(vertexFile, GL30C.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFile, GL30C.GL_FRAGMENT_SHADER);
        //create Shader Program
        programId = GL30C.glCreateProgram();
        //Attach Shaders to the Shader Program
        GL30C.glAttachShader(programId, vertexShaderId);
        GL30C.glAttachShader(programId, fragmentShaderId);
        //Bind Attributes
        bindAttributes();
        //Link This Shader Program and Validate it
        GL30C.glLinkProgram(programId);
        GL30C.glValidateProgram(programId);
        //Get All Uniform Locations
        getAllUniformLocations();
    }

    //Starting and Stopping of the Shader Program
    /** Starts the Shader Program.
     *
     * @author snoweuph
     */
    public void start(){
        GL30C.glUseProgram(programId);
    }
    /** Stops the Shader Program.
     *
     * @author snoweuph
     */
    public void stop(){
        GL30C.glUseProgram(0);
    }

    //Getting of Uniform Locations
    /** Gathers all Uniform Locations of all Shaders this Shader PRogram has.
     *
     * @author snoweuph
     */
    protected abstract void getAllUniformLocations();
    /** Gets the location of One Uniform.
     *
     * @param uniformName the Name of the Uniform.
     * @return the int that stores the Location of that Uniform.
     *
     * @author snoweuph
     */
    protected int getUniformLocation(String uniformName){
        return GL30C.glGetUniformLocation(programId, uniformName);
    }

    //Binding of Attributes
    /** Binds all Attributes of this Shader Program.
     *
     * @author snoweuph
     */
    protected abstract void bindAttributes();
    /** Binds One Attribute.
     *
     * @param index the Index of Attributes, where this Attribute is stored.
     * @param attributeName the Name of the Attribute.
     *
     * @author snoweuph
     */
    protected void bindAttribute(int index, String attributeName){
        GL30C.glBindAttribLocation(programId, index, attributeName);
    }

    //Loading of Shader
    /** Loads a Shader from a File.
     *
     * @param file the Path to the Shader File.
     * @param type the Type of Shader this is.
     * @return the ID of the Shader that just got loaded.
     *
     * @author snoweuph
     */
    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }catch (IOException error){
            System.err.println("Couldn't read Shader File!");
            error.printStackTrace();
            System.exit(-1);
        }
        int shaderId = GL30C.glCreateShader(type);
        GL30C.glShaderSource(shaderId, shaderSource);
        GL30C.glCompileShader(shaderId);
        if(GL30C.glGetShaderi(shaderId, GL30C.GL_COMPILE_STATUS) == GL30C.GL_FALSE){
            System.err.println(GL30C.glGetShaderInfoLog(shaderId, 512));
            System.err.println("Couldn't compile Shader!");
            System.exit(-1);
        }
        return shaderId;
    }

    //Functions to load different Types of Uniforms
    /** Loads a Boolean to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadBoolean(int location, boolean value){
        GL30C.glUniform1f(location, value ? 1f : 0f);
    }
    /** Loads an Integer to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadInt(int location, int value){
        GL30C.glUniform1i(location, value);
    }
    /** Loads a Float to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadFloat(int location, float value){
        GL30C.glUniform1f(location, value);
    }
    /** Loads a {@link Vector2f} to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadVector2(int location, Vector2f value){
        GL30C.glUniform2f(location, value.x, value.y);
    }
    /** Loads a {@link Vector3f} to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadVector3(int location, Vector3f value){
        GL30C.glUniform3f(location, value.x , value.y, value.z);
    }
    /** Loads a {@link Matrix4f 4x4 Matrix} to its Uniform Location.
     *
     * @param location the location to load to.
     * @param value the value to load.
     *
     * @author snoweuph
     */
    protected void loadMatrix4(int location, Matrix4f value){
        value.get(matrixBuffer);
        GL30C.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    //Clean Up
    /** Stops the Shader Program, Unbinds and Deletes its Shaders and Deletes the Program itself at the End.
     *
     * @author snoweuph
     */
    public void cleanUp(){
        //Stop Shader Program
        stop();
        //Detach all Shaders
        GL30C.glDetachShader(programId, vertexShaderId);
        if(tesselationControlShaderId != null) GL30C.glDetachShader(programId, tesselationControlShaderId);
        if(tesselationEvaluationShaderId != null) GL30C.glDetachShader(programId, tesselationEvaluationShaderId);
        if(geometryShaderId != null) GL30C.glDetachShader(programId, geometryShaderId);
        GL30C.glDetachShader(programId, fragmentShaderId);
        //Delete All Shaders
        GL30C.glDeleteShader(vertexShaderId);
        if(tesselationControlShaderId != null) GL30C.glDeleteShader(tesselationControlShaderId);
        if(tesselationEvaluationShaderId != null) GL30C.glDeleteShader(tesselationEvaluationShaderId);
        if(geometryShaderId != null) GL30C.glDeleteShader(geometryShaderId);
        GL30C.glDeleteShader(fragmentShaderId);
        //Delete Shader Program
        GL30C.glDeleteProgram(programId);
    }
}
