#coding=utf8
import httplib, urllib

def http_post(host, api, headers, params):
	return do_http('POST', host, api, headers, params)

def http_get(host, api, headers, params):
	return do_http('GET', host, api, headers, params)

def do_http(method, host, api, headers, params):
	httpClient = None
	try:
		encoded_params = urllib.urlencode(params)

		httpClient = httplib.HTTPConnection(host, 80, timeout=30)
		httpClient.request(method, api, encoded_params, headers)

		response = httpClient.getresponse()
		data = response.read()
		print '[%s] %s%s' % (method, host, api)
		print response.status, data
		# print response.reason
		# print response.getheaders()
		return data
	except Exception, e:
		print e
	finally:
		if httpClient:
			httpClient.close()

def get_bus_id():
	headers = {
		'Accept-Language': 'zh-CN',
		'X-Requested-With': 'XMLHttpRequest',
		'Accept-Charset': 'utf-8, iso-8859-1, utf-16, *;q=0.7',
		'Referer': 'http://wxbus.gzyyjt.net/wei-bus-app/route?openId=ouz9MsyNIpeYEMJEhI7E-peH3oOk',
		'User-Agent': 'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 3W Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 MicroMessenger/6.3.15.65_r81f6835.760 NetType/WIFI Language/zh_CN',
		'Origin': 'http://wxbus.gzyyjt.net',
		'Accept': '*/*',
		'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
		'Accept-Encoding': 'gzip,deflate',
		'Host': 'wxbus.gzyyjt.net',
		'Cookie': 'realOpenId=ouz9MsyNIpeYEMJEhI7E-peH3oOk; openId=ouz9MsyNIpeYEMJEhI7E-peH3oOk'
	}
	print headers
	http_post('wxbus.gzyyjt.net', '/wei-bus-app/route/getByName', headers, {'name': 777})

def get_busstop_name():
	headers = {
		'Referer': 'http://wxbus.gzyyjt.net/wei-bus-app/route/monitor/5410/0',
		'Accept-Language': 'zh-CN',
		'User-Agent': 'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 3W Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 MicroMessenger/6.3.15.65_r81f6835.760 NetType/WIFI Language/zh_CN',
		'Accept': 'application/json, text/javascript, */*; q=0.01',
		'X-Requested-With': 'XMLHttpRequest',
		'Accept-Charset': 'utf-8, iso-8859-1, utf-16, *;q=0.7',
		'Accept-Encoding': 'gzip',
		'Host': 'wxbus.gzyyjt.net',
		'Cookie': 'realOpenId=ouz9MsyNIpeYEMJEhI7E-peH3oOk; openId=ouz9MsyNIpeYEMJEhI7E-peH3oOk'
	}
	http_get('wxbus.gzyyjt.net', '/wei-bus-app/routeStation/getByRouteAndDirection/5410/0', headers, {})

def get_location():
	headers = {
			'Referer': 'http://wxbus.gzyyjt.net/wei-bus-app/route/monitor/5410/0',
			'Accept-Language': 'zh-CN',
			'User-Agent': 'Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI 3W Build/KTU84P) AppleWebKit/533.1 (KHTML, like Gecko)Version/4.0 MQQBrowser/5.4 TBS/025489 Mobile Safari/533.1 MicroMessenger/6.3.15.65_r81f6835.760 NetType/WIFI Language/zh_CN',
			'Accept': 'application/json, text/javascript, */*; q=0.01',
			'X-Requested-With': 'XMLHttpRequest',
			'Accept-Charset': 'utf-8, iso-8859-1, utf-16, *;q=0.7',
			'Accept-Encoding': 'gzip',
			'Host': 'wxbus.gzyyjt.net',
			'Cookie': 'realOpenId=ouz9MsyNIpeYEMJEhI7E-peH3oOk; openId=ouz9MsyNIpeYEMJEhI7E-peH3oOk'
		}
	http_get('wxbus.gzyyjt.net', '/wei-bus-app/runBus/getByRouteAndDirection/5410/0', headers, {})

def main():
	get_bus_id()
	get_location()
	get_busstop_name()

if __name__ == '__main__':
	main()