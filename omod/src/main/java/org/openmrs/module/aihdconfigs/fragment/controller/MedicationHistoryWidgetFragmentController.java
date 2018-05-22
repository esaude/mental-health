package org.openmrs.module.aihdconfigs.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.aihdconfigs.ConfigCoreUtils;
import org.openmrs.module.aihdconfigs.Dictionary;
import org.openmrs.module.aihdconfigs.calculation.ConfigCalculationManager;
import org.openmrs.module.aihdconfigs.calculation.ConfigCalculations;
import org.openmrs.module.aihdconfigs.calculation.ConfigEmrCalculationUtils;
import org.openmrs.module.aihdconfigs.metadata.EncounterTypes;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MedicationHistoryWidgetFragmentController {

    protected final Log log = LogFactory.getLog(getClass());

    public void controller(FragmentModel model, @FragmentParam("patient") PatientDomainWrapper patient,
                           @SpringBean ConfigCalculationManager calculationManager){

        Map<String, String> medication = new HashMap<String, String>();
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());

        medication.put("test drug", "test formulation");

        model.addAttribute("medication", medication);
        model.addAttribute("date", medicationEncounter(patient.getId(), context));

    }

    private String medicationEncounter(Integer patientId, PatientCalculationContext context){
        String message = "";
        EncounterType lastInitial = Context.getEncounterService().getEncounterTypeByUuid(EncounterTypes.DM_HTN_INITIAL_ENCOUNTER_TYPE.uuid());
        EncounterType lastIFolloUp = Context.getEncounterService().getEncounterTypeByUuid(EncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE.uuid());
        CalculationResultMap lastEncounterInitial = ConfigCalculations.lastEncounter(lastInitial, Arrays.asList(patientId), context );
        CalculationResultMap lastEncounterFollowUp = ConfigCalculations.lastEncounter(lastIFolloUp, Arrays.asList(patientId), context );

        Encounter initialEncounter = ConfigEmrCalculationUtils.encounterResultForPatient(lastEncounterInitial, patientId);
        Encounter followUpEncounter = ConfigEmrCalculationUtils.encounterResultForPatient(lastEncounterFollowUp, patientId);

        if (initialEncounter != null && followUpEncounter != null) {
            //get the latest encounter
            Date latestEncounterDate = ConfigCoreUtils.latest(initialEncounter.getEncounterDatetime(), followUpEncounter.getEncounterDatetime());
            message = latestEncounterDate.toString();
        }
        else if(initialEncounter != null){
            Set<Obs> initialObs = initialEncounter.getAllObs();
            if(initialObs != null) {
                message = initialEncounter.getEncounterDatetime().toString();
            }
        }
        else if(followUpEncounter != null){
            Set<Obs> followUpObs = followUpEncounter.getAllObs();
            if(followUpObs != null) {
                message = followUpEncounter.getEncounterDatetime().toString();
            }
        }

        return message;
    }
}
