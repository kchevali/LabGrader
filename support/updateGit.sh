cd ~/git/LabGrader/
find . -type d -name .git -prune -exec sh -c 'cd "$(dirname "$1")" && git pull' _ {} \;
echo "DONE!"