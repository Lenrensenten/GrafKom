import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private int x = 300;
    private int y = 300;
    private int size = 100;
    private int degrees = 20;
    private double angle = 0; // Angle in degrees
    private double scale = 1.0; // Scaling factor

    private JLabel centerLabel;
    private TransformationPanel panel;
    private JTextField rotateField;

    public Main() {
        setTitle("Shape Transformations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new TransformationPanel();
        add(panel);

        rotateField = new JTextField("1", 5);
        JButton rotateButton = new JButton("Scale");
        rotateButton.addActionListener(e -> {
            try {
                degrees = (int) Double.parseDouble(rotateField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid scale factor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        centerLabel = new JLabel();
        JPanel topPanel = new JPanel();
        JPanel botPanel = new JPanel();
        topPanel.add(new JLabel("Center: "));
        topPanel.add(centerLabel);
        botPanel.add(new JLabel("- Klik Kanan(drag) : Translate "));
        botPanel.add(new JLabel("- Mouse Wheel : Scaling Size"));
        botPanel.add(new JLabel("- Klik Kiri : Rotate"));
        botPanel.add(new JLabel("Rotate degrees: "));
        botPanel.add(rotateField);
        botPanel.add(rotateButton);


        add(topPanel, BorderLayout.NORTH);
        add(botPanel, BorderLayout.SOUTH);

        panel.updateCenterLabel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    class TransformationPanel extends JPanel {
        private int lastX;
        private int lastY;

        public TransformationPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastX = e.getX();
                    lastY = e.getY();

                    if (SwingUtilities.isRightMouseButton(e)) {
                        rotateShape(degrees);
                    }
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        int dx = e.getX() - lastX;
                        int dy = e.getY() - lastY;
                        translateShape(dx, dy);
                        lastX = e.getX();
                        lastY = e.getY();
                    }
                }
            });

            addMouseWheelListener(e -> {
                if (e.getWheelRotation() < 0) {
                    scaleShape(1.1);
                } else {
                    scaleShape(0.9);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);

            // Calculate the vertices of the square
            int halfSize = (int) (size * scale / 2);
            int[][] vertices = {
                    {-halfSize, -halfSize},
                    {halfSize, -halfSize},
                    {halfSize, halfSize},
                    {-halfSize, halfSize}
            };

            // Rotate vertices
            double radians = Math.toRadians(angle);
            for (int[] vertex : vertices) {
                int tempX = vertex[0];
                vertex[0] = (int) (tempX * Math.cos(radians) - vertex[1] * Math.sin(radians));
                vertex[1] = (int) (tempX * Math.sin(radians) + vertex[1] * Math.cos(radians));
            }

            // Translate vertices
            for (int[] vertex : vertices) {
                vertex[0] += x;
                vertex[1] += y;
            }

            // Draw the square using the transformed vertices
            for (int i = 0; i < vertices.length; i++) {
                int[] v1 = vertices[i];
                int[] v2 = vertices[(i + 1) % vertices.length];
                g.drawLine(v1[0], v1[1], v2[0], v2[1]);
            }
        }

        public void translateShape(int dx, int dy) {
            x += dx;
            y += dy;
            updateCenterLabel(); // Update the center label
            repaint(); // Repaint the shape after translation
        }

        public void rotateShape(double deltaAngle) {
            angle += deltaAngle;
            updateCenterLabel(); // Update the center label
            repaint(); // Repaint the shape after rotation
        }

        public void scaleShape(double factor) {
            scale *= factor;
            updateCenterLabel(); // Update the center label
            repaint(); // Repaint the shape after scaling
        }

        private void updateCenterLabel() {
            centerLabel.setText("(" + x + ", " + y + ")");
        }
    }
}
