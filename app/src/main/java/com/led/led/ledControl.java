package com.led.led;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnOn, btnOff, btnDis;
    SeekBar brightness;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean ache= false;
    private boolean checking= false;
    private boolean isBtConnected = false;
    private boolean asynctask_end = false;
    private String username="zim.",password="1234.";
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
       // btnOn = (Button)findViewById(R.id.button2);
        btnOff = (Button)findViewById(R.id.button3);
        btnDis = (Button)findViewById(R.id.button4);
        brightness = (SeekBar)findViewById(R.id.seekBar);
        lumn = (TextView)findViewById(R.id.lumn);
//        while(isBtConnected==false){
//            ConnectBT CBT = new ConnectBT(); //Call the class to connect
//            CBT.execute();
//            while(CBT.getStatus()!=AsyncTask.Status.FINISHED);
//        }


        //commands to be sent to bluetooth
        /*btnOn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                turnOnLed(username);      //method to turn on
            }
        });*/

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                turnOffLed(password);   //method to turn off
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

//        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser==true)
//                {
//                    lumn.setText(String.valueOf(progress));
//                    try
//                    {
//                        btSocket.getOutputStream().write(String.valueOf(progress).getBytes());
//                    }
//                    catch (IOException e)
//                    {
//
//                    }
//                }
//            }

//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
        boolean flg=true;
        while(flg) {
//            msg(String.valueOf(ache));
//            checkConnection check= new checkConnection();
//            check.execute();
//            while(checking);
//            msg(String.valueOf(ache));
            if(ache==false) {
                ConnectBT CBT = new ConnectBT();
                CBT.execute();
//                while(asynctask_end==false);
            }
            flg=false;
        }

    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void turnOffLed(String st)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(st.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed(String st)
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(st.toString().getBytes());


            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
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

    private void send_data(){
        turnOnLed(username);
    }


    private class checkConnection extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            msg(String.valueOf(ache));
            checking=true;
        }
        @Override
        protected Void doInBackground(Void... voids) {
//            ache= (btSocket==null);
            ache =true;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);
            checking=false;
        }


    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = false; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            boolean flg=true;
            while(flg)
            {
                while(btSocket!=null);
//                if(btSocket==null)
//                {
////                    msg("nai");
////                    isBtConnected=false;
////                    ConnectSuccess=false;
//                    progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
//                }
                while(btSocket == null || ConnectSuccess==false) {
                    try {
                        if (btSocket == null || !isBtConnected) {
                            myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                            BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                            btSocket.connect();//start connection
                            ConnectSuccess=true;
                        }
                    } catch (IOException e) {
                        ConnectSuccess = false;//if the try failed, you can check the exception here

                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(isBtConnected);
//                break;
                }
                msg("Connected.");
                isBtConnected = true;
                send_data();
//                progress.dismiss();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");

                //finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
                send_data();
            }
            asynctask_end=true;
            progress.dismiss();

        }
    }
}
