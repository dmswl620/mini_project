package jjjj;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ProductAdd extends JDialog {

	private static final long serialVersionUID = 1L;
	private Connection conn;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private String category, product;
	private int amount, per_price, auto_standard, order_amount;

	/**
	 * Create the frame.
	 */
	public ProductAdd(JFrame parent) {
		super(parent, "품목 추가", true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 330);
		setLocationRelativeTo(null); // 화면 중앙에 표시
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("품목 추가");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel.setBounds(12, 10, 100, 31);
		contentPane.add(lblNewLabel);
		
		//카테고리
		JLabel lblNewLabel_1 = new JLabel("카테고리");
		lblNewLabel_1.setBounds(37, 71, 52, 15);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(106, 68, 106, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		//--------------------------------------------------------
		
		//품목
		JLabel lblNewLabel_2 = new JLabel("품목");
		lblNewLabel_2.setBounds(37, 108, 52, 15);
		contentPane.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(106, 105, 106, 21);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		//--------------------------------------------------------

		//수량
		JLabel lblNewLabel_3 = new JLabel("수량");
		lblNewLabel_3.setBounds(37, 147, 52, 15);
		contentPane.add(lblNewLabel_3);
		
		textField_2 = new JTextField();
		textField_2.setBounds(106, 144, 106, 21);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		//--------------------------------------------------------

		//단위당 가격
		JLabel lblNewLabel_4 = new JLabel("단위당 가격");
		lblNewLabel_4.setBounds(19, 188, 75, 15);
		contentPane.add(lblNewLabel_4);
		
		textField_3 = new JTextField();
		textField_3.setBounds(106, 185, 106, 21);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		//--------------------------------------------------------

		//자동 발주 기준 수량
		JLabel lblNewLabel_5 = new JLabel("자동 발주\r\n기준 수량");
		lblNewLabel_5.setBounds(287, 60, 109, 36);
		contentPane.add(lblNewLabel_5);
		
		textField_4 = new JTextField();
		textField_4.setBounds(408, 68, 106, 21);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		//--------------------------------------------------------

		//발주 수량
		JLabel lblNewLabel_6 = new JLabel("발주 수량");
		lblNewLabel_6.setBounds(287, 122, 52, 15);
		contentPane.add(lblNewLabel_6);
		
		textField_5 = new JTextField();
		textField_5.setBounds(408, 119, 106, 21);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		//--------------------------------------------------------

		JButton btnNewButton = new JButton("취소");
		btnNewButton.setBounds(439, 260, 95, 23);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		//확인 버튼 클릭 시 textfield에 입력한 내용을 DB에 추가
		JButton btnNewButton_1 = new JButton("확인");
		btnNewButton_1.setBounds(320, 260, 95, 23);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean result1, result2, result3, result4;
				boolean dupl_check = true;
				//모든 필드에 값이 있어야함
				if(textField.getText().length() > 0 && textField_1.getText().length() > 0 &&
						textField_2.getText().length() > 0 && textField_3.getText().length() > 0 &&
						textField_4.getText().length() > 0 && textField_5.getText().length() > 0) {
					
					//수량과 가격에 숫자 입력했는지
					String check = "^[0-9]*$";
					result1 = Pattern.matches(check, textField_2.getText());
					result2 = Pattern.matches(check, textField_3.getText());
					result3 = Pattern.matches(check, textField_4.getText());
					result4 = Pattern.matches(check, textField_5.getText());
					if(result1 && result2 && result3 && result4) {
						System.out.println("숫자만 입력되었습니다.");
						category = textField.getText();
						product = textField_1.getText();
						System.out.println("이거 왜 안나옴 : " + textField_2.getText());
						amount = Integer.parseInt(textField_2.getText());
						per_price = Integer.parseInt(textField_3.getText());
						auto_standard = Integer.parseInt(textField_4.getText());
						order_amount = Integer.parseInt(textField_5.getText());
						
						//수량과 가격에는 음수가 들어갈 수 없음
						if(amount < 0 || per_price < 0 || auto_standard < 0 || order_amount < 0) {
							JOptionPane.showMessageDialog(ProductAdd.this,
									"수량과 가격에는 음수가 들어갈 수 없습니다.");
							textField_2.setText("");
							textField_3.setText("");
							textField_4.setText("");
							textField_5.setText("");
						} else {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
								conn = DriverManager.getConnection(
										"jdbc:mysql://222.119.100.89:3382/shopping",
										"minishop",
										"2m2w"
										);
								
								String sql1 = "" +
										"select product from inventory";
								PreparedStatement pstmt = conn.prepareStatement(sql1);
								ResultSet rs = pstmt.executeQuery();
								
								//입력한 품목과 DB에 있는 품목 목록중에 같은 것이 있는지 검사
								while(rs.next()) {
									String dbProduct = rs.getString("product");
									if(product.equals(dbProduct)) {
										dupl_check = false;
										JOptionPane.showMessageDialog(ProductAdd.this,
												"품목은 중복될 수 없습니다.");
									}
								}
								
								//품목이 중복이 안될 때 DB에 추가
								if(dupl_check) {
									String sql2 = "" +
											"insert into inventory (category, product, per_price, amount, auto_standard, order_amount) " +
											"values(?, ?, ?, ?, ?, ?) ";
									
									PreparedStatement pstmt2 = conn.prepareStatement(sql2);
									pstmt2.setString(1, category);
									pstmt2.setString(2, product);
									pstmt2.setInt(3, per_price);
									pstmt2.setInt(4, amount);
									pstmt2.setInt(5, auto_standard);
									pstmt2.setInt(6, order_amount);
									pstmt2.executeUpdate();
									JOptionPane.showMessageDialog(ProductAdd.this,
											"품목이 추가되었습니다.");	
									
									//품목이 추가되면 재고화면의 table을 업데이트 해야함
									InventoryPage inven = new InventoryPage();
									inven.refreshTable();
									
									dispose();
								}
								
								pstmt.close();
								conn.close();
								
							} catch(Exception e1) {
								e1.printStackTrace();
								try {
									conn.close();
								} catch (SQLException e2) {
									e2.printStackTrace();
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(ProductAdd.this,
								"수량과 가격에 문자를 입력할 수 없습니다.");
					}
				} else {
					JOptionPane.showMessageDialog(ProductAdd.this,
							"항목이 비어있습니다.");
				}
			}
		});
	}
}
