#!/usr/bin/env sh
# rename_project.sh - Cross-platform project renamer for Spring Boot
# Works on Linux, macOS, and Git Bash on Windows

set -e

# --- Prompt for input ---
printf "Please enter your_company_name: "
read NEW_COMPANY
printf "Please enter your_project_name: "
read NEW_PROJECT
printf "Please enter your_module_name: "
read NEW_MODULE

printf "\nYou entered:\n"
printf "  your_company_name -> %s\n" "$NEW_COMPANY"
printf "  your_project_name -> %s\n" "$NEW_PROJECT"
printf "  your_module_name  -> %s\n" "$NEW_MODULE"

printf "\nDo you want to proceed with renaming? (y/n): "
read CONFIRM

if [ "$CONFIRM" != "y" ] && [ "$CONFIRM" != "Y" ]; then
    printf "❌ Rename cancelled.\n"
    exit 0
fi

# --- sed wrapper for macOS/Linux ---
sed_inplace() {
    # $1 = search/replace pattern, $2 = file
    if echo "$OSTYPE" | grep -qi "darwin"; then
        sed -i "" "$1" "$2"
    else
        sed -i "$1" "$2"
    fi
}

# --- Replace inside files ---
printf "🔍 Replacing text inside files...\n"
find . -type f ! -name "$(basename "$0")" ! -name "RENAME_README.md" | while IFS= read -r file; do
    sed_inplace "s/your_company_name/$NEW_COMPANY/g" "$file"
    sed_inplace "s/your_project_name/$NEW_PROJECT/g" "$file"
    sed_inplace "s/your_module_name/$NEW_MODULE/g" "$file"
done

# --- Rename directories (deepest first) ---
printf "📂 Renaming folders...\n"
find . -depth -type d | while IFS= read -r dir; do
    base=$(basename "$dir")
    case "$base" in
        your_company_name)
            mv "$dir" "$(dirname "$dir")/$NEW_COMPANY"
            ;;
        your_project_name)
            mv "$dir" "$(dirname "$dir")/$NEW_PROJECT"
            ;;
        your_module_name)
            mv "$dir" "$(dirname "$dir")/$NEW_MODULE"
            ;;
    esac
done

# --- Rename files ---
printf "📄 Renaming files...\n"
find . -type f | while IFS= read -r f; do
    newname="$f"
    newname=$(echo "$newname" | sed "s/your_company_name/$NEW_COMPANY/g")
    newname=$(echo "$newname" | sed "s/your_project_name/$NEW_PROJECT/g")
    newname=$(echo "$newname" | sed "s/your_module_name/$NEW_MODULE/g")
    if [ "$f" != "$newname" ]; then
        mv "$f" "$newname"
    fi
done

printf "✅ Rename completed successfully!\n"

##!/usr/bin/env sh
# rename_project.sh - Cross-platform project renamer for Spring Boot
# Works on Linux, macOS, and Git Bash on Windows

#set -e
#
#echo "=== Spring Boot Project Template Renamer ==="
#echo
#
## --- Prompt for input ---
#printf "Please enter your_company_name: "
#read NEW_COMPANY
#echo "You want to rename 'your_company_name' → '$NEW_COMPANY'"
#
#printf "Please enter your_project_name: "
#read NEW_PROJECT
#echo "You want to rename 'your_project_name' → '$NEW_PROJECT'"
#
#printf "Please enter your_module_name: "
#read NEW_MODULE
#echo "You want to rename 'your_module_name' → '$NEW_MODULE'"
#
#echo
#echo "Summary of changes:"
#echo "  your_company_name -> $NEW_COMPANY"
#echo "  your_project_name -> $NEW_PROJECT"
#echo "  your_module_name  -> $NEW_MODULE"
#echo
#printf "Do you want to proceed with renaming? (y/n): "
#read CONFIRM
#
#if [ "$CONFIRM" != "y" ] && [ "$CONFIRM" != "Y" ]; then
#    echo "❌ Rename cancelled."
#    exit 0
#fi
#
## --- sed wrapper for macOS/Linux ---
#sed_inplace() {
#    if echo "$OSTYPE" | grep -qi "darwin"; then
#        sed -i "" "$1" "$2"
#    else
#        sed -i "$1" "$2"
#    fi
#}
#
## --- Replace inside files ---
#echo "🔍 Replacing text inside files..."
#find . -type f ! -name "$(basename "$0")" | while IFS= read -r file; do
#    sed_inplace "s/your_company_name/$NEW_COMPANY/g" "$file"
#    sed_inplace "s/your_project_name/$NEW_PROJECT/g" "$file"
#    sed_inplace "s/your_module_name/$NEW_MODULE/g" "$file"
#done
#
## --- Rename directories (deepest first) ---
#echo "📂 Renaming folders..."
#find . -depth -type d | while IFS= read -r dir; do
#    base=$(basename "$dir")
#    case "$base" in
#        your_company_name)
#            mv "$dir" "$(dirname "$dir")/$NEW_COMPANY"
#            ;;
#        your_project_name)
#            mv "$dir" "$(dirname "$dir")/$NEW_PROJECT"
#            ;;
#        your_module_name)
#            mv "$dir" "$(dirname "$dir")/$NEW_MODULE"
#            ;;
#    esac
#done
#
## --- Rename files ---
#echo "📄 Renaming files..."
#find . -type f | while IFS= read -r f; do
#    newname="$f"
#    newname=$(echo "$newname" | sed "s/your_company_name/$NEW_COMPANY/g")
#    newname=$(echo "$newname" | sed "s/your_project_name/$NEW_PROJECT/g")
#    newname=$(echo "$newname" | sed "s/your_module_name/$NEW_MODULE/g")
#    if [ "$f" != "$newname" ]; then
#        mv "$f" "$newname"
#    fi
#done
#
#echo "✅ Rename completed successfully!"
