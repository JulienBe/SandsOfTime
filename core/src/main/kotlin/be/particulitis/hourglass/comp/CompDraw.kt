package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GBatch
import com.artemis.Component
import com.badlogic.gdx.graphics.Color

class CompDraw : Component() {
    fun draw(dimension: CompDimension, batch: GBatch) {
        batch.setColor(1f, 0f, 0f, 1f)
        batch.draw(dimension)
        batch.color = Color.WHITE
    }
}