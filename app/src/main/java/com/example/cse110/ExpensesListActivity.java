package com.example.cse110;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpensesListActivity extends AppCompatActivity {
    public static final String MONTHLY_DATA_INTENT = "ExpenseListActivity monthlyData";
    public static final String CATEGORY_NAME_INTENT = "ExpenseListActivity categoryName";
    private static  final int MAX_EXPENSE_VALUE = 9999999;
    //Our max allowable int is 9,999,999 which is 7 place values
    private static final int MAX_BUDGET =  7;
    private EditText expenseName, expenseCost;
    private EditText categoryBudget;
    //List Structure
    private ExpenseListAdapter expenseAdapter;

    private MonthlyData monthlyData;
    private Category category;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent = getIntent();
        monthlyData = intent.getParcelableExtra(MONTHLY_DATA_INTENT);
        String categoryNameFromParent = intent.getStringExtra(CATEGORY_NAME_INTENT);
        category = monthlyData.getCategory(categoryNameFromParent);

        //Toolbar categoryToolBar = findViewById(R.id.categoryBar);
        //setActionBar(categoryToolBar);

        //textViews in the top bar
        TextView categoryName = findViewById(R.id.category_name);
        categoryName.setText(categoryNameFromParent);

        categoryBudget = findViewById((R.id.budget_display));
        categoryBudget.setText("$" + formatIntMoneyString(category.getBudgetAsString()));

        // Bind element from XML file
        expenseName = findViewById(R.id.expense_name);
        expenseCost = findViewById(R.id.expense_cost);
        Button btnAdd = findViewById(R.id.AddToList);

        // Initialize List
        final ArrayList<Expense> arrayOfItems = category.getExpenses();
        expenseAdapter = new ExpenseListAdapter(this, arrayOfItems, category);
        ListView expensesList = findViewById(R.id.Categories);
        expensesList.setAdapter(expenseAdapter);

        //Detect User Changes
        categoryBudget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && categoryBudget.getText().toString() != null) {

                    if (categoryBudget.getText().toString().length() > MAX_BUDGET) {
                        Toast.makeText(getBaseContext(), "A category cannot have a budget greater than $9,999,999.", Toast.LENGTH_LONG).show();

                    } else if (categoryBudget.getText().toString().isEmpty()) {

                        //In the case of of an empty string
                        categoryBudget.setText("$" + formatIntMoneyString(category.getBudgetAsString()));
                    } else {



                        //Ensure valid input
                        try {

                            // Create new item and update adapter
                            category.setBudget(Integer.parseInt(categoryBudget.getText().toString()));
                            categoryBudget.setText("$" + formatIntMoneyString(category.getBudgetAsString()));
                            Toast.makeText(getBaseContext(), "Category budget has been successfully updated", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Invalid input", Toast.LENGTH_LONG).show();

                        }



                    }
                }else {
                   categoryBudget.getText().clear();
                }
            }
        });

        // Set Event Handler to add items to the list
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Date Object
                Calendar today = Calendar.getInstance();

                // Ensure that both fields are filled.
                if(!expenseCost.getText().toString().isEmpty() && !expenseName.getText().toString().isEmpty() ) {

                    //Check that we do not go over the max allowed expense
                    try {
                        if(Double.parseDouble(expenseCost.getText().toString()) > MAX_EXPENSE_VALUE)
                            throw new Exception();

                        // Create new item and update adapter
                        category.createExpense(expenseName.getText().toString(), Double.parseDouble(expenseCost.getText().toString()), today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                        expenseName.getText().clear();
                        expenseCost.getText().clear();
                        expenseAdapter.notifyDataSetChanged();
                    }catch (Exception overflow){
                            Toast.makeText(getBaseContext(), "The max expense value is $9,999,999", Toast.LENGTH_LONG).show();
                    }


                } else {
                    // Insufficient number of filled fields results in an error warning.
                    Toast missingInformationWarning = Toast.makeText(getBaseContext(), "Missing Information", Toast.LENGTH_SHORT);
                    missingInformationWarning.show();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                monthlyData = data.getParcelableExtra(ExpensesListActivity.MONTHLY_DATA_INTENT);
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MONTHLY_DATA_INTENT, monthlyData);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private String formatIntMoneyString(String valueToFormat){
        int hundredthComma = valueToFormat.length() - 3;
        int thousandthComma = valueToFormat.length() - 6;

        if (valueToFormat.length() <= 3){
            return  valueToFormat;
        }else if (valueToFormat.length() <= 6){
            return valueToFormat.substring(0, hundredthComma) + "," + valueToFormat.substring(hundredthComma);
        }
        return valueToFormat.substring(0, thousandthComma) + "," + valueToFormat.substring(thousandthComma , hundredthComma) + "," + valueToFormat.substring(hundredthComma );
    }

    //Handle pressing away from setting category
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
