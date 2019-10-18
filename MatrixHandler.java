package Mathlab;

import java.io.*;
import java.util.*;
import java.math.*;

public class MatrixHandler
{
	private Matrix original;
	private Matrix transpose;
	private Matrix inverse;
	private Matrix ref;
	private Matrix rref;
	private Matrix nullspace;
	private Matrix colspace;
	private Matrix rowspace;
	private int[] leadvars;
	private int[] freevars;
	private int width, height;
	private int rank, nullity;
	private int rowswitching=0;
	private int maxlen=0;

	private Fraction det;

	private Record toref;
	private Record torref;
	private Record findinverse;

	public MatrixHandler(Matrix m)
	{
		transpose = m.getTranspose();
		width = m.getWidth();
		height = m.getHeight();
		toref = new Record();
		torref = new Record();
		findinverse = new Record();
		for(int r=0; r<height; r++)
			for(int c=0; c<width; c++)
				if(!m.get(r, c).valid())
				{
					m.set(r, c, 0);
					toref.append(0, r, c, null, m);
				}
		original = m;
		SolveEverything();
	}
	private void SolveEverything()
	{
		int r=0, c=0;
		int i=0, j=0, k=0;
		Fraction mul;
		// REF
		int nonzero;
		ref = original.copy();
		while(r < height && c < width)
		{
			for(nonzero = r+1; nonzero<height; nonzero++)
				if(!ref.get(nonzero, c).iszero())
					break;
			if(ref.get(r, c).iszero())
			{
				if(nonzero < height)
				{
					ref.rowop1(r, nonzero);
					lenupdate(ref);
					toref.append(1, r, nonzero, null, ref.copy());
					rowswitching++;
				}
				else
					c++;
				continue;
			}
			Fraction negpivot = ref.get(r, c).inverse();
			for(;nonzero < height; nonzero++)
				if(!ref.get(nonzero, c).iszero())
				{
					mul = ref.get(nonzero, c).div(negpivot);
					ref.rowop3(nonzero, r, mul);
					lenupdate(ref);
					toref.append(3, nonzero, r, mul, ref.copy());
				}
			rank++;
			r++;
			c++;
		}
		nullity = width - rank;
		leadvars = new int[rank];
		freevars = new int[nullity];
		// RREF
		rref = ref.copy();
		r=0; c=0;
		while(r < height && c < width)
		{
			Fraction f = rref.get(r, c);
			if(f.getDenominator().compareTo(BigInteger.ONE)==0 && f.getNumerator().compareTo(BigInteger.ONE)==0)
			{
				for(nonzero=r-1; nonzero>=0; nonzero--)
				{
					mul = rref.get(nonzero, c);
					if(!mul.iszero())
					{
						mul = mul.inverse();
						rref.rowop3(nonzero, r, mul);
						lenupdate(rref);
						torref.append(3, nonzero, r, mul, rref.copy());
					}
				}
				leadvars[r] = c;
				r++;
			}
			else if(!f.iszero())
			{
				mul = new Fraction(1);
				mul = mul.div(f);
				rref.rowop2(r, mul);
				lenupdate(rref);
				torref.append(2, r, 0, mul, rref.copy());
				continue;
			}
			else
				freevars[j++] = c;
			c++;
		}
		while(j < nullity)
			freevars[j++] = c++;
		// DETERMINANT
		if(width == height)
			if(rank < width)
				det = new Fraction(0);
			else
			{
				det = new Fraction(1);
				for(i=0; i<rank; i++)
					det = det.multiply(ref.get(i, leadvars[i]));
			}
		else
			det = new Fraction();
		// COLUMN SPACE AND ROW SPACE AND NULL SPACE
		colspace = new Matrix(height, rank);
		rowspace = new Matrix(width, rank);
		nullspace = new Matrix(width, nullity);
		r=0;
		c=0;
		j=0;
		while(r < height && c < width)
		{
			Fraction f = rref.get(r, c);
			if(f.getNumerator().compareTo(BigInteger.ONE) == 0)
			{
				for(i=0; i<height; i++)
					colspace.set(i, r, original.get(i, c));
				for(i=0; i<width; i++)
					rowspace.set(i, r, rref.get(r, i));
				for(i=0; i<nullity; i++)
					nullspace.set(j, i, rref.get(r, freevars[i]).inverse());
				j++;
				r++;
			}
			else
			{
				for(i=0; i<nullity; i++)
					nullspace.set(j, i, new Fraction( i==k ? 1 : 0));
				k++;
				j++;
			}
			c++;
		}
		while(j<width)
		{
			for(i=0; i<nullity; i++)
				nullspace.set(j, i, new Fraction( i==k ? 1 : 0));
			k++;
			j++;
			
		}
		// INVERSE
		if(rank == width && rank == height)
		{
			if(rank == 1)
			{
				inverse = new Matrix(1, 1);
				Fraction f = original.get(0, 0);
				inverse.set(0, 0, new Fraction(f.getDenominator(), f.getNumerator()));
			}
			else
			{
				inverse = new Matrix(height, width*2);
				for(i=0; i<width; i++)
					for(j=0; j<height; j++)
						inverse.set(j, i, original.get(j, i));
				for(i=width; i<width*2; i++)
					for(j=0; j<height; j++)
						inverse.set(j, i, (j==i-width ? 1 : 0));
				findinverse.append(4, 0, 0, null, inverse.copy());
				ListIterator iter = toref.iter();
				Record.Data data;
				i = 0;
				while(iter.hasNext())
				{
					data = (Record.Data)iter.next();
					if(data.type == 1)
						inverse.rowop1(data.row1, data.row2);
					else if(data.type == 2)
						inverse.rowop2(data.row1, data.mul);
					else if(data.type == 3)
						inverse.rowop3(data.row1, data.row2, data.mul);
					lenupdate(inverse);
					findinverse.append(data.type, data.row1, data.row2, data.mul, inverse.copy());
					if(i==0 && !iter.hasNext())
					{
						iter = torref.iter();
						i++;
					}
				}
				Matrix temp = new Matrix(width, height);
				for(i=0; i<width; i++)
					for(j=0; j<height; j++)
						temp.set(j, i, inverse.get(j, i+width));
				inverse = temp;
			}
		}
	}
	private void lenupdate(Matrix m)
	{
		int temp;
		for(int i=0; i<m.getHeight(); i++)
			for(int j=0; j<m.getWidth(); j++)
			{
				temp = m.get(i, j).toString().length()+1;
				if(temp > maxlen)
					maxlen = temp;
			}
	}
	public void printRecord(PrintStream ps)
	{
		ListIterator iter = toref.iter();
		Record.Data data;
		int step = 0;
		int i, j;
		if(toref.length() > 0)
		{
			ps.println("Reducing to Row Echelon Form(REF): ");
			while(iter.hasNext())
			{
				ps.print(String.format("Step%3d. ", ++step));
				data = (Record.Data)iter.next();
				if(data.type == 0)
					ps.println(String.format("Undefined value at (%d, %d) automaticlly changed to 0.", data.row1+1, data.row2+1));
				else if(data.type == 1)
					ps.println(String.format("Row-switching: R%d <-> R%d", data.row1+1, data.row2+1));
				else if(data.type == 2)
					ps.println(String.format("Row Multiplication: R%d <- (%s) * R%d", data.row1+1, data.mul, data.row1+1));
				else if(data.type == 3)
					ps.println(String.format("Row Addition: R%d <- R%d + (%s) * R%d", data.row1+1, data.row1+1, data.mul, data.row2+1));
				for(i=0; i<data.matrix.getHeight(); i++)
				{
					for(j=0; j<data.matrix.getWidth(); j++)
						ps.print(String.format("%"+maxlen+"s", data.matrix.get(i, j)));
					ps.println();
				}
				ps.println();
			}
			if(rank == width && width == height)
				ps.println(String.format("Notice: There are %d pivots in this square matrix and thus it is full rank.\n        That means the RREF must be the identity metrix.", rank));
		}
		else
			ps.println("This matrix is already in Row Echelon Form(REF).");
		if(torref.length() > 0)
		{
			ps.println("Then we continue to reduce it to Reduced Row Echelon Form(RREF): \n");
			iter = torref.iter();
			while(iter.hasNext())
			{
				ps.print(String.format("Step%3d. ", ++step));
				data = (Record.Data)iter.next();
				if(data.type == 1)
					ps.println(String.format("Row-switching: R%d <-> R%d", data.row1+1, data.row2+1));
				else if(data.type == 2)
					ps.println(String.format("Row Multiplication: R%d <- (%s) * R%d", data.row1+1, data.mul, data.row1+1));
				else if(data.type == 3)
					ps.println(String.format("Row Addition: R%d <- R%d + (%s) * R%d", data.row1+1, data.row1+1, data.mul, data.row2+1));
				for(i=0; i<data.matrix.getHeight(); i++)
				{
					for(j=0; j<data.matrix.getWidth(); j++)
						ps.print(String.format("%"+maxlen+"s", data.matrix.get(i, j)));
					ps.println();
				}
				ps.println();
			}
		}
		else
		{
			ps.println("\nIt is also in Reduced Row Echelon Form(RREF). \n");
		}
		ps.println("The Reduced Row Echelon Form(RREF):");
		ps.println(rref);
		if(rank == 0)
			ps.println("There are no pivots and thus all variables are free variables.");
		else if(rank == 1)
		{
			if(width == 1)
				ps.println("The only one column is a pivot variable because it is not 0.");
			else if(width == 2)
				ps.println(String.format("Column %d has a pivot and thus x_%d is free variable and x_%d is leading/basic variables.", leadvars[0], leadvars[0], freevars[0]));
			else
				ps.println(String.format("There is only 1 pivot at column x_%d and thus all variables except x_%d are free variables.", leadvars[0], leadvars[0]));
		}
		else if(nullity == 0)
			ps.println("Since it is full rank, there is no free variables.");
		else if(nullity == 1)
			ps.println(String.format("Clearly, x_%d is the only one free variables because there is no pivot in this column.", freevars[0]));
		else
		{
			ps.print(String.format("From the RREF, we can see there are %d leading/basic variables:", rank));
			for(i=0; i<rank; i++)
				ps.print(" x_" + (leadvars[i]+1));
			ps.print(String.format(", and %d free variables:", nullity));
			for(i=0; i<nullity; i++)
				ps.print(" x_" + (freevars[i]+1));
			ps.println();
		}
		ps.println(String.format("\nTherefore, the dimension of column space of this matrix is %d.", rank));
		if(rank > 0)
		{
			ps.print("A basis of the column space of this matrix is fromed by: \n{");
			for(i=0; i<rank; i++)
			{
				ps.print(String.format("column_%d=[", leadvars[i]+1));
				for(j=0; j<height; j++)
					if(j==height-1)
						ps.print(colspace.get(j, i) + "]");
					else
						ps.print(colspace.get(j, i) + ", ");
				if(i == rank-1)
					ps.print("}\n");
				else
					ps.print(", ");
			}
		}
		else
			ps.println("That means the column space is 0-dimension and just contains the zero vector.");
		ps.println(String.format("\nThe dimension of row space of this matrix equals the dimension of its RREF and thus is %d.", rank));
		if(rank > 0)
		{
			ps.print("A basis of the row space of this matrix is fromed by: \n{");
			for(i=0; i<rank; i++)
			{
				ps.print(String.format("row_%d=[", i+1));
				for(j=0; j<width; j++)
					if(j==width-1)
						ps.print(rowspace.get(j, i) + "]");
					else
						ps.print(rowspace.get(j, i) + ", ");
				if(i == rank-1)
					ps.print("}\n");
				else
					ps.print(", ");
			}
		}
		else
			ps.println("That means the row space is also 0-dimension and just contains the zero vector.");
		ps.println("\nWe can see that dim(C(A)) = dim(R(A)) = rank.\n");
		ps.println("The null space of this matrix comes from solving Ax=0.");
		if(nullity == 0)
		{
			ps.print("Since this matrix is full rank, the only solution is trivial solution (i.e. x=[");
			for(i=0; i<width; i++)
				if(i==width-1)
					ps.println("0]).");
				else
					ps.print("0, ");
		}
		else
		{
			for(i=0; i<nullity; i++)
			{
				ps.print(String.format("Step%3d. Let x_%d be 1 and other free variable(s) be 0, and we get solution:\n         {", i+1, freevars[i]+1));
				for(j=0; j<width; j++)
					if(j==width-1)
						ps.println(String.format("x_%d=%s}", j+1, nullspace.get(j, i)));
					else
						ps.print(String.format("x_%d=%s, ", j+1, nullspace.get(j, i)));
			}
			ps.println("\nThen we get the null space matrix:");
			ps.println(nullspace);
		}

		if(det.valid())
		{
			if(!det.iszero())
			{
				ps.println("The determinant of this matrix is:");
				ps.print("det(A) = ");
				if(rowswitching % 2 > 0)
					ps.print("-det(REF) = -(");
				else
					ps.print("det(REF) = ");
				for(i=0; i<rank; i++)
				{
					ps.print(String.format("(%s)", ref.get(i, leadvars[i])));
					if(i != rank-1)
						ps.print(" * ");
				}
				if(rowswitching % 2 > 0)
					ps.println(String.format(") = %s\nNotice: Because we have interchanged rows %d times (an odd number), det(A)=-det(REF).", det, rowswitching));
				else
					ps.println(" = " + det);
				if(rank == 1)
					ps.println("Since the matrix is 1x1, the inverse is just the reciprocal of this number:");
				else
				{
					ps.println("Therefore, we can find the inverse of this matrix:\n");
					iter = findinverse.iter();
					step = 0;
					while(iter.hasNext())
					{
						ps.print(String.format("Step%3d. ", ++step));
						data = (Record.Data)iter.next();
						if(data.type == 1)
							ps.println(String.format("Row-switching: R%d <-> R%d", data.row1+1, data.row2+1));
						else if(data.type == 2)
							ps.println(String.format("Row Multiplication: R%d <- (%s) * R%d", data.row1+1, data.mul, data.row1+1));
						else if(data.type == 3)
							ps.println(String.format("Row Addition: R%d <- R%d + (%s) * R%d", data.row1+1, data.row1+1, data.mul, data.row2+1));
						else if(data.type == 4)
							ps.println("We begin with writing (A|I):");
						for(i=0; i<data.matrix.getHeight(); i++)
						{
							for(j=0; j<data.matrix.getWidth(); j++)
								ps.print(String.format("%"+maxlen+"s", data.matrix.get(i, j)));
							ps.println();
						}
						ps.println();
					}
					ps.println("\nThe inverse of matrix A is:");
				}
				ps.println(inverse);
				
			}
			else
				ps.println("The determinant of this matrix is 0 because it is not full-rank.");
		}
	}
	public String getNotes()
	{
		return toref.toString()+torref.toString();
	}
	public Matrix getMatrix()
	{
		return original;
	}
	public Matrix getInverse()
	{
		return inverse;
	}
	public Matrix getREF()
	{
		return ref;
	}
	public Matrix getRREF()
	{
		return rref;
	}
	public Matrix getColspace()
	{
		return colspace;
	}
	public Matrix getRowspace()
	{
		return rowspace;
	}
	public Matrix getNullspace()
	{
		return nullspace;
	}
	public int getRank()
	{
		return rank;
	}
	public Fraction getDet()
	{
		return det;
	}
	public int getNullity()
	{
		return nullity;
	}
}
