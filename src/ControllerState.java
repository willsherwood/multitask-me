import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerState {

    public static synchronized Map<Component, Float> mapFrom(Controller k) {
        k.poll();
        Map<Component, Float> map = new HashMap<>();
        for (Component c : k.getComponents())
            map.put(c, c.getPollData());
        return map;
    }
}
