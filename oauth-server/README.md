## Oauth2-Server中生成JWT Token
### 通过keytool生成证书
```
keytool -genkeypair -alias oauth2_key -keyalg RSA -keypass 123456 -keystore oauth2_key.jks -storepass 123456
```
### 查看证书
```
keytool -list -v -keystore oauth2_key.jks -storepass 123456
```
### 查看公钥
```
keytool -list -rfc -keystore oauth2_key.jks -storepass 123456
```