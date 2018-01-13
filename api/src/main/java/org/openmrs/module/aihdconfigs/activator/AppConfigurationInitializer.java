package org.openmrs.module.aihdconfigs.activator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.aihdconfigs.AihdConstants;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;

/**
 * Custom application configurations
 */
public class AppConfigurationInitializer implements Initializer {

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public void started() {
        log.info("Setting administrative configurations for " + AihdConstants.MODULE_ID);

        SchedulerService schedulerService = Context.getSchedulerService();

        try {
            // set the AutoClose visits tasks to start automatically
            TaskDefinition autoCloseVisitsTask = (TaskDefinition) schedulerService.getTaskByName("Auto Close Visits Task");
            autoCloseVisitsTask.setStartOnStartup(true);
            schedulerService.saveTaskDefinition(autoCloseVisitsTask);

            // check the Database Backup Task
            TaskDefinition backupDatabase = (TaskDefinition) schedulerService.getTaskByName("Database Backup Task");
            if (backupDatabase != null) {
                // only execute if the task exists
                backupDatabase.setStartOnStartup(true);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                Calendar taskDefinitionCalendar = new GregorianCalendar(2016, 8, 28, 23, 59, 59);
                Calendar aihdMRCalendar = new GregorianCalendar(2016, 8, 28, 15, 59, 59);

                // change the start date
                if(sdf.format(taskDefinitionCalendar.getTime()).equals(sdf.format(backupDatabase.getStartTime()))) {
                    // set it to the new time for Uganda
                    backupDatabase.setStartTime(aihdMRCalendar.getTime());
                    log.info("AIHD backup time set");
                }
                schedulerService.saveTaskDefinition(backupDatabase);
                log.info("Database Backup Task set to start on Startup");
            }
        }
        catch (Exception e) {
            log.error("Failed to setup scheduled tasks ", e);
        }

    }

    @Override
    public void stopped() {

    }
}
