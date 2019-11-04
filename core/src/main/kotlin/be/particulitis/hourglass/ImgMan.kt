package be.particulitis.hourglass

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.assets.load

class ImgMan {

    val square: Texture
    val cube: Texture

    init {
        manager.load<Texture>(squarePath)
        manager.load<Texture>(cubePath)
        manager.finishLoading()
        square = manager.get<Texture>(squarePath)
        square.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        cube = manager.get<Texture>(cubePath)
        cube.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
    }

    companion object {
        const val squarePath = "img/square.png"
        const val cubePath = "img/square3d.png"
        val manager = AssetManager()
    }
}