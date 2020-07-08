package com.tools.commonutility.facebookads

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.LinearLayout
import com.facebook.ads.*

/*
    USAGE
    private lateinit var facebookAdsUtility: FacebookAdsUtility

    private fun setupFacebookAds() {
        facebookAdsUtility = FacebookAdsUtility(this, BuildConfig.DEBUG, AppConstants.TEST_DEVICE_ID_HASH, AppUtility.getInstallerPackageName(this))
        facebookAdsUtility.loadFacebookMedRect(getString(R.string.fb_med_rect_ad_id), facebookBannerAdContainer)
    }

    override fun onDestroy() {
        if (::facebookAdsUtility.isInitialized) {
            facebookAdsUtility.destroyFbAds()
        }
        super.onDestroy()
    }
*/
class FacebookAdsUtility(private val context: Context, private val isDebugApp: Boolean, private val deviceIdHash: String?, private val installer: String?) {

    private lateinit var adViewBanner: AdView
    private lateinit var adViewMedRect: AdView
    lateinit var interstitialAd: InterstitialAd
    lateinit var adListener: InterstitialAdListener

    lateinit var fbInterstitialAdListener: FbInterstitialAdListener
    var fbInterstitialAdData: FbInterstitialAdData<Any>? = null

    init {
        AudienceNetworkAds.initialize(context)
        if (isDebugApp) {
            Log.e("ANKUSH", "AudienceNetworkAds initialize")
            AdSettings.setTestMode(true)
        }
    }

    fun setupFacebookInterstitialAds(placementId: String) {
        if (!isDebugApp && !isAppDownloadFromPlayStore()) {
            return
        }
        var formattedPlacementId = placementId
        if (isDebugApp) {
            formattedPlacementId = "YOUR_PLACEMENT_ID"
            Log.d("ANKUSH", "formattedPlacementId = $formattedPlacementId")
        }
        interstitialAd = InterstitialAd(context, formattedPlacementId)
        adListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(p0: Ad?) {

            }

            override fun onAdClicked(p0: Ad?) {
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                fbInterstitialAdListener.onInterstitialDismissed(fbInterstitialAdData)
                ad?.loadAd()
            }

            override fun onError(p0: Ad?, p1: AdError?) {
                if (isDebugApp) {
                    Log.e("ANKUSH", "INTERSTITIAL AD ERROR - ${p1?.errorMessage}, Error Code = ${p1?.errorCode}")
                }
            }

            override fun onAdLoaded(p0: Ad?) {
                if (isDebugApp) {
                    Log.e("ANKUSH", "Interstitial Ad Loaded")
                }
            }

            override fun onLoggingImpression(p0: Ad?) {
            }
        }
        val loadConfig = interstitialAd.buildLoadAdConfig().withAdListener(adListener).build()
        interstitialAd.loadAd(loadConfig)
    }

    fun showFacebookInterstitialAd(fbInterstitialAdListener: FbInterstitialAdListener, fbInterstitialAdData: FbInterstitialAdData<Any>?) {
        this.fbInterstitialAdListener = fbInterstitialAdListener
        this.fbInterstitialAdData = fbInterstitialAdData
        if (isInterstitialAdLoaded()) {
            interstitialAd.show()
        }
    }

    fun isInterstitialAdLoaded(): Boolean {
        if (::interstitialAd.isInitialized && interstitialAd.isAdLoaded && !interstitialAd.isAdInvalidated) {
            return true
        }
        return false
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
            if (isDebugApp) {
                Log.e("ANKUSH", "destroyed facebook banner ad")
            }
        }
        if (::adViewMedRect.isInitialized) {
            adViewMedRect.destroy()
            if (isDebugApp) {
                Log.e("ANKUSH", "destroyed facebook med rect ad")
            }
        }
        if (::interstitialAd.isInitialized) {
            interstitialAd.destroy()
            if (isDebugApp) {
                Log.e("ANKUSH", "destroyed facebook interstitial ad")
            }
        }
    }
}