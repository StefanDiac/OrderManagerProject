package com.example.air_book.ordermanagerproject.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.air_book.ordermanagerproject.Model.ItemType;
import com.example.air_book.ordermanagerproject.Model.Table;
import com.example.air_book.ordermanagerproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BarChartActivity extends AppCompatActivity {

    ArrayList<Table> tables = new ArrayList<>();
    SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myDB = openOrCreateDatabase("orders",MODE_PRIVATE,null);

        try {
            String myJson = PreferenceManager.getDefaultSharedPreferences(this).getString("TablesJson","");
            JSONObject tablesJson = new JSONObject(myJson);
            JSONArray tablesArrayJson = (JSONArray)tablesJson.get("Tables");

            for(int i = 0; i<tablesArrayJson.length();i++){
                JSONObject table = tablesArrayJson.getJSONObject(i);

                Table addTable = new Table(table.getInt("UID"),table.getInt("Seats"),table.getBoolean("Smoking"));
                tables.add(addTable);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> tablePopularity = new ArrayList<>();

        for(Table table : tables) {
            String talbeEntry = "Table Number " + table.getNumber();
            Cursor cursor = myDB.rawQuery("SELECT id FROM Orders WHERE TABLE_Number='" + talbeEntry+"'", null);

            int numberOfOrdersPerTable = 0;
            try{
                while(cursor.moveToNext()){
                    numberOfOrdersPerTable++;
                }
            }finally {
                cursor.close();
            }

            tablePopularity.add(numberOfOrdersPerTable);
        }

        ArrayList<Float> tablePopularityPercentage = turnToPercentage(tablePopularity);

        PlaceToDraw barChart = new PlaceToDraw(this, tablePopularityPercentage);
        setContentView(barChart);

    }

    class PlaceToDraw extends View {

        Context context;

        Paint barPaint;
        Paint gridPaint;
        Paint guidelinePaint;
        ArrayList<Float> tablePopularity;

        public PlaceToDraw(Context context, ArrayList<Float> tablePopularity) {
            super(context);
            this.context = context;

            this.tablePopularity = tablePopularity;

            barPaint = new Paint();
            barPaint.setStyle(Paint.Style.FILL);
            barPaint.setColor(Color.BLUE);

            gridPaint = new Paint();
            gridPaint.setStyle(Paint.Style.STROKE);
            gridPaint.setColor(Color.BLACK);
            gridPaint.setStrokeWidth(5);

            guidelinePaint = new Paint();
            guidelinePaint.setStyle(Paint.Style.STROKE);
            guidelinePaint.setColor(Color.GRAY);
            guidelinePaint.setStrokeWidth(2);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            final int height = getHeight();
            final int width = getWidth();
            final float gridLeft = 20;
            final float gridBottom = height - gridLeft;
            final float gridTop = gridLeft;
            final float gridRight = width - gridLeft;

            canvas.drawLine(gridLeft,gridBottom,gridLeft,gridTop,gridPaint);
            canvas.drawLine(gridLeft,gridBottom,gridRight,gridBottom,gridPaint);

            float guideLSpacing = (gridBottom-gridTop)/10f;
            float y;
            for(int i=0;i<10;i++){
                y = gridTop+i*guideLSpacing;
                canvas.drawLine(gridLeft,y,gridRight,y,guidelinePaint);
            }

            float totalColumnSpacing = 5*(tablePopularity.size());
            float columnWidth = (gridRight-gridLeft-totalColumnSpacing)/tablePopularity.size();
            float columnLeft = gridLeft + 2;
            float columnRight = columnLeft + columnWidth;

            for(float percentage: tablePopularity) {
                float top = gridTop + (gridBottom-gridTop) * (1f-percentage);
                canvas.drawRect(columnLeft,top,columnRight,gridBottom,barPaint);

                columnLeft = columnRight + 2;
                columnRight = columnLeft + columnWidth;
            }
        }
    }

    private ArrayList<Float> turnToPercentage(ArrayList<Integer> tablePopularity) {
        ArrayList<Float> tablePopPercentage = new ArrayList<>();

        float totalVisits = 0;
        for(Integer perTablePopularity : tablePopularity) {
            totalVisits = perTablePopularity + totalVisits;
        }

        for(Integer perTablePopularity: tablePopularity) {
            float percentage = perTablePopularity/totalVisits;
            tablePopPercentage.add(percentage);
        }

        return tablePopPercentage;
    }
}
