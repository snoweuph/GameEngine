package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.entityComponentSystem.Component;

import java.util.ArrayList;
import java.util.List;

public class TestComponent extends Component {

    @Override
    protected List<Component> getRequiredComponents() {
        return new ArrayList<>();
    }
}
