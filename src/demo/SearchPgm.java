package demo;

import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;

import javafx.stage.Stage;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;



public class SearchPgm extends Application {

    private Connection connection;



    public SearchPgm(Connection connection) {

        this.connection = connection;

    }



    @Override

    public void start(Stage stage) {

        stage.setTitle("데이터 검색");



        GridPane grid = new GridPane();

        grid.setHgap(10);

        grid.setVgap(5);



        Label equipmentLabel = new Label("정산소 코드:");

        TextField equipmentField = new TextField();

        equipmentField.setPromptText("입력");



        Label vendorLabel = new Label("발급사 코드:");

        TextField vendorField = new TextField();

        vendorField.setPromptText("입력");



        Label resultLabel = new Label();



        Button equipmentSearchButton = new Button("정산소 코드 검색");

        Button vendorSearchButton = new Button("발급사 코드 검색");



        equipmentSearchButton.setOnAction(e -> {

            // Implement your equipment search logic here

            String equipmentNumber = equipmentField.getText();

            if (!equipmentNumber.isEmpty()) {

                String query = "SELECT UnitName FROM ps070 WHERE MainUnitNo = ?";

                try {

                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setString(1, equipmentNumber);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        resultLabel.setText("정산소: " + resultSet.getString("UnitName"));

                    } else {

                        resultLabel.setText("정산소를 찾을 수 없습니다.");

                    }

                } catch (SQLException ex) {

                    ex.printStackTrace();

                }

            }

        });



        vendorSearchButton.setOnAction(e -> {

            // Implement your vendor search logic here

            String vendorCode = vendorField.getText();

            if (!vendorCode.isEmpty()) {

                String query = "SELECT IssueCopName FROM ps540 WHERE TkNo = ?";

                try {

                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.setString(1, vendorCode);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {

                        resultLabel.setText("매입사: " + resultSet.getString("IssueCopName"));

                    } else {

                        resultLabel.setText("매입사를 찾을 수 없습니다.");

                    }

                } catch (SQLException ex) {

                    ex.printStackTrace();

                }

            }

        });



        grid.add(equipmentLabel, 0, 0);

        grid.add(equipmentField, 1, 0);

        grid.add(equipmentSearchButton, 2, 0);

        grid.add(vendorLabel, 0, 1);

        grid.add(vendorField, 1, 1);

        grid.add(vendorSearchButton, 2, 1);

        grid.add(resultLabel, 0, 2, 3, 1);



        Scene scene = new Scene(grid, 400, 100);

        stage.setScene(scene);

        stage.show();

    }



    public static void main(String[] args) {

        // Your database connection setup here...

        launch(args);

    }

}