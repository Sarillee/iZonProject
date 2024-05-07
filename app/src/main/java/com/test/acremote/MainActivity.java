package com.test.acremote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends ApplicationBaseActivity
{
    int lastTmp=-1;
    int powerOn=0;
    boolean isRunning=false;
    int countToUpdate=2000;
    TextView temp;
    LinearLayout fsLow,fsMedium,fsHigh,fsAuto;
    LinearLayout modeCool,modeHeat,modeVent,modeDry;
    ImageView addBtn,removeBtn,powerBtm;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fsLow = findViewById(R.id.fsLow);
        fsLow.setOnClickListener(adjustFanSpeed);
        fsMedium = findViewById(R.id.fsMedium);
        fsMedium.setOnClickListener(adjustFanSpeed);
        fsHigh = findViewById(R.id.fsHigh);
        fsHigh.setOnClickListener(adjustFanSpeed);
        fsAuto = findViewById(R.id.fsAuto);
        fsAuto.setOnClickListener(adjustFanSpeed);

        modeCool = findViewById(R.id.modeCool);
        modeCool.setOnClickListener(adjustMode);
        modeHeat = findViewById(R.id.modeHeat);
        modeHeat.setOnClickListener(adjustMode);
        modeVent = findViewById(R.id.modeVent);
        modeVent.setOnClickListener(adjustMode);
        modeDry = findViewById(R.id.modeDry);
        modeDry.setOnClickListener(adjustMode);

        temp = findViewById(R.id.tmpTV);
        addBtn = findViewById(R.id.onAdd);
        addBtn.setOnClickListener(adjustTemp);
        removeBtn = findViewById(R.id.onRemove);
        removeBtn.setOnClickListener(adjustTemp);

        powerBtm = findViewById(R.id.powerBtn);
        powerBtm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(powerOn==0){powerOn=1;}
                else{powerOn=0;}
                setPowerStatus(powerOn);
                powerStatus(powerOn);
            }
        });
        checkStatus();
    }

    View.OnClickListener adjustTemp = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int curTmp = Integer.parseInt(temp.getText().toString());
            if(lastTmp<0){lastTmp=curTmp;}
            startUpdate();
            if(view.getId() == R.id.onRemove)
            {
                if(curTmp>15)
                {
                    curTmp--;
                    temp.setText(String.valueOf(curTmp));
                }


            }
            else if(view.getId() == R.id.onAdd)
            {
                if(curTmp<30)
                {
                    curTmp++;
                    temp.setText(String.valueOf(curTmp));
                }
            }
        }
    };

    View.OnClickListener adjustFanSpeed = new View.OnClickListener()
    {
        int level=0;
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.fsLow:
                {
                    level=1;
                    break;
                }
                case R.id.fsMedium:
                {
                    level=2;
                    break;
                }
                case R.id.fsHigh:
                {
                    level=3;
                    break;
                }
                case R.id.fsAuto:
                {
                    level=4;
                    break;
                }
            }

            //update
            updateFanSpeed(level);
            setFanSpeed(level);
        }
    };

    View.OnClickListener adjustMode = new View.OnClickListener()
    {
        int level=0;
        @Override
        public void onClick(View view)
        {
            switch (view.getId())
            {
                case R.id.modeCool:
                {
                    level=1;
                    break;
                }
                case R.id.modeHeat:
                {
                    level=2;
                    break;
                }
                case R.id.modeVent:
                {
                    level=3;
                    break;
                }
                case R.id.modeDry:
                {
                    level=4;
                    break;
                }
            }

            //update
            updateMode(level);
            setModeSpeed(level);
        }
    };

    private void startUpdate()
    {
        if(isRunning)
        {
            countToUpdate = 500;
            return;
        }
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                isRunning=true;
                countToUpdate = 500;
                while(countToUpdate>0)
                {
                    Util.DelayMili(1);
                    countToUpdate--;
                }
                Log.d("TAG", "run: update");
                setTemp();
                isRunning=false;
            }
        }.start();
    }

    public void checkStatus()
    {
        startProgressDialog(this,"Query Status","Loading...");
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                int[] output = ServerConnection.uploadQuery();
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(output!=null)
                        {
                            temp.setText(String.valueOf((int) output[0]/100));
                            updateFanSpeed(output[1]);
                            updateMode(output[2]);
                            powerStatus(output[3]);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Fail to Query",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                closeProgressDialog();
            }
        }.start();
    }

    public void setTemp()
    {
        startProgressDialog(this,"Set Value","Loading...");
        new Thread() {
            @Override
            public void run()
            {
                super.run();
                int ret = ServerConnection.uploadTemp(Integer.parseInt(temp.getText().toString())*100);
                if(ret==0)
                {
                    closeProgressDialog();
                }
                else
                {
                    Util.Delay(5);
                    closeProgressDialog();
                    checkStatus();
                }
            }
        }.start();
    }

    public void setFanSpeed(int value)
    {
        startProgressDialog(this,"Set Value","Loading...");
        new Thread() {
            @Override
            public void run()
            {
                super.run();
                int ret = ServerConnection.uploadFanSpeed(value);
                if(ret==0)
                {
                    closeProgressDialog();
                }
                else
                {
                    Util.Delay(5);
                    closeProgressDialog();
                    checkStatus();
                }
            }
        }.start();
    }

    public void setModeSpeed(int value)
    {
        startProgressDialog(this,"Set Value","Loading...");
        new Thread() {
            @Override
            public void run()
            {
                super.run();
                int ret = ServerConnection.uploadMode(value);
                if(ret==0)
                {
                    closeProgressDialog();
                }
                else
                {
                    Util.Delay(5);
                    closeProgressDialog();
                    checkStatus();
                }
            }
        }.start();
    }

    public void setPowerStatus(int value)
    {
        startProgressDialog(this,"Set Value","Loading...");
        new Thread() {
            @Override
            public void run()
            {
                super.run();
                int ret = ServerConnection.uploadPowerStatus(value);
                Log.d("TAG", "run: " + ret);
                if(ret==0)
                {
                    closeProgressDialog();
                }
                else
                {
                    Util.Delay(5);
                    closeProgressDialog();
                    checkStatus();
                }
            }
        }.start();
    }

    public void updateFanSpeed(int value)
    {
        switch (value)
        {
            case 1:
            {
                fsLow.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                fsMedium.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsHigh.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsAuto.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 2:
            {
                fsLow.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsMedium.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                fsHigh.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsAuto.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 3:
            {
                fsLow.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsMedium.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsHigh.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                fsAuto.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 4:
            {
                fsLow.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsMedium.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsHigh.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                fsAuto.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                break;
            }
        }
    }

    public void updateMode(int value)
    {
        switch (value)
        {
            case 1:
            {
                modeCool.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                modeHeat.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeVent.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeDry.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 2:
            {
                modeCool.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeHeat.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                modeVent.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeDry.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 3:
            {
                modeCool.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeHeat.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeVent.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                modeDry.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                break;
            }
            case 4:
            {
                modeCool.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeHeat.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeVent.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn2));
                modeDry.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.btn3));
                break;
            }
        }
    }

    public void powerStatus(int value)
    {
        if(value==1)
        {
            powerOn =1;
            powerBtm.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.btn3), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else
        {
            powerOn=0;
            powerBtm.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }


}