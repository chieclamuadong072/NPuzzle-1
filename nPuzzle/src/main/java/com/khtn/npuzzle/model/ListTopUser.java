package com.khtn.npuzzle.model;

import java.util.ArrayList;


public class ListTopUser {
	private TopUserScore[] results;

	public TopUserScore[] getUser() {
		return results;
	}
	public void setUser(TopUserScore[] user) {
		this.results = user;
	}
	public TopUserScore getUserByID(int id)
	{
		return results[id];
	}
	public ArrayList<TopUserScore> toArrayList()
	{
		ArrayList<TopUserScore> retlist = new ArrayList<TopUserScore>();
		for (TopUserScore topUserScore : results) {
			retlist.add(topUserScore);
		}
		return retlist;
	}
}
