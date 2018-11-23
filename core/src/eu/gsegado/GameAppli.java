package eu.gsegado;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import eu.gsegado.components.FacingComponent;
import eu.gsegado.components.MapComponent;
import eu.gsegado.components.PositionComponent;
import eu.gsegado.components.SpriteComponent;
import eu.gsegado.components.TextureComponent;
import eu.gsegado.handlers.RawInputHandler;
import eu.gsegado.models.Level;
import eu.gsegado.systems.InputHandlerSystem;
import eu.gsegado.systems.MapRenderSystem;
import eu.gsegado.systems.ObjectRenderSystem;
import eu.gsegado.systems.PlayerRenderSystem;

public class GameAppli extends Game {
    private final static int FRAME_COLS = 4;
    private final static int FRAME_ROWS = 1;

    ComponentMapper<MapComponent> mm = ComponentMapper.getFor(MapComponent.class);
    private PooledEngine gameEngine = null;

    private SpriteBatch batch;
    private BitmapFont font;
	private OrthographicCamera camera;
    private float viewportWidth;
    private float viewportHeight;
    private int width;
    private int height;

    private Texture playerDownSheet;
    private Texture playerUpSheet;
    private Texture playerSideSheet;

    private Level level;
	
	@Override
	public void create () {
	    // Game Engine
        gameEngine = new PooledEngine();

        // Viewport
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        viewportWidth = (w*8*16)/h;
        viewportHeight = 8*16;
        width = (int)viewportWidth/16;
        height = 8;

        // Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        // - Level - Map
        level = new Level(gameEngine);
        level.create();

        // - Exit -
        Texture tiles = new Texture("forrestup.png"); // TODO - Refactor that + Clean
        TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
        Entity exit = new Entity();
        Vector2 exitPos = new Vector2(MathUtils.random(0,width), MathUtils.random(0,height));
        exit.add(new PositionComponent(exitPos, new Vector2(exitPos.x*16.0f, exitPos.y*16.0f)));
        exit.add(new TextureComponent(splitTiles[8][0]));
        gameEngine.addEntity(exit);

        // - Player -
        Entity skeleton = new Entity();
        skeleton.add(new PositionComponent(new Vector2(0.0f, 0.0f),
                                            new Vector2(0.0f, 4.0f)));
        skeleton.add(new FacingComponent());

        playerDownSheet  = new Texture("bones_down.png");
        playerUpSheet    = new Texture("bones_up.png");
        playerSideSheet = new Texture("bones_side.png");
        TextureRegion[][] downFrames = TextureRegion.split(playerDownSheet,
                playerDownSheet.getWidth() / FRAME_COLS,
                playerDownSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] upFrames = TextureRegion.split(playerUpSheet,
                playerUpSheet.getWidth() / FRAME_COLS,
                playerUpSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] rightFrames = TextureRegion.split(playerSideSheet,
                playerSideSheet.getWidth() / FRAME_COLS,
                playerSideSheet.getHeight() / FRAME_ROWS);
        for (int i=0; i<4; i++) {
            rightFrames[0][i].flip(true, false);
        }
        TextureRegion[][] leftFrames = TextureRegion.split(playerSideSheet,
                playerSideSheet.getWidth() / FRAME_COLS,
                playerSideSheet.getHeight() / FRAME_ROWS);
        skeleton.add(new SpriteComponent(
                    downFrames[0][1],
                    upFrames[0][1],
                    rightFrames[0][1],
                    leftFrames[0][1]));
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
        font = new BitmapFont();
        gameEngine.addSystem(new ObjectRenderSystem(batch));
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

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
		gameEngine.update(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
	    playerDownSheet.dispose();
	    playerSideSheet.dispose();
	    playerUpSheet.dispose();
        level.dispose();
        level = null;
        batch.dispose();
	}
}
