package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.entityComponentSystem.Component;

import java.util.ArrayList;
import java.util.List;

abstract public class Renderer extends Component {
    @Override
    protected List<Component> getRequiredComponents() {
        List<Component> components = new ArrayList<>();
        components.add(new Transform());
        return components;
    }
}
