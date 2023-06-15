package nl.hva.ict.data.MongoDB;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import nl.hva.ict.MainApplication;
import nl.hva.ict.models.Reiziger;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;

/**
 * Haal alle reizigers van Big Five Safari op uit de NoSQL database
 */
public class MongoReizigers extends MongoDB {

    private final List<Reiziger> reizigers;

    public MongoReizigers() {
        // init de arraylist
        reizigers = new ArrayList<>();

        // startup methode
        load();
    }

    @Override
    public void add(Object object) {
        if (object instanceof Reiziger) {
            Reiziger reiziger = (Reiziger) object;
            Document reizigerDocument = new Document("Reizigers code", reiziger.getReizigersCode())
                    .append("Voornaam", reiziger.getVoornaam())
                    .append("Achternaam", reiziger.getAchternaam())
                    .append("Adres", reiziger.getAdres())
                    .append("Plaats", reiziger.getPlaats())
                    .append("Land", reiziger.getLand())
                    .append("Postcode", reiziger.getPostcode())
                    .append("Hoofdreiziger", reiziger.getHoofdreiziger())
                    .append("Reizigers code", reiziger.getReizigersCode());

            collection.insertOne(reizigerDocument);
        }
        // Add the new document to the collection
    }


    @Override
    public void update(Object object) {
        if (object instanceof Reiziger) {
            Reiziger reiziger = (Reiziger) object;
            Bson filter = Filters.eq("Reizigers code", reiziger.getReizigersCode());
            Document reizigerDocument = new Document("Reizigers code", reiziger.getReizigersCode())
                    .append("Voornaam", reiziger.getVoornaam())
                    .append("Achternaam", reiziger.getAchternaam())
                    .append("Adres", reiziger.getAdres())
                    .append("Plaats", reiziger.getPlaats())
                    .append("Land", reiziger.getLand())
                    .append("Postcode", reiziger.getPostcode())
                    .append("Hoofdreiziger", reiziger.getHoofdreiziger())
                    .append("Reizigers code", reiziger.getReizigersCode());
            Bson update = new Document("$set", reizigerDocument);
            collection.updateOne(filter, update);
        }
        // Replace the existing document with the updated document based on the Reizigers code field
    }

    @Override
    public void remove(Object object) {
        if (object instanceof Reiziger) {
            Reiziger reiziger = (Reiziger) object;
            Bson filter = Filters.eq("Reizigers code", reiziger.getReizigersCode());
            collection.deleteOne(filter);
        }
        // Remove the document from the collection based on the Reizigers code field
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
     * Laad bij startup
     */
    public void load() {

        // Als je geen NoSQL server hebt opgegeven gaat de methode niet verder anders zou je een nullpointer krijgen
        if (MainApplication.getNosqlHost().equals(""))
            return;

        // Selecteer de juiste collecton in de NoSQL server
        this.selectedCollection("reiziger");

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
