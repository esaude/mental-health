package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IParentElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RadioElement extends InputElement implements IHandleHTMLEdit, IChildElement {
	
	protected Log log = LogFactory.getLog(getClass());
	
	
	IParentElement m_parentElement;
	
	public RadioElement(FormEntryContext context, Map<String, String> attrs, Node node, IParentElement parentElement) {
		super(context, attrs, node);
		
		log.info("creating radio element");
		
		m_parentElement = parentElement;
		
		parentElement.addHTMLValueConceptMapping(this);
		
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
	public boolean getDefaultStateFromNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void takeActionForEditMode(FormEntryContext context) {

		if(m_openMRSConcept == null || m_parentElement == null) {
			return;
		}
		
		if(m_parentElement.getValueStoredInOpenMRS(this)) {
			
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
