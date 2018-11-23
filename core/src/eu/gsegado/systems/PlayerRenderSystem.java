package eu.gsegado.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eu.gsegado.DIRECTION;
import eu.gsegado.components.FacingComponent;
import eu.gsegado.components.PositionComponent;
import eu.gsegado.components.SpriteComponent;

public class PlayerRenderSystem extends IteratingSystem {
    private ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<FacingComponent> fm = ComponentMapper.getFor(FacingComponent.class);

    private SpriteBatch batch;

    public PlayerRenderSystem(SpriteBatch batch) {
        super(Family.all(SpriteComponent.class, PositionComponent.class, FacingComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpriteComponent spriteComponent = sm.get(entity);
        PositionComponent positionComponent = pm.get(entity);
        FacingComponent facingComponent = fm.get(entity);

        batch.begin();
        if (facingComponent.getFacing() == DIRECTION.SOUTH && spriteComponent.getDown() != null) {
            batch.draw(spriteComponent.getDown(),
                    positionComponent.getWorld().x,
                    positionComponent.getWorld().y);
        } else if (facingComponent.getFacing() == DIRECTION.NORTH && spriteComponent.getUp() != null) {
            batch.draw(spriteComponent.getUp(),
                    positionComponent.getWorld().x,
                    positionComponent.getWorld().y);
        } else if (facingComponent.getFacing() == DIRECTION.EAST && spriteComponent.getRight() != null) {
            batch.draw(spriteComponent.getRight(),
                    positionComponent.getWorld().x,
                    positionComponent.getWorld().y);
        } else if (facingComponent.getFacing() == DIRECTION.WEST && spriteComponent.getLeft() != null) {
            batch.draw(spriteComponent.getLeft(),
                    positionComponent.getWorld().x,
                    positionComponent.getWorld().y);
        }
        batch.end();
    }
}
