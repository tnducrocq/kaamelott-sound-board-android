package fr.tnducrocq.kaamelott_soundboard.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 06/06/2017.
 */

public enum Person {

    Arthur("Arthur", "arthur"),//
    Bohort("Bohort", "bohort"),//
    Caius_Camillus("Caius", "caius_camillus"),//
    Dame_seli("Dame Séli", "dame_seli"),//
    Elias_de_kelliwich("Elias de Kelliwic'h", "elias_de_kelliwich"),//
    Gauvain("Gauvain", "gauvain"),//
    Guenievre("Guenièvre", "guenievre"),//
    Guethenoc("Guethenoc", "guethenoc"),//
    Interprete("L'Interprète", "interprete"),//
    Kadoc("Kadoc", "kadoc"),//
    Karadoc("Karadoc", "karadoc"),//
    Lancelot("Lancelot", "lancelot"),//
    Leodagan("Léodagan", "leodagan"),//
    Loth_d_orcanie("Loth", "loth_d_orcanie"),//
    MaitreDarme("Le Maître d'armes", "maitre_d_armes"),//
    Merlin("Merlin", "merlin"),//
    Perceval("Perceval", "perceval"),//
    Pere_blaise("Père Blaise", "pere_blaise"),//
    Roi_burgonde("Le Roi Burgonde", "roi_burgonde"),//
    Roparzh("Roparzh", "roparzh"),//
    Tavernier("Le tavernier", "tavernier"),//
    Venec("Venec", "venec"),//
    Yvain("Yvain", "yvain"),//

    Other("Other", "kaamelott");

    private String name;
    private String asset;

    private Person(String name, String asset) {
        this.name = name;
        this.asset = asset;
    }

    public String getName() {
        return name;
    }

    public String getAssetShort() {
        return asset + "_short";
    }

    public String getAssetLong() {
        return asset + "_long";
    }

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<String, Person> lookup = new HashMap<>();

    static {
        for (Person d : Person.values()) {
            lookup.put(d.getName(), d);
        }
    }

    public static Person get(String name) {
        Person value = lookup.get(name);
        if (value != null) {
            return value;
        }
        return Person.Other;
    }
}
