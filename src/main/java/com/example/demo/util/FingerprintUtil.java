package com.example.demo.util;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

public final class FingerprintUtil {

    private static final Path DEFAULT_SALT_FILE = Path.of(".appliance", "install_salt");

    /**
     * 工具类不允许实例化。
     */
    private FingerprintUtil() {
    }

    /**
     * 通过 domain、机器标识和安装盐值生成稳定实例指纹。
     *
     * @param domain 云服务域名
     * @return 64 位 SHA-256 十六进制指纹
     */
    public static String generate(String domain) {
        try {
            String machineId = resolveMachineId();
            String salt = ensureSalt(DEFAULT_SALT_FILE);
            String source = domain + "|" + machineId + "|" + salt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate instance_fingerprint", e);
        }
    }

    /**
     * 按操作系统策略解析机器标识。
     *
     * @return 机器标识文本
     * @throws IOException 主机信息无法读取时抛出
     */
    private static String resolveMachineId() throws IOException {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            String host = InetAddress.getLocalHost().getHostName();
            return host + "-" + System.getenv().getOrDefault("COMPUTERNAME", "unknown");
        }
        Path machineIdPath = Path.of("/etc/machine-id");
        if (Files.exists(machineIdPath)) {
            return Files.readString(machineIdPath, StandardCharsets.UTF_8).trim();
        }
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 读取已存在的安装盐值，若不存在则原子创建。
     *
     * @param saltFile 本地盐值文件路径
     * @return 盐值文本
     * @throws IOException 文件操作失败时抛出
     */
    private static String ensureSalt(Path saltFile) throws IOException {
        if (Files.exists(saltFile)) {
            String existing = Files.readString(saltFile, StandardCharsets.UTF_8).trim();
            if (existing.length() >= 16) {
                return existing;
            }
            throw new IOException("Invalid salt file: " + saltFile);
        }
        Files.createDirectories(saltFile.getParent());
        String salt = UUID.randomUUID().toString().replace("-", "");
        try {
            Files.writeString(saltFile, salt + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            return salt;
        } catch (IOException ex) {
            if (Files.exists(saltFile)) {
                return Files.readString(saltFile, StandardCharsets.UTF_8).trim();
            }
            throw ex;
        }
    }
}
