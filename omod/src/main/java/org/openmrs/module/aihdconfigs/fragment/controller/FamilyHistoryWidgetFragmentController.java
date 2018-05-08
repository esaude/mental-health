package org.openmrs.module.aihdconfigs.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
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

public class FamilyHistoryWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient) {
        Map<String, String> summary = new HashMap<String, String>();

        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        summary.putAll(diabetic(patient.getId(), context));
        summary.putAll(hypertension(patient.getId(), context));

        model.addAttribute("history", summary);
    }

    private Map<String, String> diabetic(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastDiabeticObs = ConfigCalculations.lastObs(Dictionary.getConcept("140228AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Obs obs = ConfigEmrCalculationUtils.obsResultForPatient(lastDiabeticObs, patientId);
        if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))){
            results.put("Diabetic", "Yes");
        }
        else {
            results.put("Diabetic", "No");
        }
        return  results;
    }

    private Map<String, String> hypertension(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastHypetension = ConfigCalculations.lastObs(Dictionary.getConcept("26d521eb-e1df-464c-8356-93616078080d"), Arrays.asList(patientId), context);
        Obs obs = ConfigEmrCalculationUtils.obsResultForPatient(lastHypetension, patientId);
        if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("1065AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))){
            results.put("Hypertension", "Yes");
        }
        else {
            results.put("Hypertension", "No");
        }
        return  results;
    }
}
