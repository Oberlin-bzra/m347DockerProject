package ch.example.fahrzeugverwaltung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Einstiegspunkt der Fahrzeugverwaltung-Applikation.
 * <p>
 * Einfache Spring-Boot-Applikation mit REST-API, die Fahrzeugdaten
 * in einer PostgreSQL-Datenbank verwaltet (CRUD).
 */
@SpringBootApplication
public class FahrzeugverwaltungApplication {

    public static void main(String[] args) {
        SpringApplication.run(FahrzeugverwaltungApplication.class, args);
    }
}
