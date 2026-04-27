# PhonePad

**PhonePad**, Android telefonunuzu aynı Wi-Fi ağı üzerinden bilgisayarınız için hızlı ve pratik bir kablosuz touchpad'e dönüştüren açık kaynaklı bir projedir. Bilgisayarınızdan uzaktayken (film izlerken, sunum yaparken vb.) fareyi telefonunuz üzerinden yüksek hassasiyetle kontrol etmenizi sağlar.

![PhonePad Konsept](https://img.shields.io/badge/Android-Client-green.svg) ![PhonePad Sunucu](https://img.shields.io/badge/Python-Server-blue.svg)

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
