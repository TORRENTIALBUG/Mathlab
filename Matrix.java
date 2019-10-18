package Mathlab;

import java.math.*;

public class Matrix
{
	Fraction[][] table;

	public Matrix(){}
	public Matrix(Fraction[][] t)
	{
		table = t;
	}
	public Matrix(int i, int j)
	{
		table = new Fraction[i][j];
		for(int m=0; m<table.length; m++)
			for(int n=0; n<table[m].length; n++)
				table[m][n] = new Fraction(0);
	}
	
	public Fraction get(int i, int j)
	{
		return table[i][j];
	}
	public void set(int i, int j, int value)
	{
		table[i][j] = new Fraction(value);
	}
	public void set(int i, int j, BigInteger value)
	{
		table[i][j] = new Fraction(value);
	}
	public void set(int i, int j, Fraction value)
	{
		table[i][j] = value;
	}
	public int getWidth()
	{
		return table[0].length;
	}
	public int getHeight()
	{
		return table.length;
	}

	// Row-switching (r1 <-> r2)
	public void rowop1(int r1, int r2)
	{
		if(r1 < 0 || r1 >= table.length || r2 < 0 || r2 >= table.length || r1 == r2)
			return;
		Fraction temp;
		for(int i=0; i<table[0].length; i++)
		{
			temp = table[r1][i];
			table[r1][i] = table[r2][i];
			table[r2][i] = temp;
		}
	}
	// Row Multiplication (r1 <- r1*m)
	public void rowop2(int r1, int m)
	{
		if(r1 < 0 || r1 >= table.length || m == 0)
			return;
		for(int i=0; i<table[0].length; i++)
			table[r1][i] = table[r1][i].multiply(m);
	}
	public void rowop2(int r1, Fraction m)
	{
		if(r1 < 0 || r1 >= table.length || m.iszero())
			return;
		for(int i=0; i<table[0].length; i++)
			table[r1][i] = table[r1][i].multiply(m);
	}
	// Row Addition (r1 <- r1 + r2*m)
	public void rowop3(int r1, int r2, int m)
	{
		if(r1 < 0 || r1 >= table.length || r2 < 0 || r2 >= table.length)
			return;
		for(int i=0; i<table[0].length; i++)
			table[r1][i] = table[r1][i].plus(table[r2][i].multiply(m));
	}
	public void rowop3(int r1, int r2, Fraction m)
	{
		if(r1 < 0 || r1 >= table.length || r2 < 0 || r2 >= table.length)
			return;
		for(int i=0; i<table[0].length; i++)
			table[r1][i] = table[r1][i].plus(table[r2][i].multiply(m));
	}
	public Matrix getTranspose()
	{
		Matrix t = new Matrix(table[0].length, table.length);
		for(int i=0; i<table[0].length; i++)
			for(int j=0; j<table.length; j++)
				t.set(i, j, get(j, i));
		return t;
	}
	public Matrix multiply(Matrix m)
	{
		if(m.getHeight() != table[0].length)
			return null;
		Matrix result = new Matrix(table.length, m.getWidth());
		for(int i=0; i<table.length; i++)
			for(int j=0; j<m.getWidth(); j++)
			{
				Fraction sum = new Fraction(0);
				for(int k=0; k<table[0].length; k++)
					sum = sum.plus(get(i, k).multiply(m.get(k, j)));
				result.set(i, j, sum);
			}
		return result;
	}
	public Matrix copy()
	{
		Matrix c = new Matrix(table.length, table[0].length);
		for(int i=0; i<table.length; i++)
			for(int j=0; j<table[0].length; j++)
				c.set(i, j, get(i, j));
		return c;
	}
	public int getMaxlen()
	{
		int maxlen = 0, len;
		for(int i=0; i<table.length; i++)
			for(int j=0; j<table[i].length; j++)
			{
				len = get(i, j).toString().length();
				if(len > maxlen)
					maxlen = len;
			}
		return maxlen+1;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		String fstr = "%"+getMaxlen()+"s";
		sb.append(table.length);
		sb.append("x");
		sb.append(table[0].length);
		sb.append(" Matrix:\n\n");
		for(int i=0; i<table.length; i++)
		{
			for(int j=0; j<table[i].length; j++)
			{
				sb.append(String.format(fstr, get(i, j)));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
