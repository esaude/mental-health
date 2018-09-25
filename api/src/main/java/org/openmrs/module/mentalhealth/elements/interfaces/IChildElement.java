package org.openmrs.module.mentalhealth.elements.interfaces;

import java.util.Map;

import org.openmrs.Concept;

public interface IChildElement {
	public Concept 	getConcept();
	public Map<String,String> getAttrs();

	//selected or checked
	public Object getDefaultStateFromNode();
}
