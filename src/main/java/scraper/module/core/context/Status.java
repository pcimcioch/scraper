package scraper.module.core.context;

import scraper.util.Utils;

import java.util.function.Consumer;

/**
 * Class representing status of execution.
 * <p>
 * Divides execution to steps. Each step can be divided further to sub steps.
 * <p>
 * Can be observed for each status change.
 */
public class Status {

    private int steps;

    private int currentStep;

    private int subSteps;

    private int currentSubStep;

    //TODO rename observator to observer
    private Consumer<Status> observator;

    public Status() {
    }

    public Status(Status other) {
        this(other.getSteps(), other.getCurrentStep(), other.getSubSteps(), other.getCurrentSubStep());
    }

    public Status(int steps, int currentStep, int subSteps, int currentSubStep) {
        this.steps = steps;
        this.currentStep = currentStep;
        this.subSteps = subSteps;
        this.currentSubStep = currentSubStep;
    }

    /**
     * Returns number of steps of this execution.
     *
     * @return number of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Returns current step of this execution.
     *
     * @return current step of this execution
     */
    public int getCurrentStep() {
        return currentStep;
    }

    /**
     * Returns number of sub steps of current step of this execution.
     *
     * @return number of sub steps
     */
    public int getSubSteps() {
        return subSteps;
    }

    /**
     * Returns current sub step of current step of this execution.
     *
     * @return current sub step
     */
    public int getCurrentSubStep() {
        return currentSubStep;
    }

    /**
     * Sets total number of the steps of this execution.
     * <p>
     * Observer will be notified about this change.
     *
     * @param steps total number of the steps
     */
    public void setSteps(int steps) {
        this.steps = steps;
        informObservator();
    }

    /**
     * Increments current step of the execution.
     * <p>
     * Will also clear this {@link #subSteps} and {@link #currentSubStep}
     * <p>
     * Observer will be notified about this change.
     */
    public void incrementCurrentStep() {
        this.currentStep++;
        this.subSteps = 0;
        this.currentSubStep = 0;
        informObservator();
    }

    /**
     * Sets total number of the sub steps of current step of this execution.
     * <p>
     * Observer will be notified about this change.
     *
     * @param subSteps total number of the sub steps
     */
    public void setSubSteps(int subSteps) {
        this.subSteps = subSteps;
        informObservator();
    }

    /**
     * Increments current sub step of current step of the execution.
     * <p>
     * Observer will be notified about this change.
     */
    public void incrementCurrentSubStep() {
        this.currentSubStep++;
        informObservator();
    }

    /**
     * Sets observer of this status.
     * <p>
     * Observer will be notified about all status changes.
     *
     * @param observator observer to set. May be null.
     */
    public void setObservator(Consumer<Status> observator) {
        this.observator = observator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Status other = (Status) o;

        return Utils.computeEq(steps, other.steps, subSteps, other.subSteps, currentStep, other.currentStep, currentSubStep, other.currentSubStep);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(steps, subSteps, currentSubStep, currentStep);
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
}
