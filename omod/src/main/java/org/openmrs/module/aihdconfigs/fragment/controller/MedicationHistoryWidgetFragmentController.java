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
        List<String> groupA_ARB = new ArrayList<String>();
        List<String> groupB = new ArrayList<String>();
        List<String> groupC = new ArrayList<String>();
        List<String> groupD = new ArrayList<String>();
        List<String> groupZ = new ArrayList<String>();
        List<String> groupOglas = new ArrayList<String>();
        List<String> insulin = new ArrayList<String>();

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
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_ace))) {
                        groupA_ACEInhibitor.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Candesartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Irbesartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Losartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Telmisartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Valsartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Olmesartan))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_arb))) {
                        groupA_ARB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Atenolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Labetolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Propranolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Carvedilol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Nebivolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Metoprolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Bisoprolol))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_b))) {
                        groupB.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Amlodipine))) {
                        groupC.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Felodipine))) {
                        groupC.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Nifedipine))) {
                        groupC.add(obs.getValueCoded().getName().getName());
                    }else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Chlorthalidone))) {
                        groupD.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.HydrochlorothiazideHCTZ))) {
                        groupD.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Indapamide))) {
                        groupD.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_d1))) {
                        groupD.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_d2))) {
                        groupD.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Methyldopa))) {
                        groupZ.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Hydralazine))) {
                        groupZ.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Prazocin))) {
                        groupZ.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_z))) {
                        groupZ.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Prazocin))) {
                        groupOglas.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other))) {
                        groupOglas.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Metformin))) {
                        groupOglas.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.Glibenclamide))) {
                        groupOglas.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.insulin_70_30))) {
                        insulin.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.other_insulin))) {
                        insulin.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.soluble_insulin))) {
                        insulin.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NPH_Type_1))) {
                        insulin.add(obs.getValueCoded().getName().getName());
                    }
                    else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NPH_Type_2))) {
                        insulin.add(obs.getValueCoded().getName().getName());
                    }

                }
            }
            medicationCategories.put("GA ACE Inhibitor", groupA_ACEInhibitor);
            medicationCategories.put("GA ARB", groupA_ARB);
            medicationCategories.put("Group B", groupB);
            medicationCategories.put("Group C", groupC);
            medicationCategories.put("Group D", groupD);
            medicationCategories.put("Group Z", groupZ);
            medicationCategories.put("Oglas", groupOglas);
            medicationCategories.put("Insulin", insulin);
        }
        return medicationCategories;
    }
}
