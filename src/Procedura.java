import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Procedura implements ActionListener {

    JFrame frame;
    JLabel cerinta;
    JButton back;
    JButton submit;

    public Procedura() {
        frame = new JFrame();

        back = new JButton("Back");
        back.addActionListener(this);

        submit = new JButton("Submit");
        submit.addActionListener(this);

        cerinta = new JLabel("<html>Să se definească o procedură stocată care va introduce în tabela Excepții\n" +
                "acele linii din tabela Piesa_Deviz ce respectă condiția pret_r > pret_c (pentru\n" +
                "piesa respectivă) sau data_constatare = data_finalizare (pentru devizul\n" +
                "respectiv). Tabela Excepții va avea aceleași coloane ca și tabela Piesa_Deviz\n" +
                "plus o coloană ce indică natura excepției.</html>");

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

        String query_procedure = "CALL ex8();";
        String query = "SELECT * FROM Exceptii;";

        PreparedStatement statement_procedure = connection.prepareCall(query_procedure);
        statement_procedure.execute();

        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet result = statement.executeQuery();

        JFrame sql_results = new JFrame("SQL Results");

        sql_results.setLayout(new BorderLayout());
        sql_results.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] coloane_tabel = {"id_d", "id_p", "cantitate", "pret_r", "natura"};
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
                            result.getInt("id_p"),
                            result.getInt("cantitate"),
                            result.getInt("pret_r"),
                            result.getString("natura")
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
