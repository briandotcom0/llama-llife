package com.bjj.life;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.ExpandableListView.*;
import android.util.*;
import android.graphics.drawable.*;
import android.graphics.*;

public class MainActivity extends Activity
{
	int[]colors;
	Game game;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		colors=new int[]{0x11111111,0x00FFFFFF,0x00FF0000,0x000000FF};
//		colors=new int[]{0b0000000000000000, 0b1111100000000000, 0b0000000000011111, 0b1111111111111111};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		TextView stuff = (TextView)findViewById(R.id.stuff);
Log.d("life","exit onCreate");
		
	}
	public void init(View v)
	{
		
		//setContentView(R.layout.game);
Log.d("life","init");
		int x=Integer.parseInt(((EditText)findViewById(R.id.etx)).getText().toString());
		int y=Integer.parseInt(((EditText)findViewById(R.id.ety)).getText().toString());
		int numC=Integer.parseInt(((EditText)findViewById(R.id.etc)).getText().toString());
		if(x<=0)
		{
			Log.e("life","x invalid "+x);
			x=5;
		}
		if(y<=0)
		{
			Log.e("life","y invalid "+y);
			y=5;
		}
		if(numC<1 || numC>3)
		{
			Log.e("life","numC invalid "+numC);
			numC=1;
		}
		setContentView(R.layout.game);
		game=new Game(x,y,numC);
		game.randomize();
Log.d("life","x="+x+" y="+y);
		GridLayout gv=(GridLayout)findViewById(R.id.grid);
Log.d("life",gv.toString());
		gv.setColumnCount(y);
		gv.setRowCount(x);
		for(int r=0;r<x;r++)
			for(int c=0;c<y;c++)
			{
				int i=c+r*y;
				
				ImageView iv=new ImageView(getApplicationContext());
				int color;
				if(game.get(r,c)==10)
					color=2;
				else if(game.get(r,c)==100)
					color=3;
				else
					color=game.get(r,c);
					
				int sz=20;
				
		//	/*	
				Bitmap b=Bitmap.createBitmap(new int[]{colors[color]}, 1,1, Bitmap.Config.RGB_565);
				Bitmap sb= Bitmap.createScaledBitmap(b, sz, sz, false);
				iv.setImageBitmap(sb);
				iv.setOnClickListener(new CL(r,c));
				iv.setMinimumHeight(25);
				iv.setMinimumWidth(25);
				gv.addView(iv,i);
		//	*/	
			/*
				ToggleButton tb=new ToggleButton(getApplicationContext());
				//tb.setChecked(game.get(r,c)!=0);
				set(tb,game.get(r,c));
				gv.addView(tb,i);
				tb.setOnClickListener(new CL(r,c));
tb.setTextOff(r+", "+c);
tb.setTextOn(i+"");
		//	*/
			}
Log.d("life","exit init");
	}
	/*									||\   ||  |=====  \\   //  ========
	*									||\\  ||  ||       \\ //      ||
	*									|| \\ ||  |=====    >x<       ||
	*									||  \\||  ||       // \\      ||
	*									||   \\|  |=====  //   \\     ||
	*/
	public void next(View v)
	{

Log.d("life","begin next");
		game.next();
Log.d("life","game next finish");
		GridLayout gv=(GridLayout)findViewById(R.id.grid);
		for(int x=0;x<game.getNumRows();x++)
			for(int y=0;y<game.getNumCols();y++)
			{
				int c;
				if(game.get(x,y)==10)
					c=2;
				else if(game.get(x,y)==100)
					c=3;
				else
					c=game.get(x,y);
					
				//((ImageView) gv.getChildAt(x*game.getNumCols()+y)).setBackgroundColor(colors[c]);
				set(gv.getChildAt(x*game.getNumCols()+y), c);
			}
		//findViewById(R.id.grid).invalidate();
Log.d("life","exit next");
	}
	
	private void set(View v, int i)
	{
	//	((ToggleButton)v).setChecked(i!=0);

		Bitmap b=Bitmap.createBitmap(new int[]{colors[i]}, 1,1, Bitmap.Config.RGB_565);
		Bitmap sb= Bitmap.createScaledBitmap(b, 20, 20, false);
		((ImageView)v).setImageBitmap(sb);
	}
	
	public void showSettings(View v)
	{
		setContentView(R.layout.main);
	}
	
	private class CL implements OnClickListener
	{
		private int x;
		private int y;
		public CL(int x, int y)
		{
			this.x=x;
			this.y=y;
		}
		public void onClick(View v)
		{
			int i=game.toggle(x,y);
//Log.d("touch","click: "+x+", "+y+": "+i);
			set(v,game.get(x,y));
			v.invalidate();
		}
		
	}
}
