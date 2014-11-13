/**********************************************************************
 *
 *   Customize the values given below to suit your needs.
 *   You can make additional copies of this file with
 *   different customizated settings if you need to load
 *   jsMath with different parameters.
 *   
 *   Load this page via:
 *   
 *   <SCRIPT SRC="path-to-jsMath/easy/load.js"></SCRIPT>
 *   
 *   (If you are including this file into your page via Server-Side
 *   Includes, you should remove line above.)
 *   
 *   You can make copies of this file with different settings
 *   if you need to have several different configurations.
 *   
 **********************************************************************/

if (!window.jsMath) {window.jsMath = {}}

jsMath.Easy = {
  //
  //  The URL of the root jsMath directory on your server
  //  (it must be in the same domain as the HTML page).
  //  It should include "http://yoursite.com/", or should
  //  be relative to the root of your server.  It is possible
  //  to be a relative URL, but it will be relative to the
  //  HTML page loading this file.
  //  
  //  If you leave this blank, jsMath will try to look it up from
  //  the URL where it loaded this file, but that may not work.
  //
  root: "",
  
  //
  //  The default scaling factor for mathematics compared to the
  //  surrounding text.
  //
  scale: 120,
  
  //
  //  1 means use the autoload plug-in to decide if jsMath should be loaded
  //  0 means always load jsMath
  //
  autoload: 1,

  //
  //  Setting any of these will cause the tex2math plugin to be used
  //  to add the <DIV> and <SPAN> tags that jsMath needs.  See the
  //  documentation for the tex2math plugin for more information.
  //
  processSlashParens: 1,       // process \(...\) in text?
  processSlashBrackets: 1,     // process \[...\] in text?
  processDoubleDollars: 1,     // process $$...$$ in text?
  processSingleDollars: 0,     // process $...$ in text?
  processLaTeXenvironments: 0, // process \begin{xxx}...\end{xxx} outside math mode?
  fixEscapedDollars: 0,        // convert \$ to $ outside of math mode?
  doubleDollarsAreInLine: 0,   // make $$...$$ be in-line math?
  allowDisableTag: 1,          // allow ID="tex2math_off" to disable tex2math?
  //
  //  If you want to use your own custom delimiters for math instead
  //  of the usual ones, then uncomment the following four lines and
  //  insert your own delimiters within the quotes.  You may want to
  //  turn off processing of the dollars and other delimiters above
  //  as well, though you can use them in combination with the
  //  custom delimiters if you wish.  See the tex2math documentation
  //  for more details.
  //  
  //customDelimiters: [
  //  '[math]','[/math]',        // to begin and end in-line math
  //  '[display]','[/display]'   // to begin and end display math
  //],

  //
  //  Disallow the use of the @(...) mechanism for including raw HTML
  //  in the contents of \hbox{}?  (If used in a content-management system
  //  where users are allowed to enter mathematics, setting this to 0
  //  would allow them to enter arbitrary HTML code within their
  //  math formulas, and that poses a security risk.)
  //
  safeHBoxes: 1,

  //
  //  Show TeX source when mathematics is double-clicked?
  //
  allowDoubleClicks: 1,
  
  //
  //  Show jsMath font warning messages?  (Disabling this prevents yours
  //  users from finding out that they can have a better experience on your
  //  site by installing some fonts, so don't disable this).
  //
  showFontWarnings: 0,
  
  //
  //  Use "Process" or "ProcessBeforeShowing".  See the jsMath
  //  author's documentation for the difference between these
  //  two routines.
  //
  method: "Process",

  //
  //  List of plug-ins and extensions that you want to be
  //  loaded automatically.  E.g.
  //      ["plugins/mimeTeX.js","extensions/AMSsymbols.js"]
  //  
  loadFiles: [],
  
  //
  //  List of fonts to load automatically.  E.g.
  //      ["cmmib10"]
  //
  //issue OLAT-6528 => use only non standard jsmath font here (except of "cmbx10","cmex10","cmmi10","cmr10","cmsy10","cmti10")  
  // => Non standards jsmath fonts are ready to use it (see *.zip under /fonts directory)
  // => when necessary enable them to unpack corresponding *.zip under /fonts and make entry in this array
  //loadFonts: ["bbold10","cmbsy10","cmmib10","cmss10","eufb10","eufm10","eurb10","eurm10","eusm10","eusb10","lasy10","lasyb10","msam10","msbm10","rsfs10","stmary10","wasy10","wasyb10"],
  loadFonts: [],
  
  //
  //  Allow jsMath to enter global mode?
  //  (Uses frames, so may not always work with complex web sites)
  //
  allowGlobal: 0,
  
  //
  //  Disable image fonts?  (In case you don't load them on your server.)
  //
  // issue OLAT-6528 => enabled using of image fonts. need to consider possible performance problems
  noImageFonts: 0
  
};

// CUSTOM OLAT CONFIGURATION
jsMath.Easy.allowGlobal = 0;
jsMath.Easy.showFontWarnings = 0;
var scriptTags = document.getElementsByTagName("script");
for (var i=0; i< scriptTags.length; i++) {
	var scriptSrc = scriptTags[i].src;
	var mathPos = scriptSrc.indexOf("js/jsMath/easy/load.js");
	if (mathPos != -1) {
		jsMath.Easy.root = scriptSrc.substring(0,mathPos) + "js/jsMath";
		break;
	}
}
//

/****************************************************************/
/****************************************************************/
//
//            DO NOT MAKE CHANGES BELOW THIS
//
/****************************************************************/
/****************************************************************/

if (jsMath.Easy.root == "") {
  jsMath.Easy.root = document.getElementsByTagName("script");
  jsMath.Easy.root = jsMath.Easy.root[jsMath.Easy.root.length-1].src
  if (jsMath.Easy.root.match(/\/easy\/[^\/]*$/)) {
    jsMath.Easy.root = jsMath.Easy.root.replace(/\/easy\/[^\/]*$/,"");
  } else {
    jsMath.Easy.root = jsMath.Easy.root.replace(/\/(jsMath\/(easy\/)?)?[^\/]*$/,"/jsMath");
  }
}
jsMath.Easy.root = jsMath.Easy.root.replace(/\/$/,""); // trim trailing "/" if any

document.write('<SCRIPT SRC="'+jsMath.Easy.root+'/jsMath-easy-load.js"><'+'/SCRIPT>');

