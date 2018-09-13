package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlFormEntryUtil;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEnter;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLView;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.openmrs.module.mentalhealth.utils.NodeUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PassthroughElement implements HtmlGeneratorElement, IHandleHTMLView, IHandleHTMLEdit, IHandleHTMLEnter, IPassthrough {

	protected Log log = LogFactory.getLog(getClass());
	
	protected Node m_originalNode;
	
	protected Map<String, String> m_parameters;
	
	protected Concept m_openMRSConcept;

	protected Integer m_obsNumber;
	
	protected String m_encounterFn;
	
	public PassthroughElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		
		//store the original node to pass through when generating HTML
		m_originalNode = originalNode;
		
		//more convenient way to check params
		m_parameters = parameters;
		
		m_encounterFn = m_parameters.get("data-encounter-function");
		
		String conceptId = m_parameters.get("data-concept-id");
		
		boolean hasConcept = conceptId != null && !conceptId.isEmpty();
		
		if( hasConcept ) {
			
			m_openMRSConcept = HtmlFormEntryUtil.getConcept(conceptId);
			
			if (m_openMRSConcept == null) {
				/*throw new IllegalArgumentException("Cannot find concept for value " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters);*/
				((Element)m_originalNode).setAttribute("style", "background-color: red;");
				return;
			}
	
			try {
				
				m_obsNumber = Integer.parseInt(m_parameters.get("data-obs-number"));
			
			} catch (Exception e) {
				m_obsNumber = 0;
			}
			
		}
		
		if( handlesSubmission() ) {
			if(requiresName() && m_parameters.get("name")==null) {
				throw new IllegalArgumentException("Cannot find name attribute for element with conceptId " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters + " " + getClass() + " " + requiresName());
			}
			
			if(requiresValue() && m_parameters.get("value")==null) {
				throw new IllegalArgumentException("Cannot find value attribute for element with conceptId " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters);
			}
		}
		
		String shouldLookupValueExpression = m_parameters.get("data-lookup-expression-value");
		
		if(shouldLookupValueExpression != null && shouldLookupValueExpression.equals("true")) {
			String lookupExpression = m_parameters.get("value");
			String lookupString = session.evaluateVelocityExpression("$!{" + lookupExpression + "}");
			((Element)m_originalNode).setAttribute("value", lookupString);
		}

		String translateValue = m_parameters.get("data-translate-value");
		if(translateValue != null && translateValue.equals("true")) {
			String translatedString = Context.getMessageSourceService().getMessage(m_parameters.get("value"));
			((Element)m_originalNode).setAttribute("value", translatedString);
		}
		
		
		
	}
	
	protected boolean requiresName() {
		return true;
	}
	
	protected boolean requiresValue() {
		return false;
	}
	
	final
	@Override
	public String generateHtml(FormEntryContext context) {
		
		if( m_originalNode!=null  )
		{
			switch(context.getMode()) {
				case VIEW:	
					this.takeActionForViewMode(context);
					
				case EDIT:
					this.takeActionForEditMode(context);
					
				case ENTER:
					this.takeActionForEnterMode(context);
					break;
			}
			
			return NodeUtil.generateSeparateTags(m_originalNode)[0];
		}
		
		return "";
	}
	
	public String closeTag() {
		
		//return m_closeTag;
		return NodeUtil.generateSeparateTags(m_originalNode)[1];
	}

	@Override
	public void takeActionForEditMode(FormEntryContext context) {
		//by default does nothing, but can be overridden by derived classes as necessary
	}

	@Override
	public void takeActionForEnterMode(FormEntryContext context) {
		//by default does nothing, but can be overridden by derived classes as necessary
	}

	@Override
	public void takeActionForViewMode(FormEntryContext context) {
		((Element)m_originalNode).setAttribute("disabled", "true");
	}

	@Override
	public boolean handlesSubmission() {
		//if an element has it's own concept id, it handles it's own submission
		//if it serves the function of providing the encounter date, it handles
		//it's own submission
				
		//if it doesn't have it's own concept id or an encounter fn,
		//it could be parented to provide the obsDateTime for the parent's
		//concept Observation, or an answerConcept, but the parent handles
		//the submission, or nothing does
		return m_parameters.get("data-concept-id") != null || m_encounterFn != null;
	}

	@Override
	public boolean requiresClosingTag() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		throw new IllegalArgumentException("PassthroughElement getTagName() should be overridden by derived type!");
	}
}
