package com.example.abhinandan.ms;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Random;
import java.util.Set;

public class host_BT extends Activity {

    ////////////multi declarations
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
    Button play_b;
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
    //////////



    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView mTitle;
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothChatService mChatService = null;

    Button tohost;
    Button tojoin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

        // Set up the custom title
       // mTitle = (TextView) findViewById(R.id.title_left_text);
        //mTitle.setText(R.string.app_name);
       // mTitle = (TextView) findViewById(R.id.title_right_text);

        //////////////id allocations multi
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
       // play_b=(Button)findViewById(R.id.play_button);

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
        connecting.setVisibility(View.GONE);
        /////////////
        block=new blocks[15][15];


        /////////////   init all values multi
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
        ////////////

        ////////////// on click listners multi
        blackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (blackout_count > 0) {
                    blackout_count--;
                    blackout_count_tv.setText("" + blackout_count);
                    blackout.setVisibility(View.GONE);
                    String message = "blackout";
                   // byte[] send = message.getBytes();
                   // ct.write(send);
                    sendMessage(message);


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
                            else if(mines_ground_o[i][j]==25)
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

        /*play_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View myview=findViewById(R.id.main_lay);
                myview.setBackgroundResource(R.drawable.gameplay);


                minefield.setVisibility(View.VISIBLE);
                txtTimer.setVisibility(View.VISIBLE);

                tojoin.setVisibility(View.GONE);
                tohost.setVisibility(View.GONE);

                mConversationView.setVisibility(View.GONE);
                mSendButton.setVisibility(View.GONE);
                mOutEditText.setVisibility(View.GONE);
                play_b.setVisibility(View.GONE);


            }
        });*/

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
        /////////////


        tohost=(Button)findViewById(R.id.hosting1);
        tojoin=(Button)findViewById(R.id.joining1);

        tohost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"in hosting");
                Intent serverIntent = new Intent(host_BT.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        });

        tojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"in joining");
                ensureDiscoverable();
            }
        });

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }



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
/*
    void refresh_all()
    {
        isTimerStarted=false;
        secondsPassed=0;

        blackout_count=0;
        minedisplacement_count=0;
        shield_count=0;
        defuse_count=0;
        open_count=0;


        minedisplace.setVisibility(View.GONE);
        blackout.setVisibility(View.GONE);
        defuse.setVisibility(View.GONE);
        shield_button.setVisibility(View.GONE);

        isblackedout=false;
        isdefuse=false;
        ismd=false;
        isshielded=false;

        int i,j;

        for(i=0;i<12;i++)
        {
            for(j=0;j<12;j++)
            {
                opened[i][j]=0;
                opened_o[i][j]=0;
                mines_ground[i][j]=0;
                mines_ground_o[i][j]=0;
                flaged[i][j]=0;
                flaged_o[i][j]=0;

                block[i][j].setText("");
                block[i][j].setBackgroundResource(R.drawable.blue_new);
            }
        }


    }
*/
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
        //byte[] send1 = message1.getBytes();
        //ct.write(send1);
        //ct.write(send1);
       // ct.write(send1);
        //ct.write(send1);
        sendMessage(message1);

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

                    String str;

                    str="powe"+x+""+y+"20";
                    sendMessage(str);

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
        //byte[] send = message.getBytes();
       //ct.write(send);
        sendMessage(message);

        stopTimer();

        dialog1=new Dialog(context);
        Window di_win = dialog1.getWindow();
        WindowManager.LayoutParams lp = di_win.getAttributes();
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.gamewin);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

        lp.width = (int )pixels;
        lp.height = (int )pixels;
        lp.alpha = 0.8f;

        dialogbutton1=(Button)dialog1.findViewById(R.id.button3);

        dialogbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                intent=new Intent(host_BT.this,host_BT.class);
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
                intent=new Intent(host_BT.this,host_BT.class);
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
        //byte[] send = message.getBytes();
        //ct.write(send);
        sendMessage(message);

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
                intent=new Intent(host_BT.this,host_BT.class);
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
                intent=new Intent(host_BT.this,host_BT.class);
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
                               // byte[] send = open_val.getBytes();
                                //ct.write(send);
                                sendMessage(open_val);


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
                                   // byte[] send = open_val.getBytes();
                                   // ct.write(send);
                                    sendMessage(open_val);

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
                                if(mines_ground_o[finalRow1][finalColumn1]!=20&&mines_ground_o[finalRow1][finalColumn1]!=25) {
                                    md_last_i = finalRow1;
                                    md_last_j = finalColumn1;
                                    click_count = 0;
                                    ismd = false;

                                    String message = "dmse" + md_start_i + "" + md_start_j + "" + md_last_i + "" + md_last_j + "20";

                                   // byte[] send = message.getBytes();
                                    //ct.write(send);
                                    sendMessage(message);

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




    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });


        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        mOutEditText.setVisibility(View.INVISIBLE);
        mSendButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this,"You are not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    if(D) Log.i(TAG, "END onEditorAction");
                    return true;
                }
            };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                           // mTitle.setText("connected:");
                           // mTitle.append(mConnectedDeviceName);
                            mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                           // mTitle.setText("connecting...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                           // mTitle.setText("not connected");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Me:  " + writeMessage);



                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);

                    //////////// multi handler

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
                                intent=new Intent(host_BT.this,host_BT.class);
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
                                intent=new Intent(host_BT.this,host_BT.class);
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
                                intent=new Intent(host_BT.this,host_BT.class);
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
                                intent=new Intent(host_BT.this,host_BT.class);
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
                        mines_ground_o[x][y]=25;
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


                    ////////////

                    mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);



                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    View myview=findViewById(R.id.main_lay);
                    myview.setBackgroundResource(R.drawable.gameplay);


                    minefield.setVisibility(View.VISIBLE);
                    txtTimer.setVisibility(View.VISIBLE);

                    tojoin.setVisibility(View.GONE);
                    tohost.setVisibility(View.GONE);

                    mConversationView.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.GONE);
                    mOutEditText.setVisibility(View.GONE);
                    //play_b.setVisibility(View.GONE);

                    ////start the multiplayer game to be added
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }



            //////////// multi handler

           /* if(readMessage.charAt(0)=='l'&&readMessage.charAt(1)=='o')
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
                        intent=new Intent(host_BT.this,MainActivity.class);
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
                        intent=new Intent(host_BT.this,MainActivity.class);
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
                        intent=new Intent(host_BT.this,MainActivity.class);
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
                        intent=new Intent(host_BT.this,MainActivity.class);
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
                *//*String first=readMessage.substring(4,5);
                String last=readMessage.substring(5,6);

                int x,y;
                x=Integer.parseInt(first);
                y=Integer.parseInt(last);

                opened_o[x][y]=1;*/

              /*  int i,j;
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
*/

            ////////////
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mChatService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this,"Bluetooth was not enabled", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.discoverable:
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
        }
        return false;
    }

}