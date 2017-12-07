package com.mengka.microservices.karaf.command;

import com.mengka.microservices.karaf.service.MengkaService;
import java.util.List;

public class CreditServiceContainer {
	
	private List<MengkaService> creditCalculatorServices;

	
	public void setCreditCalculatorServices(List<MengkaService> creditCalculatorServices) {
		this.creditCalculatorServices = creditCalculatorServices;
	}
	
	public List<MengkaService> getCreditCalculatorServices() {
		return creditCalculatorServices;
	}
	
}
