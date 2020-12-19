package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity   // WebSecurityConfiguration, SpringWebMvcImportSelector, OAuth2ImportSelector 클래스를 import 해준다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();
       http
                .formLogin();

       http
               .logout()
               .logoutUrl("/logout")
               .logoutSuccessUrl("/login")
               .addLogoutHandler(new LogoutHandler() {
                   @Override
                   public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                       HttpSession session = request.getSession();
                       session.invalidate();
                   }
               })
               .logoutSuccessHandler(new LogoutSuccessHandler() {
                   @Override
                   public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                       response.sendRedirect("/login");
                   }
               })
               .deleteCookies("remember-me")
        .and()
                .rememberMe()
                .rememberMeParameter("remember")
               .tokenValiditySeconds(3600)
               .userDetailsService(userDetailsService)
       .and()
               .sessionManagement()
               .maximumSessions(1)
               .maxSessionsPreventsLogin(true)
       ;
    }
}
