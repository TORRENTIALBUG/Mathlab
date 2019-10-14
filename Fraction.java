package Mathlab;

public class Fraction
{
	private int numerator;
	private int denominator;

	public Fraction(){}
	public Fraction(int i)
	{
		numerator = i;
		denominator = 1;
	}
	public Fraction(int i, int j)
	{
		numerator = i;
		denominator = j;
		simplify();
	}
	public int getNumerator()
	{
		return numerator;
	}
	public int getDenominator()
	{
		return denominator;
	}
	public String toString()
	{
		if(denominator == 0)
			return "NaN";
		if(numerator == 0)
			return "0";
		if(denominator == 1)
			return Integer.toString(numerator);
		return numerator + "/" + denominator;
	}
	public void simplify()
	{
		if(numerator == 0 || denominator == 0)
			return;
		while(true)
		{
			int gcd = Fraction.gcd(numerator, denominator);
			if(gcd == 1)
				break;
			numerator /= gcd;
			denominator /= gcd;
		}
		if(denominator < 0)
		{
			denominator *= -1;
			numerator *= -1;
		}
	}
	public boolean iszero()
	{
		if(denominator == 0)
			return false;
		if(numerator == 0)
			return true;
		return false;
	}
	public Fraction plus(Fraction op2)
	{
		int op1d = denominator;
		int op2d = op2.getDenominator();
		if(op1d == 0 || op2d == 0)
			return new Fraction();
		int lcm = Fraction.lcm(denominator, op2d);
		return new Fraction(numerator*lcm/op1d+op2.getNumerator()*lcm/op2d, lcm);
	}
	public Fraction minus(Fraction op2)
	{
		int op1d = denominator;
		int op2d = op2.getDenominator();
		if(op1d == 0 || op2d == 0)
			return new Fraction();
		int lcm = Fraction.lcm(denominator, op2d);
		return new Fraction(numerator*lcm/op1d-op2.getNumerator()*lcm/op2d, lcm);
	}
	public Fraction multiply(Fraction fr)
	{
		return new Fraction(numerator*fr.getNumerator(), denominator*fr.getDenominator());
	}
	public Fraction div(Fraction fr)
	{
		if(fr.getDenominator() == 0)
			return new Fraction();
		return new Fraction(numerator*fr.getDenominator(), denominator*fr.getNumerator());
	}
	
	public Fraction plus(int a)
	{
		return new Fraction(numerator+a*denominator, denominator);
	}
	public Fraction minus(int a)
	{
		return new Fraction(numerator-a*denominator, denominator);
	}
	public Fraction multiply(int m)
	{
		return new Fraction(numerator*m, denominator);
	}
	public Fraction div(int d)
	{
		return new Fraction(numerator, denominator*d);
	}
	public Fraction inverse()
	{
		if(iszero())
			return this;
		return new Fraction(-numerator, denominator);
	}

	public static int gcd(int a, int b)
	{
		if(a == 0)
			return b;
		return gcd(b % a, a);
	}
	public static int lcm(int a, int b)
	{
		return (a * b) / gcd(a, b);
	}
}
