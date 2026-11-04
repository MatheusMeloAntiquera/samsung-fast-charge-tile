# App Android: alternar "Carregamento rápido" (Samsung)

## Objetivo
Criar um app Android em Kotlin que liga e desliga o **carregamento rápido** com um toque, por **Quick Settings Tile** e/ou **widget de tela inicial**, sem precisar entrar nas Configurações → Bateria.

## Contexto e descobertas
- Não existe API pública do Android para isso. A configuração é específica do fabricante.
- Aparelho: **Samsung**. A chave descoberta foi **`adaptive_fast_charging`** em **`Settings.System`** (`1` = ligado, `0` = desligado).
  - Descoberta comparando `adb shell settings list system` antes e depois de alternar a opção na UI.
  - Possíveis chaves relacionadas, não verificadas: `super_fast_charging`, `wireless_fast_charging`.
- Apps comuns não podem gravar chaves não públicas de `Settings.System`, nem com `WRITE_SETTINGS`. A solução é conceder **`WRITE_SECURE_SETTINGS`** uma única vez via ADB, sem root.
- Em alguns aparelhos a mudança só vale na próxima vez que o carregador é conectado.

## Decisões tomadas
- Linguagem: **Kotlin**.
- Mecanismo: gravar `adaptive_fast_charging` diretamente via `Settings.System.putInt`.
- Permissão: `WRITE_SECURE_SETTINGS` concedida via `adb shell pm grant`. O Shizuku fica como alternativa futura.
- Interface: Quick Settings Tile (`TileService`) e/ou widget (`AppWidgetProvider`). **Ainda não foi definido qual, ou se os dois.**

## Ambiente (Windows)
- ADB instalado e funcionando.
- Quando houver mais de um dispositivo (`more than one device/emulator`), use `adb devices` e depois `adb -s <serial> ...`, ou `adb disconnect` / feche o emulador.
- No Windows, filtre com o pipe **dentro das aspas** (roda o `grep` no celular) ou use `findstr`:
  ```bash
  adb shell "settings list system | grep -i charg"
  adb shell settings list system | findstr /i charg
  ```

## Código até agora
Ainda não existe projeto, apenas os trechos de referência abaixo.

**Alternar o valor:**
```kotlin
val cr = context.contentResolver
val atual = Settings.System.getInt(cr, "adaptive_fast_charging", 1)
Settings.System.putInt(cr, "adaptive_fast_charging", if (atual == 1) 0 else 1)
```

**Manifest:**
```xml
<uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"
    tools:ignore="ProtectedPermissions" />
```

**Conceder a permissão (uma vez, após instalar):**
```bash
adb shell pm grant com.seu.pacote android.permission.WRITE_SECURE_SETTINGS
```

## Próximos passos
1. **Validar a escrita via ADB**, se ainda não foi feito: rodar `adb shell settings put system adaptive_fast_charging 0` e conferir em Configurações → Bateria → Mais configurações de bateria. Depois, voltar com `... 1`.
2. Decidir a interface: Tile, widget ou os dois.
3. Criar o projeto Android (Kotlin, minSdk 24+ para `TileService`) e definir o `applicationId`.
4. Implementar uma classe utilitária (ex.: `FastChargingController`) com `isEnabled()`, `toggle()` e `hasPermission()`, que checa `WRITE_SECURE_SETTINGS` com `checkSelfPermission`.
5. Implementar a interface escolhida:
   - **Tile:** `TileService` com `onClick()` → `toggle()`, e `onStartListening()` atualizando `qsTile.state` (ACTIVE/INACTIVE).
   - **Widget:** `AppWidgetProvider` com `PendingIntent` de broadcast → `toggle()` → atualizar o ícone ou texto do estado.
6. Tratar a falta de permissão: mostrar uma tela ou notificação com o comando `adb shell pm grant ...`.
7. Instalar, conceder a permissão via ADB e testar o toggle conectando e desconectando o carregador.
8. (Opcional) Observar mudanças feitas pela UI do sistema com `ContentObserver` em `Settings.System.getUriFor("adaptive_fast_charging")`, para manter o tile e o widget sincronizados.
