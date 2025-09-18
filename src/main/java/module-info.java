module chem.chemfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens chem.chemfx to javafx.fxml;
    exports chem.chemfx;
}