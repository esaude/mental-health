package org.openmrs.module.mentalhealth.elements;

import java.util.Locale;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.w3c.dom.Node;

public class NumberInputElement extends InputElement {

	
	
	public NumberInputElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		// TODO check m_parameters.get("step") (else default to html5 spec default step of 1) for step and determine the precision to provide for the input value attr in view and edit modes
	}

	private int getPrecision(String formattedString, int currentPrecision, String decimalSeparator) {
		
		if(formattedString != null && !formattedString.isEmpty()) {
			int index = formattedString.indexOf(decimalSeparator);
			
			if(index >= 0) {
				
				int precision = formattedString.length() - index - 1;
				
				if(precision > currentPrecision) {
					return precision;
				}
				
			}
		}
		
		return currentPrecision;
	}
	
	@Override
	public String derivedClassSpecializeHTMLEditProcessing()
	{
		String result = null;
		
		if(m_prevObs!=null) {
			String []formattedStrings = new String[] {
										m_parameters.get("step"), 
										m_parameters.get("min"),
										m_parameters.get("max")
									};
			
			Integer precision = 0;
			
			String decimalSeparator = ".";
			//?Context.getLocale() == new Locale("en", "US") ? "." : ",";?
			
			for(String formattedString: formattedStrings) {
				precision = getPrecision(formattedString, precision, decimalSeparator);
			}
			
			String precisionSpecifier = "%."+String.valueOf(precision)+"f";
			
			try {
				result = String.format(precisionSpecifier, m_prevObs.getValueNumeric());
			}catch(Exception e) {
				e.printStackTrace();
				result = null;
			}
		}
		
		return result;
	}
	
	
}
