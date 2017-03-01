package kz.codingwolves.unichat.usercheckers;

public class MessageNumber {
	
	private String mail;
	
	private int number;

	public MessageNumber(String mail) {
		this.mail = mail;
		this.number = 1;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MessageNumber)) {
			return false;
		}
		MessageNumber pairo = (MessageNumber) o;
		if (pairo.getMail().equals(this.mail)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "{{{" + mail + "}}}";
	}
}