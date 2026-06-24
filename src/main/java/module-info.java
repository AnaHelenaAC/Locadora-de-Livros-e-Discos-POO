module Locadora.Livros.e.Discos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens br.edu.ufersa.locadora.controllers to javafx.fxml;
    opens br.edu.ufersa.locadora.model.entities to javafx.base;
    opens br.edu.ufersa.locadora.model.DAO to java.sql;

    exports br.edu.ufersa.locadora;
    exports br.edu.ufersa.locadora.controllers;
    exports br.edu.ufersa.locadora.model.entities;
    exports br.edu.ufersa.locadora.model.Service;
}