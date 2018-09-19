package org.openmrs.module.mentalhealth.elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DateElement extends InputElement implements IChildElement, FormSubmissionControllerAction {

	ParentElement m_parent;
	
	
	public DateElement(FormEntrySession session, Map<String, String> parameters, Node originalNode, ParentElement parent) {
		super(session, parameters, originalNode);
		
		FormEntryContext context = session.getContext();
		
		//if this date input has no concept mapping of it's own, it's meant to
		//set the date on a parent elements observation
		if(m_openMRSConcept==null) {
			
			m_parent = parent;
			
			if(m_parent != null) {
				parent.addHTMLValueConceptMapping(this);
			}
			
		}

		if( m_encounterFn!=null && m_encounterFn.equals("date") ) {

			//for view/edit modes
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			
			Encounter currentEncounter = context.getExistingEncounter();
			//in enter and preview, encounterdate is null
			Date encounterDate = null;
			
			if(currentEncounter!=null) {
				encounterDate = currentEncounter.getEncounterDatetime();
			}
			
			String encounterDateStrValue = "";
			if(encounterDate != null)
			{
				encounterDateStrValue = dateFormatter.format(encounterDate);
			}
			
			((Element)originalNode).setAttribute("value", encounterDateStrValue);
		}
	}

	@Override
	public Concept getConcept() {
		return m_openMRSConcept;
	}

	@Override
	public Map<String, String> getAttrs() {
		return m_parameters;
	}

	@Override
	public Object getDefaultStateFromNode() {
		String dateString = m_parameters.get("value");
		
		if(dateString == null) {
			dateString = "";
		}
		
		return dateString;
	}

	/*@Override
	public boolean handlesSubmission() {
		//if a date has it's own concept id, it handles it's own submission
		//if it serves the function of providing the encounter date, it handles
		//it's own submission
		
		//if it doesn't have it's own concept id or an encounter fn,
		//it should be parented provide the obsDateTime for the parent's
		//concept Observation
		return m_parameters.get("data-concept-id") != null || m_encounterFn != null;
	}*/
	
	@Override
	public Object derivedClassSpecializeHandleSubmission(FormEntrySession session, String safeValue) {
			
		//parented date elements should not create there own concepts
		if(m_parent!=null)
			return null; 
			
		Date valueToStore = null;
		
		//input[type=date] displays the format to the user based on navigator.language
		//but always stores and sends yyyy-mm-dd
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			valueToStore = dateFormatter.parse(safeValue);
		} catch (ParseException e) {
			//should log error? issues that would show up here should be 
			//caught in handleValidation

			//don't throw during submission (avoid 500s)
			//return null;
		}

		
		if( m_encounterFn != null && m_encounterFn.equals("date") ) {
		
	        if (session.getSubmissionActions().getCurrentEncounter().getEncounterDatetime() != null
	                && !session.getSubmissionActions().getCurrentEncounter().getEncounterDatetime().equals(valueToStore)) {
	            session.getContext().setPreviousEncounterDate(
	                    new Date(session.getSubmissionActions().getCurrentEncounter().getEncounterDatetime().getTime()));
	        }
	        
	        session.getSubmissionActions().getCurrentEncounter().setEncounterDatetime(valueToStore);
	        
	        //set to null to indicate the base class should consider the
	        //submission already handled and it has no more work to do
	        valueToStore = null;
		}
		
		return valueToStore;
	}

	@Override
	protected boolean noHtmlAction() {
		//if this date has no concept of it's own and no parent or the parent has no concept too
		//then take no action when html is being rendered
		return m_openMRSConcept == null && (m_parent == null || !m_parent.hasConceptAssociated());
	}
	
	@Override
	public String derivedClassSpecializeHTMLEditProcessing() {
		
		Date answerDate = null;
		
		if(m_prevObs != null) {
			answerDate = m_prevObs.getValueDate();
		} else if(m_parent != null && m_parent.hasConceptAssociated()) {
			Object valFromParent = m_parent.getValueStoredInOpenMRS(this);
			
			if(valFromParent instanceof Date) {
				answerDate = (Date) valFromParent; 
			}
		}
		
		String inputValue = null;
		
		if(answerDate !=null) {
			//use format required by HTML5 spec for input[type=date]
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			inputValue = dateFormatter.format(answerDate);
		}
		
		return inputValue;
	}
	
	
}
