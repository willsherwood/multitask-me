import net.java.games.input.Component;

import java.awt.*;
import java.util.Map;

public class TitleState extends State {

    private Map<Component, Float> x;
    private int stepX;
    private int paintX;

    @Override
    public void step(Map<Component, Float> k) {
        x = k;
        stepX++;
    }

    @Override
    public void paint(Graphics2D g) {
        g.clearRect(0, 0, 1000, 1000);
        int y = 70;
        g.drawString("Analogs", 50, 40);
        for (Component c : x.keySet()) {
            if (c.isAnalog()) {
                g.drawString(c + "    " + x.get(c), 50, y += 30);
            }
        }

    }
}
