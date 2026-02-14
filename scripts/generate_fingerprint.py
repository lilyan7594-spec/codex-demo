#!/usr/bin/env python3
import argparse
import hashlib
import os
import platform
import secrets
import sys
from pathlib import Path


def read_first_existing(paths: list[str]) -> str | None:
    for path in paths:
        p = Path(path)
        if p.exists():
            value = p.read_text(encoding="utf-8", errors="ignore").strip()
            if value:
                return value
    return None


def get_machine_id() -> str:
    system = platform.system().lower()

    if system == "linux":
        machine_id = read_first_existing(
            ["/etc/machine-id", "/var/lib/dbus/machine-id", "/sys/class/dmi/id/product_uuid"]
        )
        if machine_id:
            return machine_id
        raise RuntimeError("Cannot resolve machine id on Linux.")

    if system == "windows":
        try:
            import winreg  # type: ignore
        except Exception as exc:
            raise RuntimeError(f"Cannot import winreg: {exc}") from exc

        key = winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE, r"SOFTWARE\Microsoft\Cryptography")
        try:
            value, _ = winreg.QueryValueEx(key, "MachineGuid")
            if value:
                return str(value).strip()
        finally:
            winreg.CloseKey(key)
        raise RuntimeError("MachineGuid is empty.")

    raise RuntimeError(f"Unsupported OS: {system}")


def ensure_salt(salt_file: Path) -> str:
    if salt_file.exists():
        salt = salt_file.read_text(encoding="utf-8").strip()
        if len(salt) >= 16:
            return salt
        raise RuntimeError(f"Salt file exists but is invalid: {salt_file}")

    salt_file.parent.mkdir(parents=True, exist_ok=True)
    salt = secrets.token_hex(16)
    try:
        with salt_file.open("x", encoding="utf-8") as f:
            f.write(salt + "\n")
        return salt
    except FileExistsError:
        # Another process created the file first.
        existing = salt_file.read_text(encoding="utf-8").strip()
        if len(existing) >= 16:
            return existing
        raise RuntimeError(f"Salt file exists but is invalid: {salt_file}")


def build_fingerprint(domain: str, machine_id: str, install_salt: str) -> str:
    source = f"{domain}|{machine_id}|{install_salt}"
    return hashlib.sha256(source.encode("utf-8")).hexdigest()


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate appliance instance_fingerprint = SHA-256(domain|machine_id|install_salt)"
    )
    parser.add_argument("--domain", required=True, help="Cloud domain, e.g. example.myhuaweicloud.com")
    parser.add_argument(
        "--salt-file",
        default=".appliance/install_salt",
        help="Path to persistent install_salt file (default: .appliance/install_salt)",
    )
    parser.add_argument(
        "--print-source",
        action="store_true",
        help="Print machine id and salt source metadata (without printing source plaintext).",
    )
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    domain = args.domain.strip()
    if not domain:
        print("ERROR: --domain must not be empty.", file=sys.stderr)
        return 1

    salt_file = Path(args.salt_file)
    machine_id = get_machine_id()
    install_salt = ensure_salt(salt_file)
    fingerprint = build_fingerprint(domain, machine_id, install_salt)

    if args.print_source:
        print(f"os={platform.system()}")
        print(f"salt_file={salt_file}")
        print(f"machine_id_len={len(machine_id)}")

    print(fingerprint)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
