package com.example.graduatedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText usernameText;

    private EditText passwordText;

    private Button loginButton;

    private Button registerButton;

    private List<User> userList = new ArrayList<>();

    private String LOG_TAG_PREFIX = "注册日志: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 绑定输入框、按钮
        init();
        loginButton.setOnClickListener(view -> onClick1(loginButton));
        registerButton.setOnClickListener(view -> onClick1(registerButton));
    }

    private void init() {
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
    }

    public void onClick1(View view) {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        switch (view.getId()) {
            case R.id.loginButton:
                boolean usernameExist = false;
                boolean passwordCorrect = false;
                for (User user : userList) {
                    if (user.getUsername().equals(username)) {
                        usernameExist = true;
                        if (user.getPassword().equals(password)) {
                            passwordCorrect = true;
                            break;
                        }
                    }
                }
                if (usernameExist && passwordCorrect) {
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                } else {
                    if (!usernameExist) {
                        Toast.makeText(this, "用户不存在!", Toast.LENGTH_SHORT).show();
                        usernameText.setText("");
                    } else {
                        Toast.makeText(this, "输入密码不正确!", Toast.LENGTH_SHORT).show();
                        passwordText.setText("");
                    }
                }
                break;
            case R.id.registerButton:
                if (username.length() < 5) {
                    Toast.makeText(this, "用户名长度不能小于5", Toast.LENGTH_LONG).show();
                    return;
                }

                if (password.length() < 5) {
                    Toast.makeText(this, "密码长度不能小于5", Toast.LENGTH_LONG).show();
                    return;
                }

                for (User user : userList) {
                    String name1 = user.getUsername();
                    if (name1.equals(username)) {
                        Toast.makeText(this, "当前用户已存在!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                Log.i(LOG_TAG_PREFIX, "用户名: " + username);
                Log.i(LOG_TAG_PREFIX, "密码: " + password);

                userList.add(user);

                Toast.makeText(this, "注册成功!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
