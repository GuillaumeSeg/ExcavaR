package eu.gsegado.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.jetbrains.annotations.NotNull;

import eu.gsegado.DIRECTION;
import eu.gsegado.components.FacingComponent;
import eu.gsegado.components.PositionComponent;
import eu.gsegado.handlers.InputHandlerIF;

public class InputHandlerSystem extends EntitySystem implements InputHandlerIF {
    // TODO - ComponentMapper here !

    private OrthographicCamera camera;
    private Entity skeleton;

    public InputHandlerSystem(OrthographicCamera camera, Entity skeleton) {
        super();
        this.camera = camera;
        this.skeleton = skeleton;
    }

    @Override
    public void click(@NotNull Vector3 clickCoordinates) {
        Vector3 touchViewport = camera.unproject(clickCoordinates);
        PositionComponent skeletonPos = skeleton.getComponent(PositionComponent.class);
        FacingComponent skeletonFacing = skeleton.getComponent(FacingComponent.class);
        float deltaX = touchViewport.x - skeletonPos.getWorld().x;
        float deltaY = touchViewport.y - skeletonPos.getWorld().y;

        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            if (deltaX >= 0) {
                skeletonFacing.setFacing(DIRECTION.EAST);
            } else {
                skeletonFacing.setFacing(DIRECTION.WEST);
            }
        } else {
            if (deltaY >= 0) {
                skeletonFacing.setFacing(DIRECTION.NORTH);
            } else {
                skeletonFacing.setFacing(DIRECTION.SOUTH);
            }
        }
        skeletonPos.setP(new Vector2(
                skeletonPos.getP().x + skeletonFacing.getFacing().getDx(),
                skeletonPos.getP().y + skeletonFacing.getFacing().getDy()));

        skeletonPos.setWorld(new Vector2(
                skeletonPos.getWorld().x + 16.f*skeletonFacing.getFacing().getDx(),
                skeletonPos.getWorld().y + 16.f*skeletonFacing.getFacing().getDy()));
    }
}
