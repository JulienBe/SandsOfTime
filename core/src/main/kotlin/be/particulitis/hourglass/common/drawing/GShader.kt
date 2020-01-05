package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.graphics.glutils.ShaderProgram

object GShader {
    fun createShader(vert: String, frag: String): ShaderProgram {
        ShaderProgram.pedantic = false
        val prog = ShaderProgram(Gdx.files.internal(vert).readString(), Gdx.files.internal(frag).readString())
        if (!prog.isCompiled)
            throw GdxRuntimeException("could not compile shader: " + prog.log)
        if (prog.log.isNotEmpty())
            Gdx.app.log("SHADER", prog.log)
        return prog
    }
}