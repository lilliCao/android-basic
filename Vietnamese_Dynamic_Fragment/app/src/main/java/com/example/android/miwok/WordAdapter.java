package com.example.android.miwok;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tali on 22.11.17.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    private boolean isSongActivity;
    private int color;
    private boolean noAdaptableImages;

    //0 mean not single View
    public WordAdapter(@NonNull Activity context, List<Word> listItem, int color) {
        super(context, 0, listItem);
        this.color = color;
        this.noAdaptableImages = false;
        this.isSongActivity = false;
    }

    public WordAdapter(@NonNull Activity context, List<Word> listItem, int color, boolean noAdaptableImages) {
        super(context, 0, listItem);
        this.color = color;
        this.noAdaptableImages = noAdaptableImages;
        this.isSongActivity = false;
    }

    public WordAdapter(@NonNull Activity context, List<Word> listItem, int color, boolean noAdaptableImages, boolean isSongActivity) {
        super(context, 0, listItem);
        this.color = color;
        this.noAdaptableImages = noAdaptableImages;
        this.isSongActivity = true;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        View listItemView = view;
        //If not use, -->inflating new view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }
        //current pos
        Word currerntWord = getItem(pos);
        TextView origin = (TextView) listItemView.findViewById(R.id.origin);
        origin.setText(currerntWord.getOrigin());
        TextView translation = (TextView) listItemView.findViewById(R.id.translation);
        translation.setText(currerntWord.getTranslation());
        ImageView image = (ImageView) listItemView.findViewById(R.id.image);
        //get color from int of color
        int colorToSet = ContextCompat.getColor(getContext(), color);
        listItemView.setBackgroundColor(colorToSet);
        image.setBackgroundColor(Color.WHITE);
        if (noAdaptableImages) {
            image.getLayoutParams().height = 340;
            image.getLayoutParams().width = 340;
            GlideApp.with(getContext()).load(currerntWord.getImageNumber()).into(image);
        } else {
            image.setImageResource(currerntWord.getImageNumber());
        }
        ImageView button = (ImageView) listItemView.findViewById(R.id.button);
        if (!isSongActivity) {
            button.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        }
        button.setTag(R.drawable.ic_play_circle_outline_black_24dp);

        /*Another way to create sound while click button
        define it in xml -->
         <Button
                android:id="@+id/button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true" />
        callback onClick hier -->
        Button button= (Button) listItemView.findViewById(R.id.button);
        final MediaPlayer mediaPlayer=MediaPlayer.create(getContext(),currerntWord.getAudioNumber());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });
        */
        return listItemView;

    }
}
