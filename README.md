![Beige Minimalist Branding Twitter Header](https://github.com/user-attachments/assets/790a998f-b7c1-432b-9a64-435e4ceca5f0)  

# APP-ELETRÔNICA

📱 Aplicativo educacional desenvolvido por alunos/estagiários do curso técnico de Eletrônica do [IFSUL Câmpus Pelotas](https://www.pelotas.ifsul.edu.br). Seu objetivo é facilitar o acesso a fórmulas, explicações e resumos de cada disciplina e dos principais conteúdos da área de eletrônica. O projeto atual é uma reformulação de um já [**existente**](https://github.com/YuriXbr/eletronica-app).

---

## ✨ Funcionalidades

> ⚠️ *Algumas funcionalidades ainda estão em desenvolvimento.*

- 🏠 **Página Inicial:** Carrossel de fórmulas favoritas, acesso rápido às últimas visualizadas e frases motivacionais.
- 📒 **Conteúdo Didático:** Listagem completa de fórmulas organizadas por disciplinas e semestres.
- 🔍 **Busca Inteligente:** Pesquisa rápida por nome de fórmula, disciplina ou tags.
- 📠 **Calculadora de Resistores:** Ferramenta interativa para identificar valores de resistores pelas cores (4 faixas) com validação da série E12.
- ⭐ **Favoritos:** Sistema para salvar e acessar rapidamente as fórmulas mais utilizadas.

---

## 🚀 Para o Futuro!

- 📈 Estatísticas de uso do usuário.
- 📚 Expansão para incluir disciplinas teóricas e conceituais (sem fórmulas).
- 📡 Integração com API para atualizações de conteúdo online.

---

## 🛠️ Tecnologias Utilizadas

O projeto utiliza a arquitetura nativa clássica do Android, priorizando compatibilidade e performance em dispositivos variados.

- **Linguagem:** `Kotlin`
- **IDE:** `Android Studio`
- **Interface (UI):** `XML Layouts` (View System com Material Design Components)
- **Navegação:** `ViewPager2` com `TabLayout` (Navegação por abas) e `Fragments`
- **Processamento de Dados:** - `Gson` (Leitura de conteúdo educacional via arquivos JSON locais)
  - `SharedPreferences` (Persistência leve para Favoritos, Recentes e Configurações)
- **Renderização:** `WebView` com `KaTeX` (Para exibição precisa de fórmulas matemáticas em LaTeX)
- **Imagens:** `Glide` (Carregamento e gerenciamento de mídia)

> Buscamos compatibilidade com a maior variedade de dispositivos Android (Min SDK 24 - Android 7.0).

---

## ⚙️ Instalação e Execução

1. Clone este repositório ou baixe o ZIP.  
2. Abra o projeto no **Android Studio**.  
3. Certifique-se de ter o **SDK Android atualizado** (mínimo API 24).  
4. Execute **Build > Run** ou pressione o botão de execução para instalar o aplicativo no emulador ou dispositivo conectado.

> [!WARNING]  
> ⚠️ *Evite utilizar versões do Android Studio ou Kotlin muito antigas e incompatíveis com o projeto, para prevenir erros de build.*  

> [!TIP]  
> 💡 *Na primeira execução, o Gradle pode demorar um pouco para sincronizar as dependências e indexar os assets.*

---

## 📌 Observações

Este projeto foi desenvolvido dentro de um período de estágio. **Caso esteja arquivado, verifique os FORKS** — pode haver versões atualizadas por novos estudantes da instituição.
