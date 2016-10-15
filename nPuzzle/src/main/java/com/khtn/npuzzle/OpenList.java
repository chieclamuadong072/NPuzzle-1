package com.khtn.npuzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.khtn.npuzzle.pojo.Matrix;

public class OpenList {

	private List<Matrix> openList;
	private HashSet<Integer> idList;
	
	public OpenList()
    {
		openList = new ArrayList<Matrix>();
		idList = new HashSet<Integer>();
    }
	
	public Matrix getMatrix(int index)
	{
		return openList.get(index);
	}

    public void Add(Matrix item)
    {
        if(idList.contains(item.getId()))
        {
            return;
        }
        idList.add(item.getId());
        
        for (int i = 0; i < openList.size(); i++)
        {                

            if (item.getComparingValue() <= openList.get(i).getComparingValue())
            {
            	openList.add(i, item);
                return;
            }
        }
        openList.add(item);
    }
    public void Clear()
    {
    	openList.clear();
        idList.clear();
    }
    
    public void Remove(Matrix item)
    {
        idList.remove(item.getId());
        openList.remove(item);
    }

    public int Count()
    { 
    	return openList.size(); 
    }   
}
