package org.tamm.ejbclient;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tamm.ejbbean.CalculatorServiceLocal;

public class CalculatorPage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
    	
    	private List<String> ops = Arrays.asList(new String[] { "add", "subtract", "multiply", "divide"});
    	private String selected = "add";
    	private DropDownChoice<String> operations;
    	
    	public CalculatorForm(String id) {
    		super(id);
    			
    		varField1 = new TextField<String>("varField1", Model.of(""));
    		varField2 = new TextField<String>("varField2", Model.of(""));			
    		valueLabel = new Label("valueLabel", Model.of(""));
    		operations = new DropDownChoice<String>("operations", new PropertyModel<String>(this, "selected"), ops);
    			
    		add(varField1);
    		add(varField2);
    		add(valueLabel);
    		add(operations);
    	}

    	public final void onSubmit() {
    		long var1, var2;
    		String op;
    		try
    		{
    			var1 =  parseValue((String)varField1.getDefaultModelObject());
    			var2 =  parseValue((String)varField2.getDefaultModelObject());
    			op = operations.getDefaultModelObjectAsString();
    			
    			valueLabel.setDefaultModelObject("The result is: "+calculate(var1, var2, op));
    		}
    		catch(NumberFormatException e)
    		{
    			e.printStackTrace();
    			valueLabel.setDefaultModelObject("Provide real numbers!");
    		}
    		catch(EJBException e)
    		{
    			valueLabel.setDefaultModelObject(e.getMessage());
    		}
    	}
    	
    	private Number calculate(long var1, long var2, String op) throws EJBException
    	{
    		CalculatorServiceLocal calculator = lookupEJB();
    		switch (op) {
			case "add":
				return calculator.add(var1, var2);
			case "subtract":
				return calculator.subtract(var1, var2);
			case "multiply":
				return calculator.multiply(var1, var2);
			case "divide":
				return calculator.divide(var1, var2);
			default:
				return null;
			}
    	}
    	
    	private long parseValue(String value) throws NumberFormatException
    	{
    		return Long.parseLong(value);
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
