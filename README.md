# Carregamento rápido (Quick Settings Tile)

Botão no painel rápido que liga e desliga o carregamento rápido de aparelhos Samsung
(chave `adaptive_fast_charging` em `Settings.System`). Não precisa de root.

## Build (PowerShell)
O `JAVA_HOME` padrão (JDK 11) é antigo demais para o AGP 8; use o JDK do Android Studio:

```powershell
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew.bat assembleDebug
```

## Instalar e conceder a permissão (uma vez)
Se `adb devices` listar mais de um dispositivo, acrescente `-s <serial>` depois de `adb`.

```bash
adb install -r app\build\outputs\apk\debug\app-debug.apk
```

```bash
adb shell pm grant com.matheus.fastcharge android.permission.WRITE_SECURE_SETTINGS
```

## Usar
Painel rápido → ⋮ → Editar botões → arraste **Carga rápida** para o painel.
Sem a permissão, o botão aparece indisponível e um toque abre a tela com o comando acima.
Toque longo no botão: abre as configurações de bateria.

Conferir o valor: `adb shell settings get system adaptive_fast_charging` (1 = ligado, 0 = desligado).
Em alguns aparelhos a mudança só vale na próxima vez que o carregador é conectado.

## Arquivos principais
- `app/src/main/java/com/matheus/fastcharge/FastChargingController.kt`: lê e grava a chave.
- `app/src/main/java/com/matheus/fastcharge/FastChargingTileService.kt`: o tile.
- `app/src/main/java/com/matheus/fastcharge/MainActivity.kt`: tela de status, permissão e botão de teste.
