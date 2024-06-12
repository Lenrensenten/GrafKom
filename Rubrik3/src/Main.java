import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private int x = 300;
    private int y = 300;
    private int sideLength = 200; // Panjang sisi persegi

    private Point lastMousePosition;

    public Main() {
        setTitle("Shape Transformations");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TransformationPanel panel = new TransformationPanel();
        add(panel);

        JLabel instructionLabel = new JLabel("Click and drag to translate, scroll to scale, right click to rotate");
        add(instructionLabel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }

    class TransformationPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
        private double rotationAngle = 0;
        private double scaleFactor = 1.0;

        public TransformationPanel() {
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            // Set anti-aliasing for better rendering
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Translate to the center of the object
            g2d.translate(x, y);

            // Rotate
            g2d.rotate(Math.toRadians(rotationAngle));

            // Scale
            g2d.scale(scaleFactor, scaleFactor);

            // Draw the shape
            g2d.setColor(Color.BLUE);
            g2d.fillRect(-sideLength / 2, -sideLength / 2, sideLength, sideLength); // Gambar persegi

            g2d.dispose(); // Clean up
        }

        @Override
        public void mousePressed(MouseEvent e) {
            lastMousePosition = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            lastMousePosition = null;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) { // Cek apakah tombol mouse kanan yang ditekan
                rotationAngle += 20; // Rotasi sebesar 20 derajat saat mengklik mouse kanan
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {
            if (lastMousePosition != null) {
                int dx = e.getX() - lastMousePosition.x;
                int dy = e.getY() - lastMousePosition.y;
                x += dx;
                y += dy;
                repaint();
                lastMousePosition = e.getPoint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {}

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double scaleDelta = (e.getPreciseWheelRotation() < 0) ? 0.1 : -0.1; // Scale factor
            scaleFactor += scaleDelta;
            scaleFactor = Math.max(0.0, scaleFactor); // Batas bawah untuk skala
            repaint();
        }
    }
}
