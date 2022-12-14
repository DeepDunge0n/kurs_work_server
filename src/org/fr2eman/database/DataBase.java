package org.fr2eman.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.fr2eman.service.DataService;

public class DataBase {

    private static String DB_URL = "jdbc:mysql://";

    //  Database credentials
    private static String USER;
    private static String PASS;

    private static final String FILE_SQL_SCRIPT = "resource/script.sql";

    Connection connect;
    Statement statement;

    private static DataBase instance = new DataBase();

    public static DataBase getInstance() {
        return instance;
    }

    public void initDatabaseConfigurations() {
        DB_URL += DataService.getInstance().getDatabaseURL() + ":"
                + DataService.getInstance().getDatabasePort() + "/";
        USER = new String(DataService.getInstance().getDatabaseLogin());
        PASS = new String(DataService.getInstance().getDatabasePassword());
    }

    public Statement getStatement() {
        return statement;
    }

    public void connectToBD() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connect.createStatement();
            Scanner content = null;
            try {
                content = new Scanner(new File(FILE_SQL_SCRIPT));
                content.useDelimiter(";");
            } catch (FileNotFoundException e) {
            }
            try {
                while (content.hasNext())
                {
                    String query = content.next();
                    statement.executeUpdate(query);
                }
            } catch(SQLException e) {
                System.out.println("Ошибка создания базы");
                throw e;
            }
            System.out.println("Соединение с базой: Установлено");
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Ошибка получения доступа к базе данных");
            System.out.println("База данных не найдена или в config.conf логин и пароль указан не правильно");
            throw e;
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка загрузки драйвера com.mysql.jdbc.Driver");
            throw new SQLException();
        }
    }

    public ResultSet selectData(String table, String fields,
                                String whereString, String orderString) throws SQLException {
        String sql;
        sql = String.format("SELECT %s FROM %s %s %s", fields,
                table, whereString, orderString);
        ResultSet rs = null;
        rs = statement.executeQuery(sql);
        return rs;
    }

    public ResultSet selectData(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public int insertData(String table, String... values)
            throws SQLException {
        String sql;
        String data = "";
        for(String item : values) {
            if(item != null && (item.equals("Default") || item.equals("CURDATE()")))
                data += ", " + item;
            else
                data += ", '" + item + "' ";
        }
        data = data.substring(2, data.length() - 1);
        sql = String.format("INSERT INTO %s VALUES (%s)", table, data);
        return statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    }
    public int deleteData(String table, String whereString) throws SQLException {
        String sql;
        sql = String.format("DELETE FROM %s %s", table, whereString);
        return statement.executeUpdate(sql);
    }
    public int updateData(String table, String setString, String whereString)
            throws SQLException {
        return statement.executeUpdate(
                String.format("UPDATE %s %s %s", table, setString, whereString));
    }
    public ResultSet selectData(String table, String fields, String where, String order,
                                String... joins) throws SQLException {
        String sql;
        String joinsSQL = "";
        for(String join : joins) {
            joinsSQL += join + " ";
        }
        sql = String.format("SELECT %s FROM %s %s %s %s", fields,
                table, joinsSQL, where, order);
        ResultSet rs = null;
        rs = statement.executeQuery(sql);
        return rs;
    }

    public void closeConnect() {
        try {
            statement.close();
            connect.close();
        } catch (SQLException e) {
        }
    }
}
