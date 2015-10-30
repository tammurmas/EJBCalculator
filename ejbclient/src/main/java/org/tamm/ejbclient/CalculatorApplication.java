package org.tamm.ejbclient;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class CalculatorApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		// TODO Auto-generated method stub
		return CalculatorPage.class;
	}

}
