package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper mapper;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.mapper = new ObjectMapper();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountId);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */

    private void registerAccount(Context ctx) throws JsonProcessingException, SQLException {
        
        Account account = mapper.readValue(ctx.body(), Account.class);
   
        Account addedAccount = accountService.registerAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400).result("");
        }
    }

    private void login(Context ctx) throws JsonProcessingException, SQLException {
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.login(account.getUsername(), account.getPassword());
        if (loggedIn != null) {
            ctx.json(mapper.writeValueAsString(loggedIn));
        } else {
            ctx.status(401).result("");
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException, SQLException {
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.createMessage(message);
        if (addedMessage != null) {
            ctx.json(mapper.writeValueAsString(addedMessage));
        } else {
            ctx.status(400).result("");
        }
    }

    private void getAllMessages(Context ctx) throws SQLException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) throws SQLException {

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message); 
        } else {
            ctx.result(""); 
        }
        ctx.status(200); 
        

    }


    private void getMessageByAccountId(Context ctx) throws SQLException {

        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.json(messages);

    }

    private void deleteMessage(Context ctx) throws SQLException {
        
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
         
            if (deletedMessage != null) {
                ctx.json(deletedMessage); 
            } else {
                ctx.result(""); 
            }
            ctx.status(200); 
    
            
    }

    private void updateMessage(Context ctx) throws JsonProcessingException, SQLException {

        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedText = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.updateMessage(messageId, updatedText.getMessage_text());
        if (newMessage != null) {
            
            ctx.json(mapper.writeValueAsString(newMessage));
        } else {
            ctx.status(400).result("");
        }

    }

}