import scrapy
import json
import urlparse
import HTMLParser

class WclItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    keystone = scrapy.Field()
    name = scrapy.Field()
    Mas = scrapy.Field()
    Cri = scrapy.Field()
    Ver =scrapy.Field()
    Has = scrapy.Field()
    Agi = scrapy.Field()
    Int = scrapy.Field()
    Sta = scrapy.Field()
    Lee = scrapy.Field()
    Avo = scrapy.Field()
    legend1 = scrapy.Field()
    legend2 = scrapy.Field()
    t1=scrapy.Field()
    t2=scrapy.Field()
    t3=scrapy.Field()
    t4=scrapy.Field()
    t5=scrapy.Field()
    t6=scrapy.Field()

class WclSpider(scrapy.Spider):
    name = "wcl"
    role = "BeastMastery"
    def start_requests(self):
        urls = [
            'https://www.warcraftlogs.com/rankings/table/speed/16/0/10/5/1/Any/Any/0/0/0/0/0/?search=&page=1&keystone=15',
            # 'https://www.warcraftlogs.com/rankings/table/speed/16/0/10/5/1/Any/Any/0/0/0/0/0/?search=&page=1&keystone=19',
            # 'https://www.warcraftlogs.com/rankings/table/speed/16/0/10/5/1/Any/Any/0/0/0/0/0/?search=&page=1&keystone=18',
            # 'https://www.warcraftlogs.com/rankings/table/speed/16/0/10/5/1/Any/Any/0/0/0/0/0/?search=&page=1&keystone=17',
            # 'https://www.warcraftlogs.com/rankings/table/speed/16/0/10/5/1/Any/Any/0/0/0/0/0/?search=&page=1&keystone=16',
        ]
        for url in urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        #select chac by img here
        for records in response.xpath("//img[contains(@src,'"+self.role+"')]"):
            url = records.xpath("../../@href").extract_first()
            name = records.xpath("../text()").extract_first()
            #reports/k1h72XmW3fL9wVrj#fight=1&type=
            recordId = url[9:25] 
            fightId =  url[32:-17]
            playerName = name
            #get keystone level
            oriUrl = str(response.url);
            keystone = oriUrl[-2:];
            infoUrl = "https://www.warcraftlogs.com/reports/fights_and_participants/"+ recordId+"/0?rid="+recordId+"&fid="+fightId+"&name="+playerName+"&keystone="+keystone
            # yield {'url':infoUrl}
            yield scrapy.Request(url=infoUrl, callback=self.parse2)

    def parse2(self, response):
        url = str(response.url) # response is a plain json text
        result=urlparse.urlparse(url) 
        params=urlparse.parse_qs(result.query,True)
        rid = params['rid'][0]
        fid = params['fid'][0]
        keystone = params['keystone'][0]
        fidIndex = int(fid)-1
        name = str(params['name'][0])
        jsonInfo = response.xpath("//text()").extract_first()
        data = json.loads(jsonInfo)
        startTime = data['fights'][fidIndex]['start_time']
        endTime = data['fights'][fidIndex]['end_time']
        playerId = 0
        for player in data['friendlies']:
            if(player['name'] == name ):
                playerId = player['id']
        if(playerId ==0):
            return
        #playerid may equals to 0 cos html encoded name
        # yield {
        #     'st':startTime,
        #     'et':endTime,
        #     'pid':playerId,
        #     'rid':rid,
        #     'fid':fid,
        # }
        #    https://www.warcraftlogs.com/reports/summary/VBnk8DgpQrN7HhAq/35/13980848/15239847/331/0/Any/0/-1.0.-1/0
        finalUrl = "https://www.warcraftlogs.com/reports/summary/"+rid+"/"+fid+"/"+str(startTime)+"/"+str(endTime)+"/"+str(playerId)+"/0/Any/0/-1.0.-1/0"+"?rid="+rid+"&fid="+fid+"&name="+name+"&keystone="+keystone
        yield scrapy.Request(url=finalUrl, callback=self.parse3)

      

    def parse3(self, response):
        url = str(response.url) # response is a plain json text
        result=urlparse.urlparse(url) 
        params=urlparse.parse_qs(result.query,True)
        rid = params['rid'][0]
        fid = params['fid'][0]
        keystone = params['keystone'][0]
        fidIndex = int(fid)-1
        name = str(params['name'][0])
        baseInfos = response.xpath("/html/body/div[2]/table/tbody/tr/td/span")
        res = {}
        res['keystone'] = keystone
        res['name'] = name
        #analysis base info like haste
        for baseInfo in baseInfos:
            infoName = baseInfo.xpath("text()").extract_first()[0:3]
            infoNum = baseInfo.xpath("span/span/text()").extract_first()
            res[infoName]=infoNum
        #analysisi legendary
        legendary = response.xpath("//a[@class='legendary']/span/text()").extract()
        res['legend1'] = legendary[0]
        res['legend2'] = legendary[1]
        #analysis telnet
        telents = response.xpath("//table[@id='summary-talents-0']/tbody/tr/td/a/span/text()").extract()
        res['t1'] = telents[0]
        res['t2'] = telents[1]
        res['t3'] = telents[2]
        res['t4'] = telents[3]
        res['t5'] = telents[4]
        res['t6'] = telents[5]
	return WclItem(res)
