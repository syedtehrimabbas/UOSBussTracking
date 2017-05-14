package com.glowingsoft.uosbusstracking.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.BoolRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.uosbusstracking.MainActivity;
import com.glowingsoft.uosbusstracking.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Signup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //region strings
    String nameST, phnoST,rollNo ,routeST,nearBusStop;
    private CoordinatorLayout signupLayout;
    private EditText nameET, phnoET, roll_noET;
    private Button signupBT;
    private Spinner routeSP;
    private ProgressDialog progress;
    private Global global;
    ArrayList<String> routes=new ArrayList<String>();
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        global=new Global(getApplicationContext());


        Global.sharedPreferences= getSharedPreferences(Global.KEYNAME,MODE_PRIVATE);
        Global.editor=Global.sharedPreferences.edit();
        if(Global.sharedPreferences.getString(Global.KEY_LOGIN,"notSignIn").contains("signIn"))
        {
            startActivity(new Intent(Signup.this,MainActivity.class));
            finish();
        }
        getViews();
        addRoutes();
        //region spinner
        ArrayAdapter<String> animationAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, routes);
        animationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        routeSP.setAdapter(animationAdapter);
        routeSP.setSelection(0);
        routeSP.setOnItemSelectedListener(this);
        //endregion
        signupBT.setOnClickListener(this);
    }

    void getViews() {
        nameET = (EditText) findViewById(R.id.regname);
        phnoET = (EditText) findViewById(R.id.regPhno);
        signupBT = (Button) findViewById(R.id.register);
        signupLayout= (CoordinatorLayout) findViewById(R.id.signupCoordinat);
        routeSP= (Spinner) findViewById(R.id.routeSP);
        roll_noET= (EditText) findViewById(R.id.reg_roll_no);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case  R.id.register:
                //region request
                nameST = nameET.getText().toString().trim();
                phnoST = phnoET.getText().toString().trim();
                rollNo=roll_noET.getText().toString().trim();
                if(isValidData())
                {
                    if(Global.isOnline()) {
                        signup_request();
                        setProgressBar();
                    }
                    else
                    {
                        global.showSnack("No Internet Connection",signupLayout);
                    }

                }
                else {
                    global.showSnack( "Invalid Data",signupLayout);
                }
                break;
                //endregion

        }
    }

    void signup_request() {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("name", nameST);
        paramMap.put("phone", phnoST);
        paramMap.put("route", routeST);
        paramMap.put("roll_no", rollNo);
        paramMap.put("near_stop", "");
        paramMap.put("block", "0");
        RequestParams params = new RequestParams(paramMap);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Global.IP + Global.base_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                Log.d("response: ", response.toString());
                try {
                   // JSONObject message=response.getJSONObject("error_msg");
                    boolean error=response.getBoolean("error");
                    if (!error) {
                        progress.dismiss();
                        global.showSnack("You are successfully registered",signupLayout); /////after user exist move to sign in
                        goToLogin();
                    } else if (error) {
                        progress.dismiss();
                        global.showSnack("Already Registerd Sign in please!",signupLayout);    //// if user exist then go to signin activity
                        goToLogin();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            public void onFailure(int statusCode, cz.msebera.android.httpclient.entity.mime.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("response: ", errorResponse.toString());
                global.showSnack("Please vheck your internet conncetion",signupLayout);
                progress.dismiss();
            }
        });
    }
    /////SET PROGRESS DIALOG
    public void setProgressBar() {
        progress = ProgressDialog.show(this, "Signing up","please wait....", true);
    }

    void goToLogin()
    {
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(Signup.this, MainActivity.class));
                Global.editor.putString(Global.KEY_LOGIN,"signIn");
                finish();
            }
        },2000);
    }
    boolean isValidData( )
    {
        if(
                global.isValidPhone(phnoST)
                && phnoST.length()>=11
                && phnoST.length()<=13
                && nameST.length()>0
                && rollNo.length()==12
                && routeST.length()>0
                ){
            return true;
        }
        else {
            return false;
        }
    }
    public void addRoutes()
    {
        routes.add("Thokar");
        routes.add("Mughal Pura");
        routes.add("Shahdara");
        routes.add("Bagh e Janah");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        routeST= routes.get(position);
        global.showSnack(routeST,signupLayout);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

