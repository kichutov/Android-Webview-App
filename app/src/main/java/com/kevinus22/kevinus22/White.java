package com.kevinus22.kevinus22;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class White extends AppCompatActivity {

    TextView textView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white);

        // В переменную textView заносим ссылку на компонент textView
        textView = (TextView) findViewById(R.id.textView);
        // Скрываем textView при старте приложения
        textView.setVisibility(View.GONE);
    }

    // При клике на кнопку Войти - показывает textView
    public void login(View view) {
        textView.setVisibility(View.VISIBLE);
    }

}