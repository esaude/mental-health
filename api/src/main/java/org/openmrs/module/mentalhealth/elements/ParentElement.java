package org.openmrs.module.mentalhealth.elements;

import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.w3c.dom.Node;

public abstract class ParentElement extends PassthroughElement{
	public ParentElement(FormEntrySession session, Map<String, String> parameters, Node originalNode) {
		super(session, parameters, originalNode);
		// TODO Auto-generated constructor stub
	}
	public abstract void addHTMLValueConceptMapping(IChildElement child);
	//parents are created before children, Obs already in openmrs
	//can be loaded based on the xml node parsed by the parent
	public abstract Object getValueStoredInOpenMRS(IChildElement child);
	
	public abstract boolean hasConceptAssociated();
	
	final
	protected void getPreviousObsForConcept(FormEntryContext context) {
		//has associated concept is used here because it is evaluated before
		//child nodes have been parsed, i.e. is this fieldset intended to repr.
		//an obs
		if( m_openMRSConcept == null || !hasConceptAssociated()) {
			return;
		}
		
		//Encounter viewEncounter = context.getExistingEncounter();
		Map<Concept, List<Obs>> existingObs = context.getExistingObs();
		//viewEncounter.
		if(existingObs == null) {
			return;
		}
		
		List<Obs> observations = existingObs.get(m_openMRSConcept);
		
		Integer iObsNumber = 0;
		
		try {
			iObsNumber = Integer.parseInt(m_obsNumber);
		} catch (Exception e) {
			iObsNumber = 0;
		}
		
		//since parsing in a single pass, how many of the same obs will be seen while processing the xml/html
		//is unknown. An array could be built with null entries using existing obs (basically a LUT),
		//but will take more time if done everytime here than just checking all existing obs for a given 
		//concept... if it can be built in a common handler (and some other checks like if obs exist at all),
		//some performance benefits may be seen (if necessary)
		if(observations == null || iObsNumber < 0)
		{
			return;
		}
		
		for(Obs ob : observations) {
			if(m_obsNumber.equals(ob.getComment())) {
				m_prevObs = ob;
			}
		}
	}

}
