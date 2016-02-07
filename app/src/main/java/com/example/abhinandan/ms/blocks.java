package com.example.abhinandan.ms;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by ABHINANDAN on 2/10/2015.
 */
public class blocks extends Button{
    private int value;

    public blocks(Context context) {
        super(context);
    }

    public blocks(Context context, AttributeSet attrs, int value) {
        super(context, attrs);
        this.value = value;
    }

    public void setDefaults()
    {

    }

    public void setFlag(int x)
    {
        //this.setHeight(1);
       // this.setWidth(2);
        if(x==1) {
            this.setBackgroundResource(R.drawable.flag);
        }
        else
        {
            this.setBackgroundResource(R.drawable.blue_new);
        }

    }

    public void open(int mines_ground[][],int x,int y)
    {

        ////////  to be deleted later....just for testing


        if(mines_ground[x][y]==20)
        {
            this.setText("");

            this.setTextColor(Color.WHITE);
            this.setBackgroundResource(R.drawable.garbage);
            return;
        }
        ///////

        if(mines_ground[x][y]==0)
        {
            //this.setText("0");
            this.setBackgroundResource(R.drawable.grey);
            this.setText("");
        }
        else if(mines_ground[x][y]==1) {
            this.setText("1");
            this.setBackgroundResource(R.drawable.one);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==2)
        {
            this.setText("2");
            this.setBackgroundResource(R.drawable.two);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==3)
        {
            this.setText("3");
            this.setBackgroundResource(R.drawable.three);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==4)
        {
            this.setText("4");
            this.setBackgroundResource(R.drawable.four);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==5)
        {
            this.setText("5");
            this.setBackgroundResource(R.drawable.five);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==18)
        {
            this.setText("");
            this.setBackgroundResource(R.drawable.vaccum);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==15)
        {
            this.setText("");
            this.setBackgroundResource(R.drawable.md);//md to be replaced
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==16)
        {
            this.setText("");
            this.setBackgroundResource(R.drawable.black_out);//to be replaced
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==17)
        {
            this.setText("");
            this.setBackgroundResource(R.drawable.vc);
            this.setTextColor(Color.WHITE);
        }
        else if(mines_ground[x][y]==25)
        {
            this.setText("P");
            this.setBackgroundResource(R.drawable.purple);
            this.setTextColor(Color.WHITE);
        }
    }
}
