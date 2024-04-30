package com.imashik.rtmpstream.usbcamUtils;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;

import com.pedro.rtmp.utils.ConnectCheckerRtmp;
import com.pedro.rtmp.rtmp.RtmpClient;

import java.nio.ByteBuffer;
import com.pedro.rtmp.flv.video.ProfileIop;
/**
 * More documentation see:
 * {@link com.pedro.rtplibrary.base.Camera1Base}
 *
 * Created by pedro on 25/01/17.
 */

public class RtmpUSB extends USBBaseNew {

    private RtmpClient srsFlvMuxer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RtmpUSB(OpenGlView openGlView, ConnectCheckerRtmp connectChecker) {
        super(openGlView);
        srsFlvMuxer = new RtmpClient(connectChecker);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RtmpUSB(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker) {
        super(lightOpenGlView);
        srsFlvMuxer = new RtmpClient(connectChecker);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public RtmpUSB(Context context, ConnectCheckerRtmp connectChecker) {
        super(context);
        srsFlvMuxer = new RtmpClient(connectChecker);
    }

    /**
     * H264 profile.
     *
     * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
     */
    public void setProfileIop(ProfileIop profileIop) {
        srsFlvMuxer.setProfileIop(profileIop);
    }

    @Override
    public void setAuthorization(String user, String password) {
        srsFlvMuxer.setAuthorization(user, password);
    }

    @Override
    protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
        srsFlvMuxer.setAudioInfo(sampleRate,isStereo );
    }

    @Override
    protected void startStreamRtp(String url) {
        if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
            srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
        } else {
            srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
        }
        srsFlvMuxer.connect(url);
    }

    @Override
    protected void stopStreamRtp() {
        srsFlvMuxer.disconnect();
    }

    @Override
    protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        srsFlvMuxer.sendAudio(aacBuffer, info);
    }

    @Override
    protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
        srsFlvMuxer.setSPSandPPS(sps, pps,vps);
    }

    @Override
    protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        srsFlvMuxer.sendVideo(h264Buffer, info);
    }


}

