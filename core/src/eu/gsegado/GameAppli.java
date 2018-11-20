package eu.gsegado;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import eu.gsegado.components.FacingComponent;
import eu.gsegado.components.MapComponent;
import eu.gsegado.components.PositionComponent;
import eu.gsegado.components.SpriteComponent;
import eu.gsegado.handlers.RawInputHandler;
import eu.gsegado.models.Level;
import eu.gsegado.systems.InputHandlerSystem;
import eu.gsegado.systems.MapRenderSystem;
import eu.gsegado.systems.PlayerRenderSystem;

public class GameAppli extends Game {
    private final static int FRAME_COLS = 4;
    private final static int FRAME_ROWS = 1;

    ComponentMapper<MapComponent> mm = ComponentMapper.getFor(MapComponent.class);
    private PooledEngine gameEngine = null;

    private SpriteBatch batch;
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

        // - Player -
        Entity skeleton = new Entity();
        skeleton.add(new PositionComponent(new Vector2(0.0f, 0.0f),
                                            new Vector2(0.0f, 4.0f)));
        skeleton.add(new FacingComponent());

        Texture playerDownSheet = new Texture("bones_down.png");
        Texture playerUpSheet   = new Texture("bones_up.png");
        TextureRegion[][] downFrames = TextureRegion.split(playerDownSheet,
                playerDownSheet.getWidth() / FRAME_COLS,
                playerDownSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] upFrames = TextureRegion.split(playerUpSheet,
                playerUpSheet.getWidth() / FRAME_COLS,
                playerUpSheet.getHeight() / FRAME_ROWS);
        skeleton.add(new SpriteComponent(
                    downFrames[0][1],
                    upFrames[0][1]));
        gameEngine.addEntity(skeleton);

        // - Commands
        RawInputHandler inputHandler = new RawInputHandler();
        Gdx.input.setInputProcessor(inputHandler);
        InputHandlerSystem inputHandlerSystem = new InputHandlerSystem(camera, skeleton);
        inputHandler.registerListener(inputHandlerSystem);

        MapComponent mapComponent = mm.get(level.getEntity());
        gameEngine.addSystem(new MapRenderSystem(new OrthogonalTiledMapRenderer(
                                                        mapComponent.getMap()),
                                                        camera));
        batch = new SpriteBatch();
        gameEngine.addSystem(new PlayerRenderSystem(batch));

        gameEngine.addSystem(inputHandlerSystem);
	}

	@Override
	public void render () {
        // test orangé 154 / 118 / 118
        // test vert 154 / 180 / 128
        // vert2 :  152f / 255f, 212f / 255f, 152f / 255f, 1f)
        // test ++ 153 / 135 / 119
        Gdx.gl.glClearColor(153f / 255f, 135f / 255f, 119f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
		gameEngine.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
        batch.dispose();
	}
}
