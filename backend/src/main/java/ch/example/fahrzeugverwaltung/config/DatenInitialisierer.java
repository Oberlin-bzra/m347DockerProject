package ch.example.fahrzeugverwaltung.config;

import ch.example.fahrzeugverwaltung.model.Fahrzeug;
import ch.example.fahrzeugverwaltung.repository.FahrzeugRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Befüllt die Datenbank beim ersten Start mit ein paar Beispielfahrzeugen,
 * damit die UI nicht leer ist. Läuft nur, wenn die Tabelle noch leer ist.
 */
@Configuration
public class DatenInitialisierer {

    @Bean
    CommandLineRunner initDatenbank(FahrzeugRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Fahrzeug("SG 12 345", "Volkswagen", "Golf", 2019, "Blau", 42000, "VERFUEGBAR"));
                repository.save(new Fahrzeug("SG 98 765", "Skoda", "Octavia", 2021, "Weiss", 15500, "VERFUEGBAR"));
                repository.save(new Fahrzeug("ZH 55 210", "Tesla", "Model 3", 2023, "Schwarz", 8000, "IN_WARTUNG"));
                repository.save(new Fahrzeug("SG 33 020", "Toyota", "Corolla", 2018, "Rot", 61000, "VERMIETET"));
            }
        };
    }
}
