package com.example.android.justjave;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private int numberOfCoffee=1;
    private String toppings="";
    private int price=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String result=createOrderSummary(calculate(), numberOfCoffee);
        //displayPrice(result);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"udaciy@gmail.com"});
        Editable name= ((EditText) findViewById(R.id.name)).getText();
        intent.putExtra(Intent.EXTRA_SUBJECT,"Just Java of "+name);
        intent.putExtra(Intent.EXTRA_TEXT,result);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivity(intent);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(""+number);
    }
    private void displayPrice(String priceMessage) {
        TextView priceTextView = (TextView) findViewById(R.id.oder_summary_text_view);
        priceTextView.setText(priceMessage);
    }
    int calculate(){
        return numberOfCoffee*price;
    }
    String createOrderSummary(int price, int quantity){
        Editable name= ((EditText) findViewById(R.id.name)).getText();
        String result=getString(R.string.customer)+name+"\n";
        if(!toppings.equals("")){
            result+=toppings;
        }
        result+=getString(R.string.quantity)+quantity+"$\n";
        result+=getString(R.string.price_total)+ price+"\n";
        result+=getString(R.string.thank_you)+"\n";
        return  result;
    }

    public void increase(View view) {
        if(numberOfCoffee==100){
            numberOfCoffee=100;
            Toast.makeText(this,R.string.max,Toast.LENGTH_SHORT).show();
            return;
        }else{
            numberOfCoffee++;
        }
        //numberOfCoffee=numberOfCoffee==100? 100: numberOfCoffee+1;
        display(numberOfCoffee);
    }

    public void decrease(View view) {
        if(numberOfCoffee==1){
            numberOfCoffee=1;
            Toast.makeText(this,R.string.min,Toast.LENGTH_SHORT).show();
            return;
        }else{
            numberOfCoffee--;
        }
        //numberOfCoffee=numberOfCoffee==1? 1: numberOfCoffee-1;
        display(numberOfCoffee);
    }

    public void onClick(View view) {
        boolean checked1 =((CheckBox) findViewById(R.id.iceCream)).isChecked();
        boolean checked2 =((CheckBox) findViewById(R.id.chocolate)).isChecked();
        if(checked1 || checked2){
            toppings=getString(R.string.toppings)+":\n";
        }else{
            toppings="";
        }
        String iceCream="";
        String chocolate="";
        if(checked1) {
            iceCream="\t"+getString(R.string.ice_cream)+"1$"+"\n";
            price+=1;
        }
        if(checked2){
            chocolate="\t"+getString(R.string.chocolate)+"2$"+"\n";
            price+=2;
        }
        toppings+=iceCream;
        toppings+=chocolate;
    }
}
