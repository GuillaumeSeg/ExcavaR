package eu.gsegado.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;

import eu.gsegado.components.MapComponent;

public class MapRenderSystem extends IteratingSystem {
    private ComponentMapper<MapComponent> mm = ComponentMapper.getFor(MapComponent.class);

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    public MapRenderSystem(OrthogonalTiledMapRenderer renderer, OrthographicCamera camera) {
        super(Family.all(MapComponent.class).get());
        this.renderer = renderer;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapComponent mapComponent = mm.get(entity);

        camera.update();
        renderer.setView(camera);

        AnimatedTiledMapTile.updateAnimationBaseTime();
        renderer.getBatch().begin();
        for (MapLayer layer : mapComponent.getMap().getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderer.renderTileLayer((TiledMapTileLayer)layer);
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderer.renderObject(object);
                    }
                }
            }
        }
        renderer.getBatch().end();
    }
}
