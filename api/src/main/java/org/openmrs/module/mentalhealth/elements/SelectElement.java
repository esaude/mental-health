package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IParentElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SelectElement extends PassthroughElement implements IHandleHTMLEdit, FormSubmissionControllerAction, IParentElement {

	private Map<String, Concept> m_options = new HashMap<String, Concept>();
	
	private Concept m_selectedChildConcept = null;
	
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
	public void takeActionForEditMode(FormEntryContext context) {
		
		if(m_openMRSConcept == null) {
			return;
		}
		
		//Encounter viewEncounter = context.getExistingEncounter();
		Map<Concept, List<Obs>> existingObs = context.getExistingObs();
		//viewEncounter.
		if(existingObs == null) {
			return;
		}
		
		List<Obs> observations = existingObs.get(m_openMRSConcept);
		
		if(observations == null || m_obsNumber >= observations.size())
		{
			return;
		}
		
		Concept answerConcept = observations.get(m_obsNumber).getValueCoded();
		
		if(answerConcept == null) {
			return;
		}

		m_selectedChildConcept = answerConcept;
		((Element)m_originalNode).setAttribute("data-answered-concept-id", m_selectedChildConcept.getUuid());
		
	}

	
	@Override
	public boolean getValueStoredInOpenMRS(IChildElement child) {
		
		Concept childConcept = child.getConcept();
		
		if(		childConcept == null ||
				m_selectedChildConcept == null ) {
			return false;
		}
		
		return childConcept.equals(m_selectedChildConcept);
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "select";
	}

}