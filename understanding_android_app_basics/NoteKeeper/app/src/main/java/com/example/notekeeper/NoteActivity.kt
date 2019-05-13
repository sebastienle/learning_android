package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {
    var mNote: NoteInfo? = null
    var mIsNewNote: Boolean? = null
    var mSpinnerCourses: Spinner? = null
    var mTextNoteTitle: EditText? = null
    var mTextNoteText: EditText? = null
    var mNotePosition: Int? = null
    var mIsCancelling: Boolean? = null
    var mOriginalNoteCourseId : String? = null
    var mOriginalNoteTitle : String? = null
    var mOriginalNoteText : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)

        mSpinnerCourses = findViewById(R.id.spinner_courses)

        val courses:List<CourseInfo> = DataManager.getInstance().courses
        val adapterCourses: ArrayAdapter<CourseInfo> = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            courses)
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinnerCourses?.adapter = adapterCourses

        mTextNoteTitle = findViewById(R.id.text_note_title)
        mTextNoteText = findViewById(R.id.text_note_text)

        readDisplayStateValues()
        if (savedInstanceState == null) {
            saveOriginalNoteValues()
        } else {
            restoreOriginalNoteValues(savedInstanceState)
        }

        if (mIsNewNote != null) {
            displayNote(mSpinnerCourses,mTextNoteText,mTextNoteTitle)
        }
    }

    private fun restoreOriginalNoteValues(savedInstanceState: Bundle) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID)
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE)
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT)
    }

    private fun saveOriginalNoteValues() {
        if (mIsNewNote == true) {
            return
        }
        mOriginalNoteCourseId = mNote?.course?.courseId
        mOriginalNoteTitle = mNote?.title
        mOriginalNoteText = mNote?.text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id: Int? = item?.itemId
        if (id == R.id.action_send_mail) {
            sendEmail()
            return true
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        if (mIsCancelling == true) {
            // Remove the note we had created
            if (mIsNewNote == true) {
                DataManager.getInstance().removeNote(mNotePosition!!)
            } else {
                storePreviousNoteValues()
            }
        } else {
            saveNote()
        }
    }

    private fun storePreviousNoteValues() {
        val course : CourseInfo = DataManager.getInstance().getCourse(mOriginalNoteCourseId)
        mNote?.course = course
        mNote?.title = mOriginalNoteTitle
        mNote?.text = mOriginalNoteText
    }

    private fun saveNote() {
        mNote?.course = mSpinnerCourses?.selectedItem as CourseInfo
        mNote?.title = mTextNoteTitle?.text.toString()
        mNote?.text = mTextNoteText?.text.toString()
    }

    private fun sendEmail() {
        val course: CourseInfo = mSpinnerCourses?.selectedItem as CourseInfo
        val subject: String = mTextNoteTitle?.text.toString()
        val text: String = "Check out what I learned in the Pluralsight course \"" + course.title + "\"\n" +
                mTextNoteText?.text.toString()
        val intent: Intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc2822"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putString(ORIGINAL_NOTE_COURSE_ID,mOriginalNoteCourseId)
        outState?.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle)
        outState?.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText)
    }

    private fun displayNote(spinnerCourses: Spinner?, textNoteText: EditText?, textNoteTitle: EditText?) {
        val courses: List<CourseInfo> = DataManager.getInstance().courses
        val courseIndex: Int = courses.indexOf(mNote?.course)
        spinnerCourses?.setSelection(courseIndex)

        textNoteTitle?.setText(mNote?.title)
        textNoteText?.setText(mNote?.text)
    }

    private fun readDisplayStateValues() {
        val intent: Intent = intent
        val position: Int = intent.getIntExtra(NOTE_POSITION,POSITION_NOT_SET)
        mIsNewNote = position == POSITION_NOT_SET
        if (mIsNewNote == true) {
            createNewNote()
        } else {
            mNote = DataManager.getInstance().notes[position]
        }
    }

    private fun createNewNote() {
        val dm: DataManager = DataManager.getInstance()
        mNotePosition = dm.createNewNote()
        mNote = dm.notes[mNotePosition!!]
    }

    companion object {
        const val NOTE_POSITION: String = "com.example.notekeeper.NOTE_POSITION"
        const val ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID"
        const val ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE"
        const val ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT"
        const val POSITION_NOT_SET: Int = -1
    }
}
