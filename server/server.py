import asyncio
import json
import logging
import pyautogui
import websockets

# Loglama ayarları
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# PyAutoGUI failsafe kapatma (Gereksinimlerde istendiği gibi)
pyautogui.FAILSAFE = False

# Tek bağlantı limiti için global durum
active_connection = None

def is_local_network(ip):
    # Sadece 192.168.x.x ağından (ve test için localhost) gelen isteklere izin ver
    return ip.startswith('192.168.') or ip == '127.0.0.1' or ip == '::1'

async def handle_client(websocket):
    global active_connection
    
    client_ip = websocket.remote_address[0]
    logging.info(f"Bağlantı denemesi: {client_ip}")

    # Güvenlik kontrolü: Local ağdan mı?
    if not is_local_network(client_ip):
        logging.warning(f"Reddedildi - Local olmayan IP adresi: {client_ip}")
        await websocket.close()
        return

    # Aynı anda tek bağlantı limiti
    if active_connection is not None:
        logging.warning("Bağlantı reddedildi - Zaten aktif bir bağlantı var.")
        await websocket.close()
        return

    active_connection = websocket
    logging.info(f"İstemci başarıyla bağlandı: {client_ip}")

    try:
        async for message in websocket:
            try:
                # Gelen paketi JSON olarak parse et
                data = json.loads(message)
                action_type = data.get('type')

                # Gelen paket tipine göre PyAutoGUI aksiyonu
                if action_type == 'move':
                    dx = data.get('dx', 0)
                    dy = data.get('dy', 0)
                    pyautogui.moveRel(dx, dy)
                
                elif action_type == 'click':
                    button = data.get('button', 'left')
                    pyautogui.click(button=button)
                    
                elif action_type == 'double_click':
                    button = data.get('button', 'left')
                    pyautogui.doubleClick(button=button)
                    
                elif action_type == 'scroll':
                    dy = data.get('dy', 0)
                    # PyAutoGUI'de scroll yönü işletim sistemine göre değişebilir, gerekirse ayarlanır
                    pyautogui.scroll(dy)
                    
                elif action_type == 'drag':
                    dx = data.get('dx', 0)
                    dy = data.get('dy', 0)
                    pyautogui.dragRel(dx, dy)
                    
                else:
                    logging.warning(f"Bilinmeyen eylem tipi: {action_type}")

            except json.JSONDecodeError:
                logging.error("Geçersiz JSON paketi alındı, atlanıyor.")
            except Exception as e:
                logging.error(f"Paket işlenirken hata oluştu: {e}")

    except websockets.exceptions.ConnectionClosed:
        logging.info("İstemci bağlantısı koptu.")
    except Exception as e:
        logging.error(f"Beklenmeyen bağlantı hatası: {e}")
    finally:
        active_connection = None
        logging.info("Yeni bağlantı bekleniyor...")

async def main():
    logging.info("PhonePad Server başlatılıyor... (ws://0.0.0.0:8765)")
    
    # WebSocket sunucusunu 0.0.0.0:8765 üzerinde başlat
    async with websockets.serve(handle_client, "0.0.0.0", 8765):
        await asyncio.Future()  # Sunucuyu sonsuza kadar çalıştır

if __name__ == "__main__":
    try:
        asyncio.run(main())
    except KeyboardInterrupt:
        logging.info("Sunucu manuel olarak durduruldu.")
