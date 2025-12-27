package com.demo.forum.api;

import com.demo.forum.application.ForumInfoService;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

@Configuration
@EnableAsync
public class ForumApiConfig {
    @Bean
    public ForumInfoService forumInfoService() {
        return new ForumInfoService();
    }

    @Bean
    public TaskExecutor applicationTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor jobSchedulerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("job-");
        executor.initialize();
        return executor;
    }

    @Bean
    public PluginManager pluginManager(@Value("${forum.plugins.dir:plugins}") String pluginsDir) {
        DefaultPluginManager pluginManager = new DefaultPluginManager(Path.of(pluginsDir));
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        return pluginManager;
    }
}
