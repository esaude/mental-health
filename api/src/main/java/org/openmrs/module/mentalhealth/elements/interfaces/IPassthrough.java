package org.openmrs.module.mentalhealth.elements.interfaces;


public interface IPassthrough {
	public boolean handlesSubmission();
	public boolean requiresClosingTag();
	public String getTagName();
}
