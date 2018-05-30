![Seeny - JSON Script Interpreter for Selenium, powered by Geeny.io](seeny-logo.png)

## Introduction

Seeny is a wrapper for Selenium that makes it possible to write tests
for websites by creating a list of script elements using JSON. It was developed
by the QA team at Telefónica Next/Geeny.io to reduce the amount of time
necessary to write test scripts for new websites. Seeny takes a JSON test script
as a parameter and executes it in Selenium via
Docker Compose.

## Requirements

You must meet the following requirements to use Seeny:

- **Java** is installed on your system.
- **Docker** is installed on your system.
- Your **Java path** is set correctly.
- The **jar file** of this project has been downloaded.

## Running Seeny

### Run scripts with Docker

The easiest method is to run Seeny test scripts using Docker. You must set the
scriptname as an environment variable. The script name must refer to an existing
script in either the `src/main/resources/websites` or `<path to the jar
file>/websites`  (created during build time) directory. You can access this
directory using the Docker volume mapped to `/scripts`.

Example on **Linux/Mac OS:**

```bash
SCRIPTNAME=www.google.com docker-compose up
```

On **Windows:**

```bash
set SCRIPTNAME=www.google.com && docker-compose up
```

### Configure/run Seeny manually

There are two methods for configuring and running Seeny manually:

#### Local folder method

When running a test script, Seeny will search the following directories for the
test script (in this order):

1. `/config`: This folder contains files that specify which websites to test (`websitesToTest.json`)
and which drivers are available (`availableDrivers.json`).
2. `/websites`: Store all of your JSON test scripts in this folder.

If a file cannot be found in these folders, Seeny will check the `/resources`
folder.

**Execution**

Running Seeny using the local folder method is fairly easy:

```bash
java -jar jar_file_name.jar
```

#### Parameter method

Seeny can also be started by forwarding parameters. If parameters are added to
the execution, Seeny will ignore any configuration files inside the `/config`
folder and will only execute the folder given as a parameter.

The following parameters are available:

1.  `d <driver_name>`: The driver to be used for the test (default is
`chromedriver`). 
2.  `w <website_file_name>`: The website to be tested (default is `test-website`).
3.  `r <# retries>`: Number of times to re-run the test.
4.  `--log-file-only`: Flag to tell Seeny to write only to the logfile and not
to the console.
5.  `--log-console-only`: Flag to tell Seeny to write only to the console and
not to the logfile.
6.  `--silent`: Flag to tell Seeny to be silent.
7.  `-pp-padding <# paddingsize>`: Flag to define the padding for pretty print.
8.  `-pp-width <# width>`: Flag to define the width for pretty print.

Available drivers:

1. `chromedriver`
2. `headless`

**Execution:**

Example of how to execute Seeny manually using parameters:

```bash
java -jar build/libs/shadow-1.0-all.jar -d chromedriver -w cmnty.geeny.io
```

## Script elements

### Website element (main object)

The main JSON object used by Seeny is `website.json`. You must name the test
script file after the website to be tested (e.g., `geeny.io.json` for geeny.io).

**Schema:**

```json
{
  "name": "Geeny IO",
  "close": true,
  "description": "Script to test the Geeny.io website",
  "zap-proxy": {
    "enabled": true,
    "url": "localhost",
    "port": 1337
  },
  "script": [
    {
      "actionId": 1,
      "actionType": "click",
      "actionTarget": "link",
      "id": "header_register_link"
    }
  ]
}
```

**Properties:**

1. `name`: The name of the site.
2. `close`: Specifies if the window shall be closed after the test.
3. `description`: A description of the test script.
4. `zap-proxy`: Configuration for redirecting the communication of Selenium
through an [OWASP Zed Attack Proxy (ZAP)](https://github.com/zaproxy/zaproxy/wiki/Introduction).
5. `script`: An array of script elements for Seeny to execute to test the
website.

### Basic

The basic script eleemnt is an abstract element and can not be addressed by
itself, although it has attributes which every other script element can refer
to.

**Schema:**

```json
{
  "actionId": 3,
  "actionType": "condition",
  "selectorType": "css",
  "dataComponentName": "TextInput.firstname",
  "selectorExpression": "EmailField",
  "child": {
  	
  },
  "waitBefore": 3000,
  "waitAfter": 3000,
  "required": true,
  "memory": "memoryName",
  "description": "The Error message"
}
```

**Properties:**

1. `actionId`: Specifies the order of this script element in the list of scripts
to execute.
2. `actionType`: The type of action script element.
3. `selectorType`: The selector type which shall be used to find the element.
4. `dat-comp` : Value to be used for adressing the element via css selector.
5. `selector`: Value to be used for finding the web element.
6. `child`: Child element which shall be found in relation to the parent object.
6. `waitBefore`: Time to wait (in milliseconds) before the script element executes itself.
7. `waitAfter`: Time to wait (in milliseconds) after the script element executes itself.
8. `required`: Boolean value if this step is required. If it fails, the test
will end.
9. `memory`: Specifies whether to store the value of this element for later.
8. `description`: A description of the script element for the logs.


### Condition

**Structure:**

```json
{
  "actionId": 3,
  "actionType": "condition",
  "selectorType": "id",
  "selectorExpression": "errormsg_0_Email",
  "bool": true,
  "task": "compare",
  "value": "Sorry, Google doesn't recognize that email. Create an account using that address?",
  "description": "The Error message"
}
```

**Properties:**

1. `bool`: Boolean value which represents the desired outcome.
2. `task`: What shall be compared.
3. `value`: If a string is going to be compared, then the script checks the
value of the element for this string.


### Clickable

**Schema:**

```json
{
  "actionId": 2,
  "actionType": "click",
  "selectorType": "id",
  "selectorExpression": "next",
  "redirect": null,
  "description": "Button to go forward"
}
```

**Properties:**


`redirect`: URL where the click should redirect the test.


### Input

**Schema:**

```json
{
  "actionId": 5,
  "actionType": "write | delete",
  "selectorType": "id",
  "selectorExpression": "Email",
  "content": "mobiletesting@geeny.io",
  "description": "Input field for the email"
}
```

**Properties:**

`content`: Content to be written into or deleted from the element.

### Navigate

**Schema:**

```json
{
  "actionId": 5,
  "actionType": "navigate",
  "navigateType": "goto",
  "tabId": 0,
  "expected": "http://www.google.de",
  "description": "Input field for the email"
}
```

**Properties:**

1. `navigateType`: The type of navigation action to perform.
2. `tabId`: Numerical number representing the tab in the browser.
2. `expected`: The URL which is expected to load after execution.

### LinkCheck

**Schema:**

```json
{
  "actionId": 5,
  "actionType": "linkCheck",
  "domain": "geeny.io",
  "subdomain": "docs",
  "excludes": [
  	"sdk/",
  	"api/"
  ],
  "https": true,
  "depth": 3,
  "recursive": true,
  "description": "Check website for broken links"
}
```
**Properties:** 

1. `domain`: The domain to be checked.
2. `subdomain`: Specifies a subdomain to be checked. If a subdomain is set,
then the linkchecker will only check links within the subdomain.
3. `excludes`: A list of paths to ignore when checking links. Whenever a link
contains a string from this list, it will be ignored.
4. `https`: Specifies whether to use HTTPS.
5. `depth`: If recursive is enabled, how deep shall the link checker go?
6. `recursive`: Specifies whether the checker should check the links
recursively.

### Extract

The extract element is used for extracting information out of an element on a
webpage for later use.

**Schema:**

```json
{
  "actionId": 9,
  "actionType": "extract",
  "selectorType": "xpath",
  "selectorExpression": "//*[@class='DeployCLIInstructions__snippet']/p[2]",
  "memory": "second_command",
  "items": [
    {
      "key": "registry_path",
      "task": "FIND",
      "regexp": "\"(.*)\"",
      "description": "registry path"
    },
    {
      "key": "formula_id",
      "task": "FIND",
      "regexp": "\".*/(.*)\"",
      "description": "formula id"
    }
  ],
  "waitAfter": 2000,
  "description": "Second Command for using the docker registry"
}
```

**Properties:**

1. `items`: A list of item script elements for extracting multiple items.

### Item

the item script element is used as a sub element of the extract element and is used for making it possible to extract multiple information from one element of a website

**Schema:**

```json
{
	"key": "registry_path",
	"task": "FIND",
	"value": "Success",
	"regexp": "\"(.*)\"",
	"description": "registry path"
}
```

**Properties:**

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
	"name": "<website name>",
	"close": true,
	"script": []
}
```


__input__


```json
{
	"actionId": 0,
	"actionType": "delete write",
	"selectorType": "id css comp",
	"dataComponentName": "",
	"selectorExpression": "",
	"content": "",
	"waitBefore": 0,
	"waitAfter": 0,
	"description": ""
}
```

__click__


```json
{
	"actionId": 0,
	"actionType": "click",
	"selectorType": "css id comp",
	"dataComponentName": "",
	"selectorExpression": "",
	"redirect": "",
	"waitBefore": 0,
	"waitAfter": 0,
	"description": ""
}
```

__condition__


```json
{
	"actionId": 0,
	"actionType": "condition",
	"selectorType": "css id comp",
	"dataComponentName": "",
	"selectorExpression": "",
	"bool": true false,
	"task": "enabled displayed empty selected compare",
	"value": "",
	"waitBefore": 0,
	"waitAfter": 0,
	"description": ""
}
```

__navigate__


```json
{
	"actionId": 0,
	"actionType": "navigate",
	"navigateType": "refresh back forward goto switchTab closeTab",
	"tabId": 0,
	"dataComponentName": "",
	"selectorExpression": "",
	"expected": "",
	"waitBefore": 0,
	"waitAfter": 0,
	"description": ""
}
```

__linkCheck__


```json
{
	"actionId": 0,
	"actionType": "linkCheck",
	"domain": "",
	"subdomain": "",
	"excludes": [
		"sdk/",
		"api/"
	],
	"https": true false,
	"depth": 0,
	"recursive": true false,
	"description": ""
}
```

__extract__

```json
{
	"actionId": 9,
	"actionType": "extract",
	"selectorType": "xpath",
	"selector": "//*[@class='DeployCLIInstructions__snippet']/p[2]",
	"memory": "second_command",
	"items": [{
			"key": "registry_path",
			"task": "FIND",
			"regexp": "\"(.*)\"",
			"description": "registry path"
		},
		{
			"key": "formula_id",
			"task": "FIND",
			"regexp": "\".*\/(.*)\"",
			"description": "formula id"
		}
	],
	"waitAfter": 2000,
	"description": "Second Command for using the docker registry"
}
```

## Maintainers

**Marco Bierbach** 

-  [Email: marco@bierbach.biz](mailto:marco@bierbach.biz) 
-  [Github](https://github.com/Gardosen)

**Nils Balkow-Tychsen** 

-  [Github](https://github.com/Nilsty)

**Klemen Samsa** 

-  [Email: klemen.samsa@gmail.com](mailto:klemen.samsa@gmail.com)

## License

Copyright (C) 2017 Telefónica Germany Next GmbH, Charlottenstrasse 4, 10969 Berlin.

This project is licensed under the terms of the Mozilla Public License Version 2.0.