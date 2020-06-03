package domain;

public class Personal {
    private String userAccount;
    private String password;
    private String userName;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Personal(String userAccount, String password, String userName) {
        this.userAccount = userAccount;
        this.password = password;
        this.userName = userName;
    }

    public Personal() {

    }
}
