# Content Security Policy Applier #

1. Extract all in html script 
2. Generate JSON template for every URL 
3. Filter undesired script according to local template
4. Generate CSP header

## Usage ##

``` bash
cspapplier.exe [Input file Path/Name] [Output Path] [HTTP Path] [URL] [Sample Mode]
```
* `Output Path` is the path where the HTML/JS/CSS files are placed.
* `HTTP Path` is the path filled in the `src` attribute