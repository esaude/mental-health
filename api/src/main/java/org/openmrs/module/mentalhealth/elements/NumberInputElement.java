package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.module.htmlformentry.FormEntrySession;
import org.w3c.dom.Node;

public class NumberInputElement extends InputElement {

	
	
	public NumberInputElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		// TODO check m_parameters.get("step") (else default to html5 spec default step of 1) for step and determine the precision to provide for the input value attr in view and edit modes
	}

	@Override
	public String derivedClassSpecializeHTMLEditProcessing()
	{
		String result = null;
		
		if(m_prevObs!=null) {
			//only support integer values for now
			result = String.valueOf(m_prevObs.getValueNumeric().intValue());
		}
		
		return result;
	}
	
	
}
