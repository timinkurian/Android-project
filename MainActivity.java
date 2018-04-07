package com.example.timin.mcanotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
Spinner sp1,sp2;
    String courseval,semval;
    public static final String PDF_FETCH_URL = "https://conserving-gravel.000webhostapp.com/misc/timin/viewAll.php";
    PdfAdapter pdfAdapter;
    //ListView to show the fetched Pdfs from the server
    ListView listView;

    //button to fetch the intiate the fetching of pdfs.
    Button buttonFetch;

    //Progress bar to check the progress of obtaining pdfs
    ProgressDialog progressDialog;

    //an array to hold the different pdf objects
    ArrayList<Pdf> pdfList = new ArrayList<Pdf>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp2= (Spinner) findViewById(R.id.sp2);
        sp1= (Spinner) findViewById(R.id.sp1);
        buttonFetch=(Button) findViewById(R.id.buttonFetchPdf) ;
        listView=(ListView)findViewById(R.id.listView);
        progressDialog=new ProgressDialog(MainActivity.this);

        String[] arraySpinner = new String[] {
                "S1", "S2", "S3", "S4", "S5"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()){
                    case "S1":
                        semval="S1";
                        String[] arraySpin = new String[] {
                                "RLMCA101","RLMCA103","RLMCA105","RLMCA107","RLMCA109"
                        };
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, arraySpin);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter1);
                        break;
                    case "S2":
                        semval="S2";
                        String[] arraySpin1 = new String[] {
                                "RLMCA102","RLMCA104","RLMCA106","RLMCA108","RLMCA112"
                        };
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, arraySpin1);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter2);
                        break;
                    case "S3":
                        semval="S3";
                        String[] arraySpin3 = new String[] {
                                "RLMCA201","RLMCA203","RLMCA205","RLMCA207","RLMCA209"
                        };
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, arraySpin3);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter3);
                        break;

                    case "S4":
                        semval="S4";
                        String[] arraySpin4 = new String[] {
                                "RLMCA202","RLMCA204","RLMCA206","RLMCA208","RLMCA266"
                        };
                        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, arraySpin4);
                        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter4);
                        break;
                    case "S5":
                        semval="S5";
                        String[] arraySpin5 = new String[] {
                                "RLMCA101","RLMCA103","RLMCA105","RLMCA107","RLMCA109"
                        };
                        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, arraySpin5);
                        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp2.setAdapter(adapter5);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                courseval= String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPdfs();
            }
        });
        //setting listView on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Pdf pdf = (Pdf) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(pdf.getUrl()));
                startActivity(intent);

            }
        });
    }
    private void getPdfs() {

        progressDialog.setMessage("Fetching Pdfs... Please Wait");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        pdfList.clear();
                        try {
                            JSONArray obj = new JSONArray(response);
                            /*Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("pdfs");*/

                            for (int i = 0; i < obj.length(); i++) {

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = obj.getJSONObject(i);
                                //Declaring a Pdf object to add it to the ArrayList  pdfList
                                Pdf pdf = new Pdf();
                                String pdfCategory = jsonObject.getString("category");
                                String pdfName = jsonObject.getString("name");
                                String pdfUrl = "https://conserving-gravel.000webhostapp.com/misc/timin/"+jsonObject.getString("path");

                                if (pdfName.equalsIgnoreCase(semval)){
                                    if (pdfCategory.equalsIgnoreCase(courseval)){
                                        pdf.setName(pdfName);
                                        pdf.setUrl(pdfUrl);
                                        pdf.setCategory(pdfCategory);
                                        pdfList.add(pdf);

                                    }
                                }

                            }

                            pdfAdapter = new PdfAdapter(MainActivity.this, R.layout.list_layout, pdfList);

                            listView.setAdapter(pdfAdapter);

                            pdfAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        );

        RequestQueue request = Volley.newRequestQueue(this);
        request.add(stringRequest);

    }

}

