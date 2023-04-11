package dfamin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
    private MenuItem minimizeItem;

    @FXML
    private Button stateButton;

    @FXML
    private Button initialStateButton;

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

    @FXML
    private AnchorPane machineView;

    private DFA dfa;

    private int stateCount;

    private Map<Circle, Text> statesView;

    private Map<QuadCurve, Tuple<String, Character>> transitionsView;

    private Map<QuadCurve, Tuple<Circle, Text>> transGraphics;

    private List<Circle> pendingTransition;

    private Circle initialStateGraphic;

    @FXML
    void createMealyDFA(ActionEvent event) {
        dfa = new MealyDFA(new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new Hashtable<>(),
                new Hashtable<>());
        treeView.setRoot(new TreeItem<>("Mealy Automaton"));
        updateAlphabets();
        areButtonsDisabled(false);
        reset();
    }

    @FXML
    void createMooreDFA(ActionEvent event) {
        dfa = new MooreDFA(new ArrayList<>(), null, new ArrayList<>(), new ArrayList<>(), new Hashtable<>(),
                new Hashtable<>());
        treeView.setRoot(new TreeItem<>("Moore Automaton"));
        updateAlphabets();
        areButtonsDisabled(false);
        reset();
    }

    public void reset() {
        stateCount = 0;
        pendingTransition.clear();
        statesView.clear();
        transitionsView.clear();
        transGraphics.clear();
        machineView.getChildren().clear();
    }

    private void areButtonsDisabled(boolean isDisabled) {
        minimizeItem.setDisable(isDisabled);
        stateButton.setDisable(isDisabled);
        transitionButton.setDisable(isDisabled);
        addInputButton.setDisable(isDisabled);
        removeInputButton.setDisable(isDisabled);
        addOutputButton.setDisable(isDisabled);
        removeOutputButton.setDisable(isDisabled);
        initialStateButton.setDisable(isDisabled);
    }

    private void updateAlphabets() {
        inputLabel.setText(dfa.getInAlphabet().toString());
        outputLabel.setText(dfa.getOutAlphabet().toString());
    }

    @FXML
    private void minimizeFDA(ActionEvent event) {
        if (dfa != null) {
            dfa.minimizeFDA();
        }
    }

    @FXML
    private void addInput(ActionEvent event) throws IOException {
        openPrompt("Add", true, true);
    }

    @FXML
    public void removeInput(ActionEvent event) throws IOException {
        openPrompt("Remove", true, false);
    }

    @FXML
    private void addOutput(ActionEvent event) throws IOException {
        openPrompt("Add", false, true);
    }

    @FXML
    private void removeOutput(ActionEvent event) throws IOException {
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
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();

            updateAlphabets();
        }
    }

    // This seriously NEEDS refactoring (Everything NEEDS refactoring, heck).
    // ALERT! ALERT! THIS WHOLE PROGRAM IS SPAGHETTI CODE! (I needed to get this
    // done really quickly lol).
    @FXML
    void machineViewClick(MouseEvent event) throws IOException {
        Node n = event.getPickResult().getIntersectedNode();
        if (stateButton.isFocused()) {
            if (event.getButton() == MouseButton.PRIMARY) {
                addState(event.getX(), event.getY());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (n instanceof Circle) {
                    deleteState(n);
                }
            }
        } else if (transitionButton.isFocused()) {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (pendingTransition.size() < 2 && n instanceof Circle) {
                    pendingTransition.add((Circle) n);
                } else if (pendingTransition.size() >= 2) {
                    Character c = openTransitionPrompt();
                    if (c != null && c != ' ') {
                        addTransitionInView(c, event.getX(), event.getY());
                    }
                    pendingTransition.clear();
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (n instanceof QuadCurve) {
                    removeTransition((QuadCurve) n);
                }
            } else {
                System.out.println(dfa.getStates().toString());
                System.out.println(dfa.getTransitionMatrix().toString());
                if (dfa instanceof MealyDFA) {
                    System.out.println(((MealyDFA) dfa).getOutputMatrix().toString());
                } else {
                    System.out.println(((MooreDFA) dfa).getOutFunctions().toString());
                }
                System.out.println(dfa.getInitialState());
            }
        } else if (initialStateButton.isFocused()) {
            if (event.getButton() == MouseButton.PRIMARY && n instanceof Circle) {
                String newInitialState = statesView.get(n).getText().split("/")[0];
                dfa.setInitialState(newInitialState);

                if (dfa.getInitialState() == newInitialState) {
                    if (initialStateGraphic != null) {
                        initialStateGraphic.setFill(Color.WHITE);
                    }
                    ((Circle) n).setFill(Color.GREY);
                    initialStateGraphic = ((Circle) n);
                }
            }
        }
    }

    @FXML
    void clearTransition(MouseEvent event) {
        pendingTransition.clear();
    }

    private Character openTransitionPrompt() throws IOException {
        ArrayList<Character> pendingInput = new ArrayList<>();
        if (dfa != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transition-prompt.fxml"));
            Parent root = loader.load();

            TransitionPromptController controller = ((TransitionPromptController) loader.getController());
            controller.setDfa(dfa);
            ArrayList<String> ls = new ArrayList<>();

            pendingTransition.forEach(circle -> {
                ls.add(statesView.get(circle).getText().split("/")[0]);
            });

            controller.setPendingTransition(ls);
            controller.setPendingInput(pendingInput);

            if (dfa instanceof MooreDFA) {
                controller.setDisableOutputText(true);
            } else {
                controller.setDisableOutputText(false);
            }

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
        }
        if (pendingInput.size() > 0) {
            return pendingInput.get(0);
        } else {
            return null;
        }
    }

    private void addState(Double x, Double y) throws IOException {
        if (dfa != null) {
            String id = String.valueOf("q" + stateCount);
            String gText = id;

            if (dfa instanceof MooreDFA) {
                openStatePrompt(id);
                if (((MooreDFA) dfa).getOutFunction(id) == null) {
                    return;
                }
                gText += "/" + ((MooreDFA) dfa).getOutFunction(id);
            }

            dfa.addState(id, new Hashtable<>(), new Hashtable<>());
            stateCount++;

            Circle c = new Circle(x, y, 25);
            c.setFill(Color.WHITE);
            c.setStroke(Color.BLACK);

            Text t = new Text(x - 8, y + 4, gText);
            t.setDisable(true);

            statesView.put(c, t);
            machineView.getChildren().addAll(c, t);
        }
    }

    private void openStatePrompt(String state) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("state-prompt.fxml"));
        Parent root = loader.load();

        StatePromptController controller = ((StatePromptController) loader.getController());
        controller.setDfa(dfa);
        controller.setState(state);

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.showAndWait();
    }

    private void deleteState(Node n) {
        if (dfa != null) {
            String id = statesView.get(n).getText().split("/")[0];
            dfa.deleteState(id);

            machineView.getChildren().remove(n);
            machineView.getChildren().remove(statesView.get(n));

            statesView.remove(n);

            ArrayList<QuadCurve> deletedElements = new ArrayList<>();

            for (QuadCurve q : transitionsView.keySet()) {
                Tuple<String, Character> tuple = transitionsView.get(q);
                if (tuple.getFirst() == id) {
                    deletedElements.add(q);
                }
            }

            deletedElements.forEach(elem -> {
                transitionsView.remove(elem);
            });
        }
    }

    private void addTransitionInView(Character input, double x, double y) {
        Circle sourceCircle = pendingTransition.get(0);
        Circle destinationCircle = pendingTransition.get(1);

        String state = statesView.get(sourceCircle).getText().split("/")[0];

        QuadCurve curv = null;

        for (QuadCurve q : transitionsView.keySet()) {
            Tuple<String, Character> t = transitionsView.get(q);
            String st = t.getFirst();
            Character in = t.getSecond();
            if (state == st && in == input) {
                curv = q;
                break;
            }
        }

        if (dfa.getTransitionMatrix().get(state).get(input) != null) {
            if (curv == null) {
                Double sx = sourceCircle.getCenterX();
                Double sy = sourceCircle.getCenterY();
                Double ex = destinationCircle.getCenterX();
                Double ey = destinationCircle.getCenterY();

                Double[] out1 = calculateDistances(25.0, sx, sy, x, y);
                Double[] out2 = calculateDistances(25.0, ex, ey, x, y);

                QuadCurve curve = new QuadCurve(sx + out1[0], sy - out1[1], x, y, ex + out2[0], ey - out2[1]);
                curve.setFill(null);
                curve.setStroke(Color.BLACK);
                curve.setStrokeWidth(2);

                transitionsView.put(curve, new Tuple<String, Character>(state, input));

                Circle pointer = new Circle(ex + out2[0], ey - out2[1], 5);
                pointer.setFill(Color.WHITE);
                pointer.setStroke(Color.BLACK);
                pointer.setStrokeWidth(1);

                Character output;

                Double point1 = sx + (ex - sx) / 2;
                Double point2 = sy + (ey - sy) / 2;
                Double point11 = point1 + (x - point1) * 0.6;
                Double point22 = point2 + (y - point2) * 0.6;

                Text text;

                if (dfa instanceof MealyDFA) {
                    output = ((MealyDFA) dfa).getOutFunction(state, input);
                    text = new Text(point11, point22, input + "/" + output);
                } else {
                    output = ((MooreDFA) dfa).getOutFunction(state);
                    text = new Text(point11, point22, input + "");
                }

                transGraphics.put(curve, new Tuple<>(pointer, text));

                machineView.getChildren().addAll(curve, pointer, text);
            }
        }
    }

    public Double[] calculateDistances(Double radius, Double sx, Double sy, Double ex, Double ey) {
        Double distX = ex - sx;
        Double distY = ey - sy;

        Double pointDist = Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2));
        Double angle1 = Math.acos(distX / pointDist);
        Double angle2 = Math.asin(distY / pointDist);

        Double newSX = radius * Math.cos(angle1);
        Double newSY = radius * Math.sin(angle2 - Math.PI);

        Double[] output = { newSX, newSY };

        return output;
    }

    private void removeTransition(QuadCurve curve) {
        Tuple<String, Character> t = transitionsView.get(curve);
        dfa.getTransitionMatrix().get(t.getFirst()).remove(t.getSecond());

        if (dfa instanceof MealyDFA) {
            ((MealyDFA) dfa).getOutputMatrix().get(t.getFirst()).remove(t.getSecond());
        }
        
        Tuple<Circle, Text> graphics = transGraphics.get(curve);
        graphics.getFirst();
        graphics.getSecond();

        machineView.getChildren().removeAll(graphics.getFirst(), graphics.getSecond(), curve);

        transitionsView.remove(curve);
        transGraphics.remove(curve);
    }

    @FXML
    void initialize() {
        stateCount = 0;
        areButtonsDisabled(true);

        pendingTransition = new ArrayList<>();
        statesView = new Hashtable<>();
        transitionsView = new Hashtable<>();
        transGraphics = new Hashtable<>();
    }

    protected class Tuple<T, U> {
        private T first;
        private U second;

        public Tuple(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public void setFirst(T first) {
            this.first = first;
        }

        public U getSecond() {
            return second;
        }

        public void setSecond(U second) {
            this.second = second;
        }
    }
}
