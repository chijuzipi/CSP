
    (function(){function b(){var a=window.location.href.match(/#(.)(.*)$/);return a&&a[1]=="!"&&a[2].replace(/^\//,"")}function c(b){if(!b)return!1;b=decodeURI(b.replace(/^#|\/$/,"")).toLowerCase();return b.match(a)?b:!1}function d(a){var a=c(a);if(a){var b=document
.referrer||"none",d="ev_redir_"+encodeURIComponent(a)+"="+b+"; path=/";document.cookie=d;window.location.replace("/hashtag/"+a)}}function e(){var a=b();a&&window.location.replace("//"+window.location.host+"/"+a);window.location.hash!=""&&d(window.location.
hash.substr(1).toLowerCase())}var a=/^[a-z0-9_À-ÖØ-öø-ÿĀ-ɏɓ-ɔɖ-ɗəɛɣɨɯɲʉʋʻ̀-ͯḀ-ỿЀ-ӿԀ-ԧⷠ-ⷿꙀ-֑ꚟ-ֿׁ-ׂׄ-ׇׅא-תװ-״﬒-ﬨשׁ-זּטּ-לּמּנּ-סּףּ-פּצּ-ﭏؐ-ؚؠ-ٟٮ-ۓە-ۜ۞-۪ۨ-ۯۺ-ۼۿݐ-ݿࢠࢢ-ࢬࣤ-ࣾﭐ-ﮱﯓ-ﴽﵐ-ﶏﶒ-ﷇﷰ-ﷻﹰ-ﹴﹶ-ﻼ‌ก-ฺเ-๎ᄀ-ᇿ㄰-ㆅꥠ-꥿가-힯ힰ-퟿ﾡ-ￜァ-ヺー-ヾｦ-ﾟｰ０-９Ａ-Ｚａ-ｚぁ-ゖ゙-ゞ㐀-䶿一-鿿꜀-뜿띀-렟-﨟〃々〻]*[a-z_À-ÖØ-öø-ÿĀ-ɏɓ-ɔɖ-ɗəɛɣɨɯɲʉʋʻ̀-ͯḀ-ỿЀ-ӿԀ-ԧⷠ-ⷿꙀ-֑ꚟ-ֿׁ-ׂׄ-ׇׅא-תװ-״﬒-ﬨשׁ-זּטּ-לּמּנּ-סּףּ-פּצּ-ﭏؐ-ؚؠ-ٟٮ-ۓە-ۜ۞-۪ۨ-ۯۺ-ۼۿݐ-ݿࢠࢢ-ࢬࣤ-ࣾﭐ-ﮱﯓ-ﴽﵐ-ﶏﶒ-ﷇﷰ-ﷻﹰ-ﹴﹶ-ﻼ‌ก-ฺเ-๎ᄀ-ᇿ㄰-ㆅꥠ-꥿가-힯ힰ-퟿ﾡ-ￜァ-ヺー-ヾｦ-ﾟｰ０-９Ａ-Ｚａ-ｚぁ-ゖ゙-ゞ㐀-䶿一-鿿꜀-뜿띀-렟-﨟〃々〻][a-z0-9_À-ÖØ-öø-ÿĀ-ɏɓ-ɔɖ-ɗəɛɣɨɯɲʉʋʻ̀-ͯḀ-ỿЀ-ӿԀ-ԧⷠ-ⷿꙀ-֑ꚟ-ֿׁ-ׂׄ-ׇׅא-תװ-״﬒-ﬨשׁ-זּטּ-לּמּנּ-סּףּ-פּצּ-ﭏؐ-ؚؠ-ٟٮ-ۓە-ۜ۞-۪ۨ-ۯۺ-ۼۿݐ-ݿࢠࢢ-ࢬࣤ-ࣾﭐ-ﮱﯓ-ﴽﵐ-ﶏﶒ-ﷇﷰ-ﷻﹰ-ﹴﹶ-ﻼ‌ก-ฺเ-๎ᄀ-ᇿ㄰-ㆅꥠ-꥿가-힯ힰ-퟿ﾡ-ￜァ-ヺー-ヾｦ-ﾟｰ０-９Ａ-Ｚａ-ｚぁ-ゖ゙-ゞ㐀-䶿一-鿿꜀-뜿띀-렟-﨟〃々〻]+$/
;e();window.addEventListener?window.addEventListener("hashchange",e,!1):window.attachEvent&&window.attachEvent("onhashchange",e)})();
  
    (function(){function m(a){a||(a=window.event);if(!a)return!1;a.timestamp=(new Date).getTime();!a.target&&a.srcElement&&(a.target=a.srcElement);if(document.documentElement.getAttribute("data-scribe-reduced-action-queue")){var b=a.target;while(b&&b!=document
.body){if(b.tagName=="A")return;b=b.parentNode}}r("all",s(a));if(!q(a)){r("direct",a);return!0}document.addEventListener||(a=s(a));a.preventDefault=a.stopPropagation=a.stopImmediatePropagation=function(){};if(i){f.push(a);r("captured",a)}else r("ignored",a
);return!1}function n($){p();for(var a=0,b;b=f[a];a++){var d=$(b.target),e=d.closest("a")[0];if(b.type=="click"&&e){var g=$.data(e,"events"),i=g&&g.click,j=!e.hostname.match(c)||!e.href.match(/#$/);if(!i&&j){window.location=e.href;continue}}d.trigger(b)}window
.swiftActionQueue.wasFlushed=!0}function o(){for(var a in j){if(a=="all")continue;var b=j[a];for(var c=0;c<b.length;c++)console.log("actionQueue",u(b[c]))}}function p(){clearTimeout(g);for(var a=0,b;b=e[a];a++)document["on"+b]=null}function q(a){if(!a.target
)return!1;var b=a.target,e=(b.tagName||"").toLowerCase();if(a.metaKey)return!1;if(a.shiftKey&&e=="a")return!1;if(b.hostname&&!b.hostname.match(c))return!1;if(a.type.match(d)&&w(b))return!1;if(e=="label"){var f=b.getAttribute("for");if(f){var g=document.getElementById
(f);if(g&&v(g))return!1}else for(var i=0,j;j=b.childNodes[i];i++)if(v(j))return!1}return!0}function r(a,b){b.bucket=a;j[a].push(b)}function s(a){var b={};for(var c in a)b[c]=a[c];return b}function t(a){while(a&&a!=document.body){if(a.tagName=="A")return a;
a=a.parentNode}}function u(b){var c=[];b.bucket&&c.push("["+b.bucket+"]");c.push(b.type);var d=b.target,e=t(d),f="",g,i,j=b.timestamp&&b.timestamp-a;if(b.type==="click"&&e){g=e.className.trim().replace(/\s+/g,".");i=e.id.trim();f=/[^#]$/.test(e.href)?" ("+
e.href+")":"";d='"'+e.innerText.replace(/\n+/g," ").trim()+'"'}else{g=d.className.trim().replace(/\s+/g,".");i=d.id.trim();d=d.tagName.toLowerCase();b.keyCode&&(d=String.fromCharCode(b.keyCode)+" : "+d)}c.push(d+f+(i&&"#"+i)+(!i&&g?"."+g:""));j&&c.push(j);
return c.join(" ")}function v(a){var b=(a.tagName||"").toLowerCase();return b=="input"&&a.getAttribute("type")=="checkbox"}function w(a){var b=(a.tagName||"").toLowerCase();return b=="textarea"||b=="input"&&a.getAttribute("type")=="text"||a.getAttribute("contenteditable"
)=="true"}var a=(new Date).getTime(),b=1e4,c=/^([^\.]+\.)*twitter\.com$/,d=/^key/,e=["click","keydown","keypress","keyup"],f=[],g=null,i=!0,j={captured:[],ignored:[],direct:[],all:[]};for(var k=0,l;l=e[k];k++)document["on"+l]=m;g=setTimeout(function(){i=!1
},b);window.swiftActionQueue={buckets:j,flush:n,logActions:o,wasFlushed:!1}})();
  
    (function(){function a(a){a.target.setAttribute("data-in-composition","true")}function b(a){a.target.removeAttribute("data-in-composition")}if(document.addEventListener){document.addEventListener("compositionstart",a,!1);document.addEventListener("compositionend"
,b,!1)}})();
  
        document.body.className=document.body.className+" "+document.body.getAttribute("data-fouc-class-names");
      
        document.cookie="h=%5B%7B%22newer_tweet_id%22%3A%22487246303181959168%22%2C%22older_tweet_id%22%3A%22487246244386177024%22%2C%22promoted_content%22%3A%7B%22impression_id%22%3A%22105e832bfc061e25%22%2C%22disclosure_type%22%3A%22promoted%22%2C%22disclosure_text%22%3A%22%22%7D%2C%22experiment_values%22%3A%7B%22pac_in_timeline%22%3A%22true%22%2C%22display.display_style%22%3A%22show_social_context%22%2C%22suppress_media_forward%22%3A%22true%22%7D%2C%22advertiser_id%22%3A%22240745467%22%2C%22created_at%22%3A%221405003564%22%2C%22tweet_id%22%3A%22479664908531802112%22%7D%5D; Expires=Thu, 10 Jul 2014 15:16:04 GMT; Path=/"
    