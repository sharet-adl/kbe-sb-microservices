package guru.sfg.brewery.inventory_service.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Profile;

/**
 * Created by jt on 3/4/20.
 */
@EnableDiscoveryClient
@Profile("local-discovery")
@AutoConfiguration
public class LocalDiscovery {
}
