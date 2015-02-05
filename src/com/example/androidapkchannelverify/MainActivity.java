package com.example.androidapkchannelverify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String TAG = "test";
	
	private TextView mHintTextView;
	private ProgressBar mProgressBar;
	
	private Button mVerifyButton;
	
	private TextView mResultTextView;
	
	private static final String mApkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "jaxusApks/";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        
        initViews();
    }
    
    private void initViews() {
    	mHintTextView = (TextView) findViewById(R.id.textview_hint);
    	mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
   
    	mHintTextView.setVisibility(View.GONE);
    	mProgressBar.setVisibility(View.GONE);
    	mHintTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    	
    	mResultTextView = (TextView) findViewById(R.id.textview_result);
    	mResultTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    	
    	mVerifyButton = (Button) findViewById(R.id.verify_it);
    	
    	File file = new File(mApkPath);
    	
    	if (!file.exists()) {
    		file.mkdirs();
    	}
    	
    	mVerifyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mVerifyButton.setEnabled(false);
				mHintTextView.setVisibility(View.VISIBLE);
				mProgressBar.setVisibility(View.VISIBLE);
				mResultTextView.setText(null);
				mHintTextView.setText("开始验证..");
				new VerifyTask().execute();
			}
		});
    }
    
    private String getFileNameChannel(String fileName) {
    	if (fileName == null) {
    		return null;
    	}
    	
    	int start = fileName.indexOf("_");
    	int end = fileName.indexOf("_", start+1);
    	
    	if (start < 0 || end < 0) {
    		return null;
    	}
    	
    	return fileName.substring(start+1, end);
    }
    
    private static class VerifyEntity {
    	public String mFileName;
    	public String mChannel;
    	public String mStatus;
    	
    	@Override
    	public String toString() {
    		return "名:" + mFileName + "  渠道:" + mChannel + "   状态:" + mStatus + "\n\n";
    	}
    }
    
    private class VerifyResult {
    	public List<VerifyEntity> mFailedEntity = new ArrayList<MainActivity.VerifyEntity>();
    	public String mResultString;
    	public int mSuccess;
    	public int mFailed;
    }
    
    private class VerifyTask extends AsyncTask<Void, VerifyEntity, VerifyResult> {

		@Override
		protected VerifyResult doInBackground(Void... params) {
			VerifyResult result = new VerifyResult();
			File dir = new File(mApkPath);
			if (!dir.exists()) {
				result.mResultString = "验证失败,文件夹不存在" + mApkPath;
				return result;
			}
			
			File[] apks = dir.listFiles();
			
			if (apks == null || apks.length <= 0) {
				result.mResultString = "文件夹内没有apk" + mApkPath;
				return result;
			}
			
			for (int i=0;i<apks.length; ++i) {
				VerifyEntity verify = new VerifyEntity();
				verify.mFileName = apks[i].getName();
				verify.mChannel = ApkUtil.getApkMetaData(MainActivity.this, apks[i].getPath(), "UMENG_CHANNEL");
				String fileTrueChannel = getFileNameChannel(verify.mFileName);
				if (fileTrueChannel == null || verify.mChannel == null || !TextUtils.equals(verify.mChannel, fileTrueChannel)) {
					verify.mStatus = "渠道号错误";
					result.mFailed++;
					result.mFailedEntity.add(verify);
				} else {
					verify.mStatus= "验证成功";
					result.mSuccess++;
				}
				publishProgress(verify);
			}
			
			return result;
		}
    	
		@Override
		protected void onProgressUpdate(VerifyEntity... values) {
			
			VerifyEntity entity = values[0];
			
			mResultTextView.append(entity.toString());
		}
		
		@Override
		protected void onPostExecute(VerifyResult result) {
			mVerifyButton.setEnabled(true);
			mHintTextView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mHintTextView.setText(result.mResultString);
			mHintTextView.append("成功" + result.mSuccess + "\n");
			mHintTextView.append("失败" + result.mFailed + "\n");
			for (int i=0;i<result.mFailedEntity.size(); ++i) {
				mHintTextView.append(result.mFailedEntity.get(i).toString());
			}
		}
    }
}
