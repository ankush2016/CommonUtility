package com.tools.commonutility.facebookads

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.facebook.ads.AdSettings
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.AudienceNetworkAds

class FacebookAdsUtility(private val context: Context, private val isDebugApp: Boolean, private val deviceIdHash: String?) {

    init {
        AudienceNetworkAds.initialize(context)
        if (isDebugApp) {
            Log.e("ANKUSH", "AudienceNetworkAds initialize")
        }
    }

    fun loadFacebookBannerAdSize50(placementId: String, containerView: LinearLayout) {
        val adViewBanner = AdView(context, placementId, AdSize.BANNER_HEIGHT_50)
        if (isDebugApp && !TextUtils.isEmpty(deviceIdHash)) {
            AdSettings.addTestDevice(deviceIdHash)
        }
        containerView.addView(adViewBanner)
        adViewBanner.loadAd()
    }
}