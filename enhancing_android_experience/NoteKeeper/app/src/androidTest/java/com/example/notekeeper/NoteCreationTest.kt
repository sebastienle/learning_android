package com.example.notekeeper

import android.provider.ContactsContract
import org.junit.Assert.*

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.runner.RunWith
import org.junit.Rule
import org.junit.Test

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import org.junit.BeforeClass
import org.hamcrest.Matchers.*
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.assertion.ViewAssertions.*


@RunWith(AndroidJUnit4::class)
class NoteCreationTest {
    companion object {
        lateinit var sDataManager: DataManager

        @BeforeClass
        @JvmStatic
        fun classSetUp() {
            sDataManager = DataManager.getInstance()
        }
    }

    @Rule @JvmField
    val mNoteListActivityRule: ActivityTestRule<NoteListActivity> = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun createNewNote() {
        val course:CourseInfo = sDataManager.getCourse("java_lang")
        val noteTitle: String = "Test Note title"
        val noteText: String = "This is the body of our test note"

        //val fabNewNote: ViewInteraction = onView(withId(R.id.fab))
        //fabNewNote.perform(click())
        //Same as previous two lines but on one line
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.spinner_courses)).perform(click())
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.title))))

        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle))
            .check(matches(withText(containsString(noteTitle))))
        onView(withId(R.id.text_note_text)).perform(typeText(noteText),
            closeSoftKeyboard())
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))))

        pressBack()

        val noteIndex: Int = sDataManager.notes.size - 1
        val note: NoteInfo = sDataManager.notes[noteIndex]
        assertEquals(course, note.course)
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.text)

    }
}