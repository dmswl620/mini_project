package jjjj;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MainPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MainPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 560, 330);
		setLocationRelativeTo(null); // 화면 중앙에 표시
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("재고");
		btnNewButton.setBounds(49, 101, 136, 75);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //버튼 클릭 시
				InventoryPage inventoryPage = new InventoryPage();
				inventoryPage.setVisible(true); //메인화면 띄우기
				setVisible(false); //원래 있던 창 닫기
				dispose();
			}
		});
		
		JButton btnNewButton_1 = new JButton("발주내역");
		btnNewButton_1.setBounds(224, 101, 136, 75);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("거래처 관리");
		btnNewButton_2.setBounds(398, 101, 136, 75);
		contentPane.add(btnNewButton_2);
	}

}
