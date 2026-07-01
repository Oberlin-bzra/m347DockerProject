package ch.example.fahrzeugverwaltung.repository;

import ch.example.fahrzeugverwaltung.model.Fahrzeug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FahrzeugRepository extends JpaRepository<Fahrzeug, Long> {
}
