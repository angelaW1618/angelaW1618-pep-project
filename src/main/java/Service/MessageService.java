package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

   

    

    public Message createMessage(Message message) throws SQLException {
        if(message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255 || message.getPosted_by() <= 0 || accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }

        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) throws SQLException {
        return messageDAO.getMessageById(message_id);
    }



    public Message deleteMessage(int message_id) throws SQLException {
        
        Message message = messageDAO.getMessageById(message_id);
        if (message != null) {
            boolean isDeleted = messageDAO.deleteMessage(message_id);
            if (isDeleted) {
                return message; 
            }
        }
        return null;
    }

    public Message updateMessage(int message_id, String newText) throws SQLException {
        if(newText == null || newText.length() > 255 || newText.isEmpty()) {
            return null;
        }
        return messageDAO.updateMessage(message_id, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        if(accountId <= 0) {
            return null;
        }
        return messageDAO.getMessagesByAccountId(accountId);
    }


}
