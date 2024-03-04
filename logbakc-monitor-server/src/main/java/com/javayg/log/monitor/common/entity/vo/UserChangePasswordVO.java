package com.javayg.log.monitor.common.entity.vo;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.Data;

/**
 * 修改用户密码的接口参数VO
 *
 * @author YangGang
 * @date 2023/2/20
 * @description
 */
@Data
public class UserChangePasswordVO {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword(String rsaPrivateKey) {
        RSA rsa = new RSA(rsaPrivateKey, null);
        byte[] decrypt = rsa.decrypt(Base64.decode(oldPassword.getBytes()), KeyType.PrivateKey);
        return new String(decrypt);
    }

    public String getNewPassword(String rsaPrivateKey) {
        RSA rsa = new RSA(rsaPrivateKey, null);
        byte[] decrypt = rsa.decrypt(Base64.decode(newPassword.getBytes()), KeyType.PrivateKey);
        return new String(decrypt);
    }
}