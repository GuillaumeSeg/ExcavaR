package eu.gsegado;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class TiledMapFactory extends ApplicationAdapter implements InputProcessor {
    private OrthographicCamera camera;
    private TiledMapRendererWithSprites renderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture tiles;
    private TiledMap map;
    private Player skeleton;

    private TiledMapTileSet tilesetGrass;

    private float viewportWidth;
    private float viewportHeight;

// Init --
    @Override
    public void create() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        viewportWidth = (w*8*16)/h;
        viewportHeight = 8*16;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        Gdx.input.setInputProcessor(this);

        font = new BitmapFont();
        batch = new SpriteBatch();

        skeleton = new Player(0,1);  //y : (viewportHeight/2)+4 (centered)
        skeleton.setSpriteBatch(batch);

        {
            tiles = new Texture("forrestup.png");
            TextureRegion[][] splitTiles = TextureRegion.split(tiles, 16, 16);
            map = new TiledMap();

            // Init tileset grass
            tilesetGrass = new TiledMapTileSet();
            tilesetGrass.putTile(0, new StaticTiledMapTile(splitTiles[10][0]));
            tilesetGrass.putTile(1, new StaticTiledMapTile(splitTiles[9][0]));
            tilesetGrass.putTile(2, new StaticTiledMapTile(splitTiles[9][1]));
            tilesetGrass.putTile(3, new StaticTiledMapTile(splitTiles[9][2]));

            MapLayers layers = map.getLayers();
            for (int l = 0; l < 3; l++) {
                TiledMapTileLayer layer = new TiledMapTileLayer(100, 100, 16, 16);
                for (int x = 0; x < 100; x++) {
                    for (int y = 0; y < 100; y++) {
                        Cell cell = new Cell();
                        cell.setTile(tilesetGrass.getTile(MathUtils.random(0,3)));
                        layer.setCell(x, y, cell);
                    }
                }
                layers.add(layer);
            }
        }
        renderer = new TiledMapRendererWithSprites(map);
        //renderer.addSprite(sprite);
    }

//
//    MapLayers layers = map.getLayers();
//            for (int l = 0; l < 1; l++) {
//        TiledMapTileLayer layer = new TiledMapTileLayer(100, 100, 16, 16);
//        for (int x = 0; x < 100; x++) {
//            for (int y = 0; y < 100; y++) {
//                int ty = (int)(Math.random() * splitTiles.length);
//                int tx = (int)(Math.random() * splitTiles[ty].length);
//                Cell cell = new Cell();
//                cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
//                layer.setCell(x, y, cell);
//            }
//        }
//        layers.add(layer);
//    }


// Render --
    @Override
    public void render() {
        // test orangé 154 / 118 / 118
        // test vert 154 / 180 / 128
        // vert2 :  152f / 255f, 212f / 255f, 152f / 255f, 1f)
        // test ++ 153 / 135 / 119

        Gdx.gl.glClearColor(153f / 255f, 135f / 255f, 119f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        skeleton.update(Gdx.graphics.getDeltaTime());
        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        skeleton.render();
        batch.end();
    }

// Events --
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 touchViewport = camera.unproject(clickCoordinates);
        float deltaX = touchViewport.x - skeleton.getWorldX();
        float deltaY = touchViewport.y - skeleton.getWorldY();
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            if (deltaX >= 0) {
                skeleton.move(DIRECTION.EAST);
            } else {
                skeleton.move(DIRECTION.WEST);
            }
        } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
            if (deltaY >= 0) {
                skeleton.move(DIRECTION.NORTH);
            } else {
                skeleton.move(DIRECTION.SOUTH);
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        //Vector3 position = camera.unproject(clickCoordinates);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose () {
        batch.dispose();
        tiles.dispose();
        skeleton.dispose();
    }
}
