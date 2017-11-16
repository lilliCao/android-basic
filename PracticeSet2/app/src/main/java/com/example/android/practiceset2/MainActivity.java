package com.example.android.practiceset2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    int teamA = 0;
    int teamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void result() {
        TextView viewResult = (TextView) findViewById(R.id.result);
        if (teamA < teamB) {
            viewResult.setText("B is going ahead");
        } else if (teamA > teamB) {
            viewResult.setText("A is going ahead");
        } else {
            viewResult.setText("2 teams are on the same level");
        }
    }

    public void reset(View view) {
        teamA = 0;
        teamB = 0;
        pointB();
        pointA();
        result();
    }

    public void noPointB(View view) {
    }

    public void point2B(View view) {
        teamB = teamB + 2;
        pointB();
        result();
    }

    public void point3B(View view) {
        teamB = teamB + 3;
        pointB();
        result();
    }

    public void pointB() {
        TextView viewB = (TextView) findViewById(R.id.pointB);
        viewB.setText(NumberFormat.getNumberInstance().format(teamB));
    }

    public void noPointA(View view) {
    }

    public void point2A(View view) {
        teamA = teamA + 2;
        pointA();
        result();
    }

    public void point3A(View view) {
        teamA = teamA + 3;
        pointA();
        result();
    }

    public void pointA() {
        TextView viewA = (TextView) findViewById(R.id.pointA);
        viewA.setText(NumberFormat.getNumberInstance().format(teamA));
    }
}
