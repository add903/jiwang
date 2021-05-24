package service;

import java.sql.SQLException;

import DAO.UsersDAO;
import domin.Users;
import exception.NameException;
import exception.PassException;
import util.ConnUtil;

public class UsersService {
	//（登录）service 依赖于DAO
		private UsersDAO userDAO = new UsersDAO();
		/**
		 * 登录验证方法
		 * @param name 账号
		 * @param pass 密码
		 * @return 登录成功时返回用户,数据库操作异常时，返回空值
		 * @throws NameException 
		 */
			
		public Users isLogin(String name, String pass) throws NameException,PassException{
			try {
				//2 验证账号
				Users user = this.userDAO.selectByName(name);
				if(user == null){
					//手动抛出异常
					throw new NameException("账号不存在！");
				}
						
				//3 验证密码
				if(user.getUser_pass().equals(pass)){
					//登录成功
					return user;
				}else{
					//登录失败
					throw new PassException("密码错误！");
				}	
				
									
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				//throw new RuntimeException(e);
			} finally{
				ConnUtil.closeConn();//一个业务完成，关闭数据库连接
			}
			
			
		}
		
		public Users register(String name, String pass) throws NameException,PassException{
			try {
				//2 验证账号
				Users user = this.userDAO.insert(name,pass);
				
				if(user == null){
					//手动抛出异常
					throw new NameException("注册失败！");
				}
						
				//3 验证密码
				if(user.getUser_pass().equals(pass)){
					//注册成功
					return user;
				}else{
					//注册失败
					throw new PassException("注册失败！");
				}	
				
									
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
				//throw new RuntimeException(e);
			} finally{
				ConnUtil.closeConn();//一个业务完成，关闭数据库连接
			}
			
			
		}


}
