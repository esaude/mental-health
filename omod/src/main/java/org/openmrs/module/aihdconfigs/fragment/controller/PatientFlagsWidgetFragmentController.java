package org.openmrs.module.aihdconfigs.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
        /*// Gather all flag calculations that evaluate to true in a generic way
        System.out.println("The size is"+calculationManager.getFlagCalculations().size());
        for (PatientFlagCalculation calc : calculationManager.getFlagCalculations()) {
            System.out.println("The report reaches here:"+calc.getFlagMessage());
            try {
                CalculationResult result = Context.getService(PatientCalculationService.class).evaluate(patient.getPatient().getPatientId(), calc);
                if (result != null && (Boolean) result.getValue()) {
                    flags.add(calc.getFlagMessage());
                }
            }
            catch (Exception ex) {
                log.error("Error evaluating " + calc.getClass(), ex);
            }
        }*/

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
        return "Lost to Follow up";
    }
}
