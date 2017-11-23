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

public class ColorsActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_colors);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final List<Word> listItem=new ArrayList<>(Arrays.asList(
                new Word("màu đen","black",R.drawable.color_black,R.raw.voice_den),
                new Word("màu trắng","white",R.drawable.color_white,R.raw.voice_trang),
                new Word("màu xám","gray",R.drawable.color_gray,R.raw.voice_xam),
                new Word("màu xanh","green",R.drawable.color_green,R.raw.voice_xanh),
                new Word("màu vàng","dusty yellow",R.drawable.color_dusty_yellow,R.raw.voice_vang),
                new Word("màu vàng mù tạt","mustard yellow",R.drawable.color_mustard_yellow,R.raw.voice_vangmutat),
                new Word("màu nâu","brown",R.drawable.color_brown,R.raw.voice_nau),
                new Word("màu đỏ","red",R.drawable.color_red,R.raw.voice_do)
        )
        );
        ListView listView= (ListView) findViewById(R.id.list_colors);
        WordAdapter itemAdapter=new WordAdapter(this,listItem,R.color.category_colors);
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
                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }
}
