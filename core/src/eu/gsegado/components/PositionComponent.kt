package eu.gsegado.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class PositionComponent(var p: Vector2 = Vector2(0.0f, 0.0f),
                        var world: Vector2 = Vector2(0.0f, 0.0f)) : Component