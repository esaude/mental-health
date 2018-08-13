package org.openmrs.module.mentalhealth.advice;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.mentalhealth.metadata.PersonAttributeTypes;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatientSearchAdviser extends StaticMethodMatcherPointcutAdvisor implements Advisor {

    private static final Log log = LogFactory.getLog(PatientSearchAdviser.class);

    @Override
    public boolean matches(Method method, Class targetClass) {
        if (method.getName().equals("getPatients")) {
            return true;
        }
        else if (method.getName().equals("getPatient")) {
            return true;
        }
        else if (method.getName().equals("getPatientByUuid")) {
            return true;
        }
        return false;
    }

    @Override
    public Advice getAdvice() {
        return new PatientSearchAdvise();
    }

    private class PatientSearchAdvise implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (Context.getAuthenticatedUser() == null) {
                return null;
            }
            Object object = invocation.proceed();
            if (Daemon.isDaemonUser(Context.getAuthenticatedUser()) || Context.getAuthenticatedUser().isSuperUser()) {
                return object;
            }

            Integer sessionLocationId = Context.getUserContext().getLocationId();
            String locationAttributeUuid = PersonAttributeTypes.PATIENT_LOCATION.uuid();
            if (StringUtils.isNotBlank(locationAttributeUuid)) {
                final PersonAttributeType personAttributeType = Context.getPersonService().getPersonAttributeTypeByUuid(locationAttributeUuid);
                if (sessionLocationId != null) {
                    String sessionLocationUuid = Context.getLocationService().getLocation(sessionLocationId).getUuid();
                    if(object instanceof List) {
                        List<Patient> patientList = (List<Patient>) object;
                        for (Iterator<Patient> iterator = patientList.iterator(); iterator.hasNext(); ) {
                            if(!doesPatientBelongToGivenLocation(iterator.next(), personAttributeType, sessionLocationUuid)) {
                                iterator.remove();
                            }
                        }
                        object = patientList;
                    }
                    else if(object instanceof Patient) {
                        if(!doesPatientBelongToGivenLocation((Patient)object, personAttributeType, sessionLocationUuid)) {
                            object = null;
                        }
                    }
                } else {
                    log.debug("Search Patient : Null Session Location in the UserContext");
                    if(object instanceof Patient) {
                        // If the sessionLocationId is null, then return null for a Patient instance
                        return null;
                    }
                    else {
                        // If the sessionLocationId is null, then return a empty list
                        return new ArrayList<Patient>();
                    }
                }
            }
            return object;
        }

        private Boolean doesPatientBelongToGivenLocation(Patient patient, PersonAttributeType personAttributeType, String sessionLocationUuid) {
            PersonAttribute personAttribute = patient.getAttribute(personAttributeType);
            return (personAttribute != null && compare(personAttribute.getValue(), sessionLocationUuid));
        }

        private Boolean compare(String value1, String value2) {
            boolean isSame = false;
            Location loc1 = getMyLocation(value1);
            Location loc2 = Context.getLocationService().getLocationByUuid(value2);
            if(loc1 != null && loc2 != null && loc1.equals(loc2)){
                isSame = true;
            }
            return  isSame;
        }

        private String correctValue(String str){
            return str.replace("_", " ");
        }
        private Location getMyLocation(String name){
            return Context.getLocationService().getLocation(correctValue(name));
        }
    }
}