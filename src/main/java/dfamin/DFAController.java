package dfamin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import model.DFA;
import model.MealyDFA;
import model.MooreDFA;

public class DFAController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Label inputLabel;

    @FXML
    private Label outputLabel;

    @FXML
    private Button stateButton;

    @FXML
    private Button transitionButton;

    @FXML
    private Button addInputButton;
    
    @FXML
    private Button addOutputButton;
    
    @FXML
    private Button removeInputButton;
    
    @FXML
    private Button removeOutputButton;
    
    private DFA dfa;

    public DFA getDfa() {
        return dfa;
    }

    @FXML
    void createMealyDFA(ActionEvent event) {
        dfa = new MealyDFA(new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new Hashtable<>(),
                new Hashtable<>());
        treeView.setRoot(new TreeItem<>("Mealy Automaton"));
        updateAlphabets();
        areButtonsDisabled(false);
    }

    @FXML
    void createMooreDFA(ActionEvent event) {
        dfa = new MooreDFA(new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new Hashtable<>(),
                new Hashtable<>());
        treeView.setRoot(new TreeItem<>("Moore Automaton"));
        updateAlphabets();
        areButtonsDisabled(false);
    }

    private void areButtonsDisabled(boolean isDisabled) {
        stateButton.setDisable(isDisabled);
        transitionButton.setDisable(isDisabled);
        addInputButton.setDisable(isDisabled);
        removeInputButton.setDisable(isDisabled);
        addOutputButton.setDisable(isDisabled);
        removeOutputButton.setDisable(isDisabled);
    }

    public void updateAlphabets() {
        inputLabel.setText(dfa.getInAlphabet().toString());
        outputLabel.setText(dfa.getOutAlphabet().toString());
    }

    public void minimizeFDA() {

    }

    @FXML
    public void addInput(ActionEvent event) throws IOException {
        openPrompt("Add", true, true);
    }

    @FXML
    public void removeInput() throws IOException {
        openPrompt("Remove", true, false);
    }

    @FXML
    public void addOutput() throws IOException {
        openPrompt("Add", false, true);
    }

    @FXML
    public void removeOutput() throws IOException {
        openPrompt("Remove", false, false);
    }

    private void openPrompt(String text, boolean isChangingInput, boolean isAdding) throws IOException {
        if (dfa != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("prompt.fxml"));
            Parent root = loader.load();
    
            PromptController controller = ((PromptController) loader.getController());
            controller.setConfirmText(text);
            controller.setDfa(dfa);
            controller.setChangingInput(isChangingInput);
            controller.setAdding(isAdding);
    
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.showAndWait();
    
            updateAlphabets();
        }
    }

    public void addState() {

    }

    public void removeState() {

    }

    public void addTransition() {

    }

    public void removeTransition() {

    }

    @FXML
    void initialize() {
        areButtonsDisabled(true);
    }

}
