(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-0bb87d21"],{"14c3":function(t,e,n){var r=n("c6b6"),o=n("9263");t.exports=function(t,e){var n=t.exec;if("function"===typeof n){var a=n.call(t,e);if("object"!==typeof a)throw TypeError("RegExp exec method returned something other than an Object or null");return a}if("RegExp"!==r(t))throw TypeError("RegExp#exec called on incompatible receiver");return o.call(t,e)}},"1dde":function(t,e,n){var r=n("d039"),o=n("b622"),a=n("2d00"),i=o("species");t.exports=function(t){return a>=51||!r((function(){var e=[],n=e.constructor={};return n[i]=function(){return{foo:1}},1!==e[t](Boolean).foo}))}},"25f0":function(t,e,n){"use strict";var r=n("6eeb"),o=n("825a"),a=n("d039"),i=n("ad6d"),c="toString",l=RegExp.prototype,s=l[c],u=a((function(){return"/a/b"!=s.call({source:"a",flags:"b"})})),b=s.name!=c;(u||b)&&r(RegExp.prototype,c,(function(){var t=o(this),e=String(t.source),n=t.flags,r=String(void 0===n&&t instanceof RegExp&&!("flags"in l)?i.call(t):n);return"/"+e+"/"+r}),{unsafe:!0})},"38a0":function(t,e,n){"use strict";n("ac1f"),n("5319");var r=n("e423"),o="/mng/job/queryJob/",a="/mng/job/queryJobs",i="/mng/job/addJob",c="/mng/job/updateJob",l="/mng/job//deleteJob/{jobId}",s="/mng/job/deleteJobs",u="/mng/job/{jobId}/jarList",b="/mng/job/{jobId}/uploadJar",d="/mng/job/startJob/{jobId}",f="/mng/job/startJobs",p="/mng/job/reStartJob/{jobId}",v="/mng/job/reStartJobs",g="/mng/job/startJob/{jobId}",j="/mng/job/stopJobs";function h(t){return Object(r["a"])(o+t.jobId)}function m(t){return Object(r["b"])(a,t)}function x(t){return Object(r["b"])(i,t)}function E(t){return Object(r["b"])(c,t)}function y(t){return Object(r["b"])(l.replace("{jobId}",t.jobId),t)}function I(t){return Object(r["b"])(s,t.idList)}function J(t){return Object(r["a"])(u.replace("{jobId}",t.jobId))}function O(t){return Object(r["a"])(d.replace("{jobId}",t.jobId))}function S(t){return Object(r["b"])(f,t.idList)}function k(t){return Object(r["a"])(p.replace("{jobId}",t.jobId))}function R(t){return Object(r["b"])(v,t.idList)}function $(t){return Object(r["a"])(g.replace("{jobId}",t.jobId))}function _(t){return Object(r["b"])(j,t.idList)}e["a"]={UPLOAD_JAR_URL:b,queryJob:h,queryJobs:m,addJob:x,updateJob:E,deleteJob:y,deleteJobs:I,jarList:J,startJob:O,startJobs:S,restartJob:k,restartJobs:R,stopJob:$,stopJobs:_}},"426a":function(t,e,n){"use strict";n.r(e);var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[1===t.type?n("div",[n("JobEditCustom")],1):t._e()])},o=[],a=n("d4ec"),i=n("99de"),c=n("7e84"),l=n("262e"),s=n("9ab4"),u=n("60a3"),b=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("Tabs",[n("TabPane",{attrs:{label:"基本配置",name:"basic"}},[n("Form",{attrs:{model:t.jobEdit,"label-width":100}},[n("FormItem",{attrs:{label:"作业名称 :"}},[n("Input",{attrs:{placeholder:""},model:{value:t.jobEdit.name,callback:function(e){t.$set(t.jobEdit,"name",e)},expression:"jobEdit.name"}})],1),n("FormItem",{attrs:{label:"作业类型 : "}},[n("Select",{model:{value:t.jobEdit.type,callback:function(e){t.$set(t.jobEdit,"type",e)},expression:"jobEdit.type"}},t._l(t.hintJobTypeList,(function(e){return n("Option",{key:e.value,attrs:{value:e.value}},[t._v(t._s(e.label))])})),1)],1),n("FormItem",{attrs:{label:"作业描述 :"}},[n("Input",{attrs:{type:"textarea",placeholder:"",rows:4},model:{value:t.jobEdit.description,callback:function(e){t.$set(t.jobEdit,"description",e)},expression:"jobEdit.description"}})],1)],1)],1),n("TabPane",{attrs:{label:"作业配置",name:"job"}},[n("Form",{staticStyle:{width:"80%"},attrs:{model:t.jobEdit,"label-width":100}},[n("FormItem",{attrs:{label:"客户端版本 :"}},[n("Select",{attrs:{placeholder:"请选择客户端版本"},model:{value:t.jobEdit.clientVersion,callback:function(e){t.$set(t.jobEdit,"clientVersion",e)},expression:"jobEdit.clientVersion"}},[n("Option",{attrs:{value:"1.9.0"}},[t._v("1.9.0")])],1)],1),n("FormItem",{attrs:{label:"执行文件 : "}},[n("Select",{attrs:{placeholder:"可上传文件"},model:{value:t.jobEdit.config.jarName,callback:function(e){t.$set(t.jobEdit.config,"jarName",e)},expression:"jobEdit.config.jarName"}},t._l(t.hintExecFileList,(function(e){return n("Option",{key:e.value,attrs:{value:e.value}},[t._v(t._s(e.label))])})),1),n("Upload",{attrs:{action:t.uploadJarActionUrl,"on-success":t.uploadOnSuccess}},[n("Button",{attrs:{icon:"ios-cloud-upload-outline"}},[t._v("上传文件")])],1)],1),n("FormItem",{attrs:{label:"MainClass :"}},[n("Input",{attrs:{placeholder:""},model:{value:t.jobEdit.config.mainClass,callback:function(e){t.$set(t.jobEdit.config,"mainClass",e)},expression:"jobEdit.config.mainClass"}})],1),n("FormItem",{attrs:{label:"程序参数 :"}},[n("Input",{attrs:{type:"textarea",rows:4},model:{value:t.jobEdit.config.args,callback:function(e){t.$set(t.jobEdit.config,"args",e)},expression:"jobEdit.config.args"}})],1)],1)],1),n("TabPane",{attrs:{label:"运行参数",name:"runtime"}},[n("Form",{staticStyle:{width:"80%"},attrs:{model:t.jobEdit,"label-width":100}},[n("FormItem",{attrs:{label:"作业并行度 :"}},[n("Slider",{attrs:{"show-input":""},model:{value:t.jobEdit.config.parallelism,callback:function(e){t.$set(t.jobEdit.config,"parallelism",e)},expression:"jobEdit.config.parallelism"}})],1)],1)],1),n("div",{staticStyle:{"margin-bottom":"10px",padding:"5px",background:"#f8f8f9"},attrs:{slot:"extra"},slot:"extra"},[n("Button",{staticStyle:{"margin-right":"10px"},attrs:{type:"info",size:"small"},on:{click:t.clickGoBack}},[t._v("返回")]),n("Button",{attrs:{type:"success",size:"small"},on:{click:t.clickSave}},[t._v("保存")])],1)],1)],1)},d=[],f=(n("a4d3"),n("e01a"),n("d81d"),n("b0c0"),n("d3b7"),n("ac1f"),n("25f0"),n("5319"),n("bee2")),p=n("38a0"),v=function(t){function e(){var t;return Object(a["a"])(this,e),t=Object(i["a"])(this,Object(c["a"])(e).apply(this,arguments)),t.hintJobTypeList=[{value:1,label:"自定义 / Jar"}],t.hintExecFileList=[],t.rt={jobId:""},t.jobEdit={name:"",config:{}},t.uploadJarActionUrl=p["a"].UPLOAD_JAR_URL,t}return Object(l["a"])(e,t),Object(f["a"])(e,[{key:"clickGoBack",value:function(){this.$router.go(-1)}},{key:"clickSave",value:function(){var t=this;p["a"].updateJob(this.jobEdit).then((function(e){t.$Message.success("保存配置成功")})).catch((function(e){t.$Notice.error({title:"保存配置失败",desc:e.msg})}))}},{key:"uploadOnSuccess",value:function(t,e){this.getJobJarList(),this.$Notice.success({title:"上传文件成功"})}},{key:"getJob",value:function(){var t=this;p["a"].queryJob({jobId:this.rt.jobId}).then((function(e){t.jobEdit.id=e.id,t.jobEdit.name=e.name,t.jobEdit.type=e.type,t.jobEdit.description=e.description,t.jobEdit.clientVersion=e.clientVersion,t.jobEdit.config=e.config})).catch((function(e){t.$Notice.error({title:e.msg})}))}},{key:"getJobJarList",value:function(){var t=this;p["a"].jarList({jobId:this.rt.jobId}).then((function(e){var n=e;t.hintExecFileList=n.map((function(t){return{value:t.toString(),label:t.toString()}}))}))}},{key:"parseRouter",value:function(){var t=this.$route.query.id;this.rt.jobId=t,this.uploadJarActionUrl=this.uploadJarActionUrl.replace("{jobId}",String(t))}},{key:"mounted",value:function(){this.parseRouter(),this.getJob(),this.getJobJarList()}}]),e}(u["b"]);v=s["a"]([u["a"]],v);var g=v,j=g,h=n("2877"),m=Object(h["a"])(j,b,d,!1,null,"78ff5d5d",null),x=m.exports,E=function(t){function e(){var t;return Object(a["a"])(this,e),t=Object(i["a"])(this,Object(c["a"])(e).apply(this,arguments)),t.type=1,t}return Object(l["a"])(e,t),e}(u["b"]);E=s["a"]([Object(u["a"])({components:{JobEditCustom:x}})],E);var y=E,I=y,J=Object(h["a"])(I,r,o,!1,null,"7eada1a0",null);e["default"]=J.exports},5319:function(t,e,n){"use strict";var r=n("d784"),o=n("825a"),a=n("7b0b"),i=n("50c4"),c=n("a691"),l=n("1d80"),s=n("8aa5"),u=n("14c3"),b=Math.max,d=Math.min,f=Math.floor,p=/\$([$&'`]|\d\d?|<[^>]*>)/g,v=/\$([$&'`]|\d\d?)/g,g=function(t){return void 0===t?t:String(t)};r("replace",2,(function(t,e,n,r){return[function(n,r){var o=l(this),a=void 0==n?void 0:n[t];return void 0!==a?a.call(n,o,r):e.call(String(o),n,r)},function(t,a){if(r.REPLACE_KEEPS_$0||"string"===typeof a&&-1===a.indexOf("$0")){var l=n(e,t,this,a);if(l.done)return l.value}var f=o(t),p=String(this),v="function"===typeof a;v||(a=String(a));var h=f.global;if(h){var m=f.unicode;f.lastIndex=0}var x=[];while(1){var E=u(f,p);if(null===E)break;if(x.push(E),!h)break;var y=String(E[0]);""===y&&(f.lastIndex=s(p,i(f.lastIndex),m))}for(var I="",J=0,O=0;O<x.length;O++){E=x[O];for(var S=String(E[0]),k=b(d(c(E.index),p.length),0),R=[],$=1;$<E.length;$++)R.push(g(E[$]));var _=E.groups;if(v){var w=[S].concat(R,k,p);void 0!==_&&w.push(_);var L=String(a.apply(void 0,w))}else L=j(S,p,k,R,_,a);k>=J&&(I+=p.slice(J,k)+L,J=k+S.length)}return I+p.slice(J)}];function j(t,n,r,o,i,c){var l=r+t.length,s=o.length,u=v;return void 0!==i&&(i=a(i),u=p),e.call(c,u,(function(e,a){var c;switch(a.charAt(0)){case"$":return"$";case"&":return t;case"`":return n.slice(0,r);case"'":return n.slice(l);case"<":c=i[a.slice(1,-1)];break;default:var u=+a;if(0===u)return e;if(u>s){var b=f(u/10);return 0===b?e:b<=s?void 0===o[b-1]?a.charAt(1):o[b-1]+a.charAt(1):e}c=o[u-1]}return void 0===c?"":c}))}}))},"8aa5":function(t,e,n){"use strict";var r=n("6547").charAt;t.exports=function(t,e,n){return e+(n?r(t,e).length:1)}},9263:function(t,e,n){"use strict";var r=n("ad6d"),o=n("9f7f"),a=RegExp.prototype.exec,i=String.prototype.replace,c=a,l=function(){var t=/a/,e=/b*/g;return a.call(t,"a"),a.call(e,"a"),0!==t.lastIndex||0!==e.lastIndex}(),s=o.UNSUPPORTED_Y||o.BROKEN_CARET,u=void 0!==/()??/.exec("")[1],b=l||u||s;b&&(c=function(t){var e,n,o,c,b=this,d=s&&b.sticky,f=r.call(b),p=b.source,v=0,g=t;return d&&(f=f.replace("y",""),-1===f.indexOf("g")&&(f+="g"),g=String(t).slice(b.lastIndex),b.lastIndex>0&&(!b.multiline||b.multiline&&"\n"!==t[b.lastIndex-1])&&(p="(?: "+p+")",g=" "+g,v++),n=new RegExp("^(?:"+p+")",f)),u&&(n=new RegExp("^"+p+"$(?!\\s)",f)),l&&(e=b.lastIndex),o=a.call(d?n:b,g),d?o?(o.input=o.input.slice(v),o[0]=o[0].slice(v),o.index=b.lastIndex,b.lastIndex+=o[0].length):b.lastIndex=0:l&&o&&(b.lastIndex=b.global?o.index+o[0].length:e),u&&o&&o.length>1&&i.call(o[0],n,(function(){for(c=1;c<arguments.length-2;c++)void 0===arguments[c]&&(o[c]=void 0)})),o}),t.exports=c},"9f7f":function(t,e,n){"use strict";var r=n("d039");function o(t,e){return RegExp(t,e)}e.UNSUPPORTED_Y=r((function(){var t=o("a","y");return t.lastIndex=2,null!=t.exec("abcd")})),e.BROKEN_CARET=r((function(){var t=o("^r","gy");return t.lastIndex=2,null!=t.exec("str")}))},ac1f:function(t,e,n){"use strict";var r=n("23e7"),o=n("9263");r({target:"RegExp",proto:!0,forced:/./.exec!==o},{exec:o})},ad6d:function(t,e,n){"use strict";var r=n("825a");t.exports=function(){var t=r(this),e="";return t.global&&(e+="g"),t.ignoreCase&&(e+="i"),t.multiline&&(e+="m"),t.dotAll&&(e+="s"),t.unicode&&(e+="u"),t.sticky&&(e+="y"),e}},ae40:function(t,e,n){var r=n("83ab"),o=n("d039"),a=n("5135"),i=Object.defineProperty,c=function(t){throw t};t.exports=function(t,e){e||(e={});var n=[][t],l=!!a(e,"ACCESSORS")&&e.ACCESSORS,s=a(e,0)?e[0]:c,u=a(e,1)?e[1]:void 0;return!!n&&!o((function(){if(l&&!r)return!0;var t={length:-1},e=function(e){l?i(t,e,{enumerable:!0,get:c}):t[e]=1};e(1),e(2147483646),e(4294967294),n.call(t,s,u)}))}},b0c0:function(t,e,n){var r=n("83ab"),o=n("9bf2").f,a=Function.prototype,i=a.toString,c=/^\s*function ([^ (]*)/,l="name";!r||l in a||o(a,l,{configurable:!0,get:function(){try{return i.call(this).match(c)[1]}catch(t){return""}}})},d784:function(t,e,n){"use strict";n("ac1f");var r=n("6eeb"),o=n("d039"),a=n("b622"),i=n("9263"),c=n("9112"),l=a("species"),s=!o((function(){var t=/./;return t.exec=function(){var t=[];return t.groups={a:"7"},t},"7"!=="".replace(t,"$<a>")})),u=function(){return"$0"==="a".replace(/./,"$0")}(),b=!o((function(){var t=/(?:)/,e=t.exec;t.exec=function(){return e.apply(this,arguments)};var n="ab".split(t);return 2!==n.length||"a"!==n[0]||"b"!==n[1]}));t.exports=function(t,e,n,d){var f=a(t),p=!o((function(){var e={};return e[f]=function(){return 7},7!=""[t](e)})),v=p&&!o((function(){var e=!1,n=/a/;return"split"===t&&(n={},n.constructor={},n.constructor[l]=function(){return n},n.flags="",n[f]=/./[f]),n.exec=function(){return e=!0,null},n[f](""),!e}));if(!p||!v||"replace"===t&&(!s||!u)||"split"===t&&!b){var g=/./[f],j=n(f,""[t],(function(t,e,n,r,o){return e.exec===i?p&&!o?{done:!0,value:g.call(e,n,r)}:{done:!0,value:t.call(n,e,r)}:{done:!1}}),{REPLACE_KEEPS_$0:u}),h=j[0],m=j[1];r(String.prototype,t,h),r(RegExp.prototype,f,2==e?function(t,e){return m.call(t,this,e)}:function(t){return m.call(t,this)})}d&&c(RegExp.prototype[f],"sham",!0)}},d81d:function(t,e,n){"use strict";var r=n("23e7"),o=n("b727").map,a=n("1dde"),i=n("ae40"),c=a("map"),l=i("map");r({target:"Array",proto:!0,forced:!c||!l},{map:function(t){return o(this,t,arguments.length>1?arguments[1]:void 0)}})}}]);
//# sourceMappingURL=chunk-0bb87d21.fc218e4e.js.map