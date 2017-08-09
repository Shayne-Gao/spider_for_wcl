roles=("Balance" "Guardian" "Restoration" "Brewmaster")
ls_date=`date +%Y%m%d`
for role in ${roles[@]}
do
scrapy crawl wcl -o ../output/keystone15-20_${role}_${ls_date}.csv -a role="${role}" 
cd ../processer_java/&& java Analysiser ../output/keystone15-20_${role}_${ls_date}.csv
done
