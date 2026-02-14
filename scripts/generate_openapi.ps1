param(
    [string]$InputSpec = "src/main/resources/openapi/appliance-api.yml"
)

$ErrorActionPreference = "Stop"

$outDir = "target/openapi-generated"

npx -y @openapitools/openapi-generator-cli generate `
  -g spring `
  -i $InputSpec `
  -o $outDir `
  --additional-properties=interfaceOnly=true,useSpringBoot3=true,useJakartaEe=true,useTags=true,apiPackage=com.example.demo.api,modelPackage=com.example.demo.api.model,invokerPackage=com.example.demo.api.invoker,openApiNullable=false,documentationProvider=none

New-Item -ItemType Directory -Path "src/main/java/com/example/demo/api" -Force | Out-Null
New-Item -ItemType Directory -Path "src/main/java/com/example/demo/api/model" -Force | Out-Null

Copy-Item "$outDir/src/main/java/com/example/demo/api/ApplianceApi.java" "src/main/java/com/example/demo/api/ApplianceApi.java" -Force
Copy-Item "$outDir/src/main/java/com/example/demo/api/ApiUtil.java" "src/main/java/com/example/demo/api/ApiUtil.java" -Force
Copy-Item "$outDir/src/main/java/com/example/demo/api/model/*.java" "src/main/java/com/example/demo/api/model/" -Force

Write-Host "OpenAPI interface/model generation completed."
