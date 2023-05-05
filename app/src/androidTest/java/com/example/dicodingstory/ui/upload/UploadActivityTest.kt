package com.example.dicodingstory.ui.upload

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.BundleMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dicodingstory.R
import com.example.dicodingstory.utils.EspressoIdlingResource
import org.hamcrest.core.AllOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UploadActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(UploadActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadUploadedStory_Success() {
        Intents.init()
        val expectedIntent = AllOf.allOf(
            IntentMatchers.hasAction(Intent.ACTION_CHOOSER),
            IntentMatchers.hasExtras(
                BundleMatchers.hasValue(
                    AllOf.allOf(
                        IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT),
                        IntentMatchers.hasType("image/*")
                    )
                )
            )
        )
        val activityResult = createGalleryPickActivityResult()
        intending(expectedIntent).respondWith(activityResult)

        onView(withId(R.id.btn_gallery)).perform(click())
        intended(expectedIntent)

        onView(withId(R.id.switch_location)).perform(swipeRight())
        onView(withId(R.id.ed_add_description)).perform(typeText("UI Test"))
        onView(withId(R.id.button_add)).perform(click())

        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.iv_thumbnail)).check(matches(isDisplayed()))
        pressBack()
    }

    private fun createGalleryPickActivityResult(): Instrumentation.ActivityResult {
        val resource = InstrumentationRegistry.getInstrumentation().context.resources
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    resource.getResourcePackageName(R.drawable.ic_launcher_background) + "/" +
                    resource.getResourceTypeName(R.drawable.ic_launcher_background) + "/" +
                    resource.getResourceEntryName(R.drawable.ic_launcher_background)
        )
        val resultIntent = Intent()
        val intent = Intent()
        resultIntent.type = "image/*"
        resultIntent.action = Intent.ACTION_GET_CONTENT
        resultIntent.data = imageUri
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, chooser)
    }
}