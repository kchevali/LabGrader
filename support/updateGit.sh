cd /Users/kevin/Documents/Repos/CSCILab/
find . -type d -depth 1 -exec git --git-dir={}/.git --work-tree=$PWD/{} pull origin master \;
echo "DONE!"