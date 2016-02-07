package com.example.abhinandan.ms;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;


public class Multi extends ActionBarActivity {

    int type_of_act;

    public BluetoothAdapter mBA;
    public AcceptThread mAcceptThread;
    public ConnectThread mConnectThread;
    public ConnectedThread ct;

    String address;
    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final int MESSAGE_READ = 2;

    public int open_count=0;
    TableLayout minefield;
    blocks block[][];
    private Handler timer = new Handler();
    private int secondsPassed = 0;
    TextView txtTimer;
    TextView connecting;
    private boolean isTimerStarted;
    private boolean isblackedout;
    private boolean isshielded;
    private boolean isdefuse;
    private boolean ismd;
    public int mines_ground[][];
    public int opened[][];
    public int flaged[][];
    public int move_x[];
    public int move_y[];

    public int mines_ground_o[][];
    public int opened_o[][];
    public int flaged_o[][];


    final Context context = this;

    ImageButton blackout;
    ImageButton minedisplace;
    ImageButton shield_button;
    ImageButton defuse;
    ImageView black;
    TextView powerused;
    TextView black_tv;
    TextView blackout_count_tv;
    TextView md_count_tv;
    TextView shield_c_tv;

    Button dialogbutton;
    Button dialogbutton1;
    Button dialogcancel;
    Button dialogcancel1;
    Dialog dialog;
    Dialog dialog1;
    Intent intent;
    Intent intent1;

    int blackout_count=0;
    int minedisplacement_count=0;
    int shield_count=0;
    int defuse_count=0;

    int click_count=0;
    int md_start_i,md_start_j;
    int md_last_i,md_last_j;

    String open_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);

        getSupportActionBar().hide();

        isblackedout=false;
        ismd=false;
        isshielded=false;
        isdefuse=false;



        minefield = (TableLayout)findViewById(R.id.m_Minefield);
        txtTimer=(TextView)findViewById(R.id.m_timer);
        connecting=(TextView)findViewById(R.id.connecting);
        minedisplace=(ImageButton)findViewById(R.id.md);
        blackout=(ImageButton)findViewById(R.id.bl);
        powerused=(TextView)findViewById(R.id.powerused_tv);
        black=(ImageView)findViewById(R.id.imageView);
        black_tv=(TextView)findViewById(R.id.black_tv);
        blackout_count_tv=(TextView)findViewById(R.id.black_c_tv);
        md_count_tv=(TextView)findViewById(R.id.md_c_tv);

        shield_button=(ImageButton)findViewById(R.id.shield);
        shield_c_tv=(TextView)findViewById(R.id.shield_c);

        defuse=(ImageButton)findViewById(R.id.defuse);

        minedisplace.setVisibility(View.GONE);
        blackout.setVisibility(View.GONE);
        shield_button.setVisibility(View.GONE);
        defuse.setVisibility(View.GONE);

        black.setVisibility(View.GONE);
        black_tv.setVisibility(View.GONE);

        minefield.setVisibility(View.GONE);
        txtTimer.setVisibility(View.GONE);
        connecting.setVisibility(View.VISIBLE);
        //blocks[][] block=new blocks[15][15];
        block=new blocks[15][15];

        mBA = BluetoothAdapter.getDefaultAdapter();
        intent1=getIntent();
        type_of_act=intent1.getIntExtra("type",0);


        /*md_count_tv.setVisibility(View.GONE);
        blackout_count_tv.setVisibility(View.GONE);
        shield_c_tv.setVisibility(View.GONE);*/

        mAcceptThread=null;
        mConnectThread=null;
        ct=null;

        if(type_of_act==1)
        {

            mAcceptThread=new AcceptThread();
            mAcceptThread.start();

        }
        else
        {

            address=intent1.getStringExtra("device_name");

            BluetoothDevice device = mBA.getRemoteDevice(address);
            mConnectThread = new ConnectThread(device);
            mConnectThread.start();

        }

        int i,j,k;
        open_count=0;
        //////////  init arrays
        mines_ground=new int[15][15];
        opened=new int[15][15];
        flaged=new int[15][15];
        move_x=new int[10];
        move_y=new int[10];

        mines_ground_o=new int[15][15];
        opened_o=new int[15][15];
        flaged_o=new int[15][15];


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

                mines_ground_o[i][j]=0;
                opened_o[i][j]=0;
                flaged_o[i][j]=0;
            }
        }
        /////////

        blackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blackout_count > 0) {
                    blackout_count--;
                    blackout_count_tv.setText("" + blackout_count);
                    blackout.setVisibility(View.GONE);
                    String message = "blackout";
                    byte[] send = message.getBytes();
                    ct.write(send);

                    powerused.setText("Opponent Blacked out");

                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            powerused.setText("");
                        }
                    }, 3000);


                }
            }
        });

        minedisplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minedisplacement_count > 0) {

                    minedisplacement_count--;
                    md_count_tv.setText("" + minedisplacement_count);
                    minedisplace.setVisibility(View.GONE);
                    ismd=true;

                    int i,j;

                    for(i=1;i<10;i++)
                    {
                        for(j=1;j<10;j++)
                        {
                            block[i][j].setText("");
                            if(mines_ground_o[i][j]==20)
                            {
                                block[i][j].open(mines_ground_o,i,j);
                            }
                            else if(mines_ground_o[i][j]==15)
                            {
                                block[i][j].open(mines_ground_o,i,j);
                            }
                            else if(opened_o[i][j]==1)
                            {
                                block[i][j].open(mines_ground_o,i,j);
                            }
                            else
                            {
                                block[i][j].setBackgroundResource(R.drawable.blue_new);
                                block[i][j].setText("");
                            }
                        }
                    }



                    powerused.setText("Opponent's Mine Displaced");
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            powerused.setText("");

                        }
                    }, 3000);

                }
            }
        });

        shield_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shield_count>0)
                {
                    shield_count--;
                    shield_c_tv.setText(""+shield_count);
                    shield_button.setVisibility(View.GONE);
                    isshielded=true;
                    powerused.setText("Shielded against attacks");
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        public void run() {
                            powerused.setText("");
                            isshielded=false;

                        }
                    }, 15000);
                }
            }
        });

        defuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defuse_count>0)
                {
                    isdefuse=true;
                    defuse.setVisibility(View.GONE);
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
        String message1="mine";

        int row,column,k;
        int i,j;
        int temp_x,temp_y;
        int x,y;
        Random random=new Random();

        for(row=1;row<10;row++)
        {

            k=random.nextInt(row+1941996);
            k=(k%9)+1;

            if(opened[row][k]!=1&&flaged[row][k]!=1)
            {
                mines_ground[row][k]=20;



                message1=message1+""+row+""+k;

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
        message1=message1+"20";
        byte[] send1 = message1.getBytes();
        ct.write(send1);
        ct.write(send1);
        ct.write(send1);
        ct.write(send1);

       int c=0;
        int flag1=0,flag2=1;
        for(row=0;row<2;row++)
        {
            x=random.nextInt(row+1965);
            x=x%9+1;

            y=random.nextInt(row+1965);
            y=y%9+1;

            if(x>=1&&x<=9&&y>=1&&y<=9)
            {
                if(mines_ground[x][y]==0&&opened[x][y]==0)
                {
                    mines_ground[x][y]=15+c;
                    c = c+random.nextInt(row+1965)%3+1;
                }
                else
                    row--;
            }
            else
                row--;


            /*if(mines_ground[x][y]==0&&flag1==0&&opened[x][y]==0)
            {
                mines_ground[x][y]=15;
                String message = "powe"+x+""+y+"20";
                byte[] send = message.getBytes();
                ct.write(send);
                flag1=1;
                flag2=0;
                c++;
            }

            if(mines_ground[x][y]==0&&flag2==0&&opened[x][y]==0)
            {
                mines_ground[x][y]=16;
                String message = "powe"+x+""+y+"20";
                byte[] send = message.getBytes();
                ct.write(send);
                flag2=1;
                c++;
            }*/




        }


    }

    void game_win()
    {

        String message="win";
        byte[] send = message.getBytes();
        ct.write(send);

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
                intent=new Intent(Multi.this,MainActivity.class);
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
                intent=new Intent(Multi.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        dialog1.show();

    }

    public void game_over()
    {
        String message="lose";
        byte[] send = message.getBytes();
        ct.write(send);

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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gameover);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());

        lp.width = (int )pixels;
        lp.height = (int )pixels;
        lp.alpha = 0.8f;


        dialogbutton=(Button)dialog.findViewById(R.id.play_again);

        dialogbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                intent=new Intent(Multi.this,MainActivity.class);
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
                intent=new Intent(Multi.this,MainActivity.class);
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
            if(isdefuse==false) {
                game_over();
            }

            block[x][y].open(mines_ground,x,y);
        }

        if(mines_ground[x][y]==15)
        {
            minedisplacement_count++;
            md_count_tv.setText(""+minedisplacement_count);
            minedisplace.setVisibility(View.VISIBLE);
            block[x][y].open(mines_ground,x,y);
            open_count++;
            opened[x][y]=1;
            return;
        }


        if(mines_ground[x][y]==16)
        {
            blackout_count++;
            blackout_count_tv.setText(""+blackout_count);
            blackout.setVisibility(View.VISIBLE);
            block[x][y].open(mines_ground,x,y);
            open_count++;
            opened[x][y]=1;
            return;
        }

        if(mines_ground[x][y]==17)
        {
            shield_count++;
            shield_c_tv.setText(""+shield_count);
            shield_button.setVisibility(View.VISIBLE);
            block[x][y].open(mines_ground,x,y);
            open_count++;
            opened[x][y]=1;
            return;
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
            open_val = open_val+""+x+""+y;

            return;
        }
        opened[x][y]=1;
        open_val = open_val+""+x+""+y;

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
                    if(mines_ground[temp_x][temp_y]==15)
                    {
                        continue;
                    }

                    if(mines_ground[temp_x][temp_y]==16)
                    {
                        continue;
                    }
                    if(mines_ground[temp_x][temp_y]==17)
                    {
                        continue;
                    }
                    if(mines_ground[temp_x][temp_y]==18)
                    {
                        continue;
                    }
                    open_count++;
                    block[temp_x][temp_y].open(mines_ground,temp_x,temp_y);
                    opened[temp_x][temp_y]=1;

                    open_val = open_val+""+temp_x+""+temp_y;

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
                        if (isblackedout != true&&ismd!=true) {
                            if (!isTimerStarted) {

                                startTimer();
                                // block[finalRow1][finalColumn1].open(mines_ground, finalRow1, finalColumn1);
                                opened[finalRow1][finalColumn1] = 1;
                                int i, j, k;

                                for (i = 0; i < 8; i++) {
                                    j = finalRow1 + move_x[i];
                                    k = finalColumn1 + move_y[i];

                                    opened[j][k] = 1;
                                }
                                setMines();

                                for (i = 0; i < 8; i++) {
                                    j = finalRow1 + move_x[i];
                                    k = finalColumn1 + move_y[i];

                                    opened[j][k] = 0;
                                }


                                open_val="open";
                                tapped(finalRow1, finalColumn1);
                                open_val=open_val+"20";
                                byte[] send = open_val.getBytes();
                                ct.write(send);


                                //open_count++;
                                //block[finalRow1][finalColumn1].open(mines_ground,finalRow1,finalColumn1);
                                isTimerStarted = true;
                            } else {
                              if(flaged[finalRow1][finalColumn1] != 1 && opened[finalRow1][finalColumn1] != 1) {
                                    //block[finalRow1][finalColumn1].open(mines_ground, finalRow1, finalColumn1);
                                    //opened[finalRow1][finalColumn1]=1;


                                  open_val="open";
                                  tapped(finalRow1, finalColumn1);
                                  open_val=open_val+"20";
                                  byte[] send = open_val.getBytes();
                                  ct.write(send);

                                  isdefuse=false;
                              }

                            }

                            //txtTimer.setText(""+open_count);

                            if (open_count >= 72) {
                                game_win();
                            }
                        }

                        if(ismd==true)
                        {
                            if(click_count==0)
                            {
                                if(mines_ground_o[finalRow1][finalColumn1]==20) {
                                    md_start_i = finalRow1;
                                    md_start_j = finalColumn1;
                                    click_count++;
                                }
                            }
                            else if(click_count==1)
                            {
                                if(mines_ground_o[finalRow1][finalColumn1]!=20&&mines_ground_o[finalRow1][finalColumn1]!=15) {
                                    md_last_i = finalRow1;
                                    md_last_j = finalColumn1;
                                    click_count = 0;
                                    ismd = false;

                                    String message = "dmse" + md_start_i + "" + md_start_j + "" + md_last_i + "" + md_last_j + "20";

                                    byte[] send = message.getBytes();
                                    ct.write(send);

                                    int i, j;

                                    for (i = 1; i < 10; i++) {
                                        for (j = 1; j < 10; j++) {
                                            if (flaged[i][j] == 1) {
                                                block[i][j].setFlag(1);
                                                block[i][j].setText("");
                                                continue;
                                            }
                                            if (opened[i][j] == 1) {
                                                block[i][j].open(mines_ground, i, j);
                                            }
                                            else
                                            {
                                                block[i][j].setBackgroundResource(R.drawable.blue_new);
                                                block[i][j].setText("");
                                            }

                                        }
                                    }
                                }
                            }


                        }
                    }
                });

                block[row][column].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        if (isblackedout != true&&ismd!=true) {
                            int i = finalRow;
                            int j = finalColumn;
                            if (flaged[finalRow][finalColumn] == 0 && opened[finalRow][finalColumn] != 1) {
                                flaged[finalRow][finalColumn] = 1;

                                ViewGroup.LayoutParams para = block[i][j].getLayoutParams();
                                block[i][j].setFlag(1);

                                para.height = blockDimension - (int)pixels1;
                                para.width = blockDimension - (int)pixels2;
                                block[i][j].setText("");
                                block[i][j].setLayoutParams(para);

                                return true;
                            } else if (opened[finalRow][finalColumn] != 1 && flaged[finalRow][finalColumn] == 1) {
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



    public Handler Refresh = new Handler(){
        public void handleMessage(Message msg) {

            minefield.setVisibility(View.VISIBLE);
            txtTimer.setVisibility(View.VISIBLE);
            connecting.setVisibility(View.GONE);

        }
    };

    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            byte[] readBuf = (byte[]) msg.obj;
            String readMessage = new String(readBuf, 0, msg.arg1);
            //readMessage contains recieved message from remote device

            if(readMessage.charAt(0)=='l'&&readMessage.charAt(1)=='o')
            {
                stopTimer();
                //txtTimer.setText("You Win");

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
                        intent=new Intent(Multi.this,MainActivity.class);
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
                        intent=new Intent(Multi.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    }
                });

                dialog1.show();


            }
            else if(readMessage.equals("win"))
            {
                stopTimer();
                //txtTimer.setText("You Loose");

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
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.gameover);

                float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());

                lp.width = (int )pixels;
                lp.height = (int )pixels;
                lp.alpha = 0.8f;


                dialogbutton=(Button)dialog.findViewById(R.id.play_again);

                dialogbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dialog.dismiss();
                        intent=new Intent(Multi.this,MainActivity.class);
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
                        intent=new Intent(Multi.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                    }
                });
                dialog.show();

            }
            else if(readMessage.equals("blackout"))
            {
                if(isshielded==false) {
                    black.setVisibility(View.VISIBLE);
                    black_tv.setVisibility(View.VISIBLE);
                    isblackedout = true;

                    Handler h1 = new Handler();
                    h1.postDelayed(new Runnable() {
                        public void run() {
                            black.setVisibility(View.GONE);
                            black_tv.setVisibility(View.GONE);
                            isblackedout = false;
                        }
                    }, 8000);

                }
            }
            else if(readMessage.charAt(0)=='m')
            {
                int i,j;
                int a=4,b;
                String first,last;
                for(i=1;i<10;i++)
                {
                    b=a+1;
                    first=readMessage.substring(a,b);
                    a++;
                    b++;
                    last=readMessage.substring(a,b);

                    a++;

                    int x,y;
                    x=Integer.parseInt(first);
                    y=Integer.parseInt(last);
                    mines_ground_o[x][y]=20;

                }
            }
            else if(readMessage.charAt(0)=='p')
            {
                String first=readMessage.substring(4,5);
                String last=readMessage.substring(5,6);

                int x,y;
                x=Integer.parseInt(first);
                y=Integer.parseInt(last);
                mines_ground_o[x][y]=15;
            }
            else if(readMessage.charAt(0)=='d')
            {
                int temp_x,temp_y,i,j;
                String first=readMessage.substring(4,5);
                String last=readMessage.substring(5,6);

                int x,y;
                x=Integer.parseInt(first);
                y=Integer.parseInt(last);
                mines_ground[x][y]=0;

                for(i=0;i<8;i++)
                {
                    temp_x=x+move_x[i];
                    temp_y=y+move_y[i];

                    if(temp_x>=1&&temp_x<=9&&temp_y>=1&&temp_y<=9) {

                        if(mines_ground[temp_x][temp_y]==20)
                        {
                            mines_ground[x][y]++;
                        }
                    }
                }


                for(i=0;i<8;i++)
                {
                    temp_x=x+move_x[i];
                    temp_y=y+move_y[i];

                    if(temp_x>=1&&temp_x<=9&&temp_y>=1&&temp_y<=9) {

                        if (mines_ground[temp_x][temp_y] >= 1 && mines_ground[temp_x][temp_y] <= 8) {

                            mines_ground[temp_x][temp_y]--;

                            if(mines_ground[temp_x][temp_y]<0)
                                mines_ground[temp_x][temp_y]=0;

                            if(opened[temp_x][temp_y]==1)
                                block[temp_x][temp_y].open(mines_ground,temp_x,temp_y);
                        }
                    }
                }

                first=readMessage.substring(6,7);
                last=readMessage.substring(7,8);

                x=Integer.parseInt(first);
                y=Integer.parseInt(last);

                mines_ground[x][y]=20;
                opened[x][y]=0;
                block[x][y].setBackgroundResource(R.drawable.blue_new);
                block[x][y].setText("");
                for(i=0;i<8;i++)
                {
                    temp_x=x+move_x[i];
                    temp_y=y+move_y[i];
                    if(temp_x>=1&&temp_x<=9&&temp_y>=1&&temp_y<=9) {
                        if (mines_ground[temp_x][temp_y] >= 0 && mines_ground[temp_x][temp_y] <= 8) {
                            mines_ground[temp_x][temp_y]++;

                            if(opened[temp_x][temp_y]==1)
                            {
                                block[temp_x][temp_y].open(mines_ground,temp_x,temp_y);
                            }
                        }
                    }

                }
            }
            else if(readMessage.charAt(0)=='o')
            {
                /*String first=readMessage.substring(4,5);
                String last=readMessage.substring(5,6);

                int x,y;
                x=Integer.parseInt(first);
                y=Integer.parseInt(last);

                opened_o[x][y]=1;*/

                int i,j;
                int a=4,b;
                String first,last;
                for(i=1;a<readMessage.length()-2;i++)
                {
                    b=a+1;
                    first=readMessage.substring(a,b);
                    a++;
                    b++;
                    last=readMessage.substring(a,b);

                    a++;

                    int x,y;
                    x=Integer.parseInt(first);
                    y=Integer.parseInt(last);
                    opened_o[x][y]=1;

                }
            }


        }
    };

    public class AcceptThread extends Thread {
        public final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBA.listenUsingRfcommWithServiceRecord("MS",MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    //manageConnectedSocket(socket);

                    //Connect(socket);
                   /* Refresh.obtainMessage(MESSAGE_READ, -1)
                            .sendToTarget();
                    */
                    ////////// to be uncommented
                    ct=new ConnectedThread(socket);
                    ct.start();
                   ///////////


            /*        if(ct!=null){
                        Refresh.obtainMessage(MESSAGE_READ, -1)
                                .sendToTarget();
                    }*/


                    //ConnectedThread mConnectedT=new ConnectedThread(socket);
                    //mConnectedT.start();

                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

             /*   if(ct!=null){
                    Refresh.obtainMessage(MESSAGE_READ, -1)
                            .sendToTarget();
                }*/
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }


    public class ConnectThread extends Thread {
        public final BluetoothSocket mmSocket;
        public final BluetoothDevice mmDevice;
        //public MainActivity obj;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBA.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)

            //manageConnectedSocket(mmSocket);
            //Refresh.obtainMessage(MESSAGE_READ, -1)
              //      .sendToTarget();
            //Connect(mmSocket);
            /*Refresh.obtainMessage(MESSAGE_READ, -1)
                    .sendToTarget();
               */
        /////////////// to be uncommented
            ct=new ConnectedThread(mmSocket);
            ct.start();

         /////////

                     /*  if(ct!=null){
                Refresh.obtainMessage(MESSAGE_READ, -1)
                        .sendToTarget();

            }*/


            //ConnectedThread mConnectedT=new ConnectedThread(mmSocket);
            //mConnectedT.start();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Refresh.obtainMessage(MESSAGE_READ, -1)
                    .sendToTarget();


            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            /*Toast.makeText(getApplicationContext()," Paired and transfer ready" ,
                    Toast.LENGTH_LONG).show();*/
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()



            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi, menu);
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
