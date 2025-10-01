package chem.chemfx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;

public class ChemFXRxnController {

    @FXML
    private TabPane formulaTabPane;

    // Ideal Gas
    @FXML private TextField pressureField;
    @FXML private TextField volumeField;
    @FXML private TextField temperatureField;
    @FXML private TextField molesField;

    // Equilibrium
    @FXML private TextField eqReactant1Field;
    @FXML private TextField eqReactant2Field;
    @FXML private TextField eqProduct1Field;
    @FXML private TextField eqProduct2Field;
    @FXML private Label equilibriumResultLabel; // optional label for Kc display

    @FXML
    private void calculate() {
        Tab selectedTab = formulaTabPane.getSelectionModel().getSelectedItem();

        if (selectedTab == null) return;

        String tabId = selectedTab.getId();

        switch (tabId) {
            case "idealGasTab":
                calculateIdealGas();
                break;

            case "equilibriumTab":
                calculateEquilibrium();
                break;
        }
    }

    private void calculateIdealGas() {
        try {
            double P = parseOrZero(pressureField);
            double V = parseOrZero(volumeField);
            double T = parseOrZero(temperatureField);
            double n = parseOrZero(molesField);

            final double R = 0.08206; // L·atm/(mol·K)

            // Fill the missing field
            if (pressureField.getText().isEmpty()) pressureField.setText(String.valueOf(n*R*T/V));
            if (volumeField.getText().isEmpty()) volumeField.setText(String.valueOf(n*R*T/P));
            if (temperatureField.getText().isEmpty()) temperatureField.setText(String.valueOf(P*V/(n*R)));
            if (molesField.getText().isEmpty()) molesField.setText(String.valueOf(P*V/(R*T)));

        } catch (NumberFormatException ignored) {}
    }

    private void calculateEquilibrium() {
        try {
            double A = parseOrZero(eqReactant1Field);
            double B = parseOrZero(eqReactant2Field);
            double C = parseOrZero(eqProduct1Field);
            double D = parseOrZero(eqProduct2Field);

            // Stoichiometric coefficients a=b=c=d=1 for simplicity
            double Kc = (C * D) / (A * B);

            equilibriumResultLabel.setText(String.valueOf(Kc));

        } catch (NumberFormatException e) {
            equilibriumResultLabel.setText("Invalid input");
        }
    }

    private double parseOrZero(TextField tf) {
        if (tf.getText() == null || tf.getText().isEmpty()) return 0.0;
        return Double.parseDouble(tf.getText());
    }
}
