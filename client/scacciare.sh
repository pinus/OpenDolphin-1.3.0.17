# scacciar "。" e "、"
# 文末の "。"
find . -name "*.java" | xargs sed -i -e "s/。$/./g"
i=0
while [ $i -lt 6 ]; do
# コメントライン文中  /* のパターン
  find . -name "*.java" | xargs sed -i -e "s/\(^[ \/]*\*.*\)。/\1. /g"
# 文中 // のパターン
  find . -name "*.java" | xargs sed -i -e "s/\(^ *\/\/.*\)。/\1. /g"
  i=`expr $i + 1`
  echo "${i} times repeated"
done

# コメントラインの "、" 
i=0
while [ $i -lt 6 ]; do
  # /* のパターン
  find . -name "*.java" | xargs sed -i -e "s/\(^[ \/]*\*.*\)、/\1，/g"
  # // のパターン
  find . -name "*.java" | xargs sed -i -e "s/\(^ *\/\/.*\)、/\1，/g"
  i=`expr $i + 1`
  echo "${i} times repeated"
done

find . -name "*.java-e*" | xargs rm

