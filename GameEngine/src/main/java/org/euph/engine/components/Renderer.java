package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.Component;

import java.util.ArrayList;
import java.util.List;

//TODO: this
//TODO: JavaDoc
abstract public class Renderer extends Component {
    @Override
    protected List<Component> getRequiredComponents() {
        List<Component> components = new ArrayList<>();
        return components;
    }
}
