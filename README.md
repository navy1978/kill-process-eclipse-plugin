# Kill Process Eclipse Plugin

This plugin was created taking inspiration from this project: [YATB](https://github.com/missedone/yatb).

This Eclipse plugin provides a convenient way to terminate processes directly from the Eclipse console. It is compatible with Windows, macOS, and Linux operating systems.

## Features

- **Terminate Processes:** Easily terminate running processes from the Eclipse console.
- **Multi-Platform Support:** Works seamlessly on Windows, macOS, and Linux.

## Installation

1. **Download the File:**
   - Download the plugin file from the `dist` folder.

2. **Copy the File to Eclipse's `dropins` Folder:**
   - Copy the downloaded file to the `dropins` folder of your Eclipse installation.

## Usage

1. **Run a Process in the Eclipse Console:**

   - Open a project and run a process that will display output in the Eclipse console.

2. **Terminate the Process:**

   - In the Eclipse console, you will see a "Kill Process" button in the toolbar.
   - Click the "Kill Process" button to terminate the process associated with the active console.

## How It Works

- The plugin checks if a process is running in the active console.
- It retrieves the process ID (PID) of the running process.
- Depending on the operating system, it uses the appropriate command to terminate the process:
  - **Windows:** Uses the `taskkill` command.
  - **macOS/Linux:** Uses the `kill` command.

## Contributions

Contributions are welcome! If you have suggestions or improvements, feel free to create a pull request or open an issue.


