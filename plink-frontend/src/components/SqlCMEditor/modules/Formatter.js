"use strict";

exports.__esModule = true;

var _trimEnd = require("lodash/trimEnd");

var _trimEnd2 = _interopRequireDefault(_trimEnd);

var _tokenTypes = require("sql-formatter/lib/core/tokenTypes");

var _tokenTypes2 = _interopRequireDefault(_tokenTypes);

var _Indentation = require("sql-formatter/lib/core/Indentation");

var _Indentation2 = _interopRequireDefault(_Indentation);

var _InlineBlock = require("sql-formatter/lib/core/InlineBlock");

var _InlineBlock2 = _interopRequireDefault(_InlineBlock);

var _Params = require("sql-formatter/lib/core/Params");

var _Params2 = _interopRequireDefault(_Params);

function _interopRequireDefault(obj) {
  return obj && obj.__esModule ? obj : { default: obj };
}

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

var Formatter = (function() {
  /**
   * @param {Object} cfg
   *   @param {Object} cfg.indent
   *   @param {Object} cfg.params
   * @param {Tokenizer} tokenizer
   */
  function Formatter(cfg, tokenizer) {
    _classCallCheck(this, Formatter);

    this.cfg = cfg || {};
    this.indentation = new _Indentation2["default"](this.cfg.indent);
    this.inlineBlock = new _InlineBlock2["default"]();
    this.params = new _Params2["default"](this.cfg.params);
    this.tokenizer = tokenizer;
    this.previousReservedWord = {};
    this.tokens = [];
    this.index = 0;
  }

  /**
   * Formats whitespaces in a SQL string to make it easier to read.
   *
   * @param {String} query The SQL query string
   * @return {String} formatted query
   */

  Formatter.prototype.format = function format(query) {
    this.tokens = this.tokenizer.tokenize(query);
    var formattedQuery = this.getFormattedQueryFromTokens();

    return formattedQuery.trim();
  };

  Formatter.prototype.getFormattedQueryFromTokens = function getFormattedQueryFromTokens() {
    var _this = this;

    var formattedQuery = "";

    this.tokens.forEach(function(token, index) {
      _this.index = index;

      if (token.type === _tokenTypes2["default"].WHITESPACE) {
        // ignore (we do our own whitespace formatting)
      } else if (token.type === _tokenTypes2["default"].LINE_COMMENT) {
        formattedQuery = _this.formatLineComment(token, formattedQuery);
      } else if (token.type === _tokenTypes2["default"].BLOCK_COMMENT) {
        formattedQuery = _this.formatBlockComment(token, formattedQuery);
      } else if (token.type === _tokenTypes2["default"].RESERVED_TOPLEVEL) {
        formattedQuery = _this.formatToplevelReservedWord(token, formattedQuery);
        _this.previousReservedWord = token;
      } else if (token.type === _tokenTypes2["default"].RESERVED_NEWLINE) {
        formattedQuery = _this.formatNewlineReservedWord(token, formattedQuery);
        _this.previousReservedWord = token;
      } else if (token.type === _tokenTypes2["default"].RESERVED) {
        formattedQuery = _this.formatWithSpaces(token, formattedQuery);
        _this.previousReservedWord = token;
      } else if (token.type === _tokenTypes2["default"].OPEN_PAREN) {
        formattedQuery = _this.formatOpeningParentheses(token, formattedQuery);
      } else if (token.type === _tokenTypes2["default"].CLOSE_PAREN) {
        formattedQuery = _this.formatClosingParentheses(token, formattedQuery);
      } else if (token.type === _tokenTypes2["default"].PLACEHOLDER) {
        formattedQuery = _this.formatPlaceholder(token, formattedQuery);
      } else if (token.value === ",") {
        formattedQuery = _this.formatComma(token, formattedQuery);
      } else if (token.value === ":") {
        formattedQuery = _this.formatWithSpaceAfter(token, formattedQuery);
      } else if (token.value === ".") {
        formattedQuery = _this.formatWithoutSpaces(token, formattedQuery);
      } else if (token.value === ";") {
        formattedQuery = _this.addNewline(_this.formatWithoutSpaces(token, formattedQuery)) + "\n";
      } else {
        formattedQuery = _this.formatWithSpaces(token, formattedQuery);
      }
    });
    return formattedQuery;
  };

  Formatter.prototype.formatLineComment = function formatLineComment(token, query) {
    if (this.previousNonWhitespaceToken() && this.previousNonWhitespaceToken().value === ",") {
      query = query.replace(/\s+$/, "") + this.indentation.getIndent();
    }
    return this.addNewline(query + token.value);
  };

  Formatter.prototype.formatBlockComment = function formatBlockComment(token, query) {
    return this.addNewline(this.addNewline(query) + this.indentComment(token.value));
  };

  Formatter.prototype.indentComment = function indentComment(comment) {
    return comment.replace(/\n/g, "\n" + this.indentation.getIndent());
  };

  Formatter.prototype.formatToplevelReservedWord = function formatToplevelReservedWord(token, query) {
    this.indentation.decreaseTopLevel();

    query = this.addNewline(query);

    this.indentation.increaseToplevel();

    query += this.equalizeWhitespace(token.value);

    if (token.value !== "from" && token.value !== "FROM") {
      query = this.addNewline(query);
    } else {
      query += " ";
    }

    return query;
  };

  Formatter.prototype.formatNewlineReservedWord = function formatNewlineReservedWord(token, query) {
    return this.addNewline(query) + this.equalizeWhitespace(token.value) + " ";
  };

  // Replace any sequence of whitespace characters with single space

  Formatter.prototype.equalizeWhitespace = function equalizeWhitespace(string) {
    return string.replace(/\s+/g, " ");
  };

  // Opening parentheses increase the block indent level and start a new line

  Formatter.prototype.formatOpeningParentheses = function formatOpeningParentheses(token, query) {
    // Take out the preceding space unless there was whitespace there in the original query
    // or another opening parens or line comment
    var preserveWhitespaceFor = [_tokenTypes2["default"].WHITESPACE, _tokenTypes2["default"].OPEN_PAREN, _tokenTypes2["default"].LINE_COMMENT];
    if (!preserveWhitespaceFor.includes(this.previousToken().type)) {
      query = (0, _trimEnd2["default"])(query);
    }
    query += token.value;

    this.inlineBlock.beginIfPossible(this.tokens, this.index);

    if (!this.inlineBlock.isActive()) {
      this.indentation.increaseBlockLevel();
      query = this.addNewline(query);
    }
    return query;
  };

  // Closing parentheses decrease the block indent level

  Formatter.prototype.formatClosingParentheses = function formatClosingParentheses(token, query) {
    if (this.inlineBlock.isActive()) {
      this.inlineBlock.end();
      return this.formatWithSpaceAfter(token, query);
    } else {
      this.indentation.decreaseBlockLevel();
      return this.formatWithSpaces(token, this.addNewline(query));
    }
  };

  Formatter.prototype.formatPlaceholder = function formatPlaceholder(token, query) {
    return query + this.params.get(token) + " ";
  };

  // Commas start a new line (unless within inline parentheses or SQL "LIMIT" clause)

  Formatter.prototype.formatComma = function formatComma(token, query) {
    query = this.trimTrailingWhitespace(query) + token.value + " ";
    if (this.inlineBlock.isActive()) {
      return query;
    } else if (/^LIMIT$/i.test(this.previousReservedWord.value)) {
      return query;
    } else {
      return this.addNewline(query);
    }
  };

  Formatter.prototype.formatWithSpaceAfter = function formatWithSpaceAfter(token, query) {
    return this.trimTrailingWhitespace(query) + token.value + " ";
  };

  Formatter.prototype.formatWithoutSpaces = function formatWithoutSpaces(token, query) {
    return this.trimTrailingWhitespace(query) + token.value;
  };

  Formatter.prototype.formatWithSpaces = function formatWithSpaces(token, query) {
    return query + token.value + " ";
  };

  Formatter.prototype.addNewline = function addNewline(query) {
    return (0, _trimEnd2["default"])(query) + "\n" + this.indentation.getIndent();
  };

  Formatter.prototype.trimTrailingWhitespace = function trimTrailingWhitespace(query) {
    if (this.previousNonWhitespaceToken().type === _tokenTypes2["default"].LINE_COMMENT) {
      return (0, _trimEnd2["default"])(query) + "\n";
    } else {
      return (0, _trimEnd2["default"])(query);
    }
  };

  Formatter.prototype.previousNonWhitespaceToken = function previousNonWhitespaceToken() {
    var n = 1;
    while (this.previousToken(n).type === _tokenTypes2["default"].WHITESPACE) {
      n++;
    }
    return this.previousToken(n);
  };

  Formatter.prototype.previousToken = function previousToken() {
    var offset = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : 1;

    return this.tokens[this.index - offset] || {};
  };

  return Formatter;
})();

exports["default"] = Formatter;
module.exports = exports["default"];
