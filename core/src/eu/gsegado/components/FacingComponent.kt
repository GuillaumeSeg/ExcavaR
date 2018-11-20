package eu.gsegado.components

import com.badlogic.ashley.core.Component
import eu.gsegado.DIRECTION

class FacingComponent(var facing: DIRECTION = DIRECTION.SOUTH) : Component