package com.baidu.acg.det.finance.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableAuthorizationServer
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;
    // 客户端信息
    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;

    // 支持密码模式
    private final AuthenticationManager authenticationManager;

    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);
        return services;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
    }

    // 配置客户端信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("clientDemo").secret(passwordEncoder.encode("123")).resourceIds("resource1")
            .authorizedGrantTypes("authorization_code", "refresh_token", "implicit","password","client_credentials").scopes("all")
            .redirectUris("http://localhost:8082/index","http://localhost:8082/implicit");
//
//        clients.inMemory().withClient("implicitDemo").secret(passwordEncoder.encode("123")).resourceIds("resource1")
//            .authorizedGrantTypes("implicit").scopes("all")
//            .redirectUris("http://localhost:8082/implicit");


    }


    // 配置endpoint信息
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .authorizationCodeServices(authorizationCodeServices()) // 授权码模式
            .tokenServices(tokenServices())
            // 密码模式
            .authenticationManager(authenticationManager);
    }


    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        // 配置授权码信息的存放位置
        return new InMemoryAuthorizationCodeServices();
    }
}
