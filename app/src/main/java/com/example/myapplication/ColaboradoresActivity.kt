package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri // 1. IMPORT NECESSÁRIO
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Desenvolvedor
import com.example.myapplication.models.TipoDesenvolvedor

// Herda de AppCompatActivity e implementa DevActionsListener
class ColaboradoresActivity : AppCompatActivity(), DevActionsListener {

    private lateinit var recyclerViewColaboradores: RecyclerView
    private lateinit var colaboradorAdapter: DesenvolvedorAdapter
    private lateinit var btnVoltar: ImageButton

    // usa onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o layout da Activity
        setContentView(R.layout.activity_colaboradores)

        // Configurar o Botão Voltar
        btnVoltar = findViewById(R.id.btn_voltar_colaboradores)
        btnVoltar.setOnClickListener {
            // Em Activities, usamos finish() para voltar
            finish()
        }

        // Inicializar RecyclerView
        recyclerViewColaboradores = findViewById(R.id.recycler_view_colaboradores)
        recyclerViewColaboradores.layoutManager = LinearLayoutManager(this) // usa 'this'

        // Carregar os dados
        val listaColaboradores = carregarColaboradores()

        // Instanciar o DesenvolvedorAdapter, passando 'this' como listener
        colaboradorAdapter = DesenvolvedorAdapter(listaColaboradores, this)
        recyclerViewColaboradores.adapter = colaboradorAdapter
    }

    private fun carregarColaboradores(): List<Desenvolvedor> {
        // (Sua lista de colaboradores ATUALIZADA)
        return listOf(
            Desenvolvedor(
                id = "c1",
                nome = "Mateus Mendes Gonçalves",
                fotoUrl = "ic_c1",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "mateusgoncalves@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = "https://www.linkedin.com/in/mateusmgoncalves/",
                instagramUrl = null
            ),
            Desenvolvedor(
                id = "c2",
                nome = "Igor da Rocha Barros",
                fotoUrl = "ic_c2",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "igorbarros@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = "https://www.instagram.com/professor_xiru/"
            ),
            Desenvolvedor(
                id = "c5",
                nome = "Isis Duarte Bender",
                fotoUrl = "ic_c5",
                funcao = "Professora do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "isisbender@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = null
            ),
            Desenvolvedor(
                id = "c6",
                nome = "Sandro Vilela da Silva",
                fotoUrl = "ic_c6",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "sandrosilva@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = null
            ),
            Desenvolvedor(
                id = "c3",
                nome = "Gustavo Buchweitz Giusti",
                fotoUrl = "ic_c3",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "gustavogiusti@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = null
            ),
            Desenvolvedor(
                id = "c4",
                nome = "Guilherme Schwanke Cardoso",
                fotoUrl = "ic_c4",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "guilhermecardoso@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = null,
                instagramUrl = null
            ),
            Desenvolvedor(
                id = "c7",
                nome = "Rafael Galli",
                fotoUrl = "ic_c7",
                funcao = "Professor do Curso de Eletrônica",
                tipo = TipoDesenvolvedor.PROFESSOR,
                email = "rafaelgalli@ifsul.edu.br",
                githubUrl = null,
                linkedinUrl = "https://www.linkedin.com/in/rafael-galli-964471318",
                instagramUrl = null
            )
        )
    }

    private fun fallbackEmailChooser(email: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Assunto do email")
        }

        try {
            // usa 'this' em vez de 'requireContext()'
            Toast.makeText(this, "Escolha seu aplicativo de email", Toast.LENGTH_SHORT).show()
            val chooser = Intent.createChooser(intent, "Enviar email usando:")
            startActivity(chooser)
            Log.d("ColaboradoresActivity", "AÇÃO: Chooser de email iniciado com sucesso")
        } catch (e: Exception) {
            Log.e("ColaboradoresActivity", "ERRO: Falha ao iniciar chooser de email", e)
            Toast.makeText(this, "Nenhum aplicativo de email encontrado.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEmailClick(email: String) {
        Log.d("ColaboradoresActivity", "AÇÃO: onEmailClick recebido para email: $email")

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            // 2. CORREÇÃO KTX
            data = "mailto:$email?subject=".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        try {
            startActivity(intent)
            Log.d("ColaboradoresActivity", "AÇÃO: EMAIL direto enviado")
        } catch (e: Exception) {
            Log.e("ColaboradoresActivity", "ERRO: $e")
            fallbackEmailChooser(email)
        }
    }

    override fun onGithubClick(githubUrl: String) {
        Log.d("ColaboradoresActivity", "GitHub click: $githubUrl")
        // 3. CORREÇÃO KTX
        val intent = Intent(Intent.ACTION_VIEW, githubUrl.toUri())
        startActivity(intent)
    }

    override fun onLinkedinClick(linkedinUrl: String) {
        Log.d("ColaboradoresActivity", "LinkedIn click: $linkedinUrl")
        // 4. CORREÇÃO KTX
        val intent = Intent(Intent.ACTION_VIEW, linkedinUrl.toUri())
        startActivity(intent)
    }

    override fun onInstagramClick(instagramUrl: String) {
        Log.d("ColaboradoresActivity", "Instagram click: $instagramUrl")
        // 5. CORREÇÃO KTX
        val intent = Intent(Intent.ACTION_VIEW, instagramUrl.toUri())
        startActivity(intent)
    }
}