package org.panda_lang.lily;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.panda_lang.panda.Panda;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Interface implements Initializable {

    @FXML private MenuItem menuFileOpenFile;
    @FXML private MenuItem menuFileOpenFolder;
    @FXML private MenuItem menuFileExit;
    @FXML private MenuItem menuHelpAbout;
    @FXML private TreeView<String> filesTree;
    @FXML private TabPane tabPane;

    private Explorer tree;
    private List<TabInterface> tabs;
    private TabInterface currentTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Lily.instance.initAnInterface(this);

        menuFileOpenFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(Lily.instance.getStage());
            if (file != null) {
                tree.open(file);
            }
        });
        menuFileOpenFolder.setOnAction(event -> {
            DirectoryChooser fileChooser = new DirectoryChooser();
            File file = fileChooser.showDialog(Lily.instance.getStage());
            if (file != null) {
                tree.open(file);
            }
        });
        menuFileExit.setOnAction(event -> System.exit(-1));
        menuHelpAbout.setOnAction(event -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(Lily.instance.getStage());
            VBox dialogVBox = new VBox(20);
            Text text = new Text("Lily " + Panda.PANDA_VERSION);
            dialogVBox.getChildren().add(text);
            Scene dialogScene = new Scene(dialogVBox, text.getText().length() * 6.5, 30);
            dialog.setScene(dialogScene);
            dialog.show();
        });

        this.tabs = new ArrayList<>();
        this.tree = new Explorer(filesTree);
        this.tree.open(new File("./"));
    }

    public void displayFile(File file) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tab.fxml"));
            loader.load();
            TabInterface ti = loader.getController();
            tabs.add(ti);
            ti.run(tabPane, file);
            currentTab = ti;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TabInterface getCurrentTab() {
        return currentTab;
    }

    public List<TabInterface> getTabs() {
        return tabs;
    }

}