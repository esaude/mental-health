package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.w3c.dom.Node;

public class LabelElement extends TranslatingElement implements IPassthrough {

	public LabelElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		
	}

	@Override
	public boolean handlesSubmission() {
		return false;
	}

	@Override
	public boolean requiresClosingTag() {
		return true;
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "label";
	}
	
	@Override
	public boolean requiresName() {
		return false;
	}
	
}
