package com.honorarios.honorarios_api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// Se DB_URL vier como postgresql://... (Railway), converte para jdbc: antes do datasource subir.
// Se ja vier jdbc: (local ou Railway com valor correto), nao mexe.
public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication application) {
        String url = env.getProperty("DB_URL");
        if (url == null || url.isBlank() || url.startsWith("jdbc:")) {
            return;
        }
        try {
            URI uri = URI.create(url);
            int port = uri.getPort() == -1 ? 5432 : uri.getPort();
            Map<String, Object> props = new HashMap<>();
            props.put("spring.datasource.url", "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath());

            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                String[] parts = userInfo.split(":", 2);
                props.put("spring.datasource.username", URLDecoder.decode(parts[0], StandardCharsets.UTF_8));
                if (parts.length > 1) {
                    props.put("spring.datasource.password", URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
                }
            }
            env.getPropertySources().addFirst(new MapPropertySource("railwayDatabaseUrl", props));
        } catch (Exception ignored) {
            // URL invalida -> deixa o fluxo normal reclamar de forma clara
        }
    }
}
