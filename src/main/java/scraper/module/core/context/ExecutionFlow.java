package scraper.module.core.context;

import scraper.util.Utils;

import java.util.function.Consumer;

/**
 * Class representing flow of the execution.
 * <p>
 * Allows to: <ul> <li>request stop of the execution <li>check if execution is in progress <li>access {@link Status} of the execution </ul>
 * <p>
 * Can be observed for every execution flow change.
 */
public class ExecutionFlow {

    private volatile boolean stopped;

    private boolean running;

    private final Status status;

    private Consumer<ExecutionFlow> observator;

    public ExecutionFlow() {
        this.status = new Status();
    }

    public ExecutionFlow(ExecutionFlow executionFlow) {
        this.stopped = executionFlow.stopped;
        this.running = executionFlow.running;
        this.status = new Status(executionFlow.status);
    }

    /**
     * Returns if execution was requested to stop.
     *
     * @return <tt>true</tt> if execution was requested to stop, <tt>false</tt> otherwise.
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Requests stop of the execution.
     * <p>
     * Can be called from outside of execution scope.
     * <p>
     * Observer will be notified about this request.
     */
    public void stop() {
        this.stopped = true;
        informObservator();
    }

    /**
     * Returns if execution is in progress.
     *
     * @return <tt>true</tt> if execution is in progress, <tt>false</tt> otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets running flag.
     * <p>
     * Should be called from within execution scope.
     * <p>
     * Observer will be notified about this change.
     *
     * @param running flag value to set.
     */
    public void setRunning(boolean running) {
        this.running = running;
        informObservator();
    }

    /**
     * Returns {@link Status} used by execution flow.
     * <p>
     * This status should be modified only from within execution flow.
     *
     * @return status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets this execution flow observator.
     * <p>
     * Only one observator is allowed, so it will override previous one.
     * <p>
     * Will also override this {@link ExecutionFlow#status} observator.
     *
     * @param observator observator to set. May be null
     */
    // TODO rename observator to observer
    public void setObservator(Consumer<ExecutionFlow> observator) {
        this.observator = observator;
        status.setObservator(observator == null ? null : st -> observator.accept(this));
    }

    private void informObservator() {
        if (observator != null) {
            try {
                observator.accept(this);
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecutionFlow other = (ExecutionFlow) o;

        return Utils.computeEq(stopped, other.stopped, running, other.running, status, other.status);
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(stopped, running, status);
    }
}
