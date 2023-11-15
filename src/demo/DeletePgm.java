package demo;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.geometry.Insets;

public class DeletePgm {
	private static final Logger logger = Logger.getLogger("demo.Db");
    private Connection connection;
    

    public DeletePgm(Connection connection) {
        this.connection = connection;

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDeleteScreen() {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("데이터 삭제");

        // Create UI components for delete screen
        ComboBox<String> equipmentComboBox = new ComboBox<>();
        equipmentComboBox.setPromptText("정산소");
        equipmentComboBox.getItems().addAll(
            "방문자입구", "정기권입구", "EPS", "출구무인 영수증프린터", "출구무인 티켓리더기", "출구무인 동전인식기",
            "출구무인 동전호퍼3", "출구무인 동전호퍼4", "출구무인 지폐인식기", "출구무인 지폐방출기1",
            "출구무인 지폐방출기2", "출구무인 신용카드RF동글", "출구무인 신용카드리더기",
            "출구무인 전면서브보드", "출구무인 내부서브보드", "출구무인 통합동전함", "방문자출구",
            "사전무인기", "사전무인 영수증프린터", "사전무인 티켓리더기", "사전무인 동전인식기",
            "사전무인 동전호퍼3", "사전무인 동전호퍼4", "사전무인 지폐인식기", "사전무인 지폐방출기1",
            "사전무인 지폐방출기2", "사전무인 신용카드RF동글", "사전무인 신용카드리더기",
            "사전무인 전면서브보드", "사전무인 내부서브보드", "사전무인 통합동전함"
        );

        TextField approvalNoField = new TextField();
        approvalNoField.setPromptText("승인번호");

        TextField dateField = new TextField();
        dateField.setPromptText("날짜");

        Button deleteButton = new Button("삭제");

        deleteButton.setOnAction(e -> {
            String selectedUnitName = equipmentComboBox.getValue();
            String approvalNo = approvalNoField.getText();
            String date = dateField.getText();

            if (selectedUnitName != null && !selectedUnitName.isEmpty() && !approvalNo.isEmpty() && date != null && !date.isEmpty()) {
                try {
                    // Get MainUnitNo from ps070 for the selected UnitName
                    String ps070Query = "SELECT MainUnitNo FROM ps070 WHERE UnitName = ?";
                    PreparedStatement ps070Statement = connection.prepareStatement(ps070Query);
                    ps070Statement.setString(1, selectedUnitName);
                    ResultSet ps070Result = ps070Statement.executeQuery();

                    if (ps070Result.next()) {
                        String mainUnitNo = ps070Result.getString("MainUnitNo");

                        // Delete the record from ps540 using MainUnitNo, ApprovalNo, and Date
                        String ps540Query = "DELETE FROM ps540 WHERE MainUnitNo = ? AND ApprovalNo = ? AND Procdate = ?";
                        PreparedStatement ps540Statement = connection.prepareStatement(ps540Query);
                        ps540Statement.setString(1, mainUnitNo);
                        ps540Statement.setString(2, approvalNo);
                        ps540Statement.setString(3, date);
                        int rowsAffected = ps540Statement.executeUpdate();
                        ps540Statement.close();

                        if (rowsAffected > 0) {
                            String logMessage = "정산소: " + selectedUnitName + ", 승인번호: " + approvalNo + ", 날짜: " + date + "데이터가 삭제되었습니다.";
                            logger.info(logMessage);
                            showMessage(logMessage);
                        } else {
                            showMessage("No records deleted. Please check the input data.");
                        }
                    } else {
                        showMessage("Selected UnitName not found in ps070 table.");
                    }

                    ps070Result.close();
                    ps070Statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showMessage("Error: " + ex.getMessage());
                }
            } else {
                showMessage("Please fill in all fields.");
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(equipmentComboBox, approvalNoField, dateField, deleteButton);
        Scene deleteScene = new Scene(layout, 400, 150);
        deleteStage.setScene(deleteScene);
        deleteStage.show();
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
