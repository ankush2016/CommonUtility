package com.tools.commonutility.facebookads

interface FbInterstitialAdListener {
    fun onInterstitialDismissed(fbInterstitialAdData: FbInterstitialAdData<Any>?)
}