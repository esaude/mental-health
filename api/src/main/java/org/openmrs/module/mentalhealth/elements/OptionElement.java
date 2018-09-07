package org.openmrs.module.mentalhealth.elements;

import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.module.htmlformentry.FormEntryContext;

import org.openmrs.module.htmlformentry.element.HtmlGeneratorElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IChildElement;
import org.openmrs.module.mentalhealth.elements.interfaces.IParentElement;
import org.w3c.dom.Node;

public class OptionElement extends TranslatingElement implements HtmlGeneratorElement, IChildElement{

	/*
	 * Instance Initialization Block (IIB) in Java
	 * Initialization blocks are executed whenever the class is initialized and before constructors are invoked.
	 * They are typically placed above the constructors within braces.
	 * It is not at all necessary to include them in your classes.
	*/
	
	private IParentElement m_parentSelect;
	
	public OptionElement(FormEntryContext context, Map<String, String> parameters, Node originalNode, IParentElement parentElement) {
		super(context, parameters, originalNode);
		
		/*
		 * form
This attribute lets you specify the form element to which the select element is associated (that is, its "form owner"). If this attribute is specified, its value must be the same as the id of a form element in the same document. This enables you to place select elements anywhere within a document, not just as descendants of their form elements.
		 * */
		
		m_parentSelect = parentElement;
		
		m_parentSelect.addHTMLValueConceptMapping(this);
	
	}

	@Override
	protected boolean requiresName() {
		return false;
	}
	
	@Override
	protected boolean requiresValue() {
		return true;
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
	public boolean getDefaultStateFromNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return "option";
	}

}
