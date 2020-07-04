package com.tools.commonutility.facebookads

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.facebook.ads.*

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
        adViewBanner.setAdListener(object : AdListener {
            override fun onAdClicked(p0: Ad?) {
            }

            override fun onError(p0: Ad?, p1: AdError?) {
                if (isDebugApp) {
                    Log.e("ANKUSH", "Fb - ${p1?.errorMessage}")
                }
            }

            override fun onAdLoaded(p0: Ad?) {
                if (isDebugApp) {
                    Log.e("ANKUSH", "Ad loaded")
                }
            }

            override fun onLoggingImpression(p0: Ad?) {
            }

        })
    }
}