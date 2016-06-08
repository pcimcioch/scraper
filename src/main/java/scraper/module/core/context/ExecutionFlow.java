package scraper.module.core.context;

import scraper.util.Utils;

import java.util.function.Consumer;

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

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        this.stopped = true;
        informObservator();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        informObservator();
    }

    public Status getStatus() {
        return status;
    }

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
