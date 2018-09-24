package eu.gsegado.models

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Exit(private var px: Int = 0,
           private var py: Int = 0,
           private val tile: TextureRegion? = null) {

    private var worldX: Float = 0.0f
    private var worldY: Float = 0.0f

    private var batch: SpriteBatch? = null

    fun setBatch(batch: SpriteBatch) {
        this.batch = batch
    }

    fun render() {
        batch?.draw(tile, worldX, worldY)
    }
}