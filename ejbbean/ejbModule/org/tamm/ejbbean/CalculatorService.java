package org.tamm.ejbbean;

import javax.ejb.EJBException;
import javax.ejb.Stateless;

@Stateless
public class CalculatorService implements CalculatorServiceLocal,
		CalculatorServiceRemote {

	public CalculatorService() {
	}

	@Override
	public long add(long i, long j) {
		return (i + j);
	}

	@Override
	public double divide(long i, long j) {
		if(j == 0)
		{
			throw new EJBException("Division by zero!");
		}
		return ((double) i / j);
	}

	@Override
	public long multiply(long i, long j) {
		return (i * j);
	}

	@Override
	public long subtract(long i, long j) {
		return (i - j);
	}

}