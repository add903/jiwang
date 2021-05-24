package client;


import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.*;

import domin.Users;
import exception.NameException;
import exception.PassException;
import service.UsersService;


/**
*  负责客户端的读和写，以及登录和发送的监听 
*  之所以把登录和发送的监听放在这里，是因为要共享一些数据，比如mySocket,textArea
*/
public class ClientReadAndPrint extends Thread{
	static Socket mySocket = null;  // 一定要加上static，否则新建线程时会清空
	static JTextField textInput;
	static JTextArea textShow;
	static JFrame chatViewJFrame;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static String userName;
	
	// 用于接收从服务端发送来的消息
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));  // 输入流
			while (true) {
				String str = in.readLine();  // 获取服务端发送的信息
				textShow.append(str + '\n');  // 添加进聊天客户端的文本区域
				textShow.setCaretPosition(textShow.getDocument().getLength());  // 设置滚动条在最下面
			}
		} catch (Exception e) {}
	}
	
	/**********************登录监听(内部类)**********************/
	
     class LoginListen implements ActionListener{
		JTextField textField;
		JPasswordField pwdField;
		JFrame loginJFrame;  // 登录窗口本身
		
		
		public void setJTextField(JTextField textField) {
			this.textField = textField;
		}
		public void setJPasswordField(JPasswordField pwdField) {
			this.pwdField = pwdField;
		}
		public void setJFrame(JFrame jFrame) {
			this.loginJFrame = jFrame;
		}
		
		ClientView chatView = null;
		
		public void actionPerformed(ActionEvent event) {
			int action = Integer.parseInt(event.getActionCommand());
			
			if(action==1){
				userName = textField.getText();
				String userPwd = String.valueOf(pwdField.getPassword());  // getPassword方法获得char数组
				
				UsersService user1 = new UsersService();
				Users user = null;
				try {
					user = user1.isLogin(userName, userPwd);
				} catch (NameException | PassException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				if(user != null) {  
					chatView = new ClientView(userName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
					// 建立和服务器的联系
					try {
						InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
						mySocket = new Socket(addr,8081);  // 客户端套接字
						loginJFrame.setVisible(false);  // 隐藏登录窗口
						out = new PrintWriter(mySocket.getOutputStream());  // 输出流
						out.println("用户【" + userName + "】进入聊天室！");  // 发送用户名给服务器
						out.flush();  // 清空缓冲区out中的数据
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 新建普通读写线程并启动
					ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
					readAndPrint.start();
					// 新建文件读写线程并启动
					ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
					fileThread.start();
				}
				else {
					JOptionPane.showMessageDialog(loginJFrame, "账号或密码错误，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
				}
				}		
			
		 	else{				
				loginJFrame.setVisible(false);  // 隐藏登录窗口
				new Register();
			}
			}
		}
	
    
     /**********************注册监听(内部类)**********************/
     class RegisterListen implements ActionListener{
    	 JTextField textField;
 		JPasswordField pwdField;
 		JPasswordField pwdField2;
 		JFrame registerJFrame;  // 登录窗口本身
 		
 		
 		public void setJTextField(JTextField textField) {
 			this.textField = textField;
 		}
 		public void setJPasswordField(JPasswordField pwdField,JPasswordField pwdField2 ) {
 			this.pwdField = pwdField;
 			this.pwdField2 = pwdField2;
 		}
 		
 		public void setJFrame(JFrame jFrame) {
 			this.registerJFrame = jFrame;
 		}
 		ClientView chatView = null;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			userName = textField.getText();
			String userPwd = String.valueOf(pwdField.getPassword());  // getPassword方法获得char数组
			String userPwd2 = String.valueOf(pwdField2.getPassword());
			UsersService user1 = new UsersService();
			Users user = null;
			
			if(userPwd.equals(userPwd2)){
				try {
					user = user1.register(userName, userPwd);
					
				} catch (NameException | PassException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
				JOptionPane.showMessageDialog(registerJFrame, "密码错误，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
			}
			
			if(user!=null) {  
				
				chatView = new ClientView(userName);  // 新建聊天窗口,设置聊天窗口的用户名（静态）
				// 建立和服务器的联系
				try {
					InetAddress addr = InetAddress.getByName(null);  // 获取主机地址
					mySocket = new Socket(addr,8081);  // 客户端套接字
					registerJFrame.setVisible(false);  // 隐藏注册窗口
					out = new PrintWriter(mySocket.getOutputStream());  // 输出流
					out.println("用户【" + userName + "】进入聊天室！");  // 发送用户名给服务器
					out.flush();  // 清空缓冲区out中的数据
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 新建普通读写线程并启动
				ClientReadAndPrint readAndPrint = new ClientReadAndPrint();
				readAndPrint.start();
				// 新建文件读写线程并启动
				ClientFileThread fileThread = new ClientFileThread(userName, chatViewJFrame, out);
				fileThread.start();
			}
			else {
				JOptionPane.showMessageDialog(registerJFrame, "注册失败，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
			}
			}		
     
		}
    	 
     
	
	/**********************聊天界面监听(内部类)**********************/
	class ChatViewListen implements ActionListener{
		public void setJTextField(JTextField text) {
			textInput = text;  // 放在外部类，因为其它地方也要用到
		}
		public void setJTextArea(JTextArea textArea) {
			textShow = textArea;  // 放在外部类，因为其它地方也要用到
		}
		public void setChatViewJf(JFrame jFrame) {
			chatViewJFrame = jFrame;  // 放在外部类，因为其它地方也要用到
			// 设置关闭聊天界面的监听
			chatViewJFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					out.println("用户【" + userName + "】离开聊天室！");
					out.flush();
					System.exit(0);
				}
			});
		}
		// 监听执行函数
		public void actionPerformed(ActionEvent event) {
			try {
				String str = textInput.getText();
				// 文本框内容为空
				if("".equals(str)) {
					textInput.grabFocus();  // 设置焦点（可行）
					// 弹出消息对话框（警告消息）
					JOptionPane.showMessageDialog(chatViewJFrame, "输入为空，请重新输入！", "提示", JOptionPane.WARNING_MESSAGE);
					return;
				}
				out.println(userName + "说：" + str);  // 输出给服务端
				out.flush();  // 清空缓冲区out中的数据
				
				textInput.setText("");  // 清空文本框
				textInput.grabFocus();  // 设置焦点（可行）
//				textInput.requestFocus(true);  // 设置焦点（可行）
			} catch (Exception e) {}
		}
	}
}

