package be.particulitis.hourglass.common.drawing

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompBloomer
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Dim
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.roundToInt

private val TextureRegion.hw: Float
    get() {
        return regionWidth / 2f
    }
private val TextureRegion.hh: Float
    get() {
        return regionHeight / 2f
    }
private val TextureRegion.w: Float
    get() {
        return regionWidth.toFloat()
    }
private val TextureRegion.h: Float
    get() {
        return regionHeight.toFloat()
    }

class GGraphics : SpriteBatch(8191) {

    fun draw(region: TextureRegion, space: CompSpace, dim: Dim, angle: Float) {
        draw(region, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat(), dim.hw, dim.hh, dim.w, dim.h, 1f, 1f, angle)
    }

    fun draw(region: TextureRegion, space: CompSpace, angle: Float) {
        draw(region, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat(), space.hw, space.hh, space.w, space.h, 1f, 1f, angle)
    }

    fun draw(tr: TextureRegion, space: CompSpace) {
        draw(tr, space.x, space.y)
    }


    fun drawCenteredOnBox(draw: CompDraw, space: CompSpace, textureRegion: TextureRegion) {
        draw(textureRegion,
                (space.centerX - textureRegion.hw).roundToInt().toFloat(), (space.centerY - textureRegion.hh).roundToInt().toFloat(),
                textureRegion.hw, textureRegion.hh,
                textureRegion.w, textureRegion.h,
                1f, 1f, draw.angle)
    }
    fun drawCenteredOnBoxSpaceStreched(draw: CompDraw, space: CompSpace, textureRegion: TextureRegion) {
        draw(textureRegion,
                space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat(),
                space.hw, space.hh,
                space.w, space.h,
                1f, 1f, draw.angle)
    }

    fun drawFrontStreched(bloomer: CompBloomer, space: CompSpace) {
        draw(bloomer.tr,
                space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat(),
                space.hw, space.hh,
                space.w, space.h,
                1f, 1f, bloomer.angle)
    }

    fun drawFront(bloomer: CompBloomer, space: CompSpace) {
        draw(bloomer.tr,
                (space.centerX - bloomer.tr.hw).roundToInt().toFloat(), (space.centerY - bloomer.tr.hh).roundToInt().toFloat(),
                bloomer.tr.hw, bloomer.tr.hh,
                bloomer.tr.w, bloomer.tr.h,
                1f, 1f, bloomer.angle)
    }
    fun drawOccCenteredOnBox(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBox(draw, space, draw.currentImg.occluder)
    }
    fun drawFrontCenteredOnBox(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBox(draw, space, draw.currentImg.front)
    }
    fun drawNormalCenteredOnBox(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBox(draw, space, draw.currentImg.normal)
    }
    fun drawOccCenteredOnBoxSpaceStreched(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBoxSpaceStreched(draw, space, draw.currentImg.occluder)
    }
    fun drawFrontCenteredOnBoxSpaceStreched(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBoxSpaceStreched(draw, space, draw.currentImg.front)
    }
    fun drawNormalCenteredOnBoxSpaceStreched(draw: CompDraw, space: CompSpace) {
        drawCenteredOnBoxSpaceStreched(draw, space, draw.currentImg.normal)
    }

    companion object {

        fun render(function: () -> Unit) {
            Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            cam.update()
            batch.projectionMatrix = cam.combined
            batch.begin()
            function.invoke()
            if (batch.isDrawing)
                batch.end()
        }

        fun nor(s: String): TextureRegion {
            return imgMan.nor(s)
        }
        fun tr(s: String): TextureRegion {
            return imgMan.tr(s)
        }
        fun occ(s: String): TextureRegion {
            return imgMan.occ(s)
        }
        fun img(s: String): GImage {
            return GImage(imgMan.tr(s), imgMan.nor(s), imgMan.occ(s))
        }

        fun anim(shoot: String, timePerFrame: Float): GAnim {
            return GAnim(shoot, timePerFrame)
        }

        val batch: GGraphics = GGraphics()
        val imgMan = ImgMan()
        val cam = OrthographicCamera(GResolution.areaW, GResolution.areaH)

    }

}