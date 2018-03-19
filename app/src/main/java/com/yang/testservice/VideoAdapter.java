package com.yang.testservice;

/**
 * Created by liu on 2017/7/18.
 */

import java.util.List;

import com.yang.testservice.TextureVideoView.MediaState;
import com.yang.testservice.TextureVideoView.OnStateChangeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{

    List<String> mVideos;
    Context mContext;
    String TAG = "VideoAdapter";
    private VideoViewHolder lastPlayVideo=null;
    public VideoAdapter(Context context,List<String> videos) {
        mContext=context;
        mVideos=videos;

    }

    class VideoViewHolder extends ViewHolder{
        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        TextureVideoView videoView;
        ImageView imvPreview;
        ImageView imvPlay;
        ProgressBar pbWaiting;
        ProgressBar pbProgressBar;
        TextView textView;

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final VideoViewHolder viewHolder, final int position) {
        viewHolder.videoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mVideos.get(position).length()==0){
                    Toast.makeText(mContext, "视频地址不能为空，请在Activity中设置视频地址哦",Toast.LENGTH_LONG).show();
                    return;
                }

                if(lastPlayVideo==null)
                {
                    lastPlayVideo=viewHolder;
                }else if (!viewHolder.equals(lastPlayVideo)) {
                    lastPlayVideo.videoView.stop();
                    lastPlayVideo.pbWaiting.setVisibility(View.GONE);
                    lastPlayVideo.imvPlay.setVisibility(View.VISIBLE);
                  /*  Log.i(TAG,"33333333333333333333333333333333333333");


                    MediaMetadataRetriever media = new MediaMetadataRetriever();//获取视频第一帧
                    String path = "http://ips.ifeng.com/video19.ifeng.com/video09/2017/05/24/4664192-102-008-1012.mp4";
                    media.setDataSource(path);

                    Bitmap bitmap = media.getFrameAtTime();//获取第一帧图片

                    //media.release();//释放资源

                    if(bitmap!=null){
                        lastPlayVideo.imvPreview.setImageBitmap(bitmap);
                        Log.i(TAG,"111111111111111111111111111111111");
                    }else{
                        Log.i(TAG,"222222222222222222222222222222222");

                    }*/

                    lastPlayVideo = viewHolder;

                }
                TextureVideoView textureView = (TextureVideoView) v;
                if(textureView.getState()==MediaState.INIT||textureView.getState()==MediaState.RELEASE)
                {
                    textureView.play(mVideos.get(position));
                    viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                    viewHolder.imvPlay.setVisibility(View.GONE);

                   /* Log.i(TAG,"33333333333333333333333333333333333333");
                    MediaMetadataRetriever media = new MediaMetadataRetriever();//获取视频第一帧
                    //String path = "http://ips.ifeng.com/video19.ifeng.com/video09/2017/05/24/4664192-102-008-1012.mp4";
                    //media.setDataSource();
                    Bitmap bitmap = media.getFrameAtTime();//获取第一帧图片

                    //media.release();//释放资源

                    if(bitmap!=null){
                        lastPlayVideo.imvPreview.setImageBitmap(bitmap);
                        Log.i(TAG,"111111111111111111111111111111111");
                    }else{
                        Log.i(TAG,"222222222222222222222222222222222");

                    }*/

                }else if(textureView.getState()==MediaState.PAUSE)
                {
                    Log.i(TAG,"00000000000000000000000000000000000000000");

                    textureView.start();
                    viewHolder.pbWaiting.setVisibility(View.GONE);
                    viewHolder.imvPlay.setVisibility(View.GONE);
                }else if(textureView.getState()==MediaState.PLAYING)
                {
                    textureView.pause();
                    viewHolder.pbWaiting.setVisibility(View.GONE);
                    viewHolder.imvPlay.setVisibility(View.VISIBLE);
                }else if(textureView.getState()==MediaState.PREPARING)
                {
                    textureView.stop();
                    viewHolder.pbWaiting.setVisibility(View.GONE);
                    viewHolder.imvPlay.setVisibility(View.VISIBLE);
                }
            }
        });
        viewHolder.videoView.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onSurfaceTextureDestroyed(SurfaceTexture surface) {
                viewHolder.videoView.stop();
                viewHolder.pbWaiting.setVisibility(View.GONE);
                viewHolder.imvPlay.setVisibility(View.VISIBLE);
                viewHolder.pbProgressBar.setMax(1);
                viewHolder.pbProgressBar.setProgress(0);
                viewHolder.imvPreview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPlaying() {
                viewHolder.pbWaiting.setVisibility(View.GONE);
                viewHolder.imvPlay.setVisibility(View.GONE);
            }

            @Override
            public void onBuffering() {
                viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                viewHolder.imvPlay.setVisibility(View.GONE);
            }

            @Override
            public void onSeek(int max,int progress){
                viewHolder.imvPreview.setVisibility(View.GONE);
                viewHolder.pbProgressBar.setMax(max);
                viewHolder.pbProgressBar.setProgress(progress);
            }

            @Override
            public void onStop() {
                viewHolder.pbProgressBar.setMax(1);
                viewHolder.pbProgressBar.setProgress(0);
                viewHolder.pbWaiting.setVisibility(View.GONE);
                viewHolder.imvPlay.setVisibility(View.VISIBLE);
                viewHolder.imvPreview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPause() {
                viewHolder.pbWaiting.setVisibility(View.GONE);
                viewHolder.imvPlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextureViewAvaliable() {

            }

            @Override
            public void playFinish() {
                viewHolder.pbProgressBar.setMax(1);
                viewHolder.pbProgressBar.setProgress(0);
                viewHolder.imvPlay.setVisibility(View.GONE);
                viewHolder.imvPreview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPrepare() {

            }
        });
    }

    //创建新View，被LayoutManager所调用
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup root, int position) {
        View containerView=LayoutInflater.from(mContext).inflate(R.layout.videoitem, root, false);
        VideoViewHolder videoViewHolder=new VideoViewHolder(containerView);
        videoViewHolder.videoView=(TextureVideoView) containerView.findViewById(R.id.textureview);
        videoViewHolder.imvPreview=(ImageView) containerView.findViewById(R.id.imv_preview);
        videoViewHolder.imvPlay=(ImageView) containerView.findViewById(R.id.imv_video_play);
        videoViewHolder.pbWaiting=(ProgressBar) containerView.findViewById(R.id.pb_waiting);
        videoViewHolder.pbProgressBar=(ProgressBar) containerView.findViewById(R.id.progress_progressbar);

        videoViewHolder.textView=(TextView)containerView.findViewById(R.id.textView_id);
        return videoViewHolder;
    }
}