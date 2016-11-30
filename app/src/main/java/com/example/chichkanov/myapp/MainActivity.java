package com.example.chichkanov.myapp;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    //BUG WHEN FIRST * in sin cos tg  /
    //ADD <- WORK
    //  div 0 exception!! +++++
    //fix landscape teaxtview  +++++  FIX BUTTONS WORK

    TextView matexp, result;
    String exp = "";
    String currentOperator = "";
    String res = "";
    String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setBackground();
        result = (TextView) findViewById(R.id.result);
        matexp = (TextView) findViewById(R.id.matexp);
        Locale.setDefault(new Locale("en", "US"));
    }

    private void RunAnimation()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.clear);
        matexp.startAnimation(anim);
        result.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateScreen();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void setBackground(){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Bitmap bm = BruceUtils.convertToBitmap(wallpaperDrawable, size.y, size.x);

        LinearLayout ll = (LinearLayout) findViewById(R.id.mainback);//Substitute with your layout

        Bitmap blurredBitmap = BruceUtils.blur(this, bm);
        Drawable d = new BitmapDrawable(getResources(), blurredBitmap);
        ll.setBackground(d);
    }

    public void updateScreen(){
        matexp.setText(exp);
        result.setText(res);
    }

    public void updateAnswer(){
        DecimalFormat format;
        Double d = BruceUtils.eval(exp);

            String s[] = d.toString().split("[.]");
            if (s[1].length() <= 1) {
                format = new DecimalFormat("0.#");
                res = format.format(d).toString();
            } else res = d.toString();

    }

    public void printDivError(){
        currentOperator = "";
        exp = "Error";
        res = "";
        state = "Error";
        updateScreen();
    }

    public void onClickNumber(View v){
        if(state == "Error")clear();
        Button button = (Button) v;
        exp += button.getText();
        if(!currentOperator.equals("") && !currentOperator.equals(".")){
            updateAnswer();
        }
        updateScreen();
    }

    public void onClickOperator(View v){
        Button button = (Button)v;
        exp += button.getText();
        currentOperator = button.getText().toString();
        updateScreen();
    }

    public void onClickEqual(View v){
        if(res != "") {
            exp = res;
            res = "";
            updateScreen();
        }
    }

    public void clear(){
        if(state != "Error")RunAnimation();
        currentOperator = "";
        exp = "";
        res = "";
        state = "";
    }

    public void onClickClear(View v){
        clear();
    }

    public void onClickBackspace(View v){
        if(exp.length()>0) {
            StringBuilder sb = new StringBuilder(exp);

            sb.deleteCharAt(sb.length() - 1);
            exp = sb.toString();
            updateScreen();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings: Toast.makeText(this, "Settings pressede!", Toast.LENGTH_SHORT).show(); break;
            case R.id.action_about: Toast.makeText(this, "About pressed!", Toast.LENGTH_SHORT).show(); break;
        }
        return super.onOptionsItemSelected(item);
    }
}
