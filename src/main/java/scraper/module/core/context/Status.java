package scraper.module.core.context;

import scraper.util.Utils;

import java.util.function.Consumer;

public class Status {

    private int steps;

    private int currentStep;

    private int subSteps;

    private int currentSubStep;

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

    public int getSteps() {
        return steps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getSubSteps() {
        return subSteps;
    }

    public int getCurrentSubStep() {
        return currentSubStep;
    }

    public void setSteps(int steps) {
        this.steps = steps;
        informObservator();
    }

    public void incrementCurrentStep() {
        this.currentStep++;
        this.subSteps = 0;
        this.currentSubStep = 0;
        informObservator();
    }

    public void setSubSteps(int subSteps) {
        this.subSteps = subSteps;
        informObservator();
    }

    public void incrementCurrentSubStep() {
        this.currentSubStep++;
        informObservator();
    }

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
