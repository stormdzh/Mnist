package com.stormdzh.tfmnist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.stormdzh.tfmnist.handwrite.MyPaintView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean isRegression = false; //true 使用线性模型
    private static final String TAG = "MainActivity";
    //    private   String MODEL_FILE = "file:///android_asset/mnist_dzh.pb"; //模型存放路径
//    private   String MODEL_FILE = "file:///android_asset/mnist_regression.pb"; //模型存放路径
    private String MODEL_FILE = "file:///android_asset/mnist_convolutional.pb"; //模型存放路径
    private TextView tvResult;
    private ImageView imgPrevieww;
    private Bitmap bitmap;
    private PredictionTF preTF;
    private int index = 0;
    private MyPaintView mMyPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnTest).setOnClickListener(this);
        findViewById(R.id.btnClear).setOnClickListener(this);
        findViewById(R.id.btnOk).setOnClickListener(this);
        tvResult = (TextView) findViewById(R.id.tvResult);
        imgPrevieww = (ImageView) findViewById(R.id.imgPrevieww);
        mMyPaintView = findViewById(R.id.mMyPaintView);
        getBitmap();
        if (isRegression) {
            MODEL_FILE = "file:///android_asset/mnist_regression.pb"; //模型存放路径
        } else {
            MODEL_FILE = "file:///android_asset/mnist_convolutional.pb"; //模型存放路径
        }
        preTF = new PredictionTF(getAssets(), MODEL_FILE);//输入模型存放路径，并加载TensoFlow模型
    }


    private Bitmap getBitmap() {
        switch (index) {
            case 0:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n0);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n1);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n2);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n3);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n4);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n5);
                break;
            case 6:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n6);
                break;
            case 7:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n7);
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n8);
                break;
            case 9:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.n9);
                break;
        }
        imgPrevieww.setImageBitmap(bitmap);
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTest:
                if (index > 9)
                    index = 0;
                bitmap = getBitmap();
                Log.i(TAG, "sourceBitmap=>" + bitmap.getWidth() + " :" + bitmap.getHeight());
                index++;
                recogBitmap(bitmap);
                break;
            case R.id.btnClear:
                mMyPaintView.clear();
                break;
            case R.id.btnOk:
//                Bitmap viewBitmap = convertViewToBitmap(mMyPaintView);
                Bitmap viewBitmap = mMyPaintView.getBitMap();
//                imgPrevieww.setImageBitmap(viewBitmap);
                Bitmap finalBitmap = scaledBitmap(viewBitmap);
                imgPrevieww.setImageBitmap(finalBitmap);
                Log.i(TAG, "finalBitmap=>" + finalBitmap.getWidth() + " :" + finalBitmap.getHeight());
                recogBitmap(finalBitmap);
                break;
        }
    }

    private Bitmap scaledBitmap(Bitmap viewBitmap) {

        int width = viewBitmap.getWidth();
        float scale = 74f / width;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return Bitmap.createBitmap(viewBitmap, 0, 0, viewBitmap.getWidth(),
                viewBitmap.getHeight(), matrix, true);

    }

    private void recogBitmap(Bitmap bitmap) {
        String res = "图片识别结果为：";
        int[] result = preTF.getPredict(bitmap);
        for (int i = 0; i < result.length; i++) {
            Log.i(TAG, res + result[i]);
            res = res + String.valueOf(result[i]) + " ";
        }
        tvResult.setText(res);
    }

    public Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }


}
