package org.openmrs.module.aihdconfigs.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdconfigs.Dictionary;
import org.openmrs.module.aihdconfigs.calculation.ConfigCalculations;
import org.openmrs.module.aihdconfigs.calculation.ConfigEmrCalculationUtils;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PatientVitalsWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient) {
        Map<String, String> summary = new HashMap<String, String>();

        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        summary.putAll(temperature(patient.getId(), context));
        summary.putAll(bp(patient.getId(), context));
        summary.putAll(pr(patient.getId(), context));
        summary.putAll(respiratoryRate(patient.getId(), context));
        model.addAttribute("vitals", summary);
    }

    private Map<String, String> temperature(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastTempObs = ConfigCalculations.lastObs(Dictionary.getConcept("5088AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double value = ConfigEmrCalculationUtils.numericObsResultForPatient(lastTempObs, patientId);
        if(value != null){
            results.put("Temperature", String.valueOf(value));
        }
        else {
            results.put("Temperature", "None");
        }
        return  results;
    }

    private Map<String, String> bp(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastBpSystolicObs = ConfigCalculations.lastObs(Dictionary.getConcept("5085AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        CalculationResultMap lastDiastollicObs = ConfigCalculations.lastObs(Dictionary.getConcept("5086AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double valueSystolic = ConfigEmrCalculationUtils.numericObsResultForPatient(lastBpSystolicObs, patientId);
        Double valueDiastolic = ConfigEmrCalculationUtils.numericObsResultForPatient(lastDiastollicObs, patientId);
        if(valueSystolic != null && valueDiastolic != null){
            results.put("BP", String.valueOf(valueSystolic)+"/"+String.valueOf(valueDiastolic));
        }
        else {
            results.put("BP", "None");
        }
        return  results;
    }

    private Map<String, String> pr(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastPrObs = ConfigCalculations.lastObs(Dictionary.getConcept("5087AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double valuePr = ConfigEmrCalculationUtils.numericObsResultForPatient(lastPrObs, patientId);
        if(valuePr != null){
            results.put("PR", String.valueOf(valuePr));
        }
        else {
            results.put("PR", "None");
        }
        return  results;
    }
    private Map<String, String> respiratoryRate(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastRrObs = ConfigCalculations.lastObs(Dictionary.getConcept("5242AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double valueRr = ConfigEmrCalculationUtils.numericObsResultForPatient(lastRrObs, patientId);
        if(valueRr != null){
            results.put("Respiratory rate", String.valueOf(valueRr));
        }
        else {
            results.put("Respiratory rate", "None");
        }
        return  results;
    }
}
