@echo off
title PhonePad Server
echo ===================================================
echo             PhonePad Sunucusu Baslatiliyor...
echo ===================================================
echo.

cd server

echo [1/2] Gerekli kutuphaneler kontrol ediliyor...
pip install -r requirements.txt
echo.

echo [2/2] Sunucu aktif ediliyor...
python server.py

pause
