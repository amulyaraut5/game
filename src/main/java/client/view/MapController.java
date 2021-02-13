package client.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.ArrayList;


public class MapController {
    private final ObservableList<String> maps = FXCollections.observableArrayList();
    LobbyController lobbyController;

    @FXML
    private ImageView mapImageView;
    @FXML
    private ListView<String> mapsListView;
    @FXML
    private Button selectButton;

    public void setMaps(ArrayList<String> availableMaps, LobbyController lobbyController) {
        this.lobbyController = lobbyController;
        maps.addAll(availableMaps);
        mapsListView.setItems(maps);

        mapsListView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldMap, newMap) -> {
                    try {
                        InputStream path = getClass().getResourceAsStream("/maps/" + newMap + ".PNG");
                        Image mapImage = new Image(path, 200, 200, true, true);
                        mapImageView.setImage(mapImage);
                        mapImageView.setVisible(true);
                    } catch (NullPointerException e) {
                        mapImageView.setVisible(false);
                    }
                    selectButton.setVisible(true);
                }
        );
    }

    @FXML
    void selectClicked() {
        int mapIndex = mapsListView.getSelectionModel().getSelectedIndex();
        lobbyController.mapSelected(maps.get(mapIndex));
    }
}
