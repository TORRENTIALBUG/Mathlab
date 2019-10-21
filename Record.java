package Mathlab;

import java.util.*;

public class Record
{
	private LinkedList<Data> data;
	private int length=0;
	
	class Data
	{
		public int type;
		public int row1, row2;
		public Fraction mul;
		public Matrix matrix;
		public Data(int t, int r1, int r2, Fraction m, Matrix mat)
		{
			type = t;
			row1 = r1;
			row2 = r2;
			mul = m;
			matrix = mat;
		}
	}

	public Record()
	{
		data = new LinkedList<Data>();
	}
	public Record(int t, int r1, int r2, Fraction m, Matrix mat)
	{
		data = new LinkedList<Data>();
		data.add(new Data(t, r1, r2, m, mat));
		length++;
	}

	public void append(int t, int r1, int r2, Fraction m, Matrix mat)
	{
		data.add(new Data(t, r1, r2, m, mat));
		length++;
	}
	public int length()
	{
		return length;
	}	

	public ListIterator<Data> iter()
	{
		return data.listIterator(0);
	}
		
	public static void main(String[] args)
	{
		Record r = new Record();
	}
}
