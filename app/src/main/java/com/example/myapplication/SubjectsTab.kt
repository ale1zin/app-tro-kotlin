package com.example.myapplication

import android.content.Context // Importar Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.SubjectsButtonAdapter
import com.example.myapplication.models.Subjects
import com.example.myapplication.utils.DisciplinaJsonReader
// Importar o ToggleGroup
import com.google.android.material.button.MaterialButtonToggleGroup

class SubjectsTab : Fragment() {

    // Views do layout
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var progressLoading: ProgressBar
    private lateinit var toggleGroupSort: MaterialButtonToggleGroup

    // Componentes para gerenciar disciplinas
    private lateinit var disciplinaReader: DisciplinaJsonReader
    private lateinit var adapter: SubjectsButtonAdapter
    private var disciplinas: List<Subjects> = emptyList()

    // Enum para controlar o modo de ordenação
    private enum class SortMode {
        BY_SEMESTER,
        BY_DISCIPLINE
    }

    // Variável para guardar o estado atual da ordenação
    private var currentSortMode = SortMode.BY_SEMESTER // Padrão

    // ADICIONADO: Constantes para SharedPreferences
    companion object {
        private const val PREFS_NAME = "SubjectsTabPreferences"
        private const val KEY_SORT_MODE = "sort_mode"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout XML para este fragmento
        return inflater.inflate(R.layout.tab_subjects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar componentes
        initializeViews(view)
        setupRecyclerView()
        setupDisciplinaReader()

        // MODIFICADO: Ordem de inicialização para carregar preferências
        // 1. Carrega o enum 'currentSortMode' salvo na memória
        loadSortPreference()
        // 2. Atualiza os botões (UI) para refletir o modo salvo
        applySortPreferenceToUI()
        // 3. Configura o listener para cliques futuros do usuário
        setupSortToggle()

        // 4. Carrega as disciplinas (agora usará o 'currentSortMode' correto desde o início)
        loadDisciplinas()
    }

    private fun initializeViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_disciplinas)
        emptyStateLayout = view.findViewById(R.id.layout_empty_state)
        progressLoading = view.findViewById(R.id.progress_loading)
        toggleGroupSort = view.findViewById(R.id.toggle_group_sort)
    }

    private fun setupRecyclerView() {
        // Configurar adapter com callback para cliques nos botões
        adapter = SubjectsButtonAdapter(emptyList()) { disciplina ->
            onDisciplinaButtonClick(disciplina)
        }

        // Configurar RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupDisciplinaReader() {
        disciplinaReader = DisciplinaJsonReader()
    }

    // ADICIONADO: Nova função para carregar a preferência do SharedPreferences
    private fun loadSortPreference() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        // Busca o nome do enum salvo, usando 'BY_SEMESTER' como padrão se não houver nada
        val savedModeName = prefs.getString(KEY_SORT_MODE, SortMode.BY_SEMESTER.name)

        // Converte a string salva de volta para o enum
        currentSortMode = try {
            SortMode.valueOf(savedModeName ?: SortMode.BY_SEMESTER.name)
        } catch (e: IllegalArgumentException) {
            SortMode.BY_SEMESTER // Em caso de erro, volta ao padrão
        }

        Log.d("SUBJECTS_TAB", "Preferência de ordenação carregada: $currentSortMode")
    }

    // ADICIONADO: Nova função para atualizar a UI (botões)
    private fun applySortPreferenceToUI() {
        // Define qual botão deve estar marcado com base na preferência carregada
        val checkedId = when (currentSortMode) {
            SortMode.BY_DISCIPLINE -> R.id.btn_sort_disciplina
            SortMode.BY_SEMESTER -> R.id.btn_sort_semestre
        }
        // Atualiza o ToggleGroup.
        // Como chamamos isso ANTES de 'setupSortToggle', o listener não será disparado.
        toggleGroupSort.check(checkedId)
    }

    // MODIFICADO: 'setupSortToggle' agora também SALVA a preferência
    private fun setupSortToggle() {
        toggleGroupSort.addOnButtonCheckedListener { group, checkedId, isChecked ->
            // Só executa a lógica para o botão que foi MARCADO
            if (!isChecked) return@addOnButtonCheckedListener

            // Atualiza o modo atual baseado no clique
            currentSortMode = when (checkedId) {
                R.id.btn_sort_disciplina -> SortMode.BY_DISCIPLINE
                R.id.btn_sort_semestre -> SortMode.BY_SEMESTER
                else -> SortMode.BY_SEMESTER
            }

            Log.d("SUBJECTS_TAB", "Modo de ordenação alterado para: $currentSortMode")

            // ADICIONADO: Salva a nova escolha
            saveSortPreference()

            // Re-aplica a ordenação e atualiza a lista
            applySortAndUpdateList()
        }
    }

    // ADICIONADO: Nova função para salvar a preferência no SharedPreferences
    private fun saveSortPreference() {
        Log.d("SUBJECTS_TAB", "Salvando preferência: ${currentSortMode.name}")
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        // Salva o nome do enum (ex: "BY_SEMESTER") como string
        prefs.putString(KEY_SORT_MODE, currentSortMode.name)
        prefs.apply() // 'apply()' salva em segundo plano
    }


    private fun loadDisciplinas() {
        Log.d("SUBJECTS_TAB", "Iniciando carregamento de disciplinas...")

        // Mostrar loading
        showLoading(true)

        try {
            // Lista com todos os arquivos JSON de disciplinas na pasta assets
            val jsonFiles = listOf(
                "analise-de-circuitos-i.json",
                "analise-de-circuitos-ii.json",
                "analise-de-circuitos-iii.json",
                "analise-de-circuitos-iv.json",
                "eletricidade-i.json",
                "eletricidade-ii.json",
                "eletricidade-iii.json",
                "eletronica-digital-i.json",
                "eletronica-digital-ii.json",
                "eletronica-digital-iv.json",
                "eletronica-digital-v.json",
                "eletronica-geral-i.json",
                "eletronica-geral-ii.json",
                "eletronica-geral-iii.json",
                "eletronica-geral-v.json",
                "instrumentacao-industrial.json",
                "sistemas-de-controle.json",
                "sistemas-microprocessados-iii.json",
                "sistemas-microprocessados-iv.json",
                "sistemas-de-video.json",
                "eletronica-geral-iv.json",
                "eletronica-de-potencia-i.json",
                "eletronica-de-potencia-ii.json",
                // Adicione aqui outros arquivos conforme você tiver:
                // "eletronica-geral-iv.json",
            )

            Log.d("SUBJECTS_TAB", "Tentando carregar ${jsonFiles.size} arquivo(s)...")

            // Carregar disciplinas dos JSONs
            disciplinas = disciplinaReader.loadAllDisciplinas(requireContext(), jsonFiles)

            Log.d("SUBJECTS_TAB", "Carregadas ${disciplinas.size} disciplina(s) com sucesso!")

            // Atualizar UI
            showLoading(false)

            if (disciplinas.isNotEmpty()) {
                // Chamar a função de ordenação
                // Ela já usará o 'currentSortMode' que foi carregado das preferências
                applySortAndUpdateList()

            } else {
                // Mostrar estado vazio
                showEmptyState()
                Log.w("SUBJECTS_TAB", "Nenhuma disciplina foi carregada")
            }

        } catch (e: Exception) {
            Log.e("SUBJECTS_TAB", "Erro ao carregar disciplinas: ${e.message}", e)
            showLoading(false)
            showError("Erro ao carregar disciplinas: ${e.message}")
        }
    }

    // Função para aplicar a ordenação e atualizar o adapter
    private fun applySortAndUpdateList() {
        if (disciplinas.isEmpty()) {
            Log.d("SUBJECTS_TAB", "Nenhuma disciplina para ordenar.")
            if (progressLoading.visibility == View.GONE) {
                showEmptyState()
            }
            return
        }

        // Criar a lista ordenada com base no modo atual
        val sortedList = when (currentSortMode) {
            SortMode.BY_SEMESTER -> {
                Log.d("SUBJECTS_TAB", "Ordenando por SEMESTRE...")
                disciplinas.sortedWith(compareBy<Subjects> { it.semmester }.thenBy { it.name })
            }
            SortMode.BY_DISCIPLINE -> {
                Log.d("SUBJECTS_TAB", "Ordenando por DISCIPLINA (nome)...")
                disciplinas.sortedBy { it.name }
            }
        }

        Log.d("SUBJECTS_TAB", "Atualizando adapter com ${sortedList.size} disciplinas ordenadas.")

        // Atualiza o adapter com a lista ordenada
        adapter.updateDisciplinas(sortedList)
        showDisciplinas()

        // Log detalhado das disciplinas ordenadas (para debug)
        sortedList.forEachIndexed { index, disciplina ->
            Log.d(
                "SUBJECTS_TAB",
                "${index + 1}. [${disciplina.semmester}° sem] ${disciplina.name}"
            )
        }
    }

    private fun onDisciplinaButtonClick(disciplina: Subjects) {
        Log.d("DISCIPLINA_CLICK", "Clicou na disciplina: ${disciplina.name}")

        try {

            val fileName = "${disciplina.slug}.json"

            val intent = Intent(requireContext(), FormulasActivity::class.java).apply {
                putExtra("disciplina_arquivo_json", fileName)
                putExtra("disciplina_nome", disciplina.name)
            }
            startActivity(intent)

        } catch (e: Exception) {
            Log.e("DISCIPLINA_CLICK", "Erro ao abrir FormulasActivity: ${e.message}", e)
            Toast.makeText(
                requireContext(),
                "Erro ao abrir fórmulas: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showLoading(show: Boolean) {
        progressLoading.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        toggleGroupSort.visibility = if (show) View.GONE else View.VISIBLE
        emptyStateLayout.visibility = View.GONE
    }

    private fun showDisciplinas() {
        recyclerView.visibility = View.VISIBLE
        toggleGroupSort.visibility = View.VISIBLE
        emptyStateLayout.visibility = View.GONE
        progressLoading.visibility = View.GONE
    }

    private fun showEmptyState() {
        recyclerView.visibility = View.GONE
        toggleGroupSort.visibility = View.VISIBLE
        emptyStateLayout.visibility = View.VISIBLE
        progressLoading.visibility = View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        showEmptyState()
    }


    fun refreshDisciplinas() {
        loadDisciplinas()
    }

    fun getDisciplinaBySlug(slug: String): Subjects? {
        return disciplinas.find { it.slug == slug }
    }
}