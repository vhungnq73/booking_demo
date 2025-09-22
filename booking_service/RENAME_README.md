# Project Renamer (Spring Boot Template)

`rename_project.sh` is a cross-platform shell script (Linux, macOS, Git Bash on Windows) that renames all occurrences of three placeholders across your template:

- `your_company_name`
- `your_project_name`
- `your_module_name`

It updates:
- **Text inside files** (package names, configs, docs, etc.)
- **Folder names** (renames matching directories, deepest-first)
- **File names** (renames matching files)

> ⚠️ Run this on a **fresh template** before building to avoid touching compiled artifacts (`target/`, `build/`, etc.).

---

## Prerequisites

- **Linux/macOS:** `bash`/`sh`, `find`, and `sed` (preinstalled on most systems).
- **Windows:** Install **[Git for Windows]** and run the script in **Git Bash**.
- **Git (recommended):** So you can easily undo changes if needed.

[Git for Windows]: https://git-scm.com/download/win

---

## Quick Start

1. **Place the script** in the **root of your project** (same level as `pom.xml` or `build.gradle`).

2. How to Run `rename_project.sh`
   ## Run the Script
   
   **macOS / Linux:**
   ```bash
   chmod +x rename_project.sh
   ./rename_project.sh or bash rename_project.sh
   ```
   **Windows (Git Bash):**
   ```bash
   chmod +x rename_project.sh
   ./rename_project.sh
   ```
3. **Follow the prompts** to enter your company, project, and module names.
   Please enter your_company_name: my_company
   Please enter your_project_name: billing_service
   Please enter your_module_name: api
You entered:
your_company_name -> my_company
your_project_name -> billing_service
your_module_name  -> api

Do you want to proceed with renaming? (y/n): y