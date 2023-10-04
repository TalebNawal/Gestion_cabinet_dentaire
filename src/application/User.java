package application;

import java.io.Serializable;
import java.time.LocalDate;

abstract class User implements Serializable{
	private String Login;
	private String Passwd;
	private String name;
	private String CIN;
	private String Tel;
	private String dateCreation;
	
	public User(String Login,String Passwd,String name, String cin, String tel) {
		setLogin(Login);
		setPasswd(Passwd);
		setName(name);
		String date = LocalDate.now().toString();
		setDateCreation(date);
		setCIN(cin);
		setTel(tel);
	}
	public String getCIN() {
		return CIN;
	}
	public void setCIN(String cIN) {
		CIN = cIN;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public String getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}
	public String getLogin() {
		return Login;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLogin(String login) {
		Login = login;
	}
	public String getPasswd() {
		return Passwd;
	}
	public void setPasswd(String passwd) {
		Passwd = passwd;
	}
}
