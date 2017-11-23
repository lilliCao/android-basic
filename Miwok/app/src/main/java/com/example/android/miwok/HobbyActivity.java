package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HobbyActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);
        final List<Word> listItem = new ArrayList<>(Arrays.asList(
                new Word("Em/Anh thích","I like",R.drawable.like,R.raw.voice_thich),
                new Word("Anh/Em có thể","I can",R.drawable.i_can,R.raw.voice_cothe),
                new Word("ăn","eat",R.drawable.eat,R.raw.voice_an),
                new Word("ngủ","sleep",R.drawable.sleep,R.raw.voice_ngu),
                new Word("mua sắm","go shopping",R.drawable.shopping,R.raw.voice_muasam),
                new Word("đi chợ","go to market",R.drawable.market,R.raw.voice_dicho),
                new Word("du lịch","travel",R.drawable.travel,R.raw.voice_dulich),
                new Word("nấu ăn","cook",R.drawable.cook,R.raw.voice_nauan),
                new Word("lập trình","code",R.drawable.program,R.raw.voice_laptrinh),
                new Word("xem ti vi","watch television",R.drawable.tv,R.raw.voice_xemtv),
                new Word("xem phim","watch movies",R.drawable.movie,R.raw.voice_phim),
                new Word("hát","sing",R.drawable.sing,R.raw.voice_hat),
                new Word("bơi","swim",R.drawable.swim,R.raw.voice_boi)
        ));
        WordAdapter itemAdapter = new WordAdapter(this ,listItem,R.color.category_hobby, true);
        ListView listView=(ListView) findViewById(R.id.list_hobbies);
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
                    mediaPlayer = MediaPlayer.create(HobbyActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });


    }
}
