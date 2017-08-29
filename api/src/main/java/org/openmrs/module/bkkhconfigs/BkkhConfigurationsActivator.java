/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.bkkhconfigs;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.appframework.service.AppFrameworkService;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class BkkhConfigurationsActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Bkkh Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Bkkh Configurations Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Bkkh Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		AppFrameworkService service = Context.getService(AppFrameworkService.class);
		service.disableExtension("referenceapplication.realTime.simpleAdmission");
		service.disableExtension("referenceapplication.realTime.simpleVisitNote");
		service.disableApp("coreapps.mostRecentVitals");
		service.disableApp("referenceapplication.vitals"); // Capture Vitals
		service.disableApp("xforms.formentry");
		service.disableExtension("xforms.formentry.cfpd");
		service.disableExtension("referenceapplication.realTime.vitals");
		service.disableApp("coreapps.diagnoses");
		log.info("Bkkh Configurations Module started");
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Bkkh Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Bkkh Configurations Module stopped");
	}
		
}
