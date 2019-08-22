package com.rust.aboutview;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import com.rust.aboutview.contactview.AddDataActivity;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest1 {
    private static final String TAG = "rustAppUnitTest";

    @Rule
    public ActivityTestRule<AddDataActivity> mAddData = new ActivityTestRule<>(AddDataActivity.class); // 必须是public

    @Before
    public void prepareData() {
        System.out.println("做一些准备工作,比如预装数据");
        Log.d(TAG, "prepareData.");
    }

    /// 检查初始状态
    @Test
    public void checkDefStatus() {
        onView(withText("Add Data")).check(matches(isDisplayed()));

        onView(withId(R.id.done_add_data_button)).check(matches(not(isEnabled())));
        onView(withId(R.id.done_add_data_button)).check(matches((isClickable())));
    }

    @Test
    public void addPersonData() {
        Log.d(TAG, "addPersonData test starts.");
        Matcher<View> confirmBtn = withId(R.id.done_add_data_button);
        Matcher<View> nameEt = withId(R.id.add_name);
        Matcher<View> phoneEt = withId(R.id.add_phone);
        Matcher<View> emailEt = withId(R.id.add_email);

        onView(nameEt).perform(replaceText("Test Name " + System.currentTimeMillis()));
        onView(confirmBtn).check(matches(isEnabled())); // 输入用户名后的状态
        onView(phoneEt).perform(replaceText("13312345678"));
        onView(emailEt).perform(replaceText("xx@yy.com"));
        onView(confirmBtn).perform(click());
    }
}
