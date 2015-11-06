package org.tamm.ejbclient;

import java.util.Arrays;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tamm.ejbbean.CalculatorServiceLocal;
import org.tamm.entities.Operation;
import org.tamm.entities.OperationType;

public class CalculatorPage extends WebPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private static OperationType selected = OperationType.ADD;

	public CalculatorPage(final PageParameters parameters) {
		super(parameters);
		add(new CalculatorForm("calculatorForm"));
	}

	private static class CalculatorForm extends Form<Object> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8575117117127625602L;
		
		private FeedbackPanel feedbackPanel;
		private Label resultLabel;

		public CalculatorForm(String id) {
			super(id);
			Operation operation = new Operation();
			setModel(new Model(operation));
			
			TextField<Double> varField1 = new RequiredTextField<Double>("varField1",
					new PropertyModel<Double>(operation, "var1"));
			
			TextField<Double> varField2 = new RequiredTextField<Double>("varField2",
					new PropertyModel<Double>(operation, "var2"));
			
			resultLabel = new Label("resultLabel",
					new PropertyModel<Double>(operation, "result"));
			
			DropDownChoice<OperationType> types = new DropDownChoice<OperationType>(
					"types",
					new PropertyModel<OperationType>(operation, "type"),
					Arrays.asList(OperationType.values()));

			feedbackPanel = new FeedbackPanel("feedback");
			
			add(varField1);
			add(varField2);
			add(resultLabel);
			add(types);
			add(feedbackPanel);
		}

		public final void onSubmit() {
			try
    		{
				Operation input = (Operation)getModelObject();
				Operation output = new Operation(input.getVar1(), input.getVar2(), input.getType());
				resultLabel.setDefaultModelObject(calculate(output));
    		}
			catch(EJBException e)
    		{
    			feedbackPanel.error(e.getMessage());
    		}
		}
		
		private Double calculate(Operation op) throws EJBException
    	{
			if(op.getType() == null)
			{
				throw new EJBException("Select operation's type!");
			}
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
