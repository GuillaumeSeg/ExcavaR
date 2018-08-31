package eu.gsegado;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {

    private Map<DIRECTION, Animation> walking;
    private Map<DIRECTION, TextureRegion> standing;

    public AnimationSet(Animation walkNorth,
                        Animation walkSouth,
                        Animation walkRight,
                        Animation walkLeft,
                        TextureRegion standingNorth,
                        TextureRegion standingSouth,
                        TextureRegion standingRight,
                        TextureRegion standingLeft) {
        walking = new HashMap<DIRECTION, Animation>();
        standing = new HashMap<DIRECTION, TextureRegion>();
        walking.put(DIRECTION.NORTH, walkNorth);
        walking.put(DIRECTION.SOUTH, walkSouth);
        walking.put(DIRECTION.EAST, walkRight);
        walking.put(DIRECTION.WEST, walkLeft);
        standing.put(DIRECTION.NORTH, standingNorth);
        standing.put(DIRECTION.SOUTH, standingSouth);
        standing.put(DIRECTION.EAST, standingRight);
        standing.put(DIRECTION.WEST, standingLeft);
    }

    public Animation getWalking(DIRECTION dir) {
        return walking.get(dir);
    }

    public TextureRegion getStanding(DIRECTION dir) {
        return standing.get(dir);
    }
}
