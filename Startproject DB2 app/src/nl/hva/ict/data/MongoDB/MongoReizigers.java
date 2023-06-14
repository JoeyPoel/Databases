package nl.hva.ict.data.MongoDB;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import nl.hva.ict.MainApplication;
import nl.hva.ict.models.Reiziger;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

/**
 * Haal alle reizigers van Big Five Safari op uit de NoSQL database
 */
public class MongoReizigers extends MongoDB {

    private final List<Reiziger> reizigers;

    /**
     * Construtor
     */
    public MongoReizigers() {

        // init de arraylist
        reizigers = new ArrayList<>();

        // startup methode
        load();
    }

    /**
     * Haal alle objecten op. Niet gebruikt in deze class maar door de interface data wel verplicht
     * @return een lijst
     */
    @Override
    public List getAll() {
        return reizigers;
    }

    /**
     * Haal 1 object op. Niet gebruikt in deze class maar door de interface data wel verplicht
     * @return een object
     */
    @Override
    public Object get() {
        return null;
    }

    /**
     * Voeg een object toe aan de arraylist. Niet gebruikt in deze class maar door de interface data wel verplicht
     */
    @Override
    public void add(Object object) {
        reiziger.insertOne(document);
        // Voeg het nieuwe document toe aan de collectie
    }


    /**
     * Update een object toe aan de arraylist. Niet gebruikt in deze class maar door de interface data wel verplicht
     */
    @Override
    public void update(Object object) {
        reiziger.replaceOne(new Document("_id", document.get("_id")), document);
        // De _id sleutel is de unieke identificator van het document
        // Hiermee wordt het bestaande document vervangen door het bijgewerkte document
    }

    /**
     * Update een object toe aan de arraylist. Niet gebruikt in deze class maar door de interface data wel verplicht
     */
    @Override
    public void remove(Object object) {
        reiziger.deleteOne(new Document("_id", documentId));
        // Verwijder het document met de opgegeven _id uit de collectie
    }

    /**
     * Laad bij startup
     */
    public void load() {

        // Als je geen NoSQL server hebt opgegeven gaat de methode niet verder anders zou je een nullpointer krijgen
        if (MainApplication.getNosqlHost().equals(""))
            return;

        // Selecteer de juiste collecton in de NoSQL server
        this.selectedCollection("reizigers");

        // Haal alles op uit deze collection en loop er 1 voor 1 doorheen
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            // Zolang er data is
            while (cursor.hasNext()) {
                // warning Java is case sensitive
                // Haal alle velden per record
                Document tempReiziger = cursor.next();
                String reizigerCode = (String) tempReiziger.get("Reizigers code");
                String voornaam = (String) tempReiziger.get("Voornaam");
                String achternaam = (String) tempReiziger.get("Achternaam");
                String adres = (String) tempReiziger.get("Adres");
                String postcode = (String) tempReiziger.get("Postcode");
                String plaats = (String) tempReiziger.get("Plaats");
                String land = (String) tempReiziger.get("Land");
                String hoofdreiziger = (String) tempReiziger.get("Hoofdreiziger");

                // Maak een nieuw object en voeg deze toe aan de arraylist
                reizigers.add(new Reiziger(reizigerCode, voornaam, achternaam, adres, postcode, plaats, land, hoofdreiziger));
            }
        } finally {
            // Sluit de stream
            cursor.close();
        }
    }
}
