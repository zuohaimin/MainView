package com.cinsc.MainView.config;



import com.cinsc.MainView.jwt.JWTToken;
import com.cinsc.MainView.jwt.JWTUtil;
import com.cinsc.MainView.model.UserLogin;
import com.cinsc.MainView.service.ResourceService;
import com.cinsc.MainView.service.UserService;
import com.cinsc.MainView.utils.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: 束手就擒
 * @Date: 18-6-9 下午9:02
 * @Description:
 */
@Component
@Slf4j
public class UserReal extends AuthorizingRealm {

    private UserService userService;
    private ResourceService resourceService;


    @Autowired
    public UserReal(UserService userService,
                    ResourceService resourceService){
        this.userService = userService;
        this.resourceService = resourceService;

    }


    @Value("secret")
    private String secret;
    @Override
    public boolean supports(AuthenticationToken token){
        return token instanceof JWTToken;
    }

    /**
     * 用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userAccount = JWTUtil.getUserAccount(principalCollection.toString());
        UserLogin userLogin = userService.findByAccount(userAccount);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(resourceService.seleteUserPerms(userLogin.getUserId()));
        return simpleAuthorizationInfo;
    }

    /**
     * 用户验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String)authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String userAccount = JWTUtil.getUserAccount(token);
        Integer userId = JWTUtil.getUserId(token);
        System.out.println(userAccount);
        //TODO 用ENUMS来封装数据 Optional<T>去除繁杂的if判断
        if (userAccount == null) {
            throw new AuthenticationException("token invalid");
        }

        UserLogin userLogin = userService.findByAccount(userAccount);
        if (userLogin == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token,userAccount,userId, secret)) {
            throw new AuthenticationException("Username or password error");
        }

        // TODO 需要修改

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userLogin,userLogin.getUserPwd(),this.getName());
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userLogin.getUserSalt()));
        System.out.println("hhh");
        return simpleAuthenticationInfo;
        /*
        UsernamePasswordToken token = (JWTToken) authenticationToken;
        SysUser sysUser = sysUserService.findByAccount(token.getUsername());
        if (sysUser == null){
            log.error(REnum.UNkNOWN_ACCOUNT.getMessage()+"SysUser={}",sysUser);
            throw new UnknownAccountException();
        }
        if (ForbiddenEnum.DISABLE.getCode().toString().equals(sysUser.getForbidden())){
            log.error(REnum.UNkNOWN_ACCOUNT.getMessage()+"SysUser={}",sysUser);
            throw new DisabledAccountException();
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(sysUser,sysUser.getPassword(),this.getName());
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(sysUser.getSalt()));
        return simpleAuthenticationInfo;
        */
    }

    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher){
        HashedCredentialsMatcher md5Matcher = new HashedCredentialsMatcher();
        md5Matcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        md5Matcher.setHashIterations(ShiroUtil.hashIterations);
        md5Matcher.setStoredCredentialsHexEncoded(true);
        super.setCredentialsMatcher(md5Matcher);
    }
}
