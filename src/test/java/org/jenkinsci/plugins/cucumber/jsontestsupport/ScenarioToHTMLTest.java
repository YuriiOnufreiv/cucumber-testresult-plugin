package org.jenkinsci.plugins.cucumber.jsontestsupport;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;


@RunWith(MockitoJUnitRunner.class)
public class ScenarioToHTMLTest {

    private static final String PASSED_STEP_CSS = "background-color: #e6ffcc;";

    private static final float DURATION = 1.5f;
    private static final float INVALID_DURATION = 2f;
    private static final String EXPECTED_ADD_DURATION_HTML_FOR_PASSED_STEP =
            "<td nowrap=\"nowrap\" valign=\"top\" align=\"left\" style=\"background-color: #e6ffcc;\">" +
                    "<div style=\"padding-left: 2em;background-color: #e6ffcc;\">1.500";
    private static final String TABLE_TAG = "<table>";

    private StringBuilder html;
    private ScenarioToHTML scenarioToHTML;
    @Mock
    private ScenarioResult scenarioResult;


    @Before
    public void setup() {
        html = new StringBuilder(TABLE_TAG);
        scenarioToHTML = spy(new ScenarioToHTML(scenarioResult));
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



}