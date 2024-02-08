package miniProject;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LogIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogIn frame = new LogIn();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public LogIn() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("아이디");
        lblNewLabel.setBounds(56, 72, 63, 15);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(124, 69, 229, 21);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("비밀번호");
        lblNewLabel_1.setBounds(60, 117, 52, 15);
        contentPane.add(lblNewLabel_1);

        textField_1 = new JTextField();
        textField_1.setBounds(124, 114, 229, 21);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JButton btnNewButton = new JButton("로그인");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	Connection conn = null;
        		try {
        			//JDBC Driver 등록
        			Class.forName("com.mysql.cj.jdbc.Driver");
        			
        			// 연결하기
        			conn = DriverManager.getConnection(
        					"jdbc:mysql://222.119.100.89:3382/shopping",
        					"minishop", // database id
        					"2m2w" // database password
        					);
        			
        			
        			// 입력된 아이디와 패스워드 가져오기
                    String inputId = textField.getText();
                    String inputPassword = textField_1.getText();

                    // DB에서 아이디와 패스워드 확인
                    String query = "SELECT * FROM users WHERE id=? AND password=?";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                        preparedStatement.setString(1, inputId);
                        preparedStatement.setString(2, inputPassword);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                // 아이디와 패스워드가 일치하는 경우
                                System.out.println("로그인 성공");

                                // 메인 화면으로 이동
                                dispose(); // 로그인 창 닫기
                                showMain_general(); // 메인 화면 표시
                            } else {
                                // 아이디와 패스워드가 일치하지 않는 경우
                                System.out.println("아이디 또는 패스워드가 일치하지 않습니다.");
                            //로그인 실패에 대한 동작 추가
                                return;
                            }
                        }
                    }
        			
        			System.out.println("DB 연결완료");
        		} catch (ClassNotFoundException ex) {
        			ex.printStackTrace();
        		} catch (SQLException ex) {
        			ex.printStackTrace();
        		} finally {
        			if(conn != null) {
        				try {
        					//연결 끊기
        					conn.close();
        				} catch (SQLException ex) {}
        			}
        		}
            	
            	
                //클릭 시 메인화면으로
                dispose(); // 로그인 창 닫기
                showMain_general(); //메인화면 표시
            }
        });
        btnNewButton.setBounds(111, 190, 95, 23);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("닫기");
        btnNewButton_1.setBounds(247, 190, 95, 23);
        contentPane.add(btnNewButton_1);
    }
    private void showMain_general() {
    	//메인화면 호출
    	Main_general main_general = new Main_general();
    	main_general.setVisible(true);
    }
}

