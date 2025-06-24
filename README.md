# 📝 Enhanced Text Editor (Java Swing)

A feature-rich Java Swing-based text editor built with a clean interface and powerful tools like:

- ✅ File Operations (New, Open, Save, Save As)
- 🖋️ Real-time Spell Checker (with red underline)
- 🔊 Text-to-Speech
- 🎨 Customizable Fonts, Text Color, and Background Color
- 📚 Integrated dictionary using `words.txt`

---

## 📁 Project Structure

│
├── GUI.java # Main GUI class with all features
├── Function_File.java # Handles file operations
├── TextToVoice.java # Handles text-to-speech
├── words.txt # Dictionary file for spell checking
└── logo3.jpg # Icon image (optional)


---

## 🚀 Features

| Feature               | Description                                                                 |
|-----------------------|-----------------------------------------------------------------------------|
| 📂 File Menu          | New, Open, Save, Save As, Exit                                              |
| ✂️ Edit Menu          | Cut, Copy, Paste, Select All                                                |
| 📝 Spell Checker      | Highlights misspelled words in red underline using a dictionary file        |
| 🎙️ Text-to-Speech     | Converts text to speech using system TTS (via `TextToVoice.java`)           |
| 🎨 Customization       | Change font, text color, and background color                              |
| 📖 About Section       | Displays app info and credits                                              |

---

## 📦 How to Run

### Prerequisites
- Java JDK installed (Java 8 or higher)
- A terminal or IDE (like IntelliJ, Eclipse, or VS Code)

### Compile
javac GUI.java Function_File.java TextToVoice.java

### RUN
java GUI
