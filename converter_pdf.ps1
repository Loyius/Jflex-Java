# converter_pdf.ps1
# Script para facilitar a conversão dos relatórios de Markdown (.md) para PDF.
# Oferece opções usando Pandoc, md-to-pdf (via npm) ou orientações para extensões do VS Code.

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "   CONVERSOR DE DOCUMENTOS DO COMPILADOR JAVA-- PARA PDF  " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host ""

$raiz = "c:\Users\Larissa\Programming_Languages\Jflex-Java"
$arquivos = @(
    "bin\documentacao_parser.md"
)
for ($i = 1; $i -le 10; $i++) {
    $arquivos += "bin\roteiro_$i\bin\documentacao_roteiro_$i.md"
}

# 1. Verifica se o Pandoc está instalado
$hasPandoc = $null -ne (Get-Command pandoc -ErrorAction SilentlyContinue)
# 2. Verifica se o Node.js/NPM está instalado
$hasNpm = $null -ne (Get-Command npm -ErrorAction SilentlyContinue)

if ($hasPandoc) {
    Write-Host "[OK] Pandoc detectado! Iniciando conversão de arquivos..." -ForegroundColor Green
    foreach ($arq in $arquivos) {
        $caminhoMd = Join-Path $raiz $arq
        $caminhoPdf = $caminhoMd.Replace(".md", ".pdf")
        if (Test-Path $caminhoMd) {
            Write-Host "Convertendo: $arq -> $(Split-Path $caminhoPdf -Leaf)"
            # Executa pandoc (requer weasyprint, wkhtmltopdf ou pdfengine padrão instalado, ex: MikTeX)
            try {
                pandoc $caminhoMd -o $caminhoPdf --pdf-engine=weasyprint
            } catch {
                try {
                    pandoc $caminhoMd -o $caminhoPdf
                } catch {
                    Write-Host "Erro ao gerar PDF para $arq com Pandoc. Certifique-se de que tem um PDF engine instalado (MikTeX, wkhtmltopdf ou weasyprint)." -ForegroundColor Yellow
                }
            }
        }
    }
}
elseif ($hasNpm) {
    Write-Host "[INFO] NPM detectado. Você pode utilizar a ferramenta 'md-to-pdf' para converter os arquivos." -ForegroundColor Cyan
    Write-Host "Para instalar e converter automaticamente todos os arquivos, execute:" -ForegroundColor Yellow
    Write-Host "  npx md-to-pdf --help (para testar)"
    Write-Host "Ou instale globalmente: npm install -g md-to-pdf"
    Write-Host ""
    Write-Host "Comando para conversão em lote:" -ForegroundColor Yellow
    foreach ($arq in $arquivos) {
        $caminhoMd = Join-Path $raiz $arq
        Write-Host "  npx md-to-pdf $caminhoMd"
    }
}
else {
    Write-Host "[AVISO] Nenhuma ferramenta de linha de comando automática (Pandoc ou NPM) foi detectada." -ForegroundColor Yellow
    Write-Host "Recomendamos usar uma das seguintes alternativas manuais:" -ForegroundColor White
    Write-Host ""
    Write-Host "Opção A: Extensão 'Markdown PDF' no VS Code (Recomendada)" -ForegroundColor Green
    Write-Host "  1. Abra a aba de Extensões no VS Code (Ctrl+Shift+X)."
    Write-Host "  2. Procure por 'Markdown PDF' (de yzane) e clique em Instalar."
    Write-Host "  3. Abra qualquer um dos arquivos .md gerados."
    Write-Host "  4. Clique com o botão direito no editor e selecione 'Markdown PDF: Export (pdf)'."
    Write-Host ""
    Write-Host "Opção B: Extensão 'Markdown All in One' ou exportação nativa de Markdown" -ForegroundColor Green
    Write-Host "  1. Abra o arquivo .md no VS Code."
    Write-Host "  2. Abra o Preview (Ctrl+K V)."
    Write-Host "  3. Pressione Ctrl+Shift+P, digite 'Print' ou use a opção de impressão para PDF do navegador."
}

Write-Host ""
Write-Host "----------------------------------------------------------"
Write-Host "Caminho dos arquivos Markdown gerados:" -ForegroundColor Cyan
foreach ($arq in $arquivos) {
    if (Test-Path (Join-Path $raiz $arq)) {
        Write-Host "  - [MD] $raiz\$arq" -ForegroundColor Gray
    }
}
Write-Host "----------------------------------------------------------"
