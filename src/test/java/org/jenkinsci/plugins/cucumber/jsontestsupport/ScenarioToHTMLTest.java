package org.jenkinsci.plugins.cucumber.jsontestsupport;

import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
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
    private static final String SKIPPED_STEP_CSS = "background-color: #ffffcc;";

    private static final float DURATION = 1.5f;
    private static final float INVALID_DURATION = 2f;
    private static final String EMPTY_BEFORE_OR_AFTER = "";

    private static final String EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP =
            "<td nowrap=\"nowrap\" valign=\"top\" align=\"left\" style=\"background-color: #e6ffcc;\">" +
                    "<div style=\"padding-left: 2em;background-color: #e6ffcc;\">1.500";
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


    @Before
    public void setup() {
        html = new StringBuilder(TABLE_TAG);

        scenarioToHTML = spy(new ScenarioToHTML(scenarioResult));

        when(beforeAfterResult.getMatch()).thenReturn(match);
        when(beforeAfterResult.getResult()).thenReturn(result);
        when(beforeAfterResult.getDuration()).thenReturn(DURATION);
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

}