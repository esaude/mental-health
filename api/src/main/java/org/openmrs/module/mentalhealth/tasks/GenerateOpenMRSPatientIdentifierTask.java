package org.openmrs.module.mentalhealth.tasks;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.IdentifierSource;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class GenerateOpenMRSPatientIdentifierTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(GenerateOpenMRSPatientIdentifierTask.class);

    @Override
    public void execute() {
        if (!isExecuting) {
            if (log.isDebugEnabled()) {
                log.debug("Starting to generate missing openmrs ids for patients");
            }

            startExecuting();
            try {
                //do all the work here
                generateOpenMRSIdentifierForPatientsWithout();
            }
            catch (Exception e) {
                log.error("Error while generating OpenMRS id for patient", e);
            }
            finally {
                stopExecuting();
            }
        }
    }

    private PatientIdentifier generateMissingPatientIdentifier() {
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
            PatientIdentifier pid = generateMissingPatientIdentifier();
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
}
