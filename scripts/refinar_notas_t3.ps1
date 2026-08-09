$ErrorActionPreference = 'Stop'
Set-Location "c:/Antigravity/AD/AD_Ficheros_"

$targets = @(
  "docs/T3_Formatos_diferentes/conversion.md",
  "docs/T3_Formatos_diferentes/ficheros_intercambio.md",
  "docs/T3_Formatos_diferentes/seriaci_dobjectes.md"
)

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)

foreach($file in $targets){
  $full = Join-Path (Get-Location) $file
  $text = [System.IO.File]::ReadAllText($full, [System.Text.Encoding]::UTF8)
  $text = $text -replace "`r`n", "`n"
  $lines = [System.Collections.Generic.List[string]]($text -split "`n", 0, 'SimpleMatch')

  $out = New-Object System.Collections.Generic.List[string]
  $i = 0
  $changed = $false

  while($i -lt $lines.Count){
    $line = $lines[$i]

    if($line -match '^```kotlin\s*$'){
      $out.Add($line)
      $i++
      $ann = @{}

      while($i -lt $lines.Count -and $lines[$i] -notmatch '^```\s*$'){
        $codeLine = $lines[$i]
        $out.Add($codeLine)
        if($codeLine -match '//\s*\((\d+)\)!\s*$'){
          $n = [int]$matches[1]
          $snippet = ($codeLine -replace '\s*//\s*\(\d+\)!\s*$','').Trim()
          if($snippet.Length -gt 110){ $snippet = $snippet.Substring(0,110) + '...' }
          $ann[$n] = $snippet
        }
        $i++
      }

      if($i -lt $lines.Count){
        $out.Add($lines[$i])
        $i++
      }

      if($ann.Count -gt 0){
        $k = $i
        while($k -lt $lines.Count -and $lines[$k].Trim() -eq ''){ $k++ }

        $hasNotes = $false
        $k2 = $k
        while($k2 -lt $lines.Count -and $lines[$k2] -match '^\d+\.\s+'){
          $hasNotes = $true
          $k2++
        }

        if($hasNotes){
          $i = $k2
        }

        $out.Add('')
        foreach($n in ($ann.Keys | Sort-Object)){
          $snippet = $ann[$n].Replace('`','\`')
          $out.Add(([string]::Format('{0}. Operacion tecnica: `{1}`.', $n, $snippet)))
        }
        $changed = $true
      }

      continue
    }

    $out.Add($line)
    $i++
  }

  if($changed){
    $newText = ($out -join "`n")
    [System.IO.File]::WriteAllText($full, $newText, $utf8NoBom)
  }
}

Write-Output 'notes-refined'
