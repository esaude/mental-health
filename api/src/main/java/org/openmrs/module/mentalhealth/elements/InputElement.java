package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class InputElement extends PassthroughElement implements IHandleHTMLEdit, IHandleHTMLView, FormSubmissionControllerAction, IPassthrough {
	
	public InputElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
		
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
	public boolean handlesSubmission() {
		// TODO Auto-generated method stub
		return true;
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
		
		((Element)m_originalNode).setAttribute("value", answer.getValueText());
		//result = NodeUtil.stringify();
	
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
		
		try {
			 safeValue = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		
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
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "input";
	}
	
}
