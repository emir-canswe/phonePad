# PhonePad Server

PhonePad Server, Android uygulamasından gelen dokunmatik yüzey jestlerini ve tıklama komutlarını işleyerek bilgisayarınızda fare hareketlerine çeviren bir WebSocket sunucusudur.

## Gereksinimler

- Python 3.8+
- [PyAutoGUI](https://pyautogui.readthedocs.io/en/latest/)
- [websockets](https://websockets.readthedocs.io/en/stable/)

## Kurulum

1. Depoyu bilgisayarınıza klonlayın veya indirin.
2. Gerekli kütüphaneleri yükleyin:
   ```bash
   pip install -r requirements.txt
   ```

## Çalıştırma

Sunucuyu çalıştırmak için aşağıdaki komutu kullanın:
```bash
python server.py
```

Sunucu varsayılan olarak `ws://0.0.0.0:8765` adresinde dinlemeye başlayacaktır.

## Güvenlik
- Sunucu sadece yerel ağdan (`192.168.x.x`) veya `localhost` üzerinden gelen bağlantıları kabul eder.
- Aynı anda sadece tek bir istemcinin bağlanmasına izin verilir.

## Hata Ayıklama
- `pyautogui.FAILSAFE` güvenlik nedeniyle kapatılmıştır (Android uygulamasından gelen ani/büyük hareketlerin sunucuyu kilitlememesi için).
- Bağlantı veya paket işleme sırasında oluşan hatalar konsola loglanacaktır.
