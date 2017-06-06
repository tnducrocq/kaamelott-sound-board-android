package fr.tnducrocq.kaamelott_soundboard.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 06/06/2017.
 */

public enum Person {

    Arthur("Arthur", "arthur_150x150"),//
    Bohort("Bohort", "bohort_150x150"),//
    Seli("Séli", "dame_seli_150x150"),//
    Gauvain("Gauvain", "gauvain_150x150"),//
    Guenievre("Guenièvre", "guenievre_150x150"),//
    Guethenoc("Guethenoc", "guethenoc_150x150"),//
    Kadoc("Kadoc", "kadoc_150x150"),//
    Karadoc("Karadoc", "karadoc_150x150"),//
    Lancelot("Lancelot", "lancelot_150x150"),//
    Le_Tavernier("Le tavernier", "le_tavernier_150x150"),//
    Leodagan("Léodagan", "leodagan_150x150"),//
    MaitreDarme("Le Maître d’armes", "maitre_d_armes_150x150"),//
    Merlin("Merlin", "merlin_150x150"),//
    Perceval("Perceval", "perceval_150x150"),//
    Pere_blaise("Père Blaise", "pere_blaise_150x150"),//
    Roi_burgonde("Le Roi Burgonde", "roi_burgonde_150x150"),//
    Roparzh("Roparzh", "roparzh_150x150"),//
    Yvain("Yvain", "yvain_150x150"),//

    Other("Other", "kaamelott_150x150");

    String name;
    String asset;

    private Person(String name, String asset) {
        this.name = name;
        this.asset = asset;
    }

    public String getName() {
        return name;
    }

    public String getAsset() {
        return asset;
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
