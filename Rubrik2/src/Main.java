import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private List<Point> points = new ArrayList<>();
    private JPanel drawPanel;

    public Main() {
        setTitle("Midpoint Ellipse Drawing Algorithm");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (points.size() == 3) {
                    Point center = points.get(0);
                    Point majorAxisEnd = points.get(1);
                    Point minorAxisEnd = points.get(2);
                    int majorAxisLength = (int) Math.sqrt(Math.pow(center.x - majorAxisEnd.x, 2) + Math.pow(center.y - majorAxisEnd.y, 2));
                    int minorAxisLength = (int) Math.sqrt(Math.pow(center.x - minorAxisEnd.x, 2) + Math.pow(center.y - minorAxisEnd.y, 2));
                    drawMidPointEllipse(g, center, majorAxisLength, minorAxisLength);
                }
            }
        };

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (points.size() < 3) {
                    points.add(e.getPoint());
                } else {
                    points.clear();
                    points.add(e.getPoint());
                }
                repaint();
            }
        });

        add(drawPanel);
    }

    private void drawMidPointEllipse(Graphics g, Point center, int a, int b) {
        int x0 = center.x;
        int y0 = center.y;

        int aSquared = a * a;
        int bSquared = b * b;
        int twoASquared = 2 * aSquared;
        int twoBSquared = 2 * bSquared;

        int x = a;
        int y = 0;
        int dx = bSquared * (1 - 2 * a);
        int dy = aSquared;
        int err = 0;
        int stopX = twoBSquared * a;
        int stopY = 0;

        while (stopX >= stopY) {
            drawEllipsePoints(g, x0, y0, x, y);
            y++;
            stopY += twoASquared;
            err += dy;
            dy += twoASquared;
            if ((2 * err + dx) > 0) {
                x--;
                stopX -= twoBSquared;
                err += dx;
                dx += twoBSquared;
            }
        }

        x = 0;
        y = b;
        dx = bSquared;
        dy = aSquared * (1 - 2 * b);
        err = 0;
        stopX = 0;
        stopY = twoASquared * b;

        while (stopX <= stopY) {
            drawEllipsePoints(g, x0, y0, x, y);
            x++;
            stopX += twoBSquared;
            err += dx;
            dx += twoBSquared;
            if ((2 * err + dy) > 0) {
                y--;
                stopY -= twoASquared;
                err += dy;
                dy += twoASquared;
            }
        }
    }

    private void drawEllipsePoints(Graphics g, int x0, int y0, int x, int y) {
        g.drawRect(x0 + x, y0 + y, 1, 1);
        g.drawRect(x0 - x, y0 + y, 1, 1);
        g.drawRect(x0 + x, y0 - y, 1, 1);
        g.drawRect(x0 - x, y0 - y, 1, 1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
