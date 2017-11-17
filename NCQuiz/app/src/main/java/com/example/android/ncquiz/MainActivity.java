package com.example.android.ncquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int correctAnswer = 0;
    private boolean isIni = false;
    boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;
    boolean check4 = false;
    boolean check5 = false;
    boolean check6 = false;
    private RadioButton checkQuestion1;
    private RadioButton checkQuestion11;
    private RadioButton checkQuestion13;
    private CheckBox checkQuestion21;
    private CheckBox checkQuestion22;
    private CheckBox checkQuestion23;
    private CheckBox checkQuestion24;
    private RadioButton checkQuestion3;
    private RadioButton checkQuestion32;
    private RadioButton checkQuestion33;
    private CheckBox checkQuestion41;
    private CheckBox checkQuestion42;
    private CheckBox checkQuestion43;
    private CheckBox checkQuestion44;
    private RadioButton checkQuestion5;
    private RadioButton checkQuestion51;
    private RadioButton checkQuestion52;
    private RadioButton checkQuestion53;
    private EditText checkQuestion6;
    private TextView resultView;
    private TextView detailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void detail(View view) {
        TextView detail = (TextView) findViewById(R.id.rule);
        detail.setText("I call this quiz NC. If you are german, don't worry. " +
                "There is nothing to do with the Numerus Clausus, not with exams at all. " +
                "Its name is Nerd Check. It's a small quiz to check how nerd you are :) " +
                "Or let's say there are some funny questions for you to entertain:) " +
                "\nThere are some questions where you can only choice 1 unique answer(with circle)" +
                " and some you can choose more(with square). " +
                "\nPay attention to the question before you head any answer" +
                "\nPress submit if you are sure about the answer. Submit will block you from that question " +
                "since it will show you the answer after pressing" +
                "\nIf you don't have enough time to see the answer. Don't worry, you can take a look at all the QA at the end" +
                "\nOk. Enough text. I hope you have fun checking your nerd level" +
                "\nStay foolish. Stay hungry." +
                "\nBe passionate. Be curious\nBe a nerd \nL3I2");
    }

    private void ini() {
        checkQuestion1 = findViewById(R.id.answer1);
        checkQuestion11 = findViewById(R.id.q11);
        checkQuestion13 = findViewById(R.id.q13);
        checkQuestion21 = findViewById(R.id.q21);
        checkQuestion22 = findViewById(R.id.q22);
        checkQuestion23 = findViewById(R.id.q23);
        checkQuestion24 = findViewById(R.id.q24);
        checkQuestion3 = findViewById(R.id.answer3);
        checkQuestion32 = findViewById(R.id.q32);
        checkQuestion33 = findViewById(R.id.q33);
        checkQuestion41 = findViewById(R.id.q41);
        checkQuestion42 = findViewById(R.id.q42);
        checkQuestion43 = findViewById(R.id.q43);
        checkQuestion44 = findViewById(R.id.q44);
        checkQuestion51 = findViewById(R.id.q51);
        checkQuestion52 = findViewById(R.id.q52);
        checkQuestion53 = findViewById(R.id.q53);
        checkQuestion5 = findViewById(R.id.answer5);
        checkQuestion6 = findViewById(R.id.answer6);
        resultView = findViewById(R.id.result);
        detailView = findViewById(R.id.detail);
        isIni = true;
    }

    public void submit(View view) {
        if (!isIni) {
            ini();
        }
        boolean q1 = checkQuestion1.isChecked();
        boolean q11 = checkQuestion11.isChecked();
        boolean q13 = checkQuestion13.isChecked();
        if ((q1 || q11 || q13) && !check1) {
            checkQuestion11.setClickable(false);
            checkQuestion1.setClickable(false);
            checkQuestion13.setClickable(false);
            if (q1) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! 42 is known as the answer to life, universum and everything since the movie <<The Hitchhiker's Guide to the Galaxy>>",
                        Toast.LENGTH_LONG).show();
            }
            check1 = true;
        }
        boolean q21 = checkQuestion21.isChecked();
        boolean q22 = checkQuestion22.isChecked();
        boolean q23 = checkQuestion23.isChecked();
        boolean q24 = checkQuestion24.isChecked();
        boolean q2 = !q21 && q22 && q23 && q24;
        if ((q21 || q22 || q23 || q24) && !check2) {
            checkQuestion21.setClickable(false);
            checkQuestion22.setClickable(false);
            checkQuestion23.setClickable(false);
            checkQuestion24.setClickable(false);
            if (q2) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! C3PO(image 2), R2D2(image 3) and BB8(image 4) are from star wars, Marvin (image 1) is as know as <<the most depressive robot>>" +
                                " from the movie <<The Hitchhiker's Guide to the Galaxy>>",
                        Toast.LENGTH_LONG).show();
            }
            check2 = true;
        }
        boolean q3 = checkQuestion3.isChecked();
        boolean q32 = checkQuestion32.isChecked();
        boolean q33 = checkQuestion33.isChecked();
        if ((q3 || q32 || q33) && !check3) {
            checkQuestion3.setClickable(false);
            checkQuestion32.setClickable(false);
            checkQuestion33.setClickable(false);
            if (q3) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! <<127.0.0.1>> is known as <<local host>> or <<home address>> in computer network. Of course there is no place like <<home>>",
                        Toast.LENGTH_LONG).show();
            }
            check3 = true;
        }
        boolean q41 = ((CheckBox) findViewById(R.id.q41)).isChecked();
        boolean q42 = ((CheckBox) findViewById(R.id.q42)).isChecked();
        boolean q43 = ((CheckBox) findViewById(R.id.q43)).isChecked();
        boolean q44 = ((CheckBox) findViewById(R.id.q44)).isChecked();
        boolean q4 = q41 && q42 && q43 && q44;
        if ((q41 || q42 || q43 || q44) && !check4) {
            checkQuestion41.setClickable(false);
            checkQuestion42.setClickable(false);
            checkQuestion43.setClickable(false);
            checkQuestion44.setClickable(false);
            if (q4) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! Sorry for kidding you like this. Of course 1kg is always 1kg no matter in water or sugar or iron or gold=)))",
                        Toast.LENGTH_LONG).show();
            }
            check4 = true;
        }
        boolean q5 = checkQuestion5.isChecked();
        boolean q51 = checkQuestion51.isChecked();
        boolean q52 = checkQuestion52.isChecked();
        boolean q53 = checkQuestion53.isChecked();
        if ((q51 || q52 || q53 || q5) && !check5) {
            checkQuestion51.setClickable(false);
            checkQuestion52.setClickable(false);
            checkQuestion53.setClickable(false);
            checkQuestion5.setClickable(false);
            if (q5) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! One group knows binary and one doesn't. It should be 2, shouldn't it? And 2 in binary is <<10>>",
                        Toast.LENGTH_LONG).show();
            }
            check5 = true;
        }

        Editable name = checkQuestion6.getText();
        String nameLowerCase = name.toString().toLowerCase();
        boolean q6 = nameLowerCase.contains("wtf") || nameLowerCase.contains("whatthefuck") || nameLowerCase.contains("what the fuck");
        if (!name.toString().isEmpty() && !check6) {
            checkQuestion6.setFocusable(false);
            if (q6) {
                correctAnswer++;
                Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "IRCORRECT! Have you ever say <<What the fuck>> while reading someone's code." +
                                " When you read a code, and have to say <<WTF>> the whole time, that means it's not a good code." +
                                " Number of <<What the fuck>>/minute is known as a measurement for code style",
                        Toast.LENGTH_LONG).show();
            }
            check6 = true;
        }

    }

    public void getResult(View view) {
        if (!isIni) {
            ini();
        }
        String result = "";
        switch (correctAnswer) {
            case 0:
            case 1:
            case 2:
                result = "you are quite nerdy. Be nerdier";
                break;
            case 3:
            case 4:
                result = "wow you are very nerdy. Stay nerdy";
                break;
            case 5:
            case 6:
                result = "wow you are super nerdy. Stay like this. You are doing very well";
        }
        resultView.setText(result);
    }

    public void getDetail(View view) {
        if (!isIni) {
            ini();
        }
        String detail = "QUESTION 1: What is the answer to life, the universum and everything?" +
                "\nANSWER: 42 is known as the answer to life, universum and everything since the movie <<The Hitchhiker's Guide to the Galaxy>>" +
                "\n" +
                "\nQUESTION 2: Which droids are in the movie Star wars?(choose 1 or more)" +
                "\nANSWER: C3PO(image 2), R2D2(image 3) and BB8(image 4) are from star wars, Marvin (image 1) is as know as <<the most depressive robot>> from the movie <<The Hitchhiker's Guide to the Galaxy>>" +
                "\n" +
                "\nQUESTION 3: There is no place like ..." +
                "\nANSWER: <<127.0.0.1>> is known as <<local host>> or <<home address>> in computer network. Of course there is no place like <<home>>" +
                "\n" +
                "\nQUESTION 4: Which of those answers are not heavier than 1kg iron?(choose 1 or more)" +
                "\nANSWER: Sorry for kidding you like this. Of course 1kg is always 1kg no matter in water or sugar or iron or gold=)))" +
                "\n" +
                "\nQUESTION 5: There are ... types of people in the world; those who understand binary and those who don't" +
                "\nANSWER: One group knows binary and one doesn't. It should be 2, shouldn't it? And 2 in binary is <<10>>" +
                "\n" +
                "\nQUESTION 6: The only valid measurement of code quality is ..../minute" +
                "\nANSWER: Have you ever say <<What the fuck>> while reading someone's code." +
                " When you read a code, and have to say <<WTF>> the whole time, that means it's not a good code." +
                " Number of <<What the fuck>>/minute is known as a measurement for code style";
        detailView.setText(detail);
    }
}
