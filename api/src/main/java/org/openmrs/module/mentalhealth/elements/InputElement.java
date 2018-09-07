package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLView;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.openmrs.module.mentalhealth.utils.NodeUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InputElement extends PassthroughElement implements IHandleHTMLEdit, IHandleHTMLView, FormSubmissionControllerAction, IPassthrough {
	
	public InputElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
		
	}
	
	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		
		throw new IllegalStateException("This validate stub should not be registered");
	}
	
	@Override
	public String closeTag() {
		//Inputs should always be self closing
		return "";
	}


	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		throw new IllegalStateException("This handleSubmission stub should not be registered");
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
	public String getTagName() {
		// TODO Auto-generated method stub
		return "input";
	}
	
}
