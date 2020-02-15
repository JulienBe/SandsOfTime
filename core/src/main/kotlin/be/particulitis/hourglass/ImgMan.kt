package be.particulitis.hourglass

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.load
import ktx.collections.GdxMap

class ImgMan {

    val palettes = arrayListOf(
            getTexture("img/palette_improved"),
            getTexture("img/palette"),
            getTexture("img/palette_c64"),
            getTexture("img/palette_gbc"),
            getTexture("img/palette_gameboy"),
            getTexture("img/palette_pico8")
    )
    private val walls: Array<TextureRegion>
    private val atlas: TextureAtlas
    val regions = GdxMap<String, TextureRegion>()

    private fun getTexture(path: String): Texture {
        manager.load<Texture>("$path.png")
        manager.finishLoading()
        val t = manager.get<Texture>("$path.png")
        t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        return t
    }

    fun tr(name: String): TextureRegion {
        return regions[name]
    }
    fun nor(name: String): TextureRegion {
        return regions[name + "_normal"]
    }

    fun occ(name: String): TextureRegion {
        return regions[name + "_occluder"]
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
        const val atlasPath = "textures/texture1/atlas.atlas"
        const val wall = "wall1"
        const val player = "gunner_idle"
        const val animPlayerShootBoth = "gunner_shoot"
        const val animPlayerShootLeft = "gunner_shoot_left"
        const val animPlayerShootRight = "gunner_shoot_right"
        val manager = AssetManager()
    }
}