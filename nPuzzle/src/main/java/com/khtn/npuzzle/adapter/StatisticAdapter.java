package com.khtn.npuzzle.adapter;

import java.util.ArrayList;

import com.khtn.npuzzle.R;
import com.khtn.npuzzle.model.TopUserScore;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StatisticAdapter extends ArrayAdapter<TopUserScore>{
	Activity context;
	ArrayList<TopUserScore> array = null;
	int layoutID;
	public StatisticAdapter(Activity _context, int _layoutId,ArrayList<TopUserScore> _array) 
	{
		super(_context, _layoutId, _array);
		this.context = _context;
		this.layoutID = _layoutId;
		this.array =_array;
	}
	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = this.context.getLayoutInflater();
		convertView = inflater.inflate(layoutID, null);
		if(array.size()>0)
		{
			final TextView txtPlayerNick = (TextView) convertView.findViewById(R.id.txtPlayerNickName);
			final TextView txtPlayerScore = (TextView) convertView.findViewById(R.id.txtPlayerScore);
			//final LinearLayout statisticrow = (LinearLayout) convertView.findViewById(R.id.layoutStatisticRow);
			final ImageView imgRank = (ImageView) convertView.findViewById(R.id.imageRank);
			final TopUserScore player = array.get(position);
			
			txtPlayerNick.setText(player.getNickname());
			txtPlayerScore.setText(player.getTongDiem()+"");
			switch (position) {
			case 0:
				imgRank.setImageResource(R.drawable.firstrank);
				break;
			case 1:
				imgRank.setImageResource(R.drawable.secondrank);
				break;
			case 2:
				imgRank.setImageResource(R.drawable.thirdrank);
				break;
			default:
				//imgRank.setImageResource(R.drawable.player);
				break;
			}
		}
		return convertView;
	}
}
