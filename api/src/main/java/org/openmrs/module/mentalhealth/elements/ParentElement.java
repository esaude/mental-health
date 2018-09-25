package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.w3c.dom.Node;

public abstract class ParentElement extends PassthroughElement{
	public ParentElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		// TODO Auto-generated constructor stub
	}
	public abstract void addHTMLValueConceptMapping(IChildElement child);
	//parents are created before children, Obs already in openmrs
	//can be loaded based on the xml node parsed by the parent
	public abstract Object getValueStoredInOpenMRS(IChildElement child);
	
}
