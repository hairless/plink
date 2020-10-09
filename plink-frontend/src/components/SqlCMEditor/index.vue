<template>
  <codemirror ref="cms" placeholder="" :value="sql" :options="cmOption" @input="onInput" @inputRead="onInputRead" @ready="onReady" @scroll="onScroll" @changes="onChanges" />
</template>

<script>
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/sql-hint.js";
import "codemirror/addon/selection/active-line.js";
import "codemirror/addon/selection/mark-selection.js";
import "codemirror/addon/edit/matchbrackets.js";
import "codemirror/addon/edit/closebrackets.js";
import "codemirror/addon/display/placeholder.js";
import "codemirror/addon/display/fullscreen.js";
import "codemirror/addon/fold/foldgutter.js";
import "codemirror/addon/fold/foldcode.js";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/src/util/dom.js";

import "codemirror/lib/codemirror.css";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/theme/material.css";
import "codemirror/addon/display/fullscreen.css";
import "codemirror/addon/fold/foldgutter.css";

import "codemirror/theme/dracula.css";
import "codemirror/theme/eclipse.css";
import "codemirror/theme/idea.css";

import "./modules/sql.js";
import sqlFormatter from "./modules/sqlFormatter.js";
import CodeMirror from "codemirror/lib/codemirror";

export default {
  name: "SqlEditor",
  props: {
    sql: {
      type: String,
      default: ""
    },
    readOnly: {
      type: Boolean,
      default: false
    },
    height: {
      type: String,
      default: "auto"
    },
    foldKey: {
      type: String,
      default: "F2"
    },
    formatKey: {
      type: String,
      default: "F7"
    },
    fullScreenKey: {
      type: String,
      default: "F9"
    },
    firstLineNumber: {
      type: Number,
      default: 1
    },
    theme: {
      type: String,
      default: "default"
    },
    autoScrollToBottomOnChanges: {
      type: Boolean,
      default: false
    }
  },
  model: {
    prop: "sql",
    event: "input"
  },
  data() {
    return {
      cm: null,
      cmOption: {
        theme: this.theme,
        tabSize: 4,
        lineNumbers: true,
        //autoRefresh: true,
        //滚动
        lineWrapping: true,
        cursorHeight: 0.85,
        //只读
        readOnly: this.readOnly,
        styleSelectedText: true,
        mode: "text/stream-sql",
        //选择时出现光标
        showCursorWhenSelecting: true,
        //自动提示
        hintOptions: {
          completeSingle: false
        },
        //高亮选择行
        styleActiveLine: true,
        //高亮匹配括号
        matchBrackets: true,
        //自动追加尾括号
        autoCloseBrackets: true,
        firstLineNumber: this.firstLineNumber,
        extraKeys: {
          //折叠代码
          /*"F7": this.foldCode,
                            //格式化
                            "F9": this.formatSql,
                            //全屏
                            "F10": function (cm) {
                                cm.setOption("fullScreen", !cm.getOption("fullScreen"));
                            },*/
          Esc: function(cm) {
            cm.setOption("fullScreen", false);
          }
        },
        foldGutter: {
          rangeFinder: this.rangeFinder
        },
        gutters: ["ErrorMarker", "CodeMirror-linenumbers", "CodeMirror-foldgutter"]
      },
      fold: true,
      lastLine: null
    };
  },
  methods: {
    rangeFinder(cm, start) {
      var line = start.line,
        lineText = cm.getLine(line);
      var tokenType;

      function findOpening(openCh) {
        for (var at = start.ch, pass = 0; ; ) {
          var found = at <= 0 ? -1 : lineText.lastIndexOf(openCh, at - 1);
          if (found == -1) {
            if (pass == 1) break;
            pass = 1;
            at = lineText.length;
            continue;
          }
          if (pass == 1 && found < start.ch) break;
          tokenType = cm.getTokenTypeAt(CodeMirror.Pos(line, found + 1));
          if (!/^(comment|string)/.test(tokenType)) {
            //return found + openCh.length;
            if ("(" === lineText[lineText.length - 1]) {
              return lineText.length - 1;
            }
            return lineText.length;
          }
          at = found - 1;
        }
      }

      var startToken = "create",
        endToken = ";",
        startCh = findOpening("create");
      if (startCh == null) {
        (startToken = "into"), (endToken = "from");
        startCh = findOpening(startToken, true);
      }

      if (startCh == null) {
        (startToken = "from"), (endToken = ";");
        startCh = findOpening(startToken);
      }

      if (startCh == null) return;
      var count = 1,
        lastLine = cm.lastLine(),
        end,
        endCh;
      outer: for (var i = line; i <= lastLine; ++i) {
        // debugger
        var text = cm.getLine(i),
          pos = i == line ? startCh : 0;
        for (;;) {
          var nextOpen = text.indexOf(startToken, pos),
            nextClose = text.indexOf(endToken, pos);
          if (nextOpen < 0) nextOpen = text.length;
          if (nextClose < 0) nextClose = text.length;
          pos = Math.min(nextOpen, nextClose);
          if (pos == text.length) break;
          if (pos == nextOpen) ++count;
          else if (!--count) {
            end = i;
            endCh = pos;
            break outer;
          }
          ++pos;
        }
      }
      if (end == null || (line == end && endCh == startCh)) return;
      return {
        from: CodeMirror.Pos(line, startCh),
        to: CodeMirror.Pos(end, endCh)
      };
    },
    onInput(value) {
      this.$emit("input", value);
      if (this.lastLine !== this.cm.lastLine()) {
        this.$emit("lineChange", this.cm.lastLine());
        this.lastLine = this.cm.lastLine();
      }
    },
    onInputRead(cm, read) {
      if (/[a-zA-Z]/i.test(read.text[0])) {
        cm.showHint();
      }
    },
    onReady(cm) {
      this.cm = cm;
      this.cm.setSize(null, this.height);
    },
    // eslint-disable-next-line no-unused-vars
    onScroll(cm) {},
    // eslint-disable-next-line no-unused-vars
    onChanges(cm) {
      if (this.autoScrollToBottomOnChanges) {
        this.scrollToBottomOnChanges();
      }
    },
    initKeys(e) {
      if (this.foldKey === e.key) {
        this.foldCode();
      } else if (this.formatKey === e.key) {
        this.formatSql();
      } else if (this.fullScreenKey === e.key) {
        this.fullScreen();
      }
    },
    fullScreen() {
      this.cm.setOption("fullScreen", !this.cm.getOption("fullScreen"));
      if (!this.cm.state.focused) {
        this.cm.display.input.focus();
      }
    },
    formatSql() {
      this.onInput(sqlFormatter.format(this.sql));
      if (!this.cm.state.focused) {
        this.cm.display.input.focus();
      }
    },
    foldCode() {
      let type = this.fold ? "fold" : "unfold";
      let first = this.cm.doc.first;
      let last = first + this.cm.doc.size - 1;
      for (let i = first; i <= last; i++) {
        this.cm.foldCode(i, { rangeFinder: this.rangeFinder }, type);
      }
      this.fold = !this.fold;
      if (!this.cm.state.focused) {
        this.cm.display.input.focus();
      }
    },
    markText(line, ch) {
      var startMark = CodeMirror.Pos(line, ch - 2 > 0 ? ch - 2 : 0);
      var endMark = CodeMirror.Pos(line, ch + 2);
      this.cm.markText(startMark, endMark, { className: "ErrorHighlight" });
      let elem = document.createElement("span");
      elem.innerHTML = "×";
      elem.className = "ErrorMarker";
      this.cm.setGutterMarker(line, "ErrorMarker", elem);
    },
    clearMarker() {
      this.cm.clearGutter("ErrorMarker");
      for (let marker of this.cm.getAllMarks()) {
        marker.clear();
      }
    },
    scrollToBottomOnChanges() {
      this.$nextTick(() => {
        this.cm.setCursor(this.cm.lastLine());
      });
    }
  },
  watch: {
    // eslint-disable-next-line no-unused-vars
    firstLineNumber: function(newVal, oldVal) {
      this.cm.setOption("firstLineNumber", newVal + 2);
    },
    // eslint-disable-next-line no-unused-vars
    height: function(newVal, oldVal) {
      this.cm.setSize(null, newVal);
    }
  },
  mounted() {
    //document.onkeydown = this.initKeys;
  }
};
</script>
<style>
.CodeMirror pre {
  /*font-size: large !important*/
}

.ErrorHighlight {
  display: inline-block;
  /*border: 1px solid blue*/
  background-color: #fcff0b;
  padding: 0 3px;
}

.ErrorMarker {
  font-weight: bold;
  font-size: 15px;
  color: red;
}
</style>
