package nl.hva.ict.data.MongoDB;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import nl.hva.ict.MainApplication;
import nl.hva.ict.models.Landen;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;
import static java.lang.Integer.parseInt;


/**
 * Landen informatie ophalen van de MongoDB
 */
public class MongoLandeninformatie extends MongoDB {

    private final List<Landen> landen;

    /**
     * Constructor
     */
    public MongoLandeninformatie() {
        // Init arraylist
        landen = new ArrayList<>();
    }

    /**
     * Haal alle landen op die in de arraylijst zitten
     * @return arraylijst met landen
     */
    @Override
    public List getAll() {
        return landen;
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

    }

    /**
     * Update een object toe aan de arraylist. Niet gebruikt in deze class maar door de interface data wel verplicht
     */
    @Override
    public void update(Object object) {

    }

    /**
     * Verwijder een object toe aan de arraylist. Niet gebruikt in deze class maar door de interface data wel verplicht
     */
    @Override
    public void remove(Object object) {

    }

    /**
     * Haal alle informatie op uit de NoSQL server over welke landen een bepaalde taal spreken. Gebruik hiervoor aggregation.
     * Zet het resultaat in de arraylist
     * @param taal Welke taal wil je weten
     * @param alleenAfrika filter het resultaat zodat wel of niet alleen afrikaanse landen terug komen
     */
    public void wieSpreekt(String taal, boolean alleenAfrika) {
        // Check if NoSQL server host is specified, otherwise return
        if (MainApplication.getNosqlHost().equals("")) {
            return;
        }

        // Reset ArrayList
        this.landen.clear();

        // Select collection
        this.selectedCollection("landen");

        // Aggregation function in MongoDB
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                new Document("languages.name", taal))));

        if (alleenAfrika) {
            result = collection.aggregate(Arrays.asList(new Document("$match",
                    new Document("$and", Arrays.asList(new Document("region", "Africa"),
                            new Document("languages.name", taal))))));
        }

        // Create models and add results to ArrayList
        for (Document land : result) {
            this.landen.add(new Landen(land.get("name").toString(), land.get("capital").toString()));
        }
    }

    /**
     * Haal alle informatie op uit de NoSQL server in welke landen je met een bepaalde valuta kan betalen. Gebruik hiervoor aggregation.
     * Zet het resultaat in de arraylist
     * @param valuta Welke valuta wil je weten
     * @param alleenAfrika filter het resultaat zodat wel of niet alleen afrikaanse landen terug komen
     */
    public void waarBetaalJeMet(String valuta, boolean alleenAfrika) {
        // Als je geen NoSQL server hebt opgegeven gaat de methode niet verder anders zou je een nullpointer krijgen
        if (MainApplication.getNosqlHost().equals(""))
            return;

        // reset arraylist
        this.landen.clear();

        // selecteer collection
        this.selectedCollection("landen");

        // Aggregation functie in Mongo
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                new Document())));

        if (alleenAfrika) {
            result = collection.aggregate(Arrays.asList(new Document("$match",
                    new Document("$and", Arrays.asList(new Document("region", "Africa"),
                            new Document("currencies.name", valuta))))));
        }

        // Maak models en voeg resultaat toe aan arraylist
        for (Document land : result) {
            this.landen.add(new Landen(land.get("name").toString(), land.get("capital").toString()));

        }
    }

    /**
     * Welke landen zijn er in welk werelddeel. Haal deze informatie uit de database
     * . Gebruik hiervoor aggregation.
     * Zet het resultaat in de arraylist
     * @param werelddeel Welke valuta wil je weten
     */
    public void welkeLandenZijnErIn(String werelddeel) {
        // Als je geen NoSQL server hebt opgegeven gaat de methode niet verder anders zou je een nullpointer krijgen
        if (MainApplication.getNosqlHost().equals(""))
            return;

        // reset arraylist
        this.landen.clear();

        // selecteer collection
        this.selectedCollection("landen");

        // Aggregation functie in Mongo
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                new Document("$or", Arrays.asList(new Document("region", werelddeel),
                        new Document("subregion", werelddeel))))));

        // Maak models en voeg resultaat toe aan arraylist
        for (Document land : result) {
            this.landen.add(new Landen(land.get("name").toString(), land.get("capital").toString()));
        }
    }

    /**
     * Hoeveel inwoners heeft Oost-Afrika?. Haal deze informatie uit de database en gebruik hiervoor aggregation.
     */
    public int hoeveelInwonersOostAfrika() {
        // reset arraylist
        this.landen.clear();

        // Calculate total population
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
                        new Document("subregion", "Eastern Africa")),
                new Document("$group",
                        new Document("_id", 0L)
                                .append("totalAmount",
                                        new Document("$sum", "$population"))),
                new Document("$project",
                        new Document("_id", 0L)
                                .append("totalAmount",
                                        new Document("$sum", "$totalAmount")))));
        return parseInt(result.first().get("totalAmount").toString());
    }
}