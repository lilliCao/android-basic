package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    releaseResource();
                }
            };
    private AudioManager audioManager;
    private final AudioManager.OnAudioFocusChangeListener audioListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if(mediaPlayer!=null){
                if(i==AudioManager.AUDIOFOCUS_LOSS){
                    mediaPlayer.stop();
                    releaseResource();
                }else if (i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                        i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }else if(i==AudioManager.AUDIOFOCUS_GAIN){
                    mediaPlayer.start();
                }
            }
        }
    };

    public void releaseResource(){
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.abandonAudioFocus(audioListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseResource();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final List<Word> listItem = new ArrayList<>(Arrays.asList(
            new Word("bà","grandmother",R.drawable.family_grandmother,R.raw.voice_ba),
            new Word("ông","grandfather",R.drawable.family_grandfather,R.raw.voice_ong),
            new Word("bố","father",R.drawable.family_father,R.raw.voice_bo),
            new Word("mẹ","mother",R.drawable.family_mother,R.raw.voice_me),
            new Word("con gái","daughter",R.drawable.family_daughter,R.raw.voice_cgai),
            new Word("con trai","son",R.drawable.family_son,R.raw.voice_ctrai),
            new Word("anh trai","older brother",R.drawable.family_older_brother,R.raw.voice_atrai),
            new Word("chị gái","older sister",R.drawable.family_older_sister,R.raw.voice_chigai),
            new Word("cháu gái","niece",R.drawable.family_younger_sister,R.raw.voice_chaugai),
            new Word("cháu trai","nephew",R.drawable.family_younger_brother,R.raw.voice_chautrai),
            new Word("dì","aunt",R.drawable.family_younger_brother,R.raw.voice_di),
            new Word("bác","uncle",R.drawable.family_younger_brother,R.raw.voice_bac)
            ));
        WordAdapter itemAdapter = new WordAdapter(this ,listItem,R.color.category_family);
        ListView listView=(ListView) findViewById(R.id.list_family);
        listView.setAdapter(itemAdapter);

        audioManager= (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Word word = listItem.get(i);
                releaseResource();

                int result=audioManager.requestAudioFocus(
                        audioListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Got auto focus
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });


    }
}
