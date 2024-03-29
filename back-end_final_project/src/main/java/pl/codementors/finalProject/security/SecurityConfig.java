package pl.codementors.finalProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

     @Autowired
     private LocalUserDetailsService localUserDetailsService;


     @Autowired
     private Environment env;

     @Autowired
     public void configureGlobal(AuthenticationManagerBuilder auth) {
          auth.authenticationProvider(authenticationProvider(passwordEncoder()));
     }

     @Override
     protected void configure(HttpSecurity http) throws Exception {

          http.csrf().disable().cors();

          http
                  .authorizeRequests()
                  .antMatchers("/**").permitAll()
                  .anyRequest()
                  .authenticated()
                  .and()
                  .httpBasic();
     }


     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowCredentials(true);
          config.addAllowedOrigin("http://localhost:4200");
          config.addAllowedHeader("*");
          config.addAllowedMethod("*");
          source.registerCorsConfiguration("/**", config);
          return source;
     }


     @Bean
     public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
          DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
          authProvider.setUserDetailsService(localUserDetailsService);
          authProvider.setPasswordEncoder(passwordEncoder);
          return authProvider;
     }

     @Bean
     public PasswordEncoder passwordEncoder(){
          return new BCryptPasswordEncoder();
     }
}