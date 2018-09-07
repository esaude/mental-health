package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;
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

	protected String m_openTag;
	
	protected String m_closeTag;
	
	protected Integer m_obsNumber;
	
	public PassthroughElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		
		//store the original node to pass through when generating HTML
		m_originalNode = originalNode;
		
		//more convenient way to check params
		m_parameters = parameters;
		
		String conceptId = m_parameters.get("data-concept-id");
		
		if(conceptId != null && !conceptId.isEmpty() ) {
			
			m_openMRSConcept = HtmlFormEntryUtil.getConcept(conceptId);
			
			if (m_openMRSConcept == null) {
				throw new IllegalArgumentException("Cannot find concept for value " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters);
			}
	
			if(requiresName() && m_parameters.get("name")==null) {
				throw new IllegalArgumentException("Cannot find name attribute for element with conceptId " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters + " " + getClass() + " " + requiresName());
			}
			
			if(requiresValue() && m_parameters.get("value")==null) {
				throw new IllegalArgumentException("Cannot find value attribute for element with conceptId " + conceptId
				        + " in conceptId attribute value. Parameters: " + parameters);
			}
			
			try {
				
				m_obsNumber = Integer.parseInt(m_parameters.get("data-obs-number"));
			
			} catch (Exception e) {
				m_obsNumber = 0;
			}
			
			
		}

		String tags[] = NodeUtil.generateSeparateTags(originalNode);
		m_openTag = tags[0];
		m_closeTag = tags[1];
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
		return m_closeTag;
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
		// TODO Auto-generated method stub
		return false;
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
