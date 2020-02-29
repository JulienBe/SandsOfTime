package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.graphics.g2d.TextureRegion

class GImage(val front: TextureRegion, val normal: TextureRegion, val occluder: TextureRegion) {
    constructor(name: String) : this(GGraphics.tr(name), GGraphics.nor(name), GGraphics.occ(name))
}