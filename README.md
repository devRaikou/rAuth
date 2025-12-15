# rAuth 🔒

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Platform](https://img.shields.io/badge/Platform-Paper%201.21-blue?style=for-the-badge&logo=paper)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**rAuth** is a modern, lightweight, and secure authentication plugin designed for Minecraft Paper servers. It prioritizes user experience with beautiful gradient messages, intuitive commands, and robust security features without the bloat.

## ✨ Features

*   **🎨 Modern Aesthetics**: Built with `MiniMessage` for clear, beautiful, and gradient-text support.
*   **🌍 Localization**: Full multi-language support (English & Turkish included out-of-the-box).
*   **⏱️ Login Timeout**: Configurable countdown timer (Title, Subtitle, Sound) to kick idle unauthenticated players.
*   **💾 Database Support**: Flexible storage options (SQLite for simple setups, MySQL for networks).
*   **🔄 Session Management**: Remembers players for a configurable time, preventing repetitive logins.
*   **🛡️ Security**: IP limit checks, secure password hashing, and input validation.
*   **⚡ Tab Completion**: Smart tab completion that hides sensitive data (player names) to prevent information leaks.

## 🚀 Installation

1.  Download the latest `rAuth-x.x.jar` from Releases.
2.  Drop the file into your server's `plugins/` folder.
3.  Restart your server.
4.  (Optional) Edit `config.yml` to adjust database or timeout settings.

## 🛠️ Commands

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/login` | `/login <password>` | Log in to your account. |
| `/register` | `/register <pass> <pass>` | Register a new account. |
| `/changepassword` | `/changepassword <old> <new>` | Change your current password. |
| `/logout` | `/logout` | terminate your current session. |
| `/rauth` | `/rauth <reload\|help>` | Admin management command. |

## ⚙️ Configuration

The `config.yml` is simple and powerful:

```yaml
database:
  type: SQLITE # or MYSQL

security:
  min-password-length: 6
  login-timeout: 60 # Seconds before kick
  session-timeout: 30 # Minutes to remember login
```

---

<p align="center">
  Made with ❤️ by Raikou
</p>
