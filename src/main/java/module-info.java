module Locadora.Livros.e.Discos {

    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    exports br.edu.ufersa.locadora;
    opens br.edu.ufersa.locadora.controllers to javafx.fxml;
    opens br.edu.ufersa.locadora.model.entities to javafx.base;
}