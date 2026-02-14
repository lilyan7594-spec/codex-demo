package com.example.demo.util;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public final class LocalIpUtil {

    /**
     * 工具类不允许实例化。
     */
    private LocalIpUtil() {
    }

    /**
     * 解析访问指定远端主机时的本机出口 IP。
     *
     * @param remoteHost 远端主机
     * @param remotePort 远端端口
     * @return 出口 IP，不可用时返回 null
     */
    public static String resolveOutboundIp(String remoteHost, int remotePort) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName(remoteHost), remotePort);
            String ip = socket.getLocalAddress().getHostAddress();
            return "0.0.0.0".equals(ip) ? null : ip;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 从活动网卡中获取第一个可用的非回环 IPv4 地址。
     *
     * @return 局域网 IPv4，未找到时返回 null
     */
    public static String resolveLanIpv4() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nic = interfaces.nextElement();
                String name = nic.getName().toLowerCase(Locale.ROOT);
                if (!nic.isUp() || nic.isLoopback() || nic.isVirtual()) {
                    continue;
                }
                if (name.contains("docker") || name.contains("veth") || name.contains("br-")) {
                    continue;
                }
                Enumeration<InetAddress> addresses = nic.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
