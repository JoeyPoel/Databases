package nl.hva.ict.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.hva.ict.MainApplication;
import nl.hva.ict.models.Reiziger;
import nl.hva.ict.views.ReizigersView;
import nl.hva.ict.views.View;

/**
 * ReizigerController
 * @author HBO-ICT
 */
public class ReizigerController extends Controller {

    private final ReizigersView reizigersView;

    public ReizigerController() {
        reizigersView = new ReizigersView();
        Reiziger reiziger = reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem();
        reizigersView.getReizigersViewListView().getSelectionModel().selectedItemProperty()
                .addListener(e -> getItemsInFields());
        reizigersView.getComboReistSamenMet().getSelectionModel().selectedItemProperty()
                .addListener(e -> getItemsComboBox());
        reizigersView.getBtSave().setOnAction(e -> save(reiziger));
        reizigersView.getBtUpdateData().setOnAction(e -> refreshData());
        reizigersView.getBtNew().setOnAction(e -> insert(reiziger));
        reizigersView.getBtDelete().setOnAction(e -> delete(reiziger));
        loadData();
    }

    private void loadData() {
        //haal de waardes op uit de database voor MySQL
//        ObservableList<Reiziger> reizigers = FXCollections.observableArrayList(MainApplication.getMySQLReizigers().getAll());

        // voor NOSQL
        ObservableList<Reiziger> reizigers = FXCollections.observableArrayList(MainApplication.getMongoDBReizigers().getAll());

        reizigersView.getReizigersViewListView().setItems(reizigers);
        reizigersView.getComboReistSamenMet().getSelectionModel().select(null);
    }


    private void refreshData() {
        MainApplication.getMySQLReizigers().reload();
    }

    private void save(Reiziger reiziger) {
        reiziger.setReizigersCode(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getReizigersCode());
        reiziger.setVoornaam(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getVoornaam());
        reiziger.setAchternaam(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getAchternaam());
        reiziger.setPlaats(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getPlaats());
        reiziger.setLand(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getLand());
        reiziger.setPostcode(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getPostcode());
        reiziger.setHoofdreiziger(reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem().getHoofdreiziger());
        MainApplication.getMongoDBReizigers().update(reiziger);
        // bewaar (update) record

    }

    private void delete(Reiziger reiziger) {
        reizigersView.getReizigersViewListView().getItems().remove(reiziger) ;
        MainApplication.getMongoDBReizigers().remove(reiziger);
        // delete dit record
    }

    private void insert(Reiziger reiziger) {
        reizigersView.getReizigersViewListView().getItems().add(reiziger);
        MainApplication.getMongoDBReizigers().add(reiziger);
        //Voeg toe
    }

    private void getItemsInFields() {
        Reiziger currentReiziger = reizigersView.getReizigersViewListView().getSelectionModel().getSelectedItem();
        reizigersView.getTxtReizigersCode().setText(currentReiziger.getReizigersCode());
        reizigersView.getTxtVoornaam().setText(currentReiziger.getVoornaam());
        reizigersView.getTxtAchternaam().setText(currentReiziger.getAchternaam());
        reizigersView.getTxtAdres().setText(currentReiziger.getAdres());
        reizigersView.getTxtPostcode().setText(currentReiziger.getPostcode());
        reizigersView.getTxtPlaats().setText(currentReiziger.getPlaats());
        reizigersView.getTxtLand().setText(currentReiziger.getLand());
    }


    /**
     * Nog niets mee gedaan
     */
    private void getItemsComboBox() {

    }

    /**
     * Methode om de view door te geven zoals dat ook bij OOP2 ging
     * @return View
     */
    @Override
    public View getView() {
        return reizigersView;
    }
}
