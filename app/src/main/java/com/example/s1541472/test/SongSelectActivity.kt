package com.example.s1541472.test

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_song_select.*
import android.widget.AdapterView.OnItemClickListener
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*

class SongSelectActivity : AppCompatActivity() {

    var songs = ArrayList<Song>()

    //difficulty default is 0(easy)
    var diff: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_select)

        val intent = intent

        diff = intent.getIntExtra("songle.difficultySet",0)

        //diff default is easy
        println("Difficulty Set: " + diff)

        //change Title displayed in action bar
        when(diff){
            0 -> title = "Easy"
            1 -> title = "Medium"
            2 -> title = "Hard"
            3 -> title = "Extra Hard"
            4 -> title = "Extreme"
            else -> title = "ERROR"
        }
        
        Random.setOnClickListener { songDownloadFail() }

        for(i in 1..12){
            val song1 = Song(title = "${i}", number = 1, artist = "me", link = "None", complete = 0)
            songs.add(song1)
        }

        val song1 = Song(title = "Bohemian Rhapsody", number = 1, artist = "Queen", link = "https://youtu.be/fJ9rUzIMcZQ", complete = 1)
        val song2 = Song(title = "I Fought the Law", number = 1, artist = "Queen", link = "https://youtu.be/fJ9rUzIMcZQ", complete = 1)
        songs.add(5,song1)
        songs.add(7,song2)

        //run after download and parsing
        val adapter = ArrayAdapter(this,R.layout.simplerow,songs)

        songList.adapter = adapter

        val infoBuilder = AlertDialog.Builder(this)

        songList.onItemClickListener = object: OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val entrySong = songs[p2]

                if (entrySong.complete == 1){

                    infoBuilder.setMessage("""Artist: ${entrySong.artist}
                        |Title: ${entrySong.title}
                        |Link : ${entrySong.link}
                    """.trimMargin())
                            .setTitle("Song Info")
                    //set ok button for exit
                    infoBuilder.setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, id ->
                        // User clicked return button
                    })
                    infoBuilder.setNegativeButton("Play again", DialogInterface.OnClickListener { dialog, id ->
                        // User clicked play again button
                        switchtoMap()
                    })
                    infoBuilder.setNeutralButton("Listen",DialogInterface.OnClickListener { dialog, id ->
                        watchVideoLink(entrySong.link)
                    })
                    infoBuilder.create().show()

                }else switchtoMap()

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_song_select, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        val list = ListView(this)

        val complist = ArrayList<Song>()

        val s = Song(title = "Bohemian Rhapsody", number = 1, artist = "Queen", link = "https://youtu.be/fJ9rUzIMcZQ", complete = 1)
        val r = Song(title = "I Fought the Law", number = 1, artist = "Queen", link = "https://youtu.be/fJ9rUzIMcZQ", complete = 2)
        complist.add(s)
        complist.add(r)

        val adapter = songListAdapter(complist,this)

        list.adapter = adapter

        val diabuild = AlertDialog.Builder(this)

        //building completed song List
        val inflater = layoutInflater
        val ve = inflater.inflate(R.layout.word_collected,null)
        diabuild.setView(list)
                .setCustomTitle(ve)
        val songComp = diabuild.create()


        return when (item.itemId) {
            R.id.comp_song ->  {
                songComp.show()
                true}
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchtoMap(){
        val intent = Intent(this,MapsActivity::class.java)
        startActivity(intent)

    }

    fun watchVideoLink(id: String) {

        val id_fix = id.substring(17,id.length)

        val applicationIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id_fix))
        val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id_fix))
        try {
            startActivity(applicationIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(browserIntent)
        }

    }

    private fun songDownloadFail(){

        Snackbar.make(topLayer , "Song list download has failed", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", View.OnClickListener {

                }).show()

    }

}
