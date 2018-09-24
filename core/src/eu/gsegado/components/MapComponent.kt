package eu.gsegado.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.maps.tiled.TiledMap

class MapComponent(var map: TiledMap = TiledMap()) : Component