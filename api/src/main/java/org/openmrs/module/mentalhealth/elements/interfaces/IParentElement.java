package org.openmrs.module.mentalhealth.elements.interfaces;

public interface IParentElement {
	public void addHTMLValueConceptMapping(IChildElement child);
	//parents are created before children, Obs already in openmrs
	//can be loaded based on the xml node parsed by the parent
	public boolean getValueStoredInOpenMRS(IChildElement child);
}
