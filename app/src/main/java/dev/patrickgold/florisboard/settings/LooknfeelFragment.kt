package dev.patrickgold.florisboard.settings

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.ime.text.keyboard.KeyboardMode
import dev.patrickgold.florisboard.ime.text.keyboard.KeyboardView
import dev.patrickgold.florisboard.ime.text.layout.LayoutManager
import kotlinx.coroutines.*

class LooknfeelFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var rootView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.settings_fragment_looknfeel, container, false) as LinearLayout

        launch(Dispatchers.Default) {
            val themeContext = ContextThemeWrapper(context, R.style.KeyboardTheme_MaterialLight)
            val layoutManager = LayoutManager(themeContext)
            layoutManager.autoFetchAssociationsFromPrefs()
            val keyboardView = KeyboardView(themeContext)
            keyboardView.isPreviewMode = true
            keyboardView.setKeyboardMode(KeyboardMode.CHARACTERS, layoutManager)
            keyboardView.updateVariation()
            val cardLinearLayout = rootView.findViewById<LinearLayout>(R.id.settings__looknfeel__card_linear_layout)
            withContext(Dispatchers.Main) {
                cardLinearLayout.addView(keyboardView, 0)
            }
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.settings__looknfeel__frame_container, PrefFragment())
        //transaction.addToBackStack(null)
        transaction.commit()

        return rootView
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    class PrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.prefs_looknfeel, rootKey)
        }
    }
}