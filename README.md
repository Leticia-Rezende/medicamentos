<h1>Sistema de Farmácia</h1>
<h2>Objetivo:</h2>

<p align= "left"> Desenvolver um sistema em Java com interface gráfica JavaFX para gerenciar o estoque de
medicamentos de uma farmácia. O sistema deve aplicar conceitos de programação
orientada a objetos, manipulação de arquivos e processamento de dados usando recursos
modernos do Java.</p>

<h2>Principais funcionalidades:</h2>
<ol>
  <li>O sistema deve permitir as seguintes operações básicas:</li>
  <ul>
    <li>Cadastrar um novo medicamento com todos seus dados</li>
    <li>Excluir um medicamento existente</li>
    <li>Consultar um medicamento específico por código</li>
    <li>Listar todos os medicamentos cadastrados</li> <br>
  </ul>
  <li>O sistema deve armazenar os dados em um arquivo CSV separado por
ponto-e-vírgula ( ; )</li> <br>
  <li>O sistema deve fornecer relatórios utilizando a API Stream do Java (filter, map,
reduce, etc.)</li>
</ol>

<h2>Requisitos Técnicos:</h2>
<ol>
  <li>Validação de Dados:</li>
  <ul>
    <li>Código: formato definido (por exemplo, 7 caracteres alfanuméricos)</li>
    <li>Nome: não vazio, tamanho mínimo</li>
    <li>Data de validade: não pode ser data passada</li>
    <li>Quantidade em estoque: não negativa</li> 
    <li>Preço: valor positivo</li>
    <li>CNPJ: formato válido e verificação dos dígitos</li> <br>
  </ul>
   <li>Persistência:</li>
  <ul>
    <li>Os dados devem ser salvos em um arquivo CSV com campos separados por
ponto-e-vírgula</li>
    <li>O sistema deve carregar os dados do arquivo ao iniciar</li>
    <li>As alterações devem ser salvas no arquivo após cada operação</li><br>
  </ul>
 <li>Relatórios:</li>
  <ul>
    <li>Filtrar medicamentos próximos ao vencimento (próximos 30 dias)</li>
    <li>Filtrar medicamentos com estoque baixo (menos de 5 unidades)</li><br>
  </ul>
</ol>

<h2>Como executar o programa:</h2>
<ol>
  <li> Baixe o arquivo "medicamentos" em seu computador, usando o comando <I>git clone</I> </li>
  <li> Abre a pasta em uma IDE de sua preferência</li>
  <li> Mude o caminho do PathFXML para o caminho indicado até a pasta <I>view</I></li>
  <li>Em seguida, execute o programa</li>
</ol>
<h2>Programa aberto, e agora?</h2>
<ol>
  <li> Preencha as informações, tanto de <I>Medicamento</I> tanto de <I>Fornecedor</I> </li>
  <li> Clique no botão desejado no momento:</li>
      <ul>
        <li>Cadastrar</li>
        <li>Excluir</li>
        <li>Consultar</li>
        <li>Listar Medicamentos</li>
      </ul>
</ol>


