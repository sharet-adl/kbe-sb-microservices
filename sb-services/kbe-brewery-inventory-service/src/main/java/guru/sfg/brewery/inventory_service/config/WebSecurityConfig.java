package guru.sfg.brewery.inventory_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;


/**
 * Created by jt on 5/16/20.
 */

@AutoConfiguration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/actuator/health/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().httpBasic(Customizer.withDefaults());

        return http.build();
    }
}