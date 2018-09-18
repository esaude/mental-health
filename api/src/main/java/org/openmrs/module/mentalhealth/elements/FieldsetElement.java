package org.openmrs.module.mentalhealth.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FieldsetElement extends ParentElement implements IHandleHTMLEdit, FormSubmissionControllerAction, IPassthrough {
	
	//private Map<String, Concept> m_radios = new HashMap<String, Concept>();
	
	private Map<String, IChildElement> m_children = new HashMap<String, IChildElement>();

	private Concept m_selectedChildConcept = null;
	
	private String m_formValueName;

	private Date m_previouslyRecordedObsDateTime = null;
	
	public FieldsetElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		
		log.info("fieldsetelement ctor, after super ctor");
		//parse child nodes to identify if this fieldset is the immediate parent of radios
		//if it has a fieldset child, the nodes between this one and that one will be passed through as normal
		//m_responsibleFieldset = isClosestParentOfRadios();
	}
	
	//fieldsets do not require names
	@Override
	protected boolean requiresName() {
		return false;
	}

	private boolean hasChildrenAfterHTMLParse() {
		return m_children.size()>0;
	}
	
	@Override
	public void takeActionForEditMode(FormEntryContext context) {
		
		getPreviousObsForConcept(context);
		
		if(m_prevObs == null)
			return;
		
		Concept answerConcept = m_prevObs.getValueCoded();
		
		if(answerConcept == null) {
			return;
		}

		m_selectedChildConcept = answerConcept;
		((Element)m_originalNode).setAttribute("data-answered-concept-id", String.valueOf(m_selectedChildConcept.getUuid()));

		m_previouslyRecordedObsDateTime = m_prevObs.getObsDatetime();

	}
	
	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		//if no conceptid was provided, or this is not the fieldset responsible
		//for a set of child elements 
		//(at this time child nodes were already processed during html generation), just return
		if(m_openMRSConcept == null || !hasChildrenAfterHTMLParse()) {
			return;
		}
		
		FormEntryContext context = session.getContext();
		
		String responseName = null;
		
		Map<String, Concept> radios = new HashMap<String, Concept>();
		
		Date obsDateTime = null;
		
		Concept responseConcept = null;
		
		for(Entry<String, IChildElement> childEntry: m_children.entrySet()) {
			IChildElement child = childEntry.getValue();
			
			if( child instanceof RadioElement) {
				radios.put(childEntry.getKey(), child.getConcept());
				
				responseName = m_formValueName;
				
				//comparator functor?
			} else if( child instanceof DateElement ) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				try {
					String dateFormName = child.getAttrs().get("name");
					String dateString = null;
					
					if(dateFormName!=null) {
						dateString = submission.getParameter(dateFormName);
					}
					
					if(dateString != null) {
						obsDateTime = dateFormatter.parse(dateString);
					}
				} catch (ParseException e) {
					
					e.printStackTrace();
					
				}
			} else if( child instanceof CheckboxElement ) {
				responseName = child.getAttrs().get("name");
				responseConcept = child.getConcept();
			}
			
		}
		
		String value = submission.getParameter(responseName);
		
		if(value == null || value.isEmpty()) {
			//throw new IllegalArgumentException("Value for select " + tagName + " cannot be blank/empty");
			return;
		}
		
		if(radios.size() > 0) {
			responseConcept = radios.get(value);
		}
		
		if(responseConcept == null) {
			String fieldsetId = "#"+m_parameters.get("id");
			
			if(fieldsetId.equals("#")) {
				fieldsetId = "with no id";
			}
			
			throw new IllegalArgumentException("Concept for fieldset " + fieldsetId + " radio name " + m_formValueName + " and value " + value + " not found!");
		
		}
		
		
		
		switch(context.getMode()) {
			case EDIT:
				session.getSubmissionActions().modifyObs(m_prevObs, m_openMRSConcept, responseConcept, obsDateTime, null, m_obsNumber);
				break;
			case VIEW:
				break;
			case ENTER:
				//m_obsNumber should always be at least 0, parsed to "0"
				session.getSubmissionActions().createObs(m_openMRSConcept, responseConcept, obsDateTime, null, m_obsNumber);
				break;
		
		}
		
	}

	@Override
	public boolean handlesSubmission() {
		//this class handles submission for itself (and child radio conceptIds)
		return true;
	}

	@Override
	public boolean requiresClosingTag() {
		//should always have an open and closing tag
		return false;
	}

	@Override
	public void addHTMLValueConceptMapping(IChildElement child) {

		if(!hasConceptAssociated()) {
			return;
		}
		
		//instantiation of radioelement already requires both a name and value
		
		Map<String, String> childAttrs = child.getAttrs();
		
		if(childAttrs == null)
			return;
		
		String childValue = childAttrs.get("value");
		
		if(child instanceof RadioElement) {
			String childName = childAttrs.get("name");
			
			log.info("attaching child with name "+ childName + " and value "+ childValue + " to fieldset " + m_parameters.get("id"));
			
			if(m_formValueName == null){
				m_formValueName = childName;
			}
			
			if(!m_formValueName.equals(childName)) {
				throw new IllegalArgumentException("All radios in a fieldset should have the same name assigned, expected " + m_formValueName + " but saw " + childName);
			}
			
		}/* else if(child instanceof CheckboxElement){
			m_formValueName = child.getAttrs().get("name");
		}*/
		
		m_children.put(childValue, child);
	}

	@Override
	public Object getValueStoredInOpenMRS(IChildElement child) {
		
		if(!hasConceptAssociated()) {
			return false;
		}
		
		if(!(child instanceof DateElement) ) {
		
			Concept childConcept = child.getConcept();
			
			if(		childConcept == null ||
					m_selectedChildConcept == null ) {
				return false;
			}
			
			return childConcept.equals(m_selectedChildConcept);
		}
		
		return m_previouslyRecordedObsDateTime;
		
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "fieldset";
	}

	@Override
	public boolean hasConceptAssociated() {
		return m_parameters.get("data-concept-id")!=null;
	}
	

}
