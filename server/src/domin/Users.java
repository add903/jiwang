package domin;

public class Users {
	private Integer user_id;
	private String user_name;
	private String user_pass;
	
	public Integer getUser_Id() {
		return user_id;
	}
	public void setUser_Id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_pass() {
		return user_pass;
	}
	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}
	@Override
	public String toString() {
		return "Users [user_id=" + user_id + ", user_name=" + user_name + ", user_pass=" + user_pass + "]";
	}
	
	
	
	
	

}
