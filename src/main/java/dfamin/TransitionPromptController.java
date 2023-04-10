package dfamin;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DFA;

public class TransitionPromptController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField inputText;

    @FXML
    private TextField outputText;

    private DFA dfa;

    private List<String> pendingTransition;

    private List<Character> pendingInput;

    public void setConfirmText(String text) {
        confirmButton.setText(text);
    }

    public void setDisableOutputText(boolean isDisabled) {
        outputText.setDisable(isDisabled);
    }

    public void setDfa(DFA dfa) {
        this.dfa = dfa;
    }

    public void setPendingTransition(List<String> pendingTransition) {
        this.pendingTransition = pendingTransition;
    }

    public void setPendingInput(List<Character> pendingInput) {
        this.pendingInput = pendingInput;
    }

    @FXML
    void confirm(ActionEvent event) {
        Character input = inputText.getText().charAt(0);
        pendingInput.add(input);

        Character output = outputText.getText().charAt(0);

        boolean cond = dfa.getTransitionMatrix().get(pendingTransition.get(0)).get(input) == null;
        
        if (cond && dfa.getInAlphabet().contains(input) && dfa.getOutAlphabet().contains(output)) {
            dfa.setTransFunction(pendingTransition.get(0), input, pendingTransition.get(1));
            if (!outputText.isDisable()) {
                dfa.setOutFunction(pendingTransition.get(0), input, output);
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
        setConfirmText("Add");
    }

}
