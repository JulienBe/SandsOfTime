package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GBatch
import com.badlogic.gdx.graphics.Color

class CompDraw : Comp() {
    fun draw(space: CompSpace, batch: GBatch) {
        batch.setColor(1f, 0f, 0f, 1f)
        batch.draw(space)
        batch.color = Color.WHITE
    }
}