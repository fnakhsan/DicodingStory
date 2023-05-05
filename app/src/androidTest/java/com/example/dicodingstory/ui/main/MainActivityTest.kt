package com.example.dicodingstory.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dicodingstory.BuildConfig
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.EspressoIdlingResource
import org.hamcrest.Matchers.anyOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun logoutLogin_Success() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(anyOf(withId(R.id.action_logout), withText(R.string.logout))).perform(click())
        onView(withId(R.id.ed_login_email)).perform(typeText(BuildConfig.USERNAME), pressImeActionButton())
        onView(withId(R.id.ed_login_password)).perform(typeText(BuildConfig.PASSWORD), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
    }
}