## security

该模块负责提供一些常用的加密算法工具类

### AES

密码学中的高级加密标准（Advanced Encryption Standard，AES）,用于替代之前的DES（Data Encryption Standard），它是一个对称加密算法（加解密密钥一致）。

#### 基本结构

AES为分组密码，它把明文分组，每组长度相等，每次加密一组数据，直到加密完整个明文。在AES规范中，分组长度只能是128位（16个字节），密钥长度可以使用128位、192位、或256位。根据不同的密钥长度，推荐的加密轮数也不同，如下表

| 分组长度(单位：32bit) | 密钥长度(单位：32bit) | 加密轮数 |
| --------------------- | --------------------- | -------- |
| 4                     | 4（AES-128）          | 10       |
| 4                     | 6（AES-192）          | 12       |
| 4                     | 8（AES-256）          | 14       |

#### 加密模式

##### 1.电话本模式 ECB-Electronic Codebook Book

将明文分成若干段相同的小段，然后对每一小段进行加密

缺点：ECB有个安全问题，如果使用相同的密钥，相同的明文块就会产生相同的密文块，不能很好的隐藏数据模式。

##### 2.密码分组链接模式 CBC-Cipher Block Chaining

先将明文分成若干小段，每一块明文先跟初始块(IV-Initial Vector默认是128 bit 0)或上一块的密文进行异或运算后，再与密钥进行加密

缺点：加密过程是串行化，速度较慢。不影响解密（解密可以并行化）

##### 3.计算器模式 CTR-Counter

这种模式不常用，它有一个自增的值，先用key对该子增值进行加密，然后用该密文对明文块进行异或得到密文块

##### 4.密码反馈模式 CFB-Cipher Feed Back

该模式拿IV或上一块的密文跟密钥进行加密，得到的结果在与明文进行异或运算。

##### 5.输出反馈模式 OFB-Output Feed Back

该模式拿IV用密钥进行加密，得到的结果（作为下一轮与密钥加密的内容）与明文块进行异或运算。

#### 填充模式

AES加密将数据分成了很多128bit的块，当最后一个明文块不足16个byte的时候，需要进行一些填充，才能完成加密，填充有以下几种模式：

##### NoPadding

不填充，一般不会使用，它支持加密128bit的整数倍的原文。

##### PKCS5Padding

缺几个字节就填充几个缺的字节数。如：

```
... | DD DD DD DD 04 04 04 04 |
```

注意：如果数据已经是128bit的整数倍，也需要填充，否则无法解密。PKCS5Padding限定了块大小位8bytes，但是AES将块份成了16bytes，如果缺失的bytes大于7，使用该模式会报错，因此填充不建议使用此种填充

##### PKCS7Padding

类似PKCS#5，它没有对块大小进行限制。一般默认使用这种填充模式

##### ISO10126Padding

最后一个字节是填充的字节数，其它全为随机数

### RSA

#### 填充模式

RSA是一个非对称加密算法，加密和解密用的密钥不一样。同时它也是一个块加密算法，与AES不同的是，RSA的加密块大小是根据密钥长度决定的。由于都是块加密算法，它也需要对明文进行填充，有以下几种填充方式：

| 填充方式     | 最小填充大小(byte)          | 填充后长度 |
| ------------ | --------------------------- | ---------- |
| NoPadding    | 不限制                      | 和公钥等长 |
| PKCS1Padding | 至少填充key size - 11个byte | 和公钥等长 |
| OAEPPadding  | 至少填充key size - 41个byte | 和公钥等长 |

假设密钥长度位2048位，即256个byte，三种填充模式的效果如下。

##### 1.RSA_NO_PADDING

如果明文不够256个 bytes，则会在明文前加若干个0，直到达到256个bytes。

##### 2.PKCS1Padding

每次块加密，都必须在前面填充至少11个bytes，保障相同的明文每次加密出来的内容不一样

```
#每个块内容如下，第一个byte固定位0，数据前的一个byte也为0
00 BT PS 00 DATA
```

BT表示block type, PS表示padding string，最少8字节。BT为0x00或者0x01时，表示私钥加密，PS分别为0x00和0xFF。BT为0x02时，PS由伪随机数组成。

##### 3.OAEPPadding

Optimal Asymmetric Encryption Padding scheme defined in PKCS1。它是PKCS1填充模式的另外一种类型，除了可以指定OAEPPadding外，还可以指定更具体些，如**OAEPWithMD5AndMGF1Padding** 、**OAEPWithSHA-512AndMGF1Padding**

#### 密钥操作

java程序中，公钥使用X509格式读取，私钥使用PKCS8格式读取。但是通常用open ssl工具生成的是PEM格式，需要进行一些转换工作。

##### cer提取public key

如果你的公钥文件是一个证书，那它并不代表着公钥文件，需要从证书中提取出来。通过java代码方式提取如下

```java
public static byte[] extractPubKeyFromPEMEncodedCert(byte[] certPemBytes) {
    try {
        CertificateFactory factory = CertificateFactory.getInstance(X509);
        X509Certificate cert = (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certPemBytes));
        PublicKey publicKey = cert.getPublicKey();
        byte[] result = publicKey.getEncoded();
        return result;	
    } catch(CertificateException e) {
        throw new IllegalArgumentException(e.getMessage(), e);
    }
}
```

也可以通过shell命令直接提取

```shell
openssl x509 -in cimbbank.com.ph.crt -pubkey -noout > cimbbank.com.ph.pem
```