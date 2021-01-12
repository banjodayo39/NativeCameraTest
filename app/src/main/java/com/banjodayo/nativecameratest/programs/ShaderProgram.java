package com.banjodayo.nativecameratest.programs;

import android.content.Context;

import com.banjodayo.nativecameratest.utils.ShaderHelper;
import com.banjodayo.nativecameratest.utils.TextResourcesReader;

import static android.opengl.GLES20.*;

public class ShaderProgram {


    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    // Shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId,
                            int fragmentShaderResourceId) {
// Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
                TextResourcesReader.readTextFileFromResource(
                        context, vertexShaderResourceId),
                TextResourcesReader.readTextFileFromResource(
                        context, fragmentShaderResourceId));
    }
    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
    }


}
