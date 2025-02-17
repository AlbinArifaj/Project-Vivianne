module Vivianne {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.xml.dom;
    requires javafx.base;
    requires java.desktop;

    exports app;
    opens controller to javafx.fxml ,javafx.base;
    opens model to javafx.fxml ,javafx.base;

    exports service;
    exports repository;
    exports model.dto;
    exports model.filter;
    exports controller;
    exports ENUMS;
    exports model;


}