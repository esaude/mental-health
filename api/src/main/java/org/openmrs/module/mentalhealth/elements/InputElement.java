package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.FormEntryContext.Mode;
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
	
	public void takeActionForEditMode(FormEntryContext context)
	{
		//gets corresponding m_prevObs if one exists
		getPreviousObsForConcept(context);

		//returns string based on m_prevObs
		String inputValue = derivedClassSpecializeHTMLEditProcessing();
		
		if(inputValue != null) {
			((Element)m_originalNode).setAttribute("value", inputValue);
		}
	
	}
	
	//does nothing in this class, but derived classes can override
	//this method to implement any additional procesisng that may need to
	//be done
	public String derivedClassSpecializeHTMLEditProcessing()
	{
		String result = null;
		
		if(m_prevObs!=null) {
			result = m_prevObs.getValueText();
		}

		return result;
	}
	
	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		//if it doesn't have a concept or encounter function, just return
		if( m_openMRSConcept == null && m_encounterFn == null) {
			return;
		}
		
		FormEntryContext context = session.getContext();
		
		String value = submission.getParameter(m_parameters.get("name"));
		
		Mode formMode = context.getMode();
		
		//in edit mode, if the obs doesnt exist, act like enter mode
		if(m_prevObs==null && formMode==Mode.EDIT) {
			formMode = Mode.ENTER;
		}
			
		if( formMode == Mode.ENTER && (value == null || value.isEmpty()) ) {
			return;
		}
		
		Object valueToStore = null;
		
		String safeValue = "";
		
		String htmlsafeValue = StringEscapeUtils.escapeHtml(value);
		safeValue = StringEscapeUtils.escapeSql(htmlsafeValue);

		valueToStore = safeValue;
		
		//shouldnt happen, play it safe anyway, only ignore in enter mode, in
		//edit mode, this voids an obs
		if(formMode==Mode.ENTER && (safeValue == null || safeValue.isEmpty())) {
			return;
		}
		
		valueToStore = derivedClassSpecializeHandleSubmission(session, safeValue);
		
		//a null value returned from derived element indicates it has handled
		//the submission itself, and this class should simply return
		if(formMode==Mode.ENTER && valueToStore == null)
			return;
		
		switch(formMode) {
			case EDIT:
				session.getSubmissionActions().modifyObs(m_prevObs, m_openMRSConcept, valueToStore, null, null, m_obsNumber);
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, valueToStore, null, null, m_obsNumber);
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
