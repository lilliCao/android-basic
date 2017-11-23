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

public class PhrasesActivity extends AppCompatActivity {
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<Word> listItem=new ArrayList<>(Arrays.asList(
                new Word("Con chào anh/chị/em/cô/bác","Hello",R.raw.voice_chao),
                new Word("Anh khoẻ không?","How are you?",R.raw.voice_khoe),
                new Word("Anh bao nhiêu tuổi?","How old are you?",R.raw.voice_tuoi),
                new Word("Anh tên gì ạ?","What's your name?",R.raw.voice_ten),
                new Word("Anh làm nghề gì ạ?","What does you do?",R.raw.voice_lam),
                new Word("Anh yêu em","I love you",R.raw.voice_aye),
                new Word("Em là sinh viên","I am student",R.raw.voice_sinhvien),
                new Word("Em 24 tuổi","I am 24 years old",R.raw.voice_tuoi24),
                new Word("Chúc mừng năm mới","Happy new year",R.raw.voice_nammoi),
                new Word("Anh cám ơn","Thank you",R.raw.voice_camon)
        ));
        WordAdapter itemAdapter=new WordAdapter(this,listItem,R.color.category_phrases);
        ListView listView= (ListView) findViewById(R.id.list_phrases);
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
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }
}
