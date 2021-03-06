package org.tamm.ejbclient;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tamm.ejbbean.CalculatorServiceLocal;
import org.tamm.entities.Operation;
import org.tamm.entities.OperationType;

public class CalculatorPage extends WebPage {
	
	private static final long serialVersionUID = 1L;

	public CalculatorPage(final PageParameters parameters) {
		super(parameters);
		add(new CalculatorForm("calculatorForm"));
	}

	private static class CalculatorForm extends Form<Object> {
		private static final long serialVersionUID = -8575117117127625602L;
		private Double result;
		
		//create a list of operations from the db
		private List<Operation> list = lookupEJB().findAll();

		public CalculatorForm(String id) {
			//by using model nesting we do not have to make operation serializable
			super(id, new CompoundPropertyModel<Object>(new LoadableDetachableModel<Operation>() {

				private static final long serialVersionUID = -4927172420790011314L;

				@Override 
			    protected Operation load() { 
			        return new Operation(); 
			    }

			}));
			
			lookupEJB().deleteAll();//to occasionally clear the database
			list = lookupEJB().findAll();//retrieve all records
			
			add(new RequiredTextField<Double>("var1"));
			add(new RequiredTextField<Double>("var2"));
			
			//model for updating the result label
			Model<Double> resultModel = new Model<Double>() {
				private static final long serialVersionUID = -2921734639027013889L;
				
				@Override
	            public Double getObject() {
	                return result;
	            }
	        };
	        final Label resultLabel = new Label("resultLabel", resultModel);
	        resultLabel.setOutputMarkupId(true);
	        add(resultLabel);
			
			add(new DropDownChoice<OperationType>("type",
					Arrays.asList(OperationType.values())));
			

			final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
			feedbackPanel.setOutputMarkupId(true);
			add(feedbackPanel);
			
			//table of all operations calculated so far
			PageableListView<Operation> listView = new PageableListView<Operation>("listView", list, 5)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Operation> item)
				{
					Operation listViewOp = (Operation)item.getModelObject();
					item.add(new Label("var1", listViewOp.getVar1()));
					item.add(new Label("var2", listViewOp.getVar2()));
					item.add(new Label("type", listViewOp.getType()));
			        item.add(new Label("result", listViewOp.getResult()));
				}
			};
			AjaxPagingNavigator navigator = new AjaxPagingNavigator("navigator", listView);
			navigator.setOutputMarkupId(true);
			add(navigator);
			
			WebMarkupContainer listContainer = new WebMarkupContainer("theContainer");
	        listContainer.setOutputMarkupId(true);
	        listContainer.add(listView);
	        
	        add(listContainer);
	        
	        AjaxButton ab=new AjaxButton("submit") {
				private static final long serialVersionUID = 4430289320139856428L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
	                if (target!=null)
	                {
	                	try {
	                		Operation input = (Operation) form.getModelObject();
	                		System.out.println(input);
	                		Operation output = new Operation(input.getVar1(), input.getVar2(), input.getType());
	        				result = calculate(output);
	        				
	        				list.add(output);//add the the last operation to the list of all operations
	        				target.add(listContainer);
	        				target.add(resultLabel);//update the result label
	        				target.add(feedbackPanel);//clear the feedback panel of previous errors 
	        				target.add(navigator);//update the navigator
	        			} catch (EJBException e) {
	        				feedbackPanel.error(e.getMessage());
	        				onError(target, form);
	        			}
	                }
	            }
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					if (target != null) {
						target.add(feedbackPanel);
					}
				} 
	        };
	        add(ab);
		}
		
		/**
		 * Calculate the result of the given operation
		 * @param op
		 * @return
		 * @throws EJBException
		 */
		private Double calculate(Operation op) throws EJBException {
			if (op.getType() == null) {
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

		/**
		 * Lookup and return the EJB
		 * @return
		 */
		private CalculatorServiceLocal lookupEJB() {
			InitialContext ic;
			CalculatorServiceLocal calculator = null;
			try {
				ic = new InitialContext();
				calculator = (CalculatorServiceLocal) ic
						.lookup("java:global/ejbear/ejbbean/CalculatorService!org.tamm.ejbbean.CalculatorServiceLocal");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				throw new EJBException(e.getMessage());
			}

			return calculator;
		}
	}
}
