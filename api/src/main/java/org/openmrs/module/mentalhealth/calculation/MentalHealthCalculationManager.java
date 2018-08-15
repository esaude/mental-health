/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.mentalhealth.calculation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.calculation.Calculation;
import org.openmrs.calculation.CalculationProvider;
import org.openmrs.calculation.InvalidCalculationException;
import org.openmrs.calculation.patient.PatientCalculation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MentalHealthCalculationManager implements CalculationProvider {

    protected static final Log log = LogFactory.getLog(MentalHealthCalculationManager.class);


    private List<Class<? extends PatientFlagCalculation>> flagCalculationClasses = new ArrayList<Class<? extends PatientFlagCalculation>>();
    private Map<String, Class<? extends PatientCalculation>> calculationClasses = new HashMap<String, Class<? extends PatientCalculation>>();


    /**
     * @see org.openmrs.calculation.CalculationProvider#getCalculation(java.lang.String, java.lang.String)
     */
    @Override
    public Calculation getCalculation(String calculationName, String configuration) throws InvalidCalculationException {
        Class<? extends PatientCalculation> clazz = calculationClasses.get(calculationName);
        if (clazz == null)
            throw new InvalidCalculationException("Not Found: " + calculationName + " (valid values are: " + calculationClasses.keySet() + ")");

        return MentalHealthConfigCalculationUtils.instantiateCalculation(clazz, configuration);
    }

    /**
     * Gets new instances of all patient flag calculations in this module
     * @return list of flag calculation instances
     */
    public List<PatientFlagCalculation> getFlagCalculations() {
        List<PatientFlagCalculation> ret = new ArrayList<PatientFlagCalculation>();

        for (Class<? extends PatientFlagCalculation> calculationClass : flagCalculationClasses) {
            ret.add((PatientFlagCalculation) MentalHealthConfigCalculationUtils.instantiateCalculation(calculationClass, null));
        }

        return ret;
    }
    /**
     * Gets all of the registered calculation classes
     * @return the calculation classes
     */
    public Collection<Class<? extends PatientCalculation>> getAllCalculationClasses() {
        return calculationClasses.values();
    }

}
