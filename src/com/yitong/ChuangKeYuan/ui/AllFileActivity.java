package com.yitong.ChuangKeYuan.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Say GoBay on 2016/8/9.
 * 因为魅族品牌手机无法获取视频以及其他数据，只能遍历其SD卡，然后筛选其中数据，进行选择上传；
 *
 *      这个只是获取文件类型的上传文件；
 */
public class AllFileActivity extends Activity {

    /**
     *数据源
     */
    private ListView allvideo_list;
    private ImageView allvideo_back;
    private TextView allvideo_nodata, allvideo_title;
    private LinearLayout all_file_pro;
    /**
     * int 标记；
     */
    private int ISDESTORY = 1, DESTORY = 0, ISFILE = 1, FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allvideo_list);
        initView();
    }

    private List<String> mlist;
    private Handler mHandler;

    /**
     * 初始化组件
     */
    private void initView() {

        allvideo_title = (TextView) findViewById(R.id.allvideo_title);
        allvideo_list = (ListView) findViewById(R.id.allvideo_list);
        allvideo_back = (ImageView) findViewById(R.id.allvideo_back);
        allvideo_nodata = (TextView) findViewById(R.id.allvideo_nodata);
        all_file_pro = (LinearLayout) findViewById(R.id.all_file_pro);


        allvideo_nodata.setText("对不起，没有找到可以上传的文件。");
        allvideo_title.setText("所有文件");
        //String path = "/storage/emulated/0/DCIM/Video/";
        String path = "/storage/emulated/0/";
        final File file = new File(path);

        if (file == null) {
            allvideo_nodata.setVisibility(View.VISIBLE);
            System.out.println("file does not exist");
        } else {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    mlist = (List<String>) msg.obj;
                    if (mlist.size() == 0) {
                        all_file_pro.setVisibility(View.GONE);
                        allvideo_nodata.setVisibility(View.VISIBLE);
                        allvideo_list.setVisibility(View.GONE);
                    } else {
                        all_file_pro.setVisibility(View.GONE);
                        allvideo_nodata.setVisibility(View.GONE);
                        allvideo_list.setVisibility(View.VISIBLE);
                        allvideo_list.setAdapter(new VideoAdapter(mlist));
                    }
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<String> list = new ArrayList<String>();
                    Utils.setList(list);
                    mlist = Utils.simpleScanning(file, ISDESTORY, ISFILE);
                    Message message = mHandler.obtainMessage();
                    message.obj = mlist;
                    mHandler.sendMessage(message);
                }
            }).start();
        }
        allvideo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        allvideo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //通过Intent对象返回结果，调用setResult方法
                intent.setData(Uri.parse(mlist.get(position)));
                setResult(-1, intent);
                setRequestedOrientation(11);
                finish();
            }
        });
    }

    /**
     * listview适配器
     */
    private class VideoAdapter extends BaseAdapter {
        List<String> list;

        public VideoAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewholder;
            if (convertView == null) {
                viewholder = new ViewHolder();
                convertView = LayoutInflater.from(AllFileActivity.this).inflate(R.layout.activity_all_video_item, null);
                viewholder.iv = (ImageView) convertView.findViewById(R.id.all_video_item_iv);
                viewholder.tv = (TextView) convertView.findViewById(R.id.all_video_item_tv);
                convertView.setTag(viewholder);
            } else {
                viewholder = (ViewHolder) convertView.getTag();
            }
            viewholder.iv.setImageBitmap(getVideoThumbnail(list.get(position)));
            viewholder.tv.setText(list.get(position).substring(list.get(position).lastIndexOf("/") + 1));
            return convertView;
        }

        class ViewHolder {
            ImageView iv;
            TextView tv;
        }

        public Bitmap getVideoThumbnail(String videoPath) {
            Bitmap bitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(videoPath);
                bitmap = retriever.getFrameAtTime();
            } catch (Exception e) {
            } finally {
                try {
                    retriever.release();
                } catch (RuntimeException e) {
                }
            }
            return bitmap;
        }
    }

}
