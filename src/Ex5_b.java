import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ex5_b implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JButton submit;

    public Ex5_b() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("Să se găsească descrierea și fabricantul pentru piesa cu prețul real cel mai\n " +
                "mare.");

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

        String query = "SELECT descriere, fabricant FROM Piesa " +
                "WHERE id_p = (SELECT id_p FROM Piesa_Deviz " +
                "WHERE pret_r >= ALL(SELECT pret_r FROM Piesa_Deviz));";

        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"descriere", "fabricant"};
        DefaultTableModel model_tabel = new DefaultTableModel();
        model_tabel.setColumnIdentifiers(coloane_tabel);

        JTable tabel = new JTable();
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabel.setModel(model_tabel);

        JScrollPane scroll = new JScrollPane(tabel);

        while(result.next()){

            model_tabel.addRow(
                    new Object[]{
                            result.getString("descriere"),
                            result.getString("fabricant")
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
