package ch.example.fahrzeugverwaltung.controller;

import ch.example.fahrzeugverwaltung.model.Fahrzeug;
import ch.example.fahrzeugverwaltung.repository.FahrzeugRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST-API für die Verwaltung von Fahrzeugen (CRUD).
 * Basis-Pfad: /api/fahrzeuge
 */
@RestController
@RequestMapping("/api/fahrzeuge")
public class FahrzeugController {

    private final FahrzeugRepository repository;

    public FahrzeugController(FahrzeugRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Fahrzeug> alleFahrzeuge() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Fahrzeug fahrzeugById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fahrzeug mit ID " + id + " nicht gefunden"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fahrzeug neuesFahrzeug(@Valid @RequestBody Fahrzeug fahrzeug) {
        fahrzeug.setId(null);
        return repository.save(fahrzeug);
    }

    @PutMapping("/{id}")
    public Fahrzeug fahrzeugAktualisieren(@PathVariable Long id, @Valid @RequestBody Fahrzeug aenderung) {
        Fahrzeug bestehend = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fahrzeug mit ID " + id + " nicht gefunden"));

        bestehend.setKennzeichen(aenderung.getKennzeichen());
        bestehend.setMarke(aenderung.getMarke());
        bestehend.setModell(aenderung.getModell());
        bestehend.setBaujahr(aenderung.getBaujahr());
        bestehend.setFarbe(aenderung.getFarbe());
        bestehend.setKilometerstand(aenderung.getKilometerstand());
        bestehend.setStatus(aenderung.getStatus());

        return repository.save(bestehend);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void fahrzeugLoeschen(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Fahrzeug mit ID " + id + " nicht gefunden");
        }
        repository.deleteById(id);
    }
}
