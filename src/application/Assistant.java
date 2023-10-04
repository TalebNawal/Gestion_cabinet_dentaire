package application;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Assistant extends User{
	private int ID;
	
	public Assistant(String Login, String Passwd, String name, String cin, String tel) {
		super(Login, Passwd, name, cin, tel);
		setID();
	}
	
	public int getID() {
		return ID;
	}
	public void setID() {
		int id = 1;
		boolean changed = true;
		while (changed) {
			changed = false;
			try{
				FileInputStream fis = new FileInputStream("assistants");
				while (fis.available() > 0) {
					ObjectInputStream ois = new ObjectInputStream(fis);
					Assistant assistant = (Assistant) ois.readObject();
					if (assistant.getID()==id) {
						id++;
						changed = true;
						break;
					}
				}
				fis.close();
			}catch (EOFException e) {
				// this is fine
			}catch (IOException | ClassNotFoundException ex) {
				ex.printStackTrace();
			} 
		}
		this.ID = id;
	}
}
