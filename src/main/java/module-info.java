module chem.chemfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens chem.chemfx to javafx.fxml;
    exports chem.chemfx;
}