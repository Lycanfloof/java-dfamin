package dfamin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DFA;

public class PromptController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    TextField textField;

    @FXML
    Button confirmButton;

    private DFA dfa;

    private boolean isChangingInput;

    private boolean isAdding;

    public void setConfirmText(String text) {
        confirmButton.setText(text);
    }

    public void setDfa(DFA dfa) {
        this.dfa = dfa;
    }

    public void setChangingInput(boolean isChangingInput) {
        this.isChangingInput = isChangingInput;
    }

    public void setAdding(boolean isAdding) {
        this.isAdding = isAdding;
    }

    // Needs to be refactored.
    @FXML
    void confirm(ActionEvent event) {
        boolean isValid = textField.getText().length() == 1;
        if (isValid && isAdding) {
            if (isChangingInput) {
                dfa.addToInAlphabet(textField.getText().charAt(0));
            } else {
                dfa.addToOutAlphabet(textField.getText().charAt(0));
            }
        } else if (isValid) {
            if (isChangingInput) {
                dfa.removeFromInAlphabet(textField.getText().charAt(0));
            } else {
                dfa.removeFromOutAlphabet(textField.getText().charAt(0));
            }
        }
        close(event);
    }

    @FXML
    void close(ActionEvent event) {
        Stage stage = ((Stage) confirmButton.getScene().getWindow());
        stage.close();
    }

    @FXML
    void initialize() {

    }

}
