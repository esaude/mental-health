package org.openmrs.module.mentalhealth.tasks;

import org.openmrs.scheduler.tasks.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavePatientAttachmentToDbTask extends AbstractTask {

    private static final Logger log = LoggerFactory.getLogger(SavePatientAttachmentToDbTask.class);

    @Override
    public void execute() {
        if (!isExecuting) {
            if (log.isDebugEnabled()) {
                log.debug("Starting Save patient attachment to db...");
            }

            startExecuting();
            try {
                //do all the work here
            }
            catch (Exception e) {
                log.error("Error while Saving patient attachment to db:", e);
            }
            finally {
                stopExecuting();
            }
        }
    }
}
