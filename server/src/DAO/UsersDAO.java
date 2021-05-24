package DAO;

import java.sql.Connection;
import java.sql.SQLException;

import domin.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.ConnUtil;

public class UsersDAO {
   public Users selectByName(String username) throws SQLException {
	// TODO Auto-generated constructor stub
	   String sql = "SELECT * FROM users " +"WHERE user_name = ?";
	   Connection conn = ConnUtil.getConn();
	   PreparedStatement pstat = conn.prepareStatement(sql);
	   pstat.setString(1, username);
	   ResultSet rs = pstat.executeQuery();
	   if(rs.next()){
		   Users user = new Users();
		   user.setUser_name(rs.getString("user_name"));
		   user.setUser_pass(rs.getString("user_pass"));
		   user.setUser_Id(rs.getInt("user_id"));
		   return user;
	   }else{
		   return null;
	   }
	  
   }
   
   public Users insert(String username, String userpass) throws SQLException{
	   String sql = "insert into " + " users (user_name,user_pass) " + " values(?,?) ";
	   Connection conn = ConnUtil.getConn();
	   PreparedStatement pstat = conn.prepareStatement(sql);
	   pstat.setString(1, username);
	   pstat.setString(2, userpass);
	   pstat.executeUpdate();
	   UsersDAO userdao = new UsersDAO();
	   Users user = userdao.selectByName(username);
	   return user;
	
   }
}
