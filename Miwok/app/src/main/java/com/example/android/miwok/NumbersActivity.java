package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);
        final List<Word> listItem = new ArrayList<>(Arrays.asList(
                new Word("một", "one", R.drawable.number_one, R.raw.voice_001),
                new Word("hai", "two", R.drawable.number_two, R.raw.voice_002),
                new Word("ba", "three", R.drawable.number_three, R.raw.voice_003),
                new Word("bốn", "four", R.drawable.number_four, R.raw.voice_004),
                new Word("năm", "five", R.drawable.number_five, R.raw.voice_005),
                new Word("sáu", "six", R.drawable.number_six, R.raw.voice_006),
                new Word("bảy", "seven", R.drawable.number_seven,R.raw.voice_007),
                new Word("tám", "eight", R.drawable.number_eight, R.raw.voice_008),
                new Word("chín", "nine", R.drawable.number_nine, R.raw.voice_009),
                new Word("mười", "ten", R.drawable.number_ten, R.raw.voice_010),
                new Word("mười một", "eleven", R.drawable.number_ten, R.raw.voice_011),
                new Word("mười hai", "twelve", R.drawable.number_ten, R.raw.voice_012),
                new Word("hai mươi", "twenty", R.drawable.number_ten, R.raw.voice_020),
                new Word("hai mốt", "twenty one", R.drawable.number_ten, R.raw.voice_021),
                new Word("hai hai", "twenty two", R.drawable.number_ten, R.raw.voice_022),
                new Word("ba mươi", "thirty", R.drawable.number_ten, R.raw.voice_030),
                new Word("ba mốt", "thirty one", R.drawable.number_ten, R.raw.voice_031),
                new Word("một trăm", "one hundred", R.drawable.number_ten, R.raw.voice_100)


        ));
        WordAdapter itemAdapter = new WordAdapter(this, listItem, R.color.category_numbers);
        ListView listView = (ListView) findViewById(R.id.list_numbers);
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
                    mediaPlayer = MediaPlayer.create(NumbersActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }

}