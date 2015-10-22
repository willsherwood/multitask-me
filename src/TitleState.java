import net.java.games.input.Component;

import java.awt.*;
import java.util.Map;

public class TitleState extends State {

    private Map<Component, Float> x;

    private double t;
    private double dt;

    private int q;

    @Override
    public void step(Map<Component, Float> k) {
        x = k;
        t += dt;
        dt = .999 * dt;
        q++;
        k.forEach((a,b) -> {
            if (a.getName().equals("Z Axis")) {
                dt += Math.PI / 4 / 80 / 333 * b;
            }
        });
    }

    @Override
    public void paint(Graphics2D g) {
        g.clearRect(0, 0, 1000, 1000);
        int y = 400;
        g.drawString("Analogs", 50, 40);
        for (Component c : x.keySet()) {
            if (c.isAnalog()) {
                g.drawString(c + "    " + x.get(c), 50, y += 30);
            }
        }
        g.drawString(String.format("%.2f %.2f",t ,dt), 400, 100);
        double magnitude = 100;
        g.setStroke(new BasicStroke(20));
        drawLine(g, -Math.cos(t) * magnitude, -Math.sin(t) * magnitude, 150);
        g.drawString(q + "", 100, 600);
    }

    public void drawLine(Graphics g, double a, double b, int offset) {
        g.drawLine((int) a + offset, (int) b + offset, (int) -a + offset, (int) -b + offset);
    }
}
