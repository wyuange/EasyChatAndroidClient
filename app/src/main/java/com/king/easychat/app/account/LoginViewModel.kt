package com.king.easychat.app.account

import android.app.Application
import com.king.easychat.app.Constants
import com.king.easychat.netty.NettyClient
import com.king.easychat.netty.packet.req.LoginReq
import com.king.frame.mvvmframe.base.BaseModel
import com.king.frame.mvvmframe.base.DataViewModel
import io.netty.channel.ChannelFuture
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import javax.inject.Inject

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class LoginViewModel @Inject constructor(application: Application, model: BaseModel?) : DataViewModel(application, model){

    val genericFutureListener by lazy {
        GenericFutureListener<ChannelFuture> {
            if(!it.isSuccess){
                sendSingleLiveEvent(Constants.EVENT_SUCCESS)
            }
            hideLoading()
        }
    }

    override fun onCreate() {
        super.onCreate()
        NettyClient.INSTANCE.connect()

        NettyClient.INSTANCE.addListener(genericFutureListener)
    }

    override fun onDestroy() {
        NettyClient.INSTANCE.removeListener(genericFutureListener)
        super.onDestroy()
    }

    fun login(username: String,password: String){
        showLoading()
        NettyClient.INSTANCE.sendMessage(LoginReq(username,password))
    }
}