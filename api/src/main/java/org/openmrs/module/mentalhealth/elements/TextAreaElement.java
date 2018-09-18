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

	public TextAreaElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
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
		
		String htmlsafeValue = StringEscapeUtils.escapeHtml(value);
		safeValue = StringEscapeUtils.escapeSql(htmlsafeValue);
		
		//shouldnt happen, play it safe anyway
		if(safeValue == null || safeValue.isEmpty()) {
			return;
		}
		
		
		switch(context.getMode()) {
			case EDIT:
				if(m_prevObs!=null) {
					session.getSubmissionActions().modifyObs(m_prevObs, m_openMRSConcept, safeValue, null, null, null);
				} else {
					session.getSubmissionActions().createObs(m_openMRSConcept, safeValue, null, null, null);
				}
				
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
		
		Integer iObsNumber = 0;
		
		try {
			iObsNumber = Integer.parseInt(m_obsNumber);
		} catch (Exception e) {
			iObsNumber = 0;
		}

		List<Obs> observations = existingObs.get(m_openMRSConcept);
		
		if(observations==null || iObsNumber >= observations.size()) {
			return;
		}
		
		m_prevObs = observations.get(iObsNumber);
		
		if(m_prevObs == null) {
			return;
		}
		
		Node textChildNode = m_originalNode.getOwnerDocument().createTextNode(m_prevObs.getValueText());
		
		((Element)m_originalNode).appendChild(textChildNode);
	
	}

	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
	}

}
