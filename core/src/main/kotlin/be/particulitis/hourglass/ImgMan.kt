package be.particulitis.hourglass

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.load
import ktx.collections.GdxMap

class ImgMan {

    val palette: Texture = getTexture(palettePath)
    val square: Texture = getTexture(squarePath)
    val cube: Texture = getTexture(cubePath)
    val walls: Array<TextureRegion>
    val atlas: TextureAtlas
    val regions = GdxMap<String, TextureRegion>()

    private fun getTexture(path: String): Texture {
        manager.load<Texture>(path)
        manager.finishLoading()
        val t = manager.get<Texture>(path)
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        return t
    }

    init {
        manager.load<TextureAtlas>(atlasPath)
        manager.finishLoading()
        atlas = manager.get<TextureAtlas>(atlasPath)
        atlas.regions.forEach {
            regions.put(it.name, it)
        }
        val tmpWalls = mutableListOf<TextureRegion>()
        for (i in 1..2)
            tmpWalls.add(atlas.findRegion("wall$i"))
        walls = tmpWalls.toTypedArray()
    }

    companion object {
        const val squarePath = "img/square.png"
        const val cubePath = "img/square3d.png"
        const val palettePath = "img/palette.png"
        const val atlasPath = "textures/texture1/atlas.atlas"
        val manager = AssetManager()
    }
}