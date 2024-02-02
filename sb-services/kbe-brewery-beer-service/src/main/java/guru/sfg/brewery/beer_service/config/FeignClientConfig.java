package guru.sfg.brewery.beer_service.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * Created by jt on 5/16/20.
 */
@AutoConfiguration
public class FeignClientConfig {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${sfg.brewery.inventory-user}") String user,
                @Value("${sfg.brewery.inventory-password}") String password) {


        return new BasicAuthRequestInterceptor(user, password);
    }
}
