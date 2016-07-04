package com.softdesign.devintensive.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.softdesign.devintensive.utils.ContentManager;

/**
 * Created by roman on 27.06.16.
 */
public class BaseActivity extends AppCompatActivity{
    final String TAG = ContentManager.TAG_PREFIX + BaseActivity.class.getSimpleName();

    /**
     * Метод показывает ошибку в виде всплывающего сообщения {@link Toast}
     * @param message передаваемое пользователю сообщение
     * @param error ошибка
     */
    public void showError(String message, Exception error) {
        showToast(message);
        error.printStackTrace();
    }

    /**
     * Метод показывает сообщение пользователю в виде всплываюшео сообщения {@link Toast}
     * @param message сообщение
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
