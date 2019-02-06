package com.example.adeeliftikhar.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _screen = findViewById(R.id.edit_text_result);
        _screen.setText(display);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        getSupportActionBar();
    }

    private void updateScreen() {
        _screen.setText(display);
    }

    public void onClickNumber(View view) {
        if (result != "") {
            clear();
            updateScreen();
        }
        Button button = (Button) view;
        display += button.getText();
        updateScreen();
    }

    private boolean isOperator(char op) {
        switch (op) {
            case '+':
                return true;
            case '-':
                return true;
            case '*':
                return true;
            case '%':
                return true;
            case '/':
                return true;
            default:
                return false;
        }
    }

    public void onClickOperator(View view) {
        if (display == "") return;
        Button button = (Button) view;

        if (result != "") {
            String _display = result;
            clear();
            display = _display;
        }

        if (currentOperator != "") {
            Log.d("Calc", ""+display.charAt(display.length()-1));
            if (isOperator(display.charAt(display.length() - 1))){
                display = display.replace(display.charAt(display.length() - 1), button.getText().charAt(0));
                updateScreen();
                return;
            } else {
                getResult();
                display = result;
                result = "";
            }
            currentOperator = button.getText().toString();
        }
        display += button.getText();
        currentOperator = button.getText().toString();
        updateScreen();
    }

    private void clear() {
        display = "";
        currentOperator = "";
        result = "";
    }

    public void onClickClear(View view) {
        clear();
        updateScreen();
    }

    public double operate(String a, String b, String op) {
        switch (op) {
            case "+":
                return Double.valueOf(a) + Double.valueOf(b);
            case "-":
                return Double.valueOf(a) - Double.valueOf(b);
            case "*":
                return Double.valueOf(a) * Double.valueOf(b);
            case "%":
                return Double.valueOf(a) % Double.valueOf(b);
            case "/":
                try {
                    return Double.valueOf(a) / Double.valueOf(b);
                } catch (Exception e) {
                    Log.d("Calc", e.getMessage());
                }
            default:
                return -1;
        }
    }

    private boolean getResult() {
        if(currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentOperator));
        return true;
    }

    public void onClickEqual(View view) {
        if(display == "") return;
        if (!getResult()) return;
        _screen.setText(display + "\n" + String.valueOf(result));
    }
}
