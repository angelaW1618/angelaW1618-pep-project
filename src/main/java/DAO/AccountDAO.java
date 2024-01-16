package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account createAccount(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, account.getUsername());
        pstmt.setString(2, account.getPassword());
        pstmt.executeUpdate();

        ResultSet generatredKeys = pstmt.getGeneratedKeys();
        if(generatredKeys.next()) {
            account.setAccount_id(generatredKeys.getInt(1));
        } else {
            throw new SQLException("Failled to create new account.");
        }
        
        return account;
    }

    public Account getAccountByUsername(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return account;
        }
        return null;

    }

    public Account getAccountById(int accountId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, accountId);

        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            return account;
        }
        return null;

    }
}