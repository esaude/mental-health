package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class TextAreaElement extends PassthroughElement implements IHandleHTMLEdit, FormSubmissionControllerAction {

	public TextAreaElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		if( m_openMRSConcept == null ) {
			return;
		}
		
		FormEntryContext context = session.getContext();
		
		String value = submission.getParameter(m_parameters.get("name"));
		
		if( value == null || value.isEmpty() ) {
			return;
		}
		
		String safeValue = "";
		
		safeValue = StringEscapeUtils.escapeHtml(value);
		
		//shouldnt happen, play it safe anyway
		if(safeValue == null || safeValue.isEmpty()) {
			return;
		}
		
		
		switch(context.getMode()) {
			case EDIT:
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, safeValue, null, null, null);
				break;
		
		}
	}
	
	public void takeActionForEditMode(FormEntryContext context)
	{
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
		
		if(observations==null || m_obsNumber >= observations.size()) {
			return;
		}
		
		Obs answer = observations.get(m_obsNumber);
		
		if(answer == null) {
			return;
		}
		
		Node textChildNode = m_originalNode.getOwnerDocument().createTextNode(answer.getValueText());
		
		((Element)m_originalNode).appendChild(textChildNode);
	
	}

	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
	}

}
