package com.example.notekeeper

import android.provider.ContactsContract
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass

internal class DataManagerTest {
    companion object {
        lateinit var sDataManager: DataManager

        @BeforeClass @JvmStatic
        fun classSetUp() {
            sDataManager = DataManager.getInstance()
        }
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        sDataManager.notes.clear()
        sDataManager.initializeExampleNotes()
    }

    @Test
    fun createNewNote() {
        val course:CourseInfo? = sDataManager.getCourse("android_async")
        val noteTitle = "Test note title"
        val noteText = "This is the body text of my test note"

        val noteIndex: Int = sDataManager.createNewNote()
        var newNote: NoteInfo = sDataManager.notes[noteIndex]
        newNote.course = course
        newNote.title = noteTitle
        newNote.text = noteText

        // Start of test - get the note from DataManager and compare it
        val compareNote: NoteInfo = sDataManager.notes[noteIndex]
        //assertSame(newNote, compareNote)
        assertEquals(course, compareNote.course)
        assertEquals(noteTitle, compareNote.title)
        assertEquals(noteText, compareNote.text)
    }

    @Test
    fun findSimilarNotes() {
        val course: CourseInfo = sDataManager.getCourse("android_async")
        val noteTitle: String = "Test note title"
        val noteText1: String = "This is the body text of my test note"
        val noteText2: String = "This is the body of my second test note"

        val noteIndex1: Int = sDataManager.createNewNote()
        var newNote1: NoteInfo = sDataManager.notes[noteIndex1]
        newNote1.course = course
        newNote1.title = noteTitle
        newNote1.text = noteText1

        val noteIndex2: Int = sDataManager.createNewNote()
        var newNote2: NoteInfo = sDataManager.notes[noteIndex2]
        newNote2.course = course
        newNote2.title = noteTitle
        newNote2.text = noteText2

        val foundIndex1: Int = sDataManager.findNote(newNote1)
        assertEquals(noteIndex1, foundIndex1)

        val foundIndex2: Int = sDataManager.findNote(newNote2)
        assertEquals(noteIndex2, foundIndex2)
    }

    @Test
    fun createNewNoteOneStepCreation() {
        val course: CourseInfo = sDataManager.getCourse("android_async")
        val noteTitle: String = "Test note title"
        val noteText: String = "This is the body of my test note"

        val noteIndex: Int = sDataManager.createNewNote(course, noteTitle, noteText)

        val compareNote: NoteInfo = sDataManager.notes[noteIndex]
        assertEquals(course, compareNote.course)
        assertEquals(noteTitle, compareNote.title)
        assertEquals(noteText, compareNote.text)
    }
}