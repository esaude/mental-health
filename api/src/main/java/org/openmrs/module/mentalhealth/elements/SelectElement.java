package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IParentElement;
import org.w3c.dom.Node;

public class SelectElement extends PassthroughElement implements HtmlGeneratorElement, FormSubmissionControllerAction, IParentElement {

	private Map<String, Concept> m_options = new HashMap<String, Concept>();
	
	
	public void addHTMLValueConceptMapping(IChildElement opt) {
		String childValue = opt.getAttrs().get("value");
		
		m_options.put(childValue, opt.getConcept());
	}
	
	public SelectElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
	}

	
	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		//if no data-concept-id was set 
		//(since an error should be thrown when the form is created if a conceptId
		//was provided but could not be found in openmrs)
		//do nothing for this form element
		
		if( m_openMRSConcept == null ) {
			return;
		}
			
		FormEntryContext context = session.getContext();
		
		String tagName = m_parameters.get("name");
		
		String value = submission.getParameter(tagName);
		
		if(value == null || value.isEmpty()) {
			//throw new IllegalArgumentException("Value for select " + tagName + " cannot be blank/empty");
			return;
		}
		
		Concept responseConcept = m_options.get(value);
		
		if(responseConcept == null) {
			String selectId = "#"+m_parameters.get("id");
			
			if(selectId.equals("#")) {
				selectId = "with no id";
			}
			
			throw new IllegalArgumentException("Concept for select " + selectId + " form name " + tagName + " and value " + value + " not found!");
		
		}
		
		switch(context.getMode()) {
			case EDIT:
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, responseConcept, null, null, null);
				break;
		
		}

	}

	@Override
	public boolean getValueStoredInOpenMRS(IChildElement child) {
		return false;
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "select";
	}

}
