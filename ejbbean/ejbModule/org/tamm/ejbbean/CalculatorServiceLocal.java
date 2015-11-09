package org.tamm.ejbbean;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Local;

import org.tamm.entities.Operation;

@Local
public interface CalculatorServiceLocal {

    public double add(Operation op);
    public double subtract(Operation op);
    public double multiply(Operation op);
    public double divide(Operation op) throws EJBException;
	public List<Operation> findAll();
    
}
