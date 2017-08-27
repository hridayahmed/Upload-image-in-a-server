package com.hridha.imageupload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button uploadbtn, choosebtn;
    private ImageView imgview;
    private EditText name;
    private final int IMG_REG = 1;
    private Bitmap bitmap;
    private String url = "http://websitebangladesh.com/android/imageapp/updateinfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadbtn = (Button) findViewById(R.id.uploadserver);
        choosebtn = (Button) findViewById(R.id.chooseimage);
        imgview = (ImageView) findViewById(R.id.imageview);
        name = (EditText) findViewById(R.id.imagtext);
        choosebtn.setOnClickListener(this);
        uploadbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.chooseimage:
                selectImage();
                break;

            case R.id.uploadserver:
                uploadImage();
                break;
        }
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==IMG_REG && resultCode==RESULT_OK && data!= null)
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imgview.setImageBitmap(bitmap);
                imgview.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(MainActivity.this,Response,Toast.LENGTH_LONG).show();

                    imgview.setImageResource(0);
                    imgview.setVisibility(View.GONE);
                    name.setText("");
                    name.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("name",name.getText().toString().trim());
                param.put("image",imagetoString(bitmap));

                return param;
            }
        };
        MySignleton.getmInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    private String imagetoString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgarr = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgarr,Base64.DEFAULT);
    }
}
