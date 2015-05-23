from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys
from selenium.common.exceptions import *

import time
import logging
import sys
import json

#########################################
#			Selenium Utilities			#
#########################################
def openNewTab(browser):
	if sys.platform == "darwin":
		ActionChains(browser).send_keys(Keys.COMMAND, "t").perform()
		ActionChains(browser).send_keys(Keys.COMMAND, "t").perform()
	elif sys.platform == "linux2":
		ActionChains(browser).send_keys(Keys.CONTROL, "t").perform()
		ActionChains(browser).send_keys(Keys.CONTROL, "t").perform()
	else:
		logging.error("openNewTab unsupported OS: %s"%sys.platform)

def closeCurrentTab(browser):
	if sys.platform == "darwin":
		browser.find_element_by_tag_name('body').send_keys(Keys.COMMAND + 'w')
	elif sys.platform == "linux2":
		browser.find_element_by_tag_name('body').send_keys(Keys.CONTROL + 'w')
	else:
		logging.error("closeCurrentTab unsupported OS: %s"%sys.platform)


#########################################
#			Initialization Utilities #
#########################################
def readConfigure(file_path):
	try:
		json_file = open(file_path)
		data = json.load(json_file)
		json_file.close()
		return data
	except Exception as e:
	    logging.error("failed to parse configure file "+file_path+" "+str(e))
	    return None

def readHostList(filePath):
	f = open(filePath)
	data = []
	for line in f:
		line = line.strip()
		data.append(line)
	return data


#########################################
        #		  	Main				#
#########################################
def main():
  ######### initialization logging #######
  data = readConfigure(sys.argv[1])
  if data == None:
    print("failed to read configure file") 
    return

  LOG_FILENAME = data['logfile']
  logging.basicConfig(filename=LOG_FILENAME, level=logging.DEBUG)

  ######### read initialization file #######
  logging.info("reading url list....")    
  urlList = readHostList('./archive/AlexaTopListUSA.txt')


  logging.info("start to process urls")    
  maxPage = 2
  process(urlList, data, maxPage)

def process(urlList, data, maxPage):
	
  # (1) Initializing firfox web driver
  profile1 = webdriver.FirefoxProfile(data['firefoxProfilePathWithProxy'])
  profile2 = webdriver.FirefoxProfile(data['firefoxProfilePathWithoutProxy'])
  browser1 = webdriver.Firefox(profile1)
  browser2 = webdriver.Firefox(profile2)

  # (2) Waiting load the page time to be 30s
  browser1.set_page_load_timeout(30)
  browser2.set_page_load_timeout(30)

  # (3) Open 5 tabs for each browser in case open tab failed latter
  for i in range(5):
    openTab(browser1)
    openTab(browser2)

  counter = 1
  f = open('loadTime.txt', 'a')

  for url in urlList:
    if counter > maxPage:
      break
    logging.info("processing " + url)
    print "###########################   " + str(counter) + "   #############################"
    print "using proxy: " + url
    fileName = str(counter) + '_1.png'
    capture(url, browser1, counter, f, fileName)
    print

    print "using default: " + url
    fileName = str(counter) + '_2.png'
    capture(url, browser2, counter, f, fileName)
    counter += 1
    print
    print

  browser1.close()
  browser2.close()

def capture(url, browser, counter, f, fileName):
  openTab(browser)
  startT = time.time()

  if openURL(url, browser, counter):
    endT = time.time()
    f.write(fileName + ' : ' + str(endT - startT) + '\n')
    takeScreenShot(browser, counter, fileName, url)

  closeTab(browser)

def openURL(url, browser, counter):
  print "loading url in process..."
  try:
    browser.get(url)
  except TimeoutException:
    print "####--------->Timeout, capture screenshot anyway..."
    logging.error("Loading " + url + " Timeout!")
    return True
  except:
    print "####--------->load url failed, NO screenshot taking"
    logging.error("Loading " + url + " Failed!")
    return False
  else:
    return True 

def openTab(browser):
  try:
    openNewTab(browser)
  except:
    print "####---------->open new tab failed..."
    logging.error("Open new tab failed")
  finally:
    return 
  
def closeTab(browser):
  try:
    closeCurrentTab(browser)
  except:
    print "####---------->closing tab failed, leave it open..."
    logging.error("closing tab failed, levae it open...")
  finally:
    return 

def takeScreenShot(browser, counter, fileName, url):
  print "start taking screen shot..."
  try:
    browser.save_screenshot('./screenshots/' + fileName)
  except: 
    print "taking screenshot failed, return...."
    logging.error("takeing screenshot of " + url + "failed")
  finally:
    print "taking screenshot complete"
    return 
    
if __name__ == "__main__":
	main()
	
