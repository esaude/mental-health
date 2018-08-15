package org.openmrs.module.mentalhealth.flags;

import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.mentalhealth.MentalHealthDictionary;
import org.openmrs.module.mentalhealth.calculation.AbstractPatientCalculation;
import org.openmrs.module.mentalhealth.calculation.BooleanResult;
import org.openmrs.module.mentalhealth.calculation.MentalHealthConfigCalculations;
import org.openmrs.module.mentalhealth.calculation.MentalHealthCalculationUtils;
import org.openmrs.module.mentalhealth.calculation.PatientFlagCalculation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class MissedAppointmentCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {

    @Override
    public String getFlagMessage() {
        return "Missed Appointments";
    }

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {

        CalculationResultMap ret = new CalculationResultMap();
        CalculationResultMap lastReturnDateObss = MentalHealthConfigCalculations.lastObs(MentalHealthDictionary.getConcept(MentalHealthDictionary.RETURN_VISIT_DATE), cohort, context);
        for(Integer ptId: cohort){
            boolean missed = false;
            Date lastScheduledReturnDate = MentalHealthCalculationUtils.datetimeObsResultForPatient(lastReturnDateObss, ptId);

            if (lastScheduledReturnDate != null && MentalHealthCalculationUtils.daysSince(lastScheduledReturnDate, context) > 0) {
                missed = true;
            }
            ret.put(ptId, new BooleanResult(missed, this, context));
        }
        return ret;
    }
}
