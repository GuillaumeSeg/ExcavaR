package eu.gsegado.models;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;

import eu.gsegado.components.MapComponent;

public class Level {
    private PooledEngine engine;

    Entity level;

    public Level(PooledEngine engine) {
        this.engine = engine;
    }

    public Entity getEntity() {
        return level;
    }

    public void create() {
        Texture tiles = new Texture("forrestup.png");
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);

        level = new Entity();
        TiledMap map = new TiledMap();
        // Init tileset grass
        TiledMapTileSet tilesetGrass = new TiledMapTileSet();
        tilesetGrass.putTile(0, new StaticTiledMapTile(splitTiles[10][0]));
        tilesetGrass.putTile(1, new StaticTiledMapTile(splitTiles[9][0]));
        tilesetGrass.putTile(2, new StaticTiledMapTile(splitTiles[9][1]));
        tilesetGrass.putTile(3, new StaticTiledMapTile(splitTiles[9][2]));

        MapLayers layers = map.getLayers();
        for (int l = 0; l < 3; l++) {
            TiledMapTileLayer layer = new TiledMapTileLayer(100, 100, 16, 16);
            for (int x = 0; x < 100; x++) {
                for (int y = 0; y < 100; y++) {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(tilesetGrass.getTile(MathUtils.random(0,3)));
                    layer.setCell(x, y, cell);
                }
            }
            layers.add(layer);
        }

        level.add(new MapComponent(map));
        engine.addEntity(level);
    }
}
