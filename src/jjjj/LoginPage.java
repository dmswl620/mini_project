package jjjj;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class LoginPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField passwordField;
	private Connection conn;

	//프로그램 시작 부분
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPage frame = new LoginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public LoginPage() {
		//기본 구조, 제목 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 560, 330);
		setLocationRelativeTo(null); // 화면 중앙에 표시
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 10, 522, 273);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("자동 발주 프로그램");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 16));
		lblNewLabel.setBounds(26, 10, 139, 55);
		panel.add(lblNewLabel);
		//------------------------------------------
		
		//비밀번호 입력 필드-----------------------------------
		JLabel lblNewLabel_1 = new JLabel("비밀번호 입력");
		lblNewLabel_1.setBounds(40, 75, 95, 23);
		panel.add(lblNewLabel_1);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(40, 98, 180, 35);
		panel.add(passwordField);
		//------------------------------------------
	
		//패스워드 최대 10자리 입력 가능하게 제한
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				passwordField = (JPasswordField) e.getSource();//이벤트 발생
				int textLeng = passwordField.getPassword().length;
				if(textLeng > 9) {
					e.consume(); //이벤트 소멸 메서드
				}
			}
		});
		//---------------------------------------------------
		
		JButton btnNewButton = new JButton("비밀번호 재설정");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetPassword resetpassword = new ResetPassword();
				resetpassword.setVisible(true);
			}
		});
		btnNewButton.setBounds(12, 216, 121, 23);
		panel.add(btnNewButton);
		
		//로그인 버튼-------------------------------------------
		JButton btnNewButton_1 = new JButton("로그인");
		btnNewButton_1.setBounds(297, 216, 95, 23);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//버튼 클릭 시 입력한 비밀번호와 DB에 있는 비빌번호가 일치하는지 확인
				String inputPassword = new String(passwordField.getPassword());
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://222.119.100.89:3382/shopping",
							"minishop",
							"2m2w"
							);
					
					String sql = "" +
							"select * from user";
					
					PreparedStatement pstmt = conn.prepareStatement(sql);
					ResultSet rs = pstmt.executeQuery();
					if(rs.next()) {
						String dbPassword = rs.getString("userinfo");
					
						if(inputPassword.equals(dbPassword)) {
							System.out.println("-----로그인 성공-----");
							//메인화면 띄우기
							MainPage mainPage = new MainPage();
							mainPage.setVisible(true);
							setVisible(false);
							dispose();
							conn.close();
						} else {
							System.out.println("-----로그인 실패-----");
							JOptionPane.showMessageDialog(LoginPage.this,
									"비밀번호가 일치하지 않습니다..");
							passwordField.setText("");
						}
					} else {
						System.out.println("테이블에 사용자가 없습니다.");
						JOptionPane.showMessageDialog(LoginPage.this,
								"테이블에 사용자가 없습니다.");
					}
					
					rs.close();
					pstmt.close();
					
				} catch(Exception e1) {
					e1.printStackTrace();
					try {
						conn.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}
				}
				
			}
		});
		//------------------------------------------
		
		//프로그램 종료---------------------------------
		JButton btnNewButton_2 = new JButton("종료");
		btnNewButton_2.setBounds(404, 216, 95, 23);
		panel.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		//------------------------------------------
	}
}