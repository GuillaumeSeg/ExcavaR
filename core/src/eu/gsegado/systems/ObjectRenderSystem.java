package eu.gsegado.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import eu.gsegado.components.PositionComponent;
import eu.gsegado.components.TextureComponent;

public class ObjectRenderSystem extends IteratingSystem {
    private ComponentMapper<TextureComponent> tm = ComponentMapper.getFor(TextureComponent.class);
    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

    private SpriteBatch batch;

    public ObjectRenderSystem(SpriteBatch batch) {
        super(Family.all(TextureComponent.class, PositionComponent.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent textureComponent = tm.get(entity);
        PositionComponent positionComponent = pm.get(entity);

        batch.begin();
        if (textureComponent.getTexture() != null) {
            batch.draw(textureComponent.getTexture(), positionComponent.getWorld().x, positionComponent.getWorld().y);
        }
        batch.end();
    }
}
