import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MidPoinLineWithGraphOutput extends JFrame {
    private List<Point> points = new ArrayList<>();
    private JPanel drawPanel;
    private JLabel showVertices;

    public MidPoinLineWithGraphOutput() {
        setTitle("Midpoint Line Drawing Algorithm");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showVertices = new JLabel();
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Titik: "));
        topPanel.add(showVertices);

        drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (points.size() == 2) {
                    drawMidPointLine(g, points.get(0), points.get(1));
                    updateShowVertices(points.get(0), points.get(1));
                }
            }
        };

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (points.size() < 2) {
                    points.add(e.getPoint());
                } else {
                    points.clear();
                    points.add(e.getPoint());
                }
                repaint();
            }
        });

        add(drawPanel);
        add(topPanel, BorderLayout.NORTH);
    }

    private void drawMidPointLine(Graphics g, Point p1, Point p2) {
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;

        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        if (steep) {
            // Swap x and y for both points if the line is steep
            int temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x1 > x2) {
            // Swap the points to ensure we draw from left to right
            int temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = Math.abs(y2 - y1);
        int error = dx / 2;
        int ystep = (y1 < y2) ? 1 : -1;
        int y = y1;

        for (int x = x1; x <= x2; x++) {
            if (steep) {
                g.drawRect(y, x, 1, 1); // If steep, we draw transposed
                System.out.println("{" + x + "," + y +"}");
            } else {
                g.drawRect(x, y, 1, 1); // If not steep, we draw normally
                System.out.println("{" + x + "," + y +"}");
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
            error -= dy;
            if (error < 0) {
                y += ystep;
                error += dx;
            }
        }


    }

    private void updateShowVertices(Point p1, Point p2){
        showVertices.setText(" (" + p1.x + "," + p1.y + ") ke (" + p2.x + "," + p2.y + ")");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MidPoinLineWithGraphOutput().setVisible(true);
        });
    }
}
