package com.tools.commonutility.facebookads

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.facebook.ads.*
import java.util.*
import kotlin.collections.ArrayList

class FacebookAdsUtility(private val context: Context, private val isDebugApp: Boolean, private val deviceIdHash: String?, private val installer: String?) {

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
        val adViewBanner = AdView(context, formattedPlacementId, AdSize.BANNER_HEIGHT_50)
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

    fun isAppDownloadFromPlayStore(): Boolean {
        // A list with valid installers package name
        val validInstallers: List<String> = ArrayList(Arrays.asList("com.android.vending", "com.google.android.feedback"))

        // The package name of the app that has installed your app
        //val installer = context.packageManager.getInstallerPackageName(context.packageName)

        // true if your app has been downloaded from Play Store
        return !TextUtils.isEmpty(installer) && validInstallers.contains(installer)
    }
}