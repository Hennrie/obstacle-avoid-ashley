package com.obstacleavoid.system.debug;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.util.ViewportUtils;

public class GridRenderSystem extends EntitySystem {

    // == constants ==
    private static final Logger log = new Logger(GridRenderSystem.class.getName(), Logger.DEBUG);

    // == attributes ==
    private final Viewport viewport;
    private final ShapeRenderer renderer;

    // == constructors ==
    public GridRenderSystem(Viewport viewport, ShapeRenderer renderer) {
        this.viewport = viewport;
        this.renderer = renderer;
    }

    // == update ==

    @Override
    public void update(float deltaTime) {
        log.debug("update()");
        viewport.apply();
        ViewportUtils.drawGrid(viewport, renderer);
    }
}
