package com.cinsc.MainView.jwt;

import org.apache.shiro.authc.AuthenticationToken;

public class JWTToken implements AuthenticationToken {


    /*密钥*/
    private String token;

    public JWTToken( String token) {
        this.token = token;
    }


    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
