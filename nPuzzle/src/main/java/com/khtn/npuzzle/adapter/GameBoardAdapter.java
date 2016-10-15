package com.khtn.npuzzle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khtn.npuzzle.R;
import com.khtn.npuzzle.pojo.Matrix;
import com.khtn.npuzzle.pojo.Tile;

public class GameBoardAdapter extends BaseAdapter {

	private Matrix tiles;
	private Context context;
	private int resID;
	private int boardSize;

	public GameBoardAdapter(Context context, int resID, Matrix tiles) {
		this.context = context;
		this.resID = resID;
		this.tiles = tiles;

		boardSize = tiles.getBoardSize();
	}

	@Override
	public int getCount() {
		return tiles.getLength();
	}

	@Override
	public Object getItem(int position) {
		return tiles.getTiles().get(position);
	}

	@Override
	public long getItemId(int position) {
		return tiles.getTiles().get(position).getNumber();
	}

	private class ViewHolder {
		private TextView textNumber;
		private ImageView imgTile;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		Tile tile;

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = View.inflate(context, resID, null);

			holder.textNumber = (TextView) convertView
					.findViewById(R.id.tile_number);
			holder.imgTile = (ImageView) convertView
					.findViewById(R.id.tile_image);
			// holder.imgTile.setAlpha(0.2f);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		tile = tiles.getTiles().get(position);
		int imgResID = 0;

		if (tile.allowShowNumber()) {
			holder.textNumber.setText(String.valueOf(tile.getNumber()));
			imgResID = R.drawable.fondo_tablero;

		} else {
			holder.textNumber.setText("");

			if ((position + 1) < boardSize) {
				imgResID = R.drawable.fondo_tablero_r;
			} else if ((position + 1) == boardSize) {
				imgResID = R.drawable.fondo_tablero_;
			} else if ((position + 1) % boardSize == 0) {
				imgResID = R.drawable.fondo_tablero_t;
			} else {
				imgResID = R.drawable.fondo_tablero_cl;
			}
		}
		holder.imgTile.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(), imgResID));

		return convertView;
	}

}
