import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ex3_b implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JLabel data;
    JButton submit;
    JTextField data_input;

    public Ex3_b() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        data = new JLabel("Cantitate stoc: ");

        data_input = new JTextField("5");
        data_input.setPreferredSize(new Dimension(100, data_input.getPreferredSize().height));

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("Să se găsească detaliile pieselor care au cantitate_stoc sub 5 ordonat crescător după\n " +
                "cantitate_stoc și descrescător după descriere.");

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

    public void getSQLResults(int cantitate_stoc) throws SQLException {

        String sql_url = "jdbc:mysql://localhost:3306/sql";
        String user = "root";
        String password = "root";

        Connection connection = DriverManager.getConnection(sql_url, user, password);

        String query = "SELECT * FROM Piesa " +
                "WHERE cantitate_stoc < ? " +
                "ORDER BY cantitate_stoc ASC, descriere DESC;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, cantitate_stoc);

        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"id_p", "descriere", "fabricant", "cantitate_stoc", "pret_c"};
        DefaultTableModel model_tabel = new DefaultTableModel();
        model_tabel.setColumnIdentifiers(coloane_tabel);

        JTable tabel = new JTable();
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabel.setModel(model_tabel);

        JScrollPane scroll = new JScrollPane(tabel);

        while(result.next()){

            model_tabel.addRow(
                    new Object[]{
                            result.getInt("id_p"),
                            result.getString("descriere"),
                            result.getString("fabricant"),
                            result.getInt("cantitate_stoc"),
                            result.getInt("pret_c")
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
                getSQLResults(Integer.parseInt(data_input.getText()));

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        else{
            frame.dispose();
        }
    }
}
