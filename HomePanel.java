package model;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class HomePanel extends JPanel {
	private Vector<String> col;
	private Vector<Vector> rowData;
	private JTable jtable;
	private JButton jb1, jb2, jb3, jbAddSummit, jbAddCancel;
	private CardLayout card = new CardLayout();
	private JTextField jtfAddCid, jtfAddXuefen,jtfAddUser;
	private JPanel jpanelAll = new JPanel(card);
	private JScrollPane jsp;
	private JComboBox jckAddTeacher,jckAddName,jckAddLevel;
	
	ImageIcon icon;
	Image img;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
		g.drawImage(img, 0, 0,this.getWidth(), this.getHeight(), this);
	}
	
	@SuppressWarnings("unchecked")
	public HomePanel() throws Exception {
		//初始化数据封装成一个方法
		initData();
		icon=new ImageIcon(getClass().getResource("/school.jpg"));
		img=icon.getImage();
		
		//添加
		JPanel jpanelAdd = new JPanel();
		jpanelAdd.add(new JLabel("ID:"));
		jpanelAdd.add(jtfAddCid = new JTextField(20));
		jpanelAdd.add(new JLabel("学生:"));
		jpanelAdd.add(jtfAddUser = new JTextField(20));
		
		jpanelAdd.add(new JLabel("老师:"));
		String teacher[] = { "苏老师", "孙老师" ,"刘老师", "张老师" ,"钱老师"};
		jckAddTeacher = new JComboBox(teacher);
		jpanelAdd.add(jckAddTeacher);
		jpanelAdd.add(new JLabel("课程:"));
		String name[] = { "语文", "数学" ,"英语", "化学" ,"政治"};
		jckAddName = new JComboBox(name);
		jpanelAdd.add(jckAddName);
		jpanelAdd.add(new JLabel("课程级别:"));
		String level[] = { "高级", "中级" ,"低级"};
		jckAddLevel = new JComboBox(level);
		jpanelAdd.add(jckAddLevel);
		
		jpanelAdd.add(jbAddSummit = new JButton("提交"));
		jpanelAdd.add(jbAddCancel = new JButton("取消"));
		jpanelAdd.setLayout(new GridLayout(7, 2));
		jpanelAdd.setBorder(new TitledBorder("选择:"));

		//删除
		JPanel jpanelDel = new JPanel();
		jpanelDel.setLayout(new GridLayout(2, 1));
		jpanelDel.setBorder(new TitledBorder("删除:"));

		jpanelAll.add(jsp, "All");
		jpanelAll.add(jpanelAdd, "Add");
		jpanelAll.add(jpanelDel, "Del");

		JPanel panel = new JPanel();
		panel.add(jb1 = new JButton("添加"));
		panel.add(jb2 = new JButton("删除"));
		panel.setLayout(new GridLayout(1, 2));
		
		this.add(jpanelAll,BorderLayout.SOUTH);
		this.add(panel);
	
		//===================================以下是主功能区按钮触发的监听操作区==============================//
		//选课按钮监听
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(jpanelAll, "Add");
				jtfAddCid.requestFocusInWindow();
			}
		});
		//退课按钮监听
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jtable.getSelectedRows().length == 0) {
					JOptionPane.showMessageDialog(null, "请先选择一条选课记录!");
				} else {
					if (JOptionPane.showConfirmDialog(null, "确定要删除选定的课程吗?") == 0)
						try {
							deleteCourseByTable();
							refresh();
						} catch (Exception e1) {
							e1.printStackTrace();
						}

				}
			}
		});
		
		//===================================以下是具体的按钮提交和取消的操作区==============================//
		//添加按钮提交时触发
		jbAddSummit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e){
				String cid = jtfAddCid.getText().trim();
				String user = jtfAddUser.getText().trim();
				String name = jckAddName.getSelectedItem().toString();
				String teacher = jckAddTeacher.getSelectedItem().toString();
				String level = jckAddLevel.getSelectedItem().toString();
				String strbuffer=cid+" "+user+" "+name+" "+teacher+" "+level+"\n";
				
		        try {
		        	FileWriter writer = new FileWriter("F://home.txt", true);
		        	writer.write(strbuffer);
		        	writer.flush();
		        	writer.close();
		        	initData();
		        } catch (IOException e2) {
		            e2.printStackTrace();
		        }
				JOptionPane.showMessageDialog(null, "添加成功!");
				jtfAddCid.setText(null);
				jtfAddUser.setText(null);
				card.first(jpanelAll);
			}
		});
		//添加按钮取消时触发
		jbAddCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.first(jpanelAll);
			}
		});
	}
	@SuppressWarnings("unchecked")
	private void refresh() {
		try {
			rowData = new Vector<Vector>();
			
		    FileReader reader = new FileReader("F://home.txt");
			BufferedReader br = new BufferedReader(reader);
	 
			String eachLine = null;//定义每一行
			while((eachLine = br.readLine()) != null){//读文件至末尾
				//-----split(String):根据给定正则表达式的匹配拆分此字符串。
				String[] temp = eachLine.split(" ");//每一行里的空格
				//-----声明每一行，必须在这里，外部是不行地。
				Vector<String> row = new Vector<String>();
				for(int i = 0; i < temp.length; i++){//遍历每一行
					row.add(temp[i]);//把每一行都加入row
				}
				rowData.add(row);//再把每一个row的数据给rowData
			}
			
			jtable.updateUI();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void initData() throws IOException {
		//声明表头
		col = new Vector<String>();
	    col.addElement("ID");
	    col.addElement("学生");
	    col.addElement("课程");
	    col.addElement("老师");
	    col.addElement("课程级别");
	    
	    //声明所有行
	    rowData = new Vector<Vector>();
		
	    FileReader reader = new FileReader("F://home.txt");
		BufferedReader br = new BufferedReader(reader);
 
		String eachLine = null;//定义每一行
		while((eachLine = br.readLine()) != null){//读文件至末尾
			//-----split(String):根据给定正则表达式的匹配拆分此字符串。
			String[] temp = eachLine.split(" ");//每一行里的空格
			//-----声明每一行，必须在这里，外部是不行地。
			Vector<String> row = new Vector<String>();
			for(int i = 0; i < temp.length; i++){//遍历每一行
				row.add(temp[i]);//把每一行都加入row
			}
			rowData.add(row);//再把每一个row的数据给rowData
		}
		
		DefaultTableModel dtm = new DefaultTableModel(rowData, col);
		jtable = new JTable();
		jtable.setModel(dtm);
		jsp = new JScrollPane(jtable);
		jsp.setBorder(new TitledBorder("列表信息："));
		card.first(jpanelAll);
	}

	private void deleteCourseByTable() throws Exception {
		String values[] = new String[100];
		String courseName[] = new String[100];
		int rows[] = jtable.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			values[i] = (String) jtable.getValueAt(rows[i], 0);
			courseName[i] = (String) jtable.getValueAt(rows[i], 1);
			//重新读取文件，遇到以这个开头的不写入新的文件中
			try {
				String readedLine;
				String strbuffer="";
	            BufferedReader br = new BufferedReader(new FileReader("F://home.txt"));
	            while ((readedLine = br.readLine()) != null) {
	                if (readedLine.startsWith(values[i])) {
	                    continue;
	                }
	                strbuffer=strbuffer+readedLine+"\n";
	            }
	            FileWriter writer = new FileWriter("F://home.txt");
	            writer.write(strbuffer);
	            writer.flush();
	            writer.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
			JOptionPane.showMessageDialog(null, " 【 " + courseName[i] + " 】 删除成功!");
		}
	}
}