module Locadora.Livros.e.Discos {

    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    opens br.edu.ufersa.locadora to javafx.graphics;
    opens br.edu.ufersa.locadora.controllers to javafx.fxml;
    opens br.edu.ufersa.locadora.model.entities to javafx.base;
}