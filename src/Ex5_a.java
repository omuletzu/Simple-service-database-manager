import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ex5_a implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JLabel data;
    JButton submit;
    JTextField data_input;

    public Ex5_a() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        data = new JLabel("Descriere: ");

        data_input = new JTextField("surub");

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("Să se găsească detaliile devizelor care au folosit piesa cu descrierea ‚șurub’.");

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(cerinta, gbc);

        gbc.gridy = 1;
        frame.add(back, gbc);

        gbc.gridy = 2;
        frame.add(data, gbc);

        gbc.gridy = 3;
        frame.add(data_input, gbc);

        gbc.gridy = 4;
        frame.add(submit, gbc);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void getSQLResults(String date) throws SQLException {

        String sql_url = "jdbc:mysql://localhost:3306/sql";
        String user = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(sql_url, user, password);

        String query = "SELECT * FROM Deviz " +
                "WHERE id_d IN (SELECT id_d FROM Piesa_Deviz " +
                "WHERE id_p = (SELECT id_p FROM Piesa WHERE descriere = ?));";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, date);

        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"id_d", "data_introducere", "aparat", "simptome", "defect", "data_constatare", "data_finalizare", "durata", "manopera_ora",
                "total", "id_client", "id_depanator"};
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
                            result.getDate("data_introducere"),
                            result.getString("aparat"),
                            result.getString("simptome"),
                            result.getString("defect"),
                            result.getDate("data_constatare"),
                            result.getDate("data_finalizare"),
                            result.getInt("durata"),
                            result.getInt("manopera_ora"),
                            result.getInt("total"),
                            result.getInt("id_client"),
                            result.getInt("id_depanator")
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
                getSQLResults(data_input.getText());

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            frame.dispose();
        }
    }
}
