package com.tools.commonutility.facebookads

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.facebook.ads.*
import java.util.*
import kotlin.collections.ArrayList

class FacebookAdsUtility(private val context: Context, private val isDebugApp: Boolean, private val deviceIdHash: String?, private val installer: String?) {

    private lateinit var adViewBanner: AdView
    private lateinit var adViewMedRect: AdView

    init {
        AudienceNetworkAds.initialize(context)
        if (isDebugApp) {
            Log.e("ANKUSH", "AudienceNetworkAds initialize")
        }
    }

    fun loadFacebookBannerAdSize50(placementId: String, containerView: LinearLayout) {
        if (!isDebugApp && !isAppDownloadFromPlayStore()) {
            return
        }
        var formattedPlacementId = placementId
        if (isDebugApp) {
            formattedPlacementId = "IMG_16_9_APP_INSTALL#$placementId"
        }
        adViewBanner = AdView(context, formattedPlacementId, AdSize.BANNER_HEIGHT_50)
        if (isDebugApp && !TextUtils.isEmpty(deviceIdHash)) {
            AdSettings.addTestDevice(deviceIdHash)
        }
        containerView.addView(adViewBanner)
        adViewBanner.loadAd()
        setAdListener(adViewBanner)
    }

    fun loadFacebookMedRect(placementId: String, containerView: LinearLayout) {
        if (!isDebugApp && !isAppDownloadFromPlayStore()) {
            return
        }
        var formattedPlacementId = placementId
        if (isDebugApp) {
            formattedPlacementId = "IMG_16_9_APP_INSTALL#$placementId"
        }
        adViewMedRect = AdView(context, formattedPlacementId, AdSize.RECTANGLE_HEIGHT_250)
        if (isDebugApp && !TextUtils.isEmpty(deviceIdHash)) {
            AdSettings.addTestDevice(deviceIdHash)
        }
        containerView.addView(adViewMedRect)
        adViewMedRect.loadAd()
        setAdListener(adViewMedRect)
    }

    private fun setAdListener(adView: AdView) {
        adView.setAdListener(object : AdListener {
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

    private fun isAppDownloadFromPlayStore(): Boolean {
        val validInstallers: List<String> = ArrayList(listOf("com.android.vending", "com.google.android.feedback"))
        return !TextUtils.isEmpty(installer) && validInstallers.contains(installer)
    }

    fun destroyFbAds() {
        if (::adViewBanner.isInitialized) {
            adViewBanner.destroy()
        }
        if (::adViewMedRect.isInitialized) {
            adViewMedRect.destroy()
        }
    }
}