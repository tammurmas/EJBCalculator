package org.tamm.entities;

import java.io.Serializable;

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
public class Operation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1445407149813706793L;
	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE,
	generator = "Operation_ID_Generator")
	private int id;
	private Double var1;
	private Double var2;
	private OperationType type;
	private Double result;
	
	public Operation(){}
	
	public Operation(Double v1, Double v2, OperationType op){
		var1 = v1;
		var2 = v2;
		type = op;
	}

	public Double getVar1() {
		return var1;
	}

	public void setVar1(Double var1) {
		this.var1 = var1;
	}

	public Double getVar2() {
		return var2;
	}

	public void setVar2(Double var2) {
		this.var2 = var2;
	}

	public OperationType getType() {
		return type;
	}

	public void setType(OperationType type) {
		this.type = type;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "Operation [id=" + id + ", var1=" + var1 + ", var2=" + var2
				+ ", type=" + type + ", result=" + result + "]";
	}
}
