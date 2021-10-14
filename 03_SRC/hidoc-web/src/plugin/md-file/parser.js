'use strict';

var _interopRequireDefault = require('@babel/runtime/helpers/interopRequireDefault');

exports.__esModule = true;
exports.default = _default;

var _markdownItContainer = _interopRequireDefault(require("markdown-it-emoji/light"));

function _default(vMdParser, options) {
    vMdParser.extendMarkdown(function(mdParser) {
        // extend markdown-it
        // mdParser.use(_markdownItContainer);

        // debugger;
        // if (options.customEmoji) {
        //     mdParser.renderer.rules.emoji = function(token, idx) {
        //         return '<span class="v-md-emoji emoji-' + token[idx].markup + '"></span>';
        //     };
        // }
    });
}
