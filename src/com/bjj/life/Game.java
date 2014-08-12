package com.bjj.life;
import android.util.*;

public class Game
{
//[r][c]==[x][y]
//0==false
//intergalactic laws
	private int rows;
	private int cols;
	boolean edgeWrap;
	private int[][] cells;
	//rules
	private int colors;//1==bw(default) [1-3 valid]
	private int[] ksb;
	
	public Game()
	{	this(10,10,1);	}
	public Game(int x)
	{	this(x,x,1);	}
	public Game(int x, int y)
	{	this(x,y,1);	}
	public Game(int x, int y,int c)
	{	this(x,y,c,new int[]{0,0,1,2,0,0,0,0,0,0});	}
	public Game(int x, int y,int c,int[] doWat)
	{
		edgeWrap=false;
		rows=x;
		cols=y;
		cells=new int[x][y];
		colors=c;
		ksb=doWat;
	}
	
	public int toggle(int x, int y)
	{
		if(x>=rows||y>=cols)
		{
//Log.e("OOPS!!!! "+x+' '+y);
			ArrayIndexOutOfBoundsException e=new ArrayIndexOutOfBoundsException("LOL, you suck at programming/maths "+x+' '+y);
//e.printStackTrace();
throw e;
//return false;
		}
		//if(cells[x][y]==0)
		//	cells[x][y]=1;
		//else
		//	cells[x][y]=0;
		cells[x][y]++;
		cells[x][y]%=(colors+1);
		return cells[x][y];
	}
	public int get(int x, int y)
	{	return cells[x][y];	}
	public int on(int x, int y)
	{
		return set(x,y,1);
	}
	public int off(int x, int y)
	{
		return set(x,y,0);
	}
	public int set(int x, int y, int v)
	{
		if(x>=rows||y>=cols)
		{
//			System.out.println("OOPS!!!! "+x+' '+y);
			ArrayIndexOutOfBoundsException e=new ArrayIndexOutOfBoundsException("LOL, you suck at programming "+x+' '+y);
//			e.printStackTrace();
			throw e;
//			return false;
		}
		int r=cells[x][y];
		cells[x][y]=v;
		return r;
	}
	
	protected void next()
	{
		int[][]neighbors=new int[rows][cols];
		for(int x=0; x<rows; x++)
		{
			for(int y=0; y<cols; y++)
			{
				if(cells[x][y]!=0)
				{
					int cur=(int)Math.pow(10,cells[x][y]-1);
					if(x-1>=0)
					{
						if(y-1>=0)
							neighbors[x-1][y-1]+=cur;
						else if(edgeWrap)
							neighbors[x-1][cols-1]+=cur;
						neighbors[x-1][y]+=cur;
						if(y+1<cols)
							neighbors[x-1][y+1]+=cur;
						else if(edgeWrap)
							neighbors[x-1][0]+=cur;
							
					}
					else if(edgeWrap)
					{

						if(y-1>=0)
							neighbors[rows-1][y-1]+=cur;
						else if(edgeWrap)
							neighbors[rows-1][cols-1]+=cur;
						neighbors[rows-1][y]+=cur;
						if(y+1<cols)
							neighbors[rows-1][y+1]+=cur;
						else if(edgeWrap)
							neighbors[rows-1][0]+=cur;
					}
					if(x+1<rows)
					{
						if(y-1>=0)
							neighbors[x+1][y-1]+=cur;
						else if(edgeWrap)
							neighbors[x+1][cols-1]+=cur;
						neighbors[x+1][y]+=cur;
						if(y+1<cols)
							neighbors[x+1][y+1]+=cur;
						if(edgeWrap)
							neighbors[x+1][0]+=cur;
					}
					else if(edgeWrap)
					{
						if(y-1>=0)
							neighbors[0][y-1]+=cur;
						else if(edgeWrap)
							neighbors[0][cols-1]+=cur;
						neighbors[0][y]+=cur;
						if(y+1<cols)
							neighbors[0][y+1]+=cur;
						if(edgeWrap)
							neighbors[0][0]+=cur;
					}
					if(y-1>=0)
						neighbors[x][y-1]+=cur;
					else if(edgeWrap)
						neighbors[x][cols-1]+=cur;
					if(y+1<cols)
						neighbors[x][y+1]+=cur;
					else if(edgeWrap)
						neighbors[x][0]+=cur;
				}//endif(cells[x][y])
			}//for
		}//for
		
		for(int x=0; x<rows; x++)
		{
			for(int y=0; y<cols; y++)
			{
				int[] cnts=new int[colors];
				int cnt=0;
				for(int i=0;i<colors;i++)
				{
					int tmp=neighbors[x][y];
					tmp%=Math.pow(10,i+1);
					tmp/=Math.pow(10,i);
					cnts[i]=tmp;
					cnt+=tmp;
				}
				switch(ksb[cnt])
				{
					case 0:
						cells[x][y]=0;
						break;
					case 1:
						break;
					case 2:
						switch(colors)
						{
							case 1:
								cells[x][y]=1;
								break;
							case 2:
								if(cnts[0]>cnts[1])
									cells[x][y]=1;
								else
									cells[x][y]=2;
								break;
							case 3:
								if(cnts[0]==cnts[1])
									cells[x][y]=3;
								else if(cnts[0]==cnts[2])
									cells[x][y]=2;
								else if(cnts[2]==cnts[1])
									cells[x][y]=1;
								else if(cnts[0]>cnts[1] && cnts[0]>cnts[2])
									cells[x][y]=1;
								else if(cnts[1]>cnts[0] && cnts[1]>cnts[2])
									cells[x][y]=2;
								else //if(cnts[2]>cnts[1] && cnts[2]>cnts[0])
									cells[x][y]=3;
								break;
							default:
android.util.Log.e("life","invalid colors="+colors);
							break;
						}//colors switch
						break;
					default:
android.util.Log.e("life","invalid rules: i "+cnt+" = "+ksb[cnt]);
					break;
				}//switch(ksb[cnt])
/*				if(neighbors[x][y]<2 || neighbors[x][y]>3)
					cells[x][y]=0;
				else if(neighbors[x][y]==3)
					cells[x][y]=1;
*/
			}//for y
		}//for x
		
	}//next()
	protected void randomize()
	{
		for(int x=0; x<rows; x++)
		{
			for(int y=0; y<cols; y++)
			{
				cells[x][y]=(int)((Math.random()*colors*2)+.5)-colors;
				if(cells[x][y]<0)
					cells[x][y]=0;
			}
		}
	}
	
	public String toString()
	{
		String r="";
		for(int x=0; x<rows; x++)
		{
			for(int y=0; y<cols; y++)
			{
				r+= cells[x][y];
			}
			r+='\n';
		}
		return r;
	}
	public int colors()
	{	return colors;	}
	public int getNumCols()
	{	return cols;	}
	public int getNumRows()
	{	return rows;	}
}
