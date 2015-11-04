package org.tamm.ejbbean;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import org.tamm.entities.Operation;

@Remote
public interface CalculatorServiceRemote {

    public double add(Operation op);
    public double subtract(Operation op);
    public double multiply(Operation op);
    public double divide(Operation op) throws EJBException;
    
}
