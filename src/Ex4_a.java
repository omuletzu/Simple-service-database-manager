import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ex4_a implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JButton submit;

    public Ex4_a() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("Să se găsească (id_d, descriere, fabricant, pret_c și pret_r) pentru piesele cu prețul de\n " +
                "catalog mai mare decât prețul real.");

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(cerinta, gbc);

        gbc.gridy = 1;
        frame.add(back, gbc);

        gbc.gridy = 2;
        frame.add(submit, gbc);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void getSQLResults() throws SQLException {

        String sql_url = "jdbc:mysql://localhost:3306/sql";
        String user = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(sql_url, user, password);

        String query = "SELECT id_d, descriere, fabricant, pret_c, pret_r FROM Piesa " +
                "JOIN Piesa_Deviz ON Piesa.id_p = Piesa_Deviz.id_p AND pret_c > pret_r;";

        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"id_d", "descriere", "fabricant", "pret_c", "pret_r"};
        DefaultTableModel model_tabel = new DefaultTableModel();
        model_tabel.setColumnIdentifiers(coloane_tabel);

        JTable tabel = new JTable();
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabel.setModel(model_tabel);

        JScrollPane scroll = new JScrollPane(tabel);

        while(result.next()){

            model_tabel.addRow(
                    new Object[]{
                            result.getInt("id_d"),
                            result.getString("descriere"),
                            result.getString("fabricant"),
                            result.getInt("pret_c"),
                            result.getInt("pret_r")
                    }
            );
        }

        connection.close();

        sql_results.add(scroll);
        sql_results.setExtendedState(JFrame.MAXIMIZED_BOTH);
        sql_results.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == submit) {

            try {
                getSQLResults();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            frame.dispose();
        }
    }
}
