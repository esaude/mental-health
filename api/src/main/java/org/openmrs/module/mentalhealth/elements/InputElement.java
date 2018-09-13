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
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLView;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class InputElement extends PassthroughElement implements IHandleHTMLEdit, IHandleHTMLView, FormSubmissionControllerAction, IPassthrough {
	
	
	public InputElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		
	}
	
	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		return null;
	}
	
	@Override
	public String closeTag() {
		//Inputs should always be self closing
		return "";
	}


	@Override
	public boolean requiresClosingTag() {
		//basically the only self closing tag, but including multiple types
		return false;
	}

	/*public void modifyHTMLNodeForEnterMode( context)
	{
		//result= NodeUtil.stringify(m_originalNode);
		
	}*/
	
	protected boolean noHtmlAction() {
		return m_openMRSConcept == null;
	}
	
	public void takeActionForEditMode(FormEntryContext context)
	{
		if( noHtmlAction() ) {
			return;
		}
		//Encounter viewEncounter = context.getExistingEncounter();
		Map<Concept, List<Obs>> existingObs = context.getExistingObs();
		//viewEncounter.
		
		if(existingObs == null) {
			return;
		}
		
		Obs answer = null;
		
		if( m_openMRSConcept != null ) {
			
			List<Obs> observations = existingObs.get(m_openMRSConcept);
			
			if(observations==null || m_obsNumber >= observations.size()) {
				return;
			}
			
			
			answer = observations.get(m_obsNumber);
			
			if(answer == null) {
				return;
			}
		}
		
		String inputValue = derivedClassSpecializeHTMLEditProcessing(answer);
		
		if(inputValue != null) {
			((Element)m_originalNode).setAttribute("value", inputValue);
		}
	
	}
	
	//does nothing in this class, but derived classes can override
	//this method to implement any additional procesisng that may need to
	//be done
	public String derivedClassSpecializeHTMLEditProcessing(Obs answer)
	{
		return answer.getValueText();
	}
	
	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		//if it doesn't have a concept or encounter function, just return
		if( m_openMRSConcept == null && m_encounterFn == null) {
			return;
		}
		
		FormEntryContext context = session.getContext();
		
		String value = submission.getParameter(m_parameters.get("name"));
		
		if( value == null || value.isEmpty() ) {
			return;
		}
		
		Object valueToStore = null;
		
		String safeValue = "";
		
		String htmlsafeValue = StringEscapeUtils.escapeHtml(value);
		safeValue = StringEscapeUtils.escapeSql(htmlsafeValue);

		valueToStore = safeValue;
		
		//shouldn't happen, play it safe anyway
		if(safeValue == null || safeValue.isEmpty()) {
			return;
		}
		
		valueToStore = derivedClassSpecializeHandleSubmission(session, safeValue);
		
		//a null value returned from derived element indicates it has handled
		//the submission itself, and this class should simply return
		if(valueToStore == null)
			return;
		
		switch(context.getMode()) {
			case EDIT:
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, valueToStore, null, null, null);
				break;
		
		}
	}
	
	public Object derivedClassSpecializeHandleSubmission(FormEntrySession session, String safeValue) {
		return safeValue;
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "input";
	}
	
}
