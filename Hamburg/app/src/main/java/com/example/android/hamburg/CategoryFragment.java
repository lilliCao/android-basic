package com.example.android.hamburg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    final static String itemList = "ITEM_LIST";
    final static String badImage = "BAD_IMAGE";
    final static String color = "BACKGROUND_COLOR";
    final static String titleName = "TITLE_NAME";
    final static String titleImage = "TITLE_IMAGE";
    final static String generalInformation = "GENERAL_INFORMATION";

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(ArrayList<Item> itemArrayList, int backgroundColor,
                                               boolean noAdaptableImage, int titleImageId, String name, boolean isGeneralInfor) {
        CategoryFragment categoryFragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(itemList, itemArrayList);
        bundle.putInt(color, backgroundColor);
        bundle.putBoolean(badImage, noAdaptableImage);
        bundle.putInt(titleImage, titleImageId);
        bundle.putString(titleName, name);
        bundle.putBoolean(generalInformation, isGeneralInfor);
        categoryFragment.setArguments(bundle);
        return categoryFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_item, container, false);
        //Get value from bundle
        final ArrayList<Item> items = getArguments().getParcelableArrayList(itemList);
        int imageId = getArguments().getInt(titleImage);
        String name = getArguments().getString(titleName);
        int backgroundColor = getArguments().getInt(color);
        boolean isBadImage = getArguments().getBoolean(badImage);
        final boolean isGeneral = getArguments().getBoolean(generalInformation);
        //Get view
        ImageView titleImageView = rootView.findViewById(R.id.itemImage);
        TextView titleTextView = rootView.findViewById(R.id.itemName);
        final ListView listView = rootView.findViewById(R.id.itemList);
        //Create View for Fragment
        if (isBadImage) {
            GlideApp.with(getContext()).load(imageId).into(titleImageView);
        } else {
            titleImageView.setImageResource(imageId);
        }
        titleTextView.setText(name);
        ItemAdapter itemAdapter = new ItemAdapter(getActivity(), items, backgroundColor, isBadImage);
        listView.setAdapter(itemAdapter);

        //Handle click on list view item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item currentItem = items.get(i);
                TextView itemStatus = view.findViewById(R.id.itemListStatus);
                TextView textView = view.findViewById(R.id.content);
                TextView link1View = view.findViewById(R.id.link1);
                TextView link2View = view.findViewById(R.id.link2);
                if (!isGeneral) {
                    itemStatus.setText(getString(R.string.seen));
                    items.get(i).setStatus(getString(R.string.seen));
                }
                link1View.setVisibility(View.GONE);
                link2View.setVisibility(View.GONE);

                if (textView.getText().toString().isEmpty()) {
                    textView.setText(currentItem.getText());
                    if (link1View.getText().toString().isEmpty() && !currentItem.getLink1().isEmpty()) {
                        link1View.setText(currentItem.getLink1());
                        link1View.setVisibility(View.VISIBLE);
                    }
                    if (link2View.getText().toString().isEmpty() && !currentItem.getLink2().isEmpty()) {
                        link2View.setText(currentItem.getLink2());
                        link2View.setVisibility(View.VISIBLE);
                    }
                } else {
                    textView.setText("");
                    link1View.setText("");
                    link2View.setText("");
                    link1View.setVisibility(View.GONE);
                    link2View.setVisibility(View.GONE);
                }


            }
        });
        return rootView;
    }

}
