package eu.gsegado.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SpriteComponent(var down: TextureRegion? = null,
                      var up: TextureRegion? = null) : Component {
    val sprite: Sprite? = null
}