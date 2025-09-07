package com.example.myapplication // Verifique se o nome do pacote está correto

import android.content.Context // <-- IMPORTAÇÃO ADICIONADA
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // <-- IMPORTAÇÃO ADICIONADA
import androidx.fragment.app.Fragment

class HomeTab : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla (cria) o layout XML para este fragmento. Esta parte está correta.
        return inflater.inflate(R.layout.tab_home, container, false)
    }

    /**
     * Este método é chamado logo após o onCreateView ter terminado.
     * É o lugar perfeito para encontrar as Views e configurar a lógica da UI.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Encontre o TextView no layout do fragmento pelo ID que você definiu.
        //    (Assumindo que o ID é 'welcome_home_textview' no arquivo tab_home.xml)
        val welcomeTextView = view.findViewById<TextView>(R.id.welcome_home_textview)


        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


        val userName = sharedPreferences.getString("user_name", "Usuário")

        // 4. Construa a mensagem de boas-vindas e a defina no TextView.
        welcomeTextView.text = "Olá, $userName!"
    }
}