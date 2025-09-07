package com.example.myapplication // Verifique se o nome do pacote está correto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeTab : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla (cria) o layout XML para este fragmento
        return inflater.inflate(R.layout.tab_home, container, false)
    }
}