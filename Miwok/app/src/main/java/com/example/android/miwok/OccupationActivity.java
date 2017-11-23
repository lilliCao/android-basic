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

public class OccupationActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_occupation);
        final List<Word> listItem = new ArrayList<>(Arrays.asList(
                new Word("sinh viên","student",R.drawable.sinh_vien,R.raw.voice_sinhv),
                new Word("giáo viên","teacher",R.drawable.giao_vien,R.raw.voice_giaovien),
                new Word("giảng viên","lecturer",R.drawable.lecturer,R.raw.voice_giangvien),
                new Word("nông dân","farmer",R.drawable.nong_dan,R.raw.voice_nongdan),
                new Word("công an","police",R.drawable.cong_an,R.raw.voice_congan),
                new Word("kế toán","accountant",R.drawable.ke_toan,R.raw.voice_ketoan),
                new Word("lập trình viên","programmer",R.drawable.lap_trinh_vien,R.raw.voice_laptrinhvien),
                new Word("nhân viên văn phòng","officer",R.drawable.nhan_vien_van_phong,R.raw.voice_nhanvvp)
        ));
        WordAdapter itemAdapter = new WordAdapter(this ,listItem,R.color.category_occupation,true);
        ListView listView=(ListView) findViewById(R.id.list_jobs);
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
                    mediaPlayer = MediaPlayer.create(OccupationActivity.this, word.getAudioNumber());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });


    }
}
