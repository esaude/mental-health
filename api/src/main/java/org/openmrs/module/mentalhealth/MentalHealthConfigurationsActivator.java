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
package org.openmrs.module.mentalhealth;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.mentalhealth.activator.MentalHealthAppConfigurationInitializer;
import org.openmrs.module.mentalhealth.activator.MentalHealthHtmlFormsInitializer;
import org.openmrs.module.mentalhealth.activator.MentalHealthInitializer;
import org.openmrs.module.mentalhealth.deploy.MentalHealthCommonMetadataBundle;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class MentalHealthConfigurationsActivator extends BaseModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Mental Health Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Mental Health Configurations Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Mental Health Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
		MetadataDeployService deployService = Context.getService(MetadataDeployService.class);


		// install commonly used metadata
		installCommonMetadata(deployService);

		// run the initializers
		for (MentalHealthInitializer initializer : getInitializers()) {
			initializer.started();
		}
		// generate OpenMRS ID for patients without the identifier
		generateOpenMRSIdentifierForPatientsWithout();


		log.info("Aihd Configurations Module started");
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Aihd Configurations Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Aihd Configurations Module stopped");
	}

	private List<MentalHealthInitializer> getInitializers() {
		List<MentalHealthInitializer> l = new ArrayList<MentalHealthInitializer>();
		l.add(new MentalHealthAppConfigurationInitializer());
		l.add(new MentalHealthHtmlFormsInitializer());
		return l;
	}

	/**
	 * Generate an OpenMRS ID for patients who do not have one due to a migration from an old OpenMRS ID to a new one which contains a check-digit
	 **/
	private void generateOpenMRSIdentifierForPatientsWithout() {
		PatientService patientService = Context.getPatientService();
		AdministrationService as = Context.getAdministrationService();

		List<List<Object>> patientIds = as.executeSQL("SELECT patient_id FROM patient_identifier WHERE patient_id NOT IN (SELECT patient_id FROM patient_identifier p INNER JOIN patient_identifier_type pt ON (p.identifier_type = pt.patient_identifier_type_id AND pt.uuid = '05a29f94-c0ed-11e2-94be-8c13b969e334'))", true);

		if (patientIds.size() == 0) {
			// no patients to process
			return;
		}
		// get the identifier source copied from RegistrationCoreServiceImpl

		for (List<Object> row : patientIds) {
			Patient p = patientService.getPatient((Integer) row.get(0));
			// Create new Patient Identifier
			PatientIdentifier pid = generatePatientIdentifier();
			pid.setPatient(p);
			try {
				log.info("Adding OpenMRS ID " + pid.getIdentifier() + " to patient with id " + p.getPatientId());
				// Save the patient Identifier
				patientService.savePatientIdentifier(pid);
			} catch (Exception e) {
				// log the error to the alert service but do not rethrow the exception since the module has to start
				log.error("Error updating OpenMRS identifier for patient #" + p.getPatientId(), e);
			}
		}
		log.info("All patients updated with new OpenMRS ID");
	}

	protected PatientIdentifier generatePatientIdentifier() {
		IdentifierSourceService iss = Context.getService(IdentifierSourceService.class);
		IdentifierSource idSource = iss.getIdentifierSource(1); // this is the default OpenMRS identifier source
		PatientService patientService = Context.getPatientService();

		UUID uuid = UUID.randomUUID();

		PatientIdentifierType patientIdentifierType = patientService.getPatientIdentifierTypeByUuid("05a29f94-c0ed-11e2-94be-8c13b969e334");

		PatientIdentifier pid = new PatientIdentifier();
		pid.setIdentifierType(patientIdentifierType);
		String identifier = iss.generateIdentifier(idSource, "New OpenMRS ID with CheckDigit");
		pid.setIdentifier(identifier);
		pid.setPreferred(true);
		pid.setUuid(String.valueOf(uuid));

		return pid;

	}

	private void installCommonMetadata(MetadataDeployService deployService) {
		try {
			deployService.installBundle(Context.getRegisteredComponents(MentalHealthCommonMetadataBundle.class).get(0));


		}
		catch (Exception e) {
			Module mod = ModuleFactory.getModuleById("mentalhealth");
			ModuleFactory.stopModule(mod);
			throw new RuntimeException("failed to install the common metadata ", e);
		}
	}


}
