# PhonePad 🖱️

**PhonePad**, Android telefonunuzu **Bluetooth HID (Human Interface Device)** protokolü aracılığıyla bilgisayarınız için kablosuz bir fareye dönüştüren açık kaynaklı bir uygulamadır.

Sunucu gerekmez, Wi-Fi gerekmez. Telefonunuz kendini doğrudan bir Bluetooth fare olarak tanıtır — tıpkı marketten aldığınız kablosuz bir fare gibi.

![Android](https://img.shields.io/badge/Android-API%2028+-green.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-blue.svg)
![Bluetooth](https://img.shields.io/badge/Bluetooth-HID%20Classic-purple.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

---

## ✨ Özellikler

- 🔵 **Sürücüsüz Bluetooth HID** — Bilgisayara hiçbir yazılım kurmaya gerek yok
- ⚡ **Sıfır Gecikme** — Ham pointer input ile Android'in iç filtresi bypass edildi
- 🎯 **Yüksek Hassasiyet** — 2.5x çarpan + ondalık birikim ile smooth hareket
- 🖱️ **Sol / Sağ Tık** — Dokunmatik alan üzerinden hem buton hem gesture
- 🌑 **Koyu Tema** — Tam siyah arka plan, yeşil accent renkleri
- 📡 **Otomatik Cihaz Keşfi** — Kayıtlı ve yeni cihazları listeler

---

## 📋 Gereksinimler

| Gereksinim | Minimum |
|---|---|
| Android Sürümü | **Android 9 (API 28)** veya üzeri |
| Bluetooth | **Bluetooth Classic** (BLE değil) |
| Bilgisayar | Windows / macOS / Linux — herhangi biri |

---

## 📱 Kurulum

### APK ile Doğrudan Kurulum (Kolay)
1. [Releases](https://github.com/emir-canswe/phonePad/releases) sayfasından son APK'yı indirin
2. Telefonunuzda **"Bilinmeyen kaynaklardan yükleme"** iznini verin
3. APK'ya dokunup **Yükle** deyin

### Android Studio ile Derleme
1. `android/PhonePad` klasörünü Android Studio'da açın
2. `gradle.properties` içinde `android.useAndroidX=true` olduğunu doğrulayın
3. Yeşil **Run ▶️** butonuna basın ya da terminalde:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🚀 Nasıl Kullanılır?

### Bilgisayardan Bağlanma (Önerilen)
1. Uygulamayı telefonunuzda açın
2. **"Bilgisayardan Bağlan"** butonuna basın → telefonunuz 5 dakika görünür olur
3. Windows: `Ayarlar → Bluetooth ve Cihazlar → Cihaz Ekle` → **PhonePad Mouse** seçin
4. Eşleşme onayını her iki tarafta da kabul edin
5. Bağlandı! 🎉 Dokunmatik ekran aktif olacak

### Telefondan Bağlanma
1. **"Ağ Ara (Cihaz Bul)"** butonuna basın
2. Bilgisayarınızın Bluetooth ayarlarının açık olduğundan emin olun
3. Listede bilgisayarınızın adını görünce üzerine dokunun

---

## 🎮 Kontroller

| Jest | Eylem |
|---|---|
| Tek parmak sürükle | Fare hareketi |
| Kısa dokunuş | Sol tık |
| Uzun basış | Sağ tık |
| Çift dokunuş | Çift tık |
| **Sol Tık** butonu | Sol tık |
| **Sağ Tık** butonu | Sağ tık |

---

## 📂 Proje Yapısı

```text
phonePad/
├── android/
│   └── PhonePad/                        # Android Studio Projesi
│       └── app/src/main/java/com/phonepad/
│           ├── MainActivity.kt           # İzin yönetimi, navigasyon
│           ├── bluetooth/
│           │   └── BluetoothHidManager.kt # HID profili, bağlantı yönetimi
│           ├── ui/
│           │   ├── ConnectScreen.kt      # Cihaz arama & bağlantı ekranı
│           │   ├── TouchpadScreen.kt     # Dokunmatik alan & tık butonları
│           │   └── SettingsScreen.kt     # Ayarlar
│           └── utils/
│               └── GestureHandler.kt    # Dokunma → HID raporu dönüşümü
├── server/                              # (Eski Wi-Fi mimarisi — artık kullanılmıyor)
└── README.md
```

---

## 🔧 Teknik Detaylar

- **Protokol:** Bluetooth Classic HID (BluetoothHidDevice API)
- **HID Raporu:** 4 byte — `[butonlar, dx, dy, scroll]`
- **Hassasiyet:** 2.5x çarpan + sub-pixel ondalık birikim
- **Input Yöntemi:** `awaitPointerEventScope` (native, gecikme yok)
- **Min SDK:** API 28 (Android 9)
- **Dil:** Kotlin + Jetpack Compose

---

## 📄 Lisans

MIT License — Dilediğiniz gibi kullanabilir, değiştirebilir ve dağıtabilirsiniz.


---

## Proje Bileşenleri

Proje iki ana bileşenden oluşur:
1. **Android İstemcisi (Client):** Kotlin ve Jetpack Compose ile yazılmış olan, telefon ekranında dokunmatik algılayıp bu jestleri WebSocket üzerinden sunucuya gönderen mobil uygulama.
2. **Python Sunucusu (Server):** Bilgisayarınızda (Windows/macOS/Linux) çalışan, telefondan gelen sinyalleri algılayıp `PyAutoGUI` sayesinde gerçek fare hareketlerine ve tıklamalarına çeviren arka plan yazılımı.

---

## Özellikler
- **Düşük Gecikme:** OkHttp WebSockets ile anlık (<20ms) etkileşim.
- **Fare Jestleri:** Tek parmakla fare imlecini oynatma, tıklama ve basılı tutma desteği. 
- **Güvenlik:** Sadece yerel ağınızdan (192.168.x.x) gelen komutlara izin verir. Başkalarının dışarıdan bağlanmasını engeller. Aynı anda sadece tek bir cihazın bağlanmasına olanak tanır.
- **Gelişmiş Ayarlar:** Fare hassasiyet çarpanı, scroll hızı ayarı, titreşim geri bildirimi ve kullanım sırasında ekranın açık kalması (WakeLock).

---

## Kurulum ve Kullanım

### 1. Python Sunucusu (Bilgisayarınız için)
Bilgisayarınızda farenin kontrol edilebilmesi için bu sunucunun çalışıyor olması gerekir.

**Gereksinimler:** Python 3.8 veya daha güncel bir sürüm.

1. Terminal/Komut İstemcisi'ni açın ve proje dizinindeki `server` klasörüne gidin:
   ```bash
   cd server
   ```
2. Gerekli kütüphaneleri yükleyin:
   ```bash
   pip install -r requirements.txt
   ```
3. Sunucuyu başlatın:
   ```bash
   python server.py
   ```
4. Sunucu `0.0.0.0:8765` üzerinde dinlemeye başlayacaktır. Bilgisayarınızın **Yerel IP Adresini** (Örneğin `192.168.1.55`) bulun, telefondan bağlanırken buna ihtiyacınız olacak. *(Windows için `ipconfig` komutuyla görebilirsiniz)*

---

### 2. Android Uygulaması (Telefonunuz için)
1. **Android Studio**'yu bilgisayarınıza indirin ve kurun.
2. Android Studio'yu açıp **Open (Aç)** diyerek projede bulunan `android/PhonePad` klasörünü seçin.
3. Android Studio, gerekli bağımlılıkları (Jetpack Compose, OkHttp vs.) arka planda indirecektir. İşlem tamamlanana kadar bekleyin.
4. Android cihazınızı USB kablosu ile bağlayın ve Geliştirici Seçeneklerinden **USB Hata Ayıklama (USB Debugging)** özelliğinin açık olduğundan emin olun.
5. Android Studio üzerinden yeşil `Run` (Play) butonuna basarak uygulamayı telefonunuza yükleyin.

### Nasıl Kullanılır?
1. Bilgisayarınızda Python sunucusunu çalıştırın.
2. Telefonunuzda **PhonePad** uygulamasını açın.
3. Ekrana bilgisayarınızın yerel IP adresini (örn. `192.168.1.55`) ve portu (`8765`) yazıp **Bağlan** butonuna basın.
4. Bağlantı başarılı olduğunda "Touchpad" ekranına yönlendirileceksiniz. Parmağınızı kaydırarak bilgisayar farenizi kontrol etmeye başlayabilirsiniz!

---

## Proje Klasör Yapısı
```text
PhonePad/
├── android/            # Android Jetpack Compose Projesi
│   └── PhonePad/       # Kotlin kaynak kodları ve uygulamanın tasarımı
├── server/             # Python WebSocket Sunucusu
│   ├── server.py       # Sunucu kodları
│   └── requirements.txt# Sunucu gereksinimleri
└── README.md           # Bu dosya
```
