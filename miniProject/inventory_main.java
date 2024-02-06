package miniProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

public class inventory_main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model; // 테이블 모델 추가
	private JComboBox<String> comboBox1;
	private JComboBox<String> comboBox2;



	public inventory_main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 900, 600);
		setLocationRelativeTo(null); // 화면 중앙에 표시
		

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		// 이미지를 담을 JPanel 생성
		JPanel imagePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// 이미지 그리기
				ImageIcon imageIcon = new ImageIcon("C:/Users/codepc/Desktop/project/aa.png");
				Image image = imageIcon.getImage();
				// 이미지 크기를 30x30으로 조정
				g.drawImage(image, 0, 0, 30, 30, this);
			}
		};
		imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		Panel panel = new Panel();
		imagePanel.add(panel);

		// 콤보박스 추가
		comboBox1 = new JComboBox<>();
		comboBox2 = new JComboBox<>();

		// 패널에 콤보박스 추가
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new FlowLayout());
		
		
		
		comboPanel.add(comboBox1);
		// 콤보박스1에 카테고리 목록 로드
        loadCategoriesToComboBox();
		
		comboPanel.add(comboBox2);
		
		//////콤보박스의 이벤트 처리
		comboBox1.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		       
		    }
		});
		
		comboBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// 선택된 카테고리에 따라 테이블 업데이트
                updateTableByCategory(comboBox1.getSelectedItem().toString());
            }
        });
		
		imagePanel.add(comboPanel);

		contentPane.setLayout(new BorderLayout(0, 0));
		// 이미지 패널을 프레임의 North에 추가
		contentPane.add(imagePanel, BorderLayout.NORTH);

		// 띄어쓰기 ( 간격 100 )
		Component horizontalStrut = Box.createHorizontalStrut(100);
		imagePanel.add(horizontalStrut);

		// 메인 버튼
		JButton btnNewButton = new JButton("메인으로");
		imagePanel.add(btnNewButton);

		// 나머지 컴포넌트를 추가하거나 필요에 맞게 작성

		// 이미지 경로 디렉토리 출력
		System.out.println("Current Directory: " + System.getProperty("user.dir"));

		setContentPane(contentPane);

		// JScrollPanel 추가
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		//테이블 모델 생성 및 초기화
		  String[] colName = { "카테고리", "품목", "단위당 가격", "수량", "자동발주 기준 수량", "기준 미달 시 발주 수량" };
		  model = new DefaultTableModel(colName, 0);
	      
		//테이블 생성 및 모델 설정
		  table = new JTable(model);
		  scrollPane.setViewportView(table);
		
		
		////DB연결///////////////////////////////////////////////////////////////////////////////////////////////////////////
		Connection conn = null;
		try {
			// JDBC Driver 등록
			Class.forName("com.mysql.cj.jdbc.Driver");

			//연결하기
			conn = DriverManager.getConnection(
					"jdbc:mysql://222.119.100.89:3382/shopping",
					"minishop",
					"2m2w"
					);
			System.out.println("Database connected!");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
			
			String query = "SELECT * FROM inventory order by category";
	         PreparedStatement preparedStatement = conn.prepareStatement(query);
	         ResultSet resultSet = preparedStatement.executeQuery();

	         while (resultSet.next()) {
	            // 각 열의 데이터를 가져와서 테이블 모델에 추가
	            model.addRow(new Object[] { resultSet.getString("category"), resultSet.getString("product"),
	                  resultSet.getDouble("per_price"), resultSet.getInt("amount"), resultSet.getInt("auto_standard"),
	                  resultSet.getInt("order_amount") });

	         }

	         resultSet.close();
	         preparedStatement.close();

			
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		  } catch (ClassNotFoundException | SQLException e) {
		         e.printStackTrace();
		      }

	   ////DB연결끊기///////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		  scrollPane.setViewportView(table);


		// south 에 패널 추가 + 버튼 3개
		Panel panel_1 = new Panel();
		contentPane.add(panel_1, BorderLayout.SOUTH);

		JButton btnNewButton_1 = new JButton("판매");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 판매 버튼 클릭 시 처리 로직 추가
			}
		});
		panel_1.add(btnNewButton_1);

		Component horizontalStrut_1 = Box.createHorizontalStrut(180);
		panel_1.add(horizontalStrut_1);

		JButton btnNewButton_2 = new JButton("추가");
		
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 추가 버튼 클릭 시 처리 로직 추가
			}
		});
		panel_1.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("삭제");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//삭제 버튼 클릭 시 처리 로직 추가
			}
		});
		panel_1.add(btnNewButton_3);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				inventory_main frame = new inventory_main();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
    
}
