package org.tamm.ejbclient;

import java.util.Arrays;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tamm.ejbbean.CalculatorServiceLocal;
import org.tamm.entities.Operation;
import org.tamm.entities.OperationType;

public class CalculatorPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static OperationType selected = OperationType.ADD;

	public CalculatorPage(final PageParameters parameters) {
		super(parameters);
    	add(new CalculatorForm("calculatorForm"));
    }
    
    private static class CalculatorForm extends Form<Object>{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private TextField<String> varField1;
    	private TextField<String> varField2;
    	private Label valueLabel;
    	
    	private DropDownChoice<OperationType> operations;
    	
    	public CalculatorForm(String id) {
    		super(id);
    			
    		varField1 = new TextField<String>("varField1", Model.of(""));
    		varField2 = new TextField<String>("varField2", Model.of(""));			
    		valueLabel = new Label("valueLabel", Model.of(""));
    		operations = new DropDownChoice<OperationType>("operations", new Model<OperationType>(selected), Arrays.asList(OperationType.values()));
    		
    		add(varField1);
    		add(varField2);
    		add(valueLabel);
    		add(operations);
    	}

    	public final void onSubmit() {
    		double var1, var2;
    		OperationType op_type;
    		try
    		{
    			var1 =  parseValue((String)varField1.getDefaultModelObject());
    			var2 =  parseValue((String)varField2.getDefaultModelObject());
    			op_type = (OperationType) operations.getDefaultModelObject();
    			
    			Operation op = new Operation(var1, var2, op_type);
    			
    			valueLabel.setDefaultModelObject("The result is: "+calculate(op));
    		}
    		catch(NumberFormatException e)
    		{
    			e.printStackTrace();
    			valueLabel.setDefaultModelObject("Provide integers!");
    		}
    		catch(EJBException e)
    		{
    			valueLabel.setDefaultModelObject(e.getMessage());
    		}
    	}
    	
    	private Number calculate(Operation op) throws EJBException
    	{
    		CalculatorServiceLocal calculator = lookupEJB();
    		switch (op.getType()) {
			case ADD:
				return calculator.add(op);
			case SUBTRACT:
				return calculator.subtract(op);
			case MULTIPLY:
				return calculator.multiply(op);
			case DIVIDE:
				return calculator.divide(op);
			default:
				return null;
			}
    	}
    	
    	private double parseValue(String value) throws NumberFormatException
    	{
    		return Double.parseDouble(value);
    	}
    	
    	private CalculatorServiceLocal lookupEJB()
    	{
    		InitialContext ic;
    		CalculatorServiceLocal calculator = null;
    		try {
    			ic = new InitialContext();
    			calculator = (CalculatorServiceLocal) ic.lookup("java:global/ejbear/ejbbean/CalculatorService!org.tamm.ejbbean.CalculatorServiceLocal");
    		} catch (NamingException e) {
    			// TODO Auto-generated catch block
    			throw new EJBException(e.getMessage());
    		}
    		
    		return calculator;
    	}
    }

}
