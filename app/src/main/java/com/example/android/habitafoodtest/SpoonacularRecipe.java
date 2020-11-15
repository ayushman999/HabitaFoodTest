package com.example.android.habitafoodtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SpoonacularRecipe extends AppCompatActivity {
    private static RecyclerView recyclerView;
    Button getClarifai;
    static ArrayList<Elements> item;
    static ArrayList<String> data;
    static ArrayList<SElements> recipes;
    byte[] byteArray;
    static SpoonacularAdapter mAdapter;
    private static final String baseURL="https://api.spoonacular.com/recipes/findByIngredients?ingredients=";
    private static final String KEY="&apiKey=5014df46d71648219de5bada69aa5ded";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_recipe);
        recyclerView=(RecyclerView) findViewById(R.id.prediction_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        byteArray=getIntent().getByteArrayExtra("byteArray");
        getClarifai=(Button) findViewById(R.id.getClarifai);
        item=new ArrayList<>();
        ClarifaiAsync task=new ClarifaiAsync();
        task.execute();

        getClarifai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(SpoonacularRecipe.this,Prediction.class);
                startActivity(transfer);
            }
        });

    }

    private ArrayList<String> fetchCData(ArrayList<Elements> cData) {
        ArrayList<String> data=new ArrayList<>();
        for(Elements current: cData)
        {
            data.add(current.getItem());
        }
        return data;
    }
    private class ClarifaiAsync extends AsyncTask<Void,Void,ArrayList<Elements>>
    {
        @Override
        protected void onPostExecute(ArrayList<Elements> elements) {
            super.onPostExecute(elements);
            data=fetchCData(item);
            Toast.makeText(SpoonacularRecipe.this,"Ingredients found!",Toast.LENGTH_SHORT).show();
            SpoonacularAsync spoonacularAsync=new SpoonacularAsync();
            spoonacularAsync.execute(data);
        }

        @Override
        protected ArrayList<Elements> doInBackground(Void... voids) {
            ClarifaiUtils.fetchData(byteArray,SpoonacularRecipe.this);
            while(item.size()<1) {
                item = ClarifaiUtils.getData();
            }
            Log.i("spoon clarifai background:","data size:"+item.size());
            return item;
        }
    }
    private class SpoonacularAsync extends AsyncTask<ArrayList<String>,Void,ArrayList<SElements>>
    {
        @Override
        protected void onPostExecute(ArrayList<SElements> sElements) {
            super.onPostExecute(sElements);
            updateData(sElements);
        }

        @Override
        protected ArrayList<SElements> doInBackground(ArrayList<String>... arrayLists) {
            recipes=fetchData(arrayLists[0],SpoonacularRecipe.this);
            return recipes;
        }
    }
    public void updateData(ArrayList<SElements> data)
    {
        mAdapter=new SpoonacularAdapter(data,getApplicationContext());
        recyclerView.setAdapter(mAdapter);

    }
    private static String getfinalURL(ArrayList<String> ing_list) {
        String ing = "";
        for (int i = 0; i < ing_list.size(); i++) {
            ing = ing + ing_list.get(i).toString() + ",+";
        }
        return ing;
    }
    public ArrayList<SElements> fetchData(ArrayList<String> arrayList, Context context) {
        String url =baseURL+getfinalURL(arrayList)+KEY;
        String url1="https://www.google.com";
        recipes=new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url2 ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String title="";
                        String img_url="";
                        try {
                            Log.i("Inside"," onResponse");
                            for(int i=0;i<response.length();i++) {
                                JSONObject object = response.getJSONObject(i);
                                title = object.getString("title");
                                img_url = object.getString("image");
                                JSONArray missedIngredients=object.getJSONArray("missedIngredients");
                                JSONArray usedIngredients=object.getJSONArray("usedIngredients");
                                ArrayList<String> missed=new ArrayList<>();
                                ArrayList<String> used=new ArrayList<>();
                                for (int j = 0; j < missedIngredients.length(); j++) {
                                    JSONObject current_ing = missedIngredients.getJSONObject(j);
                                    String ing = current_ing.getString("name");
                                    missed.add(ing);
                                }
                                for (int j = 0; j < usedIngredients.length(); j++) {
                                    JSONObject current_ing = usedIngredients.getJSONObject(j);
                                    String ing = current_ing.getString("name");
                                    used.add(ing);
                                }
                                recipes.add(new SElements(img_url, title, missed, used));
                                Log.i("Added: ", "Element");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("onResponse:","eneterd,url="+url+"title:"+title+" img url:"+img_url);
                        updateData(recipes);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        return recipes;
    }


}