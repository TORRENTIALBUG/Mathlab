package Mathlab;

import java.math.*;

public class Fraction
{
	private BigInteger numerator;
	private BigInteger denominator;

	public Fraction(){}
	public Fraction(int i)
	{
		numerator = BigInteger.valueOf(i);
		denominator = BigInteger.ONE;
	}
	public Fraction(BigInteger i)
	{
		numerator = i;
		denominator = BigInteger.ONE;
	}
	public Fraction(int i, int j)
	{
		numerator = BigInteger.valueOf(i);
		denominator = BigInteger.valueOf(j);
		simplify();
	}
	public Fraction(BigInteger i, BigInteger j)
	{
		numerator = i;
		denominator = j;
		simplify();
	}
	public BigInteger getNumerator()
	{
		return numerator;
	}
	public BigInteger getDenominator()
	{
		return denominator;
	}
	public String toString()
	{
		if(denominator.compareTo(BigInteger.ZERO)==0)
			return "NaN";
		if(numerator.compareTo(BigInteger.ZERO)==0)
			return "0";
		if(denominator.compareTo(BigInteger.ONE)==0)
			return numerator.toString();
		return numerator + "/" + denominator;
	}
	public void simplify()
	{
		if(numerator.compareTo(BigInteger.ZERO)==0 || denominator.compareTo(BigInteger.ZERO)==0)
			return;
		while(true)
		{
			BigInteger gcd = Fraction.gcd(numerator, denominator);
			if(gcd.compareTo(BigInteger.ONE)==0)
				break;
			numerator = numerator.divide(gcd);
			denominator = denominator.divide(gcd);
		}
		if(denominator.compareTo(BigInteger.ZERO) < 0)
		{
			denominator = BigInteger.ZERO.subtract(denominator);
			numerator = BigInteger.ZERO.subtract(numerator);
		}
	}
	public boolean iszero()
	{
		if(denominator.compareTo(BigInteger.ZERO)==0)
			return false;
		if(numerator.compareTo(BigInteger.ZERO)==0)
			return true;
		return false;
	}
	public boolean isone()
	{
		if(denominator.compareTo(BigInteger.ZERO)==0)
			return false;
		if(numerator.compareTo(BigInteger.ONE)==0 && denominator.compareTo(BigInteger.ONE)==0)
			return true;
		return false;
	}
	public boolean valid()
	{
		return (denominator != null && numerator != null && denominator.compareTo(BigInteger.ZERO)!=0);
	}
	public Fraction plus(Fraction op2)
	{
		BigInteger op1d = denominator;
		BigInteger op2d = op2.getDenominator();
		if(op1d.compareTo(BigInteger.ZERO)==0 || op2d.compareTo(BigInteger.ZERO)==0)
			return new Fraction();
		BigInteger lcm = Fraction.lcm(denominator, op2d);
		return new Fraction(numerator.multiply(lcm).divide(op1d).add(op2.getNumerator().multiply(lcm).divide(op2d)), lcm);
	}
	public Fraction minus(Fraction op2)
	{
		BigInteger op1d = denominator;
		BigInteger op2d = op2.getDenominator();
		if(op1d.compareTo(BigInteger.ZERO)==0 || op2d.compareTo(BigInteger.ZERO)==0)
			return new Fraction();
		BigInteger lcm = Fraction.lcm(denominator, op2d);
		return new Fraction(numerator.multiply(lcm).divide(op1d).subtract(op2.getNumerator().multiply(lcm).divide(op2d)), lcm);
	}
	public Fraction multiply(Fraction fr)
	{
		return new Fraction(numerator.multiply(fr.getNumerator()), denominator.multiply(fr.getDenominator()));
	}
	public Fraction div(Fraction fr)
	{
		if(fr.getDenominator().compareTo(BigInteger.ZERO)==0)
			return new Fraction();
		return new Fraction(numerator.multiply(fr.getDenominator()), denominator.multiply(fr.getNumerator()));
	}
	
	public Fraction plus(int a)
	{
		return new Fraction(numerator.add(BigInteger.valueOf(a).multiply(denominator)), denominator);
	}
	public Fraction minus(int a)
	{
		return new Fraction(numerator.subtract(BigInteger.valueOf(a).multiply(denominator)), denominator);
	}
	public Fraction multiply(int m)
	{
		return new Fraction(numerator.multiply(BigInteger.valueOf(m)), denominator);
	}
	public Fraction div(int d)
	{
		return new Fraction(numerator, denominator.multiply(BigInteger.valueOf(d)));
	}
	public Fraction inverse()
	{
		if(iszero())
			return this;
		return new Fraction(BigInteger.ZERO.subtract(numerator), denominator);
	}

	public static BigInteger gcd(BigInteger a, BigInteger b)
	{
		if(a.compareTo(BigInteger.ZERO) == 0)
			return b;
		if(a.compareTo(BigInteger.ZERO) < 0)
			a = BigInteger.ZERO.subtract(a);
		return gcd(b.mod(a), a);
	}
	public static BigInteger lcm(BigInteger a, BigInteger b)
	{
		return (a.multiply(b)).divide(gcd(a, b));
	}
	public static void main(String[] args)
	{
		Fraction f = new Fraction(1234, 1);
		Fraction f2 = new Fraction(BigInteger.ONE, BigInteger.TEN.multiply(BigInteger.valueOf(122345678)));
		Fraction f3 = f2.multiply(f2).multiply(f2).multiply(f2).multiply(f2);
		System.out.println(f);
		System.out.println(f2);
		System.out.println(f3);
		System.out.println(f.multiply(f3));
	}
}
