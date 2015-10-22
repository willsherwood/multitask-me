import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static final int WIDTH = 1440, HEIGHT = 1440 / 16 * 9;
    public static final int STEP_FPS_CAP = 80;

    public static void main (String[] args) throws InterruptedException {
        Set<Controller> joySticks = Arrays.stream(
                ControllerEnvironment
                        .getDefaultEnvironment()
                        .getControllers())
                .filter(a -> a.getType().equals(Controller.Type.STICK))
                .collect(Collectors.toSet());
        if (joySticks.isEmpty()) {
            throw new RuntimeException("No supported controller found . . .");
        }
        Controller joy = joySticks.iterator().next();
        JFrame frame = new JFrame();
        JPanel art = new JPanel();
        art.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(art);
            frame.pack();
            frame.setVisible(true);
        });

        State state = new TitleState();
        art.setDoubleBuffered(true);
        BufferedImage db = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D dg = (Graphics2D) db.getGraphics();
        dg.setFont(new Font("Arial", 0, 26));
        dg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        new Thread(() -> {
            while (true) {
                state.paint(dg);
                SwingUtilities.invokeLater(() -> {
                    art.getGraphics().drawImage(db, 0, 0, art.getWidth(), art.getHeight(), null);
                });
                try {
                    Thread.sleep(1000 / 70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        int iterations = 0;
        long time = System.currentTimeMillis();
        int lag = 0;

        while (true) {
            Map<net.java.games.input.Component, Float> x = ControllerState.mapFrom(joy);
            if (lag > 0) {
                Thread.sleep(lag);
            }
            state.step(x);
            iterations++;
            if (iterations % 400 == 0) {
                iterations = 0;
                double fps = 400 / ((-time + (time = System.currentTimeMillis())) / 1000.);
                lag = Math.max(lag + (int) (1000. / STEP_FPS_CAP - 1000. / fps), 0);
                System.out.println(lag);
            }
        }
    }
}
