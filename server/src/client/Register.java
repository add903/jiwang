package client;

import java.awt.*;
import javax.swing.*;


public class Register {

	JTextField textField = null;
	JPasswordField pwdField = null;
	JPasswordField pwdField2 = null;
	ClientReadAndPrint.RegisterListen listener=null;

	
	// 构造函数
	public Register() {
		init();
	}
	
	void init() {
		JFrame jf = new JFrame("注册"); //登录界面 ；
		jf.setBounds(500,100,300,300);//setBounds(int x, int y, int width, int height)
		jf.setResizable(true);  // 设置是否缩放
		
		JPanel jp1 = new JPanel();
		JLabel headJLabel = new JLabel("注册界面");
		headJLabel.setFont(new Font(null, 0, 35));  // 设置文本的字体类型、样式 和 大小
		jp1.add(headJLabel); 
		
		JPanel jp2 = new JPanel();
		JLabel nameJLabel = new JLabel("用户名：");
		textField = new JTextField(20);
		JLabel pwdJLabel = new JLabel("密码：    ");
		pwdField = new JPasswordField(20);
		JLabel pwdJLabel2 = new JLabel("重复密码：    ");
		pwdField2 = new JPasswordField(20);
		
		JButton registerButton = new JButton("注册"); 
		//  //将按钮添加到JPanel
		jp2.add(nameJLabel);
		jp2.add(textField);
		jp2.add(pwdJLabel);
		jp2.add(pwdField);
		jp2.add(pwdJLabel2);
		jp2.add(pwdField2);
		jp2.add(registerButton);
		
		JPanel jp = new JPanel(new BorderLayout());  // BorderLayout布局
		jp.add(jp1, BorderLayout.NORTH);
		jp.add(jp2, BorderLayout.CENTER);
		
		
		// 设置监控
		listener = new ClientReadAndPrint().new RegisterListen();  // 新建监听类
		listener.setJTextField(textField);  // 调用PoliceListen类的方法
		listener.setJPasswordField(pwdField,pwdField2);
		listener.setJFrame(jf);
		pwdField.addActionListener(listener);  // 密码框添加监听
		pwdField2.addActionListener(listener);  // 密码框添加监听
		registerButton.addActionListener(listener);  // 按钮添加监听
		
		
		jf.add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置关闭图标作用
		jf.setVisible(true);  // 设置可见
	}
}

