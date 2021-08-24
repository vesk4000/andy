package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;

import java.util.Collections;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Action.*;
import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Mode.GRADING;

public class ModeActionSelector {

    private Mode mode;
    private Action action;

    public ModeActionSelector(Mode mode, Action action) {
        this.mode = mode;
        this.action = action;
    }

    public Mode getMode() {
        return mode;
    }

    public Action getAction() {
        return action;
    }

    public List<ExecutionStep> getCorrectSteps() {
        switch (mode) {
            case PRACTICE -> {
                return getPracticeMode();
            }
            case EXAM -> {
                return getExamMode();
            }
            case GRADING -> {
                return getGradingMode();
            }
        }
        return Collections.emptyList();
    }

    public boolean shouldShowHints() {
        return mode == GRADING || action == HINTS || action == CUSTOM;
    }

    private List<ExecutionStep> getPracticeMode() {
        if (action == HINTS || action == NO_HINTS) {
            return fullMode();
        } else if (action == COVERAGE) {
            return withCoverage();
        } else {
            return justTests();
        }
    }

    private List<ExecutionStep> getExamMode() {
        if (action == HINTS || action == NO_HINTS || action == COVERAGE) {
            return withCoverage();
        } else {
            return justTests();
        }
    }

    private List<ExecutionStep> getGradingMode() {
        return fullMode();
    }

    public static List<ExecutionStep> justTests() {
        return List.of(new RunJUnitTestsStep());
    }

    public static List<ExecutionStep> withCoverage() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep()
        );
    }

    public static List<ExecutionStep> fullMode() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep(),
                new RunCodeChecksStep(),
                new RunMetaTestsStep(),
                new CalculateFinalGradeStep()
        );
    }

}