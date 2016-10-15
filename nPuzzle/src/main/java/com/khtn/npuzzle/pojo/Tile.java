package com.khtn.npuzzle.pojo;

public class Tile {

	private int number;
	private int imgPath;
	private boolean showNumber;
	private boolean showImage;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	public void showNumber(boolean show)
	{
		this.showNumber = show;
	}
	public boolean allowShowNumber()
	{
		return showNumber;
	}
	
	public int getImgPath() {
		return imgPath;
	}
	public void setImgPath(int imgPath) {
		this.imgPath = imgPath;
	}

	public void showImage(boolean show)
	{
		this.showImage = show;
	}
	public boolean allowShowImage()
	{
		return showImage;
	}
	
}
