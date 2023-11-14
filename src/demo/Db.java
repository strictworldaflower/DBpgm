package demo;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Pos;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.io.File;


//나머지 코드



public class Db extends Application {
    private static final Logger logger = Logger.getLogger("demo.Db");
    private static FileHandler fileHandler;
    private ScheduledExecutorService executor;
    private boolean connected = false;
    private ServerSocket serverSocket;

    private Connection connection;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
    	
    	
    	
        this.primaryStage = primaryStage;
        
        if (isAlreadyRunning()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("어플리케이션이 이미 실행중입니다.");
                alert.showAndWait();
                Platform.exit();
            });
            return;
        }
        this.primaryStage.setTitle("데이터베이스 앱");

        TextField ipField = new TextField();
        ipField.setPromptText("IP 주소");
        TextField portField = new TextField();
        portField.setPromptText("포트번호");
        TextField dbNameField = new TextField();
        dbNameField.setPromptText("데이터베이스 이름");
        TextField usernameField = new TextField();
        usernameField.setPromptText("사용자");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호");
        Button connectButton = new Button("연결");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(ipField, portField, dbNameField, usernameField, passwordField, connectButton);
        Scene scene = new Scene(layout, 450, 230);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        connectButton.setOnAction(e -> {
            String ip = ipField.getText();
            String port = portField.getText();
            String dbName = dbNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();

            // GUI 스레드에서 연결 시도
            connectToDatabase(ip, port, dbName, username, password);
        });
    }

    private void connectToDatabase(String ip, String port, String dbName, String username, String password) {
        if (connected) {
            return; // Already connected, no need to retry
        }
        connected = true; // Connection attempt in progress

        executor = Executors.newScheduledThreadPool(1);
        executor.schedule(() -> {
            showErrorMessage("데이터베이스 연결 시간 초과. 네트워크 또는 연결 구성을 확인하십시오.");
            executor.shutdown();
            connected = false;
        }, 1, TimeUnit.SECONDS);

        Thread thread = new Thread(() -> {
            try {
                String url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + dbName + ";encrypt=false";
                connection = DriverManager.getConnection(url, username, password);
                if (connection != null) {
                    Platform.runLater(() -> showMainScreen());
                } else {
                    showErrorMessage("데이터베이스 연결에 실패했습니다. 올바른 연결 정보를 확인하십시오.");
                }
            } catch (SQLException e) {
                showErrorMessage("데이터베이스 연결에 실패했습니다. 올바른 연결 정보를 확인하십시오.");
            } finally {
                executor.shutdown();
                connected = false;
            }
        });
        thread.start();
    }

    private void showErrorMessage(String message) {
        if (!connected) {
            return; // 이미 연결 시도가 완료되었으면 에러 메시지 표시하지 않음
        }

        connected = false; // 에러 발생시 연결 시도 완료
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("에러");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showMainScreen() {
        primaryStage.setTitle("주요기능");

        Button insertButton = new Button("데이터 삽입");
        insertButton.getStyleClass().add("main-button");

        Button deleteButton = new Button("데이터 삭제");
        deleteButton.getStyleClass().add("main-button");

        Button searchButton = new Button("데이터 검색");
        searchButton.getStyleClass().add("main-button");


        insertButton.setOnAction(e -> {
            InsertPgm insertPgm = new InsertPgm(connection);
            Stage insertStage = new Stage();
            insertPgm.start(insertStage);
        });

        deleteButton.setOnAction(e -> {
            DeletePgm deletePgm = new DeletePgm(connection);
            deletePgm.showDeleteScreen();
        });

        searchButton.setOnAction(e -> {
            SearchPgm searchPgm = new SearchPgm(connection);
            Stage searchStage = new Stage();
            searchPgm.start(searchStage);
        });

        HBox buttonBox = new HBox(10); // 10은 버튼 사이의 간격입니다.
        buttonBox.getChildren().addAll(insertButton, deleteButton, searchButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().add(buttonBox);
        mainLayout.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(mainLayout, 400, 100);
        mainScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(mainScene);
    }

    private boolean isAlreadyRunning() {
        try {
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            // 이미 실행 중으로 판단하고 서버 소켓을 닫도록 수정
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    
    private void showMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    @Override
    public void stop() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static void main(String[] args) {
        try {
            // 중복 실행을 방지하는 락 파일 생성 또는 이미 존재하는지 확인
            File lockFile = new File("running.lock");
            if (lockFile.exists()) {
                System.out.println("어플리케이션이 이미 실행 중입니다.");
                System.exit(0);
            } else {
                lockFile.createNewFile();
            }

            // 로그 파일 이름 생성
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();
            String logFileName = dateFormat.format(now) + "-log.txt";

            // 로그 파일 경로 설정
            fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);

            logger.info("어플리케이션 실행");

            launch(args);

            logger.info("어플리케이션 종료");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 어플리케이션 종료 후 락 파일 삭제
            File lockFile = new File("running.lock");
            if (lockFile.exists()) {
                lockFile.delete();
            }
        }
    }
}