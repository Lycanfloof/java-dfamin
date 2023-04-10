package dfamin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DFA;
import model.MooreDFA;

public class StatePromptController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    TextField textField;

    @FXML
    Button confirmButton;

    private DFA dfa;

    private String state;

    public void setConfirmText(String text) {
        confirmButton.setText(text);
    }

    public void setDfa(DFA dfa) {
        this.dfa = dfa;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Needs to be refactored.
    @FXML
    void confirm(ActionEvent event) {
        Character c = (!textField.getText().isBlank()) ? textField.getText().charAt(0) : null;
        if (c != null && dfa.getOutAlphabet().contains(c)) {
            ((MooreDFA) dfa).setOutFunctionUnsafe(state, ' ', c);
            close(event);
        }
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = ((Stage) confirmButton.getScene().getWindow());
        stage.close();
    }

    @FXML
    void initialize() {
        setConfirmText("Add");
    }

}
