package jjjj;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.Color;

public class InventoryPage extends JFrame {

	private static final long serialVersionUID = 1L;
//	private static InventoryPage dbConnection = new InventoryPage();
	// private String selectedItem = "기타";
//	private static Connection conn;
//	private static java.lang.String[] columnNames = {
//			"카테고리", "품목", "단위당 가격", "수량", "자동 발주 기준 수량", "기준 미달 시 발주 수량"
//		};
	private JPanel contentPane;
	private JTable table;
	private JComboBox<String> comboBox;
	private JButton btnNewButton_2;
	private static java.lang.String[] columnNames = { "카테고리", "품목", "단위당 가격", "수량", "발주기준수량", "발주수량" };
	private static DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	public String product;

	/**
	 * Create the frame.
	 */
	public InventoryPage() {
		// 메인창 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(0, 0, 1000, 600);
		setLocationRelativeTo(null); // 화면 중앙에 표시
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 210, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("재고 관리");
		lblNewLabel.setFont(new Font("D2Coding", Font.PLAIN, 20));
		lblNewLabel.setBounds(22, 13, 97, 24);
		contentPane.add(lblNewLabel);
		// --------------------------------------------------------------------------------

		// 카테고리 콤보 박스
		comboBox = new JComboBox();
//		comboBox.setModel(new DefaultComboBoxModel(new String[] {"기타", "과일", "시럽", "원두", "MD"}));
		comboBox.setBounds(142, 14, 132, 24);
		contentPane.add(comboBox());

		//////////////////////////////////////////////////////

		// 카테고리 목록을 DB에서 가져와서 콤보박스에 추가
		try {
			//DBConn.DBconnection();

			// 콤보박스에 카테고리 목록 추가
			updateCategoryComboBox();

			//DBConn.conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				DBConn.conn.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

		}

		////////////////////////////////////////////////////////////
		// System.out.println("선택된 카테고리: " + selectedItem);
		// --------------------------------------------------------------------------------

		// 메인화면으로 다시 가는 버튼
		JButton btnNewButton = new JButton("메인화면으로");
		btnNewButton.setBounds(861, 15, 113, 23);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 버튼 클릭 시
				MainPage mainPage = new MainPage();
				mainPage.setVisible(true); // 메인화면 띄우기
				setVisible(false); // 원래 있던 창 닫기
				dispose();
			}
		});
		// --------------------------------------------------------------------------------

		// table 만들고
		// DB에서 가져온 데이터 table로 띄우기
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(96, 71, 800, 400);
		contentPane.add(scrollPane);

		/*
		 * table = new JTable(new DefaultTableModel( new Object[][] { }, new String[] {
		 * "카테고리", "\uD488\uBAA9", "\uB2E8\uC704\uB2F9 \uAC00\uACA9", "\uC218\uB7C9",
		 * "발주기준수량", "발주수량" } ));
		 */
		table = new JTable(tableModel);
		table.setFont(new Font("D2Coding", Font.PLAIN, 15));

		try {
			DBConn.DBconnection();

			String sql = "" + "select * from inventory";

			PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tableModel.addRow(
						new Object[] { rs.getString("category"), rs.getString("product"), rs.getInt("per_price"),
								rs.getInt("amount"), rs.getInt("auto_standard"), rs.getInt("order_amount") });

				System.out.println(rs.getString("category") + "\t" + rs.getString("product") + "\t"
						+ rs.getInt("per_price") + "\t" + rs.getInt("amount") + "\t" + rs.getInt("auto_standard") + "\t"
						+ rs.getInt("order_amount"));
			}

			rs.close();
			pstmt.close();
			DBConn.conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				DBConn.conn.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

		}
		scrollPane.setViewportView(table);
		// --------------------------------------------------------------------------------

		// 판매 버튼을 누르면 inventory 테이블의 모든 품목의 재고가 -1 감소
		// DB에서 가져온 품목의 재고가 발주 기준 수량보다 작으면 발주 수량을 품목의 재고에 + 하고
		// 발주 기준 수량 보다 작았던 품목의 카테고리, 품목, 발주 수량, 단위당 가격, 거래처명, 날짜를 발주 내역 테이블에 추가
		// 거래처명은 거래처 테이블을 조회하여 그 품목을 파는 거래처명을 추가함.
		JButton btnNewButton_1 = new JButton("판매");
		btnNewButton_1.setBounds(116, 504, 95, 23);
		contentPane.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 버튼 클릭 시
				try {
					DBConn.DBconnection();

					// 판매 버튼을 누르면 inventory 테이블의 모든 품목의 재고가 -1 감소
					String sql1 = "" + "update inventory set amount = amount -1";
					PreparedStatement pstmt1 = DBConn.conn.prepareStatement(sql1);
					pstmt1.executeUpdate();

					// DB에서 가져온 품목의 재고가 발주 기준 수량보다 작으면 발주 수량을 품목의 재고에 +
					String sql2 = "" + "select product, amount, auto_standard " + "from inventory "
							+ "where amount < auto_standard";
					PreparedStatement pstmt2 = DBConn.conn.prepareStatement(sql2);
					pstmt2.executeQuery();
					// ------하는중-------

					DBConn.conn.close();

				} catch (Exception e1) {
					e1.printStackTrace();
					try {
						DBConn.conn.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}

				}
			}
		});
		// --------------------------------------------------------------------------------

		// 테이블에 있는 행 클릭 시 해당 행의 항목 삭제 버튼
		btnNewButton_2 = new JButton("삭제");
		btnNewButton_2.setBounds(806, 504, 95, 23);
		contentPane.add(getBtnConfirm());

		// 어떤 행을 선택했는지 테이블에 마우스 리스너 달기
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int rowIndex = table.getSelectedRow();
				if (rowIndex != -1) {
					// 클릭된 행의 셀값 읽기
					product = (String) table.getValueAt(rowIndex, 1);
					// StateInfo info = new StateInfo();
					// info.setProduct(product);
					System.out.println("선택된 행의 품목명 : " + product);
				}
			}
		});

		// --------------------------------------------------------------------------------

		// 추가 버튼 클릭 시 품목 추가 창 띄움

		contentPane.add(ProductAdd());
		// --------------------------------------------------------------------------------

	}

	// 카테고리 콤보 박스 클릭 시 선택된 카테고리만 테이블에 출력
	public JComboBox comboBox() {
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedItem = (String) comboBox.getSelectedItem();
				System.out.println("선택된 카테고리: " + selectedItem);

				////////////////////////////////////////////////////////////

				/////////////////////////////////////////////////////////////
				tableModel.setRowCount(0); // 원래 표시되던 테이블 목록 초기화
				
				try {
					DBConn.DBconnection();
					String sql;

					if (!selectedItem.equals("전체")) {
						sql = "SELECT * FROM inventory WHERE category = ? order by category";
					} else {
						sql = "SELECT * FROM inventory order by category";
					}

					PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
					if (!selectedItem.equals("전체")) {
						pstmt.setString(1, selectedItem);
					}
					ResultSet rs = pstmt.executeQuery();

					while (rs.next()) {
						tableModel.addRow(new Object[] { rs.getString("category"), rs.getString("product"),
								rs.getInt("per_price"), rs.getInt("amount"), rs.getInt("auto_standard"),
								rs.getInt("order_amount") });

						System.out.println(rs.getString("category") + "\t" + rs.getString("product") + "\t"
								+ rs.getInt("per_price") + "\t" + rs.getInt("amount") + "\t"
								+ rs.getInt("auto_standard") + "\t" + rs.getInt("order_amount"));
					}

					rs.close();
					pstmt.close();
					DBConn.conn.close();
					
				} catch (Exception e1) {
					e1.printStackTrace();
					try {
						DBConn.conn.close();
					} catch (SQLException e2) {
						e2.printStackTrace();
					}

				}
			}
		});
		return comboBox;
	}
	// --------------------------------------------------------------------------------

	// 삭체 버튼 클릭 시 ConfirmDialog 띄움
	public JButton getBtnConfirm() {
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 버튼 클릭 시

				if (product != null && !product.isEmpty()) { // 선택된 행이 있는지 확인
					// ConfirmDialog를 보여줌
					int option = JOptionPane.showConfirmDialog(InventoryPage.this, "정말 삭제하시겠습니까?", "삭제",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

					// 클릭된 버튼 확인
					if (option == JOptionPane.OK_OPTION) {
						try {
							DBConn.DBconnection();

							String sql = "" + "delete from inventory where product = ?";
							PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
							pstmt.setString(1, product);
							pstmt.executeUpdate();
							refreshTable();

							DBConn.conn.close();

							JOptionPane.showMessageDialog(InventoryPage.this, "삭제되었습니다.");
							product = null;

						} catch (Exception e1) {
							e1.printStackTrace();
							try {
								DBConn.conn.close();
							} catch (SQLException e2) {
								e2.printStackTrace();
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(InventoryPage.this, "삭제할 품목을 선택하세요.");
				}

			}
		});
		return btnNewButton_2;
	}
	// --------------------------------------------------------------------------------

	//언니 바보
	// 추가 버튼 클릭 시 품목을 추가할 수 있는 Dialog 띄움
	public JButton ProductAdd() {
		JButton btnNewButton_2_1 = new JButton("추가");
		btnNewButton_2_1.setBounds(699, 504, 95, 23);
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 버튼 클릭 시
				ProductAdd productAdd = new ProductAdd(null);
				productAdd.setVisible(true);
				try {
					updateCategoryComboBox();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return btnNewButton_2_1;
	}

	// 재고 화면의 Table 업데이트
	// static으로 잡아주면 다른 클래스에서 사용 가능
	// 안에 들어가는 변수도 다 static 설정해야함
	public static void refreshTable() {
		tableModel.setRowCount(0);
		try {
			DBConn.DBconnection();

			String sql = "" + "select * from inventory";

			PreparedStatement pstmt = DBConn.conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tableModel.addRow(
						new Object[] { rs.getString("category"), rs.getString("product"), rs.getInt("per_price"),
								rs.getInt("amount"), rs.getInt("auto_standard"), rs.getInt("order_amount") });
			}

			rs.close();
			pstmt.close();
			DBConn.conn.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				DBConn.conn.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	// --------------------------------------------------------------------------------
	/*
	 * //DB 연결을 싱글톤으로 만듦 public static InventoryPage DBconnection() { try {
	 * Class.forName("com.mysql.cj.jdbc.Driver");
	 * 
	 * conn = DriverManager.getConnection(
	 * "jdbc:mysql://222.119.100.89:3382/shopping", "minishop", "2m2w" ); } catch
	 * (Exception e) { try { conn.close(); } catch (SQLException e1) {
	 * e1.printStackTrace(); } }
	 * 
	 * return dbConnection; }
	 */
	// ---------------------------------------------------------------------------------------------

	// DB에서 카테고리 목록을 가져와 콤보박스에 추가하는 메소드
	public void updateCategoryComboBox() throws SQLException {
		DBConn.DBconnection();
		String query = "SELECT DISTINCT category FROM inventory";
		PreparedStatement preparedStatement = DBConn.conn.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();

		// "전체" 옵션 추가
		comboBox.addItem("전체");

		while (resultSet.next()) {
			String category = resultSet.getString("category");
			comboBox.addItem(category);
		}

		resultSet.close();
		preparedStatement.close();
		DBConn.conn.close();
	}
	////////////////////////////////////////////////////////////////////////////

	// 콤보박스1 테이블 카테고리 필터링 메소드
//    public static void filterTableByCategory(String category) {
//        DefaultTableModel table = (DefaultTableModel) tableModel.getModel();
//        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
//        tableModel.setRowSorter(sorter);
//
//        if (!category.equals("전체")) {
//            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter(category, 0); // 0은 카테고리 열의 인덱스
//            sorter.setRowFilter(rowFilter);
//        } else {
//            sorter.setRowFilter(null); //"전체"를 선택한 경우 모든 행 보여주기
//       }
//    }
}
