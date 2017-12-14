package com.example.android.platinebuilder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int DEFAULT_ZOOM_PROZENT = 100;
    private ArrayList<Integer> chosen = new ArrayList<>();
    private GridView gridView;
    private Button connect;
    private Button remove;
    private Button win;
    private ArrayList<Item> items;
    private ItemAdapter itemAdapter;
    private Button load;
    private static int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = findViewById(R.id.dropdown);
        gridView = findViewById(R.id.gridview);
        connect = findViewById(R.id.connectB);
        remove = findViewById(R.id.removeB);
        win = findViewById(R.id.winB);
        load = findViewById(R.id.load);
        //toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.drowdownList, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        itemAdapter = new ItemAdapter(this, new ArrayList<Item>());
        gridView.setAdapter(itemAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (items != null) {
                    items.clear();
                } else {
                    items = new ArrayList<>();
                }
                getData(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DEFAULT_ZOOM_PROZENT = 100;
                chosen.clear();
                itemAdapter.clear();
                itemAdapter.addAll(items);
                gridView.setAdapter(itemAdapter);
                gridView.setEnabled(true);
                connect.setClickable(true);
                remove.setClickable(true);
                win.setClickable(true);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!items.get(i).isPlatine()) {
                    return;
                }
                LinearLayout platines = view.findViewById(R.id.platines);
                platines.setBackgroundColor(Color.WHITE);
                if (chosen.size() == 2) {
                    View v = gridView.getChildAt(chosen.get(0));
                    LinearLayout r = v.findViewById(R.id.platines);
                    r.setBackgroundColor(getResources().getColor(R.color.colorDefault, null));
                    chosen.remove(0);
                }
                if (!chosen.contains(i)) {
                    chosen.add(i);
                }


            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosen != null && chosen.size() == 2 && items.get(chosen.get(0)).isPlatine() && items.get(chosen.get(1)).isPlatine()) {
                    if (isConnectPossible(chosen.get(0), chosen.get(1))) {
                        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Connect impossible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please choose 2 platines to connect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosen != null && chosen.size() == 2 && items.get(chosen.get(0)).isPlatine() && items.get(chosen.get(1)).isPlatine()) {
                    if (isRemovePossible(chosen.get(0), chosen.get(1))) {
                        Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Remove impossible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please choose 2 platines to remove loading", Toast.LENGTH_SHORT).show();
                }

            }
        });
        win.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWinning()) {
                    Toast.makeText(MainActivity.this, "Well done. Next level?", Toast.LENGTH_SHORT).show();
                    gridView.setEnabled(false);
                    connect.setClickable(false);
                    remove.setClickable(false);
                    win.setClickable(false);
                } else {
                    Toast.makeText(MainActivity.this, "Not really. One more try?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getData(int i) {
        switch (i) {
            case 0:
                deepCopy(Data.level1);
                currentLevel = 1;
                break;
            case 1:
                deepCopy(Data.level2);
                currentLevel = 2;
                break;
            case 2:
                deepCopy(Data.level3);
                currentLevel = 3;
                break;
            case 3:
                deepCopy(Data.level4);
                currentLevel = 4;
                break;
            case 4:
                deepCopy(Data.level5);
                currentLevel = 5;
                break;
            case 5:
                deepCopy(Data.level6);
                currentLevel = 6;
                break;
            case 6:
                deepCopy(Data.level7);
                currentLevel = 7;
                break;
            case 7:
                deepCopy(Data.level8);
                currentLevel = 8;
                break;
            case 8:
                deepCopy(Data.level9);
                currentLevel = 9;
                break;
            case 9:
                deepCopy(Data.level10);
                currentLevel = 10;
                break;
            default:
                deepCopy(Data.level11);
                currentLevel = 11;
                break;

        }
    }

    private void deepCopy(ArrayList<Item> list) {
        for (Item item : list) {
            try {
                items.add((Item) item.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isRemovePossible(int i, int j) {

        int row1 = i % 5;
        int row2 = j % 5;
        int count = Math.abs(i - j);
        boolean sameColumn = (row1 == row2) && count > 5;
        boolean sameRow = (i / 5) == (j / 5) && count < 5 && count > 1;
        int start;
        int end;
        if (sameRow) {
            if (i > j) {
                start = j + 1;
                end = i;
            } else {
                start = i + 1;
                end = j;
            }
            for (int index = start; index < end; index++) {
                View view = gridView.getChildAt(index);
                LinearLayout horizon = view.findViewById(R.id.connectors_horizontal);
                View view1 = view.findViewById(R.id.connect1);
                View view2 = view.findViewById(R.id.connect2);
                int numberOfConnect = items.get(index).getNumberOfConect();
                switch (numberOfConnect) {
                    case 0:
                        return false;
                    case 1:
                        horizon.setVisibility(View.GONE);
                        items.get(index).setNumberOfConect(0);
                        break;
                    default:
                        horizon.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);
                        items.get(index).setNumberOfConect(1);
                        break;
                }
            }
            return true;
        } else if (sameColumn) {
            if (i > j) {
                start = j / 5 + 1;
                end = i;
            } else {
                start = i / 5 + 1;
                end = j;
            }
            for (int index = start; index * 5 + row1 < end; index++) {
                int toCheck = (index * 5) + row1;
                View view = gridView.getChildAt(toCheck);
                LinearLayout vertical = view.findViewById(R.id.connectors_vertical);
                View view1 = view.findViewById(R.id.connectors_vertical1);
                View view2 = view.findViewById(R.id.connectors_vertical2);
                int connectors = items.get(toCheck).getNumberOfConect();
                switch (connectors) {
                    case 0:
                        return false;
                    case 1:
                        vertical.setVisibility(View.GONE);
                        items.get(toCheck).setNumberOfConect(0);
                        break;
                    default:
                        vertical.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);
                        items.get(toCheck).setNumberOfConect(1);
                        break;
                }
            }
            return true;

        }
        return false;
    }

    private boolean isWinning() {
        int max;
        int up;
        int down;
        int left;
        int right;
        for (int indexI = 0; indexI < items.size(); indexI++) {
            if (items.get(indexI).isPlatine()) {
                down = indexI + 5;
                up = indexI - 5;
                right = indexI + 1;
                left = indexI - 1;
                max = (indexI / 5 + 1) * 5;
                if ((up >= 0) && (up <= max + 5) && (up <= items.size() - 1) && (!items.get(up).isHorizon())) {
                    int newTotal = items.get(indexI).getTotal() + items.get(up).getNumberOfConect();
                    items.get(indexI).setTotal(newTotal);
                }
                if ((down >= 0) && (down <= max + 5) && (down <= items.size() - 1) && (!items.get(down).isHorizon())) {
                    int newTotal = items.get(indexI).getTotal() + items.get(down).getNumberOfConect();
                    items.get(indexI).setTotal(newTotal);
                }
                if ((right >= 0) && (right < max) && (right <= items.size() - 1) && (items.get(right).isHorizon())) {
                    int newTotal = items.get(indexI).getTotal() + items.get(right).getNumberOfConect();
                    items.get(indexI).setTotal(newTotal);
                }
                if ((left >= 0) && (left < max) && (left <= items.size() - 1) && (items.get(left).isHorizon())) {
                    int newTotal = items.get(indexI).getTotal() + items.get(left).getNumberOfConect();
                    items.get(indexI).setTotal(newTotal);
                }

                if (items.get(indexI).getTotal() != items.get(indexI).getCapacity()) {
                    for (Item item : items) {
                        item.setTotal(0);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isConnectPossible(int i, int j) {
        ArrayList<Integer> toConnect = new ArrayList<>();
        int row1 = i % 5;
        int row2 = j % 5;
        int count = Math.abs(i - j);
        boolean sameColumn = (row1 == row2) && count > 5;
        boolean sameRow = (i / 5) == (j / 5) && count < 5 && count > 1;
        int start;
        int end;
        if (sameRow) {
            if (i > j) {
                start = j + 1;
                end = i;
            } else {
                start = i + 1;
                end = j;
            }
            for (int index = start; index < end; index++) {
                toConnect.add(index);
                if ((items.get(index).isPlatine()) || (items.get(index).getNumberOfConect() == 2)) {
                    toConnect.clear();
                    return false;
                }
            }
            for (int toDraw : toConnect) {
                View view = gridView.getChildAt(toDraw);
                LinearLayout horizon = view.findViewById(R.id.connectors_horizontal);
                horizon.setVisibility(View.VISIBLE);
                View view1 = view.findViewById(R.id.connect1);
                View view2 = view.findViewById(R.id.connect2);
                int connectors = items.get(toDraw).getNumberOfConect();
                if (connectors == 0) {
                    items.get(toDraw).setNumberOfConect(1);
                    items.get(toDraw).setHorizon(true);
                    view1.setVisibility(View.VISIBLE);
                } else {
                    items.get(toDraw).setNumberOfConect(2);
                    items.get(toDraw).setHorizon(true);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }
            }
            return true;
        } else if (sameColumn) {
            int base;
            if (i > j) {
                start = j / 5 + 1;
                end = i;
            } else {
                start = i / 5 + 1;
                end = j;
            }
            for (int index = start; index * 5 + row1 < end; index++) {
                int toAdd = (index * 5) + row1;
                toConnect.add(toAdd);
                if ((items.get(toAdd).isPlatine()) || (items.get(toAdd).getNumberOfConect() == 2)) {
                    toConnect.clear();
                    return false;
                }
            }
            for (int toDraw : toConnect) {
                View view = gridView.getChildAt(toDraw);
                LinearLayout horizon = view.findViewById(R.id.connectors_vertical);
                horizon.setVisibility(View.VISIBLE);
                View view1 = view.findViewById(R.id.connectors_vertical1);
                View view2 = view.findViewById(R.id.connectors_vertical2);
                int connectors = items.get(toDraw).getNumberOfConect();
                if (connectors == 0) {
                    items.get(toDraw).setNumberOfConect(1);
                    items.get(toDraw).setHorizon(false);
                    view1.setVisibility(View.VISIBLE);
                } else {
                    items.get(toDraw).setNumberOfConect(2);
                    items.get(toDraw).setHorizon(false);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = findViewById(R.id.help);
        switch (item.getItemId()) {
            case R.id.help:
                //create popup menu
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_items, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.about:
                                DialogFragment dialog = new AboutDialogFragment();
                                dialog.show(getSupportFragmentManager(), "");
                                break;
                            case R.id.feedback:
                                DialogFragment dialogFragment2 = new ContactDialogFragment();
                                dialogFragment2.show(getSupportFragmentManager(), "");
                                break;
                            default:
                                Intent intent = new Intent(MainActivity.this, SolutionActivity.class);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            case R.id.zoom_in:
                DEFAULT_ZOOM_PROZENT += 10;
                if (DEFAULT_ZOOM_PROZENT >= 200) {
                    DEFAULT_ZOOM_PROZENT = 200;
                }
                recreateGridView();
                return true;
            case R.id.zoom_out:
                DEFAULT_ZOOM_PROZENT += -10;
                if (DEFAULT_ZOOM_PROZENT <= 60) {
                    DEFAULT_ZOOM_PROZENT = 60;
                }
                recreateGridView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void recreateGridView() {
        chosen.clear();
        itemAdapter.clear();
        items.clear();
        getData(currentLevel - 1);
        itemAdapter.addAll(items);
        gridView.setAdapter(itemAdapter);
    }

    public static class AboutDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("This application is built for " +
                    "the IT talent competition 2018." +
                    "\nTopic: simulate the old japanese mystery " +
                    "by designing a game of building platine" +
                    "\nAuthor: L3I2")
                    .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            return builder.create();
        }
    }

    public static class ContactDialogFragment extends DialogFragment {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.contact, null);
            final EditText user = view.findViewById(R.id.user);
            final EditText feed = view.findViewById(R.id.feed);
            builder.setTitle("Feedback")
                    .setView(view)
                    .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"caothivananh98@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for BookSearch App from " + user.getText().toString());
                            intent.putExtra(Intent.EXTRA_TEXT, feed.getText().toString());
                            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                                startActivity(intent);
                            }
                            dialogInterface.cancel();
                        }
                    });
            return builder.create();
        }
    }
}
