import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main implements MouseListener {

    JFrame meniu = new JFrame();

    JLabel Ex_06_03_A;
    JLabel Ex_06_03_B;
    JLabel Ex_06_04_A;
    JLabel Ex_06_04_B;
    JLabel Ex_06_05_A;
    JLabel Ex_06_05_B;
    JLabel Ex_06_06_A;
    JLabel Ex_06_06_B;
    JLabel procedura;

    public Main(){

        meniu.setLayout(new GridLayout(3, 3));
        meniu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        meniu.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Ex_06_03_A = new JLabel("Ex_06_03_A");
        Ex_06_03_A.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_03_A);
        meniu.add(Ex_06_03_A);
        Ex_06_03_A.addMouseListener(this);

        Ex_06_03_B = new JLabel("Ex_06_03_B");
        Ex_06_03_B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_03_B);
        meniu.add(Ex_06_03_B);
        Ex_06_03_B.addMouseListener(this);

        Ex_06_04_A = new JLabel("Ex_06_04_A");
        Ex_06_04_A.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_04_A);
        meniu.add(Ex_06_04_A);
        Ex_06_04_A.addMouseListener(this);

        Ex_06_04_B = new JLabel("Ex_06_04_B");
        Ex_06_04_B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_04_B);
        meniu.add(Ex_06_04_B);
        Ex_06_04_B.addMouseListener(this);

        Ex_06_05_A = new JLabel("Ex_06_05_A");
        Ex_06_05_A.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_05_A);
        meniu.add(Ex_06_05_A);
        Ex_06_05_A.addMouseListener(this);

        Ex_06_05_B = new JLabel("Ex_06_05_B");
        Ex_06_05_B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_05_B);
        meniu.add(Ex_06_05_B);
        Ex_06_05_B.addMouseListener(this);

        Ex_06_06_A = new JLabel("Ex_06_06_A");
        Ex_06_06_A.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_06_A);
        meniu.add(Ex_06_06_A);
        Ex_06_06_A.addMouseListener(this);

        Ex_06_06_B = new JLabel("Ex_06_06_B");
        Ex_06_06_B.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(Ex_06_06_B);
        meniu.add(Ex_06_06_B);
        Ex_06_06_B.addMouseListener(this);

        procedura = new JLabel("Procedura");
        procedura.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cell_align(procedura);
        meniu.add(procedura);
        procedura.addMouseListener(this);

        meniu.setTitle("Colocviu final");
        meniu.setVisible(true);
    }

    public static void cell_align(JLabel cell){
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setVerticalAlignment(SwingConstants.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }

    public void openNewFrame(JFrame frame){
        frame.setVisible(true);
        meniu.setVisible(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                meniu.setVisible(true);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object source = e.getSource();

        if(source == Ex_06_03_A){
            openNewFrame(new Ex3_a().frame);
        }

        if(source == Ex_06_03_B){
            openNewFrame(new Ex3_b().frame);
        }

        if(source == Ex_06_04_A){
            openNewFrame(new Ex4_a().frame);
        }

        if(source == Ex_06_04_B){
            openNewFrame(new Ex4_b().frame);
        }

        if(source == Ex_06_05_A){
            openNewFrame(new Ex5_a().frame);
        }

        if(source == Ex_06_05_B){
            openNewFrame(new Ex5_b().frame);
        }

        if(source == Ex_06_06_A){
            openNewFrame(new Ex6_a().frame);
        }

        if(source == Ex_06_06_B){
            openNewFrame(new Ex6_b().frame);
        }

        if(source == procedura){
            openNewFrame(new Procedura().frame);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
