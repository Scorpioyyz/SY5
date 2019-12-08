package model;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import model.*;

public class MyFrame extends JFrame {
	JButton jbCheckStudent, jbCheckTeacher, jbCheckStatis, jbCheckCourse, jbCheckStatisXuefen, jbMainframe, jbCheck, jbExit, jbOrder;
	CardLayout card = new CardLayout();
	JPanel cardpanel;
	private JScrollPane jsp;

	public MyFrame() throws Exception {
		super();
		initFrame();
	}

	private void initFrame() throws Exception {
		
		this.setTitle("课程管理系统");
		JLabel jlWelcon = new JLabel("课程管理系统", JLabel.CENTER);
		jlWelcon.setFont(new Font("Aharoni", Font.BOLD, 24));
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.green);
		panel1.add(jlWelcon, BorderLayout.SOUTH);
		this.add(panel1, BorderLayout.NORTH);
		

		cardpanel = new JPanel(card);
		cardpanel.setBackground(Color.white);
		cardpanel.add(new HomePanel(), "myframe");
		
		this.add(cardpanel, BorderLayout.CENTER);

		this.setSize(1000, 860);
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}
}