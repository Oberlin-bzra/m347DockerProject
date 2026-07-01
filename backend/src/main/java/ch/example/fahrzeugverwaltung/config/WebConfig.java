package ch.example.fahrzeugverwaltung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Erlaubt Cross-Origin-Aufrufe der API. Im produktiven Betrieb übernimmt
 * der nginx-Reverse-Proxy des Frontend-Containers die Weiterleitung,
 * CORS wird hier nur für die lokale Entwicklung (z.B. Frontend auf
 * einem anderen Port) benötigt.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
