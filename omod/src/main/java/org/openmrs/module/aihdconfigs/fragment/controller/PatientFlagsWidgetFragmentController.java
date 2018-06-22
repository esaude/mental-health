package org.openmrs.module.aihdconfigs.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResult;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdconfigs.Dictionary;
import org.openmrs.module.aihdconfigs.calculation.ConfigCalculationManager;
import org.openmrs.module.aihdconfigs.calculation.ConfigCalculations;
import org.openmrs.module.aihdconfigs.calculation.ConfigEmrCalculationUtils;
import org.openmrs.module.aihdconfigs.calculation.PatientFlagCalculation;
import org.openmrs.module.aihdconfigs.metadata.PersonAttributeTypes;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PatientFlagsWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient,
                           @SpringBean ConfigCalculationManager calculationManager){

        List<String> flags = new ArrayList<String>();
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        flags.add(missedAppointments(patient.getId(), context));
        flags.add(lostToFollowUp(patient.getId(), context));
        flags.add(inTransit(patient.getId()));
        flags.add(transferredOut(patient.getId(), context));

        model.addAttribute("flags", flags);

    }

    private String missedAppointments(Integer patientId, PatientCalculationContext context){
        String message = "";
        CalculationResultMap lastReturnDateObss = ConfigCalculations.lastObs(Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE), Arrays.asList(patientId), context);
        Date lastScheduledReturnDate = ConfigEmrCalculationUtils.datetimeObsResultForPatient(lastReturnDateObss, patientId);
        if (lastScheduledReturnDate != null && ConfigEmrCalculationUtils.daysSince(lastScheduledReturnDate, context) > 0) {
            message = "Missed Appointment";
        }

        return message;
    }

    private String lostToFollowUp(Integer patientId, PatientCalculationContext context){
        String message = "";
        CalculationResultMap lastReturnDateObss = ConfigCalculations.lastObs(Dictionary.getConcept(Dictionary.RETURN_VISIT_DATE), Arrays.asList(patientId), context);
        Date lastScheduledReturnDate = ConfigEmrCalculationUtils.datetimeObsResultForPatient(lastReturnDateObss, patientId);
        if (lastScheduledReturnDate != null && ConfigEmrCalculationUtils.daysSince(lastScheduledReturnDate, context) > 90) {
            message = "Lost to Followup";
        }

        return message;
    }
    private String inTransit(Integer patientId){
        String message = "";
        PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(PersonAttributeTypes.PATIENT_TYPE.uuid());
        Person person = Context.getPersonService().getPerson(patientId);

        if (person != null && person.getAttribute(personAttributeType) != null) {
            if(person.getAttribute(personAttributeType).getValue().equals("patient_in_transit")) {
                message = "In Transit";
            }
        }

        return message;
    }

    private String transferredOut(Integer patientId, PatientCalculationContext context){
        String message = "";
        CalculationResultMap lastDiscontinueReason = ConfigCalculations.lastObs(Dictionary.getConcept(Dictionary.DISCONTINUE_REASON), Arrays.asList(patientId), context);
        Concept transferOut = ConfigEmrCalculationUtils.codedObsResultForPatient(lastDiscontinueReason, patientId);
        if (transferOut != null && transferOut.equals(Dictionary.getConcept(Dictionary.TRANSFER_OUT))) {
            message = "Transferred Out";
        }

        return message;
    }
}
