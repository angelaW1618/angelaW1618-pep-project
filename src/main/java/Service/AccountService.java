package Service;

import java.sql.SQLException;


import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
 
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account) throws SQLException {
        if(account.getUsername() == null || account.getUsername().trim().isEmpty() || account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        if(accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
            
        }

        return accountDAO.createAccount(account);
    }
    
    public Account login(String username, String password) throws SQLException {
        if(username == null || username.trim().isEmpty() || password == null) {
            return null;
        }

        Account account = accountDAO.getAccountByUsername(username);
        if(account != null && account.getPassword().equals(password)) {
            return account;
        } else {
            return null;
        }
    }
    
}
