package org.jenkinsci.plugins.cucumber.jsontestsupport;

import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ScenarioToHTMLTest {

    private static final String PASSED_STEP_CSS = "background-color: #e6ffcc;";
    private static final String FAILED_STEP_CSS = "background-color: #ffeeee;";
    private static final String UNDEFINED_STEP_CSS = "background-color: #ffeeee;";
    private static final String SKIPPED_STEP_CSS = "background-color: #ffffcc;";

    private static final float DURATION = 1.5f;
    private static final float INVALID_DURATION = 2f;
    private static final String EMPTY_BEFORE_OR_AFTER = "";

    private static final String EXPECTED_ADD_DURATION_HTML_FOR_UNDEFINED_STEP =
            "<td nowrap=\"nowrap\" valign=\"top\" align=\"left\" style=\"background-color: #ffeeee;\">" +
            "<div style=\"padding-left: 2em;background-color: #ffeeee;\">0.000";
    private static final String EXPECTED_ADD_DURATION_HTML_FOR_FAILED_STEP =
            "<td nowrap=\"nowrap\" valign=\"top\" align=\"left\" style=\"background-color: #ffeeee;\">" +
            "<div style=\"padding-left: 2em;background-color: #ffeeee;\">0.000";
    private static final String EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP =
            "<td nowrap=\"nowrap\" valign=\"top\" align=\"left\" style=\"background-color: #e6ffcc;\">" +
                    "<div style=\"padding-left: 2em;background-color: #e6ffcc;\">1.500";

    private static final String ERROR_MESSAGE = "";
    private static final String UNDEFINED_RESULT_STATUS = "undefined";
    private static final String TABLE_TAG = "<table>";

    private StringBuilder html;
    private ScenarioToHTML scenarioToHTML;
    @Mock
    private ScenarioResult scenarioResult;
    @Mock
    private BeforeAfterResult beforeAfterResult;
    @Mock
    private Match match;
    @Mock
    private Result result;
    @Mock
    private Step step;


    @Before
    public void setup() {
        html = new StringBuilder(TABLE_TAG);

        scenarioToHTML = spy(new ScenarioToHTML(scenarioResult));

        when(beforeAfterResult.getMatch()).thenReturn(match);
        when(beforeAfterResult.getResult()).thenReturn(result);
        when(beforeAfterResult.getDuration()).thenReturn(DURATION);
        when(step.getComments()).thenReturn(null);
        when(result.getErrorMessage()).thenReturn(ERROR_MESSAGE);
    }

    @Test
    public void shouldAddDurationHtmlFragment() {
        scenarioToHTML.addDuration(html, PASSED_STEP_CSS, DURATION);

        assertTrue(html.toString().contains(EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP));
    }

    @Test
    public void shouldNotContainAddDurationHtmlFragmentForInvalidDuration() {
        scenarioToHTML.addDuration(html, PASSED_STEP_CSS, INVALID_DURATION);

        assertFalse(html.toString().contains(EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP));
    }

    @Test
    public void shouldContainAddDurationInAddBeforeAfterResult() {
        Mockito.doReturn(PASSED_STEP_CSS).when(scenarioToHTML).getCssFormatting(result);

        String actual = scenarioToHTML.addBeforeAfterResult(html, EMPTY_BEFORE_OR_AFTER, beforeAfterResult).toString();

        assertTrue(actual.contains(EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP));
    }

    @Test
    public void shouldNotContainAddDurationInAddBeforeAfterResultForInvalidStep() {
        Mockito.doReturn(SKIPPED_STEP_CSS).when(scenarioToHTML).getCssFormatting(result);

        String actual = scenarioToHTML.addBeforeAfterResult(html, EMPTY_BEFORE_OR_AFTER, beforeAfterResult).toString();

        assertFalse(actual.contains(EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP));
    }

    @Test
    public void shouldContainAddDurationInAddFailureForFailedStep() {
        Mockito.doReturn(FAILED_STEP_CSS).when(scenarioToHTML).getCssFormatting(result);
        when(result.getStatus()).thenReturn(Result.FAILED);

        String actual = scenarioToHTML.addFailure(html, result).toString();

        assertTrue(actual.contains(EXPECTED_ADD_DURATION_HTML_FOR_FAILED_STEP));
    }

    @Test
    public void shouldAddDurationInAddFailureForUndefinedStep() {
        Mockito.doReturn(UNDEFINED_STEP_CSS).when(scenarioToHTML).getCssFormatting(result);
        when(result.getStatus()).thenReturn(UNDEFINED_RESULT_STATUS);

        String actual = scenarioToHTML.addFailure(html, result).toString();

        assertTrue(actual.contains(EXPECTED_ADD_DURATION_HTML_FOR_UNDEFINED_STEP));
    }

    @Test
    public void shouldNotContainAddDurationInAddFailureForInvalidStep() {
        Mockito.doReturn(UNDEFINED_STEP_CSS).when(scenarioToHTML).getCssFormatting(result);
        when(result.getStatus()).thenReturn(UNDEFINED_RESULT_STATUS);

        String actual = scenarioToHTML.addFailure(html, result).toString();

        assertFalse(actual.contains(EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP));
    }

}