package com.alvaroquintana.edadperruna.managers

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AdMobAdsClientTest {

    private val context = mockk<Context>(relaxed = true)
    private val activity = mockk<Activity>(relaxed = true)

    @Before
    fun setUp() {
        // android.util.Log is invoked indirectly via utils.log() — JVM unit tests
        // need it mocked or they throw "Method not mocked".
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `showRewardedIfAvailable does nothing when no ad has been loaded`() {
        val client = AdMobAdsClient(context)
        var rewarded = false

        client.showRewardedIfAvailable(activity) { rewarded = true }

        assertFalse(rewarded)
    }

    @Test
    fun `showRewardedIfAvailable shows the cached ad and forwards the reward callback`() {
        val client = AdMobAdsClient(context)
        val ad = mockk<RewardedAd>()
        val rewardItem = mockk<RewardItem>(relaxed = true)
        every { ad.show(activity, any()) } answers {
            secondArg<OnUserEarnedRewardListener>().onUserEarnedReward(rewardItem)
        }
        client.rewardedAd = ad

        var rewarded = false
        client.showRewardedIfAvailable(activity) { rewarded = true }

        verify { ad.show(activity, any()) }
        assertTrue(rewarded)
    }
}
