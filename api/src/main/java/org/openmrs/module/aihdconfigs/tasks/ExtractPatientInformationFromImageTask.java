package org.openmrs.module.aihdconfigs.tasks;

import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractPatientInformationFromImageTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(ExtractPatientInformationFromImageTask.class);

    @Override
    public void execute() {
        if (!isExecuting) {
            if (log.isDebugEnabled()) {
                log.debug("Starting Extract patient information from image task...");
            }

            startExecuting();
            try {
                //do all the work here
            }
            catch (Exception e) {
                log.error("Error while Extracting patient information from image task:", e);
            }
            finally {
                stopExecuting();
            }
        }

    }
}
