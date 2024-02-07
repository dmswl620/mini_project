package miniProject;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class add_inventory {

	private JFrame frame;
	private JTextField txtcategory;
	private JTextField txtproduct;
	private JTextField txtamount;
	private JTextField txtprice;
	private JTextField txtauto_standard;
	private JTextField txtorder_amount;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					add_inventory window = new add_inventory();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public add_inventory() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 501, 362);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("카테고리");
		lblNewLabel.setBounds(86, 97, 136, 15);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("품목");
		lblNewLabel_1.setBounds(86, 122, 136, 15);
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("수량");
		lblNewLabel_2.setBounds(86, 152, 136, 15);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("단위당 가격");
		lblNewLabel_3.setBounds(86, 183, 136, 15);
		panel.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("자동발주기준수량");
		lblNewLabel_4.setBounds(86, 211, 136, 15);
		panel.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("발주수량");
		lblNewLabel_5.setBounds(86, 242, 136, 15);
		panel.add(lblNewLabel_5);

		txtcategory = new JTextField();
		txtcategory.setBounds(234, 94, 106, 21);
		panel.add(txtcategory);
		txtcategory.setColumns(10);

		txtproduct = new JTextField();
		txtproduct.setBounds(234, 119, 106, 21);
		panel.add(txtproduct);
		txtproduct.setColumns(10);

		txtamount = new JTextField();
		txtamount.setBounds(234, 149, 106, 21);
		panel.add(txtamount);
		txtamount.setColumns(10);

		txtprice = new JTextField();
		txtprice.setBounds(234, 180, 106, 21);
		panel.add(txtprice);
		txtprice.setColumns(10);

		txtauto_standard = new JTextField();
		txtauto_standard.setBounds(234, 208, 106, 21);
		panel.add(txtauto_standard);
		txtauto_standard.setColumns(10);

		txtorder_amount = new JTextField();
		txtorder_amount.setBounds(234, 239, 106, 21);
		panel.add(txtorder_amount);
		txtorder_amount.setColumns(10);

		JButton btnNewButton = new JButton("확인");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Connection conn = null;

				try {
					// JDBC Driver 등록
					Class.forName("com.mysql.cj.jdbc.Driver");

					// 연결하기
					conn = DriverManager.getConnection("jdbc:mysql://222.119.100.89:3382/shopping", "minishop", "2m2w");

					String sql = "insert into inventory(category, product, per_price, amount, auto_standard, order_amount) values (?,?,?,?,?,?)";
					
					PreparedStatement pstmt =  conn.prepareStatement(sql);
					pstmt.setString(1,txtcategory.getText());
					pstmt.setString(2,txtproduct.getText());
					pstmt.setInt(3, Integer.parseInt(txtamount.getText()));
					pstmt.setInt(4,Integer.parseInt(txtprice.getText()));
					pstmt.setInt(5,Integer.parseInt(txtauto_standard.getText()));
					pstmt.setInt(6,Integer.parseInt(txtorder_amount.getText()));
					
					pstmt.executeUpdate();
					
					txtcategory.setText("");
					txtproduct.setText("");
					txtamount.setText("");
					txtprice.setText("");
					txtauto_standard.setText("");
					txtorder_amount.setText("");
					
					inventory_main.reflash();

				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				} catch (SQLException ex) {
					ex.printStackTrace();
				} finally {
					if (conn != null) {
						try {
							// 연결 끊기
							conn.close();
							System.out.println("연결 끊기");
						} catch (SQLException ex) {
						}
					}
				}

			}
		});
		btnNewButton.setBounds(156, 292, 95, 23);
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(319, 292, 95, 23);
		panel.add(btnNewButton_1);
		
		JLabel lblNewLabel_6 = new JLabel("New label");
		lblNewLabel_6.setBounds(60, 35, 52, 15);
		panel.add(lblNewLabel_6);
	}
}
