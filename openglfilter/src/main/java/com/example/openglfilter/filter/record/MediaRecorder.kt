package com.example.openglfilter.filter.record

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaMuxer
import android.opengl.EGLContext
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface

class MediaRecorder(
    val context: Context,
    val path: String,
    val glContext: EGLContext,
    val width: Int,
    val height: Int
) {

    private var speed = 1f
    private lateinit var mediaFormat: MediaFormat
    private lateinit var mediaCodec: MediaCodec

    private lateinit var mSurface: Surface

    private lateinit var mMuxer: MediaMuxer
    private lateinit var mHandler: Handler
    private lateinit var eglEvn: EGLEnv
    private var isStart = false
    private var mLastTimestamp = 0L

    private var track = 0

    fun start(speed: Float) {
        this.speed = speed
        mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height)
        mediaFormat.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1500_000)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25)
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10)

        mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)

        mSurface = mediaCodec.createInputSurface()

        mMuxer = MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

        mediaCodec.start()

        val mHandlerThread = HandlerThread("codec-gl")
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper)

        mHandler.post {
            eglEvn = EGLEnv(context, glContext, mSurface, width, height)
            isStart = true
        }
    }

    fun fireFrame(textureId: Int, timestamp: Long) {
        if (!isStart) return
        mHandler.post {
            eglEvn.draw(textureId, timestamp)
            codec(false)

        }
    }

    private fun codec(endOfStream: Boolean) {

//        数据什么时候
//        编码
        //给个结束信号
        if (endOfStream) {
            mediaCodec.signalEndOfInputStream()
        }
        while (true) {
            val bufferInfo = MediaCodec.BufferInfo()
            val outIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10_000)
            if (outIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                if (!endOfStream) break
            } else if (outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                val format = mediaCodec.outputFormat
                track = mMuxer.addTrack(format)
                mMuxer.start()
            } else if (outIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {

            } else {
                bufferInfo.presentationTimeUs = (bufferInfo.presentationTimeUs / speed).toLong()
                if (bufferInfo.presentationTimeUs <= mLastTimestamp) {
                    bufferInfo.presentationTimeUs = (mLastTimestamp + 1_000_000 / 25 / speed).toLong()
                }
                mLastTimestamp = bufferInfo.presentationTimeUs

                val byteBuffer = mediaCodec.getOutputBuffer(outIndex)
                //如果当前的buffer是配置信息，不管它 不用写出去
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                    bufferInfo.size = 0
                }
                if (bufferInfo.size != 0) {
                    byteBuffer?.position(bufferInfo.offset)
                    byteBuffer?.limit(bufferInfo.offset + bufferInfo.size)
                    mMuxer.writeSampleData(track, byteBuffer!!, bufferInfo)
                }
                mediaCodec.releaseOutputBuffer(outIndex, false)
                // 如果给了结束信号 signalEndOfInputStream
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    break
                }
            }
        }
    }

    fun stop() {
        isStart = false
        mHandler.post {
            codec(true)
            mediaCodec.stop()
            mediaCodec.release()

            mMuxer.stop()
            mMuxer.release()

            eglEvn.release()

            mHandler.looper.quitSafely()

        }
    }

}