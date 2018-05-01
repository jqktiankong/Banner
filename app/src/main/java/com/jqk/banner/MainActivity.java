package com.jqk.banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jqk.bannerlibrary.view.BannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;
    private List<String> imgsPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerView = (BannerView) findViewById(R.id.bannerView);

        imgsPath = new ArrayList<String>();

        imgsPath.add("http://d.hiphotos.baidu.com/image/h%3D300/sign=6ba54520f7f2b211fb2e834efa816511/bd315c6034a85edff62cdd7045540923dc5475c4.jpg");
        imgsPath.add("http://f.hiphotos.baidu.com/image/h%3D300/sign=404a1782eb1190ef1efb94dffe1a9df7/3ac79f3df8dcd1007fde3f4e7e8b4710b9122f1b.jpg");
        imgsPath.add("http://e.hiphotos.baidu.com/image/h%3D300/sign=67599edcdd58ccbf04bcb33a29d9bcd4/aa18972bd40735fa324a79d792510fb30f240821.jpg");
        imgsPath.add("http://e.hiphotos.baidu.com/image/h%3D300/sign=95a13e933ad3d539de3d09c30a86e927/ae51f3deb48f8c54c8a5db4236292df5e0fe7f6c.jpg");

        bannerView.setData(imgsPath).start();
    }
}
