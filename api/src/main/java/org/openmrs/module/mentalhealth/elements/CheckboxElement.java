package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CheckboxElement extends InputElement implements IChildElement, IHandleHTMLEdit {

	ParentElement m_parentElement;
	
	public CheckboxElement(FormEntrySession session, Map<String, String> parameters, Node originalNode, ParentElement parent) {
		super(session, parameters, originalNode);
		// TODO Auto-generated constructor stub
		m_parentElement = parent;
		
		if(parent != null) {
			parent.addHTMLValueConceptMapping(this);
		}
	}

	@Override
	public boolean handlesSubmission() {
		
		if(m_parentElement == null) {
			return true;
		}
		
		return !m_parentElement.hasConceptAssociated();
	}
	
	@Override
	public Concept getConcept() {
		// TODO Auto-generated method stub
		return m_openMRSConcept;
	}

	@Override
	public Map<String, String> getAttrs() {
		// TODO Auto-generated method stub
		return m_parameters;
	}

	@Override
	public Object getDefaultStateFromNode() {
		// TODO Auto-generated method stub
		return (m_parameters.get("checked")!=null);
	}
	
	@Override
	protected boolean noHtmlAction() {
		
		return m_openMRSConcept == null && m_parentElement == null;
	}

	@Override
	public void takeActionForEditMode(FormEntryContext context) {

		getPreviousObsForConcept(context);
		
		if( m_prevObs != null ) {
			
			((Element)m_originalNode).setAttribute("checked", "true");
			
		} else if( m_parentElement!=null ) {

			if((Boolean)m_parentElement.getValueStoredInOpenMRS(this)) {
				
				((Element)m_originalNode).setAttribute("checked", "true");
				
			} else {
	
				((Element)m_originalNode).removeAttribute("checked");
				
			}
		}
	}


}
