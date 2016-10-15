package com.khtn.npuzzle.model;

public class TopUserScore {
	private String Nickname;
	private int TongDiem;
	public String getNickname() {
		return Nickname;
	}
	public void setNickname(String nickname) {
		Nickname = nickname;
	}
	public int getTongDiem() {
		return TongDiem;
	}
	public void setTongDiem(int tongDiem) {
		TongDiem = tongDiem;
	}
	public TopUserScore(String nickname, int tongDiem) {
		super();
		Nickname = nickname;
		TongDiem = tongDiem;
	}
	@Override
	public String toString() {
		return "TopUserScore [Nickname=" + Nickname + ", TongDiem=" + TongDiem
				+ "]";
	}
	
}
