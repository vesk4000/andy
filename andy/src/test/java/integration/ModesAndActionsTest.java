package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static integration.BaseMetaTestsTest.failedMetaTest;
import static integration.CodeChecksTest.codeCheck;
import static integration.CodeChecksTest.penaltyCodeCheck;
import static org.assertj.core.api.Assertions.assertThat;

public class ModesAndActionsTest extends IntegrationTestBase {

    @Test
    void practiceModeRunsEverything() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(200);
        assertThat(result.getPenaltyCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(200);
        assertThat(result)
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(91);
    }

    @Test
    void noPenaltyCodeChecksDefined() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksWithoutPenaltyCodeChecks");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().hasChecks()).isFalse();
        assertThat(result.getPenaltyCodeChecks().wasExecuted()).isTrue();
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(91);
    }

    @Test
    void failingPenaltyCodeChecks() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksPenaltyCodeChecksFailing");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().hasChecks()).isTrue();
        assertThat(result.getPenaltyCodeChecks().wasExecuted()).isTrue();
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(10);
        assertThat(result.getPenaltyCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(10 + 5 + 3);
        assertThat(result)
                .has(penaltyCodeCheck("Trip Repository should not be mocked penalty", false, 5))
                .has(penaltyCodeCheck("Trip should be mocked penalty", false, 3))
                .has(penaltyCodeCheck("getTripById should be set up penalty", true, 10));
        assertThat(result.getFinalGrade()).isEqualTo(91 - (5 + 3));
    }

    @Test
    void failingPenaltyCodeChecksOverrideGradeTo0() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksPenaltyCodeChecksFailing2");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().hasChecks()).isTrue();
        assertThat(result.getPenaltyCodeChecks().wasExecuted()).isTrue();
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().getUnweightedNumberOfPassedChecks()).isEqualTo(1);
        assertThat(result.getPenaltyCodeChecks().getTotalUnweightedNumberOfChecks()).isEqualTo(3);
        assertThat(result)
                .has(penaltyCodeCheck("Trip Repository should not be mocked penalty", false, 100))
                .has(penaltyCodeCheck("Trip should be mocked penalty", false, 30))
                .has(penaltyCodeCheck("getTripById should be set up penalty", true, 10));
        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @Test
    void runOnlyTests() {
        Result result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result.getTests().wasExecuted()).isTrue();
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);

        assertThat(result.getCoverage().wasExecuted()).isTrue(); // Since 2023, tests also run coverage
        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result.getMutationTesting().wasExecuted()).isFalse();
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
        assertThat(result.getPenaltyCodeChecks().wasExecuted()).isFalse();

        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @Test
    void runOnlyTestsAndCoverageToolsDuringExam() {
        Result result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result.getTests().wasExecuted()).isTrue();
        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);

        assertThat(result.getCoverage().wasExecuted()).isTrue();
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);

        assertThat(result.getMutationTesting().wasExecuted()).isTrue();
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);

        assertThat(result.getMetaTests().wasExecuted()).isFalse();
        assertThat(result.getCodeChecks().wasExecuted()).isFalse();
        assertThat(result.getPenaltyCodeChecks().wasExecuted()).isFalse();

        assertThat(result.getFinalGrade()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"TESTS", "COVERAGE", "FULL_WITHOUT_HINTS"})
    void gradingModeShouldRunEverything(Action action) {
        Result result = run(action, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksGrading");

        assertThat(result.getTests().getTestsSucceeded()).isEqualTo(2);
        assertThat(result.getCoverage().getCoveredLines()).isEqualTo(11);
        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(8);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(9);
        assertThat(result.getCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(3);
        assertThat(result.getCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(3);
        assertThat(result.getPenaltyCodeChecks().getWeightedNumberOfPassedChecks()).isEqualTo(100);
        assertThat(result.getPenaltyCodeChecks().getTotalWeightedNumberOfChecks()).isEqualTo(105);
        assertThat(result)
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1))
                .has(penaltyCodeCheck("Trip Repository should be mocked required", true, 100))
                .has(penaltyCodeCheck("getTripById should not be set up penalty", false, 5));
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getMetaTests()).has(failedMetaTest("DoesNotCheckInvalidTripId"));
        assertThat(result.getFinalGrade()).isEqualTo(86);
    }

}
