module Locadora.Livros.e.Discos {

    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens br.edu.ufersa.locadora to javafx.graphics;
    opens br.edu.ufersa.locadora.controllers to javafx.fxml;
}