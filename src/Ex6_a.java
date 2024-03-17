import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ex6_a implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JLabel data;
    JButton submit;
    JTextField data_input;
    JTextField an;
    JLabel an_label;

    public Ex6_a() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        data = new JLabel("Luna: ");

        data_input = new JTextField("9");

        an = new JTextField("2023");

        an_label = new JLabel("An: ");

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("Să se găsească pentru fiecare depanator numărul de devize (nume_depanator,\n" +
                "câte_devize) cu data_finalizare în luna X.");

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
        frame.add(an_label, gbc);

        gbc.gridy = 5;
        frame.add(an, gbc);

        gbc.gridy = 6;
        frame.add(submit, gbc);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void getSQLResults(int luna, int an) throws SQLException {

        String sql_url = "jdbc:mysql://localhost:3306/sql";
        String user = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(sql_url, user, password);

        String query = "SELECT nume, id, " +
                "(SELECT COUNT(*) FROM Deviz " +
                "WHERE id_depanator = id AND MONTH(data_finalizare) = ? AND YEAR(data_finalizare) = ?) AS \"Count\" " +
                "FROM persoana " +
                "JOIN deviz ON id = id_depanator " +
                "GROUP BY nume, id;";


        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, luna);
        statement.setInt(2, an);

        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"nume", "id", "Count"};
        DefaultTableModel model_tabel = new DefaultTableModel();
        model_tabel.setColumnIdentifiers(coloane_tabel);

        JTable tabel = new JTable();
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabel.setModel(model_tabel);

        JScrollPane scroll = new JScrollPane(tabel);

        while(result.next()){

            model_tabel.addRow(
                    new Object[]{
                            result.getString("nume"),
                            result.getInt("id"),
                            result.getInt("Count")
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
                getSQLResults(Integer.parseInt(data_input.getText()), Integer.parseInt(an.getText()));

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            frame.dispose();
        }
    }
}
