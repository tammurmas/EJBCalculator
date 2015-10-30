package org.tamm.ejbbean;

import javax.ejb.EJBException;
import javax.ejb.Remote;

@Remote
public interface CalculatorServiceRemote {

    public long add(long i, long j);
    public long subtract(long i, long j);
    public long multiply(long i, long j);
    public double divide(long i, long j) throws EJBException;
    
}
