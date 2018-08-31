package eu.gsegado;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class Player {
    private final static int FRAME_COLS = 4;
    private final static int FRAME_ROWS = 1;
    private final Texture playerUpSheet;
    private final Texture playerDownSheet;
    private final Texture playerRightSheet;
    private final Texture playerLeftSheet;
    private TextureRegion[] character = new TextureRegion[2];
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> rightAnimation;
    private Animation<TextureRegion> leftAnimation;
    private int px;
    private int py;
    private float worldX;
    private float worldY;
    private DIRECTION facing;

    private AnimationSet animations;

    private float srcX, srcY;
    private float destX, destY;
    private SpriteBatch batch;
    private float stateTime;
    private static float ANIM_TIME = 0.80f;
    private static float WALKING_TIME = 0.75f;

    private float walkTimer;
    private boolean moveRequestThisFrame;

    private PLAYER_STATE state;

    protected Player(int x, int y) {
        playerUpSheet   = new Texture("bones_up.png");
        playerDownSheet = new Texture("bones_down.png");
        playerRightSheet = new Texture("bones_side.png");
        playerLeftSheet = new Texture("bones_side.png");

        TextureRegion[][] downFrames = TextureRegion.split(playerDownSheet,
                playerDownSheet.getWidth() / FRAME_COLS,
                playerDownSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] upFrames = TextureRegion.split(playerUpSheet,
                playerUpSheet.getWidth() / FRAME_COLS,
                playerUpSheet.getHeight() / FRAME_ROWS);
        TextureRegion[][] rightFrames = TextureRegion.split(playerRightSheet,
                playerRightSheet.getWidth() / FRAME_COLS,
                playerRightSheet.getHeight() / FRAME_ROWS);
        for (int i=0; i<4; i++) {
            rightFrames[0][i].flip(true, false);
        }
        TextureRegion[][] leftFrames = TextureRegion.split(playerLeftSheet,
                playerLeftSheet.getWidth() / FRAME_COLS,
                playerLeftSheet.getHeight() / FRAME_ROWS);


        downAnimation = new Animation<TextureRegion>(WALKING_TIME/4.f, downFrames[0]);
        upAnimation = new Animation<TextureRegion>(WALKING_TIME/4.f, upFrames[0]);
        rightAnimation = new Animation<TextureRegion>(WALKING_TIME/4.f, rightFrames[0]);
        leftAnimation = new Animation<TextureRegion>(WALKING_TIME/4.f, leftFrames[0]);

        animations = new AnimationSet(
                upAnimation,
                downAnimation,
                rightAnimation,
                leftAnimation,
                upFrames[0][1],
                downFrames[0][1],
                rightFrames[0][1],
                leftFrames[0][1]
        );

        px = x;
        py = y;
        this.worldX = 16*x;
        this.worldY = 16*y + 4;
        this.state = PLAYER_STATE.IDLE;
        this.facing = DIRECTION.SOUTH;

        stateTime = 0.0f;
    }

    public enum PLAYER_STATE {
        WALKING,
        IDLE,
        ;
    }

    public float getX() {
        return px;
    }

    public float getY() {
        return py;
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void update(float delta) {
        if (state == PLAYER_STATE.WALKING) {
            stateTime += delta;
            walkTimer += delta;
            worldX = Interpolation.bounce.apply(srcX, destX, stateTime / ANIM_TIME)*16;
            worldY = Interpolation.bounce.apply(srcY, destY, stateTime / ANIM_TIME)*16 + 4;
            // fade ++
            // bounce ++
            float deltaX = destX - srcX;
            float deltaY = destY - srcY;
            /*if (Math.abs(deltaX) < Math.abs(deltaY)) {
                if (deltaY >= 0) {
                    reg = upAnimation.getKeyFrame(stateTime);
                } else {

                }
            }*/
            if (stateTime > ANIM_TIME) {
                float leftOverTime = stateTime - ANIM_TIME;
                walkTimer -= leftOverTime;
                finishMove();
                if (moveRequestThisFrame) {
                    move(facing);
                } else {
                    walkTimer = 0.f;
                }
            }
        }
        moveRequestThisFrame = false;
    }

    private void initializeMove(int oldX, int oldY, DIRECTION dir) {
        this.facing = dir;
        this.srcX = oldX;
        this.srcY = oldY;
        this.destX = oldX + dir.getDx();
        this.destY = oldY + dir.getDy();
        stateTime = 0.0f;
        state = PLAYER_STATE.WALKING;
    }

    private void finishMove() {
        state = PLAYER_STATE.IDLE;
        this.worldX = destX*16;
        this.worldY = destY*16 + 4;
        this.srcX = 0;
        this.srcY = 0;
        this.destX = 0;
        this.destY = 0;

    }

    public boolean move(DIRECTION dir) {
        if (state == PLAYER_STATE.WALKING) {
            if (facing == dir) {
                moveRequestThisFrame = true;
            }
            return false;
        }
        initializeMove(px, py, dir);

        float deltaX = destX - srcX;
        float deltaY = destY - srcY;
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            if (deltaX >= 0) {
                //reg = character[0];
            } else {
                //reg = character[1];
            }
        } else if (Math.abs(deltaX) < Math.abs(deltaY)) {
            if (deltaY >= 0) {
                //reg = upAnimation.getKeyFrame(0.0f);
            }
        }

        px += dir.getDx();
        py += dir.getDy();

        worldX += 16.f*dir.getDx();
        worldY += 16.f*dir.getDy();
        return true;
    }

    public void render() {
        //stateTime += Gdx.graphics.getDeltaTime();
        batch.draw(this.getSprite(), worldX, worldY);
    }

    public TextureRegion getSprite() {
        if (state == PLAYER_STATE.WALKING) {
            //Gdx.app.log("WTF", "frame = "+animations.getWalking(facing).getKeyFrameIndex(walkTimer));
            //Gdx.app.log("WTF", "timer = "+walkTimer);
            return (TextureRegion)animations.getWalking(facing).getKeyFrame(walkTimer);
        } else if (state == PLAYER_STATE.IDLE) {
            return animations.getStanding(facing);
        }
        return animations.getStanding(DIRECTION.SOUTH);
    }

    public void dispose(){
        playerUpSheet.dispose();
        playerDownSheet.dispose();
        playerRightSheet.dispose();
        playerLeftSheet.dispose();
    }
}
