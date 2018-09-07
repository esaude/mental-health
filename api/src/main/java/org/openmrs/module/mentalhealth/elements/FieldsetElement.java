package org.openmrs.module.mentalhealth.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.FormSubmissionError;
import org.openmrs.module.htmlformentry.action.FormSubmissionControllerAction;
import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IHandleHTMLEdit;
import org.openmrs.module.mentalhealth.elements.interfaces.IParentElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IPassthrough;
import org.openmrs.module.mentalhealth.utils.NodeUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FieldsetElement extends PassthroughElement implements IHandleHTMLEdit, FormSubmissionControllerAction, IParentElement, IPassthrough {
	
	protected Log log = LogFactory.getLog(getClass());
	
	
	private Map<String, Concept> m_radios = new HashMap<String, Concept>();
	
	private Map<Concept, Boolean> m_radioStates = new HashMap<Concept, Boolean>();
	
	private boolean m_responsibleFieldset = false;
	
	private String m_radioGroupName;
	
	public FieldsetElement(FormEntryContext context, Map<String, String> parameters, Node originalNode) {
		super(context, parameters, originalNode);
		
		log.info("fieldsetelement ctor, after super ctor");
		//parse child nodes to identify if this fieldset is the immediate parent of radios
		//if it has a fieldset child, the nodes between this one and that one will be passed through as normal
		m_responsibleFieldset = isClosestParentOfRadios();
	}
	
	//fieldsets do not require names
	@Override
	protected boolean requiresName() {
		return false;
	}

	private boolean isClosestParentOfRadios() {
		/*boolean result = false;
		
		boolean madeDecision = false;
		
		Node child = m_originalNode.getFirstChild();
		Node sibling = null;
		Stack<Node> parents = new Stack<Node>();
		
		while(child != null) {
			
			/*switch(((Element)child).getTagName().toLowerCase()) {
				case "fieldset":
						result = false;
						madeDecision = true;
					break;
				case "radio":
						result = true;
						madeDecision = true;
					break;
			}*/
		/*	
		String tag = ((Element)child).getTagName().toLowerCase();
			if(tag.equals("fieldset")) {
				result = false;
				madeDecision = true;
			}
			else if(tag.equals("radio")) {
				result = true;
				madeDecision = true;
			}
			
			if(madeDecision)
				break;
			
			if(child.hasChildNodes()) {
				parents.push(child);
				child = child.getFirstChild();
			} 
			
			sibling = child.getNextSibling();
			
			if(sibling!=null) {
				child = child.getNextSibling();
			} else {
				
				try {
					child = parents.pop();
				} catch (Exception e) {
					child = null;
				}
				
			}
			
		}
*/
		boolean parentOfAnotherFieldset = ((Element)m_originalNode).getElementsByTagName("fieldset").getLength()<1;
		
		//((Element)m_originalNode).getElementsByTagName("input").getLength()>0;
		//filter based on attribute type
		//boolean parentOfRadios =  
		
		return parentOfAnotherFieldset;
	}
	
	@Override
	public void takeActionForEditMode(FormEntryContext context) {
		
		//Encounter viewEncounter = context.getExistingEncounter();
		Map<Concept, List<Obs>> existingObs = context.getExistingObs();
		//viewEncounter.
		List<Obs> observations = existingObs.get(m_openMRSConcept);
		
		//NodeList childRadios = ((Element)m_originalNode).getElementsByTagName("radio");
		
		m_radioStates.put(observations.get(0).getConcept(), true);
		//when this is parsing for view/edit, the radios wont have been
		//encountered yet, but we can still build the map
		
		//((Element)m_originalNode).setAttribute("value", observations.get(0).getValueText());
		//result = NodeUtil.stringify();
	}
	
	@Override
	public Collection<FormSubmissionError> validateSubmission(FormEntryContext context, HttpServletRequest submission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleSubmission(FormEntrySession session, HttpServletRequest submission) {
		
		//if no conceptid was provided, or this is not the fieldset responsible
		//for a set of child radios, just return
		if(m_openMRSConcept == null || m_responsibleFieldset) {
			return;
		}
		
		FormEntryContext context = session.getContext();
		
		String value = submission.getParameter(m_radioGroupName);
		
		if(value == null || value.isEmpty()) {
			//throw new IllegalArgumentException("Value for select " + tagName + " cannot be blank/empty");
			return;
		}
		
		Concept responseConcept = m_radios.get(value);
		
		if(responseConcept == null) {
			String fieldsetId = "#"+m_parameters.get("id");
			
			if(fieldsetId.equals("#")) {
				fieldsetId = "with no id";
			}
			
			throw new IllegalArgumentException("Concept for fieldset " + fieldsetId + " radio name " + m_radioGroupName + " and value " + value + " not found!");
		
		}
		
		switch(context.getMode()) {
			case EDIT:
				break;
			case VIEW:
				break;
			case ENTER:
				session.getSubmissionActions().createObs(m_openMRSConcept, responseConcept, null, null, null);
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

		
		//instantiation of radioelement already requires both a name and value
		
		Map<String, String> childAttrs = child.getAttrs();
		
		String childName = childAttrs.get("name");
		String childValue = childAttrs.get("value");
		
		log.info("attaching child with name "+ childName + " and value "+ childValue + " to fieldset " + m_parameters.get("id"));
		
		
		if(m_radioGroupName == null){
			m_radioGroupName = childName;
		}
		
		if(!m_radioGroupName.equals(childName))
			throw new IllegalArgumentException("All radios in a fieldset should have the same name assigned, expected " + m_radioGroupName + " but saw " + childName);
		
		m_radios.put(childValue, child.getConcept());
	}

	@Override
	public boolean getValueStoredInOpenMRS(IChildElement child) {
		// TODO Auto-generated method stub
		return m_radioStates.get(child.getConcept());
	}
	
	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "fieldset";
	}
	

}
