package miadesign.hu.cakerecipes.ui.activity;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import miadesign.hu.cakerecipes.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import miadesign.hu.cakerecipes.ui.activity.RecipeListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = mActivityTestRule.getActivity().getEspressoIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Test
    public void bakingAppMainTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.recipe_name), withText("Brownies"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recipe_card_view),
                                        0),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("Brownies")));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipes_recycler_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction button = onView(
                allOf(withId(R.id.step_description),
                        childAtPosition(
                                allOf(withId(R.id.ingredients_and_steps_list),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                3),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.select_ingredients_list), withText("Ingredients"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.ingredient_name), withText("vanilla"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.ingredients_and_steps_list),
                                        4),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("vanilla")));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.select_steps_list), withText("Steps"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.ingredients_and_steps_list),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(4, click()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.step_short_description), withText("Start filling prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_detail_container),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Start filling prep")));

    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
