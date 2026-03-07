package com.example.guesstheobject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.guesstheobject.R

class ModeSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_mode_selection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnFreeMode).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, DrawFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.btnLearnMode).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, LearnFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<View>(R.id.btnShapesMode).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ShapeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
