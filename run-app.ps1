$envFile = Join-Path $PSScriptRoot ".env"

if (Test-Path $envFile) {
    Get-Content $envFile | Where-Object { $_ -match '^\s*[^#\s]' } | ForEach-Object {
        $name, $value = $_ -split '=', 2
        [System.Environment]::SetEnvironmentVariable($name.Trim(), $value.Trim(), "Process")
    }
}

mvn clean compile
mvn exec:java "-Dexec.mainClass=com.placement.App"
