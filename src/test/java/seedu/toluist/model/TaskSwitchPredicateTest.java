package seedu.toluist.model;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

/**
 * Tests for TaskSwitchPredicate
 */
public class TaskSwitchPredicateTest {
    @Test
    public void todaySwitchPredicate() {
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now(), TaskSwitchPredicate.TODAY_SWITCH_PREDICATE, true);
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now().minusDays(1), TaskSwitchPredicate
                        .TODAY_SWITCH_PREDICATE,
                false);
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now().plusDays(1), TaskSwitchPredicate
                        .TODAY_SWITCH_PREDICATE,
                false);
    }

    @Test
    public void within7DaysSwitchPredicate() {
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now(), TaskSwitchPredicate.WITHIN_7_DAYS_SWITCH_PREDICATE, true);
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now().plusDays(6), TaskSwitchPredicate
                .WITHIN_7_DAYS_SWITCH_PREDICATE, true);
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now().minusDays(1), TaskSwitchPredicate
                        .TODAY_SWITCH_PREDICATE,
                false);
        assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime.now().plusDays(7), TaskSwitchPredicate
                        .TODAY_SWITCH_PREDICATE,
                false);
    }


    /**
     * Helper method to check that a predicate works correctly
     * @param deadline a deadline to check
     * @param predicate a TaskSwitchPredicate instance
     * @param isSatisfied should the predicate be satisfied
     */
    private void assertTaskWithDeadlineSatisfiesPredicate(LocalDateTime deadline, TaskSwitchPredicate predicate,
                                                          boolean isSatisfied) {
        Task task = new Task("task", deadline);
        assertEquals(predicate.getPredicate().test(task), isSatisfied);
    }
}
