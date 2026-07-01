package ch.example.fahrzeugverwaltung.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Year;

/**
 * Repräsentiert ein Fahrzeug in der Flotte.
 */
@Entity
@Table(name = "fahrzeuge")
public class Fahrzeug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kennzeichen darf nicht leer sein")
    @Column(nullable = false, unique = true, length = 20)
    private String kennzeichen;

    @NotBlank(message = "Marke darf nicht leer sein")
    @Column(nullable = false, length = 50)
    private String marke;

    @NotBlank(message = "Modell darf nicht leer sein")
    @Column(nullable = false, length = 50)
    private String modell;

    @NotNull(message = "Baujahr ist erforderlich")
    @Min(value = 1950, message = "Baujahr muss nach 1950 sein")
    @Max(value = 2100, message = "Baujahr ist ungültig")
    @Column(nullable = false)
    private Integer baujahr;

    @Column(length = 30)
    private String farbe;

    @Column(name = "kilometerstand")
    private Integer kilometerstand;

    @Column(length = 20)
    private String status = "VERFUEGBAR";

    public Fahrzeug() {
    }

    public Fahrzeug(String kennzeichen, String marke, String modell, Integer baujahr,
                     String farbe, Integer kilometerstand, String status) {
        this.kennzeichen = kennzeichen;
        this.marke = marke;
        this.modell = modell;
        this.baujahr = baujahr;
        this.farbe = farbe;
        this.kilometerstand = kilometerstand;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    public Integer getBaujahr() {
        return baujahr;
    }

    public void setBaujahr(Integer baujahr) {
        this.baujahr = baujahr;
    }

    public String getFarbe() {
        return farbe;
    }

    public void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public Integer getKilometerstand() {
        return kilometerstand;
    }

    public void setKilometerstand(Integer kilometerstand) {
        this.kilometerstand = kilometerstand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static int aktuellesJahr() {
        return Year.now().getValue();
    }
}
