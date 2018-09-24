package eu.gsegado;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import eu.gsegado.components.MapComponent;
import eu.gsegado.models.Level;
import eu.gsegado.systems.MapRenderSystem;

public class GameAppli extends Game {
    ComponentMapper<MapComponent> mm = ComponentMapper.getFor(MapComponent.class);
    private PooledEngine gameEngine = null;

	private OrthographicCamera camera;
    private float viewportWidth;
    private float viewportHeight;
	
	@Override
	public void create () {
	    // Game Engine
        gameEngine = new PooledEngine();

        // Viewport
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        viewportWidth = (w*8*16)/h;
        viewportHeight = 8*16;

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        // - Level - Map
        Level level = new Level(gameEngine);
        level.create();

        MapComponent mapComponent = mm.get(level.getEntity());
        gameEngine.addSystem(new MapRenderSystem(new OrthogonalTiledMapRenderer(
                                                        mapComponent.getMap()),
                                                        camera));
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(153f / 255f, 135f / 255f, 119f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameEngine.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {

	}
}
