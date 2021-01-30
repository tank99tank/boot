package org.uushang.boot.auth.constant;

public enum Param {
	USERID("userId", 1), USERNO("userNo", 1), USERNAME("username", 1), NICKNAME("nickName", 1), EMAIL("email", 1),
	TELEPHONE("telephone", 1), ADDRESS("address", 1), FULLPATH("fullPath", 1), ORGNO("orgNo", 3), ORGNAME("orgName", 3),
	CREDITCODE("orgCreditCode", 3), CARDNUMBER("cardNumber", 1);

	public String toString() {
		return "Param." + name() + "(name=" + getName() + ", type=" + this.type + ")";
	}

	String name;

	int type;

	Param(String name, int type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public int type() {
		return this.type;
	}
}
