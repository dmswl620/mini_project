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
import javax.swing.table.TableRowSorter;

import jjjj.DBConn;

public class inventory_main extends JFrame {

   private static final long serialVersionUID = 1L;
   private JPanel contentPane;
   private static JTable table;
   private static Connection connection;
   
   private static JComboBox<String> comboBox1;
   

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
      imagePanel.setBounds(5, 5, 876, 43);
      imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

      // 콤보박스 추가
      // 카테고리 목록을 추가하고, "전체"옵션을 넣어 전체 데이터를 보여줄 수 있도록 함.
      
      comboBox1 = new JComboBox<>();
   // 패널에 콤보박스 추가
      JPanel comboPanel = new JPanel();
      comboPanel.setLayout(new FlowLayout());
      //카테고리 콤보박스 
      comboPanel.add(comboBox1);
      
      
      
      
      
   // 카테고리 목록을 DB에서 가져와서 콤보박스에 추가
      try {
          // JDBC Driver 등록
          Class.forName("com.mysql.cj.jdbc.Driver");

          // 데이터베이스 연결 정보 설정
          String url = "jdbc:mysql://222.119.100.89:3382/shopping";
          String username = "minishop";
          String password = "2m2w";

          // 데이터베이스 연결
          connection = DriverManager.getConnection(url, username, password);
          System.out.println("Database connected!");

          // 콤보박스에 카테고리 목록 추가
          updateCategoryComboBox();

      } catch (ClassNotFoundException | SQLException e) {
          e.printStackTrace();
      }
      
      
      //콤보박스1의 이벤트 처리
      comboBox1.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String selectedCategory = (String) comboBox1.getSelectedItem();
 //             filterTableByCategory(selectedCategory);
              
              // 선택한 카테고리에 따라 테이블 데이터를 다시 가져오기
              try {
                  // 기존 데이터 제거
                  DefaultTableModel model = (DefaultTableModel) table.getModel();
                  model.setRowCount(0);

                  String query;
                  if (!selectedCategory.equals("전체")) {
                      // 선택한 카테고리에 따라 쿼리 작성
                      query = "SELECT * FROM inventory WHERE category = ? order by category";
                  } else {
                      // 전체를 선택한 경우 모든 데이터 가져오기
                      query = "SELECT * FROM inventory order by category";
                  }

                  PreparedStatement preparedStatement = connection.prepareStatement(query);
                  if (!selectedCategory.equals("전체")) {
                      preparedStatement.setString(1, selectedCategory);
                  }
                  ResultSet resultSet = preparedStatement.executeQuery();

                  while (resultSet.next()) {
                      // 각 열의 데이터를 가져와서 테이블 모델에 추가
                      model.addRow(new Object[]{resultSet.getString("category"), resultSet.getString("product"),
                              resultSet.getDouble("per_price"), resultSet.getInt("amount"), resultSet.getInt("auto_standard"),
                              resultSet.getInt("order_amount")});
                  }

                  resultSet.close();
                  preparedStatement.close();

              } catch (SQLException ex) {
                  ex.printStackTrace();
              }
          }
      });
      contentPane.setLayout(null);

              
      imagePanel.add(comboPanel);
      // 이미지 패널을 프레임의 North에 추가
      contentPane.add(imagePanel);

      // 메인 버튼
      JButton btnNewButton = new JButton("메인으로");
      imagePanel.add(btnNewButton);

      // 나머지 컴포넌트를 추가하거나 필요에 맞게 작성

      // 이미지 경로 디렉토리 출력
      System.out.println("Current Directory: " + System.getProperty("user.dir"));

      setContentPane(contentPane);

      // JScrollPanel 추가
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(5, 48, 876, 434);
      contentPane.add(scrollPane);

      String[] colName = { "카테고리", "품목", "단위당 가격", "수량", "자동발주 기준 수량", "기준 미달 시 발주 수량" };
      DefaultTableModel model = new DefaultTableModel(colName, 0);

      model.setRowCount(0); // 기존 데이터 제거
      table = new JTable(model);
      table.setFont(new Font("돋움", Font.PLAIN, 14)); // 테이블 폰트
      
      
      
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
              conn.close();
           
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
          } catch (ClassNotFoundException | SQLException e) {
                 e.printStackTrace();
              }

        ////DB연결끊기///////////////////////////////////////////////////////////////////////////////////////////////////////////



      scrollPane.setViewportView(table);

//      // south 에 패널 추가 + 버튼 3개
//      Panel panel_1 = new Panel();
//      panel_1.setBounds(5, 525, 876, 33);
//      contentPane.add(panel_1);

      JButton saleButton = new JButton("판매");
      saleButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // 판매 버튼 클릭 시 처리 로직 추가
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
                System.out.println("salButton Database connected!");
             /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
                
              // 판매 버튼 클릭 시 처리 로직 추가
                
                // 일괄적으로 차감할 수량
                int soldAmount = 1;
                
                // 수량 차감 로직
                try {
                   // DB에서 모든 행의 데이터 가져오기
                   DefaultTableModel model = (DefaultTableModel) table.getModel();
                   
                   for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
                      String product = (String) model.getValueAt(rowIndex, 1);
                      int currentAmount = (int) model.getValueAt(rowIndex, 3);

                      // 수량이 충분한 경우에만 차감
                      if (currentAmount >= soldAmount) {
                         // 판매 후의 수량 계산
                         int newAmount = currentAmount - soldAmount;

                         // DB 업데이트 쿼리 실행
                         String updateQuery = "UPDATE inventory SET amount = amount -1 ";
                         PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                         updateStatement.setInt(1, newAmount);
//                         updateStatement.setString(2, product);
                         updateStatement.executeUpdate();
                      } else {
                         // 재고 부족 시 예외 처리 또는 로그 등 필요한 처리 추가
                      }
                   }
                   
                   // 판매 후 테이블 데이터 다시 로드
                   reloadTableData();
                   
                   // 판매 로그 등 추가적인 로직 구현 가능
                   
                   // 성공 메시지 출력 (예: JOptionPane 활용)
                   JOptionPane.showMessageDialog(inventory_main.this, "일괄 판매가 완료되었습니다.");
                   
                } catch (SQLException ex) {
                   ex.printStackTrace();
                   // 실패 메시지 출력
                   JOptionPane.showMessageDialog(inventory_main.this, "판매에 실패하였습니다.");
                }
             
                      
                      
                      
                      
                      

                
             /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
               } catch (ClassNotFoundException | SQLException e1) {
                      e1.printStackTrace();
                   }

             ////DB연결끊기///////////////////////////////////////////////////////////////////////////////////////////////////////////
         }
      });
      getContentPane().add(saleButton);

      JButton btnNewButton_2 = new JButton("추가");
      btnNewButton_2.setBounds(460, 528, 127, 24);
      
      btnNewButton_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // 추가 버튼 클릭 시 처리 로직 추가
            
            add_inventory ai = new add_inventory();
            ai.main(null);
            
         }
      });
      getContentPane().add(btnNewButton_2);

      JButton btnNewButton_3 = new JButton("삭제");
      btnNewButton_3.setBounds(627, 529, 187, 23);
      btnNewButton_3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //삭제 버튼 클릭 시 처리 로직 추가
            
            JOptionPane.showMessageDialog(
                    null, "알파벳 이군요");
            
            
            
            
            //
         }
      });
      getContentPane().add(btnNewButton_3);
   }
      
      
   // DB에서 카테고리 목록을 가져와 콤보박스에 추가하는 메소드
      public void updateCategoryComboBox() throws SQLException {
         String query = "SELECT DISTINCT category FROM inventory";
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery();

         // "전체" 옵션 추가
         comboBox1.addItem("전체");

         while (resultSet.next()) {
            String category = resultSet.getString("category");
            comboBox1.addItem(category);
         }

         resultSet.close();
         preparedStatement.close();
      }
      
      public static void reflash() {
         String selectedCategory = (String) comboBox1.getSelectedItem();
          filterTableByCategory(selectedCategory);
          
          // 선택한 카테고리에 따라 테이블 데이터를 다시 가져오기
          try {
              // 기존 데이터 제거
              DefaultTableModel model = (DefaultTableModel) table.getModel();
              model.setRowCount(0);

              String query;
              if (!selectedCategory.equals("전체")) {
                  // 선택한 카테고리에 따라 쿼리 작성
                  query = "SELECT * FROM inventory WHERE category = ? order by category";
              } else {
                  // 전체를 선택한 경우 모든 데이터 가져오기
                  query = "SELECT * FROM inventory order by category";
              }

              PreparedStatement preparedStatement = connection.prepareStatement(query);
              if (!selectedCategory.equals("전체")) {
                  preparedStatement.setString(1, selectedCategory);
              }
              ResultSet resultSet = preparedStatement.executeQuery();

              while (resultSet.next()) {
                  // 각 열의 데이터를 가져와서 테이블 모델에 추가
                  model.addRow(new Object[]{resultSet.getString("category"), resultSet.getString("product"),
                          resultSet.getDouble("per_price"), resultSet.getInt("amount"), resultSet.getInt("auto_standard"),
                          resultSet.getInt("order_amount")});
              }

              resultSet.close();
              preparedStatement.close();

          } catch (SQLException ex) {
              ex.printStackTrace();
          }
      }
   

   //콤보박스1 테이블 카테고리 필터링 메소드
         public static void filterTableByCategory(String category) {
             DefaultTableModel model = (DefaultTableModel) table.getModel();
             TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
             table.setRowSorter(sorter);

             if (!category.equals("전체")) {
                 RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter(category, 0); // 0은 카테고리 열의 인덱스
                 sorter.setRowFilter(rowFilter);
             } else {
                 sorter.setRowFilter(null); //"전체"를 선택한 경우 모든 행 보여주기
            }
         }
         
      // 판매 후 테이블 데이터 다시 로드하는 메소드
         public void reloadTableData() {
            try {
               // 기존 데이터 제거
               DefaultTableModel model = (DefaultTableModel) table.getModel();
               model.setRowCount(0);

               // DB에서 데이터 다시 가져오기
               String query = "SELECT * FROM inventory order by category";
               PreparedStatement preparedStatement = connection.prepareStatement(query);
               ResultSet resultSet = preparedStatement.executeQuery();

               while (resultSet.next()) {
                  // 각 열의 데이터를 가져와서 테이블 모델에 추가
                  model.addRow(new Object[]{resultSet.getString("category"), resultSet.getString("product"),
                        resultSet.getDouble("per_price"), resultSet.getInt("amount"), resultSet.getInt("auto_standard"),
                        resultSet.getInt("order_amount")});
               }

               resultSet.close();
               preparedStatement.close();

            } catch (SQLException ex) {
               ex.printStackTrace();
            }
         }
         
}        