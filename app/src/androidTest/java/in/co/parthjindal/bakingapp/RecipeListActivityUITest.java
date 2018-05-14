package in.co.parthjindal.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityUITest {

    @Test
    public void clickOnRecyclerViewItem() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.ingredient_label)).check(matches(isDisplayed()));

        onView(withId(R.id.step_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.view_pager))
                .check(matches(isDisplayed()));
    }
}
