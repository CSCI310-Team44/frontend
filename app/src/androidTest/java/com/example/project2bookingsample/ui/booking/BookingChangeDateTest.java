package com.example.project2bookingsample.ui.booking;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.project2bookingsample.R;
import com.example.project2bookingsample.ui.login.LoginActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BookingChangeDateTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void bookingChangeDateTest() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("1000000001"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.username), withText("1000000001"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password), withText("password"),
                        childAtPosition(
                                allOf(withId(R.id.container),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction button = onView(
                allOf(withId(R.id.makeBookingButton), withText("Book"),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.ytd), withText("YTD"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_linear),
                                        5),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.date), withText(df.format(new Date(System.currentTimeMillis()-24*60*60*1000))),
                        withParent(allOf(withId(R.id.main_linear),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView2.check(matches(withText(df.format(new Date(System.currentTimeMillis()-24*60*60*1000)))));

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.ytd), withText("YTD"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_linear),
                                        5),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.date), withText(df.format(new Date(System.currentTimeMillis()-48*60*60*1000))),
                        withParent(allOf(withId(R.id.main_linear),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView3.check(matches(withText(df.format(new Date(System.currentTimeMillis()-48*60*60*1000)))));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.tmr), withText("TMR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_linear),
                                        5),
                                2),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.tmr), withText("TMR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_linear),
                                        5),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction button2 = onView(
                allOf(withId(R.id.time11), withText("16:00\n\nLEFT:\n50"),
                        withParent(withParent(withId(R.id.main_linear))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.time7), withText("12:00\n\nLEFT:\n50"),
                        withParent(withParent(withId(R.id.main_linear))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.tmr), withText("TMR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.main_linear),
                                        5),
                                2),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction button5 = onView(
                allOf(withId(R.id.time8), withText("13:00\n\nLEFT:\n50"),
                        withParent(withParent(withId(R.id.main_linear))),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.date), withText(df.format(new Date(System.currentTimeMillis()+24*60*60*1000))),
                        withParent(allOf(withId(R.id.main_linear),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView4.check(matches(withText(df.format(new Date(System.currentTimeMillis()+24*60*60*1000)))));
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
