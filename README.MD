![Seeny - Json Script Interpreter for Selenium, powered by Geeny.io](seeny-logo.png)


## Maintainer

_Marco Bierbach_ [email: marco@bierbach.biz](mailto:marco@bierbach.biz) [github link](https://github.com/Gardosen)

_Nils Balkow-Tychsen_ [github link](https://github.com/Nilsty)

_Klemen Samsa_ [email: klemen.samsa@gmail.com](mailto:klemen.samsa@gmail.com)


## Introduction

This Document will describe how the Selenium Wrapper called Seeny,
developed by the QA Team of Telefonica Next / Geeny.io, works and
how it can be used

## Purpose

With the release of the Platform and also with a growing success,
the websites which needs to be tested, will grow exponential.
Writing Selenium Tests for these websites, with a team of 3 people
would overload the team on some point, and reduce the time available
for other tasks. Seeny is supposed to jump into this area as a script
interpreter. With the help of Seeny, test scripts for new websites, can
be written within under an hour.

## Functionality

Seeny makes it possible, to write selenium
tests in json. by creating a list of script elements, a user can let Seeny
test any website, which is supposed to be tested. via parameters, it can
be decided which driver is going to be used, which website shall be tested
and also how often the website should be tested in a row.

## Setup

### Requirements
to be able to execute the test, the following requirements have to be meet:
- java is installed on the system
- java path is set correctly
- jar file of this project has been downloaded

### Execute scripts with Docker

In order to execute a specific script the scriptname has to be set as an environment variable.
e.g.
on Linux/MacOS
```bash
SCRIPTNAME=community.geeny.io docker-compose up
```

on Windows
```bash
set SCRIPTNAME=community.geeny.io && docker-compose up
```

The script name needs to refer to an existing script in either of these folders
src/main/resources/websites or <path to the jar file>/websites (created during build time) This folder can be accessed via the docker volume mapped to /scripts

### How to start seeny manually

seeny can be started in 2 ways.

**local folder method**

seeny comes with some local folders. if these folders exists, seeny will priorize the files inside these folders if no parameters exists. these folders can contain config files and test scripts. if one file is not existing inside of the folders, even tho the local folders are beeing used, seeny will check for these files in the resouces folder insid itself

_CONFIGURATION_

1. config - this folder contains files which tell seeny which websites to test and what drivers are available by the files:
- availableDrivers.json

```json
[{
"name" : "chromedriver",
"path" : "webdriver/chromedriver/",
"propertyName" : "webdriver.chrome.driver"
},
{
"name" : "headless",
"path" : null,
"propertyName" : null
}
]
```
- websitesToTest.json

```json
[
{
"name" : "cmnty.geeny.io",
"driver" : "chromedriver",
"retries" : 3,
"timeout" : 0
},
{
"name" : "geeny.io",
"driver" : "chromedriver",
"retries" : 2,
"timeout" : 0
}
]
```

2. websites

this folder contains all the json based test scripts for the websites.

_EXECUTION_

Executing seeny with the local folder method is fairly ea
```bash
java -jar jar_file_name.jar
```

**Parameter method**

seeny can also be started by forwarding parameters, if parameters are added to the execution, seeny will ignore any configuration files inside the config folder and will only execute the folder given via parameter.

_CONFIGURATION_

in order to start seeny via parameters, the following values can be given to seeny

_Parameters_
1. d <driver_name> - the driver which shall be used for the test (default is chromedriver)
2. w <website_file_name> - the website which shall be tested (default is test-website)
3. r <# retries> - how often shall this test be rerun
4. --log-file-only - flag to tell seeny to only write to the logfile and not to the console
5. --log-console-only - flag to tell seeny to only write to the console and not to the logfile
6. --silent - flag to tell seeny to be silent
7. -pp-padding <# paddingsize> - flag to define the padding for pretty print
8. -pp-width <# width> - flag to define the width for pretty print

_Drivers available_
1. chromedriver
2. headless

_EXECUTION_

Executing seeny with parameters can look like this

```bash
java -jar build/libs/shadow-1.0-all.jar -d chromedriver -w cmnty.geeny.io
```


### Elements

**Website.json**

The main json object which is used for seeny, is the website.json.
This file is named by the website it is supposed to be tested. (E.g 'geeny.io.json')

_Structure:_

```json
{
"name" : "Geeny IO",
"close" : true,
"description" : "Script to test the Geeny.io website",
"zap-proxy" : {
  "enabled" : true,
  "url" : "localhost",
  "port" : 1337
},
"script" : [
{
"actionId" : 1,
"actionType" : "click",
"actionTarget" : "link",
"id" : "header_register_link"
}]
}
```

The structure of the website file is separated into 5 elements.
1. name : the name of the webpage
2. close : boolean if the window shall be closed after finishing the test
3. description : the description for this test script
4. zap-proxy : configuration to redirect the communication of selenium through a owasp zap proxy
5. script : a list of script element which seeny shall go through to test the website

**Basic**

_Structure_

```json
{
"actionId" : 3,
"actionType" : "condition",
"selectorType" : "css",
"dataComponentName" : "TextInput.firstname",
"selectorExpression" : "EmailField",
"child" : {
...
},
"waitBefore" : 3000,
"waitAfter" : 3000,
"required" : true,
"memory" : "memoryName",
"description" : "The Error message"
}
```

This element is an abstract element and can not be addressed by itself. even tho, the Basic script element
has attributes which every other script element can refer to

1. _actionId_ : the action id, which defines which step in the script list this is
2. _actionType_ : the type of action script element
3. _selectorType_ : the selector type which shall be used to find the element
4. _data-comp_ : value to be used for adressing the element via css selector
5. _selector_ : value to be used for finding the web element
6. _child_ : sub element which shall be found related to the parent object
6. _waitBefore_ : value in milli seconds how long the script element has to wait before execute itself
7. _waitAfter_ : value in milli seconds how long the script element has to wait after execute itself
8. _required_ : boolean value if this step is required, if it fails, the test will be ended
9. _memory_ : value to show if the value of this element should be saved for later
8. _description_ : a description of the script element for the logs


**Condition**

_Structure:_

```json
{
"actionId" : 3,
"actionType" : "condition",
"selectorType" : "id",
"selectorExpression" : "errormsg_0_Email",
"bool" : true,
"task" : "compare",
"value" : "Sorry, Google doesn't recognize that email. Create an account using that address?",
"description" : "The Error message"
}

```
The Structure of the condition script element contains 3 unique elements for the Condition
1. _bool_ : boolean value which represents the desired outcome
2. _task_ : what shall be compared
3. _value_ : if a string is going to be compared, the value contains the expected string


**Clickable**

```json
{
"actionId" : 2,
"actionType" : "click",
"selectorType" : "id",
"selectorExpression" : "next",
"redirect" : null,
"description" : "Button to go forward"
}

```

The structure of the Clickable script element contains one unique element for the Clickable
2. _redirect_ : what is the url the click shall redirect the test to?


**Input**
`
_Structure:_
```json
{
"actionId" : 5,
"actionType" : "write | delete",
"selectorType" : "id",
"selectorExpression" : "Email",
"content" : "mobiletesting@geeny.io",
"description" : "Input field for the email"
}
```

The structure of the Input script element contains one unique element for the Input field
1. _content_ : what shall be written into/deleted from the input field/text area

**Navigate**

_Structure_

```json
{
"actionId" : 5,
"actionType" : "navigate",
"navigateType" : "goto",
"tabId" : 0,
"expected" : "http://www.google.de",
"description" : "Input field for the email"
}
```

The structure of the Input script element contains 2 unique elements for the navigate logic
1. _navigateType_ : the type of what navigation action shall be done
2. _tabId_ : numerical number representing the tab in the browser (mostly 0)
2. _expected_ : the website which is expected to appear after the execution

**LinkCheck**

_Structure_

```json
{
"actionId" : 5,
"actionType" : "linkCheck",
"domain" : "geeny.io",
"subdomain" : "docs",
"excludes" : [
  "sdk/",
  "api/"
],
"https": true,
"depth" : 3,
"recursive" : true,
"description" : "Check website for broken links"
}
```

LinkCheck script element can be called at any point during the script to verify links on the current page, it contains 6 specific elements
1. _domain_ : the domain which will be checked
2. _subdomain_ : the sub domain this linkcheck shall look for, if subdomain is set, the linkchecker will only check the healthiness of links with the specific subdomain
3. _excludes_ : a list of paths which shall be ignored on checking links, whenever a link contains a string from this list, this link will be ignored
4. _https_ : is https used?
5. _depth_ : if recursive is enabled, how deep shall the link checker go?
6. _recursive_ : shall the link checker go recursivly, through the websites he finds to check for other links?

**Extract**

_Structure_

```json
{
"actionId" : 9,
"actionType" : "extract",
"selectorType" : "xpath",
"selectorExpression" : "//*[@class='DeployCLIInstructions__snippet']/p[2]",
"memory" : "second_command",
"items" : [
{
"key" : "registry_path",
"task" : "FIND",
"regexp" : "\"(.*)\"",
"description" : "registry path"
},
{
"key" : "formula_id",
"task" : "FIND",
"regexp" : "\".*\/(.*)\"",
"description" : "formula id"
}
],
"waitAfter" : 2000,
"description" : "Second Command for using the docker registry"
}
```

the extract element is used for extracting information out of an element from the website, to use those information later.

The structure of the Extract element contains one unique element
1. items - list of item script elements for extracting multiple parts as single from the overall found label

**Item**

_Structure_

```json
{
"key" : "registry_path",
"task" : "FIND",
"value" : "Success",
"regexp" : "\"(.*)\"",
"description" : "registry path"
}
```
the item script element is used as a sub element of the extract element and is used for making it possible to extract multiple information from one element of a website

The item element contains 4 unique elements
1. key : key under which the found information will be stored in the memory queue
2. task : identifier if the item needs to be found, or just compared
3. value : content which will be checked for with the regular expression
4. regexp : regular expression for finding the element inside of the parental label

### Available Types

some of the items of the Script elements have different possible types which can be used to define the purpose
of the element:

1. actionType
- _write_ : defines that this script element is supposed to write a text into an input field or text area
- _delete_ : defines that this script element is supposed to delete an input field or text area from its text
- _click_ : defines that this script element is supposed to click any clickable element on the webpage
- _condition_ : defines that this script element is supposed to check for a specific condition and verify if it is true or false
- _cli_ : defines a command line interface script element, for doing tasks inside of the terminal/cmd
- _navigate_ : defines that the script is supposed to control the webbrowser itself, e.g. refreshing the page or going backwards
- _linkCheck_ : defines that the script is supposed to check ALL links on the current page for their healthiness
- _extract_ : defines that the script is supposed to extract content from the website and save it in the memory queue for later use

2. selectorType

- _css_ : use a cssSelector to find the web element
- _id_ : use the id of the element to find the web element
- _comp_ : use a cssSelector in combination with the geeny custom data-component-name element
- _xpath_ : use a XPath to find the web element

3. bool

- _true_ : defines that the condition which shall be checked, needs to be positive (not the value, the check)
- _false_ : defines that the condition which shall be checked, needs to be negative (not the value, the check)

4. task

- _enabled_ : check if the found web element is enabled/clickable
- _displayed_ : check if the found web element is visible for the user
- _empty_ : check if the found web element is empty (contains no text)
- _selected_ : check if the found web element has been selected
- _compare_ : compare the text in the found element with the expected value
- _find_ : task element to find a label based on a regular expression of the Item sub element for extract

5. navigateType

- _back_ : go backwards in the browser
- _forward_ : go forward in the browser
- _refresh_ : refresh the browser
- _goto_ : move to the specific url in the browser
- _closeTab_ : close the tab with the given tabId of this script element
- _switchTab_ : switch the browser to the given tabId of this script element

### Example Script Elements

The following elements can be used to write the script.
These dummies, contain all possible element types.

__website__

```json
{
"name" : "<website name>",
"close" : true,
"script" : [
]
}
```


__input__


```json
{
"actionId" : 0,
"actionType" : "delete write",
"selectorType" : "id css comp",
"dataComponentName" : "",
"selectorExpression" : "",
"content" : "",
"waitBefore" : 0,
"waitAfter" : 0,
"description" : ""
}
```

__click__


```json
{
"actionId" : 0,
"actionType" : "click",
"selectorType" : "css id comp",
"dataComponentName" : "",
"selectorExpression" : "",
"redirect" : "",
"waitBefore" : 0,
"waitAfter" : 0,
"description" : ""
}
```

__condition__


```json
{
"actionId" : 0,
"actionType" : "condition",
"selectorType" : "css id comp",
"dataComponentName" : "",
"selectorExpression" : "",
"bool" : true false,
"task" : "enabled displayed empty selected compare",
"value" : "",
"waitBefore" : 0,
"waitAfter" : 0,
"description" : ""
}
```

__navigate__


```json
{
"actionId" : 0,
"actionType" : "navigate",
"navigateType" : "refresh back forward goto switchTab closeTab",
"tabId" : 0,
"dataComponentName" : "",
"selectorExpression" : "",
"expected" : "",
"waitBefore" : 0,
"waitAfter" : 0,
"description" : ""
}
```

__linkCheck__


```json
{
"actionId": 0,
"actionType": "linkCheck",
"description": "check all links from this page"
}
```

__extract__

```json
{
"actionId" : 9,
"actionType" : "extract",
"selectorType" : "xpath",
"selector" : "//*[@class='DeployCLIInstructions__snippet']/p[2]",
"memory" : "second_command",
"items" : [
{
"key" : "registry_path",
"task" : "FIND",
"regexp" : "\"(.*)\"",
"description" : "registry path"
},
{
"key" : "formula_id",
"task" : "FIND",
"regexp" : "\".*\/(.*)\"",
"description" : "formula id"
}
],
"waitAfter" : 2000,
"description" : "Second Command for using the docker registry"
}
```


## License

Copyright (C) 2017 Telefónica Germany Next GmbH, Charlottenstrasse 4, 10969 Berlin.

This project is licensed under the terms of the Mozilla Public License Version 2.0.