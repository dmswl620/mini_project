package miniProject;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class login_test extends JFrame {

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

    public login_test() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("아이디");
        lblNewLabel.setBounds(50, 50, 50, 20);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(110, 50, 200, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("비밀번호");
        lblNewLabel_1.setBounds(50, 100, 50, 20);
        contentPane.add(lblNewLabel_1);

        textField_1 = new JTextField();
        textField_1.setBounds(110, 100, 200, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        JButton btnNewButton = new JButton("로그인");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(textField.getText());
            }
        });
        btnNewButton.setBounds(110, 150, 90, 30);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("닫기");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnNewButton_1.setBounds(210, 150, 90, 30);
        contentPane.add(btnNewButton_1);
    }
}