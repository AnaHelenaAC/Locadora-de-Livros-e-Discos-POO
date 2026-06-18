module Locadora.Livros.e.Discos {

    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    exports br.edu.ufersa.locadora;
    opens br.edu.ufersa.locadora.controllers to javafx.fxml;
}