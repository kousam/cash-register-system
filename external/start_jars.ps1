$JavaPath = ""

# Locate Java 8
$JavaVersions = Get-Command -All -Name java
foreach ($element in $JavaVersions) {
    if ($element.Version.Major -eq 8) {
        $JavaPath = $element.Source
        break
    }
}

# Show error if Java wasn't found
if (!$JavaPath) {
    Write-Host "Failed to locate Java 8!"
    return
}

# Start jars
Start-Process -FilePath $JavaPath -ArgumentList "-jar", "CardReader.jar"
Start-Process -FilePath $JavaPath -ArgumentList "-jar", "CashBox.jar"
Start-Process -FilePath $JavaPath -ArgumentList "-jar", "CustomerRegister.jar"
Start-Process -FilePath $JavaPath -ArgumentList "-jar", "ProductCatalog.jar"