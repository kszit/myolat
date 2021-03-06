package org.olat.lms.course.statistic;

import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.olat.data.basesecurity.Identity;
import org.olat.lms.commons.taskexecutor.TaskExecutorService;
import org.olat.system.commons.manager.BasicManager;
import org.olat.system.logging.log4j.LoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles asynchronous aspects of log export - including limiting the number of concurrent exports
 * <P>
 * Initial Date: 13.01.2010 <br>
 * 
 * @author Stefan
 */
public class AsyncExportManager extends BasicManager {

    /** the logging object used in this class **/
    private static final Logger log = LoggerHelper.getLogger();

    /** AsyncExportManager is a singleton, configured by spring **/
    private static AsyncExportManager INSTANCE;

    /** set via spring **/
    private int concurrentExportsPerNode_ = 2;

    /** the identities currently executing an export **/
    private final Set<Identity> identitiesOfJobsCurrentlyRunning_ = new HashSet<Identity>();

    /** DEBUG ONLY: how many runnables are queued up - are we overloading the ThreadPoolTaskExecutor ? **/
    private int waitingCnt_ = 0;

    @Autowired
    private TaskExecutorService taskExecutorService;

    /** created via spring **/
    private AsyncExportManager() {
        INSTANCE = this;
    }

    /**
     * @return Singleton.
     */
    public static final AsyncExportManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("AsyncExportManager bean not created via spring. Configuration error!");
        }
        return INSTANCE;
    }

    /** called via spring, sets the number of concurrent exports within one node, default is 2 **/
    public void setConcurrentExportsPerNode(final int concurrentExportsPerNode) {
        this.concurrentExportsPerNode_ = concurrentExportsPerNode;
    }

    public void asyncArchiveCourseLogFiles(final Identity identity, final Runnable callback, final Long oresID, final String exportDir, final Date begin, final Date end,
            final boolean adminLog, final boolean userLog, final boolean statisticLog, final String charset, final Locale locale, final String email) {
        // argument checks
        if (identity == null) {
            throw new IllegalArgumentException("identity must not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }

        // DEBUG ONLY
        synchronized (this) {
            log.info("asyncArchiveCourseLogFiles: user " + identity.getName() + " wants to archive a course log. Already pending jobs: " + waitingCnt_);
        }

        taskExecutorService.runTask(new Runnable() {

            @Override
            public void run() {
                try {
                    log.info("asyncArchiveCourseLogFiles: user " + identity.getName() + " aquires lock for archiving course log");
                    waitForSlot(identity);
                    log.info("asyncArchiveCourseLogFiles: user " + identity.getName() + " starts archiving...");
                    ExportManager.getInstance().archiveCourseLogFiles(oresID, exportDir, begin, end, adminLog, userLog, statisticLog, charset, locale, email);
                    log.info("asyncArchiveCourseLogFiles: user " + identity.getName() + " finished archiving...");
                } finally {
                    returnSlot(identity);
                    log.info("asyncArchiveCourseLogFiles: user " + identity.getName() + " releases lock for archiving course log");
                    callback.run();
                }
            }

        });
    }

    public synchronized boolean asyncArchiveCourseLogOngoingFor(final Identity identity) {
        return identitiesOfJobsCurrentlyRunning_.contains(identity);
    }

    /** internal counter method **/
    private synchronized void waitForSlot(final Identity identity) {
        waitingCnt_++;
        while (identitiesOfJobsCurrentlyRunning_.size() > concurrentExportsPerNode_ || identitiesOfJobsCurrentlyRunning_.contains(identity)) {
            try {
                log.info("waitForSlot: user " + identity.getName() + " wants to archive a course log, but the queue is full. Running count: "
                        + identitiesOfJobsCurrentlyRunning_.size() + ". Total pending jobs: " + waitingCnt_);
                wait();
            } catch (final InterruptedException ie) {
                // this empty catch is ok
            }
        }
        waitingCnt_--;
        identitiesOfJobsCurrentlyRunning_.add(identity);
    }

    /** internal counter method **/
    private synchronized void returnSlot(final Identity identity) {
        identitiesOfJobsCurrentlyRunning_.remove(identity);
        log.info("returnSlot: user " + identity.getName() + " returns a slot. Running count: " + identitiesOfJobsCurrentlyRunning_.size() + ", Total pending jobs: "
                + waitingCnt_);
        notifyAll();
    }
}
