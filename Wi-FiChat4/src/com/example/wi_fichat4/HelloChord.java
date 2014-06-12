package com.example.wi_fichat4;

import java.util.List;

import android.app.Activity;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.ChordManager.INetworkListener;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class HelloChord extends Activity {
	
	     String CHORD_HELLO_TEST_CHANNEL;

	     String CHORD_SAMPLE_MESSAGE_TYPE="message";
	     String username;

	     ChordManager mChordManager = null;

	     ChordLogView mLogView;

	     Button mStart_stop_btn, send;
	    
	     EditText msg;

	     boolean bStarted = false;

	     int mSelectedInterface = -1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		Bundle extras = getIntent().getExtras();
		
		if(extras != null)
		{
			CHORD_HELLO_TEST_CHANNEL = extras.getString("groupname");
			username = extras.getString("name");
		}
		
		mLogView = (ChordLogView)findViewById(R.id.hello_log_text);
		
        mStart_stop_btn = (Button)findViewById(R.id.start);
        send = (Button)findViewById(R.id.send);
        msg = (EditText)findViewById(R.id.editText1);
        
        mStart_stop_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!bStarted) {
                   
                    mLogView.appendLog(" Start Chord!");
                    Toast.makeText(HelloChord.this,"start", 3000).show();
                   
                    startChord();
                } else {
                    
                    mLogView.appendLog("Stop Chord!");
                    stopChord();
                }
            }
        });
        
        send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(bStarted)
				{
					String m = msg.getText().toString();
					if(m != "")
					{
						byte[][] payload = new byte[2][];
			            payload[1] = m.getBytes();
			            payload[0] = username.getBytes();
						IChordChannel channel = mChordManager.getJoinedChannel(CHORD_HELLO_TEST_CHANNEL);
						
						channel.sendDataToAll(CHORD_SAMPLE_MESSAGE_TYPE, payload );
						
						msg.setText("");
						mLogView.appendLog("Me:\n"+m+"\n");
					}
					
				}
				else
				{
					Toast.makeText(HelloChord.this,"Start Chord First", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}

    

    @Override
    public void onDestroy() {
        
        if (mChordManager != null) {
        	mChordManager.close();
            mChordManager = null;
        }

        super.onDestroy();
    }

    private void initChord() {

        
        mChordManager = ChordManager.getInstance(HelloChord.this);                
       
        mChordManager.setHandleEventLooper(HelloChord.this.getMainLooper());

        mChordManager.setNetworkListener(new INetworkListener() {

            @Override
            public void onDisconnected(int interfaceType) {
                if (interfaceType == mSelectedInterface) {
                    Toast.makeText(HelloChord.this,
                            getInterfaceName(interfaceType) + " is disconnected",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConnected(int interfaceType) {
                if (interfaceType == mSelectedInterface) {
                    Toast.makeText(HelloChord.this,
                            getInterfaceName(interfaceType) + " is connected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getInterfaceName(int interfaceType) {
    	
        if (ChordManager.INTERFACE_TYPE_WIFI == interfaceType)
            return "Wi-Fi";
        else if (ChordManager.INTERFACE_TYPE_WIFIAP == interfaceType)
            return "Mobile AP";
        else if (ChordManager.INTERFACE_TYPE_WIFIP2P == interfaceType)
            return "Wi-Fi Direct";

        return "UNKNOWN";
    }

    private void startChord() {
    	
    	initChord();
        
        List<Integer> infList = mChordManager.getAvailableInterfaceTypes();
        
        if(infList.isEmpty()){
            mLogView.appendLog("	There is no available connection.");
            return;
        }
        
        int interfaceType = infList.get(0);
        
        int nError = mChordManager.start(interfaceType, mManagerListener);
        mSelectedInterface = interfaceType;
        
        mStart_stop_btn.setEnabled(false);

        if (ChordManager.ERROR_INVALID_STATE == nError) {
            mLogView.appendLog("    Invalid state!");
        } else if (ChordManager.ERROR_INVALID_INTERFACE == nError) {
            mLogView.appendLog("    Invalid connection!");
        } else if (ChordManager.ERROR_INVALID_PARAM == nError) {
            mLogView.appendLog("    Invalid argument!");
        } else if (ChordManager.ERROR_FAILED == nError) {
            mLogView.appendLog("    Fail to start! - internal error ");
        }
        

    }

   
    IChordManagerListener mManagerListener = new IChordManagerListener() {

        @Override
        public void onStarted(String nodeName, int reason) {
            
            bStarted = true;
            mStart_stop_btn.setText("Stop");
            mStart_stop_btn.setEnabled(true);

            if (reason == STARTED_BY_USER) {
                // Success to start by calling start() method
               
                joinTestChannel();
            } else if (reason == STARTED_BY_RECONNECTION) {
                // Re-start by network re-connection.
                
            }

        }

        @Override
        public void onStopped(int reason) {
            
            bStarted = false;
            mStart_stop_btn.setText("Start");
            mStart_stop_btn.setEnabled(true);

            if (STOPPED_BY_USER == reason) {
                // Success to stop by calling stop() method
                
            } else if (NETWORK_DISCONNECTED == reason) {
                // Stopped by network disconnected
               
            }
        }

        @Override
        public void onNetworkDisconnected() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onError(int error) {
            // TODO Auto-generated method stub
            
        }
    };

    private void joinTestChannel() {
        
    	IChordChannel channel = null;
        
        
        
        channel = mChordManager.joinChannel(CHORD_HELLO_TEST_CHANNEL, mChannelListener);

        if (channel == null) {
            mLogView.appendLog("    Fail to joinChannel");
        }
    }

    private void stopChord() {
        
    	if (mChordManager == null)
            return;

        
        mChordManager.setNetworkListener(null);

       
        
        mLogView.appendLog("Stop");
        mChordManager.stop();
        mStart_stop_btn.setEnabled(false);

    }

    
    private IChordChannelListener mChannelListener = new IChordChannelListener() {

       
        @Override
        public void onNodeLeft(String fromNode, String fromChannel) {
            
        }

        
        @Override
        public void onNodeJoined(String fromNode, String fromChannel) {
            

        	
            
            
            
        }

        
        @Override
        public void onDataReceived(String fromNode, String fromChannel, String payloadType,
                byte[][] payload) {

            
            if(payloadType.equals(CHORD_SAMPLE_MESSAGE_TYPE)){
                mLogView.appendLog(  new String(payload[0]) + ":\n " + new String( payload[1]) + "\n");
                
            }
            
        }

        
        @Override
        public void onMultiFilesWillReceive(String fromNode, String fromChannel, String fileName,
                String taskId, int totalCount, String fileType, long fileSize) {

        }

        @Override
        public void onMultiFilesSent(String toNode, String toChannel, String fileName,
                String taskId, int index, String fileType) {

        }

        @Override
        public void onMultiFilesReceived(String fromNode, String fromChannel, String fileName,
                String taskId, int index, String fileType, long fileSize, String tmpFilePath) {

        }

        @Override
        public void onMultiFilesFinished(String node, String channel, String taskId, int reason) {

        }

        @Override
        public void onMultiFilesFailed(String node, String channel, String fileName, String taskId,
                int index, int reason) {

        }

        @Override
        public void onMultiFilesChunkSent(String toNode, String toChannel, String fileName,
                String taskId, int index, String fileType, long fileSize, long offset,
                long chunkSize) {

        }

        @Override
        public void onMultiFilesChunkReceived(String fromNode, String fromChannel, String fileName,
                String taskId, int index, String fileType, long fileSize, long offset) {

        }

        @Override
        public void onFileWillReceive(String fromNode, String fromChannel, String fileName,
                String hash, String fileType, String exchangeId, long fileSize) {

        }

        @Override
        public void onFileSent(String toNode, String toChannel, String fileName, String hash,
                String fileType, String exchangeId) {

        }

        @Override
        public void onFileReceived(String fromNode, String fromChannel, String fileName,
                String hash, String fileType, String exchangeId, long fileSize, String tmpFilePath) {

        }

        @Override
        public void onFileFailed(String node, String channel, String fileName, String hash,
                String exchangeId, int reason) {

        }

        @Override
        public void onFileChunkSent(String toNode, String toChannel, String fileName, String hash,
                String fileType, String exchangeId, long fileSize, long offset, long chunkSize) {

        }

        @Override
        public void onFileChunkReceived(String fromNode, String fromChannel, String fileName,
                String hash, String fileType, String exchangeId, long fileSize, long offset) {

        }

    };

   
}
