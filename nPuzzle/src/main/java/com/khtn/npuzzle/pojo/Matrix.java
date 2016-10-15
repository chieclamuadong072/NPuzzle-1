package com.khtn.npuzzle.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.khtn.npuzzle.GameEngine;
import com.khtn.npuzzle.myenum.Direction;

public class Matrix {

	private int Id;
	private List<Tile> tiles = new ArrayList<Tile>();

	// previous state
	private Matrix parent;
	
	// comparingValue = stepCount + heuristicScore
	private int stepCount;
	private int heuristicScore;
	private int comparingValue;

	// board size N x N
	private int boardSize;
	private int length;
	private int blankValue;
	private int blankTilePos;

	// direction moved from previous state to current state
	private Direction direction;
	
	public Matrix()
	{
		
	}

	// initiate new state as follow:
	// 1 2 3
	// 4 5 6
	// 7 8 []
	// [] = value 9
	public Matrix(int size) {
		this.boardSize = size;
		this.blankValue = size * size;
		this.length = size * size;
		InitMatrix();
	}

	public void InitMatrix() {
		
		for (int i = 0; i < length; i++) {
			Tile tile = new Tile();
			tile.setNumber(i + 1);
			tile.showNumber(true);
			tile.showImage(true);
			
			if(i == length - 1)
			{
				tile.showNumber(false);
				tile.showImage(false);
			}
			
			tiles.add(tile);
		}
		
		blankTilePos = length - 1;
		
		makeMove(Direction.UP);
		makeMove(Direction.LEFT);
		makeMove(Direction.UP);
		makeMove(Direction.RIGHT);
		makeMove(Direction.DOWN);
		makeMove(Direction.LEFT);
		makeMove(Direction.LEFT);
		makeMove(Direction.DOWN);
	}
	
	protected Matrix(Matrix matrixCopy)
	{
		this.Id = matrixCopy.Id;
		this.tiles = matrixCopy.tiles;

		this.stepCount = matrixCopy.stepCount;
		this.heuristicScore = matrixCopy.heuristicScore;
		this.comparingValue = matrixCopy.comparingValue;

		this.boardSize = matrixCopy.boardSize;
		this.length = matrixCopy.length;
		this.blankValue = matrixCopy.blankValue;
		this.blankTilePos = matrixCopy.blankTilePos;

		this.parent = matrixCopy.parent;

		this.direction = matrixCopy.direction;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
		getId();
	}

	public int getHeuristicScore() {
		return heuristicScore;
	}

	public void setHeuristicScore(int heuristicScore) {
		this.heuristicScore = heuristicScore;
		comparingValue = stepCount + heuristicScore;
	}

	public int getStepCount() {
		return stepCount;
	}

	public void increaseStep() {
		this.stepCount++;
	}

	public int getComparingValue() {
		return comparingValue;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getLength() {
		return length;
	}

	public int getBlankValue() {
		return blankValue;
	}

	public int getBlankTilePos() {
		return blankTilePos;
	}

	public Matrix getParent() {
		return parent;
	}

	public void setParent(Matrix value) {
		this.parent = value;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	// create state Id based on how the tiles are aligned
	// ease the checking / searching when it's in a collection
	public int getId() {
		this.Id = 0;
		int n = 1;
		for (int i = 0; i < (boardSize * boardSize) - 1; i++) {
			if (tiles.get(i).getNumber() == blankValue) {
				blankTilePos = i;
			}
			this.Id += tiles.get(i).getNumber() * n;
			n *= 10;
		}
		return Id;
	}
	
	public Object clone() {
		
		Matrix result = new Matrix(this);
		result.tiles = new ArrayList<Tile>(this.tiles);
		
        return result;
    }

	public void Shuffle() {
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
			int a = rnd.nextInt(length);

			if (i != a) {
				Tile t = tiles.get(i);
				tiles.set(i, tiles.get(a));
				tiles.set(a, t);

				if (tiles.get(i).getNumber() == blankValue) {
					blankTilePos = i;
				} else if (tiles.get(a).getNumber() == blankValue) {
					blankTilePos = a;
				}
			}
		}
		getId();
	}

	public void makeMove(Direction direction) {
		
		int position;
		if (direction == Direction.UP) {
			position = blankTilePos - boardSize;
		} else if (direction == Direction.DOWN) {
			position = blankTilePos + boardSize;
		} else if (direction == Direction.LEFT) {
			position = blankTilePos - 1;
		} else// if (direction == Direction.RIGHT)
		{
			position = blankTilePos + 1;
		}
		//Log.d("acdp", "position pos: " + position + " - value: " + tiles.get(position).getNumber());

		Tile t = tiles.get(blankTilePos);
		tiles.set(blankTilePos, tiles.get(position));
		tiles.set(position, t);

		//Log.d("acdp", "blank pos2: " + blankTilePos + " - value: " + tiles.get(blankTilePos).getNumber());
		//Log.d("acdp", "position pos2: " + position + " - value: " + tiles.get(position).getNumber());

		blankTilePos = position;
		getId();
	}

	public boolean CanMoveUp() {
		return blankTilePos > boardSize - 1;
	}

	public boolean CanMoveDown() {
		return blankTilePos < length - boardSize;
	}

	public boolean CanMoveLeft() {
		return GameEngine.IndexCols[blankTilePos] > 0;
	}

	public boolean CanMoveRight() {
		return GameEngine.IndexCols[blankTilePos] < boardSize - 1;
	}
}
