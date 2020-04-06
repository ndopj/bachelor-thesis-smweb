package cz.sm.ng.spring.launcher;

import cz.sm.ng.security.SHA1CryptPasswordEncoder;
import cz.sm.ng.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Configures Spring Boot security to secure
 * destinations of web application. Defines URL
 * where client will be redirected in case of
 * unauthorized access. Also specifies how logout
 * should be handled.
 *
 * @author Norbert Dopjera
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/clodwar/**").authorizeRequests()
                .antMatchers("/clodwar/pilot/**").hasAnyAuthority("PILOT", "ADMIN")
                .antMatchers("/clodwar/profile/**").hasAnyAuthority("PILOT", "ADMIN")
                .antMatchers("/clodwar/lobby**").hasAnyAuthority("PILOT", "GENERAL", "ADMIN")
                .antMatchers("/clodwar/").permitAll()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/clodwar/login.xhtml"))
                .and().exceptionHandling().accessDeniedPage("/clodwar/login.xhtml");
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/pilot/**").hasAnyAuthority("PILOT", "ADMIN")
                .antMatchers("/general/**").hasAnyAuthority("GENERAL", "ADMIN")
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/api/module/**").hasAnyAuthority("ADMIN")
                .antMatchers("/").permitAll()
                .and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/signin.xhtml"))
                .and().exceptionHandling().accessDeniedPage("/signin.xhtml")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logedoff.xhtml")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public SHA1CryptPasswordEncoder sha1CryptPasswordEncoder() { return new SHA1CryptPasswordEncoder(); }

    /**
     * Provides custom User details service implemented
     * in smweb-ng-security module and custom password
     * encoder to be used when identity is authenticated.
     *
     * @param auth authentication manager builder to configure
     * @throws Exception should not occur
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(sha1CryptPasswordEncoder());
    }

} // WebSecurityConfiguration

