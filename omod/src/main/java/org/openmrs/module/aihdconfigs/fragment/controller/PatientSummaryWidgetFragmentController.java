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

public class PatientSummaryWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient){
        Map<String, String> summary = new HashMap<String, String>();
        Map<String, String> diabetes = new HashMap<String, String>();
        Map<String, String> hypertension = new HashMap<String, String>();


        //set context
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        diabetes.putAll(diabetic(patient.getId(), context));
        hypertension.putAll(hypertension(patient.getId(), context));
        summary.putAll(rbs(patient.getId(), context));
        summary.putAll(fbs(patient.getId(), context));
        summary.putAll(uecs(patient.getId(), context));
        //summary.putAll(familyHistory(patient.getId(), context));
        model.addAttribute("summary", summary);
        model.addAttribute("diabetes", diabetes);
        model.addAttribute("hypertension", hypertension);
    }

    //Start processing the summaries here
    private Map<String, String> diabetic(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastDiabeticObs = ConfigCalculations.lastObs(Dictionary.getConcept("2d0d45ca-a92f-4fb2-a6af-c53a1c079bf3"), Arrays.asList(patientId), context);
        Obs obs = ConfigEmrCalculationUtils.obsResultForPatient(lastDiabeticObs, patientId);
        if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("78144858-1452-4b31-af12-fdfd303fc77a"))){
            results.put("Diabetic", "Yes");
        }
        else if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("2e385fe5-2d51-4d86-862e-a7752470c508"))){
            results.put("Diabetic", "Yes");
        }
        else {
            results.put("Diabetic", "No");
        }
        return  results;
    }


    private Map<String, String> hypertension(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastHypertensionObs = ConfigCalculations.lastObs(Dictionary.getConcept("02c2e1f7-7f0c-4bd1-b89e-1a1b37855a6a"), Arrays.asList(patientId), context);
        Obs obs = ConfigEmrCalculationUtils.obsResultForPatient(lastHypertensionObs, patientId);
        if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("d88104a5-9126-4b5a-9380-e31a8e7e9442"))){
            results.put("Hypertension", "Yes");
        }
        else if(obs != null && obs.getValueCoded().equals(Dictionary.getConcept("9b3d4986-ab44-41eb-afc8-504ab5bfe6e2"))){
            results.put("Hypertension", "Yes");
        }
        else {
            results.put("Hypertension", "No");
        }
        return  results;
    }

    private Map<String, String> rbs(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastRbsObs = ConfigCalculations.lastObs(Dictionary.getConcept("887AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double value = ConfigEmrCalculationUtils.numericObsResultForPatient(lastRbsObs, patientId);
        if(value != null){
            results.put("RBS", String.valueOf(value));
        }
        else {
            results.put("RBS", "None");
        }
        return  results;
    }

    private Map<String, String> fbs(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastFbsObs = ConfigCalculations.lastObs(Dictionary.getConcept("160912AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double value = ConfigEmrCalculationUtils.numericObsResultForPatient(lastFbsObs, patientId);
        if(value != null){
            results.put("FBS", String.valueOf(value));
        }
        else {
            results.put("FBS", "None");
        }
        return  results;
    }

    private Map<String, String> uecs(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastFbsObs = ConfigCalculations.lastObs(Dictionary.getConcept("0b7706e7-bcbe-48dc-9651-7d7a7688215f"), Arrays.asList(patientId), context);
        Obs value = ConfigEmrCalculationUtils.obsResultForPatient(lastFbsObs, patientId);
        if(value != null){
            results.put("UECs", value.getValueText());
        }
        else {
            results.put("UECs", "None");
        }
        return  results;
    }

}
