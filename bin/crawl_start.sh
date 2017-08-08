roles=("Balance" "Guardian" "Restoration" "Brewmaster")
ls_date=`date +%Y%m%d`
for role in ${roles[@]}
do
scrapy crawl wcl -o keystone15-20_${role}_${ls_date}.csv -a role="${role}" 
done
