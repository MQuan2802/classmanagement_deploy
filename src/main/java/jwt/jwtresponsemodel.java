package jwt;

import java.io.Serializable;

public class jwtresponsemodel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String token;

    public jwtresponsemodel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
