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

        Map<String, List<String>> medication = new HashMap<String, List<String>>();
        PatientCalculationService patientCalculationService = Context.getService(PatientCalculationService.class);
        PatientCalculationContext context = patientCalculationService.createCalculationContext();
        context.setNow(new Date());
        medication.putAll(medications(patient.getId(), context));
        model.addAttribute("medication", medication);
        model.addAttribute("date", ConfigCoreUtils.formatDates(medicationEncounter(patient.getId(), context).getEncounterDatetime()));

    }

    private Encounter medicationEncounter(Integer patientId, PatientCalculationContext context){
        Encounter encounter = new Encounter();
        EncounterType lastInitial = Context.getEncounterService().getEncounterTypeByUuid(EncounterTypes.DM_HTN_INITIAL_ENCOUNTER_TYPE.uuid());
        EncounterType lastIFolloUp = Context.getEncounterService().getEncounterTypeByUuid(EncounterTypes.DIABETIC_CLINICAL_FOLLOW_UP_ENCOUNTER_TYPE.uuid());
        CalculationResultMap lastEncounterInitial = ConfigCalculations.lastEncounter(lastInitial, Arrays.asList(patientId), context );
        CalculationResultMap lastEncounterFollowUp = ConfigCalculations.lastEncounter(lastIFolloUp, Arrays.asList(patientId), context );

        Encounter initialEncounter = ConfigEmrCalculationUtils.encounterResultForPatient(lastEncounterInitial, patientId);
        Encounter followUpEncounter = ConfigEmrCalculationUtils.encounterResultForPatient(lastEncounterFollowUp, patientId);



        if (initialEncounter != null && followUpEncounter != null) {
            encounter = followUpEncounter;
        }
        else if(initialEncounter != null){
            encounter = initialEncounter;
        }
        else if(followUpEncounter != null){
            encounter = followUpEncounter;

        }

        return encounter;
    }

    private Map<String, List<String>> medications(Integer patientId, PatientCalculationContext context){
        Map<String, List<String>> medicationCategories = new HashMap<String, List<String>>();
        List<String> groupA_ACEInhibitor = new ArrayList<String>();
        List<Obs> groupA_ARB = new ArrayList<Obs>();
        List<Obs> groupB = new ArrayList<Obs>();
        List<Obs> groupC = new ArrayList<Obs>();
        List<Obs> groupD = new ArrayList<Obs>();
        List<Obs> groupZ = new ArrayList<Obs>();
        List<Obs> groupOglas = new ArrayList<Obs>();

        Encounter encounter = medicationEncounter(patientId, context);
        if(encounter != null){
            Set<Obs>  obsSet = encounter.getAllObs();
            for(Obs obs: obsSet){
                if(obs.getConcept().equals(Dictionary.getConcept(Dictionary.MEDICATION_ORDER))){
                    //start looking at the answers and the obsgroup comaprison to list the drugs

                    if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Captopril))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Enalapril))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Lisinopril))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Perindopril))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Ramipril))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                }
            }
            medicationCategories.put("GA ACE Inhibitor", groupA_ACEInhibitor);
            //medicationCategories.put("groupA_ARB", groupA_ARB);
            //medicationCategories.put("groupB", groupB);
            //medicationCategories.put("groupC", groupC);
            //medicationCategories.put("groupD", groupD);
            //medicationCategories.put("groupZ", groupZ);
            //medicationCategories.put("groupOglas", groupOglas);
        }
        return medicationCategories;
    }
}
