package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.entityComponentSystem.Component;

import java.util.ArrayList;

public class TestComponent extends Component {

    @Override
    public ArrayList<Component> getRequiredComponents() {
        return new ArrayList<>();
    }
}
