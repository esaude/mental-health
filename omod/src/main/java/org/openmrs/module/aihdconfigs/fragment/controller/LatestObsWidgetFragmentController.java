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

public class LatestObsWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient) {
        Map<String, String> summary = new HashMap<String, String>();
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        summary.putAll(height(patient.getId(), context));
        summary.putAll(weight(patient.getId(), context));

        model.addAttribute("obs", summary);
        model.addAttribute("greenBmi", greenBmi(patient.getId(), context));
        model.addAttribute("yellowBmi", Math.round(yellowBmi(patient.getId(), context) * 100.0) / 100.0);
        model.addAttribute("redBmi", Math.round(redBmi(patient.getId(), context)* 100.0) / 100.0);
        model.addAttribute("circRatio", Math.round(hipWaistRatio(patient.getId(), context) * 100.0) / 100.0 );
    }

    private Map<String, String> height(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastHeightObs = ConfigCalculations.lastObs(Dictionary.getConcept("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double value = ConfigEmrCalculationUtils.numericObsResultForPatient(lastHeightObs, patientId);
        if(value != null){
            results.put("Height", String.valueOf(value));
        }
        else {
            results.put("Height", "None");
        }
        return  results;
    }

    private Map<String, String> weight(Integer patientId, PatientCalculationContext context){
        Map<String, String> results = new HashMap<String, String>();
        CalculationResultMap lastWeightObs = ConfigCalculations.lastObs(Dictionary.getConcept("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double value = ConfigEmrCalculationUtils.numericObsResultForPatient(lastWeightObs, patientId);
        if(value != null){
            results.put("Weight", String.valueOf(value));
        }
        else {
            results.put("Weight", "None");
        }
        return  results;
    }

    private Double calculateBmi(Integer patientId, PatientCalculationContext context){
        Double bmi = null;
        CalculationResultMap lastHeightObs = ConfigCalculations.lastObs(Dictionary.getConcept("5090AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        CalculationResultMap lastWeightObs = ConfigCalculations.lastObs(Dictionary.getConcept("5089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double heightValue = ConfigEmrCalculationUtils.numericObsResultForPatient(lastHeightObs, patientId);
        Double weightValue = ConfigEmrCalculationUtils.numericObsResultForPatient(lastWeightObs, patientId);
        if(heightValue != null && weightValue != null){
            double convertedHeihgt = heightValue/100;
            double productHeight = convertedHeihgt * convertedHeihgt;
            bmi = weightValue/productHeight;
        }

       return bmi;
    }

    private Double greenBmi(Integer patientId, PatientCalculationContext context){
        Double greenBmi = 0.0;
        Double bmi = calculateBmi(patientId, context);
        if(bmi != null && bmi >= 18.5 && bmi <= 25.0){
            greenBmi = bmi;
        }
        return greenBmi;
    }
    private Double yellowBmi(Integer patientId, PatientCalculationContext context){
        Double yellowBmi = 0.0;
        Double bmi = calculateBmi(patientId, context);
        if(bmi != null && bmi > 25.0 && bmi <= 30.0){
            yellowBmi = bmi;
        }
        return yellowBmi;
    }

    private Double redBmi(Integer patientId, PatientCalculationContext context){
        Double redBmi = 0.0;
        Double bmi = calculateBmi(patientId, context);
        if(bmi != null && bmi > 30.0){
            redBmi = bmi;
        }
        return redBmi;
    }
    private Double hipWaistRatio(Integer patientId, PatientCalculationContext context){
        Double hipWaistRatio = 0.0;
        CalculationResultMap lastWaistCircObs = ConfigCalculations.lastObs(Dictionary.getConcept("163080AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        CalculationResultMap lastHipCircObs = ConfigCalculations.lastObs(Dictionary.getConcept("163081AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"), Arrays.asList(patientId), context);
        Double waistValue = ConfigEmrCalculationUtils.numericObsResultForPatient(lastWaistCircObs, patientId);
        Double hipValue = ConfigEmrCalculationUtils.numericObsResultForPatient(lastHipCircObs, patientId);
        if(waistValue != null && hipValue != null){
            hipWaistRatio = hipValue/waistValue;
        }

        return hipWaistRatio;
    }
}
