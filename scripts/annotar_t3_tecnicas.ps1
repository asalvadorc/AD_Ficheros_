$ErrorActionPreference = 'Stop'
Set-Location "c:/Antigravity/AD/AD_Ficheros_"

$targets = @(
  "docs/T3_Formatos_diferentes/conversion.md",
  "docs/T3_Formatos_diferentes/ficheros_intercambio.md",
  "docs/T3_Formatos_diferentes/seriaci_dobjectes.md"
)

$utf8NoBom = New-Object System.Text.UTF8Encoding($false)

function IsExampleHeader([string]$line) {
  return ($line -match 'Ejemplo' -and $line -match '\.kt')
}

function Leading([string]$s) {
  if ($s -match '^(\s*)\S') { return $matches[1].Length }
  return 0
}

function IsCodeLike([string[]]$block) {
  $j = $block -join "`n"
  return ($j -match '\b(import|fun|val|var|if|for|while|try|catch|class|object|data class|Serializable|Paths|Files|FileReader|FileWriter|CSV|Json|Xml|ObjectOutputStream|ObjectInputStream|ByteBuffer|FileChannel|ImageIO|DocumentBuilder|Element|Mapper|readValue|writeValue)\b')
}

function ShouldAnnotate([string]$line) {
  $t = $line.Trim()
  if ($t -eq '' -or $t -match '^import\b' -or $t -match '^fun\s+main\b' -or $t -match '^fun\s+\w+\(' -or $t -match '^//|^/\*|^\*|^\*/' -or $t -eq '{' -or $t -eq '}') {
    return $false
  }
  if ($t -match '\b(print|println|printf|System\.out|System\.err)\b') {
    return $false
  }

  return (
    $t -match '\b(Paths|get\(|Path\.of|Files\.|FileReader|FileWriter|ObjectOutputStream|ObjectInputStream|readObject|writeObject|CSVReader|CSVWriter|csvReader|csvWriter|jacksonObjectMapper|ObjectMapper|XmlMapper|readValue|writeValue|registerKotlinModule|readTree|writeWithDefaultPrettyPrinter|toInt\(|toDouble\(|toFile\(|File\(|ByteBuffer|FileChannel|ImageIO|DocumentBuilderFactory|DocumentBuilder|TransformerFactory|createElement|appendChild|setAttribute|setTextContent|NodeList|JSONObject|JSONArray|put\(|getString\(|getInt\(|decodeFromString|encodeToString|Json\.|@Serializable|@JacksonXml|readAll|writeNext|withCSVParser|withSkipLines|split\(|lineSequence\()\b'
  )
}

function BuildNote([string]$line) {
  $t = ($line.Trim() -replace '\s+//.*$','')
  if ($t.Length -gt 110) { $t = $t.Substring(0,110) + '...' }
  $t = $t.Replace('`','\`')
  return "Ejecuta la operación técnica: `$t`."
}

function AnnotateCode([string[]]$code) {
  foreach ($l in $code) { if ($l -match '//\s*\(\d+\)!') { return @{ Code = $code; Notes = @(); Added = $false } } }

  $out = New-Object System.Collections.Generic.List[string]
  $notes = New-Object System.Collections.Generic.List[string]
  $n = 0

  foreach ($line in $code) {
    if (ShouldAnnotate $line) {
      $n++
      $out.Add($line + " // ($n)!")
      $notes.Add("$n. $(BuildNote $line)")
    } else {
      $out.Add($line)
    }
  }

  if ($n -eq 0) { return @{ Code = $code; Notes = @(); Added = $false } }
  return @{ Code = $out; Notes = $notes; Added = $true }
}

$changed = 0

foreach ($file in $targets) {
  $full = Join-Path (Get-Location) $file
  $text = [System.IO.File]::ReadAllText($full, [System.Text.Encoding]::UTF8)
  $text = $text -replace "`r`n", "`n"
  $lines = [System.Collections.Generic.List[string]]($text -split "`n", 0, 'SimpleMatch')

  $out = New-Object System.Collections.Generic.List[string]
  $i = 0
  $fileChanged = $false

  while ($i -lt $lines.Count) {
    $line = $lines[$i]
    $out.Add($line)

    if (-not (IsExampleHeader $line)) { $i++; continue }

    $k = $i + 1
    $codeStart = -1
    while ($k -lt $lines.Count) {
      if ($lines[$k] -match '^##\s+' -or ($k -gt $i + 1 -and (IsExampleHeader $lines[$k]))) { break }
      if ($lines[$k] -match '^```' -or $lines[$k] -match '^\s{4,}\S') { $codeStart = $k; break }
      $k++
    }

    if ($codeStart -lt 0) { $i++; continue }

    for ($m = $i + 1; $m -lt $codeStart; $m++) { $out.Add($lines[$m]) }

    if ($lines[$codeStart] -match '^```') {
      $out.Add($lines[$codeStart])
      $j = $codeStart + 1
      $code = New-Object System.Collections.Generic.List[string]
      while ($j -lt $lines.Count -and $lines[$j] -notmatch '^```') { $code.Add($lines[$j]); $j++ }
      $ann = AnnotateCode $code
      foreach ($cl in $ann.Code) { $out.Add($cl) }
      if ($j -lt $lines.Count) { $out.Add($lines[$j]); $j++ }
      if ($ann.Added) {
        $out.Add('')
        foreach ($nline in $ann.Notes) { $out.Add($nline) }
        $fileChanged = $true
      }
      $i = $j
      continue
    }

    $j = $codeStart
    $block = New-Object System.Collections.Generic.List[string]
    while ($j -lt $lines.Count) {
      $cur = $lines[$j]
      if ($cur.Trim() -eq '' -or $cur -match '^\s{4,}') { $block.Add($cur); $j++ }
      else { break }
    }

    if (-not (IsCodeLike $block)) {
      for ($m = $codeStart; $m -lt $j; $m++) { $out.Add($lines[$m]) }
      $i = $j
      continue
    }

    $min = 999
    foreach ($b in $block) {
      if ($b.Trim() -ne '') {
        $ld = Leading $b
        if ($ld -lt $min) { $min = $ld }
      }
    }
    if ($min -eq 999) { $min = 0 }

    $ded = New-Object System.Collections.Generic.List[string]
    foreach ($b in $block) {
      if ($b.Trim() -eq '') { $ded.Add('') }
      elseif ($b.Length -ge $min) { $ded.Add($b.Substring($min)) }
      else { $ded.Add($b.TrimStart()) }
    }

    $ann = AnnotateCode $ded
    $out.Add('```kotlin')
    foreach ($cl in $ann.Code) { $out.Add($cl) }
    $out.Add('```')
    if ($ann.Added) {
      $out.Add('')
      foreach ($nline in $ann.Notes) { $out.Add($nline) }
    }
    $fileChanged = $true
    $i = $j
  }

  if ($fileChanged) {
    $newText = ($out -join "`n")
    [System.IO.File]::WriteAllText($full, $newText, $utf8NoBom)
    $changed++
  }
}

Write-Output "Files changed: $changed"
