package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RadioElement extends InputElement implements IHandleHTMLEdit, IChildElement {

	ParentElement m_parentElement;
	
	public RadioElement(FormEntrySession session, Map<String, String> attrs, Node node, ParentElement parentElement) {
		super(session, attrs, node);
		
		m_parentElement = parentElement;
		
		if(m_parentElement != null) {
			parentElement.addHTMLValueConceptMapping(this);
		}
		
	}
	

	//radios require both values and names
	@Override
	protected boolean requiresValue() {
		return true;
	}
	

	@Override
	public Concept getConcept() {
		// TODO Auto-generated method stub
		return m_openMRSConcept;
	}

	@Override
	public Map<String, String> getAttrs() {
		return m_parameters;
	}

	@Override
	public Object getDefaultStateFromNode() {
		Boolean checked = m_parameters.get("checked")!=null;
		return checked;
	}

	@Override
	public void takeActionForEditMode(FormEntryContext context) {

		if(m_openMRSConcept == null || m_parentElement == null) {
			return;
		}
		
		if((Boolean)m_parentElement.getValueStoredInOpenMRS(this)) {
			
			((Element)m_originalNode).setAttribute("checked", "true");
			
		} else {

			((Element)m_originalNode).removeAttribute("checked");
			
		}
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "input[type='radio']";
	}

}
