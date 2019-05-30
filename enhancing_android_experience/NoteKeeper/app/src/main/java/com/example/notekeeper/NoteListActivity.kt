package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_note_list.*

class NoteListActivity() : AppCompatActivity() {
    var mAdapterNotes: ArrayAdapter<NoteInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            startActivity(Intent(this@NoteListActivity, NoteActivity::class.java))
        }

        initializeDisplayContent()
    }

    override fun onResume() {
        super.onResume()
        mAdapterNotes?.notifyDataSetChanged()
    }

    private fun initializeDisplayContent() {
        val listNotes: ListView = findViewById(R.id.list_notes)

        val notes: List<NoteInfo> = DataManager.getInstance().notes
        mAdapterNotes = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)

        listNotes.adapter = mAdapterNotes
//      First Implementation - Obtained from the web, https://www.androidly.net/394/android-listview-using-kotlin
//      listNotes.setOnItemClickListener { parent, view, position: Int, id: Long ->
//          val intent = Intent(this,NoteActivity::class.java)
//          startActivity(intent)
//      }

//      Second Implementation - instantiating an object of the type of the Interface required by setOnItemClickListener
//      listNotes.setOnItemClickListener(object: AdapterView.OnItemClickListener {
//          override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//              val intent = Intent(this@NoteListActivity,NoteActivity::class.java)
//              startActivity(intent)
//          }
//      })

//      Third Implementation - same as the second but using property access syntax
//      listNotes.onItemClickListener = object: AdapterView.OnItemClickListener {
//          override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//              val intent = Intent(this@NoteListActivity,NoteActivity::class.java)
//              startActivity(intent)
//          }
//      }

//      Fourth Implementation - Converted to Lambda
        listNotes.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@NoteListActivity,NoteActivity::class.java)
            //Obtain a reference to the NoteInfo that was clicked on
            //val note: NoteInfo = listNotes.getItemAtPosition(position) as NoteInfo
            //Now need to add it as an extra to my intent
            intent.putExtra(NoteActivity.NOTE_POSITION, position)
            startActivity(intent)
        }

    }

}
