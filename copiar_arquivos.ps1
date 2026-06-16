# copiar_arquivos.ps1
$raiz = "c:\Users\Larissa\Programming_Languages\Jflex-Java"
for ($i = 1; $i -le 10; $i++) {
    $dest = "$raiz\bin\roteiro_$i\bin"
    if (-not (Test-Path $dest)) {
        New-Item -ItemType Directory -Path $dest -Force | Out-Null
    }
    Copy-Item "$raiz\Scanner.flex" -Destination $dest -Force
    Copy-Item "$raiz\bin\Parser.cup" -Destination $dest -Force
    Write-Host "Roteiro $i - Arquivos copiados com sucesso."
}
Write-Host "Sincronizacao concluida!"
