package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, message.getPosted_by());
        pstmt.setString(2, message.getMessage_text());
        pstmt.setLong(3, message.getTime_posted_epoch());
        pstmt.executeUpdate();

        ResultSet generatredKeys = pstmt.getGeneratedKeys();
        if(generatredKeys.next()) {
            message.setMessage_id(generatredKeys.getInt(1));
        } else {
            throw new SQLException("Failled to create new message.");
        }
        
        return message;
    }

    public List<Message> getAllMessages() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
       
        String sql = "SELECT * FROM message;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            messages.add(message);
        }
    

        return messages;
    }

    public Message getMessageById(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, message_id);

        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
        }
        return null;

    }

    public boolean deleteMessage(int message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, message_id);

        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
        
    }

    public Message updateMessage(int message_id, String newText) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newText);
        pstmt.setInt(2, message_id);
        pstmt.executeUpdate();
       
        return getMessageById(message_id);

    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?;";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, accountId);

        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));

        }

        return messages;

    }
    
}
