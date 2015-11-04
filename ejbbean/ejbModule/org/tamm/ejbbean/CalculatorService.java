package org.tamm.ejbbean;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.tamm.entities.Operation;

@Stateless
public class CalculatorService implements CalculatorServiceLocal,
		CalculatorServiceRemote {

	@PersistenceContext(unitName = "Calculator")
	private EntityManager em;

	public CalculatorService() {
	}

	@Override
	public double add(Operation op) {
		op.setResult(op.getVar1() + op.getVar2());
		persistEntity(op);
		mergeEntity(op);
		return op.getResult();
	}

	@Override
	public double divide(Operation op) {
		if (op.getVar2() == 0) {
			throw new EJBException("Division by zero!");
		} else {
			op.setResult(op.getVar1() / op.getVar2());
		}
		persistEntity(op);
		mergeEntity(op);
		return (op.getResult());
	}

	@Override
	public double multiply(Operation op) {
		op.setResult(op.getVar1() * op.getVar2());
		persistEntity(op);
		mergeEntity(op);
		return (op.getResult());
	}

	@Override
	public double subtract(Operation op) {
		op.setResult(op.getVar1() - op.getVar2());
		persistEntity(op);
		mergeEntity(op);
		return (op.getResult());
	}

	private <T> T persistEntity(T entity) {
		em.persist(entity);
		return entity;
	}

	private <T> T mergeEntity(T entity) {
		return em.merge(entity);
	}

}