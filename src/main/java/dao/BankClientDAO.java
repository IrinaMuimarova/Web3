package dao;

import model.BankClient;
import org.graalvm.compiler.phases.graph.ScopedPostOrderNodeIterator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        List<BankClient> list = new ArrayList<>();
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client");
        ResultSet result = stmt.getResultSet();
        while (result.next()) {
            list.add(new BankClient(result.getLong(1),
                    result.getNString(2),
                    result.getNString(3),
                    result.getLong(4)));
        }
        result.close();
        stmt.close();
        return list;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select password from bank_client where name='" + name + "'");
        ResultSet resultSet = stmt.getResultSet();
        resultSet.next();
        boolean result = resultSet.getString(1).equals(password);
        resultSet.close();
        stmt.close();
        return result;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select money from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long money = result.getLong(1);
        stmt.executeUpdate("update bank_client set money= " + (money + transactValue) + " where name='" + name + "'");
        result.close();
        stmt.close();
    }

    public BankClient getClientById(long id) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where id=" + id);
        ResultSet result = stmt.getResultSet();
        result.next();
        BankClient client = new BankClient(result.getLong(1),
                result.getString(2),
                result.getString(3),
                result.getLong(4));
        result.close();
        stmt.close();
        return client;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select money from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        if (result.getLong(1) >= expectedSum) {
            return true;
        }
        return false;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        BankClient client = null;
        if (result.next()) {
            client = new BankClient(result.getLong(1),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4));
        }
        result.close();
        stmt.close();
        return client;
    }

    public void addClient(BankClient client) throws SQLException {
        createTable();
        Statement stmt = connection.createStatement();
        if (getClientByName(client.getName()) == null) {
            stmt.execute("insert into bank_client(name, password, money) values ('" + client.getName() + "', '" + client.getPassword() + "', " + client.getMoney() + ")");
        } else {
            throw new SQLException("Клиент с таким именем уже сущществует.");
        }
        stmt.close();
        List<BankClient> list = getAllBankClient();
        for (BankClient c : list) {
            System.out.println(client.getName() + " " + client.getPassword() + " " + client.getMoney());
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }

    public boolean deleteBankClient(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("delete from bank_clients where name = '" + name + "'");
        stmt.close();
        return true;
    }
}