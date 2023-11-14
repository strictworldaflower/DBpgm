package demo;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



public class InsertPgm extends Application {
	private static final Logger logger = Logger.getLogger("demo.Db");


    private Connection connection;

    public InsertPgm(Connection connection) {

        this.connection = connection;
        try {
       
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    	

    	

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }



        // 나머지 JDBC 연결 및 SQL 작업을 수행하는 코드를 이어서 작성합니다.

    }



    @Override

    public void start(Stage primaryStage) {

        primaryStage.setTitle("데이터 삽입");



        TextField transactionTimeField = new TextField();

        TextField cardNumberField = new TextField();

        TextField amountField = new TextField();

        TextField approvalNumberField = new TextField();

        TextField billingDateField = new TextField();

        TextField merchantField = new TextField();



        transactionTimeField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d{0,6}")) {

                transactionTimeField.setText(newValue.replaceAll("[^\\d]", "").substring(0, 6));

            }

        });

        

        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 16) {
                // 값이 16자리를 초과하면 입력을 막습니다.
                cardNumberField.setText(oldValue);
            } else if (newValue.length() > 6) {
                // 앞 6자리를 추출
                String firstSixDigits = newValue.substring(0, 6);
                // 뒤 10자리를 '*'로 대체
                String maskedPortion = newValue.substring(6).replaceAll("[0-9]", "*");
                // 새로운 값으로 설정
                cardNumberField.setText(firstSixDigits + maskedPortion);
            }
        });



        billingDateField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d{0,8}")) {

                billingDateField.setText(newValue.replaceAll("[^\\d]", "").substring(0, 8));

            }

        });

        

        ComboBox<String> merchantComboBox = new ComboBox<>();

        merchantComboBox.getItems().addAll(

        		"아주대의료원","광주신용","SC비씨체크","하나비씨신용","부산비씨신용","수협","비씨카드","우리신용신용","광주은행","우리신용","NH비씨신용","케이뱅크체크","비씨신용","비씨체크",

                "IBK기업비씨체크","비씨카드 EDC","우리비씨체크체크","수협은행","우리비씨신용신용","우리비씨신용체크","KB국민BC신용","수협체크","씨티체크","농협비씨체크","IBK비씨체크","우체국체크",

                "신한비씨신용","대구비씨신용","산림조합중앙회체","NH농협비씨체크","모빌체크","NH비씨체크","우리카드신용","광주체크","부산비씨체크","카카오페이체크","우리체크카드체크","신협체크","경남비씨체크",

                "우리체크","GmoneyCard체크","씨티비씨 신용","새마을금고체크","우리카드체크","한국투자증권체크","경남비씨신용","씨티카드","하나비씨신용신용","하나비씨체크","저축은행체크","수협카드","SC비씨신용",

                "광주은행체크","새마을체크","우리비씨체크","광주은행신용","IBK비씨신용","제주카드","국민비씨신용","IBK삼성PAY체크","KDB산업체크","우리비씨신용","IBK기업비씨신용","농협비씨신용","씨티체크카드",

                "KB국민체크카드","KB증권","KB국민실버카드","국민실버카드","국민신용","국민플래티늄","국민행복신용","KB국민체크",

                "국민체크카드","KB은련카드","국민MASTER카드","JB전북카드","국민골드카드","카카오뱅크mini","PAYCO카드","KB국민카드","국민카드","코나카드","카카오뱅크체크","하나(구외환)","하나체크","하나카드","하나기업카드","토스뱅크","하나체크카드","하나기업체크","삼성체크카드","삼성체크","삼성마스타","삼성","삼성신용카드","삼성아멕스",

                "이마트삼성카드","해외아멕스","삼성비자체크",

                "삼성비자","삼성마스타법인","삼성은련법인","삼성마스터신용",

                "삼성비자법인","삼성은련카드",

                "삼성VISA카드","삼성법인",

                "삼성카드","삼성로컬신용","삼성선불앱카드","신한프리미엄","신한은련개인","신한신용카드","신한다자녀신용","신한VIP카드","신한카드","신한신용","신한카드-선불",

                "신한카드-법인",

                "신한카드-체크","신한체크카드",

                "해외MASTER카드",

                "해외VISA카드",

                "신한체크",

                "TE플래티늄",

                "신한AlwaysOn",

                "현대thePurple",

                "현대카드",

                "현대개인",

                "현대로컬개인",

                "현대하이브리드","현대카드M2",

                "현대개인신용",

                "현대체크카드","개인레드",

                "현대비자개인",

                "현대개인비자",

                "현대개인퍼플",

                "현대마스터개인","현대로컬",

                "NH Platinum",

                "NH농협카드",

                "NH기업카드",

                "NH Titanium",

                "NH체크",

                "NH카드",

                "NH체크카드",

                "NH World",

                "NH Diamond",

                "NH기업체크",

                "롯데아멕스",

                "롯데플래티늄",

                "롯데카드",

                "롯데신용카드",

                "롯데신용",

                "롯데더드림",

                "롯데패밀리W",

                "롯데다이아몬드",

                "롯데체크카드",

                "롯데국민행복",

                "롯데법인카드"



        );

        merchantComboBox.setPromptText("매입사 선택");

        

        ComboBox<String> unitNameComboBox = new ComboBox<>();

        unitNameComboBox.getItems().addAll(

            // ComboBox에 표시할 옵션을 여기에 추가하세요

        		"방문자입구",

        		"정기권입구",

        		"EPS",

        		"출구무인 영수증프린터",

        		"출구무인 티켓리더기",

        		"출구무인 동전인식기",

        		"출구무인 동전호퍼3",

        		"출구무인 동전호퍼4",

        		"출구무인 지폐인식기",

        		"출구무인 지폐방출기1",

        		"출구무인 지폐방출기2",

        		"출구무인 신용카드RF동글",

        		"출구무인 신용카드리더기",

        		"출구무인 전면서브보드",

        		"출구무인 내부서브보드",

        		"출구무인 통합동전함",

        		"방문자출구",

        		"사전무인기",

        		"사전무인 영수증프린터",

        		"사전무인 티켓리더기",

        		"사전무인 동전인식기",

        		"사전무인 동전호퍼3",

        		"사전무인 동전호퍼4",

        		"사전무인 지폐인식기",

        		"사전무인 지폐방출기1",

        		"사전무인 지폐방출기2",

        		"사전무인 신용카드RF동글",

        		"사전무인 신용카드리더기",

        		"사전무인 전면서브보드",

        		"사전무인 내부서브보드",

        		"사전무인 통합동전함" // 원하는 옵션을 추가하세요

        );

        unitNameComboBox.setPromptText("정산소 선택");





        Button submitButton = new Button("입력");





        VBox root = new VBox(10);

        root.getChildren().addAll(

                new Label("거래시간: "),

                transactionTimeField,

                new Label("카드번호: "),

                cardNumberField,

                new Label("금액: "),

                amountField,

                new Label("승인번호: "),

                approvalNumberField,

                new Label("청구일자: "),

                billingDateField,

                new Label("매입사: "),

                merchantComboBox,  // 매입사 콤보 박스 추가

                new Label("정산소:"),

                unitNameComboBox,

                submitButton

        );



        submitButton.setOnAction(event -> {

                    String amount = amountField.getText();

                    String approvalNumber = approvalNumberField.getText();

                    String billingDate = billingDateField.getText();

                    String unitName = unitNameComboBox.getValue();

                    String transactionTime = transactionTimeField.getText();

                    String cardNumber = cardNumberField.getText();

                    String merchant = merchantComboBox.getValue(); 

                    String tkNo = getTkNoByMerchant(merchant);

                    String cardType = "";



            if ("아주대의료원".equals(merchant)) {

                cardType = "01"; // 아주대의료원인 경우 Card Type를 01로 설정

            } else {

                cardType = "02"; // 아주대의료원이 아닌 경우 Card Type를 02로 설정

            }



            String returnMsg = "";



            if (cardNumber.length() == 16) {

                cardNumber = cardNumber.substring(0, 6) + "**********";

            } else {

                System.err.println("Error: Card Number must be exactly 16 characters.");

                return;

            }





            if (transactionTime.isEmpty()) {

                transactionTime = "0";

            }



            if (amount.isEmpty()) {

                amount = "0";

            }



            String mainUnitNo = getMainUnitNoByUnitName(unitName);



            if (mainUnitNo == null) {

                System.err.println("Error: MainUnitNo not found for the given UnitName.");

                return;

            }

            if (!approvalNumber.isEmpty()) {

                try {

                    // Attempt to parse the approvalNumber as an integer

                    int approvalNumberInt = Integer.parseInt(approvalNumber);



                    if ("02".equals(cardType)) {

                        returnMsg = "정상승인"; // Card Type가 02인 경우 "정상승인"으로 설정

                    }

                } catch (NumberFormatException e) {

                    returnMsg = "Invalid Approval Number"; // Handle non-integer input

                }

            }



            try {



                SimpleDateFormat timeFormatWithSeconds = new SimpleDateFormat("yyyyMMddHHmmss");

                String currentTimeWithSeconds = timeFormatWithSeconds.format(new java.util.Date());



                String systemNo = "0001";

                String parkNo = "01";



                String insPgm = "CMS";

                String insId = "CMS";

                String updPgm = "CMS";

                String updId = "CMS";



                SimpleDateFormat insDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String insDate = insDateFormat.format(new Date());

                String updDate = insDate;



                String transDate = billingDate;

                String transTime = transactionTime;





                String sql = "INSERT INTO PS540 (procdate, proctime, TrackData, ApprovalAmt, ApprovalNo, transdate, TransTime, IssueCopName, SystemNo, ParkNo, CardType, MainUnitNo, MsgID, TermNo, ClientType, ReturnCode, TotSendMsg, Closing, InSeqNo, InsPgm, InsId, UpdPgm, UpdId, InsDate, UpdDate, TkNo, ReturnMsg, TransRequestAmt, BeforeAmt, AfterAmt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, transDate);

                preparedStatement.setString(2, transactionTime);

                preparedStatement.setString(3, cardNumber);

                preparedStatement.setString(4, amount);

                preparedStatement.setString(5, approvalNumber);

                preparedStatement.setString(6, transDate);

                preparedStatement.setString(7, transTime);

                preparedStatement.setString(8, merchant);

                preparedStatement.setString(9, systemNo);

                preparedStatement.setString(10, parkNo);

                preparedStatement.setString(11, cardType);

                preparedStatement.setString(12, mainUnitNo);

                preparedStatement.setString(13, "");

                preparedStatement.setString(14, "");

                preparedStatement.setString(15, "0210");

                preparedStatement.setString(16, "0000");

                preparedStatement.setString(17, "");

                preparedStatement.setString(18, "");

                preparedStatement.setString(19, "");

                preparedStatement.setString(20, insPgm);

                preparedStatement.setString(21, insId);

                preparedStatement.setString(22, updPgm);

                preparedStatement.setString(23, updId);

                preparedStatement.setString(24, insDate);

                preparedStatement.setString(25, updDate);

                preparedStatement.setString(26, tkNo);

                preparedStatement.setString(27, returnMsg);
                
                preparedStatement.setString(28, amount);
                
                preparedStatement.setInt(29, 0);
                
                preparedStatement.setInt(30, 0);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {

                    connection.commit();



                    System.out.println("A new record was inserted successfully!");

                }

                // 데이터 삽입이 성공하면 메시지 표시

                showSuccessMessage("데이터가 성공적으로 삽입되었습니다.");

            

                preparedStatement.close();

				/* connection.close(); */

            } catch (SQLException e) {

                e.printStackTrace();

            }

        });

        Scene scene = new Scene(root, 400, 500);

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    private String getTkNoByMerchant(String merchant) {

        // Merchant 값과 TkNo를 매핑할 데이터

        String[] merchants = {

                "아주대의료원","광주신용","SC비씨체크","하나비씨신용","부산비씨신용","수협","비씨카드","우리신용신용","광주은행","우리신용","NH비씨신용","케이뱅크체크","비씨신용","비씨체크",

                "IBK기업비씨체크","비씨카드 EDC","우리비씨체크체크","수협은행","우리비씨신용신용","우리비씨신용체크","KB국민BC신용","수협체크","씨티체크","농협비씨체크","IBK비씨체크","우체국체크",

                "신한비씨신용","대구비씨신용","산림조합중앙회체","NH농협비씨체크","모빌체크","NH비씨체크","우리카드신용","광주체크","부산비씨체크","카카오페이체크","우리체크카드체크","신협체크","경남비씨체크",

                "우리체크","GmoneyCard체크","씨티비씨 신용","새마을금고체크","우리카드체크","한국투자증권체크","경남비씨신용","씨티카드","하나비씨신용신용","하나비씨체크","저축은행체크","수협카드","SC비씨신용",

                "광주은행체크","새마을체크","우리비씨체크","광주은행신용","IBK비씨신용","제주카드","국민비씨신용","IBK삼성PAY체크","KDB산업체크","우리비씨신용","IBK기업비씨신용","농협비씨신용","씨티체크카드",

                "KB국민체크카드","KB증권","KB국민실버카드","국민실버카드","국민신용","국민플래티늄","국민행복신용","KB국민체크",

                "국민체크카드","KB은련카드","국민MASTER카드","JB전북카드","국민골드카드","카카오뱅크mini","PAYCO카드","KB국민카드","국민카드","코나카드","카카오뱅크체크","하나(구외환)","하나체크","하나카드","하나기업카드","토스뱅크","하나체크카드","하나기업체크","삼성체크카드","삼성체크","삼성마스타","삼성","삼성신용카드","삼성아멕스",

                "이마트삼성카드","해외아멕스","삼성비자체크",

                "삼성비자","삼성마스타법인","삼성은련법인","삼성마스터신용",

                "삼성비자법인","삼성은련카드",

                "삼성VISA카드","삼성법인",

                "삼성카드","삼성로컬신용","삼성선불앱카드","신한프리미엄","신한은련개인","신한신용카드","신한다자녀신용","신한VIP카드","신한카드","신한신용","신한카드-선불",

                "신한카드-법인",

                "신한카드-체크","신한체크카드",

                "해외MASTER카드",

                "해외VISA카드",

                "신한체크",

                "TE플래티늄",

                "신한AlwaysOn",

                "현대thePurple",

                "현대카드",

                "현대개인",

                "현대로컬개인",

                "현대하이브리드","현대카드M2",

                "현대개인신용",

                "현대체크카드","개인레드",

                "현대비자개인",

                "현대개인비자",

                "현대개인퍼플",

                "현대마스터개인","현대로컬",

                "NH Platinum",

                "NH농협카드",

                "NH기업카드",

                "NH Titanium",

                "NH체크",

                "NH카드",

                "NH체크카드",

                "NH World",

                "NH Diamond",

                "NH기업체크",

                "롯데아멕스",

                "롯데플래티늄",

                "롯데카드",

                "롯데신용카드",

                "롯데신용",

                "롯데더드림",

                "롯데패밀리W",

                "롯데다이아몬드",

                "롯데체크카드",

                "롯데국민행복",

                "롯데법인카드"





// ... (계속 나열)



        };



        String[] tkNos = {
        	    "00", "01", "02", "03", "06", "07", "08", "11", "33"  // 다른 TkNo 값들도 여기에 추가할 수 있습니다.
        	};

        	String result = "기본 TkNo";  // 기본값 설정

        	for (int i = 0; i < merchants.length; i++) {
        	    if (merchants[i].equals(merchant)) {
        	        result = tkNos[i];
        	        break;  // 일치하는 값을 찾았으므로 루프 종료
        	    }
        	}

        	return result;
    }




    private String getMainUnitNoByUnitName(String unitName) {

        try {

            String sql = "SELECT MainUnitNo FROM PS070 WHERE UnitName = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, unitName);

            ResultSet resultSet = preparedStatement.executeQuery();



            if (resultSet.next()) {

                return resultSet.getString("MainUnitNo");

                

            }



            resultSet.close();

            preparedStatement.close();

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }



        return null;

    }

    private void showSuccessMessage(String message) {

    	Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("성공");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        // 성공 메시지를 로그에도 남깁니다.
        logger.info(message);
    }
}