import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R
import com.example.guesstheobject.ui.DrawFragment
import com.example.guesstheobject.ui.LearnFragment

class ModeSelectionFragment : Fragment(R.layout.fragment_mode_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.findViewById<Button>(R.id.btnFreeMode).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, DrawFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.btnLearnMode).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, LearnFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}