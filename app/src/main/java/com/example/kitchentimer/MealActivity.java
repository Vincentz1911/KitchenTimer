package com.example.kitchentimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class MealActivity extends AppCompatActivity {

    String TAG = "Meal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        EditText search = (EditText) findViewById(R.id.et_search);
        ((ImageButton) findViewById(R.id.btn_search)).setOnClickListener(v -> requestApi(search));

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                requestApi(search);
                return true;
            }
            return false;
        });
    }

    void showMeals(List<Meal> meals) {
        Spinner spMeals = findViewById(R.id.sp_meals);
        List<String> names = meals.stream().map(user -> user.strMeal).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, names);
        spMeals.setAdapter(adapter);
        spMeals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showMeal(meals.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void showMeal(Meal meal) {
        if (meal != null) {
            ImageView img = ((ImageView) findViewById(R.id.img_meal));
            Picasso.get().load(meal.strMealThumb).into(img);
            ((TextView) findViewById(R.id.tv_recipe)).setText(meal.strInstructions);
        }
    }

    void requestApi(EditText et) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.clearFocus();
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=" + et.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            MealRoot data = new Gson().fromJson(response, MealRoot.class);
            if (data != null) showMeals(data.meals);
        }, error -> Log.i(TAG, "Error :" + error.toString()));

        requestQueue.add(stringRequest);
    }
}

