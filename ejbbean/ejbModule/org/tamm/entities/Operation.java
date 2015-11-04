package org.tamm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
@NamedQuery(name = "Operation.findAll",
query = "select o from Operation o")})
public class Operation {
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE,
	generator = "Customer_ID_Generator")
	private int id;
	private double var1;
	private double var2;
	private OperationType type;
	private double result;
	
	public Operation(){}
	
	public Operation(double v1, double v2, OperationType op){
		var1 = v1;
		var2 = v2;
		type = op;
	}

	public double getVar1() {
		return var1;
	}

	public void setVar1(double var1) {
		this.var1 = var1;
	}

	public double getVar2() {
		return var2;
	}

	public void setVar2(double var2) {
		this.var2 = var2;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "Operation [id=" + id + ", var1=" + var1 + ", var2=" + var2
				+ ", type=" + type + ", result=" + result + "]";
	}
}
