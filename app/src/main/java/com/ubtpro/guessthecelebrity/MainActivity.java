package com.ubtpro.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ImageView celebrityImage, shinchan, collage;
    TextView timerTextView, scoreTextView,totaltimerTextView,scorecardTextView;
    Button startButton,button0,button1,button2,button3,playAgain;
    GridLayout optionLayout;
    ArrayList<String> names;
    ArrayList<String> urlList;
    Random rand = new Random();
    int[] arr = new int[72];
    int locationcorrectanswer;
    int questionindex,totalanswer=0,correctanswer=0;
    String[] options=new String[4];
    CountDownTimer countDownTimer,totalcountDownTimer;


    public void playAgain(View view){
        playAgain.setVisibility(view.GONE);
        scorecardTextView.setVisibility(View.GONE);
        button0.setClickable(true);
        button1.setClickable(true);
        button2.setClickable(true);
        button3.setClickable(true);
        countdown();
        totalcountdown();
        setImage();
        totalanswer=0;
        correctanswer=0;
        scoreTextView.setText("0/0");


    }



    public void clickOption(View view){
        totalanswer+=1;
        if(view.getTag().toString().equals(Integer.toString(locationcorrectanswer))){
            Toast.makeText(MainActivity.this,"Absolutely correct",Toast.LENGTH_SHORT).show();
            correctanswer+=1;
        }
        else
        {
            Toast.makeText(MainActivity.this,"Incorrect!-Answer: "+names.get(questionindex),Toast.LENGTH_SHORT).show();
        }


        scoreTextView.setText(Integer.toString(correctanswer)+"/"+Integer.toString(totalanswer));
        setImage();
        countDownTimer.cancel();
        countdown();



    }


    public void startFunction(View view) {
        collage.setVisibility(View.GONE);
        startButton.setVisibility(View.GONE);
        celebrityImage.setVisibility(View.VISIBLE);
        shinchan.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);
        optionLayout.setVisibility(View.VISIBLE);
        totaltimerTextView.setVisibility(View.VISIBLE);

        countdown();
        totalcountdown();
        setImage();





    }


    public void setImage() {


        questionindex = rand.nextInt(urlList.size());

        if (arr[questionindex] == 2) {


            setImage task = new setImage();
            try {
                Bitmap myImage = task.execute(urlList.get(questionindex)).get();

                celebrityImage.setImageBitmap(myImage);

                options = new String[4];


                locationcorrectanswer = rand.nextInt(4);

                int incorrectanswer;
                for (int i = 0; i < 4; i++) {
                    if (i == locationcorrectanswer) {
                        options[i] = (names.get(questionindex));
                    } else {
                        incorrectanswer = rand.nextInt(urlList.size());
                        while (incorrectanswer == questionindex) {
                            incorrectanswer = rand.nextInt(urlList.size());
                        }
                        options[i] = (names.get(incorrectanswer));
                    }


                }
                button0.setText(options[0]);
                button1.setText(options[1]);
                button2.setText(options[2]);
                button3.setText(options[3]);


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            arr[questionindex]=1;
        }
        else {

            setImage();
        }
    }


    public class setImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream In = connection.getInputStream();
                Bitmap mybitmap = BitmapFactory.decodeStream(In);

                return mybitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public class downloadContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection connection = null;
            String result = "";
            int data;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream In = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(In);
                data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;


            } catch (Exception e) {
                e.printStackTrace();
                return "failed";

            }

        }
    }

    public void totalcountdown(){
        totalcountDownTimer=new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long l) {
                int minutes=(int) l/60000;
                int seconds=(int)l/1000-minutes*60;
                String secondString=Integer.toString(seconds);
                if(seconds<=9){
                    secondString="0"+secondString;

                }

                totaltimerTextView.setText(Integer.toString(minutes)+":"+secondString);


            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                playAgain.setVisibility(View.VISIBLE);
                scorecardTextView.setVisibility(View.VISIBLE);
                scorecardTextView.setText(Integer.toString(correctanswer)+"/"+Integer.toString(totalanswer));
                button0.setClickable(false);
                button1.setClickable(false);
                button2.setClickable(false);
                button3.setClickable(false);


            }
        }.start();

    }


        public void countdown() {
            countDownTimer = new CountDownTimer(10000 + 500, 1000) {
                @Override
                public void onTick(long l) {

                    timerTextView.setText(Long.toString(l / 1000));


                }

                @Override
                public void onFinish() {

                    totalanswer+=1;
                    scoreTextView.setText(Integer.toString(correctanswer)+"/"+Integer.toString(totalanswer));
                    setImage();
                    countdown();


                }
            }.start();
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            celebrityImage = (ImageView) findViewById(R.id.celebrityImage);
            timerTextView = (TextView) findViewById(R.id.timerTextView);
            scoreTextView = (TextView) findViewById(R.id.scoreTextView);
            shinchan = (ImageView) findViewById(R.id.shinchan);
            startButton = (Button) findViewById(R.id.startButton);
            collage = (ImageView) findViewById(R.id.collage);
            optionLayout = (GridLayout) findViewById(R.id.optionLayout);
            totaltimerTextView=(TextView) findViewById(R.id.totaltimerTextView);
            playAgain=(Button) findViewById(R.id.playAgain);
            scorecardTextView=(TextView) findViewById(R.id.scoreCardTextView);


            button0=(Button) findViewById(R.id.button0);
            button1=(Button) findViewById(R.id.button1);
            button2=(Button) findViewById(R.id.button2);
            button3=(Button) findViewById(R.id.button3);

            for(int i=0;i<72;i++){
                arr[i]=2;
            }



            celebrityImage.setVisibility(View.GONE);
            shinchan.setVisibility(View.GONE);
            timerTextView.setVisibility(View.GONE);
            scoreTextView.setVisibility(View.GONE);
            optionLayout.setVisibility(View.GONE);
            totaltimerTextView.setVisibility(View.GONE);
            scorecardTextView.setVisibility(View.GONE);
            playAgain.setVisibility(View.GONE);

            names = new ArrayList<String>();
            urlList = new ArrayList<String>();
            downloadContent task = new downloadContent();
            String result = null;
            String[] finalresult = null;
            try {
                result = task.execute("http://www.posh24.se/kandisar").get();


                finalresult = result.split("ListedArticles");
                Pattern p1 = Pattern.compile("src=\"(.*?)\" alt");
                Pattern p2 = Pattern.compile("alt=\"(.*?)\"/>");
                Matcher m1 = p1.matcher(finalresult[0]);
                Matcher m2 = p2.matcher(finalresult[0]);
                while (m1.find() && m2.find()) {


                    names.add(m2.group(1));
                    urlList.add(m1.group(1));

                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }




        }
    }














