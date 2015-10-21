import net.java.games.input.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
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
        art.setPreferredSize(new Dimension(900, 900 / 16 * 9));
        SwingUtilities.invokeLater(() -> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setLocationRelativeTo(null);
            frame.add(art);
            frame.pack();
            frame.setVisible(true);
        });

        State state = new TitleState();
        art.setDoubleBuffered(true);
        BufferedImage db = new BufferedImage(800, 800 / 16 * 9, BufferedImage.TYPE_INT_RGB);
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

        while (true) {
            Map<net.java.games.input.Component, Float> x = ControllerState.mapFrom(joy);
            state.step(x);
            iterations++;
            if (iterations % 100 == 0) {
                iterations = 0;
                double fps = 100 / ((-time + (time = System.currentTimeMillis())) / 1000.);
//                System.out.println(fps);
            }
        }
    }
}
