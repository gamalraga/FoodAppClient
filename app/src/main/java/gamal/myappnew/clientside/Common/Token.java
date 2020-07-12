package gamal.myappnew.clientside.Common;

import java.sql.DataTruncation;

public class Token {
    String token;
    boolean serverToken;

    public Token() {
    }

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.serverToken = isServerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return serverToken;
    }

    public void setServerToken(boolean serverToken) {
        serverToken = serverToken;
    }
}
