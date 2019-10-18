package Mathlab;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

public class Lab
{
	static Scanner s = new Scanner(System.in);
	public static void sline()
	{
		System.out.println();
		for(int i=0; i<128; i++)
			System.out.print("=");
		System.out.println("\n");
	}
	
	public static int inputI(String prompt)
	{
		System.out.println(prompt);
		System.out.print(" >> ");
		int input=-1;
		try{
			input = s.nextInt();
		}catch(InputMismatchException e){s.nextLine();}
		while(input <= 0)
		{
			System.out.print("Re-enter >> ");
			try{
				input = s.nextInt();
			}catch(InputMismatchException e){s.nextLine();}
		}
		return input;
	}
	public static int inputI(String prompt, int min, int max)
	{
		System.out.println(prompt);
		System.out.print(" >> ");
		int input=-1;
		try{
			input = s.nextInt();
		}catch(InputMismatchException e){s.nextLine();}
		while(input < min || input > max)
		{
			System.out.print("Re-enter >> ");
			try{
				input = s.nextInt();
			}catch(InputMismatchException e){s.nextLine();}
		}
		return input;
	}
	public static Fraction inputF(String prompt)
	{
		System.out.print(prompt);
		System.out.print(" >> ");
		String input;
		Fraction f;
		int i, j, k;
		Pattern p = Pattern.compile("^(-?\\d+)((\\.|/)(\\d+))?$");
		while(true)
		{
			input = s.next();
			Matcher m = p.matcher(input);
			if(m.find())
			{
				if(m.group(2) == null)
					return new Fraction(Integer.parseInt(m.group(1)));
				else if(m.group(3).equals("."))
					return new Fraction(Integer.parseInt(m.group(1)+m.group(4)), (int)Math.pow(10, m.group(4).length()));
				else if(Integer.parseInt(m.group(4)) != 0)
					return new Fraction(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(4)));
					
			}
			System.out.print("Re-enter >> ");
			s.nextLine();
		}
	}
	public static void inputM(Matrix mat)
	{
		s.nextLine();
		System.out.print("Enter multiple numbers >> ");
		String input;
		int i=0, j=mat.getWidth(), k=mat.getHeight();
		Pattern p = Pattern.compile("(-?\\d+)((\\.|/)(\\d+))?");
		while(true)
		{
			input = s.nextLine();
			Matcher m = p.matcher(input);
			while(m.find() && i<j*k)
			{
				if(m.group(2) == null)
					mat.set(i/j, i%j, new Fraction(Integer.parseInt(m.group(1))));
				else if(m.group(3).equals("."))
					mat.set(i/j, i%j, new Fraction(Integer.parseInt(m.group(1)+m.group(4)), (int)Math.pow(10, m.group(4).length())));
				else
					mat.set(i/j, i%j, new Fraction(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(4))));
				i++;
			}
			if(i>=j*k)
				break;
			System.out.print("Continue >> ");
		}
	}
	public static String inputS(String prompt)
	{
		s.nextLine();
		System.out.print(prompt); 
		System.out.print(" >> "); 
		return s.next();
	}
	public static int input(String[] list)
	{
		sline();
		System.out.println("Enter a number to make you choice (enter 0 to quit):");
		for(int i=0; i<list.length; i++)
			System.out.println(String.format("%2d. %s ", i+1, list[i]));
		System.out.print("\n >> ");
		int input=-1;
		try{
			input = s.nextInt();
		}catch(InputMismatchException e){s.nextLine();}
		while(input < 0 || input > list.length)
		{
			System.out.print("Re-enter >> ");
			try{
				input = s.nextInt();
			}catch(InputMismatchException e){s.nextLine();}
		}
		sline();
		if(input == 0)
			System.exit(0);
		return input;
	}
	public static void main(String[] args)
	{
		System.out.println();
		System.out.println("Welcome! This is a mathematic tool written in pure JAVA and aimed at analysing a matrix accurately.\nIt will help you find some basic information about the mtrix you inputed (such as REF and RREF).\nTo begin, you need input a matrix.");
		int input = input(new String[]{"Generate a random square matrix", "Generate a random symmetric matrix", "Enter a square matrix", "Enter a general matrix", "Parsing a square matrix", "Parsing a general matrix"});
		MatrixHandler mh = null;
		Matrix m = null;
		if(input == 1)
		{
			int wh = inputI("Please enter the number of columns of this matrix:");
			m = new Matrix(wh, wh);
			for(int i=0; i<wh*wh; i++)
				m.set(i%wh, i/wh, (int)(Math.random() * 10));
		}
		else if(input == 2)
		{
			int wh = inputI("Please enter the number of columns of this matrix:");
			int temp;
			m = new Matrix(wh, wh);
			for(int i=0; i<wh; i++)
				for(int j=i; j<wh; j++)
					if(i == j)
						m.set(i, i, (int)(Math.random() * 10));
					else
					{
						temp = (int)(Math.random() * 10);
						m.set(i, j, temp);
						m.set(j, i, temp);
					}
		}
		else if(input == 3)
		{
			int wh = inputI("Please enter the number of columns of this matrix:");
			m = new Matrix(wh, wh);
			for(int i=0; i<wh; i++)
				for(int j=0; j<wh; j++)
					m.set(i, j, inputF(String.format("Please enter the element at (%d, %d) of this matrix:", i+1, j+1)));
		}
		else if(input == 4)
		{
			int h = inputI("Please enter the number of rows of this matrix:");
			int w = inputI("Please enter the number of columns of this matrix:");
			m = new Matrix(h, w);
			for(int i=0; i<h; i++)
				for(int j=0; j<w; j++)
					m.set(i, j, inputF(String.format("Please enter the element at (%d, %d) of this matrix:", i+1, j+1)));
		}
		else if(input == 5)
		{
			int wh = inputI("Please enter the number of columns of this matrix:");
			m = new Matrix(wh, wh);
			inputM(m);
		}
		else if(input == 6)
		{
			int h = inputI("Please enter the number of rows of this matrix:");
			int w = inputI("Please enter the number of columns of this matrix:");
			m = new Matrix(h, w);
			inputM(m);
		}
		mh = new MatrixHandler(m);
		while(true)
		{
			System.out.println("\nThe matrix now is:");
			System.out.println(m);
			input = input(new String[]{"Solve everything and print it out (may be long)", "Solve everything and save to a text file", "Find its REF", "Find its RREF", "Find the inverse (if exists)", "Calculate the determinant (if exists)", "Find a basis of its column space", "Find a basis of its row space", "Find a basis of its null space", "Solve the linear system Ax=b", "Transpose it to find more", "Edit this matrix"});
			
			if(input == 1)
			{
				mh.printRecord(System.out);
			}
			else if(input == 2)
			{
				String filename = inputS("Enter the file name:");
				if(!filename.endsWith(".txt"))
					filename = filename.concat(".txt");
				try
				{
					File file = new File(filename);
					if(file.exists())
						System.out.print(String.format("File [%s] updating..", filename));
					else
						System.out.print(String.format("File [%s] creating..", filename));
					PrintStream ps = new PrintStream(file);
					ps.print("This text file is created automaticlly at ");
					ps.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
					ps.println("\nThe original matrix is:");
					ps.println(m.toString());
					mh.printRecord(ps);
					System.out.println("\b\b\b\b\bed.  ");
				}
				catch(Exception e)
				{
					System.out.println("Something happens. :(");
				}
			}
			else if(input == 3)
			{
				System.out.println("The Echelon form (REF) is:");
				System.out.println(mh.getREF());
			}
			else if(input == 4)
			{
				System.out.println("The Reduced Echelon form (RREF) is:");
				System.out.println(mh.getRREF());
			}
			else if(input == 5)
				if(mh.getInverse() == null)
					System.out.println("The inverse does not exist.");
				else
				{
					System.out.println("The inverse of this matrix is:");
					System.out.println(mh.getInverse());
				}
			else if(input == 6)
				if(mh.getDet().valid())
					System.out.println("The determinant is: " + mh.getDet());
				else
					System.out.println("The determinant does not exist. ");
			else if(input == 7)
				if(mh.getRank() == 0)
					System.out.println("The rank is 0, and thus the column space only contains the zero vector.");
				else
				{
					System.out.println("A basis of the column space of this matrix is:");
					System.out.println(mh.getColspace());
				}
			else if(input == 8)
				if(mh.getRank() == 0)
					System.out.println("The rank is 0, and thus the row space only contains the zero vector.");
				else
				{
					System.out.println("A basis of the row space of this matrix is:");
					System.out.println(mh.getRowspace());
				}
			else if(input == 9)
				if(mh.getNullity() == 0)
					System.out.println("This matrix is full-rank, and thus the null space only contains the zero vector.");
				else
				{
					System.out.println("The null space matrix is:");
					System.out.println(mh.getNullspace());
				}
			else if(input == 10)
			{
				Matrix b = new Matrix(m.getHeight(), 1);
				System.out.println("The enter the vector b:");
				inputM(b);
				Matrix mb = new Matrix(m.getHeight(), m.getWidth()+1);
				int i, j;
				int maxlen = m.getMaxlen();
				Fraction f=null;
				for(i=0; i<m.getHeight(); i++)
				{
					for(j=0; j<m.getWidth(); j++)
						mb.set(i, j, m.get(i, j));
					mb.set(i, j, b.get(i, 0));
				}
				System.out.println("\nThat is, ");
				boolean allzero;
				boolean inconsistent=false;
				for(i=0; i<m.getHeight(); i++)
				{
					allzero = true;
					for(j=0; j<m.getWidth(); j++)
					{
						f = m.get(i, j);
						if(!f.iszero())
						{
							if(!allzero)
								System.out.print(" + ");
							else
								System.out.print("   ");
							System.out.print(String.format("%"+maxlen+"s * x_%-2d", f, j+1));
							allzero = false;
						}
						else if(j == m.getWidth()-1 && allzero)
						{
								System.out.print(new String(new char[maxlen+7]).replace((char)0, (char)32));
								System.out.print(" 0 ");
								if(!b.get(i, 0).iszero())
									inconsistent = true;
						}
						else
							System.out.print(new String(new char[maxlen+10]).replace((char)0, (char)32));
					}
					System.out.print(" = ");
					System.out.println(b.get(i, 0));
				}
				MatrixHandler solver = new MatrixHandler(mb);
				for(i=0; i<mb.getHeight(); i++)
				{
					for(j=0; j<mb.getWidth()-1; j++)
						if(!solver.getRREF().get(i, j).iszero())
							break;
					if(j==mb.getWidth()-1 && !solver.getRREF().get(i, j).iszero())
					{
						inconsistent = true;
						break;
					}
				}
				if(mh.getRank()==m.getWidth() && !inconsistent)
				{
					Matrix mbt = new Matrix(mh.getRank(), mh.getRank()+1);
					for(i=0; i<mh.getRank(); i++)
						for(j=0; j<mh.getRank()+1; j++)
							mbt.set(i, j, mb.get(i, j));
					solver = new MatrixHandler(mbt);
					System.out.print("The unique solution of this system is:\n{");
				}
				else
				{
					System.out.print("\nThe system is inconsistent.\nWe cannot find a exactly solution for this system.\nAs an alternative, we can find the Least Square Approximation:\n{");
					Matrix mt = m.getTranspose();
					Matrix mtm = mt.multiply(m);
					b = mt.multiply(b);
					mb = new Matrix(mtm.getWidth(), mtm.getWidth()+1);
					for(i=0; i<mtm.getWidth(); i++)
					{
						for(j=0; j<mtm.getWidth(); j++)
							mb.set(i, j, mtm.get(i, j));
						mb.set(i, j, b.get(i, 0));
					}	
					solver = new MatrixHandler(mb);
				}
				for(i=0; i<m.getWidth(); i++)
				{
					System.out.print(String.format("x_%d = %s", i+1, solver.getRREF().get(i, solver.getMatrix().getWidth()-1)));
					if(i != m.getWidth()-1)
						System.out.print(", ");
				}
				System.out.println("}");
			}
			else if(input == 11)
			{
				m = m.getTranspose();
				mh = new MatrixHandler(m);
			}
			else if(input == 12)
			{
				System.out.println("\nThe matrix now is:");
				System.out.println(m);
				input = input(new String[]{"Edit one entry", "Resize the matrix", "Delete one row", "Delete one column", "Re-enter one row", "Re-enter one column", "Fill with one number", "Keep the upper triangle", "Keep the lower triangle", "Randomly fill", "Shuffle"});
				if(input == 1)
					m.set(inputI("Enter the row of the entry you want to edit:", 1, m.getHeight())-1,
						inputI("Enter the column of the entry you want to edit:", 1, m.getWidth())-1,
						inputF("Enter the new value of this entry:"));
				else if(input == 2)
				{
					Matrix temp = m.copy();
					m = new Matrix(inputI("Enter the number of rows of the matrix"),
							inputI("Enter the number of columns of the matrix"));
					for(int i=0; i<temp.getWidth() && i<m.getWidth(); i++)
						for(int j=0; j<temp.getHeight() && j<m.getHeight(); j++)
							m.set(j, i, temp.get(j, i));
				}
				else if(input == 3)
				{
					if(m.getHeight()==1)
					{
						System.out.println("Error: You cannot delete the only 1 row.");
						continue;
					}
					input = inputI("Enter the row you want to delete:", 1, m.getHeight());
					Matrix temp = new Matrix(m.getHeight()-1, m.getWidth());
					for(int i=0; i<m.getWidth(); i++)
					{
						for(int j=0; j<input-1; j++)
							temp.set(j, i, m.get(j, i));
						for(int j=input-1; j<m.getHeight()-1; j++)
							temp.set(j, i, m.get(j+1, i));
					}
					m = temp;
				}
				else if(input == 4)
				{
					if(m.getWidth()==1)
					{
						System.out.println("Error: You cannot delete the only 1 column.");
						continue;
					}
					input = inputI("Enter the column you want to delete:", 1, m.getWidth());
					Matrix temp = new Matrix(m.getHeight(), m.getWidth()-1);
					for(int i=0; i<m.getHeight(); i++)
					{
						for(int j=0; j<input-1; j++)
							temp.set(i, j, m.get(i, j));
						for(int j=input-1; j<m.getWidth()-1; j++)
							temp.set(i, j, m.get(i, j+1));
					}
					m = temp;
				}
				else if(input == 5)	
				{
					input = inputI("Enter the row you want to re-enter:", 1, m.getHeight())-1;
					Matrix temp = new Matrix(1, m.getWidth());
					inputM(temp);
					for(int i=0; i<m.getWidth(); i++)
						m.set(input, i, temp.get(0, i));
				}
				else if(input == 6)	
				{
					input = inputI("Enter the column you want to re-enter:", 1, m.getWidth())-1;
					Matrix temp = new Matrix(m.getHeight(), 1);
					inputM(temp);
					for(int i=0; i<m.getHeight(); i++)
						m.set(i, input, temp.get(i, 0));
				}
				else if(input == 7)
				{
					Fraction f = inputF("Enter the number:");
					for(int i=0; i<m.getHeight(); i++)
						for(int j=0; j<m.getWidth(); j++)
							m.set(i, j, f);
				}
				else if(input == 8)
				{
					for(int i=1; i<m.getHeight(); i++)
						for(int j=0; j<i && j<m.getWidth(); j++)
							m.set(i, j, 0);
				}
				else if(input == 9)
				{
					for(int i=0; i<m.getWidth(); i++)
						for(int j=0; j<i && j<m.getHeight(); j++)
							m.set(j, i, 0);
				}
				else if(input == 10)
				{
					for(int i=0; i<m.getHeight(); i++)
						for(int j=0; j<m.getWidth(); j++)
							m.set(i, j, (int)(Math.random()*10));
				}
				else if(input == 11)
				{
					List<Fraction> numbers = new ArrayList<Fraction>();
					for(int i=0; i<m.getWidth(); i++)
						for(int j=0; j<m.getHeight(); j++)
							numbers.add(m.get(j, i));
					Collections.shuffle(numbers);
					for(int i=0; i<m.getWidth(); i++)
						for(int j=0; j<m.getHeight(); j++)
							m.set(j, i, numbers.remove(0));
				}
				mh = new MatrixHandler(m);
			}
			sline();
		}
	}
}
