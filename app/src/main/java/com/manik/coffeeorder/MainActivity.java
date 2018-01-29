package com.manik.coffeeorder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int quantity = 0;
    //Order and reply
    Locale loc = new Locale("en", "in");

    //This method is called when the order button is clicked.
    public void submitOrder(View view) {

        CheckBox cream = (CheckBox) findViewById(R.id.wCream);
        boolean hascream = cream.isChecked();
        CheckBox choco = (CheckBox) findViewById(R.id.chocolate);
        boolean choc = choco.isChecked();

        int price = calculatePrice(hascream, choc);
        if (cusName().isEmpty()) {
        } else {
            String orderSummary = createOrderSummary(cusName(), price, hascream, choc);
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto: maniksingh10@gmail.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "Order for " + cusName());
            intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    public void orderSum(View view) {

        if (quantity == 0) {
            Toast.makeText(this, "Add Coffee", Toast.LENGTH_SHORT).show();
            return;
        }

        CheckBox cream = (CheckBox) findViewById(R.id.wCream);
        boolean hascream = cream.isChecked();
        CheckBox choco = (CheckBox) findViewById(R.id.chocolate);
        boolean choc = choco.isChecked();

        int price = calculatePrice(hascream, choc);
        String orderSummary = createOrderSummary(cusName(), price, hascream, choc);
        displayOrderSummary(orderSummary);
    }

    //Reset Button
    public void reset(View view) {
        quantity = 0;
        TextView orderS = (TextView) findViewById(R.id.orderS);
        orderS.setText("Reorder");
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
        displayQuantity(0);
        EditText etUserName = (EditText) findViewById(R.id.name);
        etUserName.setText(null);

    }

    //Price Calculate
    private int calculatePrice(boolean addCream, boolean addChoco) {
        int base = 110;
        if (addChoco) {
            base += 50;
        }
        if (addCream) {
            base += 30;
        }
        return base * quantity;
    }

    public String createOrderSummary(String name, int price, boolean creamAns, boolean chocoAns) {
        String orderSummar = "Name : " + name;
        orderSummar += " \n Whipped Cream is " +
                creamAns
                + " \n Choco is " + chocoAns + "\nQuantity: "
                + quantity + "\nTotal Price " +
                (NumberFormat.getCurrencyInstance(loc).format(price)) +
                "\nThank You!!";
        return orderSummar;
    }

    public void increment(View view) {
        if (quantity == 15) {
            Toast.makeText(this, "Too much coffee for one person", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);

    }

    public void decrement(View view) {

        if (quantity > 0) {
            quantity -= 1;
        } else {
            Toast.makeText(this, "Not possible", Toast.LENGTH_SHORT).show();
        }
        displayQuantity(quantity);
    }

    //This method displays the given quantity value on the screen.
    private void displayQuantity(int noOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + noOfCoffees);
    }

    private void displayOrderSummary(String message) {
        TextView orderS = (TextView) findViewById(R.id.orderS);
        orderS.setText(message);

    }


    public String cusName() {
        EditText etUserName = (EditText) findViewById(R.id.name);
        String strUserName = etUserName.getText().toString();
        if (TextUtils.isEmpty(strUserName)) {
            etUserName.setError("Enter Name");
        }
        return strUserName;

    }

}
