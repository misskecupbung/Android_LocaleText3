/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.localetext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private int mInputQuantity = 1;

    // TODO: Get the number format for this locale.

    private double mPrice = 0.10;

    double mIdExchangeRate = 14000;

    double mFrExchangeRate = 0.93; // 0.93 euros = $1.
    double mIwExchangeRate = 3.61; // 3.61 new shekels = $1.

    // TODO: Get locale's currency.
    private final NumberFormat mNumberFormat = NumberFormat.getInstance();
    private NumberFormat mCurrencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelp();
            }
        });

        final Date myDate = new Date();
        final long expirationDate = myDate.getTime() + TimeUnit.DAYS.toMillis(5);
        myDate.setTime(expirationDate);

        // TODO: Format the date for the locale.
        String myFormattedDate = DateFormat.getDateInstance().format(myDate);
        TextView expDateView = findViewById(R.id.date);
        expDateView.setText(myFormattedDate);

        // TODO: Apply the exchange rate and calculate the price.
        final String myFormattedPrice;
        String deviceLocale = Locale.getDefault().getCountry();
        if (deviceLocale.equals("ID")) {
            mPrice *= mIdExchangeRate;
            myFormattedPrice = mCurrencyFormat.format(mPrice);
        } else {
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            myFormattedPrice = mCurrencyFormat.format(mPrice);
        }

        // TODO: Show the price string.
        TextView localePrice = findViewById(R.id.price);
        localePrice.setText(myFormattedPrice);

        final TextView totalPrice = findViewById(R.id.total);

        final EditText enteredQuantity = findViewById(R.id.quantity);
        enteredQuantity.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService
                            (Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    if (v.toString().equals("")) {
                    } else {
                        try {
                            mInputQuantity = mNumberFormat.parse(v.getText().toString()).intValue();
                            v.setError(null);
                            String myFormattedQuantity = mNumberFormat.format(mInputQuantity);
                            v.setText(myFormattedQuantity);
                            double total = mInputQuantity * mPrice;
                            String myFormattedTotalPrice = mCurrencyFormat.format(total);
                            totalPrice.setText(myFormattedTotalPrice);
                        } catch (ParseException e) {
                            v.setError(getText(R.string.enter_number));
                            e.printStackTrace();
                        }

                        // TODO: Parse string in view v to a number.

                        // TODO: Convert to string using locale's number format.

                        // TODO: Homework: Calculate the total amount from price and quantity.

                        // TODO: Homework: Use currency format for France (FR) or Israel (IL).

                        // TODO: Homework: Show the total amount string.

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showHelp() {
        Intent helpIntent = new Intent(this, HelpActivity.class);
        startActivity(helpIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((EditText) findViewById(R.id.quantity)).getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle options menu item clicks here.
        int itemId = item.getItemId();// Do nothing
        if (itemId == R.id.action_help) {
            Intent helpIntent = new Intent(this, HelpActivity.class);
            startActivity(helpIntent);
            return true;
        } else if (itemId == R.id.action_language) {
            Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(languageIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
