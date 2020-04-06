package cz.sm.web.prototype.springbootjsf;

import cz.sm.web.prototype.springbootjsf.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/pilot/**").hasAnyAuthority("PILOT", "ADMIN")
                .antMatchers("/general/**").hasAnyAuthority("GENERAL", "ADMIN")
                .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
                .antMatchers("/").permitAll()
             .and().authorizeRequests()
                .antMatchers("/console/**").permitAll()
             .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
