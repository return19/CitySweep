package com.example.abhinandan.ms;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

import java.util.Random;


public class SinglePlayer extends ActionBarActivity {

    public int open_count=0;
    final Context context = this;
    Button dialogbutton;
    Button dialogbutton1;
    Button dialogcancel;
    Button dialogcancel1;
    Dialog dialog;
    Dialog dialog1;
    Intent intent;
    TextView tv6;
    ImageButton defuse;


    TableLayout minefield;
    blocks block[][];
    private Handler timer = new Handler();
    private int secondsPassed = 0;
    TextView txtTimer;
    private boolean isTimerStarted;
    private boolean isdefused;
    public int mines_ground[][];
    public int opened[][];
    public int flaged[][];
    public int move_x[];
    public int move_y[];
    public int defuse_count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        getSupportActionBar().hide();

           int i,j,k;

        isdefused=false;

        open_count=0;
        //////////  init arrays
        mines_ground=new int[15][15];
        opened=new int[15][15];
        flaged=new int[15][15];
        move_x=new int[10];
        move_y=new int[10];

        move_x[0]=0;  move_y[0]=-1;
        move_x[1]=-1;  move_y[1]=-1;
        move_x[2]=-1;  move_y[2]=0;
        move_x[3]=-1;  move_y[3]=1;
        move_x[4]=0;  move_y[4]=1;
        move_x[5]=1;  move_y[5]=1;
        move_x[6]=1;  move_y[6]=0;
        move_x[7]=1;  move_y[7]=-1;

        for(i=0;i<12;i++)
        {
            for(j=0;j<12;j++)
            {
                mines_ground[i][j]=0;
                opened[i][j]=0;
                flaged[i][j]=0;
            }
        }
        /////////

        float pixels1;
        pixels1=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());

        minefield = (TableLayout)findViewById(R.id.Minefield);
        txtTimer=(TextView)findViewById(R.id.timer);
        defuse=(ImageButton)findViewById(R.id.defuser);

        defuse.setVisibility(View.INVISIBLE);

        ViewGroup.LayoutParams para = defuse.getLayoutParams();


        para.height =(int)pixels1;
        para.width =(int)pixels1;
        defuse.setLayoutParams(para);


        //blocks[][] block=new blocks[15][15];
        block=new blocks[15][15];

        defuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defuse_count>0) {
                    isdefused = true;
                    defuse.setVisibility(View.INVISIBLE);
                }
            }
        });

        showMineField(block);


    }

    public void startTimer()
    {
        if (secondsPassed == 0)
        {
            timer.removeCallbacks(updateTimeElasped);
            // tell timer to run call back after 1 second
            timer.postDelayed(updateTimeElasped, 1000);
        }
    }

    public void stopTimer()
    {
        // disable call backs
        timer.removeCallbacks(updateTimeElasped);
    }

    private Runnable updateTimeElasped = new Runnable()
    {
        public void run()
        {
            long currentMilliseconds = System.currentTimeMillis();
            ++secondsPassed;
            txtTimer.setText(Integer.toString(secondsPassed));

            // add notification
            timer.postAtTime(this, currentMilliseconds);
            // notify to call back after 1 seconds
            // basically to remain in the timer loop
            timer.postDelayed(updateTimeElasped, 1000);
        }
    };

    void setMines()
    {
        int row,column,k;
        int i,j;
        int temp_x,temp_y;
        Random random=new Random();
        for(row=1;row<10;row++)
        {

            k=random.nextInt(row+1941996);
            k=(k%9)+1;

            if(opened[row][k]!=1&&flaged[row][k]!=1)
            {
                mines_ground[row][k]=20;

                ///////  incrementing surrounding blocks for count of mines

                for(i=0;i<8;i++)
                {
                    temp_x=row+move_x[i];
                    temp_y=k+move_y[i];

                    if(temp_x>=1&&temp_x<=9&&temp_y>=1&&temp_y<=9&&mines_ground[temp_x][temp_y]!=20)
                    {
                        mines_ground[temp_x][temp_y]++;
                    }
                }

                //////////

            }
            else
            {
                row--;
            }

        }

        int x,y;
        row=1;

        for(i=0;i<1;row++)
        {
            x=random.nextInt(1965+row);
            x=x%9+1;
            y=random.nextInt(1965+row);
            y=y%9+1;
            if(x>=1&&x<=9&&y>=1&&y<=9) {
                if (mines_ground[x][y] == 0 && opened[x][y] == 0) {
                    mines_ground[x][y] = 18;
                    i++;
                }
            }
        }


    }

    void game_win()
    {

        stopTimer();

        dialog1=new Dialog(context);
        Window di_win = dialog1.getWindow();
        WindowManager.LayoutParams lp = di_win.getAttributes();
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.gamewin);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());

        lp.width = (int )pixels;
        lp.height = (int )pixels;
        lp.alpha = 0.8f;

        dialogbutton1=(Button)dialog1.findViewById(R.id.button3);

        dialogbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                intent=new Intent(SinglePlayer.this,SinglePlayer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        dialogcancel=(Button)dialog1.findViewById(R.id.button4);

        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                intent=new Intent(SinglePlayer.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });
        dialog1.show();

    }

    void game_over()
    {
        stopTimer();

        int i,j;
        for(i=1;i<=9;i++)
        {
            for(j=1;j<=9;j++)
            {
                if(mines_ground[i][j]==20)
                {
                    //open_count++;
                    open_count=0;
                    block[i][j].open(mines_ground,i,j);

                }
            }
        }

        dialog=new Dialog(context);
        Window di_win = dialog.getWindow();
        WindowManager.LayoutParams lp = di_win.getAttributes();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.gameover);

        dialogbutton=(Button)dialog.findViewById(R.id.play_again);


        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());

        lp.width = (int )pixels;
        lp.height = (int )pixels;
        lp.alpha = 0.8f;

        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //dialog.dismiss();
                intent=new Intent(SinglePlayer.this,SinglePlayer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        dialogcancel1=(Button)dialog.findViewById(R.id.cancel);

        dialogcancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                intent=new Intent(SinglePlayer.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        dialog.show();
    }

    void tapped(int x,int y)
    {
        int i,j,k,temp_x,temp_y;

        if(mines_ground[x][y]==20)
        {
            // game_over to be added
            //// to be deleted
            /*block[x][y].open(mines_ground,x,y);
            opened[x][y]=1;
            return;*/
            ////
            if(isdefused==false) {
                game_over();
            }
            isdefused=false;
            open_count--;
        }

        if(mines_ground[x][y]==18)
        {
            defuse_count++;
            block[x][y].open(mines_ground,x,y);
            defuse.setVisibility(View.VISIBLE);
            opened[x][y]=1;
            open_count++;
            return;
        }

        if(mines_ground[x][y]!=0)
        {
            open_count++;
            block[x][y].open(mines_ground,x,y);
            opened[x][y]=1;
            return;
        }
        opened[x][y]=1;
        open_count++;
        block[x][y].open(mines_ground,x,y);

        for(i=0;i<8;i++)
        {
            temp_x=x+move_x[i];
            temp_y=y+move_y[i];

            if(temp_x>=1&&temp_x<=9&&temp_y>=1&&temp_y<=9)
            {
                if(opened[temp_x][temp_y]!=1&&mines_ground[temp_x][temp_y]!=20&&flaged[temp_x][temp_y]!=1&&mines_ground[temp_x][temp_y]==0)
                {
                    tapped(temp_x,temp_y);
                }
                else if(flaged[temp_x][temp_y]!=1&&mines_ground[temp_x][temp_y]!=20&&opened[temp_x][temp_y]!=1&&mines_ground[temp_x][temp_y]!=0)
                {
                    if(mines_ground[temp_x][temp_y]==18)
                    {
                        continue;
                    }

                    open_count++;
                    block[temp_x][temp_y].open(mines_ground,temp_x,temp_y);
                    opened[temp_x][temp_y]=1;
                }
            }
        }

    }

    void showMineField(final blocks block[][])
    {
        int numberOfRowsInMineField=9;
        int numberOfColumnsInMineField=9;

        final int blockPadding=0;

        float pixels= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics());
        final int blockDimension=(int)pixels;
        //float pixels2=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0,getResources().getDisplayMetrics());
        final float pixels1=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,getResources().getDisplayMetrics());
        final float pixels2=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());




        for (int row = 1; row < numberOfRowsInMineField + 1; row++)
        {
            TableRow tableRow = new TableRow(this);

            tableRow.setLayoutParams(new TableRow.LayoutParams((blockDimension +2* blockPadding) *
                    numberOfColumnsInMineField, blockDimension+2 * blockPadding));

            for (int column = 1; column < numberOfColumnsInMineField + 1; column++)
            {
                //Button bt=new Button(this);
                //bt.setText(""+row);
                final int finalRow = row;
                final int finalColumn = column;

                block[row][column]=new blocks(this);

                //block[row][column].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                //block[row][column].setHeight(1);
                //block[row][column].setWidth(2);

                final int finalRow1 = row;
                final int finalColumn1 = column;
                block[row][column].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isTimerStarted)
                        {

                            startTimer();
                           // block[finalRow1][finalColumn1].open(mines_ground, finalRow1, finalColumn1);
                            opened[finalRow1][finalColumn1]=1;
                            int i,j,k;

                            for(i=0;i<8;i++)
                            {
                                j=finalRow1+move_x[i];
                                k=finalColumn1+move_y[i];

                                opened[j][k]=1;
                            }
                            setMines();

                            for(i=0;i<8;i++)
                            {
                                j=finalRow1+move_x[i];
                                k=finalColumn1+move_y[i];

                                opened[j][k]=0;
                            }

                            tapped(finalRow1,finalColumn1);
                            //open_count++;
                            //block[finalRow1][finalColumn1].open(mines_ground,finalRow1,finalColumn1);
                            isTimerStarted = true;
                        }
                        else {
                            if (flaged[finalRow1][finalColumn1] != 1&&opened[finalRow1][finalColumn1]!=1) {
                                //block[finalRow1][finalColumn1].open(mines_ground, finalRow1, finalColumn1);
                                //opened[finalRow1][finalColumn1]=1;

                                tapped(finalRow1,finalColumn1);
                            }

                        }

                        //txtTimer.setText(""+open_count);

                        if(open_count>=72)
                        {
                            game_win();
                        }
                    }
                });

                block[row][column].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int i= finalRow;
                        int j= finalColumn;
                        if(flaged[finalRow][finalColumn]==0&&opened[finalRow][finalColumn]!=1) {
                            flaged[finalRow][finalColumn] = 1;

                            ViewGroup.LayoutParams para = block[i][j].getLayoutParams();
                            block[i][j].setFlag(1);

                            para.height = blockDimension - (int)pixels1;
                            para.width = blockDimension - (int)pixels2;
                            block[i][j].setText("");
                            block[i][j].setLayoutParams(para);

                            return true;
                        }
                        else if(opened[finalRow][finalColumn]!=1&&flaged[finalRow][finalColumn]==1)
                        {
                            flaged[finalRow][finalColumn] = 0;

                            //ViewGroup.LayoutParams para = block[i][j].getLayoutParams();
                            block[i][j].setFlag(0);

                            /*para.height = blockDimension - 15;
                            para.width = blockDimension - 20;

                            block[i][j].setLayoutParams(para);*/

                            block[i][j].setLayoutParams(new TableRow.LayoutParams(
                                    blockDimension + 2 * blockPadding,
                                    blockDimension + 2 * blockPadding));
                            block[i][j].setPadding(blockPadding, blockPadding,
                                    blockPadding, blockPadding);
                            block[i][j].setText("");
                            return true;
                        }
                        return true;
                    }
                });
               // block[row][column].setText("");
                block[row][column].setBackgroundResource(R.drawable.blue_new);

                /*ViewGroup.LayoutParams para=block[row][column].getLayoutParams();
                para.height=blockDimension-15;
                para.width=blockDimension-20;

                block[row][column].setLayoutParams(para);*/


                block[row][column].setLayoutParams(new TableRow.LayoutParams(
                        blockDimension + 2 * blockPadding,
                        blockDimension + 2 * blockPadding));
                block[row][column].setPadding(blockPadding, blockPadding,
                        blockPadding, blockPadding);
                tableRow.addView(block[row][column]);
                //tableRow.addView(blocks[row][column]);
            }
            minefield.addView(tableRow,new TableLayout.LayoutParams(
                    (blockDimension * blockPadding) * numberOfColumnsInMineField,
                    blockDimension * blockPadding));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
