package pl.nogacz.forum.config.authentication;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.nogacz.forum.config.authentication.util.AuthenticationEntryPoint;
import pl.nogacz.forum.config.authentication.util.RequestFilter;
import pl.nogacz.forum.domain.user.Role;
import pl.nogacz.forum.service.user.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private RequestFilter requestFilter;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication) throws Exception {
        authentication.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder());
    }

    private PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers("/user/**").hasAuthority(Role.USER.toString())
                    .antMatchers("/login", "/register").anonymous()
                    .antMatchers("/**").permitAll()
                .anyRequest()
                    .authenticated().and()
                .exceptionHandling()
                    .authenticationEntryPoint(this.authenticationEntryPoint).and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(this.requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}