package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntryContext.Mode;
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
		
		Mode formMode = context.getMode();
		
		//in edit mode, if the obs doesnt exist, act like enter mode
		if(m_prevObs==null && formMode==Mode.EDIT) {
			formMode = Mode.ENTER;
		}
		
		if( formMode==Mode.ENTER && (value == null || value.isEmpty()) ) {
			return;
		}
		
		String safeValue = "";
		
		String htmlsafeValue = StringEscapeUtils.escapeHtml(value);
		safeValue = StringEscapeUtils.escapeSql(htmlsafeValue);
		
		//shouldnt happen, play it safe anyway, only ignore in enter mode, in
		//edit mode, this voids an obs
		if( formMode==Mode.ENTER && (safeValue == null || safeValue.isEmpty())) {
			return;
		}
		
		switch(formMode) {
			case EDIT:
				session.getSubmissionActions().modifyObs(m_prevObs, m_openMRSConcept, safeValue, null, null, m_obsNumber);
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, safeValue, null, null, m_obsNumber);
				break;
		
		}
	}
	
	public void takeActionForEditMode(FormEntryContext context)
	{
		getPreviousObsForConcept(context);
		
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
