package guru.sfg.brewery.inventory_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Task Configuration - enable asyc tasks
 */
@EnableScheduling
@EnableAsync
@AutoConfiguration
public class TaskConfig {
    @Bean
    TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }
}
