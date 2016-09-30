package com.example.lmont.iceicebb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.lmont.iceicebb.TabMainActivity.isByAlphabetQuery;
import static com.example.lmont.iceicebb.TabMainActivity.isByRatingQuery;
import static com.example.lmont.iceicebb.TabMainActivity.isCleanQuery;
import static com.example.lmont.iceicebb.TabMainActivity.query;
import static com.example.lmont.iceicebb.TabMainActivity.tagsQuery;

public class GameDetailActivity extends AppCompatActivity {

    IcebreakerDBHelper dbHelper = IcebreakerDBHelper.getInstance(this);
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        String name = getIntent().getStringExtra("name");
        game = dbHelper.getGameWithName(name);


        LinearLayout titleBar = (LinearLayout) findViewById(R.id.gameNameLayout);
        TextView gameName = (TextView)findViewById(R.id.gameNameDetail);
        TextView playerCount = (TextView)findViewById(R.id.playerCount);
        ImageView sfwIconAngel = (ImageView)findViewById(R.id.sfwIconAngel);
        ImageView sfwIconDevil = (ImageView)findViewById(R.id.sfwIconDevil);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBarDetail);
        TextView gameMaterials = (TextView)findViewById(R.id.gameMaterials);
        TextView gameRules = (TextView)findViewById(R.id.gameRules);
        TextView gameComments = (TextView)findViewById(R.id.gameComments);
        Button diceRollerButton = (Button)findViewById(R.id.diceRollerButton);
        final Button cardDeckButton = (Button)findViewById(R.id.cardFlipperButton);

        LinearLayout tagHolder = (LinearLayout) findViewById(R.id.tagFrameDetail);
        ImageView drinkIcon = (ImageView) findViewById(R.id.drinkIcon);
        ImageView movingIcon = (ImageView) findViewById(R.id.movingIcon);
        ImageView carIcon = (ImageView) findViewById(R.id.carIcon);
        final ImageView paperIcon = (ImageView) findViewById(R.id.paperIcon);

        float rating = (float) (game.rating)/2;


        gameName.setText(game.name);
        gameName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameDetailActivity.this, WebViewActivity.class);
                intent.putExtra("VIDEO", game.url);
                intent.putExtra("NAME", game.name);
                startActivity(intent);
            }
        });
        gameMaterials.setText("Required Materials: "+game.materials);
        gameRules.setText("Rules: \n"+game.rules);
        gameComments.setText("Comments\n"+game.comment);
        ratingBar.setRating(rating);
        playerCount.setText(game.minPlayers+"-"+game.maxPlayers);


        if (game.hasCards){
            cardDeckButton.setVisibility(View.VISIBLE);
        }
        if (game.hasDice){
            diceRollerButton.setVisibility(View.VISIBLE);
        }

//ONCLICKS to send user to appropriate tool activity
        diceRollerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DiceActivity.class);
                startActivity(intent);
            }
        });

        cardDeckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(intent);
            }
        });
//IF elses (there's probably a DRYer way) to set SFW icon + visibility of tags
        if (game.isclean){
            sfwIconAngel.setVisibility(View.VISIBLE);
        }
        if (!game.isclean) {
            sfwIconDevil.setVisibility(View.VISIBLE);
        }
//TODOne: check what tags are present in GAME object and toggle visibility of appropriate tag icons
        drinkIcon.setVisibility(View.GONE);
        movingIcon.setVisibility(View.GONE);
        carIcon.setVisibility(View.GONE);
        paperIcon.setVisibility(View.GONE);

        if (game.tags.contains("drinking")){
            drinkIcon.setVisibility(View.VISIBLE);
        }
        if (game.tags.contains("movement")){
            movingIcon.setVisibility(View.VISIBLE);
        }
        if (game.tags.contains("car")){
            carIcon.setVisibility(View.VISIBLE);
        }
        if (game.tags.contains("writing")){
            paperIcon.setVisibility(View.VISIBLE);
        }

        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast carToast = Toast.makeText(getApplicationContext(), "Car: This is an ideal game to play while (someone else is) driving", Toast.LENGTH_SHORT);
                        carToast.show();
            }
        });
        drinkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast drinkToast = Toast.makeText(getApplicationContext(), "Drinking: This game is best played while drinking. Cheers!", Toast.LENGTH_SHORT);
                drinkToast.show();
            }
        });
        movingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast movingToast = Toast.makeText(getApplicationContext(), "Movement: This game requires a bit of physical activity.", Toast.LENGTH_SHORT);
                movingToast.show();
            }
        });
        paperIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast writingToast = Toast.makeText(getApplicationContext(), "Writing: This game requires you to write something down", Toast.LENGTH_SHORT);
                writingToast.show();
            }
        });

    }
    public void onBackPressed() {
        query = "";
        tagsQuery = "";
        isCleanQuery = false;
        isByRatingQuery = false;
        isByAlphabetQuery = false;

        Intent intent = new Intent(this,TabMainActivity.class);
        startActivity(intent);
        finish();
    }
}
