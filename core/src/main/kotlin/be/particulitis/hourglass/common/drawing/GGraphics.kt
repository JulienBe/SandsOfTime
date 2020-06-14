package be.particulitis.hourglass.common.drawing

import be.particulitis.hourglass.AssMan
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.comp.draw.CompBloomer
import be.particulitis.hourglass.comp.draw.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Dim
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.roundToInt

internal val TextureRegion.hw: Float
    get() {
        return regionWidth / 2f
    }
internal val TextureRegion.hh: Float
    get() {
        return regionHeight / 2f
    }
internal val TextureRegion.w: Float
    get() {
        return regionWidth.toFloat()
    }
internal val TextureRegion.h: Float
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
    fun draw(region: TextureRegion, x: Float, y: Float, angle: Float) {
        draw(region, x.roundToInt().toFloat(), y.roundToInt().toFloat(), region.hw, region.hh, region.w, region.h, 1f, 1f, angle)
    }
    fun draw(region: TextureRegion, x: Float, y: Float, w: Float, h: Float, angle: Float) {
        draw(region, x.roundToInt().toFloat(), y.roundToInt().toFloat(), w / 2f, h / 2f, w, h, 1f, 1f, angle)
    }
    fun drawRandomCenter(region: TextureRegion, x: Float, y: Float, w: Float, h: Float, angle: Float) {
        draw(region, x.roundToInt().toFloat(), y.roundToInt().toFloat(), GRand.float(0f, w), GRand.float(0f, w), w, h, 1f, 1f, angle)
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

    fun drawStreched(tr: TextureRegion, space: CompSpace) {
        batch.draw(tr, space.x, space.y, space.w, space.h)
    }

    companion object {

        fun render(function: () -> Unit) {
            Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            cam.update()
            batch.projectionMatrix = cam.combined
            batch.begin()
            function.invoke()
            if (batch.isDrawing)
                batch.end()
        }

        fun nor(s: String): TextureRegion {
            return assMan.nor(s)
        }
        fun tr(s: String): TextureRegion {
            return assMan.tr(s)
        }
        fun occ(s: String): TextureRegion {
            return assMan.occ(s)
        }
        fun img(s: String): GImage {
            return GImage(assMan.tr(s), assMan.nor(s), assMan.occ(s))
        }

        val batch: GGraphics = GGraphics()
        val assMan = AssMan()
        val cam = OrthographicCamera(GResolution.areaW, GResolution.areaH)

        val black = GImage("squares/square_black")
        val blue = GImage("squares/square_blue")
        val brown = GImage("squares/square_brown")
        val dark_blue = GImage("squares/square_dark_blue")
        val dark_green = GImage("squares/square_dark_green")
        val dark_grey = GImage("squares/square_dark_grey")
        val dark_purple = GImage("squares/square_dark_purple")
        val green = GImage("squares/square_green")
        val light_grey = GImage("squares/square_light_grey")
        val orange = GImage("squares/square_orange")
        val pink = GImage("squares/square_pink")
        val pink_skin = GImage("squares/square_pink_skin")
        val red = GImage("squares/square_red")
        val white = GImage("squares/square_white")
        val yellow = GImage("squares/square_yellow")

    }

}