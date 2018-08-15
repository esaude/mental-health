package org.openmrs.module.mentalhealth.activator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mentalhealth.MentalHealthConstants;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;

/**
 * Custom application configurations
 */
public class MentalHealthAppConfigurationInitializer implements MentalHealthInitializer {

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public void started() {
        log.info("Setting administrative configurations for " + MentalHealthConstants.MODULE_ID);

        SchedulerService schedulerService = Context.getSchedulerService();

        try {
            // set the AutoClose visits tasks to start automatically
            TaskDefinition autoCloseVisitsTask = (TaskDefinition) schedulerService.getTaskByName("Auto Close Visits Task");
            autoCloseVisitsTask.setStartOnStartup(true);
            schedulerService.saveTaskDefinition(autoCloseVisitsTask);
        }
        catch (Exception e) {
            log.error("Failed to setup scheduled tasks ", e);
        }

    }

    @Override
    public void stopped() {

    }
}
