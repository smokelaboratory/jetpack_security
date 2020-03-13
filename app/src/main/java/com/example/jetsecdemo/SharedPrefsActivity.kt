package com.example.jetsecdemo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.android.synthetic.main.activity_shared_prefs.*

class SharedPrefsActivity : AppCompatActivity() {

    private val savedValueKey = "saved_value"
    private lateinit var securedSharedPrefs: SharedPreferences
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_prefs)

        init()

        listeners()
    }

    private fun init() {
        /**
         * hardcoded key =>
         *
         * val masterKey = "master_key"
         */

        /**
         * recommended way for general purposes
         */
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        /**
         * Custom key for more security =>
         *
         * val masterKey = MasterKeys.getOrCreate(
         *     KeyGenParameterSpec.Builder(
         *         "key_alias",
         *         KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
         *     ).apply {
         *         setKeySize(256)
         *         setBlockModes(KeyProperties.BLOCK_MODE_GCM)
         *         setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
         *     }.build()
         * )
         */

        sharedPrefs = getSharedPreferences("values", Context.MODE_PRIVATE)

        securedSharedPrefs = EncryptedSharedPreferences.create(
            "values_secured",   //xml file name
            masterKey,   //master key
            this,   //context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,  //key encryption technique
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM //value encryption technique
        )

        setUi()
    }

    private fun listeners() {
        bt_save.setOnClickListener {
            if (isSavingValid()) {
                sharedPrefs.edit().putString(savedValueKey, et_data.text.toString()).apply()
                setUi()
            }
        }

        bt_save_encrypted.setOnClickListener {
            if (isSavingValid()) {
                securedSharedPrefs.edit().putString(savedValueKey, et_data.text.toString()).apply()
                setUi()
            }
        }
    }

    private fun setUi() {
        tv_normal_value.text = getString(R.string.value_x, sharedPrefs.getString(savedValueKey, ""))
        tv_encrypted_value.text =
            getString(R.string.value_jetpack_x, securedSharedPrefs.getString(savedValueKey, ""))
    }

    private fun isSavingValid(): Boolean {
        return if (et_data.text.toString().isNotEmpty())
            true
        else {
            Toast.makeText(this, getString(R.string.enter_any_value), Toast.LENGTH_LONG).show()
            false
        }
    }
}
