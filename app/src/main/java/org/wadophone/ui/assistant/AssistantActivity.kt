/*
 * Copyright (c) 2010-2023 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.wadophone.ui.assistant

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import kotlin.math.max
import org.wadophone.LinphoneApplication.Companion.coreContext
import org.wadophone.LinphoneApplication.Companion.corePreferences
import org.wadophone.R
import org.wadophone.compatibility.Compatibility
import org.linphone.core.tools.Log
import org.wadophone.databinding.AssistantActivityBinding
import org.wadophone.ui.GenericActivity

@UiThread
class AssistantActivity : GenericActivity() {
    companion object {
        private const val TAG = "[Assistant Activity]"

        // Activity was started only to request permissions (from the welcome tour),
        // leave it as soon as they've been granted instead of showing account screens
        const val PERMISSIONS_ONLY_EXTRA = "PermissionsOnly"
    }

    private lateinit var binding: AssistantActivityBinding

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navController = binding.assistantNavContainer.findNavController()
            if (navController.currentDestination?.id != navController.graph.startDestinationId) {
                navController.popBackStack()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (areAllPermissionsGranted() && intent.getBooleanExtra(PERMISSIONS_ONLY_EXTRA, false)) {
            Log.w(
                "$TAG Activity was only started to request permissions and they're already granted, leaving"
            )
            finish()
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.assistant_activity)
        binding.lifecycleOwner = this
        setUpToastsArea(binding.toastsArea)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = windowInsets.getInsets(WindowInsetsCompat.Type.ime())
            v.updatePadding(
                insets.left,
                insets.top,
                insets.right,
                max(insets.bottom, keyboard.bottom)
            )
            WindowInsetsCompat.CONSUMED
        }

        // Pick the graph's start destination synchronously, before the NavHostFragment
        // attaches anything, so we never briefly show a screen we're about to leave
        // (e.g. the Linphone account login form on the landing page).
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.assistant_nav_container) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.assistant_nav_graph)
        navGraph.setStartDestination(
            if (!areAllPermissionsGranted()) {
                Log.w("$TAG Not all required permissions are granted, starting on Permissions fragment")
                R.id.permissionsFragment
            } else if (corePreferences.assistantDirectlyGoToThirdPartySipAccountLogin) {
                Log.w(
                    "$TAG Configured to go directly to third-party SIP account login, skipping landing page"
                )
                R.id.thirdPartySipAccountLoginFragment
            } else {
                R.id.landingFragment
            }
        )
        navController.graph = navGraph

        coreContext.postOnCoreThread { core ->
            if (core.accountList.isEmpty()) {
                Log.i("$TAG No account configured, disabling back gesture")
                coreContext.postOnMainThread {
                    // Only allow to navigate back within the assistant nav controller,
                    // not to leave the AssistantActivity
                    onBackPressedDispatcher.addCallback(backPressedCallback)
                }
            }
        }

        coreContext.mdmConfigAppliedEvent.observe(this) {
            it.consume {
                Log.i("$TAG Managed configuration applied, checking for accounts")
                leaveAssistantIfAnAccountIsConfigured()
            }
        }

        coreContext.provisioningAppliedEvent.observe(this) {
            it.consume {
                Log.i("$TAG Provisioning applied, checking for accounts")
                leaveAssistantIfAnAccountIsConfigured()
            }
        }
    }

    private fun areAllPermissionsGranted(): Boolean {
        for (permission in Compatibility.getAllRequiredPermissionsArray()) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.w("$TAG Permission [$permission] hasn't been granted yet!")
                return false
            }
        }

        val granted = Compatibility.hasFullScreenIntentPermission(this)
        if (granted) {
            Log.i("$TAG All permissions have been granted!")
        }
        return granted
    }

    private fun leaveAssistantIfAnAccountIsConfigured() {
        coreContext.postOnCoreThread { core ->
            if (core.accountList.isNotEmpty()) {
                coreContext.postOnMainThread {
                    try {
                        Log.w("$TAG At least one account was found, leaving assistant")
                        finish()
                    } catch (ise: IllegalStateException) {
                        Log.e("$TAG Can't finish activity: $ise")
                    }
                }
            }
        }
    }
}
