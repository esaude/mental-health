package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;

import org.openmrs.module.mentalhealth.utils.NodeUtil;

//reference org.openmrs.module.htmlformentry.element.ObsSubmissionElement;

public class StringValueObsElement extends InputElement implements FormSubmissionControllerAction {

	public enum Type{
		Input,
		TextArea
	};
	
	private Type m_type;
		
	public StringValueObsElement(FormEntryContext context, Map<String, String> parameters, Node originalNode, Type inputType) {
		super(context, parameters, originalNode);
		
		//a fieldset handler will correlate radios concept ids
		//it should be implemented as an iteratingHandler to allow it consume
		//the child rows, preventing this from having to process that case at all
		/*if( m_parameters.containsKey("type") && m_parameters.get("type").toLowerCase()!="radio")
		{
			m_handlesOwnSubmission = false;
		}*/
		
		m_type=inputType;
	}

	public Type getStringValueType() {
		return m_type;
	}

	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
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
		
		
		switch(context.getMode()) {
			case EDIT:
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, value, null, null, null);
				break;
		
		}
	}
	
	@Override
	public String closeTag() {
		
		if(m_type == Type.TextArea)
			return m_closeTag;
		//Inputs should always be self closing
		return "";
	}

}
