package com.khtn.npuzzle;

import java.util.Stack;
import java.util.TreeMap;

import com.khtn.npuzzle.myenum.Direction;
import com.khtn.npuzzle.pojo.Matrix;

public class GameEngine {
	
	public static int[] IndexRows;
    public static int[] IndexCols;
    
    private int size = 3;
    private Matrix matrix;
    private int WIN_VALUE = 0;
    public OpenList openQ;

    private TreeMap<Integer, Matrix> closeQ;
    public Stack<Direction> solution;

	public GameEngine()
    {
        openQ = new OpenList();
        closeQ = new TreeMap<Integer, Matrix>();

        solution = new Stack<Direction>();
    }
    
    public int getSize()
    {
    	return this.size;
    }
    
    public Stack<Direction> getSolution() {
		return solution;
	}

	public void setSize(int value) {
		this.size = value;
		matrix = new Matrix(this.size);

		int m = this.size * this.size;
		IndexRows = new int[m];
		IndexCols = new int[m];
		for (int i = 0; i < m; i++) {
			IndexRows[i] = i / this.size;
			IndexCols[i] = i % this.size;
		}
	}
	
	public Matrix getMatrix()
	{
		return this.matrix;
	}
	
	public void setMatrix(Matrix matrix)
	{
		this.matrix = matrix;
	}
	
    public void Shuffle()
    {
        do
        {
            this.matrix.Shuffle();
        }
        while (!CanSolve(this.matrix));
    }
    public boolean CanSolve()
    {
        return CanSolve(this.matrix);
    }
    public boolean CanSolve(Matrix matrix)
    {
        int value = 0;
        for (int i = 0; i < matrix.getLength(); i++)
        {
            int t = matrix.getTiles().get(i).getNumber();
            if (t > 1 && t < matrix.getBlankValue())
            {
                for (int m = i + 1; m < matrix.getLength(); m++)
                {
                    if (matrix.getTiles().get(m).getNumber() < t)
                    {
                        value++;
                    }
                }
            }
        }

        if (size % 2 == 1)
        {
            return value % 2 == 0;
        }
        else
        {
            int row = IndexRows[matrix.getBlankTilePos()] + 1;
            return value % 2 == row % 2;
        }
    }
    
    public void Solve()
    {
        closeQ.clear();
        openQ.Clear();
        solution.clear();

        // Add current state to OPEN
        this.matrix.setParent(null);
        this.matrix.setHeuristicScore(Evaluate(this.matrix));
        openQ.Add(this.matrix);

        while (openQ.Count() > 0)
        {
            // Get state with smallest ComparingValue
            Matrix m = openQ.getMatrix(0);
            
            // Check if state is goal
            if (m.getHeuristicScore() == WIN_VALUE)
            {
                // Create solution
                TrackPath(m);
                return;
            }

            // Remove 1st element of OPEN
            openQ.Remove(m);
            
            // Generate next states from m
            GenMove(m);
        }
    }
    
    private void TrackPath(Matrix matrix)
    {
        if (matrix.getParent() != null)
        {
            solution.push(matrix.getDirection());
            TrackPath(matrix.getParent());
        }
    }
    
    private void GenMove(Matrix matrix)
    {
        Matrix m1;
        // If this state is verified
        if (closeQ.containsKey(matrix.getId()))
        {
            m1 = closeQ.get(matrix.getId());
            // Update if step count is smaller in CLOSE
            if (matrix.getStepCount() < m1.getStepCount())
            {
                m1 = matrix;
            }
        }
        else
        {
            closeQ.put(matrix.getId(), matrix);
        }
        
        // Create child states
        if (matrix.getDirection() != Direction.LEFT && matrix.CanMoveRight())
        {
            CloneMove(matrix, Direction.RIGHT);
        }
        if (matrix.getDirection() != Direction.UP && matrix.CanMoveDown())
        {
            CloneMove(matrix, Direction.DOWN);
        }
        if (matrix.getDirection() != Direction.RIGHT && matrix.CanMoveLeft())
        {
            CloneMove(matrix, Direction.LEFT);
        }
        if (matrix.getDirection() != Direction.DOWN && matrix.CanMoveUp())
        {
            CloneMove(matrix, Direction.UP);
        }
    }
    private void CloneMove(Matrix parent, Direction direction)
    {
        // Create and update child states' value
        //Matrix m = new Matrix(parent);
		Matrix m = (Matrix) parent.clone();
        m.makeMove(direction);
        m.setDirection(direction);
        m.increaseStep();
        
        // If this state is already in CLOSE
        if (closeQ.containsKey(m.getId()))
        {
            Matrix m1 = closeQ.get(m.getId());
            if (m.getStepCount() < m1.getStepCount())
                m1 = m;
        }
        else
        {
            // Add to OPEN
            m.setParent(parent);
            m.setHeuristicScore(Evaluate(m));
            openQ.Add(m);
        }
    }
    public int Evaluate(Matrix matrix)
    {
        // Wrong position tile is evaluated by summing its value with value of itself in the right position
        int score = 0;

        for (int i = 0; i < matrix.getLength(); i++)
        {
            int value = matrix.getTiles().get(i).getNumber() - 1;
            score += Math.abs(IndexRows[i] - IndexRows[value]) + Math.abs(IndexCols[i] - IndexCols[value]);
        }
        return score;
    }
}
